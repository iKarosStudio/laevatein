package laevatein.game.model.item.potion;

import java.util.Random;

import laevatein.server.*;
import laevatein.server.process_server.*;
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
		
		byte[] visualPacket = null;
		
		switch (_item.id) {
		case 40010: case 40019: case 40029: //紅水類
			break;
			
		case 40011: case 40020: //橙水類
			break;
			
		case 40012: case 40021: //白水類
			break;
		
		case 40022: //古紅
			break;
			
		case 40023: //古橙
			break;
		
		case 40024: //古白
			break;
			
		case 40506: //安特水果
			break;
		
		case 40043: //兔肝
			break;
			
		case 40013: //綠水
		case 40030: //象牙塔綠水
			useHastePotion (300);
			break;
			
		case 40018: //強綠
			useHastePotion (1800);
			break;
			
		case 40014: //勇敢藥水
			useBravePotion (300);
			break;
		
		case 40086: //精靈餅乾
			useElfCookie (300);
			break;
			
		default:
			System.out.printf ("%s 還沒處理的藥水\n", _item.name);
			break;
		}
		
		if (_item.id == 40013 || _item.id == 40030) { //綠水, 象牙塔綠色藥水
			useHastePotion (300); //300s=5min
			
			
			pc.moveSpeed = 1;
			visualPacket = new VisualEffect (pc.uuid, 191).getRaw ();
			handle.sendPacket (visualPacket) ; //Virtual effect
			//handle.sendPacket (new SkillHaste (pc.uuid, 1, 300).getRaw ());
			//pc.addSkillEffect (SkillId.STATUS_HASTE, 300);
			
		} else if (_item.id == 40018) { //強綠
			useHastePotion (1800); //1800s=30min
			
			pc.moveSpeed = 1;
			visualPacket = new VisualEffect (pc.uuid, 191).getRaw ();
			handle.sendPacket (visualPacket) ; //Virtual effect
			//handle.sendPacket (new SkillHaste (pc.uuid, 1, 1800).getRaw ());
			//pc.addSkillEffect (SkillId.STATUS_HASTE, 1800);
			
		} else if (_item.id == 40014) { //勇敢藥水
			//pc.setBrave ();
			visualPacket = new VisualEffect (pc.uuid, 751).getRaw ();
			handle.sendPacket (visualPacket) ; //Virtual effect
			//handle.sendPacket (new SkillBrave (pc.uuid, 1, 300).getRaw ());
			//pc.addSkillEffect (SkillId.STATUS_BRAVE, 300) ;
			
		} else if (_item.id == 40068) { //精靈餅乾
			//pc.setBrave 9);
			visualPacket = new VisualEffect (pc.uuid, 751).getRaw ();
			handle.sendPacket (visualPacket) ; //Virtual effect
			//handle.sendPacket (new SkillBrave (pc.uuid, 1, 300).getRaw ());
			//pc.addSkillEffect (SkillId.STATUS_BRAVE, 300) ;
			
		} else if (_item.id == 40010 || _item.id == 40019 || _item.id == 40029) { //紅色藥水, 濃縮紅色藥水,象牙塔紅色藥水
			if (isPotionCoolDown (_item) ) {
				if (useHealPotion (15, _item.delayTime)) { //成功使用藥水
					visualPacket = new VisualEffect (pc.uuid, 189).getRaw ();
					handle.sendPacket (visualPacket) ; //Virtual effect
				}
				_item.count -= 1;
			}
		} else if (_item.id == 40011 || _item.id == 40020) { //橙色藥水, 濃縮橙色藥水
			if (isPotionCoolDown (_item) ) {
				if (useHealPotion (45, _item.delayTime)) { //成功使用藥水
					visualPacket = new VisualEffect (pc.uuid, 194).getRaw ();
					handle.sendPacket (visualPacket) ; //Virtual effect	
				}
			}
			
		} else if (_item.id == 40012 || _item.id == 40021) { //白色藥水, 濃縮白色藥水
			if (isPotionCoolDown (_item) ) {
				if (useHealPotion (75, _item.delayTime)) { //成功使用藥水
					visualPacket = new VisualEffect (pc.uuid, 197).getRaw ();
					handle.sendPacket (visualPacket) ; //Virtual effect	
				}
			}
			
		} else if (_item.id == 40022) { //古代紅色藥水
			if (isPotionCoolDown (_item) ) {
				if (useHealPotion (20, _item.delayTime)) { //成功使用藥水
					visualPacket = new VisualEffect (pc.uuid, 189).getRaw ();
					handle.sendPacket (visualPacket) ; //Virtual effect	
				}
			}
			
		} else if (_item.id == 40023) { //古代澄色藥水
			if (isPotionCoolDown (_item) ) {
				if (useHealPotion (30, _item.delayTime)) { //成功使用藥水
					visualPacket = new VisualEffect (pc.uuid, 194).getRaw ();
					handle.sendPacket (visualPacket) ; //Virtual effect	
				}
			}
			
		} else if (_item.id == 40024) { //古代白色藥水
			if (isPotionCoolDown (_item) ) {
				if (useHealPotion (55, _item.delayTime)) { //成功使用藥水
					visualPacket = new VisualEffect (pc.uuid, 197).getRaw ();
					handle.sendPacket (visualPacket) ; //Virtual effect	
				}
			}
			
		} else if (_item.id == 40506) { //安特的水果
			if (isPotionCoolDown (_item) ) {
				if (useHealPotion (70, _item.delayTime)) { //成功使用藥水
					visualPacket = new VisualEffect (pc.uuid, 197).getRaw ();
					handle.sendPacket (visualPacket) ; //Virtual effect	
				}
			}
		} else if (_item.id == 40043) { //兔肝
			//
		} else {
			System.out.printf ("%s使用不明的藥水ItemId:%d\n", pc.name, _item.id);
		}
		
		pc.boardcastPcInsight (visualPacket);
	}
	
	public boolean isPotionCoolDown (ItemInstance i) {
		boolean res = false;
		long nowTime = System.currentTimeMillis ();
		/*
		if (pc.getItemDelay (i.id, nowTime) > i.delayTime) {
			pc.setItemDelay (i.id, nowTime);
			res = true;
		} else {
			res = false;
		}
		*/
		return res;
	}
	
	public void useBravePotion (int time) {
		//C_ItemUse.java : 4006
		//TODO
	}
	
	public void useElfCookie (int time) {
		//TODO
	}
	
	public void useHastePotion (int time) {
		//TODO
	}
	
	public boolean useHealPotion (int _healHp, int _delay, int gfx) {
		Random random = new Random ();
		//檢查藥水霜化
		//解除絕對屏障
		//return false
		
		//heal_hp *= ((r.nextGaussian () / 5.0) + 1.0) ;
		_healHp += _healHp * (1 + random.nextInt (20)) / 100;
		
		//System.out.printf ("回復%d HP\n", heal_hp) ;
		if (pc.hp + _healHp > pc.getMaxHp ()) {
			pc.hp = pc.getMaxHp () ;
		} else {
			pc.hp += _healHp;
		}
		
		handle.sendPacket (new GameMessage (77).getRaw ()); //覺得舒服多了
		//handle.sendPacket (new UpdateHp (pc.hp, pc.getMaxHp ()).getRaw ());
		return true;
	}
}
