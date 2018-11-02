package laevatein.server.process_client;

import laevatein.server.*;
import laevatein.server.packet.*;
import laevatein.game.model.player.*;
import laevatein.game.model.item.*;

public class ItemDelete
{
	public ItemDelete (SessionHandler handle, byte[] data) {
		PacketReader packetReader = new PacketReader (data);
		PcInstance pc = handle.user.activePc;
		
		int itemUuid = packetReader.readDoubleWord ();
		
		ItemInstance item = pc.itemBag.get (itemUuid);
		if (item != null) {
			pc.deleteItem (item);
		}
	}
}
