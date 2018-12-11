package laevatein.server.process_server;

import laevatein.server.opcodes.*;

public class MapId extends _PacketFrame
{
	public MapId (int mapId) {
		packet.writeByte (ServerOpcodes.MAP_ID);
		packet.writeWord (mapId);
		packet.writeByte (0x00); //1->underwater
		packet.writeByte (0x00);
		packet.writeWord (0x0000);
		packet.writeByte (0x00);
		packet.writeDoubleWord (0x00000000);
	}
}
