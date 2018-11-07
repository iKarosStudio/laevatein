package laevatein.server.process_server;

import laevatein.server.*;
import laevatein.server.opcodes.*;
import laevatein.server.packet.*;
import laevatein.game.model.player.*;

/* 報告玩家角色各項數值 */
public class ModelStatus
{
	PacketBuilder packet = new PacketBuilder ();
	
	public ModelStatus (PcInstance p) {
		ServerTime serverTime = ServerTime.getInstance ();
		try {
			packet.writeByte (ServerOpcodes.UPDATE_MODEL_STATUS);
			packet.writeDoubleWord (p.uuid); //id
			packet.writeByte (p.level);
			packet.writeDoubleWord (p.exp);
			packet.writeByte (p.getStr ()); //str
			packet.writeByte (p.getIntel ()); //intel
			packet.writeByte (p.getWis ()); //wis
			packet.writeByte (p.getDex ()); //dex
			packet.writeByte (p.getCon ()); //con
			packet.writeByte (p.getCha ()); //cha
			packet.writeWord (p.hp);
			packet.writeWord (p.getMaxHp ()); //max hp
			packet.writeWord (p.mp);
			packet.writeWord (p.getMaxMp ()); //max mp
			packet.writeByte (p.getAc ()); //ac
			packet.writeDoubleWord (serverTime.getTime ()); //time
			packet.writeByte (p.satiation);
			packet.writeByte (p.weightScale30);//weight 0~30 = 0~100%, 1:3.4
			packet.writeWord (p.lawful);
			packet.writeByte (0);//fire
			packet.writeByte (0);//water
			packet.writeByte (0);//wind
			packet.writeByte (0);//earth
			
		} catch (Exception e) {
			e.printStackTrace ();
		}
	}
	
	public byte[] getRaw () {
		return packet.getPacketNoPadding ();
	}
}
