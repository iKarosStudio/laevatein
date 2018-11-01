package laevatein.game.template;

public class ItemTypeTable
{
	/* WEAPON MINOR-TYPE */
	public static final int WEAPON_TYPE_SWORD = 1;
	public static final int WEAPON_TYPE_DAGGER = 2;
	public static final int WEAPON_TYPE_TOHAND_SWORD = 3;
	public static final int WEAPON_TYPE_BOW = 4;
	public static final int WEAPON_TYPE_SPEAR = 5;
	public static final int WEAPON_TYPE_BLUNT = 6;
	public static final int WEAPON_TYPE_STAFF = 7;
	public static final int WEAPON_TYPE_THROWING_KNIFE = 8;
	public static final int WEAPON_TYPE_ARROW = 9;
	public static final int WEAPON_TYPE_GAUNTLET = 10;
	public static final int WEAPON_TYPE_CLAW = 11;
	public static final int WEAPON_TYPE_EDORYU = 12;
	public static final int WEAPON_TYPE_SINGLE_BOW = 13;
	public static final int WEAPON_TYPE_SINGLE_SPEAR = 14;
	public static final int WEAPON_TYPE_TOHAND_BLUNT = 15;
	public static final int WEAPON_TYPE_TOHAND_STAFF = 16;
	
	public static final int WEAPON_GFX_HAND = 0;
	public static final int WEAPON_GFX_SWORD = 4;
	public static final int WEAPON_GFX_DAGGER = 46;
	public static final int WEAPON_GFX_TOHAND_SWORD = 50;
	public static final int WEAPON_GFX_BOW = 20;
	public static final int WEAPON_GFX_BLUNT = 11;
	public static final int WEAPON_GFX_SPEAR = 24;
	public static final int WEAPON_GFX_STAFF = 40;
	public static final int WEAPON_GFX_THROWING_KNIFE = 2922;
	public static final int WEAPON_GFX_ARROW = 66;
	public static final int WEAPON_GFX_GAUNTLET = 62;
	public static final int WEAPON_GFX_CLAW = 58;
	public static final int WEAPON_GFX_EDORYU = 54;
	public static final int WEAPON_GFX_SINGLE_BOW = 20;
	public static final int WEAPON_GFX_SINGLE_SPEAR = 24;
	public static final int WEAPON_GFX_TOHAND_BLUNT = 11;
	public static final int WEAPON_GFX_TOHAND_STAFF = 40;
	
	public static final int[] WeaponGfxTable = {
			WEAPON_GFX_HAND,
			WEAPON_GFX_SWORD,
			WEAPON_GFX_DAGGER,
			WEAPON_GFX_TOHAND_SWORD,
			WEAPON_GFX_BOW,
			WEAPON_GFX_BLUNT,
			WEAPON_GFX_SPEAR,
			WEAPON_GFX_STAFF,
			WEAPON_GFX_THROWING_KNIFE,
			WEAPON_GFX_ARROW,
			WEAPON_GFX_GAUNTLET,
			WEAPON_GFX_CLAW,
			WEAPON_GFX_EDORYU,
			WEAPON_GFX_SINGLE_BOW,
			WEAPON_GFX_SINGLE_SPEAR,
			WEAPON_GFX_TOHAND_BLUNT,
			WEAPON_GFX_TOHAND_STAFF } ;
	
	/* ARMOR MINOR-TYPE */
	public static final int ARMOR_TYPE_NONE = 0;
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
	
	/* ETC ITEM MINOR-TYPE */
	public static final int TYPE_ARROW = 0;
	public static final int TYPE_WAND = 1;
	public static final int TYPE_LIGHT = 2;
	public static final int TYPE_GEM = 3;
	public static final int TYPE_TOTEM = 4;
	public static final int TYPE_FIRECRACKER = 5;
	public static final int TYPE_POTION = 6;
	public static final int TYPE_FOOD = 7;
	public static final int TYPE_SCROLL = 8;
	public static final int TYPE_QUEST_ITEM = 9;
	public static final int TYPE_SPELL_BOOK = 10;
	public static final int TYPE_PET_ITEM = 11;
	public static final int TYPE_OTHER = 12;
	public static final int TYPE_MATERIAL = 13;
	public static final int TYPE_EVENT = 14;
	public static final int TYPE_STING = 15;
	
