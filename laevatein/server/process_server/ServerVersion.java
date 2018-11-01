package laevatein.server.process_server;

import laevatein.server.*;
import laevatein.server.packet.*;
import laevatein.server.opcodes.*;

public class ServerVersion
{
	public ServerVersion (SessionHandler Handle) {
		PacketBuilder packet = new PacketBuilder ();
		ServerTime serverTime = ServerTime.getInstance ();
		
		packet.writeByte (ServerOpcodes.SERVER_VERSION);
		packet.writeByte (0x00);
		packet.writeByte (0x02);
		packet.writeDoubleWord (0x00009D7C);
		packet.writeDoubleWord (0x0000791A);
		packet.writeDoubleWord (0x0000791A);
		packet.writeDoubleWord (0x00009DD1);
		packet.writeDoubleWord (serverTime.getTime ()); //time
		packet.writeByte (0x00);
		packet.writeByte (0x00);
		packet.writeByte (0x03); //3:繁體中文
		
		Handle.sendPacket (packet.getPacket ());
	}
}
