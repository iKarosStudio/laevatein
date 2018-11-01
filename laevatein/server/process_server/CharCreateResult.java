package laevatein.server.process_server;

import laevatein.server.packet.*;
import laevatein.server.opcodes.*;

public class CharCreateResult
{
	PacketBuilder packet = new PacketBuilder () ;
	
	public static int OK = 0x02;
	public static int ALREADY_EXIST = 0x06;
	public static int INVALID_ID = 0x09;
	public static int WRONG_AMOUNT = 0x15;
	
	public CharCreateResult (int result) {
		packet.writeByte (ServerOpcodes.CHAR_CREATE_RESULT);
		packet.writeByte (result);
		packet.writeDoubleWord (0);
		packet.writeDoubleWord (0);
	}
	
	public byte[] getRaw () {
		return packet.getPacket () ;
	}
}