	/* USE-TYPE */
	public static final int TYPE_USE_NONE = -1;
	public static final int TYPE_USE_NORMAL = 0;
	public static final int TYPE_USE_WEAPON = 1;
	public static final int TYPE_USE_ARMOR = 2;
	public static final int TYPE_USE_SPELL_LONG = 5;
	public static final int TYPE_USE_NTELE = 6;
	public static final int TYPE_USE_IDENTIFY = 7;
	public static final int TYPE_USE_RES = 8;
	public static final int TYPE_USE_LETTER = 12;
	public static final int TYPE_USE_LETTER_W = 13;
	public static final int TYPE_USE_CHOICE = 14;
	public static final int TYPE_USE_INSTRUMENT = 15;
	public static final int TYPE_USE_SOSC = 16;
	public static final int TYPE_USE_SPELL_SHORT = 17;
	public static final int TYPE_USE_T = 18;
	public static final int TYPE_USE_CLOAK = 19;
	public static final int TYPE_USE_GLOVE = 20;
	public static final int TYPE_USE_BOOTS = 21;
	public static final int TYPE_USE_HELM = 22;
	public static final int TYPE_USE_RING = 23;
	public static final int TYPE_USE_AMULET = 24;
	public static final int TYPE_USE_SHIELD = 25;
	public static final int TYPE_USE_DAI = 26;
	public static final int TYPE_USE_ZEL = 27;
	public static final int TYPE_USE_BLANK = 28;
	public static final int TYPE_USE_BTELE = 29;
	public static final int TYPE_USE_SPELL_BUFF = 30;
	public static final int TYPE_USE_CCARD = 31;
	public static final int TYPE_USE_CCARD_W = 32;
	public static final int TYPE_USE_VCARD = 33;
	public static final int TYPE_USE_VCARD_W = 34;
	public static final int TYPE_USE_WCARD = 35;
	public static final int TYPE_USE_WCARD_W = 36;
	public static final int TYPE_USE_BELT = 37;
	public static final int TYPE_USE_EARRING = 40;
	public static final int TYPE_USE_FISHING_ROD = 42;
	
	public static final int MATERIAL_NONE = 0;
	public static final int MATERIAL_LIQUID = 1;
	public static final int MATERIAL_WEB = 2;
	public static final int MATERIAL_VEGETATION = 3;
	public static final int MATERIAL_ANIMALMATTER = 4;
	public static final int MATERIAL_PAPER = 5;
	public static final int MATERIAL_CLOTH = 6;
	public static final int MATERIAL_LEATHER = 7;
	public static final int MATERIAL_WOOD = 8;
	public static final int MATERIAL_BONE = 9;
	public static final int MATERIAL_DRAGONSCALE = 10;
	public static final int MATERIAL_IRON = 11;
	public static final int MATERIAL_STEEL = 12;
	public static final int MATERIAL_COPPER = 13;
	public static final int MATERIAL_SILVER = 14;
	public static final int MATERIAL_GOLD = 15;
	public static final int MATERIAL_PLATINUM = 16;
	public static final int MATERIAL_MITHRIL = 17;
	public static final int MATERIAL_BLACKMITHRIL = 18;
	public static final int MATERIAL_GLASS = 19;
	public static final int MATERIAL_MINERAL = 20;
	public static final int MATERIAL_ORIHARUKON = 21;
	
	public static int ArmorMinorType2UseType (int minor_type) {
		switch (minor_type) {
			case ARMOR_TYPE_HELM  : return TYPE_USE_HELM;
			case ARMOR_TYPE_ARMOR : return TYPE_USE_ARMOR;
			case ARMOR_TYPE_T     : return TYPE_USE_T;
			case ARMOR_TYPE_CLOAK : return TYPE_USE_CLOAK;
			case ARMOR_TYPE_GLOVE : return TYPE_USE_GLOVE;
			case ARMOR_TYPE_BOOTS : return TYPE_USE_BOOTS;
			case ARMOR_TYPE_SHIELD: return TYPE_USE_SHIELD;
			case ARMOR_TYPE_AMULET: return TYPE_USE_AMULET;
			case ARMOR_TYPE_RING  : return TYPE_USE_RING;
			case ARMOR_TYPE_RING2 : return TYPE_USE_RING;
			case ARMOR_TYPE_BELT  : return TYPE_USE_BELT;
			case ARMOR_TYPE_EARRING:return TYPE_USE_EARRING;
			default : return TYPE_USE_NONE;
		}
	}
}
