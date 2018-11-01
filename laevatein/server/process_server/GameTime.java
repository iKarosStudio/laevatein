package laevatein.server.process_server;

import laevatein.server.*;
import laevatein.server.packet.*;
import laevatein.server.opcodes.*;

public class GameTime
{
	PacketBuilder packet = new PacketBuilder ();
	
	public GameTime () {
		ServerTime serverTime = ServerTime.getInstance ();
		
		packet.writeByte (ServerOpcodes.SYS_TICK) ;
		packet.writeDoubleWord (serverTime.getTime ()); //get sys tick time
	}
	
	public byte[] getRaw () {
		return packet.getPacket ();
	}
}
