package laevatein.server.process_server;

import laevatein.server.*;
import laevatein.server.packet.*;
import laevatein.server.opcodes.*;

public class Unknown2
{
	public Unknown2 (SessionHandler Handle) {
		PacketBuilder packet = new PacketBuilder ();
		packet.writeByte (ServerOpcodes.UNKNOWN2);
		packet.writeByte (0xFF);
		packet.writeByte (0x7F);
		packet.writeByte (0x03);
		Handle.sendPacket (packet.getPacket ());
	}
}
