package laevatein.server.process_server;

import laevatein.types.*;
import laevatein.server.*;
import laevatein.game.*;
import laevatein.game.model.player.*;

public class Teleport
{
	public Teleport (PcInstance pc, Location dest, boolean useVirtualEffect) {
		SessionHandler handle = pc.getHandle ();
		
		if (pc.getLocation ().mapId != dest.mapId) { //map change
			pc.getMap ().removePlayer (pc.getUuid ());
			pc.getLocation ().mapId = dest.mapId;
			
			try{
				pc.sight.wait ();
				pc.pcsInsight.clear ();
				pc.objectsInsight.clear ();
				pc.sight.notify ();
			} catch (Exception e) {
				e.printStackTrace ();
			}
			
			//update map
			pc.setMap (Laevatein.getInstance ().getMap (pc.getLocation ().mapId));
			pc.getMap ().addPlayer (pc);
			
			
		}
		
		pc.saveBuffs ();
		
		//update coordinate
		pc.getLocation ().x = dest.x;
		pc.getLocation ().y = dest.y;
		
		byte[] mapPacket = new MapId (pc.getLocation ().mapId).getPacket ();
		byte[] pcPacket = pc.getPacket ();
		
		if (useVirtualEffect) {
			byte[] effectPacket = new VisualEffect (pc.getUuid (), 169).getPacket ();
			handle.sendPacket (effectPacket);
			pc.boardcastPcInsight (effectPacket);
			
			try { //TODO:0.5s delay改成config檔案可以設定
				Thread.sleep (500); 
			} catch (Exception e) {
				e.printStackTrace (); 
			}
		}
		
		//重置視野內物件
		pc.pcsInsight.clear ();
		pc.objectsInsight.clear ();
		
		handle.sendPacket (mapPacket);
		handle.sendPacket (pcPacket);
		handle.sendPacket (new UpdateModelActId (pc.getUuid (), pc.actId).getPacket ());
		
		//update Skills
		pc.loadBuffs ();
		
		pc.boardcastPcInsight (pcPacket);
	}
}
