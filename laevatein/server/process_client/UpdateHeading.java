package laevatein.server.process_client;

import laevatein.server.*;
import laevatein.server.packet.*;
import laevatein.server.process_server.*;
import laevatein.game.model.player.*;

public class UpdateHeading
{
	public UpdateHeading (SessionHandler handle, byte[] data) {
		PacketReader packetReader = new PacketReader (data) ;
		PcInstance pc = handle.getUser ().getActivePc ();
		int heading = packetReader.readByte ();
		
		pc.heading = heading;
		
		//Pc.BoardcastPcInsightExceptSelf (new NodeHeading (Pc.Uuid, Pc.location.Heading).getRaw () ) ;
		pc.boardcastPcInsight (new ModelHeading (pc.getUuid (), pc.heading).getPacket ());
	}
}
