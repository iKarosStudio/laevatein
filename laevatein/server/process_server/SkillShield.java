package laevatein.server.process_server;

import laevatein.server.opcodes.*;

public class SkillShield extends _PacketFrame
{

	public SkillShield (int remainTime, int type) {
		packet.writeByte (ServerOpcodes.SKILL_SHIELD);
		
		//Remain Time  0xFFFF -> 永久效果
		packet.writeWord (remainTime);
		packet.writeByte (type);
		packet.writeDoubleWord (0);
	}
}
