package laevatein.server.process_server;

import laevatein.server.opcodes.*;

public class UpdateHp extends _PacketFrame
{
	public UpdateHp (int hp, int maxHp) {
		packet.writeByte (ServerOpcodes.UPDATE_HP);
		packet.writeWord (hp);
		packet.writeWord (maxHp);
	}
}
