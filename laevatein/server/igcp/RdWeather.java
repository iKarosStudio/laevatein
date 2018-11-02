package laevatein.server.igcp;

import laevatein.server.utility.*;
import laevatein.server.*;
import laevatein.server.process_server.SystemMessage;
import laevatein.game.*;
import laevatein.game.model.player.*;
import laevatein.game.model.item.*;

public class RdWeather
{
	public RdWeather (PcInstance rd, String cmd) {
		//SessionHandler handle = rd.getHandle ();
		
		String[] weatherCmd = cmd.split (" ");
		
		if (weatherCmd.length == 2) {
			int weather = Integer.decode (weatherCmd[1]);
			Laevatein.getInstance ().setWeather (0, weather);
		}
		
	}
}
