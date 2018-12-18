package laevatein.server.process_server;

import laevatein.server.opcodes.*;
import laevatein.game.model.player.*;

/* 新增一個角色到選擇畫面 */
public class NewCharacterPack extends _PacketFrame
{
	public NewCharacterPack (PcInstance pc) {
		packet.writeByte (ServerOpcodes.NEW_CHARACTER_PACK) ;
		packet.writeString (pc.getName ());
		packet.writeString (pc.getTitle ());
		packet.writeByte (pc.getType ());
		packet.writeByte (pc.getSex ());
		packet.writeWord (pc.getLawful ());
		packet.writeWord (pc.basicParameters.getHp ());
		packet.writeWord (pc.basicParameters.getMp ());
		packet.writeByte (pc.basicParameters.getAc ());
		packet.writeByte (pc.getLevel ());
		packet.writeByte (pc.basicParameters.getStr ());
		packet.writeByte (pc.basicParameters.getDex ());
		packet.writeByte (pc.basicParameters.getCon ());
		packet.writeByte (pc.basicParameters.getWis ());
		packet.writeByte (pc.basicParameters.getCha ());
		packet.writeByte (pc.basicParameters.getInt ());
		packet.writeByte (0);	
	}
}
