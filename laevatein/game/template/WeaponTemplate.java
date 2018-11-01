package laevatein.game.template;

import laevatein.server.packet.*;
import static laevatein.game.template.ItemTypeTable.*;

public class WeaponTemplate extends ItemTemplate
{	
	public int actId = 0;
	
	public int safeEnchant;
	public boolean isRoyalUsable;
	public boolean isKnightUsable;
	public boolean isWizardUsable;
	public boolean isElfUsable;
	public boolean isDarkelfUsable;
	public int hitModify;
	public int dmgModify;
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
	public boolean isManaDrain;
	public boolean isHasteItem;
	public int magicDmgModify;
	public boolean isDamageable;
	
	public WeaponTemplate (
		int _itemId,
		String _name,
		String _nameId,
		String _type,
		String _material,
		int _weight,
		int _gfxInBag,
		int _gfxOnGround,
		int _descriptId,
		int _dmgSmall,
		int _dmgLarge,
		int _safeEnchant,
		boolean _isRoyalUsable,
		boolean _isKnightUsable,
		boolean _isWizardUsable,
		boolean _isElfUsable,
		boolean _isDarkelfUsable,
		int _hitModify,
		int _dmgModify,
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
		int _mr,
		boolean _isHasteItem,
		int _magicDmgModify,
		boolean _isDamageable,
		int _minLevel,
		int _maxLevel,
		int _bless,
		boolean _isTradeable,
		boolean _isManaDrain) {
		
		id = _itemId;
		name = _name;
		nameId = _nameId;
		typeName = _type;
		materialName = _material;
		weight = _weight;
		gfxInBag = _gfxInBag;
		gfxOnGround = _gfxOnGround;
		descriptId = _descriptId;
		dmgSmall = _dmgSmall;
		dmgLarge = _dmgLarge;
		safeEnchant = _safeEnchant;
		isRoyalUsable = _isRoyalUsable;
		isKnightUsable = _isKnightUsable;
		isWizardUsable = _isWizardUsable;
		isElfUsable = _isElfUsable;
		isDarkelfUsable = _isDarkelfUsable;
		hitModify = _hitModify;
		dmgModify = _dmgModify;
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
		mr = _mr;
		isHasteItem = _isHasteItem;
		magicDmgModify = _magicDmgModify;
		isDamageable = _isDamageable;
		minLevel = _minLevel;
		maxLevel = _maxLevel;
		bless = _bless;
		isTradeable = _isTradeable;
		isManaDrain = _isManaDrain;
		
		switch (typeName) {
		case "sword" :
			minorType = WEAPON_TYPE_SWORD; 
			actId = WEAPON_GFX_SWORD;
			break;
		case "dagger" :
			minorType = WEAPON_TYPE_DAGGER; 
			actId = WEAPON_GFX_DAGGER;
			break;
		case "tohandsword" :
			minorType = WEAPON_TYPE_TOHAND_SWORD;
			actId = WEAPON_GFX_TOHAND_SWORD;
			break;
		case "bow" :
			minorType = WEAPON_TYPE_BOW; 
			actId = WEAPON_GFX_BOW;
			break;
		case "spear" :
			minorType = WEAPON_TYPE_SPEAR;
			actId = WEAPON_GFX_SPEAR;
			break;
		case "blunt" :
			minorType = WEAPON_TYPE_BLUNT;
			actId = WEAPON_GFX_BLUNT;
			break;
		case "staff" :
			minorType = WEAPON_TYPE_STAFF;
			actId = WEAPON_GFX_STAFF;
			break;
		case "throwingknife" :
			minorType = WEAPON_TYPE_THROWING_KNIFE;
			actId = WEAPON_GFX_THROWING_KNIFE;
			break;
		case "arrow" :
			minorType = WEAPON_TYPE_ARROW;
			actId = WEAPON_GFX_ARROW;
			break;
		case "gauntlet" :
			minorType = WEAPON_TYPE_GAUNTLET;
			actId = WEAPON_GFX_GAUNTLET;
			break;
		case "claw" :
			minorType = WEAPON_TYPE_CLAW;
			actId = WEAPON_GFX_CLAW;
			break;
		case "edoryu" :
			minorType = WEAPON_TYPE_EDORYU;
			actId = WEAPON_GFX_EDORYU;
			break;
		case "singlebow" :
			minorType = WEAPON_TYPE_SINGLE_BOW;
			actId = WEAPON_GFX_SINGLE_BOW;
			break;
		case "singlespear" :
			minorType = WEAPON_TYPE_SINGLE_SPEAR;
			actId = WEAPON_GFX_SINGLE_SPEAR;
			break;
		case "tohandblunt" :
			minorType = WEAPON_TYPE_TOHAND_BLUNT;
			actId = WEAPON_GFX_TOHAND_BLUNT;
			break;
		case "tohandstaff" :
			minorType = WEAPON_TYPE_TOHAND_STAFF;
			actId = WEAPON_GFX_TOHAND_STAFF;
			break;
		default :
			minorType = 0xFF;
			actId = 0;
			break;
		}
		
		switch (materialName) {
		case "none" : material = MATERIAL_NONE; break;
		case "liquid" : material = MATERIAL_LIQUID; break;
		case "web" : material = MATERIAL_WEB; break;
		case "vegetation" : material = MATERIAL_VEGETATION; break;
		case "animalmetter" : material = MATERIAL_ANIMALMATTER; break;
		case "paper" : material = MATERIAL_PAPER; break;
		case "cloth" : material = MATERIAL_CLOTH; break;
		case "leather" : material = MATERIAL_LEATHER; break;
		case "wood" : material = MATERIAL_WOOD; break;
		case "bone" : material = MATERIAL_BONE; break;
		case "dragonscale" : material = MATERIAL_DRAGONSCALE; break;
		case "iron" : material = MATERIAL_IRON; break;
		case "steel" : material = MATERIAL_STEEL; break;
		case "copper" : material = MATERIAL_COPPER; break;
		case "silver" : material = MATERIAL_SILVER; break;
		case "gold" : material = MATERIAL_GOLD; break;
		case "platinum" : material = MATERIAL_PLATINUM; break;
		case "mithril" : material = MATERIAL_MITHRIL; break;
		case "blackmithril" : material = MATERIAL_BLACKMITHRIL; break;
		case "glass" : material = MATERIAL_GLASS; break;
		case "mineral" : material = MATERIAL_MINERAL; break;
		case "oriharukon" : material = MATERIAL_ORIHARUKON; break;
		default: material = 0xFF;
		}
		
	}
	
