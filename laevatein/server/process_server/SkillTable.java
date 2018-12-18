package laevatein.server.process_server;

import java.util.*;

import laevatein.server.opcodes.*;

public class SkillTable extends _PacketFrame
{
	public SkillTable (int _pcType, HashMap<Integer, Integer> skillTable) {		
		packet.writeByte (ServerOpcodes.SKILL_TABLE);
		
		int hasLevel5to8Skill = 0;
		int hasLevel9To10Skill = 0;
		
		for (int i = 5; i <= 10; i++) {
			if (i < 9) {
				if (skillTable.get (i) == null) { //not found
					hasLevel5to8Skill += 0;
				} else {
					hasLevel5to8Skill += skillTable.get (i);
				}
			} else {
				if (skillTable.get (i) == null) {
					hasLevel9To10Skill += 0;
				} else {
					hasLevel9To10Skill += skillTable.get (i);
				}
			}
		}
		
		//應該是技能欄的表現形態
		if ((hasLevel5to8Skill > 0) && (hasLevel9To10Skill == 0)) {
			packet.writeByte (50); //0x32
		} else if (hasLevel9To10Skill > 0) {
			packet.writeByte (100); //0x64
		} else {
			packet.writeByte (22); //0x16
		}
		
		for (int i = 1; i <= 24; i++) {
			if (skillTable.get (i) == null) {
				packet.writeByte (0);
			} else {
				packet.writeByte (skillTable.get (i));
			}
		}
		
		//格式(都是byte)
		//L1~L10
		//Knight1
		//Knight2(不確定, 但有使用到)
		//DarkElf1
		//DarkElf2
		//Royal1
		//x3(未知)
		//ELF1~ELF6
		//y5(未知)
		//x5(未知)
		//4byte0
		//4byte0
		
		packet.writeDoubleWord (0);
		packet.writeDoubleWord (0);
	}
}
