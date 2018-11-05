package laevatein.server.process_server;

import laevatein.server.*;
import laevatein.server.packet.*;
import laevatein.server.opcodes.*;
import laevatein.game.model.player.*;

public class ReportSpMr
{
	PacketBuilder packet = new PacketBuilder ();
	
	public ReportSpMr (int sp, int mr) {
		packet.writeByte (ServerOpcodes.MATK_MRST) ;
		packet.writeByte (sp); //sp
		packet.writeByte (mr); //mr
	}
	
	public byte[] getRaw () {
		return packet.getPacket ();
	}
}
