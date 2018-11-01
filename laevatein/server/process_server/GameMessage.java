package laevatein.server.process_server;

import laevatein.server.opcodes.*;
import laevatein.server.packet.*;

//參考
//https://github.com/tpai/docker-l1jtw-server/blob/master/L1J-TW_3.50c/src/l1j/server/server/model/identity/L1SystemMessageId.java
public class GameMessage
{
	PacketBuilder packet = new PacketBuilder ();
	
	public GameMessage (int _messageId) {
		this (_messageId, (String[]) null);
	}
	
	//public GameMessage (int _messageId, String[] _messageArgs) {
	public GameMessage (int _messageId, String... _messageArgs) {
		packet.writeByte (ServerOpcodes.SERVER_MSG) ;
		packet.writeWord (_messageId);
		
		if (_messageArgs == null) {
			packet.writeByte (0);
		} else {
			packet.writeByte (_messageArgs.length) ;
			for (String _messageArg : _messageArgs) {
				packet.writeString (_messageArg);
			}
		}
	}
	
	public byte[] getRaw () {
		return packet.getPacket ();
	}
}
