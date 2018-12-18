package laevatein.game.model.monster;

import java.util.*;

import laevatein.game.*;
import laevatein.game.model.*;
import laevatein.game.model.player.*;
import laevatein.game.skill.*;

import laevatein.server.opcodes.*;
import laevatein.server.packet.*;

public class MonsterInstance extends Objeto
{
	public MonsterInstance () {
		//
	}
	
	@Override
	public byte[] getPacket () {
		PacketBuilder packet = new PacketBuilder ();
		
		packet.writeByte (ServerOpcodes.MODEL_PACK);
		packet.writeWord (loc.x);
		packet.writeWord (loc.y);
		packet.writeDoubleWord (uuid);
		packet.writeWord (gfx); //外型
		packet.writeByte (actId); //動作
		packet.writeByte (heading); //方向
		packet.writeByte (light);
		packet.writeByte (moveSpeed);
		packet.writeDoubleWord (exp);
		packet.writeWord (lawful);
		packet.writeString (name);
		packet.writeString (title);
		packet.writeByte (status);
		packet.writeDoubleWord (clanId);
		packet.writeString (clanName);
		packet.writeString (null);
		packet.writeByte (0x00);
		packet.writeByte (hpScale); //血條百分比
		packet.writeByte (0x00);
		packet.writeByte (levelScale);
		packet.writeByte (0x00);
		packet.writeByte (0xFF);
		packet.writeByte (0xFF);
		
		return packet.getPacket ();
	}

	@Override
	public void boardcastPcInsight (byte[] packet) {
		List<PcInstance> pcs = Laevatein.getInstance ().getMap (loc.mapId).getPcsInsight (loc.x, loc.y);
		pcs.forEach ((PcInstance p)->{
			p.getHandle ().sendPacket (packet);
		});
	}

	@Override
	public void receiveAttack (NormalAttack attack) {
		// TODO Auto-generated method stub
	}

	@Override
	public void die () {
		// TODO Auto-generated method stub
		
	}

}
