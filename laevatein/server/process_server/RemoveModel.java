package laevatein.server.process_server;

import laevatein.server.packet.*;
import laevatein.server.opcodes.*;

//要求客戶端移除uuid物件的顯示
public class RemoveModel
{
	private PacketBuilder packet = new PacketBuilder ();
	
	public RemoveModel (int _uuid) {
		packet.writeByte (ServerOpcodes.REMOVE_OBJECT);
		packet.writeDoubleWord (_uuid);
	}
	
	public byte[] getRaw () {
		return packet.getPacket ();
	}
}
