package laevatein.server.process_server;

import laevatein.server.opcodes.*;

public class UpdateMp extends _PacketFrame
{
	public UpdateMp (int mp, int maxMp) {
		packet.writeByte (ServerOpcodes.UPDATE_MP);
		packet.writeWord (mp);
		packet.writeWord (maxMp);
	}
}
