package laevatein.server.process_server;

import laevatein.server.opcodes.*;

public class ReportWeather extends _PacketFrame
{
	public ReportWeather (int weather) {
		packet.writeByte (ServerOpcodes.WEATHER);
		packet.writeByte (weather);
	}
}
