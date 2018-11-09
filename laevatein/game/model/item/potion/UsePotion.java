package laevatein.game.model.item.potion;

import java.util.Random;

import laevatein.server.*;
import laevatein.server.process_server.*;
import  laevatein.game.model.*;
import laevatein.game.model.player.*;
import laevatein.game.model.item.*;
import laevatein.game.skill.*;

public class UsePotion
{
	PcInstance pc;
	SessionHandler handle;
	
	public UsePotion (PcInstance _pc, ItemInstance _item) {
		pc = _pc;
		handle = pc.getHandle ();
		
		//喝水CD檢查
		if (isCoolingDown (_item)) {
			return; //還在CD
		}
		
		//藥水霜化檢查
		if (pc.hasSkillEffect (SkillId.DECAY_POTION)) {
			handle.sendPacket (new GameMessage (698).getRaw ()); //不能喝東西
			return;
		}
		
		//解除絕對屏障
		if (pc.hasSkillEffect (SkillId.ABSOLUTE_BARRIER)) {
			pc.removeSkillEffect (SkillId.ABSOLUTE_BARRIER);
		}
		
		switch (_item.id) {
		case 40010: case 40019: case 40029: //紅水類
			useHealPotion (15, _item.delayTime, 189);
			break;
			
		case 40011: case 40020: //橙水類
			useHealPotion (45, _item.delayTime, 194);
			break;
			
		case 40012: case 40021: //白水類
			useHealPotion (75, _item.delayTime, 197);
			break;
		
		case 40022: //古紅
			useHealPotion (20, _item.delayTime, 189);
			break;
			
		case 40023: //古橙
			useHealPotion (30, _item.delayTime, 194);
			break;
		
		case 40024: //古白
			useHealPotion (55, _item.delayTime, 197);
			break;
			
		case 40506: //安特水果
			useHealPotion (70, _item.delayTime, 197);
			break;
		
		case 40043: //兔肝
			break;
		
		case 40015: //藍色藥水
			break;
			
		case 40016: //慎重藥水
			break;
			
		case 40017: //解毒藥水
			break;
			
		case 40013: //綠水
		case 40030: //象牙塔綠水
			useHastePotion (300);
			break;
			
		case 40018: //強綠
			useHastePotion (1800);
			break;
			
		case 40014: //勇敢藥水
			useBravePotion (10);
			break;
		
		case 40086: //精靈餅乾
			useElfCookie (300);
			break;
			
		default:
			System.out.printf ("%s 還沒處理的藥水\n", _item.name);
			break;
		}
		
		pc.removeItem (_item.uuid, 1);
		pc.setItemDelay (_item.id, System.currentTimeMillis ());
	}
	
	public boolean isCoolingDown (ItemInstance i) {
		return !(pc.getItemDelay (i.id, System.currentTimeMillis ()) > i.delayTime);
	}
	
	public void useBravePotion (int time) {
		//C_ItemUse.java : 4006
		//TODO
		//chech type => $79
		
		//產生特效
		byte[] visualPacket = new VisualEffect (pc.uuid, 751).getRaw ();
		handle.sendPacket (visualPacket);
		pc.boardcastPcInsight (visualPacket);
		
		//套用效果
		pc.addSkillEffect (SkillId.STATUS_BRAVE, time);
		
	}
	
	public void useElfCookie (int time) {
		//TODO
	}
	
	public void useHastePotion (int time) {
		//TODO:加入緩速狀態檢查
		
		//產生特效
		byte[] visualPacket = new VisualEffect (pc.uuid, 191).getRaw ();
		handle.sendPacket (visualPacket);
		pc.boardcastPcInsight (visualPacket);
		
		//套用加速效果
		pc.addSkillEffect (SkillId.STATUS_HASTE, time);
	}
	
	public void useHealPotion (int _healHp, int _delay, int gfx) {
		Random random = new Random ();
		int maxHp = pc.getMaxHp ();
		
		//heal_hp *= ((r.nextGaussian () / 5.0) + 1.0) ;
		_healHp += _healHp * (1 + random.nextInt (20)) / 100;

		if (pc.hp + _healHp > maxHp) {
			pc.hp = maxHp;
		} else {
			pc.hp += _healHp;
		}
		
		byte[] virtualPacket = new VisualEffect (pc.uuid, gfx).getRaw ();
		
		handle.sendPacket (new GameMessage (77).getRaw ()); //覺得舒服多了
		handle.sendPacket (new UpdateHp (pc.hp, pc.getMaxHp ()).getRaw ());
		
		handle.sendPacket (virtualPacket);
		pc.boardcastPcInsight (virtualPacket);
	}
}
