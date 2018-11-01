package laevatein.server.process_server;

import laevatein.server.packet.*;
import laevatein.server.opcodes.*;

//指定uuid物件的外型變成gfx代號
public class UpdateModelGfx
{
	PacketBuilder packet = new PacketBuilder () ;
	
	public UpdateModelGfx (int uuid, int gfx) {
		packet.writeByte (ServerOpcodes.UPDATE_PC_GFX);
		packet.writeDoubleWord (uuid);
		packet.writeByte (gfx);
	}
	
	public byte[] getRaw () {
		return packet.getPacket ();
	}
}
