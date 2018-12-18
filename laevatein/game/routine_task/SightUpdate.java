package laevatein.game.routine_task;

import java.util.*;
import java.util.concurrent.*;

import laevatein.config.*;
import laevatein.server.*;
import laevatein.server.threadpool.*;
import laevatein.server.process_server.*;
import laevatein.game.model.*;
import laevatein.game.model.player.*;
import laevatein.game.skill.*;

/*
 * 這個class他媽的就只用來做更新視線物件
 * 別他媽的給我搞其他事 注意
 */
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
		//當下視線範圍內所有物件
		List<Objeto> objsInRange = pc.getMap ().getObjsInsight (pc.getLocation ().x, pc.getLocation ().y);
		
		//移出不在視線內物件
		pc.objectsInsight.forEach ((Integer uuid, Objeto obj)->{
			//if (!objsInRange.contains (obj)) {
			if (obj.getDistanceTo (pc.getLocation ().x, pc.getLocation ().y) > Configurations.SIGHT_RAGNE) {
				pc.objectsInsight.remove (uuid);
				handle.sendPacket (new RemoveModel (uuid).getPacket ());
				//System.out.printf ("0x%08X %s 離開視線範圍\n", uuid, obj.getName ());
			}
		});
		
		pc.pcsInsight.forEach ((Integer uuid, PcInstance obj)->{
			//if (!objsInRange.contains (obj)) {
			if (obj.getDistanceTo (pc.getLocation ().x, pc.getLocation ().y) > Configurations.SIGHT_RAGNE) {
				pc.pcsInsight.remove (uuid);
				handle.sendPacket (new RemoveModel (uuid).getPacket ());
				//System.out.printf ("0x%08X %s 離開視線範圍\n", uuid, obj.getName ());
			}
		});
		
		for (Objeto obj : objsInRange) {
			if (obj.isInvisible ()) {
				continue;
			}
			
			if (obj.isPc ()) { //檢查pc物件
				//if (obj.uuid == pc.uuid) {
				if (obj.equals (pc)) {
					continue; //你自己!
				}
				
				if (!pc.pcsInsight.containsKey (obj.getUuid ()) && (obj.getUuid () != pc.getUuid ())) {
					pc.pcsInsight.put (obj.getUuid (), (PcInstance) obj);
					handle.sendPacket (obj.getPacket ());
					//System.out.printf ("0x%08X %s 進入視線範圍\n", obj.getUuid (), obj.getName ());
				}
				
				
			} else { //檢查一般地圖物件
				if (!pc.objectsInsight.containsKey (obj.getUuid ())) {
					pc.objectsInsight.put (obj.getUuid (), obj);
					handle.sendPacket (obj.getPacket ());
					//System.out.printf ("0x%08X %s 進入視線範圍\n", obj.getUuid (), obj.getName ());
					
					if (obj instanceof AiControllable) {
						((AiControllable) obj).toggleAi ();
					}
				}				
			} //End of if obj is pc
			
			if (obj.isDead) {
				handle.sendPacket (new ModelAction (ActionId.DIE, obj.getUuid (), obj.heading).getPacket ());
			}
			
			if (obj instanceof SkillAffect) {
				if (obj.isPoison () || ((SkillAffect) obj).hasSkillEffect (SkillId.STATUS_POISON)) {
					handle.sendPacket (new Poison (obj.getUuid (), Poison.COLOR_POISON).getPacket ());
				}
				
				if (((SkillAffect) obj).hasSkillEffect (SkillId.STATUS_CURSE_PARALYZING) || 
					((SkillAffect) obj).hasSkillEffect (SkillId.STATUS_CURSE_PARALYZED) ||
					((SkillAffect) obj).hasSkillEffect (SkillId.STATUS_POISON_PARALYZING) ||
					((SkillAffect) obj).hasSkillEffect (SkillId.STATUS_POISON_PARALYZED)) {
					handle.sendPacket (new Poison (obj.getUuid (), Poison.COLOR_PARALYZE).getPacket ());
				}
			} //End of skillaffect
			
		} //End of objsInRange		
	}
	
	public void start () {
		schedulor = KernelThreadPool.getInstance ().ScheduleAtFixedRate (this, 300, 300);
	}
	
	public void stop () {
		schedulor.cancel (true);
	}
}
