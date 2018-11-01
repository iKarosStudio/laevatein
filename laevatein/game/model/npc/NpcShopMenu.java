package laevatein.game.model.npc;

import laevatein.server.utility.*;
import laevatein.game.*;
import laevatein.game.model.item.*;

public class NpcShopMenu
{
	public int npcId;
	public int itemId;
	public int orderId;
	public int sellingPrice;
	public int packCount;
	public int purchasingPrice;
	
	public NpcShopMenu (
			int _npcId,
			int _itemId,
			int _orderId,
			int _sellingPrice,
			int _packCount,
			int _purchasingPrice) {
		npcId = _npcId;
		itemId = _itemId;
		orderId = _orderId;
		sellingPrice = _sellingPrice; //販售價
		packCount = _packCount;
		purchasingPrice = _purchasingPrice; //收購價
	}
	
	public boolean isStackable () {
		boolean result = false;
		
		if (CacheData.item.containsKey (itemId)) {
			result = CacheData.item.get (itemId).isStackable;
		} 
		
		return result;
	}
	
	public ItemInstance getItem () {
		ItemInstance item = new ItemInstance (itemId, UuidGenerator.next(), 0, 0, 1, 0, 100, false, true);
		return item;
	}
	
	/*
	public ItemInstance getItemInstance () {
		ItemInstance i = new ItemInstance (
				itemId,
				UuidGenerator.next(),
				0,//owner id
				1,//count
				0,//durability
				100,//chargecount
				false,//isUsing
				true);//isIdentified
		return i;
	}
	
	public WeaponInstance getWeaponInstance () {
		WeaponInstance weapon = new WeaponInstance (itemId, UuidGenerator.next(), 0, 0, 0, false, true);
		return weapon;
	}
	
	public ArmorInstance getArmorInstance () {
		ArmorInstance armor = new ArmorInstance (itemId, UuidGenerator.next(), 0, 0, 0, false, true);
		return armor;
	}
	*/
}
