package laevatein.game.skill;

import laevatein.server.*;
import laevatein.server.process_server.ModelAction;
import laevatein.game.model.ActionId;
import laevatein.game.model.player.*;

public class NormalAttack
{
	PcInstance pc;
	public NormalAttack (SessionHandler _handle, byte[] packet) {
		pc = _handle.user.activePc;
		
		_handle.sendPacket (new ModelAction (ActionId.ATTACK, pc.uuid, pc.heading).getRaw ());
	}
}
