package laevatein.server.process_server;

import laevatein.server.packet.*;
import laevatein.server.opcodes.*;

public class UseMap extends _PacketFrame
{
	public UseMap (int uuid, int itemId) {
		int mapValue = 0;
		
		packet.writeByte (ServerOpcodes.USE_MAP);
		packet.writeDoubleWord (uuid);
		
		switch (itemId) {
		case 40373: //大陸全圖
			mapValue = 16; break;
		case 40374: //說話之島
			mapValue = 1; break;
		case 40375: //古魯丁
			mapValue = 2; break;
		case 40376: //肯特
			mapValue = 3; break;
		case 40377: //燃柳
			mapValue = 4; break;
		case 40378: //妖精森林
			mapValue = 5; break;
		case 40379: //風木
			mapValue = 6; break;
		case 40380: //銀騎士村莊
			mapValue = 7; break;
		case 40381: //龍之谷
			mapValue = 8; break;
		case 40382: //奇岩
			mapValue = 9; break;
		case 40383: //歌唱之島
			mapValue = 10; break;
		case 40384: //隱藏之谷
			mapValue = 11; break;
		case 40385: //海音
			mapValue = 12; break;
		case 40386: //威頓
			mapValue = 13; break;
		case 40387: //歐瑞
			mapValue = 14; break;
		case 40388: //亞丁
			mapValue = 15; break;
		case 40389: //沉默洞穴
			mapValue = 17; break;
		case 40390: //海賊島
			mapValue = 18; break;
		default :
			mapValue = 1; break;
		}
		
		packet.writeDoubleWord (mapValue) ;
	}
}
