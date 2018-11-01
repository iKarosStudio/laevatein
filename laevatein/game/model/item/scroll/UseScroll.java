package laevatein.game.model.item.scroll;

import laevatein.types.*;
import laevatein.server.*;
import laevatein.server.packet.*;
import laevatein.server.process_server.*;
import laevatein.game.model.*;
import laevatein.game.model.player.*;
import laevatein.game.model.item.*;
import static laevatein.game.template.ItemTypeTable.*;

public class UseScroll
{
	PcInstance pc;
	SessionHandler handle;
	public UseScroll (PacketReader packetReader, PcInstance _pc, ItemInstance scroll) {
		pc = _pc;
		handle = _pc.getHandle ();
		
		switch (scroll.useType) {
		case TYPE_USE_NTELE:
			TeleportScroll (scroll);
			break;
			
		case TYPE_USE_SOSC:
			//變形類道具
			String s = packetReader.readString ();
			break;
		case TYPE_USE_BLANK: //空的魔法卷軸
			int skill = packetReader.readByte ();
			break;
			
		case TYPE_USE_IDENTIFY:
			System.out.printf ("鑑定卷軸\n");
			int uuid = packetReader.readDoubleWord ();
			ItemInstance identifyItem = pc.itemBag.get (uuid);
			if (identifyItem != null) {
				//TODO:顯示鑑定文字
				//訊息$133~$135使用
			}
			
			break;
		case TYPE_USE_RES: //復活卷軸
			int target = packetReader.readDoubleWord ();
			break;
			
		default:
			handle.sendPacket (new GameMessage (74).getRaw ());
			System.out.printf ("未知種類的捲軸或還沒有處理:%d %s\n", scroll.id, scroll.name);
			break;
		}
	}
	
	public boolean checkScrollDelay (ItemInstance i) {
		boolean res;
		long nowTime = System.currentTimeMillis ();
		
		if (pc.getItemDelay (i.id, nowTime) > i.delayTime) {
			pc.setItemDelay (i.id, nowTime);
			res = true;
		} else {
			res = false;
		}
		
		return res;
	}
	
	/* 順移卷軸 */
	private void TeleportScroll (ItemInstance scroll) {
		//麻痺時不能順移
		//
		
		//冷凍時不能順移
		if (pc.isFreeze ()) {
		}
		
		while (!checkScrollDelay (scroll) ) {
			try {
				Thread.sleep (500);
			} catch (Exception e) {
				e.printStackTrace ();
			} 
		}
		
		Location dest;
		dest = pc.map.getRandomLocation ();		
		new Teleport (pc, dest, true);
	}
}
