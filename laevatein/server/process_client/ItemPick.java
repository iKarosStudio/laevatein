package laevatein.server.process_client;

import laevatein.server.*;
import laevatein.server.packet.*;
import laevatein.game.model.player.*;

public class ItemPick
{
	public ItemPick (SessionHandler handle, byte[] packet) {
		PacketReader reader = new PacketReader (packet);
		PcInstance pc = handle.user.activePc;
		
		int x = reader.readWord ();
		int y = reader.readWord ();
		int uuid = reader.readDoubleWord ();
		int count = reader.readDoubleWord ();

		pc.pickItem (uuid, count, x, y);
	}
}
