package laevatein.server.process_server;

import laevatein.server.opcodes.*;

public class Poison extends _PacketFrame
{
	public static final int COLOR_NORMAL = 0x0;
	public static final int COLOR_POISON = 0x1;
	public static final int COLOR_PARALYZE = 0x2;
	
	public Poison (int uuid, int type) {
		packet.writeByte (ServerOpcodes.POISON);
		packet.writeDoubleWord (uuid);
		switch (type) {
		case COLOR_NORMAL:
			packet.writeByte (0x00);
			packet.writeByte (0x00);
			break;
			
		case COLOR_POISON:
			packet.writeByte (0x01);
			packet.writeByte (0x00);
			break;
			
		case COLOR_PARALYZE:
			packet.writeByte (0x00);
			packet.writeByte (0x01);
			break;
			
		default:
			packet.writeByte (0x00);
			packet.writeByte (0x00);
			break;
		}
	}
}
