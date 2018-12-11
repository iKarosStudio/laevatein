package laevatein.server.process_server;

import laevatein.server.opcodes.*;

//告知客戶端NPC含有對話內容的HTML ID編號
public class NpcAccessResult extends _PacketFrame
{
	public NpcAccessResult (int npcId, String htmlKey) {
		
		packet.writeByte (ServerOpcodes.NPC_RESULT);
		packet.writeDoubleWord (npcId);
		packet.writeString (htmlKey);
		//有參數在這邊帶入
		//參考S_NPCTalksReturn.java
		packet.writeWord (0x00);
		packet.writeWord (0x00);

	}
}
