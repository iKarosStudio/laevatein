package laevatein.server.packet;

public class PacketReader
{
	private int offset = 1;
	private byte[] raw;
	
	private static final int BYTE_SIZE = 1;
	private static final int WORD_SIZE = 2;
	private static final int DWORD_SIZE= 4;
	
	public PacketReader (byte[] data) {
		raw = data;
	}
	
	public byte[] readRaw () {
		byte[] result = new byte[raw.length - offset];
		try {
			System.arraycopy (raw, offset, result, 0, raw.length - offset);
			offset = raw.length;
		} catch (Exception e) {
			e.printStackTrace ();
		}
		
		return result;
	}

	public byte readByte () {
		byte b = raw[offset];
		offset += BYTE_SIZE;
		return b;
	}
	
	public int readWord () {
		int w = ((raw[offset+1] << 8) & 0xFF00) | (raw[offset] & 0xFF);
		offset += WORD_SIZE;
		return w;
	}
	
	public int readDoubleWord () {
		int dw = ((raw[offset+3] & 0xFF) << 24) | ((raw[offset+2] & 0xFF) << 16) | ((raw[offset+1] & 0xFF) << 8) | (raw[offset] & 0xFF);
		offset += DWORD_SIZE;
		return dw;
	}
	
	public String readString () {
		String parse = null;
		try{
			parse = new String (raw, offset, raw.length - offset, "BIG5");
			parse = parse.substring (0, parse.indexOf ('\0'));
			offset += parse.getBytes ("BIG5").length + 1;
			
			return parse;
		} catch (Exception e) {
			e.printStackTrace ();
			return null;
		}
	}
}
