package laevatein.server.process_server;

import laevatein.server.packet.*;
import laevatein.server.opcodes.*;

/*
 * 指定uuid執行一個action code
 */
public class ModelAction
{
	PacketBuilder packet = new PacketBuilder ();
	
	public ModelAction (int actionCode, int uuid, int heading) {
		packet.writeByte (ServerOpcodes.MODEL_ACTION) ;
		packet.writeByte (actionCode);
		packet.writeDoubleWord (uuid);
		packet.writeDoubleWord (0);
		packet.writeByte (0x4E);
		packet.writeByte (heading); //目標物件的heading
		packet.writeWord (0x00);
		packet.writeWord (0x00);
		packet.writeByte (0x00);
	}
	
	public byte[] getRaw () {
		return packet.getPacket ();
	}
}
