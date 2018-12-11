package laevatein.game.model.item.scroll;

import laevatein.types.*;
import laevatein.server.*;
import laevatein.server.packet.*;
import laevatein.server.process_server.*;

import laevatein.game.model.player.*;
import laevatein.game.model.item.*;
import laevatein.game.skill.*;
import laevatein.game.template.*;
import laevatein.game.*;

import static laevatein.game.template.ItemTypeTable.*;

public class UseScroll
{
	PcInstance pc;
	SessionHandler handle;
	public UseScroll (PcInstance _pc, ItemInstance scroll, PacketReader packetReader) {
		pc = _pc;
		handle = _pc.getHandle ();
		
		if (isCoolingDown (scroll)) {
			return;
		}		
		
		switch (scroll.useType) {
		case TYPE_USE_NTELE:
			TeleportScroll (scroll);
			break;
			
		case TYPE_USE_SOSC:
			PolyScroll (scroll, packetReader);
			//變形類道具
			/* 參考l1j
			public static void doPoly(L1PcInstance pc, int polyId, int timeSecs) {
		pc.killSkillEffectTimer(L1SkillId.SHAPE_CHANGE);
		pc.setSkillEffect(L1SkillId.SHAPE_CHANGE, timeSecs * 1000);
		if (pc.getTempCharGfx() != polyId) { // ???澈?文??????縑隞亙???閬???
			L1ItemInstance weapon = pc.getWeapon();
			// ?澈?∴??甇血??????
			boolean weaponTakeoff = (weapon != null && !isEquipableWeapon(
					polyId, weapon.getItem().getType()));
			pc.setTempCharGfx(polyId);
			pc
					.sendPackets(new S_ChangeShape(pc.getId(), polyId,
							weaponTakeoff));
			if (!pc.isGmInvis() && !pc.isInvisble()) {
				pc.broadcastPacket(new S_ChangeShape(pc.getId(), polyId));
			}
			pc.getInventory().takeoffEquip(polyId);
			weapon = pc.getWeapon();
			if (weapon != null) {
				S_CharVisualUpdate charVisual = new S_CharVisualUpdate(pc);
				pc.sendPackets(charVisual);
				pc.broadcastPacket(charVisual);
			}
		}
		pc.sendPackets(new S_SkillIconGFX(35, timeSecs));
	}
			 */
			break;
		case TYPE_USE_BLANK: //空的魔法卷軸
			//int skill = packetReader.readByte ();
			break;
			
		case TYPE_USE_IDENTIFY:
			System.out.printf ("鑑定卷軸\n");
			int uuid = packetReader.readDoubleWord ();
			ItemInstance identifyItem = pc.itemBag.get (uuid);
			if (identifyItem != null) {
				//TODO:顯示鑑定文字
				//訊息$133~$135使用
			}
			
			break;
		case TYPE_USE_RES: //復活卷軸
			int target = packetReader.readDoubleWord ();
			break;
			
		default:
			handle.sendPacket (new GameMessage (74).getPacket ());
			System.out.printf ("未知種類的捲軸或還沒有處理:%d %s\n", scroll.id, scroll.name);
			break;
		}
	}
	
	public boolean isCoolingDown (ItemInstance i) {
		return !(pc.getItemDelay (i.id, System.currentTimeMillis ()) > i.delayTime);
	}
	
	//順移卷軸
	private void TeleportScroll (ItemInstance scroll) {
	
		try {
			Thread.sleep (500);
		} catch (Exception e) {
			e.printStackTrace ();
		}
		
		Location dest;
		dest = pc.map.getRandomLocation ();		
		new Teleport (pc, dest, true);
		
		pc.removeItem (scroll.uuid, 1);
	}
	
	//變形卷軸
	private void PolyScroll (ItemInstance scroll, PacketReader packetReader) {
		String polyName = packetReader.readString ();
		
		if (polyName.equals ("")) {
			pc.removeSkillEffect (SkillId.SHAPE_CHANGE);
			pc.removeItem (scroll.uuid, 1);
			
		} else {
			int polyId = CacheData.polyIds.get (polyName);
			PolyTemplate poly = CacheData.poly.get (polyId);
			
			//level check
			//if (pc.level < poly.minLevel) {
				//handle.sendPacket (new GameMessage (181).getRaw ());
			//} else {
				pc.addSkillEffect (SkillId.SHAPE_CHANGE, 10, polyId);
				pc.removeItem (scroll.uuid, 1);
			//}
			
		} //end of polyName
		
	}
}
