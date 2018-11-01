package laevatein.game.model;

public class AbilityParameter
{
	/* Str:力量 Dex:敏捷 Con:體質 Wis:精神 Cha:魅力 Intel:智力 */
	public int str, dex, con, wis, cha, intel;
	
	/* Sp:額外魔法點數 Mr:魔法防禦 */
	public int sp, mr;
	
	/* Ac:防禦(Armor Class) */
	public int ac;
	public int maxHp, maxMp, hpR, mpR;
	public int defFire, defWater, defWind, defEarth;
	//resists
	
	
	/*
	 * 額外攻擊點數修正
	 * 額外命中修正
	 * 魔法攻擊修正
	 * 弓箭命中修正
	 */
	//public int dmgModify;
	//public int hitModify;
	//public int spModify;
	//public int bowHitModify;
	
	/*
	 * 傷害減免修正
	 * 負重減免修正
	 */
	public int dmgReduction;
	public int weightReduction;
}
