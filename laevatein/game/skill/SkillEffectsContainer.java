package laevatein.game.skill;

import java.sql.*;
import java.util.*;
import java.util.concurrent.*;

import laevatein.server.*;
import laevatein.server.database.*;
import laevatein.server.process_server.*;
import laevatein.game.model.player.*;

/*
 * 所有技能共用的計時器
 * 新增技能效果要新增在HashMap Effect裡面
 */
public class SkillEffectsContainer extends TimerTask implements Runnable
{
	//private final Timer timer = new Timer ("SkillEffectTimer") ;
	private PcInstance pc;
	private SessionHandler handle;
	private ConcurrentHashMap<Integer, SkillEffect> buffs = null;
	
	public SkillEffectsContainer (PcInstance pc) {
		buffs = new ConcurrentHashMap<Integer, SkillEffect> () ;
		this.pc = pc;
		handle = pc.getHandle ();
	}
	
	public void run () {//每一秒定時檢查技能存在時間
		if (!buffs.isEmpty ()) {
			buffs.forEach ((Integer skillId, SkillEffect buff)->{
				if (buff.remainTime == 0xFFFF) {
					return;
				} else if (buff.remainTime > 0) {
					buff.remainTime--;
				} else {
					//Stop buff
					removeSkill (skillId);
					buffs.remove (skillId);
				}
			});
		}
	}
	
	public void updateSkillEffects () {
		buffs.forEach ((Integer skillId, SkillEffect effects)->{
			if (skillId == SkillId.STATUS_HASTE) {
				//byte[] data = new SkillHaste (pc.uuid, pc.moveSpeed, effects.remainTime).getRaw () ;
				//handle.sendPacket (data);
				
			} else if (skillId == SkillId.STATUS_BRAVE) {
				//byte[] data = new SkillBrave (pc.uuid, pc.braveSpeed, effects.remainTime).getRaw () ;
				//handle.sendPacket (data);
			} else {
				//其他持續性技能
			}
		}) ;
	}
	
	//登入&換地圖時重新載入角色技能效果資訊
	public void loadBuffs () {		
		buffs.forEach ((Integer skillId, SkillEffect effect)->{
			removeSkill (skillId);
		});
		buffs.clear ();
		
		ResultSet rs = null;
		try {
			rs = DatabaseCmds.loadSkillEffects (pc.getUuid ());
			while (rs.next ()) {
				int skillId = rs.getInt ("skill_id");
				int remainTime = rs.getInt ("remaining_time");
				int polyGfx = rs.getInt ("poly_id");
				SkillEffect effect = new SkillEffect (skillId, remainTime, polyGfx);
				
				buffs.put (skillId, effect);
				
				//重發更新技能封包
				skillPacket (skillId);
			}
		} catch (Exception e) {
			e.printStackTrace ();
			
		} finally {
			DatabaseUtil.close (rs);
		}
	}
	
	public void saveBuffs () {
		//清空全部紀錄
		DatabaseCmds.deleteSkillEffects (pc.getUuid ());
		
		//塞新的進去
		buffs.forEach ((Integer skillId, SkillEffect effect)->{
			DatabaseCmds.insertSkillEffect (pc.getUuid (), skillId, effect.remainTime, effect.polyGfx);
		});
	}
	
	private void skillPacket (int skillId) {
		switch (skillId) {
		case SkillId.SHIELD:
			pc.skillParameters.setAc (pc.skillParameters.getAc () - 1);
			//handle.sendPacket (new UpdateAc (pc).getRaw ());
			//handle.sendPacket (new SkillShield (effects.get (skillId).remainTime, 0).getRaw ());
			break;
		
		case SkillId.STATUS_HASTE:
			//handle.sendPacket (new SkillHaste (pc.uuid, 1, effects.get(skillId).remainTime).getRaw());
			break;
			
		case SkillId.STATUS_BRAVE:
			//handle.sendPacket (new SkillBrave (pc.uuid, 1, effects.get(skillId).remainTime).getRaw());
			//pc.setBrave ();
			break;
		
		default:
			break;
		}
	}
	
	public boolean hasSkillEffect (int skillId) {
		return buffs.containsKey (skillId);
	}
	
	public void addSkill (int skillId, int remainTime, int polyId) { //skillid, remain time poly
		SkillEffect effect = new SkillEffect (skillId, remainTime, polyId);
		buffs.put (skillId, effect);
		skillPacket (skillId);
	}
	
	public void removeSkill (int skillId) {
		switch (skillId) {
		case SkillId.SHIELD: //保護罩
			pc.skillParameters.setAc (pc.skillParameters.getAc () + 1);
			//handle.sendPacket (new UpdateAc (pc).getRaw ());
			//handle.sendPacket (new SkillShield (0, 0).getRaw ());
			break;
		
		case SkillId.STATUS_HASTE: //加速
			pc.moveSpeed = 0;
			//handle.sendPacket (new SkillHaste (pc.uuid, 0, 0).getRaw ());
			break;
			
		case SkillId.STATUS_BRAVE: //勇敢藥水
			//pc.unsetBrave ();
			//handle.sendPacket (new SkillBrave (pc.uuid, 0, 0).getRaw ());
			pc.status &= 0xEF;
			break;
		
		default:
			break;
		}
	}
	
	public ConcurrentHashMap<Integer, SkillEffect> getEffects () {
		return buffs;
	}
	
	public void start () {
		//timer.scheduleAtFixedRate (this, 0, 1000); //1S interval
	}
	
	public void stop () {
		//timer.cancel ();
	}
}
