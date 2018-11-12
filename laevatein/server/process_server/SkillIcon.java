package laevatein.server.process_server;

import laevatein.server.opcodes.*;

public class SkillIcon extends _PacketFrame
{	
	public SkillIcon (int iconId, int time) {
		packet.writeByte (ServerOpcodes.SKILL_ICON);
		packet.writeByte (iconId);
		packet.writeWord (time);
	}
}
