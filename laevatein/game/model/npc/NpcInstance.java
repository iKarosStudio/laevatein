package laevatein.game.model.npc;

import laevatein.types.*;

import java.util.List;

import laevatein.game.Laevatein;
import laevatein.game.model.*;
import laevatein.game.model.player.PcInstance;
import laevatein.game.skill.NormalAttack;
import laevatein.game.template.*;
import laevatein.server.opcodes.*;
import laevatein.server.packet.*;

public class NpcInstance extends Objeto
{
	public String nameId;
	
	public NpcInstance (NpcTemplate _template) {
		hp = _template.basicParameters.maxHp;
		uuid = _template.uuid;
		gfx = _template.gfx;
		name = _template.name;
		nameId = _template.nameId;
		level = _template.level;
		
		loc = new Location ();
	}
	
	

	@Override
	public byte[] getPacket () {
		PacketBuilder packet = new PacketBuilder ();
		
		packet.writeByte (ServerOpcodes.MODEL_PACK);
		packet.writeWord (loc.p.x);
		packet.writeWord (loc.p.y);
		packet.writeDoubleWord (uuid);
		packet.writeWord (gfx); //外型
		packet.writeByte (actId); //動作
		packet.writeByte (heading); //方向
		packet.writeByte (light);
		packet.writeByte (moveSpeed);
		packet.writeDoubleWord (exp);
		packet.writeWord (lawful);
		packet.writeString (nameId);
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
		List<PcInstance> pcs = Laevatein.getInstance ().getMap (loc.mapId).getPcsInsight (loc.p);
			pcs.forEach ((PcInstance p)->{p.getHandle ().sendPacket (packet);
		});
	}

	@Override
	public void receiveAttack (NormalAttack attack) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void die () {
		//
	}
	
}
