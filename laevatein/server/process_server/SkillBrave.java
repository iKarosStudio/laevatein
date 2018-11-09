package laevatein.server.process_server;

import laevatein.server.opcodes.*;

public class SkillBrave extends _PacketFrame
{
	public SkillBrave (int _uuid, int _braveSpeed, int _remainTime) {
		packet.writeByte (ServerOpcodes.SKILL_BRAVE); 
		packet.writeDoubleWord (_uuid);
		packet.writeByte (_braveSpeed);
		
		//Remain Time  0xFFFF -> 永久效果
		packet.writeWord (_remainTime);
	}
}
