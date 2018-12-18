package laevatein.server.process_client;

import java.util.*;

import laevatein.server.*;
import laevatein.server.packet.*;
import laevatein.server.process_server.*;
import laevatein.server.database.*;
import laevatein.game.*;
import laevatein.game.model.player.*;

public class SkillBuyOrder
{
	PacketReader reader;
	
	public SkillBuyOrder (SessionHandler handle, byte[] data) {
		reader = new PacketReader (data);
		PcInstance pc = handle.getUser ().getActivePc ();
		
		int OrderCount = reader.readWord ();
		int SkillItem[] = new int[OrderCount] ;
		int L1 = 0, L1Cost = 0;
		int L2 = 0, L2Cost = 0;
		int L3 = 0, L3Cost = 0;
		int price = 0;
		
		/* 讀取買入的選項 */
		for (int i = 0; i < OrderCount; i++) {
			SkillItem[i] = reader.readDoubleWord ();
			
			if (SkillItem[i] < 8) { //level 1
				L1 |= (1 << SkillItem[i]);
				L1Cost += 100;
			} else if (SkillItem[i] < 16) { //level 2
				L2 |= (1 << (SkillItem[i]-8));
				L2Cost += 400;
			} else if (SkillItem[i] < 24) { //level 3
				L3 |= (1 << (SkillItem[i]-16));
				L3Cost += 900;
			}
			
		}
		
		/* 根據職業及角色等級過濾還不能學的選項 */
		if (pc.isRoyal ()) {
			if (pc.getLevel () < 10) {
				L1 = 0; L1Cost = 0;
				L2 = 0; L2Cost = 0;
			} else if (pc.getLevel () < 20) {
				L2 = 0; L2Cost = 0;
			}
			L3 = 0; L3Cost = 0;
		}
		
		if (pc.isKnight ()) {
			if (pc.getLevel () < 50) {
				L1 = 0; L1Cost = 0;
			}
			L2 = 0; L2Cost = 0;
			L3 = 0; L3Cost = 0;
		}
		
		if (pc.isElf ()) {
			if (pc.getLevel () < 8) {
				L1 = 0; L1Cost = 0;
				L2 = 0; L2Cost = 0;
				L3 = 0; L3Cost = 0;
			} else if (pc.getLevel () < 16) {
				L2 = 0; L2Cost = 0;
				L3 = 0; L3Cost = 0;
			} else if (pc.getLevel () < 24) {
				L3 = 0; L3Cost = 0;
			}
		}
		
		if (pc.isWizard ()) {
			if (pc.getLevel () < 4) {
				L1 = 0; L1Cost = 0;
				L2 = 0; L2Cost = 0;
				L3 = 0; L3Cost = 0;
			} else if (pc.getLevel () < 8) {
				L2 = 0; L2Cost = 0;
				L3 = 0; L3Cost = 0;
			} else if (pc.getLevel () < 12) {
				L3 = 0; L3Cost = 0;
			}
		}
		
		if (pc.isDarkelf ()) {
			if (pc.getLevel () < 12) {
				L1 = 0; L1Cost = 0;
				L2 = 0; L2Cost = 0;
			} else if (pc.getLevel () < 24) {
				L2 = 0; L2Cost = 0;
			}
			L3 = 0; L3Cost = 0;
		}
		
		if ( (L1 | L2 | L3) == 0) {
			return;
		}
		
		price = L1Cost + L2Cost + L3Cost;
		if (pc.getMoney () < price) {
			/* 錢不夠 */
			handle.sendPacket (new GameMessage (189).getPacket ());
		} else {
			HashMap<Integer, Integer> s = new HashMap<Integer, Integer> () ;
			for (int level = 1; level <= 24; level++) {
				if (level == 1) {
					s.put (level, L1) ;
				} else if (level == 2) {
					s.put (level, L2) ;
				} else if (level == 3) {
					s.put (level, L3) ;
				} else {
					s.put (level, 0) ;
				}
			}
			
			/* 檢查各等級沒學過的技能並寫入資料庫 */
			if (L1 != 0) {
				for (int b = 0; b < 8; b++) {
					if (((L1 >>> b) & 0x01) > 0) {
						if (!DatabaseCmds.checkSkill (pc.getUuid (), 1+b)) {
							DatabaseCmds.saveSkills (pc.getUuid (), 1+b, CacheData.skill.get (1+b).name);
						}
					}
				}
			}
			
			if (L2 != 0) {
				for (int b = 0; b < 8; b++) {
					if (((L2 >>> b) & 0x01) > 0) {
						if (!DatabaseCmds.checkSkill (pc.getUuid (), 9+b) ) {
							DatabaseCmds.saveSkills (pc.getUuid (), 9+b, CacheData.skill.get (9+b).name) ;
						}
					}
				}
			}
			
			if (L3 != 0) {
				for (int b = 0; b < 8; b++) {
					if (((L3 >>> b) & 0x01) > 0) {
						if (!DatabaseCmds.checkSkill (pc.getUuid (), 17+b) ) {
							DatabaseCmds.saveSkills (pc.getUuid (), 17+b, CacheData.skill.get (17+b).name) ;
						}
					}
				}
			}
			
			/* show virtual effect */
			handle.sendPacket (new VisualEffect (pc.getUuid (), 224).getPacket ());
			handle.sendPacket (new SkillTable (pc.getType (), s).getPacket ());
			
			/* 扣錢  */
			//Pc.removeItemByItemId (40308, Price) ;
		}
		
	}
}
