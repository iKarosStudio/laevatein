package laevatein.server.utility;

//import java.util.Random;

import laevatein.game.model.player.*;

public class Utility
{
	//private static Random random = new Random (System.currentTimeMillis ());
	
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
