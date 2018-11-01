package laevatein.server.process_server;

import laevatein.server.packet.*;
import laevatein.server.opcodes.*;

public class UpdateMp
{
	PacketBuilder builder = new PacketBuilder ();
	
	public UpdateMp (int mp, int maxMp) {
		builder.writeByte (ServerOpcodes.MP_UPDATE);
		builder.writeWord (mp);
		builder.writeWord (maxMp);
	}
	
	public byte[] getRaw () {
		return builder.getPacket ();
	}
}
