package laevatein.game.model.item;

import laevatein.game.model.player.*;
import laevatein.game.skill.SkillId;
import laevatein.server.*;
import laevatein.server.packet.*;
import laevatein.server.process_server.*;

import laevatein.game.model.item.potion.*;
import laevatein.game.model.item.scroll.*;

import static laevatein.game.template.ItemTypeTable.*;

public class ItemUseParser
{
	PcInstance pc;
	SessionHandler handle;
	
	public ItemUseParser (PcInstance _pc, ItemInstance item, PacketReader packetReader) {
		pc = _pc;
		handle = pc.getHandle ();
		
		System.out.printf ("item-parser:%s 使用道具-%s,major[%d],minor[%d],useType[%d]\n", pc.name, item.name, item.majorType, item.minorType, item.useType);
		
		//麻痺時不能使用
		if (pc.hasSkillEffect (SkillId.CURSE_PARALYZE) ||
			pc.hasSkillEffect (SkillId.STATUS_CURSE_PARALYZED) ||
			pc.hasSkillEffect (SkillId.STATUS_POISON_PARALYZED)) {
			return;
		}
		
		//冷凍時不能使用
		if (pc.isFreeze ()) {
			return;
		}
		
		switch (item.minorType) {
		case TYPE_POTION:
			new UsePotion (_pc, item);
			break;
			
		case TYPE_SCROLL:
			new UseScroll (_pc, item, packetReader);
			break;
			
		case TYPE_ARROW:
			break;
		
		case TYPE_FIRECRACKER:
			break;
		
		case TYPE_LIGHT:
			break;
			
		case TYPE_FOOD:
			break;
			
		case TYPE_SPELL_BOOK: //魔法書
			break;
			
		case TYPE_OTHER:
			if (item.id == 40310) { //一般信件
				//int mailCode = packetReader.readWord ();
				//String mailReciever = packetReader.readString ();
				//byte[] mailText = packetReader.readRaw ();
				//TODO:建立伺服器端郵件控制
			}
			
			if (item.id == 40311) { //血盟信件
				//TODO:建立伺服器端郵件控制
			}
			
			if (item.id >= 40373 && item.id <= 40390) { //使用地圖
				handle.sendPacket (new UseMap (item.uuid, item.id).getPacket ());
			}
			break;
			
		case TYPE_GEM:
		case TYPE_TOTEM:
		case TYPE_QUEST_ITEM:
		case TYPE_MATERIAL:
			handle.sendPacket (new GameMessage (74, item.getName ()).getPacket ()); // ${name}無法使用
			break;
			
		default:
			System.out.printf ("%s unknown minor_type:%d\n", item.name, item.minorType);
			break;
		}
		//
		
		/*
		 if (item.minorType == TYPE_ARROW) {
					//pc.setArrow (item.uuid);
					
				} else if (item.minorType == TYPE_WAND) {
					//
				} else if (item.minorType == TYPE_LIGHT) {
					//
				} else if (item.minorType == TYPE_GEM) {
					//handle.sendPacket (new GameMessage (74, new String[] {item.name}).getRaw ());
					
				} else if (item.minorType == TYPE_TOTEM) {
					//handle.sendPacket (new GameMessage (74, new String[] {item.name}).getRaw ());
					
				} else if (item.minorType == TYPE_FIRECRACKER) {
					//
				} else if (item.minorType == TYPE_POTION) {
					//new UsePotion (pc, item) ;
					//Pc.removeItem (i.Uuid, 1) ;
					
				} else if (item.minorType == TYPE_FOOD) {
					//
				} else if (item.minorType == TYPE_SCROLL) {
					//new UseScroll (packetReader, pc, item);
					
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
		 */
	}
}
