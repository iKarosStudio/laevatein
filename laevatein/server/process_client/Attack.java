package laevatein.server.process_client;

import laevatein.server.*;
import laevatein.server.packet.*;
import laevatein.server.process_server.*;
import laevatein.game.model.player.*;

/* 攻擊驗算參考 model/L1Attack.java */
public class Attack
{
	public Attack (SessionHandler handle, byte[] data) {
		PacketReader packetReader = new PacketReader (data);
		PcInstance pc = handle.user.activePc;
		
		int tid = packetReader.readDoubleWord ();
		int x = packetReader.readWord ();
		int y = packetReader.readWord ();
		
		if (pc.getWeightInScale30 () > 24) { //太重
			handle.sendPacket (new GameMessage (110).getRaw ());
			return;
		}
		
		/* 傷害判定, 表現攻擊動作 */		
		//pc.attack (tid, x, y);
	}
}
