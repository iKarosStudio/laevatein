package laevatein.server.process_client;

import laevatein.server.*;
import laevatein.server.packet.*;
import laevatein.game.model.player.*;

public class ItemDrop
{
	public ItemDrop (SessionHandler handle, byte[] packet) {
		PacketReader packetReader = new PacketReader (packet);
		PcInstance pc = handle.user.activePc;
		
		int x = packetReader.readWord ();
		int y = packetReader.readWord ();
		int uuid = packetReader.readDoubleWord ();
		int count = packetReader.readDoubleWord ();
		
		pc.dropItem (uuid, count, x, y);
	}
}
