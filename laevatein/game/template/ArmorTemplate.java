package laevatein.game.template;

import laevatein.server.packet.*;
import static laevatein.game.template.ItemTypeTable.*;

public class ArmorTemplate extends ItemTemplate
{
	public int ac;
	public int safeEnchant;
	public boolean isRoyalUsable;
	public boolean isKnightUsable;
	public boolean isWizardUsable;
	public boolean isElfUsable;
	public boolean isDarkelfUsable;
	public int str;
	public int con;
	public int dex;
	public int intel;
	public int wis;
	public int cha;
	public int hp;
	public int mp;
	public int hpr;
	public int mpr;
	public int sp;
	public int mr;
	public boolean isHasteItem;
	public int dmgReduction;
	public int weightReduction;
	public int bowHitRate;
	public int defenseWater;
	public int defenseWind;
	public int defenseFire;
	public int defenseEarth;
	public int resistStan;
	public int resistStone;
	public int resistSleep;
	public int resistFreeze;
	
	public ArmorTemplate (
		int _itemId,
		String _name,
		String _nameId,
		String _type,
		String _material,
		int _weight,
		int _gfxInBag,
		int _gfxOnGround,
		int _descriptId,
		int _ac,
		int _safeEnchant,
		boolean _isRoyalUsable,
		boolean _isKnightUsable,
		boolean _isWizardUsable,
		boolean _isElfUsable,
		boolean _isDarkelfUsable,
		int _str,
		int _con,
		int _dex,
		int _intel,
		int _wis,
		int _cha,
		int _hp,
		int _mp,
		int _hpr,
		int _mpr,
		int _sp,
		int _minLevel,
		int _maxLevel,
		int _mr,
		boolean _isHasteItem,
		int _dmgReduction,
		int _weightReduction,
		int _bowHitRate,
		int _bless,
		boolean _isTradeable,
		int _defenseWater,
		int _defenseWind,
		int _defenseFire,
		int _defenseEarth,
		int _resistStan,
		int _resistStone,
		int _resistSleep,
		int _resistFreeze) {
		
		id = _itemId;
		name = _name;
		nameId = _nameId;
		typeName = _type;
		materialName = _material;
		weight = _weight;
		gfxInBag = _gfxInBag;
		gfxOnGround = _gfxOnGround;
		descriptId = _descriptId;
		ac = _ac;
		safeEnchant = _safeEnchant;
		isRoyalUsable = _isRoyalUsable;
		isKnightUsable = _isKnightUsable;
		isWizardUsable = _isWizardUsable;
		isElfUsable = _isElfUsable;
		isDarkelfUsable = _isDarkelfUsable;
		str = _str;
		con = _con;
		dex = _dex;
		intel = _intel;
		wis = _wis;
		cha = _cha;
		hp = _hp;
		mp = _mp;
		hpr = _hpr;
		mpr = _mpr;
		sp = _sp;
		minLevel = _minLevel;
		maxLevel = _maxLevel;
		mr = _mr;
		isHasteItem = _isHasteItem;
		dmgReduction = _dmgReduction;
		weightReduction = _weightReduction;
		bowHitRate = _bowHitRate;
		bless = _bless;
		isTradeable = _isTradeable;
		defenseWater = _defenseWater;
		defenseWind = _defenseWind;
		defenseFire = _defenseFire;
		defenseEarth = _defenseEarth;
		resistStan = _resistStan;
		resistStone = _resistStone;
		resistSleep = _resistSleep;
		resistFreeze = _resistFreeze;
		
		switch (typeName) {
		case "none" :
			minorType = ARMOR_TYPE_NONE;
			break;
		case "helm" :
			minorType = ARMOR_TYPE_HELM;
			break;
		case "armor" :
			minorType = ARMOR_TYPE_ARMOR;
			break;
		case "T" :
			minorType = ARMOR_TYPE_T;
			break;
		case "cloak" :
			minorType = ARMOR_TYPE_CLOAK;
			break;
		case "glove" :
			minorType = ARMOR_TYPE_GLOVE;
			break;
		case "boots" :
			minorType = ARMOR_TYPE_BOOTS;
			break;
		case "shield" :
			minorType = ARMOR_TYPE_SHIELD;
			break;
		case "amulet" :
			minorType = ARMOR_TYPE_AMULET;
			break;
		case "ring" :
			minorType = ARMOR_TYPE_RING;
			break;
		case "belt" :
			minorType = ARMOR_TYPE_BELT;
			break;
		case "ring2" :
			minorType = ARMOR_TYPE_RING2;
			break;
		case "earring" :
			minorType = ARMOR_TYPE_EARRING;
			break;
		default :
			minorType = 0xFF;
			break;
		}
		
		switch (materialName) {
		case "none" :
			material = MATERIAL_NONE;
			break;
		case "liquid" :
			material = MATERIAL_LIQUID;
			break;
		case "web" :
			material = MATERIAL_WEB;
			break;
		case "vegetation" :
			material = MATERIAL_VEGETATION;
			break;
		case "animalmetter" :
			material = MATERIAL_ANIMALMATTER;
			break;
		case "paper" :
			material = MATERIAL_PAPER;
			break;
		case "cloth" :
			material = MATERIAL_CLOTH;
			break;
		case "leather" :
			material = MATERIAL_LEATHER;
			break;
		case "wood" :
			material = MATERIAL_WOOD;
			break;
		case "bone" :
			material = MATERIAL_BONE;
			break;
		case "dragonscale" :
			material = MATERIAL_DRAGONSCALE;
			break;
		case "iron" :
			material = MATERIAL_IRON;
			break;
		case "steel" :
			material = MATERIAL_STEEL;
			break;
		case "copper" :
			material = MATERIAL_COPPER;
			break;
		case "silver" :
			material = MATERIAL_SILVER;
			break;
		case "gold" :
			material = MATERIAL_GOLD;
			break;
		case "platinum" :
			material = MATERIAL_PLATINUM;
			break;
		case "mithril" :
			material = MATERIAL_MITHRIL;
			break;
		case "blackmithril" :
			material = MATERIAL_BLACKMITHRIL;
			break;
		case "glass" :
			material = MATERIAL_GLASS;
			break;
		case "mineral" :
			material = MATERIAL_MINERAL;
			break;
		case "oriharukon" :
			material = MATERIAL_ORIHARUKON;
			break;
		default: 
			material = 0xFF;
			break;
		}
	}
	
