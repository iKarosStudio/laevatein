package laevatein.game.skill;

import laevatein.game.model.*;
import laevatein.game.model.player.*;
import laevatein.server.process_server.*;

import static laevatein.game.skill.SkillId.*;

public class SkillEffect
{
	public int skillId; //
	public int remainTime; //持續時間
	public int polyGfx = 0; //變身ID
	
	public SkillEffect (int _skillId, int _remainTime) {
		skillId = _skillId;
		remainTime = _remainTime;
	}
	
	public SkillEffect (int _skillId, int _remainTime, int _polyGfx) {
		skillId = _skillId;
		remainTime = _remainTime;
		polyGfx = _polyGfx;
	}
	
	//做teleport時要重新打icon給使用者
	public void sendSkillIcon (PcInstance pc) {
		switch (skillId) {
		case SkillId.SHIELD:
			break;
		
		case SkillId.STATUS_HASTE:
			pc.sendPacket (new SkillHaste (pc.uuid, 1, remainTime).getPacket ());
			break;
			
		case SkillId.STATUS_BRAVE:
			pc.sendPacket (new SkillBrave (pc.uuid, (remainTime > 0) ? 1:0, remainTime).getPacket ());
			break;
			
		case SkillId.STATUS_BLUE_POTION:
			pc.sendPacket (new SkillIcon (34, remainTime).getPacket ());
			break;
		
		case SkillId.STATUS_WISDOM_POTION:
			//
			break;
			
		case SkillId.SHAPE_CHANGE:
			if (remainTime > 0) {
				new Polymorph (pc, polyGfx, remainTime);
			} else {
				new Polymorph (pc);
			}
			break;
		}
	}
	
	public void addSkillEffect (PcInstance pc) {
		//if (!pc.hasSkillEffect (skillId)) {
			switch (skillId) {
			case SHIELD://防護罩
				pc.skillParameters.ac -= 1;
				pc.updateAc ();
				break;
			
			case STATUS_HASTE://加速
				pc.moveSpeed = 1;
				
				int messageId = 0;
				if (pc.hasSkillEffect (SkillId.STATUS_HASTE)) {
					messageId = 183; //你的腿得到新的能量。
				} else {
					messageId = 184; //你的動作突然變快。
				}
				
				pc.getHandle ().sendPacket (new GameMessage (messageId).getPacket ());
				
				break;
				
			case STATUS_BRAVE://勇水, 精靈餅乾
				pc.status |= StatusId.STATUS_BRAVE;
				pc.boardcastPcInsight (pc.getPacket ());
				break;
			
			case STATUS_BLUE_POTION://藍色藥水
				break;
				
			case STATUS_WISDOM_POTION://慎重藥水
				pc.skillParameters.sp += 2;
				pc.updateSpMr ();
				break;
				
			case SHAPE_CHANGE://變形
				new Polymorph (pc, polyGfx, remainTime);
				break;
				
			default:
				break;
			}
		//}
		
		sendSkillIcon (pc);
	}
	
	public void removeSkillEffect (PcInstance pc) {
		//if (pc.hasSkillEffect (skillId)) {
			byte[] packet;
			
			switch (skillId) {
			case SHIELD:
				pc.skillParameters.ac += 1;
				remainTime = 0;
				break;
			
			case STATUS_HASTE:
				pc.moveSpeed = 0;
				pc.sendPacket (new GameMessage (185).getPacket ()); //你感覺你自己減慢下來
				packet = pc.getPacket ();
				pc.sendPacket (packet);
				pc.boardcastPcInsight (packet);
				break;
				
			case STATUS_BRAVE:
				pc.status &= ~StatusId.STATUS_BRAVE;
				packet = pc.getPacket ();
				pc.sendPacket (packet);
				pc.boardcastPcInsight (packet);
				break;
			
			case STATUS_BLUE_POTION:
				break;
			
			case STATUS_WISDOM_POTION:
				pc.skillParameters.sp -= 2;
				pc.updateSpMr ();
				break;
			
			case SHAPE_CHANGE:
				new Polymorph (pc);
				break;
			
			default:
				break;
			}
		//}
		
		sendSkillIcon (pc);
	}
	
}
