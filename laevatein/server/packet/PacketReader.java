package laevatein.server.packet;

public class PacketReader
{
	private int offset = 1;
	private byte[] packet;
	
	private static final int BYTE_SIZE = 1;
	private static final int WORD_SIZE = 2;
	private static final int DWORD_SIZE= 4;
	
	public PacketReader (byte[] data) {
		packet = data;
	}
	
	public byte[] readRaw () {
		byte[] result = new byte[packet.length - offset];
		try {
			System.arraycopy (packet, offset, result, 0, packet.length - offset);
			offset = packet.length;
		} catch (Exception e) {
			e.printStackTrace ();
		}
		
		return result;
	}

	public byte readByte () {
		byte b = packet[offset];
		offset += BYTE_SIZE;
		return b;
	}
	
	public int readWord () {
		int w = ((packet[offset+1] << 8) & 0xFF00) | (packet[offset] & 0xFF);
		offset += WORD_SIZE;
		return w;
	}
	
	public int readDoubleWord () {
		int dw = ((packet[offset+3] & 0xFF) << 24) | ((packet[offset+2] & 0xFF) << 16) | ((packet[offset+1] & 0xFF) << 8) | (packet[offset] & 0xFF);
		offset += DWORD_SIZE;
		return dw;
	}
	
	public String readString () {
		String parse = null;
		try{
			parse = new String (packet, offset, packet.length - offset, "BIG5");

			parse = parse.substring (0, parse.indexOf ('\0'));
			offset += parse.getBytes ("BIG5").length + 1;

		} catch (Exception e) {
			System.out.printf ("raw:");
			for (byte b : packet) {
				System.out.printf ("0x%02X ", b);
			}
			System.out.println ();
			
			e.printStackTrace ();
		}
		
		return parse;
	}
	
	public String readAsciiString () {
		String parse = null;
		try{			
			parse = new String (packet, offset, packet.length - offset);
			byte[] p = parse.getBytes ();
			
			int i = 0;
			for (byte b : p) {
				if (b == 0x00) {
					break;
				}
				i++;
			}

			//parse = parse.substring (0, parse.indexOf ("\0"));
			parse = parse.substring (0, i);
			offset += i + 1;

		} catch (Exception e) {
			System.out.printf ("raw:");
			for (byte b : packet) {
				System.out.printf ("0x%02X ", b);
			}
			System.out.println ();
			
			e.printStackTrace ();
		}
		
		return parse;
	}
	
	public byte[] getPacket () {
		return packet;
	}
}
