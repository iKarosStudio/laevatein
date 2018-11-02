package laevatein.server.process_server;

import laevatein.server.packet.*;
import laevatein.server.opcodes.*;

public class SkillIcon
{
	PacketBuilder packet = new PacketBuilder ();
	
	public SkillIcon (int iconId, int time) {
		packet.writeByte (ServerOpcodes.SKILL_ICON);
		packet.writeByte (iconId);
		packet.writeWord (time);
	}
	
	public byte[] getPacket () {
		return packet.getPacket ();
	}
}
