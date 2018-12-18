package laevatein.server.process_client;

import laevatein.server.*;

public class ExitGame
{
	public ExitGame (SessionHandler handle, byte[] packet) {
		if (handle.getUser () != null) {
			Account user = handle.getUser ();
			if (user.getActivePc () != null) {
				try {
					user.getActivePc ().isExit = true;
					user.getActivePc ().save ();
					
					/* 20180411
					 * 正常離開遊戲, 要多加一個旗標判斷
					 * 否則在sessionhandler exception捕捉斷線會kick()重複執行
					 */
					
					handle.disconnect ();
					
				} catch (Exception e) {
					e.printStackTrace ();
				}
			}
		}
		
		
		/* @Deprecated
		if (handle.user != null && 
			handle.user.activePc != null) {
			try {
				if (handle.user != null && handle.user.activePc != null) {
					handle.user.activePc.isExit = true;
					handle.user.activePc.save ();
					
					handle.disconnect ();
				}
			} catch (Exception e) {
				e.printStackTrace ();
			}
		}*/	
	}
}
