package laevatein.game.model.player;

import java.util.*;

import laevatein.game.model.*;
import laevatein.game.model.item.*;
import laevatein.server.process_server.UpdateModelGfx;

import static laevatein.game.template.ItemTypeTable.*;

public class Equipment implements ApAccessable
{
	public static final int INDEX_WEAPON = 0; 
	public static final int INDEX_HELM = ARMOR_TYPE_HELM; //1
	public static final int INDEX_ARMOR = ARMOR_TYPE_ARMOR; //2
	public static final int INDEX_TSHIRT = ARMOR_TYPE_T; //3
	public static final int INDEX_CLOAK = ARMOR_TYPE_CLOAK; //4
	public static final int INDEX_GLOVE = ARMOR_TYPE_GLOVE; //5
	public static final int INDEX_BOOTS = ARMOR_TYPE_BOOTS; //6
	public static final int INDEX_SHIELD = ARMOR_TYPE_SHIELD; //7
	public static final int INDEX_AMULET = ARMOR_TYPE_AMULET; //8
	public static final int INDEX_RING1 = ARMOR_TYPE_RING; //9
	public static final int INDEX_BELT = ARMOR_TYPE_BELT; //10
	public static final int INDEX_RING2 = ARMOR_TYPE_RING2; //11
	public static final int INDEX_EARRING = ARMOR_TYPE_EARRING; //12
	public static final int INDEX_ARROW = 13;
	public static final int INDEX_STING = 14;
	
	private ArrayList<ItemInstance> equipment;
	
	/*
	public static final int ARMOR_TYPE_HELM = 1;
	public static final int ARMOR_TYPE_ARMOR = 2;
	public static final int ARMOR_TYPE_T = 3;
	public static final int ARMOR_TYPE_CLOAK = 4;
	public static final int ARMOR_TYPE_GLOVE = 5;
	public static final int ARMOR_TYPE_BOOTS = 6;
	public static final int ARMOR_TYPE_SHIELD = 7;
	public static final int ARMOR_TYPE_AMULET = 8;
	public static final int ARMOR_TYPE_RING = 9;
	public static final int ARMOR_TYPE_BELT = 10;
	public static final int ARMOR_TYPE_RING2 = 11;
	public static final int ARMOR_TYPE_EARRING = 12;
	*/
	
	//$318:等級?以上才可以使用
	//$673:等級?以下才可以使用 
	//$264:你的職業無法使用
	
	//$124:已經裝備其他東西
	//$127:你不能脫掉那個
	//$128:拿著盾時不能使用雙手武器
	//$129:使用雙手武器不能使用盾牌
	
	//$230:燈籠加了新的燈油
	//$822:你感受到體內深處產生一股不明力量
	//$166:
	

	public Equipment () {
		equipment = new ArrayList<ItemInstance> (15);
	}
	
	public List<ItemInstance> toList () {
		return equipment;
	}
	
	public void setWeapon (ItemInstance weapon) {
		if (equipment.get (INDEX_WEAPON) == null) { //空手->weapon
			
		} else {
			if (equipment.get (INDEX_WEAPON).uuid == weapon.uuid) { //weapon->空手
				
			} else { //weapon交換
				
			}
		}
		
		//new UpdateModelGfx (pc.uuid, equipment.get (INDEX_WEAPON).actId).getRaw ();
	}
	
	public void setArmor (ItemInstance armor) {
	}
	
	public void setArrow (ItemInstance arrow) {
	}
	
	public void setSting (ItemInstance sting) {
	}
	
	public void useArrow () {
		//Arrow --;
	}
	
	public void useSting () {
	}
	
	public void useArrow (int amount) { //for triple shot
		//Arrow -= amount;
	}
	
	public void useSting (int amount) {
	}
	
	@Override
	public int getStr () {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getCon () {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getDex () {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getWis () {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getCha () {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getIntel () {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMaxHp () {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMaxMp () {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSp () {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMr () {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getAc () {
		// TODO Auto-generated method stub
		return 0;
	}

}
