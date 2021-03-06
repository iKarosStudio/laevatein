package laevatein.game.model.player;

import java.util.*;
import java.util.concurrent.*;

import laevatein.game.skill.*;
import laevatein.game.model.*;
import laevatein.game.model.item.*;
import laevatein.game.template.*;
import laevatein.server.*;
import laevatein.server.process_server.*;

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
	
	private SessionHandler handle;
	private PcInstance pc;
	private ConcurrentHashMap<Integer, ItemInstance> equipment;
	
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
	

	public Equipment (SessionHandler _handle) {
		handle = _handle;
		pc = handle.getUser().getActivePc ();
		equipment = new ConcurrentHashMap<Integer, ItemInstance> (15);
	}
	
	public List<ItemInstance> getList () {
		List<ItemInstance> result = new ArrayList<ItemInstance> (equipment.values ());
		return result;
	}
	
	public Iterator<ItemInstance> getIterator () {
		Iterator<ItemInstance> result = equipment.values ().iterator ();
		return result;
	}
	
	public AbilityParameter getAbilities () {
		AbilityParameter result = new AbilityParameter (pc.getCallback ());
		for (ItemInstance e : getList ()) {
			result.setStr (result.getStr () + e.str);
			result.setCon (result.getCon () + e.con);
			result.setDex (result.getDex () + e.dex);
			result.setWis (result.getWis () + e.wis);
			result.setCha (result.getCha () + e.cha);
			result.setInt (result.getInt () + e.intel);
			
			result.setSp (result.getSp () + e.sp);
			result.setMr (result.getMr () + e.sp);
			
			result.setHp (result.getHp () + e.hp);
			result.setMp (result.getMp () + e.mp);

			result.setHpr (result.getHpr () + e.hpr);
			result.setMpr (result.getMpr () + e.mpr);
			
			result.setAc (result.getAc () + e.ac);
			
			result.setDmgReduction (result.getDmgReduction () + e.dmgReduction);
			result.setWeightReduction (result.getWeightReduction () + e.weightReduction);
		}
		
		return result;
	}
	
	public void set (ItemInstance e) {
		switch (e.minorType) {		
		case ARMOR_TYPE_ARMOR:
			equipment.put (INDEX_ARMOR, e);
			break;
			
		case ARMOR_TYPE_T:
			equipment.put (INDEX_TSHIRT, e);
			break;
			
		case ARMOR_TYPE_SHIELD:
			equipment.put (INDEX_SHIELD, e);
			break;
			
		case ARMOR_TYPE_CLOAK:
			equipment.put (INDEX_CLOAK, e);
			break;
			
		case ARMOR_TYPE_GLOVE:
			equipment.put (INDEX_GLOVE, e);
			break;
		
		case ARMOR_TYPE_BOOTS:
			equipment.put (INDEX_BOOTS, e);
			break;
		
		case ARMOR_TYPE_HELM:
			equipment.put (INDEX_HELM, e);
			break;
			
		case ARMOR_TYPE_AMULET:
			equipment.put (INDEX_AMULET, e);
			break;
			
		case ARMOR_TYPE_RING:
			equipment.put (INDEX_RING1, e);
			break;
		
		case ARMOR_TYPE_RING2:
			equipment.put (INDEX_RING2, e);
			break;
			
		case ARMOR_TYPE_BELT:
			equipment.put (INDEX_BELT, e);
			break;
			
		case ARMOR_TYPE_EARRING:
			equipment.put (INDEX_EARRING, e);
			break;
			
		default:
			break;
		}
	}
	
	private void putOn (int index, ItemInstance e) {
		e.isUsing = true;
		equipment.put (index, e);
		handle.sendPacket (new UpdateItemName (e).getPacket ());
		
		if (e.isHasteEffect) {
			pc.addSkillEffect (SkillId.STATUS_HASTE, 0xFFFF);
		}
	}
	
	private void takeOff (int index, ItemInstance e) {
		e.isUsing = false;
		equipment.remove (index);
		handle.sendPacket (new UpdateItemName (e).getPacket ());
		
		if (e.isHasteEffect) {
			pc.removeSkillEffect (SkillId.STATUS_HASTE);
		}
	}
	
	private void swapTo (int index, ItemInstance e) {
		ItemInstance prevE = equipment.get (index);
		prevE.isUsing = false;
		handle.sendPacket (new UpdateItemName (prevE).getPacket ());
		
		e.isUsing = true;
		equipment.replace (index, e);
		handle.sendPacket (new UpdateItemName (e).getPacket ());
		
		if (e.isHasteEffect) {
			pc.addSkillEffect (SkillId.STATUS_HASTE, 0xFFFF);
		}
	}
	
	public void setWeapon (ItemInstance weapon) {
		int prevActId = pc.actId;
		//職業可用性檢查
		if (!weapon.isClassUsable (pc.type)) {
			handle.sendPacket (new GameMessage (264).getPacket ());
			return;
		}
		
		//持有盾牌檢查
		if (weapon.isTwoHanded && equipment.containsKey (INDEX_SHIELD)) {
			handle.sendPacket (new GameMessage (128).getPacket ());
			return;			
		}
		
		//最低等級使用檢查
		if (pc.getLevel () < weapon.minLevel) {
			handle.sendPacket (new GameMessage (318, String.format ("%s", weapon.minLevel)).getPacket ());
			return;	
		}
		
		//最高等級使用檢查
		//if (pc.level > weapon.maxLevel) {
		//	handle.sendPacket (new GameMessage (673, String.format ("%s", weapon.maxLevel)).getRaw ());
		//	return;	
		//}
		
		//換裝備
		if (equipment.containsKey (INDEX_WEAPON)) {
			if (equipment.get (INDEX_WEAPON).uuid == weapon.uuid) { //weapon->null
				takeOff (INDEX_WEAPON, weapon);
				pc.actId = 0;
				
			} else { //weapon1->weapon2
				swapTo (INDEX_WEAPON, weapon);
				pc.actId = weapon.actId;
			}
		} else { //null->weapon
			putOn (INDEX_WEAPON, weapon);
			pc.actId = weapon.actId;
		}
		
		//改變角色外型(若需要)
		if (prevActId != pc.actId) {
			byte[] packet = new UpdateModelActId (pc.getUuid (), pc.actId).getPacket ();
			handle.sendPacket (packet);
			pc.boardcastPcInsight (packet);
		}
	}
	
	public void setEquipment (ItemInstance e) {	
		
		switch (e.minorType) {
		case ARMOR_TYPE_ARMOR:
			setArmor (e);
			break;
			
		case ARMOR_TYPE_T:
			setTshirt (e);
			break;
			
		case ARMOR_TYPE_SHIELD:
			setShield (e);
			break;
		
		//
			
		case ARMOR_TYPE_CLOAK:
			setE (INDEX_CLOAK, e);
			break;
			
		case ARMOR_TYPE_GLOVE:
			setE (INDEX_GLOVE, e);
			break;
		
		case ARMOR_TYPE_BOOTS:
			setE (INDEX_BOOTS, e);
			break;
		
		case ARMOR_TYPE_HELM:
			setE (INDEX_HELM, e);
			break;
			
		case ARMOR_TYPE_AMULET:
			setE (INDEX_AMULET, e);
			break;
			
		case ARMOR_TYPE_RING:
			setE (INDEX_RING1, e);
			break;
		
		case ARMOR_TYPE_RING2:
			setE (INDEX_RING2, e);
			break;
			
		case ARMOR_TYPE_BELT:
			setE (INDEX_BELT, e);
			break;
			
		case ARMOR_TYPE_EARRING:
			setE (INDEX_EARRING, e);
			break;
			
		default:
			break;
		}
	}
	
	private void setArmor (ItemInstance e) {		
		if (equipment.containsKey (INDEX_ARMOR)) {
			if (equipment.get (INDEX_ARMOR).uuid == e.uuid) {
				//take off
				if (equipment.containsKey (INDEX_CLOAK)) {
					handle.sendPacket (new GameMessage (127).getPacket ());
				} else {
					takeOff (INDEX_ARMOR, e);
				}
				
			} else {
				//swpa
				handle.sendPacket (new GameMessage (124).getPacket ());
				
			}
		} else {
			//puton
			if (equipment.containsKey (INDEX_CLOAK)) {
				handle.sendPacket (new GameMessage (126, e.name, equipment.get (INDEX_CLOAK).name).getPacket ());
			} else {
				putOn (INDEX_ARMOR, e);
			}
			
		}
	}
	
	private void setTshirt (ItemInstance e) {
		if (equipment.containsKey (INDEX_TSHIRT)) {
			if (equipment.get (INDEX_TSHIRT).uuid == e.uuid) {
				//take off
				if (equipment.containsKey (INDEX_ARMOR)) {
					handle.sendPacket (new GameMessage (127).getPacket ());
					return;
				}
				
				if (equipment.containsKey (INDEX_CLOAK)) {
					handle.sendPacket (new GameMessage (127).getPacket ());
					return;
				}
				
				takeOff (INDEX_TSHIRT, e);
				
			} else {
				//swap
				handle.sendPacket (new GameMessage (124).getPacket ());
			}
		} else {
			//puton
			if (equipment.containsKey (INDEX_CLOAK)) {
				handle.sendPacket (new GameMessage (126, e.name, equipment.get (INDEX_ARMOR).name).getPacket ());
				return;
			}
			
			if (equipment.containsKey (INDEX_ARMOR)) {
				handle.sendPacket (new GameMessage (126, e.name, equipment.get (INDEX_CLOAK).name).getPacket ());
				return;
			}
			
			putOn (INDEX_TSHIRT, e);
		}
	}
	
	private void setShield (ItemInstance e) {
		if (equipment.containsKey (INDEX_SHIELD)) {
			if (equipment.get (INDEX_SHIELD).uuid == e.uuid) {
				//takeoff
				takeOff (INDEX_SHIELD, e);
			} else {
				//swap
				handle.sendPacket (new GameMessage (124).getPacket ());
			}
		} else {
			//puton
			if (equipment.containsKey (INDEX_WEAPON)) {
				if (equipment.get (INDEX_WEAPON).isTwoHanded) {
					handle.sendPacket (new GameMessage (129).getPacket ());
				} else {
					putOn (INDEX_SHIELD, e);
				}
			} else {
				putOn (INDEX_SHIELD, e);
			}
		}
	}
	
	
	private void setE (int index, ItemInstance e) {
		System.out.printf ("index:%d\n", index) ;
		if (equipment.containsKey (index)) {
			if (equipment.get (index).uuid == e.uuid) {
				//take off
				takeOff (index, e);
				
			} else {
				//swap
				handle.sendPacket (new GameMessage (124).getPacket ());
			}
		} else {
			//puton
			putOn (index, e);
		}
	}
	
	
	public void setArrow (ItemInstance arrow) {
		if (equipment.containsKey (INDEX_ARROW)) {
			if (equipment.get (INDEX_ARROW).uuid == arrow.uuid) { //weapon->null
				takeOff (INDEX_ARROW, arrow);
				
			} else { //weapon1->weapon2
				swapTo (INDEX_ARROW, arrow);
				handle.sendPacket (new GameMessage (452, arrow.getName ()).getPacket ());
			}
		} else { //null->weapon
			putOn (INDEX_ARROW, arrow);
			handle.sendPacket (new GameMessage (452, arrow.getName ()).getPacket ());
		}
	}
	
	public void setSting (ItemInstance sting) {
		if (equipment.containsKey (INDEX_STING)) {
			if (equipment.get (INDEX_STING).uuid == sting.uuid) {
				takeOff (INDEX_STING, sting);
				
			} else {
				swapTo (INDEX_STING, sting);
				handle.sendPacket (new GameMessage (452, sting.getName ()).getPacket ());
			}
		} else {
			putOn (INDEX_STING, sting);
			handle.sendPacket (new GameMessage (452, sting.getName ()).getPacket ());
		}
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
	
	//套裝確認 => private
	
	@Override
	public int getStr () {
		int result = 0;
		for (ItemInstance e : getList ()) {
			result += e.str;
		}
		return result;
	}

	@Override
	public int getCon () {
		int result = 0;
		for (ItemInstance e : getList ()) {
			result += e.con;
		}
		return result;
	}

	@Override
	public int getDex () {
		int result = 0;
		for (ItemInstance e : getList ()) {
			result += e.dex;
		}
		return result;
	}

	@Override
	public int getWis () {
		int result = 0;
		for (ItemInstance e : getList ()) {
			result += e.wis;
		}
		return result;
	}

	@Override
	public int getCha () {
		int result = 0;
		for (ItemInstance e : getList ()) {
			result += e.cha;
		}
		return result;
	}

	@Override
	public int getIntel () {
		int result = 0;
		for (ItemInstance e : getList ()) {
			result += e.intel;
		}
		return result;
	}

	@Override
	public int getMaxHp () {
		int result = 0;
		for (ItemInstance e : getList ()) {
			result += e.hp;
		}
		return result;
	}

	@Override
	public int getMaxMp () {
		int result = 0;
		for (ItemInstance e : getList ()) {
			result += e.mp;
		}
		return result;
	}
	
	@Override
	public int getHpR () {
		int result = 0;
		for (ItemInstance e : getList ()) {
			result += e.hpr;
		}
		return result;
	}

	@Override
	public int getMpR () {
		int result = 0;
		for (ItemInstance e : getList ()) {
			result += e.mpr;
		}
		return result;
	}

	@Override
	public int getSp () {
		int result = 0;
		for (ItemInstance e : getList ()) {
			result += e.sp;
		}
		return result;
	}

	@Override
	public int getMr () {
		int result = 0;
		for (ItemInstance e : getList ()) {
			result += e.mr;
		}
		return result;
	}

	@Override
	public int getAc () {
		int result = 0;
		for (ItemInstance e : getList ()) {
			result += e.ac;
		}
		return result;
	}
	
	public void fitPoly (PolyTemplate poly) {
		equipment.forEachValue (1, (ItemInstance e)->{
			if (e.isWeapon ()) {
				if (poly.weaponType.get (e.minorType) == false) {
					System.out.printf ("不能用的武器類型%d:%s\n", e.minorType, e.name);
					setWeapon (e); //take off
				}
			} else if (e.isArmor ()) {
				if (poly.armorType.get (e.minorType) == false) {
					takeOff (e.minorType, e);
				}
			}
		});
	}

}
