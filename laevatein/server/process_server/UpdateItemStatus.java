package laevatein.server.process_server;

import laevatein.server.opcodes.*;
import laevatein.game.model.item.*;

public class UpdateItemStatus extends _PacketFrame
{	
	public UpdateItemStatus (ItemInstance item) {
		packet.writeByte (ServerOpcodes.ITEM_UPDATE_STATUS);
		packet.writeDoubleWord (item.uuid);
		packet.writeString (item.getName ());
		packet.writeDoubleWord (item.count);
		
		if (item.isIdentified) {
			byte[] detail = item.getDetail ();
			packet.writeByte (detail.length);
			packet.writeByte (detail);
		} else {
			packet.writeByte (0);
		}
	}
}
