package laevatein.server.process_server;

import laevatein.server.*;
import laevatein.server.opcodes.*;

public class GameTime extends _PacketFrame
{
	public GameTime () {
		ServerTime serverTime = ServerTime.getInstance ();
		
		packet.writeByte (ServerOpcodes.SYS_TICK) ;
		packet.writeDoubleWord (serverTime.getTime ()); //get sys tick time
	}
}
