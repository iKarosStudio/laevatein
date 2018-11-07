package laevatein.server.process_server;

import laevatein.server.packet.*;
import laevatein.server.opcodes.*;

public class UpdateHp
{
	PacketBuilder packet = new PacketBuilder ();
	
	public UpdateHp (int hp, int maxHp) {
		packet.writeByte (ServerOpcodes.UPDATE_HP);
		packet.writeWord (hp);
		packet.writeWord (maxHp);
	}
	
	public byte[] getRaw () {
		return packet.getPacket ();
	}
}
