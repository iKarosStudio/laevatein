package laevatein.server.process_client;

import laevatein.server.*;
import laevatein.server.packet.*;
import laevatein.game.model.player.*;
import laevatein.game.skill.SkillId;
import laevatein.game.model.item.*;

//無法使用 server_id=74
//e.g. handle.sendPacket (new ServerMessage (74, new String[] {item.name}).getRaw ());

public class ItemUse
{
	PcInstance pc;
	SessionHandler handle;
	
	public ItemUse (SessionHandler _handle, byte[] packet) {
		PacketReader packetReader = new PacketReader (packet);
		
		handle = _handle;
		pc = handle.getUser().getActivePc ();
		
		if (pc.isFreeze ()) {
			return;
		}
		
		if (pc.hasSkillEffect (SkillId.STATUS_CURSE_PARALYZING) || pc.hasSkillEffect (SkillId.STATUS_POISON_PARALYZED)) {
			return;
		}
		
		int itemUuid = packetReader.readDoubleWord ();
		
		ItemInstance item = pc.getItemBag ().get (itemUuid);
		if (item != null) {			
			switch (item.majorType) {
			case 0: //道具
				new ItemUseParser (pc, item, packetReader);
				break;
			
			case 1: //武器
				pc.setWeapon (item.uuid);
				break;
				
			case 2: //防具
				pc.setArmor (item.uuid);
				break;
			
			default:
				System.out.printf ("%s 使用不明道具%d(Major type:%d)\n", pc.getName (), item.uuid, item.majorType);
				break;
			} //end of switch
		} //item!=null
	}
}
