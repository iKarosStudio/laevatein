package laevatein.server.process_server;

import laevatein.server.*;
import laevatein.server.packet.*;
import laevatein.server.opcodes.*;

public class SkillBuyList
{
	PacketBuilder packet = new PacketBuilder ();
	
	public SkillBuyList (SessionHandler handle, int pcType) {
		int count = 0;
		
		packet.writeByte (ServerOpcodes.SKILL_BUY_RESULT);
		packet.writeDoubleWord (100);
		
		/* count應為表達個職業可在NPC學習的技能數量限制-1(id) */
		if (pcType == 0) { //Royal
			count = 16;
		} else if (pcType == 1) { //Knight
			count = 8;
		} else if (pcType == 2) { //Elf
			count = 23;
		} else if (pcType == 3) { //Mage
			count = 23;
		} else if (pcType == 4) { //Darkelf
			count = 16;
		}
		
		packet.writeWord (count) ;
		for (int i = 0; i < count; i++) {
			packet.writeDoubleWord (i);
		}
		
		handle.sendPacket (packet.getPacket ());
	}
}
