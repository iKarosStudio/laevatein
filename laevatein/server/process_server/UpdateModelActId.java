package laevatein.server.process_server;

import laevatein.server.opcodes.*;

//指定uuid物件的外型變成gfx代號
public class UpdateModelActId extends _PacketFrame
{
	public UpdateModelActId (int uuid, int actId) {
		packet.writeByte (ServerOpcodes.UPDATE_MODEL_ACTID);
		packet.writeDoubleWord (uuid);
		packet.writeByte (actId);
	}
}
