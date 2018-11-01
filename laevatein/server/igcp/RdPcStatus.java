package laevatein.server.igcp;

import laevatein.game.model.player.*;

public class RdPcStatus {
	public RdPcStatus (PcInstance p, String cmd) {
		String[] cmds = cmd.split (" ");
		
		if (cmds.length == 2) {
			int newStatus = Integer.decode (cmds[1]);
			System.out.printf ("status:%02x\n", newStatus);
			
			p.status = newStatus;
			p.getHandle ().sendPacket (p.getPacket ());
		}
	}
}
