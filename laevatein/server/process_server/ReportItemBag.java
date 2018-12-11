package laevatein.server.process_server;

import java.util.concurrent.*;

import laevatein.server.opcodes.*;
import laevatein.game.model.item.*;

public class ReportItemBag extends _PacketFrame
{
	public ReportItemBag (ConcurrentHashMap<Integer, ItemInstance> itemBag) {
		
		packet.writeByte (ServerOpcodes.ITEM_LIST) ;
		packet.writeByte (itemBag.size ()); //道具項目數量
		
		itemBag.forEach ((Integer uuid, ItemInstance item)->{			
			packet.writeDoubleWord (uuid);
			packet.writeByte (item.useType);
			packet.writeByte (0);
			packet.writeWord (item.gfxInBag);
			packet.writeByte (item.bless);
			packet.writeDoubleWord (item.count);
			packet.writeByte (item.isIdentified);				
			packet.writeString (item.getName ());
			
			if (item.isIdentified) {
				byte[] detail = null;
				detail = item.getDetail ();

				packet.writeByte (detail.length);
				packet.writeByte (detail);
			} else {
				packet.writeByte (0);
			}
		}) ;
	}
}
