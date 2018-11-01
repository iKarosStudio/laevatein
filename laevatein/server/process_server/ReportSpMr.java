package laevatein.server.process_server;

import laevatein.server.*;
import laevatein.server.packet.*;
import laevatein.server.opcodes.*;
import laevatein.game.model.player.*;

public class ReportSpMr
{
	PacketBuilder packet = new PacketBuilder () ;
	
	public ReportSpMr (SessionHandler handle) {
		PcInstance pc = handle.user.activePc;
		
		packet.writeByte (ServerOpcodes.MATK_MRST) ;
		packet.writeByte (0); //sp
		packet.writeByte (0); //mr
	}
	
	public byte[] getRaw () {
		return packet.getPacket ();
	}
}
