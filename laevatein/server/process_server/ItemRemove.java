package laevatein.server.process_server;

import laevatein.server.packet.*;
import laevatein.server.opcodes.*;
import laevatein.game.model.item.*;

public class ItemRemove
{
	PacketBuilder packet = new PacketBuilder ();
	
	public ItemRemove (ItemInstance item) {
		packet.writeByte (ServerOpcodes.ITEM_REMOVE);
		packet.writeDoubleWord (item.uuid);
	}
	
	public byte[] getRaw () {
		return packet.getPacket ();
	}
}
