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
		pc = _handle.user.activePc;
		
		int tid = packetReader.readDoubleWord ();
		int x = packetReader.readWord ();
		int y = packetReader.readWord ();
		
		if (pc.getWeightInScale30 () > 24) { //太重
			_handle.sendPacket (new GameMessage (110).getRaw ());
			return;
		}
		
		pc.heading = pc.getDirection (x, y);
		_handle.sendPacket (new ModelAction (ActionId.ATTACK, pc.uuid, pc.heading).getRaw ());
	}
}
