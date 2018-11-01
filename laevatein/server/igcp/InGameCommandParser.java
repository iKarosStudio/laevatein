package laevatein.server.igcp;

import laevatein.server.*;
import laevatein.server.process_server.*;
import laevatein.game.model.player.*;

public class InGameCommandParser
{
	SessionHandler handle;
	PcInstance pc;
	String text;
	
	public InGameCommandParser (SessionHandler _handle, String _text) {
		handle = _handle;
		pc = handle.user.activePc;
		text = _text;
	}
	
	public void setText (String _text) {
		text = _text;
	}
	
	public boolean parse () {
		boolean isValid = true;
		
		if (text.startsWith (".rdoff")) {
			pc.isRd = false;
			handle.sendPacket (new SystemMessage ("關閉RD命令解析").getRaw ());
			
		} else if (text.startsWith (".tp")) { //傳送自己
			new RdTeleport (pc, text);
			
		} else if (text.startsWith (".create")) { //創造道具
			new RdCreateItem (pc, text);
			
		} else if (text.startsWith (".pc")) { //顯示pc資料
			new RdPcData (pc, text);
			
		} else if (text.startsWith (".status")) { //改變status參數
			new RdPcStatus (pc, text);
		} else if (text.startsWith (".server")) { //顯示伺服器資料
			//
		} else if (text.startsWith (".map")) { //顯示地圖資料
			//
		} else if (text.startsWith (".tile")) { //顯示座標資料
			//
		} else {
			isValid = false;
		}
		
		return isValid;
	}
}
