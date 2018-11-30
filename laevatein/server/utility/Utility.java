package laevatein.server.utility;

import java.util.*;

import laevatein.config.*;
import laevatein.game.model.player.*;

public class Utility
{
	private static Random random = new Random ();
	
	//TODO:實作體質影響
	public static int calcIncreaseHp (int type, int hp, int maxHp, int con) {
		int randomHp = 0;
		
		if (con > 15) {
			randomHp = (short) (con - 15) ;
		}
		
		switch (type) {
		case TypeId.ROYAL:
			randomHp += (5 + random.nextInt (6));
			if ((hp + maxHp) > Configurations.MAX_HP_ROYAL) {
				randomHp = Configurations.MAX_HP_ROYAL - maxHp;
			}
			break;
			
		case TypeId.KNIGHT:
			randomHp += (6 + random.nextInt (7));
			if ((hp + maxHp) > Configurations.MAX_HP_KNIGHT) {
				randomHp = Configurations.MAX_HP_KNIGHT - maxHp;
			}
			break;
			
		case TypeId.ELF:
			randomHp += (5 + random.nextInt (6));
			if ((hp + maxHp) > Configurations.MAX_HP_ELF) {
				randomHp = Configurations.MAX_HP_ELF - maxHp;
			}
			break;
			
		case TypeId.WIZARD:
			randomHp += (3 + random.nextInt (4));
			if ((hp + maxHp) > Configurations.MAX_HP_MAGE) {
				randomHp = Configurations.MAX_HP_MAGE - maxHp;
			}
			break;
			
		case TypeId.DARKELF:
			randomHp += (5 + random.nextInt (6));
			if ((hp + maxHp) > Configurations.MAX_HP_DARKELF) {
				randomHp = Configurations.MAX_HP_DARKELF - maxHp;
			}
			break;
			
		default:
			randomHp = 0;
			break;
		}
		
		return randomHp;
		
	}
	
	static final int SEED[] = {
			-2, -2, -2, -2, -2, -2, -2, -2, -2, -2, //0-9
			-1, -1,  0,  0,  0,  2,  2,  2,  3,  3, //10-19
			 4,  5,  5,  5,  6,  7,  9 //20-26
	};
	public static int calcIncreaseMp (int type, int mp, int maxMp, int wis) {
		int randomMp = 0;
		int seed = 0;
		
		if (wis > 26) {
			seed = SEED[26];
		} else {
			seed = SEED[wis];
		}
		
		/*
		randommp = 2 + rnd.nextInt(3 + seed % 2 + (seed / 6) * 2) + seed / 2
				- seed / 6;
		*/
		//TODO
		//幹破你娘在算殺小
		//這組沒改掉我跟你姓
		randomMp = 2 + random.nextInt (3 + seed % 2 + (seed / 6) * 2) + seed / 2 - seed / 6;
		
		if (type == 0) {
			if (maxMp + randomMp > Configurations.MAX_MP_ROYAL) {
				randomMp = Configurations.MAX_MP_ROYAL - maxMp;
			}
			
		} else if (type == 1) {
			if (wis == 9) {
				randomMp --;
			} else {
				randomMp = (int) (1.0 * randomMp / 2 + 0.5) ;
			}
			
			if (maxMp + randomMp > Configurations.MAX_MP_KNIGHT) {
				randomMp = Configurations.MAX_MP_KNIGHT - maxMp;
			}
		} else if (type == 2) {
			randomMp = (int) (randomMp * 1.5) ;
			if (maxMp + randomMp > Configurations.MAX_MP_ELF) {
				randomMp = Configurations.MAX_MP_ELF - maxMp;
			}
			
		} else if (type == 3) {
			randomMp = (int) (randomMp * 2.0) ;
			if (maxMp + randomMp > Configurations.MAX_MP_MAGE) {
				randomMp = Configurations.MAX_MP_MAGE - maxMp;
			}
			
		} else if (type == 4) {
			randomMp = (int) (randomMp * 1.5) ;
			if (maxMp + randomMp > Configurations.MAX_MP_DARKELF) {
				randomMp = Configurations.MAX_MP_DARKELF - maxMp;
			}
			
		} else {
			return 0;
		}
		return randomMp;
	}
	
	public static int calcHpr (int con) {
		if (con < 14) {
			return 1;
		} else {
			return (con - 12);
		}
	}
	
	public static int calcMpr (int wis) {
		if (wis < 15) {
			return 1;
		} else if (wis < 17) {
			return 2;
		} else {
			return 3;
		}
	}
	
	private static final int MR_K[] = {
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, //0-9
			0, 0, 0, 0, 0, 3, 3, 6,10,15, //10-19
			21,28,37,47,50 //20-24
	} ;
	public static int calcMr (int type, int level, int wis) {
		int mr = 0;
		int k = 0;
		
		//set base MR
		if (type == PcInstance.TYPE_ROYAL) {
			mr = 10;
		} else if (type == PcInstance.TYPE_KNIGHT) {
			mr = 0;
		} else if (type == PcInstance.TYPE_ELF) {
			mr = 25;
		} else if (type == PcInstance.TYPE_WIZARD) {
			mr = 10;
		} else if (type == PcInstance.TYPE_DARKELF) {
			mr = 10;
		}
		
		//精神修正
		if (wis > 24) {
			k = MR_K[24];
		} else {
			k = MR_K[wis];
		}
		
		mr += k + (level >> 1) ;
		
		return mr;
	}
	
	//http://gametsg.techbang.com/lineage/index.php?view=article&articleid=1163448
	private static final int SP_K[] = { //額外魔法點數
			-1, -1, -1, -1, -1, -1, -1, -1, -1, 0, //0-9
			 0,  0,  1,  1,  1,  2,  2,  2 //10-17
	} ;
	public static int calcSp (int type, int level, int intel) {
		int sp = 0;
		int k = 0;
		
		if (type == PcInstance.TYPE_ROYAL) { //royan
			sp = level / 10;
		} else if (type == PcInstance.TYPE_KNIGHT) { //knight
			sp = level / 50;
		} else if (type == PcInstance.TYPE_ELF) { //elf
			//sp = level / 8;
			sp = level >>> 3;
		} else if (type == PcInstance.TYPE_WIZARD) { //wizard
			//sp = level / 4;
			sp = level >>> 2;
		} else if (type == PcInstance.TYPE_DARKELF) { //darkelf
			sp = level / 12;
		}
		
		if (intel > 17) {
			k = intel - 15;
		} else {
			k = SP_K[intel];
		}
		
		return sp + k;
	}
	
	private static final int DEX_K[] = {
			8, 8, 8, 8, 8, 8, 8, 8, 8, 8, //0~9 DEX
			7, 7, 7, 6, 6, 6, 5, 5, 4} ;  //10~18 DEX
	public static int calcAcBonusFromDex (int level, int dex) {
		int bonus = 10;
		int k = 0;
		if (dex > 18) {
			k = DEX_K[18];
		} else {
			k = DEX_K[dex];
		}
		
		bonus -= level / k;
		return bonus;
	}
}