	public byte[] ParseArmorDetail () {
		PacketBuilder packet = new PacketBuilder ();
		
		packet.writeByte (19) ;
		packet.writeByte (Math.abs (ac) ) ;
		packet.writeByte (material) ;
		packet.writeDoubleWord (weight / 1000) ;
		
		//
		packet.writeByte (2) ; //Enchant level
		packet.writeByte (0) ;
		
		packet.writeByte (3) ; //Durability
		packet.writeByte (100) ;
		
		byte UseClass = 0;
		if (isRoyalUsable) UseClass   |= 0x01;
		if (isKnightUsable) UseClass  |= 0x02;
		if (isElfUsable) UseClass     |= 0x04;
		if (isWizardUsable) UseClass  |= 0x08;
		if (isDarkelfUsable) UseClass |= 0x10;
		packet.writeByte (7) ; //use class
		packet.writeByte (UseClass);

		
		if (bowHitRate > 0) {
			packet.writeByte (24);
			packet.writeByte (bowHitRate);
		}
		
		if (str > 0) {
			packet.writeByte (8);
			packet.writeByte (str);
		}
		
		if (dex > 0) {
			packet.writeByte (9);
			packet.writeByte (dex);
		}
		
		if (con > 0) {
			packet.writeByte (10);
			packet.writeByte (con);
		}
		
		if (intel > 0) {
			packet.writeByte (11);
			packet.writeByte (intel);
		}
		
		if (wis > 0) {
			packet.writeByte (12);
			packet.writeByte (wis);
		}
		
		if (cha > 0) {
			packet.writeByte (13);
			packet.writeByte (cha);
		}
		
		if (hp > 0) {
			packet.writeByte (31);
			packet.writeByte (hp);
		}
		
		if (mp > 0) {
			packet.writeByte (32);
			packet.writeByte (mp);
		}
		
		if (mr > 0) {
			packet.writeByte (15);
			packet.writeWord (mr);
		}
		
		if (sp > 0) {
			packet.writeByte (17);
			packet.writeWord (sp);
		}
		
		if (isHasteItem) {
			packet.writeByte (18);
		}
		
		if (defenseFire > 0) {
			packet.writeByte (27);
			packet.writeByte (defenseFire);
		}
		
		if (defenseWater > 0) {
			packet.writeByte (28);
			packet.writeByte (defenseWater);
		}
		
		if (defenseWind > 0) {
			packet.writeByte (29);
			packet.writeByte (defenseWind);
		}
		
		if (defenseEarth > 0) {
			packet.writeByte (30);
			packet.writeByte (defenseEarth);
		}
		
		return packet.getPacketNoPadding ();
	}
}
