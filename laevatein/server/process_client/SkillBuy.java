package laevatein.server.process_client;

import laevatein.server.*;
import laevatein.server.packet.*;
import laevatein.server.process_server.*;

public class SkillBuy
{
	SessionHandler handle;
	PacketReader reader;
	
	public SkillBuy (SessionHandler _handle, byte[] packet) {
		handle = _handle;
		reader = new PacketReader (packet);
		int skill = reader.readDoubleWord ();
		
		new SkillBuyList (handle, handle.getUser ().getActivePc ().getType ());
	}
}
