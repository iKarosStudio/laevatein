package laevatein.server.process_server;

import laevatein.server.*;
import laevatein.server.opcodes.*;

public class ReportTitle extends _PacketFrame
{
	public ReportTitle (SessionHandler handle) {
		packet.writeByte (ServerOpcodes.CHAR_TITLE);
		packet.writeDoubleWord (handle.user.activePc.uuid);
		packet.writeString (handle.user.activePc.title);
	}
	
	public ReportTitle (int uuid, String title) {
		packet.writeByte (ServerOpcodes.CHAR_TITLE);
		packet.writeDoubleWord (uuid);
		packet.writeString (title);
	}
}
