package laevatein.server.process_server;

import laevatein.server.opcodes.*;
import laevatein.game.model.item.*;

public class ItemRemove extends _PacketFrame
{
	public ItemRemove (ItemInstance item) {
		packet.writeByte (ServerOpcodes.ITEM_REMOVE);
		packet.writeDoubleWord (item.uuid);
	}
}
