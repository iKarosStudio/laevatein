package laevatein.server.packet;


public class PacketCodec
{
	public int[] decodeKeyL = {0, 0} ;
	public int[] encodeKeyL = {0, 0} ;
	private byte[] decodeKey = new byte[8] ;
	private byte[] encodeKey = new byte[8] ;
	
	public synchronized void updateDecodeKey (byte[] Data) {
		int mask = ((Data[3] & 0xFF) << 24) | ((Data[2] & 0xFF) << 16) | ((Data[1] & 0xFF) << 8) | (Data[0] & 0xFF);
		decodeKeyL[0] ^= mask;
		decodeKeyL[1] += 0x287EFFC3;
		
		for (int i = 0; i < 8; i++) {
			if (i < 4) {
				//DecodeKey[i] = (byte) ((DecodeKeyL[0] >> (i<<3)) & 0xFF) ;
				decodeKey[i] = (byte) (decodeKeyL[0] >>> (i<<3));
			} else {
				//DecodeKey[i] = (byte) ((DecodeKeyL[1] >> ((i - 4) << 3) ) & 0xFF) ;
				decodeKey[i] = (byte) (decodeKeyL[1] >>> ((i - 4) << 3));
			}
		}
	}
	
	public synchronized void updateEncodeKey (byte[] Data) {
		int mask = ((Data[3] & 0xFF) << 24) | ((Data[2] & 0xFF) << 16) | ((Data[1] & 0xFF) << 8) | (Data[0] & 0xFF);
		encodeKeyL[0] ^= mask;
		encodeKeyL[1] += 0x287EFFC3;
		
		for (int i = 0; i < 8; i++) {
			if (i < 4) {
				//EncodeKey[i] = (byte) ((EncodeKeyL[0] >> (i << 3) ) & 0xFF) ;
				encodeKey[i] = (byte) (encodeKeyL[0] >>> (i << 3));
			} else {
				//EncodeKey[i] = (byte) ((EncodeKeyL[1] >> ((i - 4) << 3) ) & 0xFF) ;
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
	
	public byte[] encode (byte[] Data) {
		int Size = Data.length;
		byte[] Raw = Data.clone ();
		
		Raw[0] ^= encodeKey[0];
		
		for (int i = 1; i < Size; i++) {
			Raw[i] ^= (Raw[i - 1] ^ encodeKey[i & 0x07]);
		}
		
		Raw[3] = (byte) (Raw[3] ^ encodeKey[2]) ;
		Raw[2] = (byte) (Raw[2] ^ Raw[3] ^ encodeKey[3]);
		Raw[1] = (byte) (Raw[1] ^ Raw[2] ^ encodeKey[4]);
		Raw[0] = (byte) (Raw[0] ^ Raw[1] ^ encodeKey[5]);
		
		int EncodedDataSize = Size + 2; 
		byte[] EncodedData = new byte[EncodedDataSize];
		
		EncodedData[0] = (byte) (EncodedDataSize & 0xFF);
		EncodedData[1] = (byte) ((EncodedDataSize >> 8) & 0xFF);
		
		System.arraycopy (Raw, 0, EncodedData, 2, Size);
		updateEncodeKey (Data);
		return EncodedData;
	}
	
	public void decode (byte[] Data, int Size) {
		byte b3 = Data[3];
		Data[3] ^= decodeKey[2];
		
		byte b2 = Data[2]; 
		Data[2] ^= (b3 ^ decodeKey[3]);
		
		byte b1 = Data[1];
		Data[1] ^= (b2 ^ decodeKey[4]);
		
		byte k = (byte) (Data[0] ^ b1 ^ decodeKey[5]);
		Data[0] = (byte) (k ^ decodeKey[0]);
		
		for (int Index = 1; Index < Size; Index++) {
			byte t = Data[Index];
			Data[Index] ^= (decodeKey[Index & 0x07] ^ k);
			k = t;
		}
	}
}
