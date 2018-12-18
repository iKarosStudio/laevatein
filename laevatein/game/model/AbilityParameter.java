package laevatein.game.model;

import laevatein.callback.*;
//import laevatein.game.model.player.*;

public class AbilityParameter
{	
	private Callback callback;
	
	public AbilityParameter (Callback callback) {
		this.callback = callback;
	}
	
	//力量
	int str;
	
	//敏捷
	int dex; 
	
	//體質
	int con;
	
	//精神
	int wis;
	
	//魅力
	int cha;
	
	//智力
	int intel;
	
	//魔法點數
	int sp;
	
	//魔法防禦
	int mr;
	
	//防禦(armor class)
	int ac;
	
	//最大HP增加
	int hp;
	
	//最大MP增加
	int mp;
	
	//HP回復量
	int hpr;
	
	//MP回復量
	int mpr;
	
	//火屬性防禦
	int resistFire;
	
	//水屬性防禦
	int resistWater;
	
	//風屬性防禦
	int resistWind;
	
	//地屬性防禦
	int resistEarth;
	
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

	//傷害減免
	int dmgReduction;
	
	//負重減免
	int weightReduction;
	
	public int getStr () {
		return str;
	}
	
	public void setStr (int str) {
		this.str = str;
		callback.updateStr ();
	}
	
	public int getDex () {
		return dex;
	}
	
	public void setDex (int dex) {
		this.dex = dex;
		callback.updateDex ();
	}
	
	public int getCon () {
		return con;
	}
	
	public void setCon (int con) {
		this.con = con;
		callback.updateCon ();
	}
	
	public int getWis () {
		return wis;
	}
	
	public void setWis (int wis) {
		this.wis = wis;
		callback.updateWis ();
	}
	
	public int getCha () {
		return cha;
	}
	
	public void setCha (int cha) {
		this.cha = cha;
		callback.updateCha ();
	}
	
	public int getInt () {
		return intel;
	}
	
	public void setInt (int intel) {
		this.intel = intel;
		callback.updateIntel ();
	}
	
	public int getSp () {
		return sp;
	}
	
	public void setSp (int sp) {
		this.sp = sp;
		callback.updateSpMr ();
	}
	
	public int getMr () {
		return mr;
	}
	
	public void setMr (int mr) {
		this.mr = mr;
		callback.updateSpMr ();
	}
	
	public int getAc () {
		return ac;
	}
	
	public void setAc (int ac) {
		this.ac = ac;
		callback.updateAc ();
	}
	
	public int getHp () {
		return hp;
	}
	
	public void setHp (int hp) {
		this.hp = hp;
		callback.updateMaxHp ();
	}
	
	public int getMp () {
		return mp;
	}
	
	public void setMp (int mp) {
		this.mp = mp;
		callback.updateMaxMp ();
	}
	
	public int getHpr () {
		return hpr;
	}
	
	public void setHpr (int hpr) {
		this.hpr = hpr;
	}
	
	public int getMpr () {
		return mpr;
	}
	
	public void setMpr (int mpr) {
		this.mpr = mpr;
	}
	
	public int getResistFire () {
		return resistFire;
	}
	
	public void setResistFire (int resistFire) {
		this.resistFire = resistFire;
	}
	
	public int getResistWater () {
		return resistWater;
	}
	
	public void setResistWater (int resistWater) {
		this.resistWater = resistWater;
	}
	
	public int getResistWind () {
		return resistWind;
	}
	
	public void setResistWind (int resistWind) {
		this.resistWind = resistWind;
	}
	
	public int getResistEarth () {
		return resistEarth;
	}
	
	public void setResistEarth (int resistEarth) {
		this.resistEarth = resistEarth;
	}
	
	public int getDmgReduction () {
		return dmgReduction;
	}
	
	public void setDmgReduction (int dmgReduction) {
		this.dmgReduction = dmgReduction;
	}
	
	public int getWeightReduction () {
		return weightReduction;
	}
	
	public void setWeightReduction (int weightReduction) {
		this.weightReduction = weightReduction;
	}
	
}
