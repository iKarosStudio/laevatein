package laevatein.server.process_client;

import laevatein.server.*;
import laevatein.server.packet.*;
import laevatein.server.process_server.*;
import laevatein.game.model.player.*;

/* 攻擊驗算參考 model/L1Attack.java */
@Deprecated
public class Attack
{
	public Attack (SessionHandler handle, byte[] data) {
		PacketReader packetReader = new PacketReader (data);
		PcInstance pc = handle.getUser().getActivePc ();
		
		int tid = packetReader.readDoubleWord ();
		int x = packetReader.readWord ();
		int y = packetReader.readWord ();
		
		if (pc.getWeightScale30 () > 24) { //太重 or 0x18運算
			handle.sendPacket (new GameMessage (110).getPacket ());
			return;
		}
		
		/* 傷害判定, 表現攻擊動作 */		
		//pc.attack (tid, x, y);
	}
}
