package laevatein.server.process_server;

import laevatein.server.opcodes.*;

public class UpdateWeight extends _PacketFrame
{
	public UpdateWeight (int weight30) {
		packet.writeByte (ServerOpcodes.PACKET_BOX);
		packet.writeByte (10); //sub-op
		packet.writeByte (weight30);
	}
}