	public byte[] ParseWeaponDetail () {
		PacketBuilder builder = new PacketBuilder () ;
		
		builder.writeByte (1) ;
		builder.writeByte (dmgSmall) ;
		builder.writeByte (dmgLarge) ;
		builder.writeByte (material) ;
		builder.writeDoubleWord (weight / 1000) ;
		
		//
		builder.writeByte (2) ; //Enchant level
		builder.writeByte (0) ;
		
		//builder.WriteByte (3) ; //Durability
		//builder.WriteByte (100) ;
		
		if (isTwohandedWeapon () ) {
			builder.writeByte (4) ;
		}
		
		if (hitModify > 0) {
			builder.writeByte (5) ; //hit modifier
			builder.writeByte (hitModify) ;
		}
		
		if (dmgModify > 0) {
			builder.writeByte (6) ; //dmg modifier
			builder.writeByte (dmgModify) ;
		}
		
		byte UseClass = 0;
		if (isRoyalUsable) UseClass   |= 0x01;
		if (isKnightUsable) UseClass  |= 0x02;
		if (isElfUsable) UseClass     |= 0x04;
		if (isWizardUsable) UseClass    |= 0x08;
		if (isDarkelfUsable) UseClass |= 0x10;
		builder.writeByte (7) ; //use class
		builder.writeByte (UseClass);
		
		if (isManaDrain || id == 126 || id == 127) {
			builder.writeByte (16) ;
		}
		
		if (str > 0) {
			builder.writeByte (8) ;
			builder.writeByte (str) ;
		}
		
		if (dex > 0) {
			builder.writeByte (9) ;
			builder.writeByte (dex) ;
		}
		
		if (con > 0) {
			builder.writeByte (10) ;
			builder.writeByte (con) ;
		}
		
		if (intel > 0) {
			builder.writeByte (11) ;
			builder.writeByte (intel) ;
		}
		
		if (wis > 0) {
			builder.writeByte (12) ;
			builder.writeByte (wis) ;
		}
		
		if (cha > 0) {
			builder.writeByte (13) ;
			builder.writeByte (cha) ;
		}
		
		if (hp > 0) {
			builder.writeByte (31) ;
			builder.writeByte (hp) ;
		}
		
		if (mp > 0) {
			builder.writeByte (32) ;
			builder.writeByte (mp) ;
		}
		
		if (mr > 0) {
			builder.writeByte (15) ;
			builder.writeWord (mr) ;
		}
		
		if (sp > 0) {
			builder.writeByte (17) ;
			builder.writeWord (sp) ;
		}
		
		if (isHasteItem) {
			builder.writeByte (18) ;
		}
		
		return builder.getPacketNoPadding () ;
	}
	
	public boolean isTwohandedWeapon () {
		return (minorType == WEAPON_TYPE_TOHAND_SWORD) || (minorType == WEAPON_TYPE_TOHAND_STAFF) ||
				(minorType == WEAPON_TYPE_TOHAND_BLUNT) || (minorType == WEAPON_TYPE_BOW) ||
				(minorType == WEAPON_TYPE_CLAW) || (minorType == WEAPON_TYPE_EDORYU) ;
	}
}
