package laevatein.server.process_server;

import laevatein.server.opcodes.*;

public class ReportSpMr extends _PacketFrame
{
	public ReportSpMr (int sp, int mr) {
		packet.writeByte (ServerOpcodes.MATK_MRST) ;
		packet.writeByte (sp); //sp
		packet.writeByte (mr); //mr
	}
}
