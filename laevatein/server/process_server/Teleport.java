package laevatein.server.process_server;

import laevatein.types.*;
import laevatein.server.*;
import laevatein.game.*;
import laevatein.game.model.player.*;

public class Teleport
{
	public Teleport (PcInstance pc, Location dest, boolean useVirtualEffect) {
		SessionHandler handle = pc.getHandle ();
		
		if (pc.loc.mapId != dest.mapId) { //map change
			pc.map.removePlayer (pc.uuid);
			pc.loc.mapId = dest.mapId;
			
			pc.pcsInsight.clear ();
			pc.modelsInsight.clear ();
			
			//update map
			pc.map = Laevatein.getInstance ().getMap (pc.loc.mapId);
			pc.map.addPlayer (pc);
			//pc.saveSkillEffects ();
		}
		
		//update coordinate
		pc.loc.p.x = dest.p.x;
		pc.loc.p.y = dest.p.y;
		
		byte[] mapPacket = new MapId (pc.loc.mapId).getRaw ();
		byte[] pcPacket = pc.getPacket ();
		
		if (useVirtualEffect) {
			byte[] effectPacket = new VisualEffect (pc.uuid, 169).getRaw ();
			handle.sendPacket (effectPacket);
			pc.boardcastPcInsight (effectPacket);
			
			try {
				Thread.sleep (700); //0.7s delay
			} catch (Exception e) {
				e.printStackTrace (); 
			}
		}
		
		pc.pcsInsight.clear ();
		pc.modelsInsight.clear ();
		
		//update Skills
		//pc.skillBuffs.updateSkillEffects ();
		
		handle.sendPacket (mapPacket);
		handle.sendPacket (pcPacket);
		handle.sendPacket (new UpdateModelGfx (pc.uuid, pc.actId).getRaw ());
	
		pc.boardcastPcInsight (pcPacket);
	}
}
