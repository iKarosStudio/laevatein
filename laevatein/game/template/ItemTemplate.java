package laevatein.game.template;

import laevatein.server.packet.*;
import static laevatein.game.template.ItemTypeTable.*;

public class ItemTemplate
{	
	public int majorType = 0; //0:etcitem 1:weapon 2:armor
	public int minorType;
	public int useType;
	public int material;
	
	public int id;
	public String name;
	public String nameId;
	public String typeName;
	public String useTypeName;
	public String materialName;
	public int weight;
	public int gfxInBag;
	public int gfxOnGround;
	public int descriptId;
	public boolean isStackable = false;
	public int maxCharge = 0;
	public int dmgSmall = 0;
	public int dmgLarge = 0;
	public int minLevel = 0;
	public int maxLevel = 0;
	public int bless = 0;
	public boolean isTradeable = true;
	public int delayId = 0;
	public int delayTime = 0;
	public int foodValue = 0;
	public boolean isSaveAtOnce = false;
	
	public ItemTemplate () {}
	
	public ItemTemplate (
			int _itemId, 
			String _name, 
			String _nameId, 
			String _itemType, 
			String _useType,
			String _material,
			int _weight,
			int _gfxInBag,
			int _gfxOnGround,
			int _descriptId,
			boolean _isStackable,
			int _maxCharge,
			int _dmgSmall,
			int _dmgLarge,
			int _minLevel,
			int _maxLevel,
			int _bless,
			boolean _isTradeable,
			int _delayId,
			int _delayTime,
			int _foodValue,
			boolean _saveAtOnce) {
		id = _itemId;
		name = _name;
		nameId = _nameId;
		typeName = _itemType;
		useTypeName = _useType;
		materialName = _material;
		weight = _weight;
		gfxInBag = _gfxInBag;
		gfxOnGround = _gfxOnGround;
		descriptId = _descriptId;
		isStackable = _isStackable;
		maxCharge = _maxCharge;
		dmgSmall = _dmgSmall;
		dmgLarge = _dmgLarge;
		minLevel = _minLevel;
		maxLevel = _maxLevel;
		bless = _bless;
		isTradeable = _isTradeable;
		delayId = _delayId;
		delayTime = _delayTime;
		foodValue = _foodValue;
		isSaveAtOnce = _saveAtOnce;
		
		
		switch (typeName) {
			case "arrow" : minorType = TYPE_ARROW; break;
			case "wand" : minorType = TYPE_WAND; break;
			case "light" : minorType = TYPE_LIGHT; break;
			case "gem" : minorType = TYPE_GEM; break;
			case "totem" : minorType = TYPE_TOTEM; break;
			case "firecracker" : minorType = TYPE_FIRECRACKER; break;
			case "potion" : minorType = TYPE_POTION; break;
			case "food" : minorType = TYPE_FOOD; break;
			case "scroll" : minorType = TYPE_SCROLL; break;
			case "questitem" : minorType = TYPE_QUEST_ITEM; break;
			case "spellbook" : minorType = TYPE_SPELL_BOOK; break;
			case "petitem" : minorType = TYPE_PET_ITEM; break;
			case "other" : minorType = TYPE_OTHER; break;
			case "material" : minorType = TYPE_MATERIAL; break;
			case "event" : minorType = TYPE_EVENT; break;
			case "sting" : minorType = TYPE_STING; break;
			default: minorType = 0xFF; break;
		}
		
		switch (useTypeName) {
			case "none" : useType = -1; break;
			case "normal" : useType = 0; break;
			case "weapon" : useType = 1; break;
			case "armor" : useType = 2; break;
			case "spell_long" : useType = 5; break;
			case "ntele" : useType = 6; break;
			case "identify" : useType = 7; break;
			case "res" : useType = 8; break;
			case "letter" : useType = 12; break;
			case "letter_w" : useType = 13; break;
			case "choice" : useType = 14; break;
			case "instrument" : useType = 15; break;
			case "sosc" : useType = 16; break;
			case "spell_short" : useType = 17; break;
			case "T" : useType = 18; break;
			case "cloak" : useType = 19; break;
			case "glove" : useType = 20; break;
			case "boots" : useType = 21; break;
			case "helm" : useType = 22; break;
			case "ring" : useType = 23; break;
			case "amulet" : useType = 24; break;
			case "shield" : useType = 25; break;
			case "dai" : useType = 26; break;
			case "zel" : useType = 27; break;
			case "blank" : useType = 28; break;
			case "btele" : useType = 29; break;
			case "spell_buff" : useType = 30; break;
			case "ccard" : useType = 31; break;
			case "ccard_w" : useType = 32; break;
			case "vcard" : useType = 33; break;
			case "vcard_w" : useType = 34; break;
			case "wcard" : useType = 35; break;
			case "wcard_w" : useType = 36; break;
			case "belt" : useType = 37; break;
			case "earring" : useType = 40; break;
			case "fishing_rod" : useType = 42; break;
			default : useType = 0xFF; break;
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
	
	
	public byte[] ParseItemDetail () {
		return ParseItemDetail (1) ;
	}
	
	public byte[] ParseItemDetail (int count) {
		PacketBuilder detail = new PacketBuilder () ;
		
		detail.writeByte (material) ;
		detail.writeDoubleWord (weight / 1000);
		
		switch (typeName) {
		case "light" :
			detail.writeByte (22) ;
			detail.writeWord (10) ; //light range
			break;
			
		case "food" :
			detail.writeByte (21) ;
			detail.writeWord (foodValue) ;
			break;
		
		case "arrow" :
		case "sting" :
			detail.writeByte (1) ;
			detail.writeByte (dmgSmall) ;
			detail.writeByte (dmgLarge) ;
			
		default :
			detail.writeByte (23) ;
			break;
		}

		return detail.getPacketNoPadding () ;
	}
}
