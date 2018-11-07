package laevatein.server.process_server;

import laevatein.server.packet.*;
import laevatein.server.opcodes.*;

public class UpdateAc
{
	PacketBuilder packet = new PacketBuilder ();
	
	public UpdateAc (int ac) {
		packet.writeByte (ServerOpcodes.UPDATE_MODEL_AC) ;
		packet.writeByte (ac); //Ac
		packet.writeByte (0) ; //fire
		packet.writeByte (0) ; //water
		packet.writeByte (0) ; //wind
		packet.writeByte (0) ; //earth
	}
	
	public byte[] getRaw () {
		return packet.getPacket ();
	}
}
