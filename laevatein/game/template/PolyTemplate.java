package laevatein.game.template;

import java.util.*;

import static laevatein.game.template.ItemTypeTable.*;

public class PolyTemplate
{
	public final int id;
	public final String name;
	public final int polyId;
	public final int minLevel;
	public final int weaponEquipCode;
	public final int armorEquipCode;
	public final boolean isSkillCastable;
	
	//<K, V> => <item.minorType, usable>
	public HashMap<Integer, Boolean> weaponType = new HashMap<Integer, Boolean> (16);
	public HashMap<Integer, Boolean> armorType = new HashMap<Integer, Boolean> (11);
	
	public PolyTemplate (int _id, String _name, int _polyId, int _minLevel, int _weapon, int _armor, boolean _isSkillCastable) {
		id = _id;
		name = _name;
		polyId = _polyId;
		minLevel = _minLevel;
		weaponEquipCode = _weapon;
		armorEquipCode = _armor;
		isSkillCastable = _isSkillCastable;
		
		for (int index = 0; index <= 8; index++) { //main types of weapons			
			boolean usable = ((weaponEquipCode >>> index) & 0x01) > 0;
			
			switch (index) {
			case 0://dagger
				weaponType.put (WEAPON_TYPE_DAGGER, usable);
				break;
			case 1://sword
				weaponType.put (WEAPON_TYPE_SWORD, usable);
				break;
			case 2://two-handed sword
				weaponType.put (WEAPON_TYPE_TOHAND_SWORD, usable);
				break;
			case 3://axe
				weaponType.put (WEAPON_TYPE_BLUNT, usable);
				weaponType.put (WEAPON_TYPE_TOHAND_BLUNT, usable);
				break;
			case 4://spear
				weaponType.put (WEAPON_TYPE_SPEAR, usable);
				weaponType.put (WEAPON_TYPE_SINGLE_SPEAR, usable);
				break;
			case 5://staff
				weaponType.put (WEAPON_TYPE_STAFF, usable);
				weaponType.put (WEAPON_TYPE_TOHAND_STAFF, usable);
				break;
			case 6://edoryu
				weaponType.put (WEAPON_TYPE_EDORYU, usable);
				break;
			case 7://claw
				weaponType.put (WEAPON_TYPE_CLAW, usable);
				break;
			case 8://bow
				weaponType.put (WEAPON_TYPE_BOW, usable);
				weaponType.put (WEAPON_TYPE_SINGLE_BOW, usable);
				weaponType.put (WEAPON_TYPE_GAUNTLET, usable);
				break;
			default:
				break;
			}
		}
		
		for (int index = 0; index <= 10; index++) { //11 types of armors
			boolean usable = ((armorEquipCode >>> index) & 0x01) > 0;
			
			switch (index) {
			case 0://helm
				armorType.put (ARMOR_TYPE_HELM, usable);
				break;
			case 1://amlet
				armorType.put (ARMOR_TYPE_AMULET, usable);
				break;
			case 2://earring
				armorType.put (ARMOR_TYPE_EARRING, usable);
				break;
			case 3://tshirt
				armorType.put (ARMOR_TYPE_T, usable);
				break;
			case 4://armor
				armorType.put (ARMOR_TYPE_ARMOR, usable);
				break;
			case 5://cloak
				armorType.put (ARMOR_TYPE_CLOAK, usable);
				break;
			case 6://belt
				armorType.put (ARMOR_TYPE_BELT, usable);
				break;
			case 7://shield
				armorType.put (ARMOR_TYPE_SHIELD, usable);
				break;
			case 8://gloves
				armorType.put (ARMOR_TYPE_GLOVE, usable);
				break;
			case 9://ring
				armorType.put (ARMOR_TYPE_RING, usable);
				armorType.put (ARMOR_TYPE_RING2, usable);
				break;
			case 10://boots
				armorType.put (ARMOR_TYPE_BOOTS, usable);
				break;
			default:
				break;
			}
		}
	}
	
}
