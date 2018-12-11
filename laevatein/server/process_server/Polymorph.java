package laevatein.server.process_server;

import laevatein.game.*;
import laevatein.game.template.*;
import laevatein.game.model.player.*;

public class Polymorph extends _PacketFrame
{
	//變成指定怪物
	public Polymorph (PcInstance pc, int polyId, int time) {		
		PolyTemplate poly = CacheData.poly.get (polyId);
		
		if (poly != null) {
			//變身裝備調整
			pc.equipment.fitPoly (poly);
			
			pc.gfx = poly.polyId;
			
			byte[] updateGfxPacket = new UpdateModelGfx (pc.uuid, poly.polyId, false).getPacket ();
			byte[] updateActIdPacket = new UpdateModelActId (pc.uuid, pc.actId).getPacket ();
			
			pc.sendPacket (updateGfxPacket);
			pc.sendPacket (updateActIdPacket);
			pc.sendPacket (new SkillIcon (35, time).getPacket ());
			if (!pc.isInvisible ()) {
				pc.boardcastPcInsight (updateGfxPacket);
				pc.boardcastPcInsight (updateActIdPacket);
			}			
		} //end of poly exist
	}
	
	//變回原狀
	public Polymorph (PcInstance pc) {
		pc.gfx = pc.originGfx;
		
		byte[] updateGfxPacket = new UpdateModelGfx (pc.uuid, pc.gfx, false).getPacket ();
		byte[] updateActIdPacket = new UpdateModelActId (pc.uuid, pc.actId).getPacket ();
		
		pc.sendPacket (updateGfxPacket);
		pc.sendPacket (updateActIdPacket);
		pc.sendPacket (new SkillIcon (35, 0).getPacket ());
		if (!pc.isInvisible ()) {
			pc.boardcastPcInsight (updateGfxPacket);
			pc.boardcastPcInsight (updateActIdPacket);
		}
	}
}
