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
	private ScheduledFuture<?> schedulor;
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
			//System.out.printf ("%s see %s\n", pc.name, eachPc.name);
			
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
		List<Objeto> objects = pc.map.getModelsInsight (pc.loc.p);
		objects.forEach ((Objeto obj)->{
			if (!pc.objectsInsight.containsKey (obj.uuid)) { //還沒有快取
				pc.objectsInsight.put (obj.uuid, obj);
				handle.sendPacket (obj.getPacket ());
			}
		});
		
		pc.objectsInsight.forEachValue (Configurations.PARALLELISM_THRESHOLD, (Objeto obj)->{
			if (!pc.isInsight (obj.loc) || !objects.contains (obj)) {//需要移出視線
				pc.objectsInsight.remove (obj.uuid);
				handle.sendPacket (new RemoveModel (obj.uuid).getRaw ());
			} else { //觸發AI
				if (obj instanceof AiControllable) {
					((AiControllable) obj).toggleAi ();
				}
			}
		});
		
	}
	
	public void start () {
		schedulor = KernelThreadPool.getInstance ().ScheduleAtFixedRate (this, 200, 600);
	}
	
	public void stop () {
		schedulor.cancel (true);
	}
}
