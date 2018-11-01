package laevatein.server.process_server;

import laevatein.server.packet.*;
import laevatein.server.opcodes.*;
import laevatein.game.model.player.*;

public class NewCharacterPack
{
	PacketBuilder packet = new PacketBuilder () ;
	
	public NewCharacterPack (PcInstance pc) {
		packet.writeByte (ServerOpcodes.NEW_CHARACTER_PACK) ;
		packet.writeString (pc.name);
		packet.writeString ("");
		packet.writeByte (pc.type);
		packet.writeByte (pc.sex);
		packet.writeWord (pc.lawful);
		packet.writeWord (pc.basicParameters.maxHp);
		packet.writeWord (pc.basicParameters.maxMp);
		packet.writeByte (pc.basicParameters.ac);
		packet.writeByte (pc.level);
		packet.writeByte (pc.basicParameters.str);
		packet.writeByte (pc.basicParameters.dex);
		packet.writeByte (pc.basicParameters.con);
		packet.writeByte (pc.basicParameters.wis);
		packet.writeByte (pc.basicParameters.cha);
		packet.writeByte (pc.basicParameters.intel);
		packet.writeByte (0) ;	
	}
	
	public byte[] getRaw () {
		return packet.getPacket ();
	}
}
