package laevatein.server.process_client;

import laevatein.server.*;
import laevatein.server.packet.*;
import laevatein.game.model.player.*;

public class Move {
	public Move (SessionHandler handle, byte[] data) {
		PacketReader packetReader = new PacketReader (data);
		PcInstance pc = handle.getUser().getActivePc ();
		
		int tmpX = packetReader.readWord () ; //pseudo
		int tmpY = packetReader.readWord () ; //pseudo
		int heading = packetReader.readByte () ;
		
		pc.move (tmpX, tmpY, heading);
	}
}
