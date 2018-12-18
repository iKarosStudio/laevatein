package laevatein.server.process_server;

import laevatein.server.*;
import laevatein.server.opcodes.*;
import laevatein.game.model.player.*;

/* 報告玩家角色各項數值 */
public class ModelStatus extends _PacketFrame
{
	public ModelStatus (PcInstance pc) {
		ServerTime serverTime = ServerTime.getInstance ();
		try {
			packet.writeByte (ServerOpcodes.UPDATE_MODEL_STATUS);
			packet.writeDoubleWord (pc.getUuid ()); //id
			packet.writeByte (pc.getLevel ());
			packet.writeDoubleWord (pc.getExp ());
			packet.writeByte (pc.getStr ()); //str
			packet.writeByte (pc.getIntel ()); //intel
			packet.writeByte (pc.getWis ()); //wis
			packet.writeByte (pc.getDex ()); //dex
			packet.writeByte (pc.getCon ()); //con
			packet.writeByte (pc.getCha ()); //cha
			packet.writeWord (pc.hp);
			packet.writeWord (pc.getMaxHp ()); //max hp
			packet.writeWord (pc.mp);
			packet.writeWord (pc.getMaxMp ()); //max mp
			packet.writeByte (pc.getAc ()); //ac
			packet.writeDoubleWord (serverTime.getTime ()); //time
			packet.writeByte (pc.getSatiation ());
			packet.writeByte (pc.weightScale30);//weight 0~30 = 0~100%, 1:3.4
			packet.writeWord (pc.getLawful ());
			packet.writeByte (0);//fire
			packet.writeByte (0);//water
			packet.writeByte (0);//wind
			packet.writeByte (0);//earth
			
		} catch (Exception e) {
			e.printStackTrace ();
		}
	}
}
