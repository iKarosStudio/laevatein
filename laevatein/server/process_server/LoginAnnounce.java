package laevatein.server.process_server;

import laevatein.server.*;
import laevatein.server.packet.*;
import laevatein.server.opcodes.*;

/*
 * 登入公告訊息
 */
public class LoginAnnounce
{
	public LoginAnnounce (SessionHandler handle) {
		PacketBuilder packet = new PacketBuilder ();
		String defaultMessage = String.format ("帳號:%s\n密碼:%s\n登入IP:%s\n", handle.user.name, handle.user.password, handle.getIP () ) ;
		
		packet.writeByte (ServerOpcodes.LOGIN_WELCOME_MSG);
		packet.writeString (defaultMessage);
		handle.sendPacket (packet.getPacket ());
	}
	
	public LoginAnnounce (SessionHandler handle, String message) {
		PacketBuilder packet = new PacketBuilder ();
		
		packet.writeByte (ServerOpcodes.LOGIN_WELCOME_MSG);
		packet.writeString (message);
		handle.sendPacket (packet.getPacket ());
	}
}
