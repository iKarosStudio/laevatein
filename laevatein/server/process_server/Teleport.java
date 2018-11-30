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
			
			try{
				pc.sight.wait ();
				pc.pcsInsight.clear ();
				pc.objectsInsight.clear ();
				pc.sight.notify ();
			} catch (Exception e) {
				e.printStackTrace ();
			}
			
			//update map
			pc.map = Laevatein.getInstance ().getMap (pc.loc.mapId);
			pc.map.addPlayer (pc);
			
			
		}
		
		pc.saveBuffs ();
		
		//update coordinate
		pc.loc.p.x = dest.p.x;
		pc.loc.p.y = dest.p.y;
		
		byte[] mapPacket = new MapId (pc.loc.mapId).getRaw ();
		byte[] pcPacket = pc.getPacket ();
		
		if (useVirtualEffect) {
			byte[] effectPacket = new VisualEffect (pc.uuid, 169).getRaw ();
			handle.sendPacket (effectPacket);
			pc.boardcastPcInsight (effectPacket);
			
			try { //TODO:0.5s delay改成config檔案可以設定
				Thread.sleep (500); 
			} catch (Exception e) {
				e.printStackTrace (); 
			}
		}
		
		pc.pcsInsight.clear ();
		pc.objectsInsight.clear ();		
		
		handle.sendPacket (mapPacket);
		handle.sendPacket (pcPacket);
		handle.sendPacket (new UpdateModelActId (pc.uuid, pc.actId).getRaw ());
		
		//update Skills
		pc.loadBuffs ();
		
		pc.boardcastPcInsight (pcPacket);
	}
}
