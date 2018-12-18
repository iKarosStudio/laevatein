package laevatein.server.process_client;

import laevatein.server.*;
import laevatein.server.packet.*;

import laevatein.game.model.player.*;

public class GlobalTalk
{
	SessionHandler handle;
	PcInstance pc;
	
	public GlobalTalk (SessionHandler _handle, byte[] packet) {
		PacketReader packetReader = new PacketReader (packet);
		
		handle = _handle;
		pc = handle.getUser().getActivePc ();
		
		int chatType = packetReader.readByte ();
		String chat = packetReader.readString ();
		
		System.out.printf ("chat type:%d-%s\n", chatType, chat);
		
		//檢查等級
		
		
	}
}
