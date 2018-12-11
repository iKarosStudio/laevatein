package laevatein.server.packet;

import java.io.ByteArrayOutputStream;

public class PacketBuilder
{
	ByteArrayOutputStream packet = new ByteArrayOutputStream ();
	
	public PacketBuilder () {
		//Packet = new ByteArrayOutputStream () ;
		//Packet.reset () ; 
	}
	
	public void writeByte (int value) {
		packet.write (value & 0xFF) ;
	}
	
	public void writeByte (byte[] value) {
		for (byte b : value) {
			packet.write (b);
		}
	}
	
	public void writeByte (boolean value) {
		if (value) {
			packet.write (1);
		} else {
			packet.write (0);
		}
	}
	
	public void writeWord (int value) {
		packet.write (value & 0xFF);
		packet.write ((value >> 8) & 0xFF);
	}
	
	public void writeDoubleWord (int value) {
		packet.write (value & 0xFF);
		packet.write ((value >>  8) & 0xFF);
		packet.write ((value >> 16) & 0xFF);
		packet.write ((value >> 24) & 0xFF);
	}
	
	public void writeFloat (Float value) {
		//
	}
	
	public void writeString (String string) {
		if (string == null) {
			//
		} else {
			try {
				packet.write (string.getBytes ("BIG5"));
			} catch (Exception e) {
				e.printStackTrace ();
			}
		}
		packet.write (0); //結束字元
	}
	
	public void reset () {
		packet.reset ();
	}
	
	public byte[] getPacket () {
		int padding = packet.size () % 4; //封包4byte資料對齊
		
		if (padding != 0) {
			for (int i = padding; i < 4; i++) {
				packet.write (0x00);
			}
		}
		
		return packet.toByteArray ();
	}
	
	public byte[] getPacketNoPadding () {
		return packet.toByteArray ();
	}
}
