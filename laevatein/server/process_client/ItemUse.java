package laevatein.server.process_client;

import laevatein.server.*;
import laevatein.server.packet.*;
import laevatein.server.process_server.*;
import laevatein.game.model.player.*;
import laevatein.game.model.item.*;
import laevatein.game.model.item.potion.*;
import laevatein.game.model.item.scroll.*;
import static laevatein.game.template.ItemTypeTable.*;

//無法使用 server_id=74
//e.g. handle.sendPacket (new ServerMessage (74, new String[] {item.name}).getRaw ());

public class ItemUse
{
	PcInstance pc;
	SessionHandler handle;
	
	public ItemUse (SessionHandler _handle, byte[] data) {
		PacketReader packetReader = new PacketReader (data);
		
		handle = _handle;
		pc = handle.user.activePc;
		
		int itemUuid = packetReader.readDoubleWord ();
		
		ItemInstance item = pc.itemBag.get (itemUuid);
		if (item != null) {			
			/* 使用道具 */
			if (item.isItem ()) {
				System.out.printf ("使用道具-%s,major[%d],minor[%d],useType[%d]\n", item.name, item.majorType, item.minorType, item.useType);
				
				/*
				switch (item.minorType) {
				case TYPE_POTION:
					break;
				
				case TYPE_SCROLL:
					break;
					
				case TYPE_ARROW:
					break;
				
				case TYPE_STING:
					break;
				
				default:
					break;
				}*/
				
				if (item.minorType == TYPE_ARROW) {
					//pc.setArrow (item.uuid);
					
				} else if (item.minorType == TYPE_WAND) {
					//
				} else if (item.minorType == TYPE_LIGHT) {
					//
				} else if (item.minorType == TYPE_GEM) {
					handle.sendPacket (new GameMessage (74, new String[] {item.name}).getRaw ());
					
				} else if (item.minorType == TYPE_TOTEM) {
					handle.sendPacket (new GameMessage (74, new String[] {item.name}).getRaw ());
					
				} else if (item.minorType == TYPE_FIRECRACKER) {
					//
				} else if (item.minorType == TYPE_POTION) {
					new UsePotion (pc, item) ;
					//Pc.removeItem (i.Uuid, 1) ;
					
				} else if (item.minorType == TYPE_FOOD) {
					//
				} else if (item.minorType == TYPE_SCROLL) {
					new UseScroll (packetReader, pc, item);
					
				} else if (item.minorType == TYPE_QUEST_ITEM) {
					//
				} else if (item.minorType == TYPE_SPELL_BOOK) {
					//
				} else if (item.minorType == TYPE_PET_ITEM) {
					//
				} else if (item.minorType == TYPE_OTHER) {
					if (item.id == 40310) { //一般信件
						int mailCode = packetReader.readWord () ;
						String mailReciever = packetReader.readString () ;
						byte[] mailText = packetReader.readRaw () ;
						
						System.out.printf ("Mail code:%d\n", mailCode) ;
						System.out.printf ("To:%s\n", mailReciever) ;
						System.out.println (mailText.toString ());
					}
					
					if (item.id == 40311) { //血盟信件
					}
					
					if (item.id >= 40373 && item.id <= 40390) { //使用地圖
						//handle.sendPacket (new MapUse (item.uuid, item.id).getRaw ());
					}
				} else if (item.minorType == TYPE_MATERIAL) {
					handle.sendPacket (new GameMessage (74, new String[] {item.name}).getRaw ());
					
				} else if (item.minorType == TYPE_EVENT) {
					//
				} else if (item.minorType == TYPE_STING) {
					//
				} else { //未知道具 無法使用
					System.out.printf ("%s 使用未知種類道具%d(Type:%d)\n", pc.name, item.uuid, item.minorType) ;
				}
				
			/* 使用武器 */
			} else if (item.isWeapon ()) {
				pc.setWeapon (item.uuid);
				
				/* 更新腳色武器外型 */
				/*
				if (pc.isPoly ()) {
					//
				} else {
					byte[] packet = new UpdateModelGfx (pc.uuid, pc.getWeaponGfx ()).getRaw ();
					handle.sendPacket (packet) ;
					pc.boardcastPcInsight (packet) ;
				}*/
			
			/* 使用防具 */
			} else if (item.isArmor ()) {
				pc.setArmor (item.uuid);
				
			} else {
				System.out.printf ("%s 使用不明道具%d(Major type:%d)\n", pc.name, item.uuid, item.majorType) ;
			}
		}
	}
}
