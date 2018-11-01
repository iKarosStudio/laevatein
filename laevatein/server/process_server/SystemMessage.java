package laevatein.server.process_server;

import laevatein.server.packet.*;
import laevatein.server.opcodes.*;

public class SystemMessage
{
	PacketBuilder packet = new PacketBuilder ();
	
	public SystemMessage (String msg) {
		packet.writeByte (ServerOpcodes.SYSTEM_MSG);
		packet.writeByte (0x09);
		packet.writeString (msg);
	}
	
	public byte[] getRaw () {
		return packet.getPacket ();
	}
}
