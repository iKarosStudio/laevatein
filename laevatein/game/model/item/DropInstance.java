package laevatein.game.model.item;

import java.util.*;

import laevatein.types.*;
import laevatein.game.*;
import laevatein.game.model.*;
import laevatein.game.model.player.*;
import laevatein.game.skill.*;
import laevatein.server.opcodes.*;
import laevatein.server.packet.*;

//掉在地上的物品敘述
public class DropInstance extends Objeto
{	
	ItemInstance instance;
	
	public DropInstance (ItemInstance drop) {
		loc = new Location ();
		instance = drop;
		
		uuid = drop.uuid;
		gfx = drop.gfx;
		actId = 0;
		heading = 0;
		light = 0;
		moveSpeed = 0;
		exp = drop.count;
		lawful = 0;
		name = drop.getName ();
		title = null;
		status = 0;
		clanId = 0;
		clanName = null;
	}
	
	public void setLocation (Location _loc) {
		loc = _loc;
	}
	
	public ItemInstance getItemInstance () {
		return instance;
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
		packet.writeDoubleWord (1); //exp
		packet.writeWord (lawful);
		packet.writeString (name);
		packet.writeString (title);
		packet.writeByte (status);
		packet.writeDoubleWord (clanId);
		packet.writeString (clanName);
		packet.writeString (null);
		packet.writeByte (0x00);
		packet.writeByte (0xFF); //血條百分比
		packet.writeByte (0x00);
		packet.writeByte (0x00);
		packet.writeByte (0x00);
		packet.writeByte (0xFF);
		packet.writeByte (0xFF);
		
		return packet.getPacket ();
	}

	@Override
	public void boardcastPcInsight (byte[] packet) {
		List<PcInstance> pcs = Laevatein.getInstance ().getMap (loc.mapId).getPcsInsight (loc.x, loc.y);
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
