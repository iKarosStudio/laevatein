package laevatein.server.igcp;

import java.util.*;

import laevatein.server.process_server.*;
import laevatein.game.model.player.*;

public class RdPcSkill
{
	PcInstance pc;
	
	public RdPcSkill (PcInstance _pc, String cmd) {
		pc = _pc;
		pc.sendPacket (new SystemMessage ("暫時取得全部技能").getPacket ());
		
		HashMap<Integer, Integer> skill = new HashMap<Integer, Integer> ();
		
		if (pc.isRoyal ()) {
			skill.put (1, 0xFF);
			skill.put (2, 0xFF);
			skill.put (15, 0xFF);
			
		} else if (pc.isKnight ()) {
			skill.put (1, 0xFF);
			skill.put (11, 0xC0); //192
			skill.put (12, 0x07);
			
		} else if (pc.isElf ()) {
			for (int i = 1; i <= 6; i++) {
				skill.put (i, 0xFF);
			}
			skill.replace (3, 0x7F);
			
			skill.put (17, 0x7F);
			skill.put (18, 0x03);
			skill.put (19, 0xFF);
			skill.put (20, 0xFF);
			skill.put (21, 0xFF);
			skill.put (22, 0xFF);
			
		} else if (pc.isWizard ()) {
			for (int i = 1; i <= 10; i++) {
				skill.put (i, 0xFF);
			}
			skill.replace (3, 0x7F);
			
		} else if (pc.isDarkelf ()) {
			skill.put (1, 0xFF);
			skill.put (2, 0xFF);
			skill.put (13, 0xFF);
			skill.put (14, 0x7F);
			
		} else {
			//
		}
		
		pc.sendPacket (new SkillTable (pc.getType (), skill).getPacket ());
	}
}
