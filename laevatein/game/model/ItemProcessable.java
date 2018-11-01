package laevatein.game.model;

import laevatein.game.model.item.*;

public interface ItemProcessable {
	//impletment itemBag
	public void pickItem (int itemUuid, int count, int x, int y);
	public void dropItem (int itemUuid, int count, int x, int y);
	public void giveItem ();
	public void recvItem ();
	
	public void addItem (ItemInstance item);
	public void addItem (int itemId, int amount);
	public void removeItem (ItemInstance item);
	public void removeItem (int itemUuid, int amount);
	
	//implement itemDelay
	public long getItemDelay (int itemId, long nowTime);
	public void setItemDelay (int itemId, long nowTime);
}
