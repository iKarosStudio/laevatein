package laevatein.server.process_server;

import laevatein.server.packet.*;
import laevatein.server.opcodes.*;

public class ReportWeather
{
	PacketBuilder packet = new PacketBuilder ();
	
	public ReportWeather (int weather) {
		packet.writeByte (ServerOpcodes.WEATHER);
		packet.writeByte (weather);
	}
	
	public byte[] getRaw () {
		return packet.getPacket ();
	}
}
