package laevatein.server.process_server;

import laevatein.server.packet.*;

public abstract class _PacketFrame
{
	PacketBuilder packet = new PacketBuilder ();
	
	public byte[] getPacket () {
		return packet.getPacket ();
	}
}
