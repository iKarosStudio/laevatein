package laevatein.server.process_server;

import laevatein.server.opcodes.*;

public class CharCreateResult extends _PacketFrame
{
	public static final int OK = 0x02;
	public static final int ALREADY_EXIST = 0x06;
	public static final int INVALID_ID = 0x09;
	public static final int WRONG_AMOUNT = 0x15;
	
	public CharCreateResult (int result) {
		packet.writeByte (ServerOpcodes.CHAR_CREATE_RESULT);
		packet.writeByte (result);
		packet.writeDoubleWord (0);
		packet.writeDoubleWord (0);
	}
}
