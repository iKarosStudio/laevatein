package laevatein.game.routine_task;

import java.util.*;
import java.util.concurrent.*;

import laevatein.config.*;
import laevatein.server.*;
import laevatein.server.threadpool.*;
import laevatein.server.process_server.*;
import laevatein.game.model.*;
import laevatein.game.model.player.*;

public class SightUpdate implements Runnable
{
	ScheduledFuture<?> schedulor;
	private PcInstance pc;
	private SessionHandler handle;

	public SightUpdate (PcInstance pc) {
		this.pc = pc;
		handle = pc.getHandle ();
	}
	
	public void run () {
		/* 更新視界各類物件 */
		updatePcs ();
		updateModels ();
	}
	
	private void updatePcs () {
		//
		// 加入/移出不在清單內卻在視距內的物件
		// 注意:不會自己將自身視為端點
		//
		List<PcInstance> pcs = pc.map.getPcsInsight (pc.loc.p);
		pcs.forEach ((PcInstance eachPc)->{
			if (!pc.pcsInsight.containsKey (eachPc.uuid) && (eachPc.uuid != pc.uuid)  && !(eachPc.isInvisible ())) {
				pc.pcsInsight.putIfAbsent (eachPc.uuid, eachPc);
				handle.sendPacket (eachPc.getPacket ());
			}
		});
		
		//
		//玩家必須額外注意是否已經離線, 檢查socket close or thread is Alive
		//
		pc.pcsInsight.forEachValue (Configurations.PARALLELISM_THRESHOLD, (PcInstance p)->{
			if (!pc.isInsight (p.loc) || !pcs.contains (p) || pc.isInvisible ()) {
				pc.pcsInsight.remove (p.uuid);
				handle.sendPacket (new RemoveModel (p.uuid).getRaw ());
			}
		});
	}
	
	private void updateModels () {
		List<Objeto> models = pc.map.getModelsInsight (pc.loc.p);
		models.forEach ((Objeto m)->{
			if (!pc.modelsInsight.containsKey (m.uuid)) {
				pc.modelsInsight.put (m.uuid, m);
				handle.sendPacket (m.getPacket ());
			}
		});
		
		pc.modelsInsight.forEachValue (Configurations.PARALLELISM_THRESHOLD, (Objeto m)->{
			if (!pc.isInsight (m.loc) || !models.contains (m)) {
				pc.modelsInsight.remove (m.uuid);
				handle.sendPacket (new RemoveModel (m.uuid).getRaw ());
			} else {
				if (m instanceof AiControllable) {
					((AiControllable) m).toggleAi ();
				}
			}
		});
	}
	
	public void start () {
		schedulor = KernelThreadPool.getInstance ().ScheduleAtFixedRate (this, 200, 500);
	}
	
	public void stop () {
		schedulor.cancel (true);
	}
}
