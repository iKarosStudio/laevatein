package laevatein.server.igcp;

import laevatein.server.*;
import laevatein.server.process_server.SystemMessage;
import laevatein.game.*;
import laevatein.game.model.player.*;
import laevatein.game.model.item.*;
import laevatein.game.skill.*;

public class RdPcData
{
	public RdPcData (PcInstance rd, String cmd) {
		SessionHandler handle = rd.getHandle ();
		
		String[] cmdParse = cmd.split (" ");
		int pcUuid = 0;
		
		if (cmdParse.length == 2) {
			pcUuid = Integer.valueOf (cmdParse[1]);
		} else if (cmdParse.length == 1) {
			pcUuid = rd.uuid;
		} else {
			return;
		}
		
		Laevatein world = Laevatein.getInstance ();
		PcInstance pc = world.getPlayer (pcUuid);
		if (pc != null) {
			StringBuffer console = new StringBuffer ();
			
			console.append (String.format ("  %s[UUID:%d]\n", pc.name, pc.uuid));
			console.append (String.format ("  location={mapid:%d, x:%d, y:%d, heading:%d}\n", pc.loc.mapId, pc.loc.p.x, pc.loc.p.y, pc.heading));
			console.append (String.format ("  moveSpeed:%d, actId:%d\n", pc.moveSpeed, pc.actId));
			console.append (String.format ("  status:0x%02X\n", pc.status));
			console.append (String.format ("  str:%d, con:%d, dex:%d\n", pc.getStr(), pc.getCon(), pc.getDex()));
			console.append (String.format ("  wis:%d, cha:%d, int:%d\n", pc.getWis(), pc.getCha(), pc.getIntel()));
			console.append (String.format ("  sp:%d, mr:%d\n", pc.getSp(), pc.getMr()));
			console.append (String.format ("  hpr:%d, mpr:%d\n", pc.basicParameters.hpR, pc.basicParameters.mpR));
			/*
			console.append (String.format ("  [skill effects]->\n"));
			pc.skillBuffs.getEffects ().forEach ((Integer skillId, SkillEffect effect)->{
				console.append (String.format ("    id:%d remain time:%d\n", skillId, effect.remainTime));
			});*/
			
			
			console.append (String.format ("  [item bag]->\n"));
			pc.itemBag.forEach ((Integer iid, ItemInstance item)->{
				console.append (String.format ("    %d:%s\n", iid, item.getName ()));
			});
			
			handle.sendPacket (new SystemMessage (console.toString ()).getRaw ());
			
		} else {
			handle.sendPacket (new SystemMessage (String.format ("UUID:%d NOT FOUND\n", pcUuid)).getRaw ());
			
		}		
	}
}
