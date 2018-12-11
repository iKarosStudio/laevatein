package laevatein.game.routine_task;

import java.util.concurrent.ScheduledFuture;

import laevatein.server.*;
import laevatein.server.threadpool.*;
import laevatein.server.process_server.*;
import laevatein.game.model.player.*;
import laevatein.game.skill.*;

//LS(Low Speed) task 1s interval
//hp, mp resume
public class LsTask implements Runnable
{
	ScheduledFuture<?> schedulor;
	PcInstance pc;
	SessionHandler handle;
	
	private int resumeTimer = 0;
	
	public LsTask (PcInstance p) {
		pc = p;
		handle = pc.getHandle ();
	}
	
	public void run () {//1s interval
		pc.updateBuffTime ();
		
		if ((pc.weightScale30 & 0x10) > 0) {
			//負重過半
		} else {
			resumeTrigger ();
		}
		
		if (pc.battleCounter > 0) {
			pc.battleCounter --;
		}
		
		if (pc.moveCounter > 0) {
			pc.moveCounter --;
		}
	}
	
	private void resumeTrigger () {
		int timeThreshold = 0;
		if (pc.satiation < 29) {
			return; //沒飽食度不回復
		}
		
		if (pc.weightScale30 > 15) {
			return; //負重過半不回復
		}
		
		resumeTimer++;
		if (pc.battleCounter > 0) {
			timeThreshold = 0x40; //64s
		} else if (pc.moveCounter > 0) {
			timeThreshold = 0x20; //32s
		} else {
			timeThreshold = 0x10; //16s
		}
		
		if ((resumeTimer & timeThreshold) > 0) {
			hpResume ();
			mpResume ();
			resumeTimer ^= resumeTimer; //clear
		}
	}
	
	private void hpResume () {
		int hpr = pc.getHpR ();
		int maxHp = pc.getMaxHp ();
		
		if ((pc.hp + hpr) > maxHp) {
			pc.hp = maxHp;
		} else {
			pc.hp += hpr;
		}
		
		handle.sendPacket (new UpdateHp (pc.hp, maxHp).getRaw ());
	}
	
	private void mpResume () {
		int mpr = pc.getMpR ();
		int maxMp = pc.getMaxMp ();
		
		
		if (pc.hasSkillEffect (SkillId.STATUS_BLUE_POTION)) {
			int wis = pc.getWis ();
			if (wis < 12) {
				mpr += 1;
			} else {
				mpr += (wis - 10);
			}
		}
		
		if (pc.hasSkillEffect (SkillId.STATUS_WISDOM_POTION)) {
			mpr += 2;
		}
		
		if ((pc.mp + mpr) > maxMp) {
			pc.mp = maxMp;
		} else {
			pc.mp += mpr;
		}
		
		handle.sendPacket (new UpdateMp (pc.mp, maxMp).getPacket ());
	}
	
	public void start () {
		schedulor = KernelThreadPool.getInstance ().ScheduleAtFixedRate (this, 200, 1000);
	}
	
	public void stop () {
		schedulor.cancel (true);
	}
}
