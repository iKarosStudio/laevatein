package laevatein.server.process_server;

public class ModelTalks extends _PacketFrame
{
	String name;
	int uuid;
	int opcode;
	int talkType;
	String text;
	
	
	public ModelTalks (int _uuid, String _name, String _text, int _opcode, int _talkType) {
		name = _name;
		uuid = _uuid;
		opcode = _opcode;
		talkType = _talkType;
		text = _text;
	}
	
	public void withName () {
		packet.writeByte (opcode);
		packet.writeByte (talkType);
		packet.writeDoubleWord (uuid);
		packet.writeString (name + ":" + text);
	}
	
	public void withoutName () {
		packet.writeByte (opcode);
		packet.writeByte (talkType);
		packet.writeDoubleWord (uuid);
		packet.writeString (text);
	}
}
