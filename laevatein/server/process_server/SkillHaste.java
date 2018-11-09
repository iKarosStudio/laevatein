package laevatein.server.process_server;

import laevatein.server.opcodes.*;

public class SkillHaste extends _PacketFrame
{	
	public SkillHaste (int _uuid, int _moveSpeed, int _remainTime) {
		packet.writeByte (ServerOpcodes.SKILL_HASTE); 
		packet.writeDoubleWord (_uuid);
		packet.writeByte (_moveSpeed);
		
		//Remain Time  0xFFFF -> 永久效果
		packet.writeWord (_remainTime);
	}
}
