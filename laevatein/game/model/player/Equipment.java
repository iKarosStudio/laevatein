package laevatein.game.model.player;

import java.util.*;

import laevatein.game.model.*;
import laevatein.game.model.item.*;
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
	private HashMap<Integer, ItemInstance> equipment;
	
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
	

	public Equipment (SessionHandler _handle) {
		handle = _handle;
		pc = handle.user.activePc;
		equipment = new HashMap<Integer, ItemInstance> (15);
	}
	
	public Iterator<ItemInstance> getIterator () {
		Iterator<ItemInstance> result = equipment.values ().iterator ();
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
		handle.sendPacket (new UpdateItemName (e).getRaw ());
	}
	
	private void takeOff (int index, ItemInstance e) {
		e.isUsing = false;
		equipment.remove (index);
		handle.sendPacket (new UpdateItemName (e).getRaw ());
	}
	
	private void swapTo (int index, ItemInstance e) {
		ItemInstance prevE = equipment.get (index);
		prevE.isUsing = false;
		handle.sendPacket (new UpdateItemName (prevE).getRaw ());
		
		e.isUsing = true;
		equipment.replace (index, e);
		handle.sendPacket (new UpdateItemName (e).getRaw ());
		
	}
	
	public void setWeapon (ItemInstance weapon) {
		int prevActId = pc.actId;
		
		//職業可用性檢查
		if (!weapon.isClassUsable (pc.type)) {
			handle.sendPacket (new GameMessage (264).getRaw ());
			return;
		}
		
		//持有盾牌檢查
		if (weapon.isTwoHanded && equipment.containsKey (INDEX_SHIELD)) {
			handle.sendPacket (new GameMessage (128).getRaw ());
			return;			
		}
		
		//最低等級使用檢查
		if (pc.level < weapon.minLevel) {
			handle.sendPacket (new GameMessage (318, String.format ("%s", weapon.minLevel)).getRaw ());
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
			byte[] packet = new UpdateModelActId (pc.uuid, pc.actId).getRaw ();
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
					handle.sendPacket (new GameMessage (127).getRaw ());
				} else {
					takeOff (INDEX_ARMOR, e);
				}
				
			} else {
				//swpa
				handle.sendPacket (new GameMessage (124).getRaw ());
				
			}
		} else {
			//puton
			if (equipment.containsKey (INDEX_CLOAK)) {
				handle.sendPacket (new GameMessage (126, equipment.get (INDEX_CLOAK).name).getRaw ());
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
					handle.sendPacket (new GameMessage (127).getRaw ());
					return;
				}
				
				if (equipment.containsKey (INDEX_CLOAK)) {
					handle.sendPacket (new GameMessage (127).getRaw ());
					return;
				}
				
				takeOff (INDEX_TSHIRT, e);
				
			} else {
				//swap
				handle.sendPacket (new GameMessage (124).getRaw ());
			}
		} else {
			//puton
			if (equipment.containsKey (INDEX_CLOAK)) {
				handle.sendPacket (new GameMessage (126, equipment.get (INDEX_CLOAK).name).getRaw ());
				return;
			}
			
			if (equipment.containsKey (INDEX_ARMOR)) {
				handle.sendPacket (new GameMessage (126, equipment.get (INDEX_ARMOR).name).getRaw ());
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
				handle.sendPacket (new GameMessage (124).getRaw ());
			}
		} else {
			//puton
			if (equipment.containsKey (INDEX_WEAPON)) {
				if (equipment.get (INDEX_WEAPON).isTwoHanded) {
					handle.sendPacket (new GameMessage (129).getRaw ());
				} else {
					putOn (INDEX_SHIELD, e);
				}
			} else {
				putOn (INDEX_SHIELD, e);
			}
		}
	}
	
	
	private void setE (int index, ItemInstance e) {
		if (equipment.containsKey (index)) {
			if (equipment.get (index).uuid == e.uuid) {
				//take off
				takeOff (index, e);
				
			} else {
				//swap
				handle.sendPacket (new GameMessage (124).getRaw ());
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
				handle.sendPacket (new GameMessage (452, arrow.getName ()).getRaw ());
			}
		} else { //null->weapon
			putOn (INDEX_ARROW, arrow);
			handle.sendPacket (new GameMessage (452, arrow.getName ()).getRaw ());
		}
	}
	
	public void setSting (ItemInstance sting) {
		if (equipment.containsKey (INDEX_STING)) {
			if (equipment.get (INDEX_STING).uuid == sting.uuid) {
				takeOff (INDEX_STING, sting);
				
			} else {
				swapTo (INDEX_STING, sting);
				handle.sendPacket (new GameMessage (452, sting.getName ()).getRaw ());
			}
		} else {
			putOn (INDEX_STING, sting);
			handle.sendPacket (new GameMessage (452, sting.getName ()).getRaw ());
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
