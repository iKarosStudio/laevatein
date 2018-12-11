package laevatein.server.process_server;

import laevatein.server.opcodes.*;
import laevatein.game.model.item.*;

public class UpdateItemAmount extends _PacketFrame
{
	public UpdateItemAmount (ItemInstance item) {
		packet.writeByte (ServerOpcodes.ITEM_UPDATE_AMOUNT);
		packet.writeDoubleWord (item.uuid);
		packet.writeDoubleWord (item.count);
		packet.writeByte (0);
	}
}
