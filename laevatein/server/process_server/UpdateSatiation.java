package laevatein.server.process_server;

import laevatein.server.opcodes.ServerOpcodes;

public class UpdateSatiation extends _PacketFrame
{
	public UpdateSatiation (int satiation) {
		packet.writeByte (ServerOpcodes.PACKET_BOX);
		packet.writeByte (11); //sub-op
		packet.writeByte (satiation);
	}
}
