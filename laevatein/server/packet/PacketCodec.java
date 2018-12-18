package laevatein.server.packet;

public class PacketCodec
{
	int[] decodeKeyL = {0, 0};
	int[] encodeKeyL = {0, 0};
	byte[] decodeKey = new byte[8];
	byte[] encodeKey = new byte[8];
	
	public synchronized void updateDecodeKey (byte[] packet) {
		int mask = ((packet[3] & 0xFF) << 24) | ((packet[2] & 0xFF) << 16) | ((packet[1] & 0xFF) << 8) | (packet[0] & 0xFF);
		decodeKeyL[0] ^= mask;
		decodeKeyL[1] += 0x287EFFC3;
		
		for (int i = 0; i < 8; i++) {
			if (i < 4) {
				decodeKey[i] = (byte) (decodeKeyL[0] >>> (i<<3));
			} else {
				decodeKey[i] = (byte) (decodeKeyL[1] >>> ((i - 4) << 3));
			}
		}
	}
	
	public synchronized void updateEncodeKey (byte[] packet) {
		int mask = ((packet[3] & 0xFF) << 24) | ((packet[2] & 0xFF) << 16) | ((packet[1] & 0xFF) << 8) | (packet[0] & 0xFF);
		encodeKeyL[0] ^= mask;
		encodeKeyL[1] += 0x287EFFC3;
		
		for (int i = 0; i < 8; i++) {
			if (i < 4) {
				encodeKey[i] = (byte) (encodeKeyL[0] >>> (i << 3));
			} else {
				encodeKey[i] = (byte) (encodeKeyL[1] >>> ((i - 4) << 3));
			}
		}
	}
	
	public void initKey () {
		decodeKeyL[0] = 0x2EAE07B2;
		decodeKeyL[1] = 0xC1D339C3;
		
		encodeKeyL[0] = 0x2EAE07B2;
		encodeKeyL[1] = 0xC1D339C3;
		
		//Init Key : 0xB2 0x07 0xAE 0x2E 0xC3 0x39 0xD3 0xC1
		for (int i = 0; i < 8; i++) {
			if (i < 4) {
				decodeKey[i] = (byte) ((decodeKeyL[0] >> (i<<3)) & 0xFF);
				encodeKey[i] = decodeKey[i];
			} else {
				decodeKey[i] = (byte) ((decodeKeyL[1] >> ((i - 4)<<3)) & 0xFF);
				encodeKey[i] = decodeKey[i];
			}
		}
	}
	
	public byte[] encode (byte[] packet) {
		int size = packet.length;
		byte[] raw = packet.clone ();
		
		raw[0] ^= encodeKey[0];
		
		for (int i = 1; i < size; i++) {
			raw[i] ^= (raw[i - 1] ^ encodeKey[i & 0x07]);
		}
		
		raw[3] = (byte) (raw[3] ^ encodeKey[2]) ;
		raw[2] = (byte) (raw[2] ^ raw[3] ^ encodeKey[3]);
		raw[1] = (byte) (raw[1] ^ raw[2] ^ encodeKey[4]);
		raw[0] = (byte) (raw[0] ^ raw[1] ^ encodeKey[5]);
		
		int encodedDataSize = size + 2; 
		byte[] encodedData = new byte[encodedDataSize];
		
		encodedData[0] = (byte) (encodedDataSize & 0xFF);
		encodedData[1] = (byte) ((encodedDataSize >> 8) & 0xFF);
		
		System.arraycopy (raw, 0, encodedData, 2, size);
		updateEncodeKey (packet);
		return encodedData;
	}
	
	public void decode (byte[] packet, int size) {
		byte b3 = packet[3];
		packet[3] ^= decodeKey[2];
		
		byte b2 = packet[2]; 
		packet[2] ^= (b3 ^ decodeKey[3]);
		
		byte b1 = packet[1];
		packet[1] ^= (b2 ^ decodeKey[4]);
		
		byte k = (byte) (packet[0] ^ b1 ^ decodeKey[5]);
		packet[0] = (byte) (k ^ decodeKey[0]);
		
		for (int Index = 1; Index < size; Index++) {
			byte t = packet[Index];
			packet[Index] ^= (decodeKey[Index & 0x07] ^ k);
			k = t;
		}
	}
}
