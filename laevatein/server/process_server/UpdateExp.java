package laevatein.server.process_server;

import laevatein.server.opcodes.*;
import laevatein.game.model.player.*;

public class UpdateExp extends _PacketFrame
{
	public UpdateExp (PcInstance pc) {
		packet.writeByte (ServerOpcodes.UPDATE_EXP);
		packet.writeByte (pc.getLevel ());
		packet.writeDoubleWord (pc.getExp ());
	}

}
