package laevatein.server.process_server;

import laevatein.server.packet.*;
import laevatein.server.opcodes.*;

//指定uuid物件的外型變成gfx代號
public class UpdateModelActId
{
	PacketBuilder packet = new PacketBuilder () ;
	
	public UpdateModelActId (int uuid, int actId) {
		packet.writeByte (ServerOpcodes.UPDATE_MODEL_ACTID);
		packet.writeDoubleWord (uuid);
		packet.writeByte (actId);
	}
	
	public byte[] getRaw () {
		return packet.getPacket ();
	}
}
