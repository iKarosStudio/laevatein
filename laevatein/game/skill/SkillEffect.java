package laevatein.game.skill;

import laevatein.game.model.*;
import laevatein.game.model.player.*;
import laevatein.server.process_server.*;

public class SkillEffect
{
	public int skillId;
	public int remainTime; /* Sesond */
	public int polyGfx = 0; //poly gfx of target item uid
	
	public SkillEffect (int _skillId, int _remainTime) {
		skillId = _skillId;
		remainTime = _remainTime;
	}
	
	public SkillEffect (int _skillId, int _remainTime, int _polyGfx) {
		skillId = _skillId;
		remainTime = _remainTime;
		polyGfx = _polyGfx;
	}
	
	public void setSkillEffect (PcInstance pc) {
		switch (skillId) {
		case SkillId.SHIELD:
			pc.skillParameters.ac -= 1;
			pc.updateAc ();
			//handle.sendPacket (new SkillShield (effects.get (skillId).remainTime, 0).getRaw ());
			break;
		
		case SkillId.STATUS_HASTE:
			pc.moveSpeed = 1;
			pc.getHandle ().sendPacket (new SkillHaste (pc.uuid, 1, remainTime).getPacket ());
			
			int messageId = 0;
			if (pc.hasSkillEffect (SkillId.STATUS_HASTE)) {
				messageId = 183; //你的腿得到新的能量。
			} else {
				messageId = 184; //你的動作突然變快。
			}
			
			pc.getHandle ().sendPacket (new GameMessage (messageId).getRaw ());
			break;
			
		case SkillId.STATUS_BRAVE:
			pc.status |= StatusId.STATUS_BRAVE;
			pc.getHandle ().sendPacket (new SkillBrave (pc.uuid, 1, remainTime).getPacket ());
			//if (!pc.hasSkillEffect (SkillId.STATUS_BRAVE)){
			//	pc.getHandle ().sendPacket (new GameMessage (347).getRaw ()); //從身體的深處感到熱血沸騰(客戶端自行產生)
			//}
			break;
			
		case SkillId.STATUS_BLUE_POTION:
			pc.getHandle ().sendPacket (new SkillIcon (34, remainTime).getPacket ()); //skill_icon34
			break;
			
		case SkillId.STATUS_WISDOM_POTION:
			pc.skillParameters.sp += 2;
			//pc.getHandle ().sendPacket (new SkillBrave (pc.uuid, 2, remainTime).getPacket ());
			break;
		
		case SkillId.SHAPE_CHANGE:
			break;
			
		default:
			break;
		} //end of switch
		
	}
	
	public void unsetSkillEffect (PcInstance pc) {
		switch (skillId) {
		case SkillId.SHIELD: //保護罩
			pc.skillParameters.ac += 1;
			//handle.sendPacket (new UpdateAc (pc).getRaw ());
			//handle.sendPacket (new SkillShield (0, 0).getRaw ());
			break;
		
		case SkillId.STATUS_HASTE: //加速
			pc.moveSpeed = 0;
			pc.getHandle ().sendPacket (new SkillHaste (pc.uuid, 0, 0).getPacket ());
			pc.getHandle ().sendPacket (new GameMessage (185).getRaw ()); //你感覺你自己減慢下來
			pc.boardcastPcInsight (pc.getPacket ());
			break;
			
		case SkillId.STATUS_BRAVE: //勇敢藥水
			pc.status &= ~StatusId.STATUS_BRAVE;
			pc.getHandle ().sendPacket (new SkillBrave (pc.uuid, 0, remainTime).getPacket ());
			pc.boardcastPcInsight (pc.getPacket ());
			//pc.getHandle ().sendPacket (pc.getPacket ()); //update status
			//pc.getHandle ().sendPacket (new GameMessage (349).getRaw ()); //你的情緒回復到正常 (客戶端自動訊息)
			break;
		
		case SkillId.STATUS_BLUE_POTION:
			pc.getHandle ().sendPacket (new SkillIcon (34, 0).getPacket ()); //skill_icon34
			break;
			
		case SkillId.STATUS_WISDOM_POTION:
			pc.skillParameters.sp -= 2;
			pc.getHandle ().sendPacket (new SkillBrave (pc.uuid, 2, 0).getPacket ());
			break;
		
		case SkillId.SHAPE_CHANGE:
			break;
			
		default:
			break;
		} //end of switch
	}
}
