package laevatein.server.process_server;

import laevatein.server.packet.*;
import laevatein.server.opcodes.*;
import laevatein.game.model.player.*;

public class UpdateExp
{
	PacketBuilder packet = new PacketBuilder ();
	
	public UpdateExp (PcInstance pc) {
		packet.writeByte (ServerOpcodes.UPDATE_EXP);
		packet.writeByte (pc.level);
		packet.writeDoubleWord (pc.exp);
	}
	
	public byte[] getRaw () {
		return packet.getPacket ();
	}
}
