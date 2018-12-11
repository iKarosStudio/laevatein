package laevatein.server.process_server;

import laevatein.server.opcodes.*;

//要求客戶端移除uuid物件的顯示
public class RemoveModel extends _PacketFrame
{
	public RemoveModel (int _uuid) {
		packet.writeByte (ServerOpcodes.REMOVE_OBJECT);
		packet.writeDoubleWord (_uuid);
	}
}
