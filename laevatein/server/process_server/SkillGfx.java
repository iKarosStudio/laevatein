package laevatein.server.process_server;

import java.util.concurrent.atomic.*;

import laevatein.server.opcodes.*;
import laevatein.game.model.*;

/* 產生技能特效 */
public class SkillGfx extends _PacketFrame
{
	private static AtomicInteger sequentialNumber = new AtomicInteger(0);
	
	public SkillGfx (Objeto src, Objeto dest, int actionId, int heading, int skillGfx) {
		packet.writeByte (ServerOpcodes.MODEL_ACTION);
		packet.writeByte (actionId);
		
		//src->dest uuid
		packet.writeDoubleWord (src.uuid);
		packet.writeDoubleWord (dest.uuid);
		
		//命中表現
		packet.writeByte (6); //hit
		//packet.writeByte (0); //not hit
		
		//heading
		packet.writeByte (heading);
		
		//serial code
		packet.writeDoubleWord (sequentialNumber.getAndIncrement ());
		
		//SkillGfx
		packet.writeWord (skillGfx);
		
		//unknown
		packet.writeByte (127);
		
		//發動方接受方座標
		packet.writeWord (src.loc.p.x);
		packet.writeWord (src.loc.p.y);
		packet.writeWord (dest.loc.p.x);
		packet.writeWord (dest.loc.p.y);
		
		packet.writeWord (0);
		packet.writeByte (0);
	}
	
}
