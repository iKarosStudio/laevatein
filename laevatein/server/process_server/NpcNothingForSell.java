package laevatein.server.process_server;

import laevatein.server.packet.*;
import laevatein.server.opcodes.*;

public class NpcNothingForSell
{
	//告知客戶端NPC含有對話內容的HTML ID編號
	private PacketBuilder packet = new PacketBuilder ();
	
	public NpcNothingForSell (int npcId) {
		packet.writeByte (ServerOpcodes.NPC_RESULT);
		packet.writeDoubleWord (npcId);
		packet.writeString ("nosell");
		packet.writeByte (0x01) ;

	}
	
	public byte[] getRaw () {
		return packet.getPacket ();
	}
}
