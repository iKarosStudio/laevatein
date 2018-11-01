package laevatein.server.process_server; 

import laevatein.server.packet.*;
import laevatein.server.opcodes.*;

/*
 * 指定uuid物件面向Heading
 */
public class ModelHeading
{
	PacketBuilder packet = new PacketBuilder ();
	public ModelHeading (int _uuid, int _heading) {
		
		packet.writeByte (ServerOpcodes.SET_HEADING);
		packet.writeDoubleWord (_uuid);
		packet.writeByte (_heading);
	}
	
	public byte[] getRaw () {
		return packet.getPacket ();
	}
}
