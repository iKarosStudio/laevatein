package laevatein.server.process_client;

import laevatein.server.*;
import laevatein.server.packet.*;
import laevatein.server.process_server.*;
import laevatein.game.*;
import laevatein.game.model.player.*;
import laevatein.game.model.npc.*;


/*
 * 由使用者發起一個NPC(uuid)的內容訪問
 * 參考C_NPCTalk & NpcActionTable
 */

public class NpcAccess
{
	
	public NpcAccess (SessionHandler handle, byte[] data) {
		PacketReader packetReader = new PacketReader (data) ;
		PcInstance pc = handle.user.activePc;
		int npcUuid = packetReader.readDoubleWord ();
		
		System.out.printf ("%s access npc(%d)\n", pc.name, npcUuid);
		
		if (npcUuid == 70522) { //甘特
			String htmlKey = null;
			switch (pc.type) {
			case 0:
				htmlKey = "gunterp9"; break;
			case 1:
				htmlKey = "gunterk9"; break;
			case 2:
				htmlKey = "guntere1"; break;
			case 3:
				htmlKey = "gunterw1"; break;
			case 4:
				htmlKey = "gunterde1"; break;
			}
			
			byte[] packet = new NpcAccessResult (npcUuid, htmlKey).getPacket ();
			handle.sendPacket (packet) ;
			return ;
		}
		
		if (npcUuid == 70009) { //吉倫
			String htmlKey = null;
			switch (pc.type) {
			case 0: //Roayn
				htmlKey = "gerengp1"; break;
			case 1: //Knight
				htmlKey = "gerengk1"; break;
			case 2: //Elf
				htmlKey = "gerenge1"; break;
			case 3: //Mage
				htmlKey = "gerengw3"; break;
			case 4: //DarkElf
				htmlKey = "gerengde1"; break;
			default:
				break;
			}
			
			byte[] packet = new NpcAccessResult (npcUuid, htmlKey).getPacket () ;
			handle.sendPacket (packet) ;
			return ;
		}
		
		if (CacheData.npcTalkData.containsKey (npcUuid) ) {
			NpcTalkData talkData = CacheData.npcTalkData.get (npcUuid);
			NpcAccessResult result;
			
			if (pc.lawful < 0) {//邪惡
				result = new NpcAccessResult (npcUuid, talkData.caoticAction);
			} else {//中立, 正義
				result = new NpcAccessResult (npcUuid, talkData.normalAction);
			}
			
			handle.sendPacket (result.getPacket ());
			
		} else {
			System.out.printf ("找不到NPCID\n") ;
		}
	}
}
