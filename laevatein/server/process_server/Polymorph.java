package laevatein.server.process_server;

import laevatein.game.*;
import laevatein.game.skill.*;
import laevatein.game.template.*;
import laevatein.game.model.player.*;

public class Polymorph extends _PacketFrame
{
	public Polymorph (PcInstance pc, int polyId) {
		PolyTemplate poly = CacheData.polies.get (polyId);
		
		if (poly != null) {
			
			//
			
			//pc.addSkillEffect (SkillId.SHAPE_CHANGE, 1800, polyId);
			
		} //end of poly exist
	}
	
	
}
