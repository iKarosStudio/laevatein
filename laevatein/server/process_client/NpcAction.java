package laevatein.server.process_client;

import laevatein.server.*;
import laevatein.server.packet.*;
import laevatein.game.model.npc.*;

public class NpcAction
{
	public NpcAction (SessionHandler handle, byte[] data) {
		PacketReader reader = new PacketReader (data);
		
		int npcId = reader.readDoubleWord ();
		String actionCode = reader.readString ();
		
		new NpcActionCodeHandler (handle, npcId, actionCode);
	}
}
