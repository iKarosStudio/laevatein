package laevatein.server.process_server;

import laevatein.server.*;
import laevatein.server.packet.*;
import laevatein.server.opcodes.*;

public class Unknown1
{
	public Unknown1 (SessionHandler Handle) {
		PacketBuilder packet = new PacketBuilder ();
		packet.writeByte (ServerOpcodes.UNKNOWN1);
		packet.writeByte (0x03);
		packet.writeByte (0x00);
		packet.writeByte (0xF7);
		packet.writeByte (0xAD);
		packet.writeByte (0x74);
		packet.writeByte (0x00);
		packet.writeByte (0xE5);
		Handle.sendPacket (packet.getPacket ());
	}
}
