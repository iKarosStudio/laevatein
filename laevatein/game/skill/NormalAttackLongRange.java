package laevatein.game.skill;

import laevatein.server.*;
import laevatein.server.packet.*;
import laevatein.server.process_server.*;
import laevatein.game.model.*;
import laevatein.game.model.player.*;

public class NormalAttackLongRange
{
	PcInstance pc;
	
	public NormalAttackLongRange (SessionHandler _handle, byte[] packet) {
		PacketReader packetReader = new PacketReader (packet);
		pc = _handle.getUser ().getActivePc ();
		
		int tid = packetReader.readDoubleWord ();
		int x = packetReader.readWord ();
		int y = packetReader.readWord ();
		
		if (pc.getWeightScale30 () > 24) { //太重
			_handle.sendPacket (new GameMessage (110).getPacket ());
			return;
		}
		
		pc.heading = pc.getDirection (x, y);
		_handle.sendPacket (new ModelAction (ActionId.ATTACK, pc.getUuid (), pc.heading).getPacket ());
	}
}
