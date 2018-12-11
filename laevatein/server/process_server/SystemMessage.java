package laevatein.server.process_server;

import laevatein.server.opcodes.*;

public class SystemMessage extends _PacketFrame
{
	public SystemMessage (String msg) {
		packet.writeByte (ServerOpcodes.SYSTEM_MSG);
		packet.writeByte (0x09);
		packet.writeString (msg);
	}
}
