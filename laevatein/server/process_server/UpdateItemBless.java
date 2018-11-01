package laevatein.server.process_server;

import laevatein.server.packet.*;
import laevatein.server.opcodes.*;
import laevatein.game.model.item.*; 

public class UpdateItemBless
{
	PacketBuilder packet = new PacketBuilder ();
	
	public UpdateItemBless (ItemInstance item) {
		packet.writeByte (ServerOpcodes.ITEM_UPDATE_BLESS);
		packet.writeDoubleWord (item.uuid);
		packet.writeByte (item.bless);
	}

	public byte[] getRaw () {
		return packet.getPacket () ;
	}
}
