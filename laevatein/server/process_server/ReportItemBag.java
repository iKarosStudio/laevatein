package laevatein.server.process_server;

import java.util.concurrent.*;

import laevatein.server.opcodes.*;
import laevatein.server.packet.*;
import laevatein.game.model.item.*;

public class ReportItemBag
{
	PacketBuilder packet = new PacketBuilder () ;
	
	public ReportItemBag (ConcurrentHashMap<Integer, ItemInstance> itemBag) {
		
		packet.writeByte (ServerOpcodes.ITEM_LIST) ;
		packet.writeByte (itemBag.size ());
		
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
	
	public byte[] getRaw () {
		return packet.getPacket ();
	}
}
