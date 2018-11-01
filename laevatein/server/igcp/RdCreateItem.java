package laevatein.server.igcp;

import laevatein.server.utility.*;
import laevatein.server.*;
import laevatein.server.process_server.SystemMessage;
import laevatein.game.*;
import laevatein.game.model.player.*;
import laevatein.game.model.item.*;

public class RdCreateItem
{
	public RdCreateItem (PcInstance rd, String cmd) {
		SessionHandler handle = rd.getHandle ();
		boolean cmdFormatValid = false;
		
		String[] createCmd = cmd.split (" ");
		
		int itemId = 0;
		int itemAmount = 0;
		
		if (createCmd.length == 2) {
			itemId = Integer.valueOf (createCmd[1]);
			itemAmount = 1;
			cmdFormatValid = true;
			
		} else if (createCmd.length == 3) {
			itemId = Integer.valueOf (createCmd[1]);
			itemAmount = Integer.valueOf (createCmd[2]);
			cmdFormatValid = true;
			
		}
		
		if (CacheData.weapon.containsKey (itemId) || 
			CacheData.armor.containsKey (itemId) || 
			CacheData.item.containsKey (itemId)) {
			//
		} else {
			cmdFormatValid = false;
		}
		
		if (cmdFormatValid) {
			ItemInstance i = new ItemInstance (itemId, UuidGenerator.next (), rd.uuid, 0, itemAmount, 0, 0, false, true);
			//TODO:fix
			//rd.addItem (i);
			
			handle.sendPacket (new SystemMessage (String.format ("新增%d個%s", i.count, i.name)).getRaw ());
			
		} else {
			handle.sendPacket (new SystemMessage ("無效命令或itemId不存在 -> .create itemId amount").getRaw ());
		}		
	}
}
