package laevatein.server;

import java.io.*;
import java.net.*;

import laevatein.server.packet.*;

public class SessionHandler extends Thread implements Runnable
{
	private Socket sock;
	
	public Account user;
	
	private InputStream inStream;
	private OutputStream outStream;
	
	private PacketHandler packetHandle;
	private PacketCodec packetCodec;
	
	private static final byte[] INIT_PACKET = {
			(byte) 0xB1, (byte) 0x3C, (byte) 0x2C, (byte) 0x28, (byte) 0xF6,
			(byte) 0x65, (byte) 0x1D, (byte) 0xDD, (byte) 0x56, (byte) 0xE3, 
			(byte) 0xEF
	};
		
	public void firstPacket () {
		byte[] initPacket = new byte[18];
		
		initPacket[0] = (byte) (INIT_PACKET.length + 7);
		initPacket[1] = (byte) 0;
		initPacket[2] = (byte) 0x20;
		initPacket[3] = (byte) 0xEC;
		initPacket[4] = (byte) 0x90;
		initPacket[5] = (byte) 0xC6;
		initPacket[6] = (byte) 0x5C;
		System.arraycopy (INIT_PACKET, 0, initPacket, 7, INIT_PACKET.length);
		
		try {
			outStream.write (initPacket);
			outStream.flush ();
			
		} catch (Exception e) {
			e.printStackTrace ();
			
		}
	}
	
	/* 接收加密封包回傳已解密封包  */
	public byte[] receivePacket () throws IOException {
		try {
			int sizeHi = inStream.read ();
			int sizeLo = inStream.read ();
			int size = ((sizeLo << 8) | sizeHi) - 2;
			
			byte[] packet = new byte[size];	
			
			inStream.read (packet);
			packetCodec.decode (packet, size);
			packetCodec.updateDecodeKey (packet);
			
			return packet;
		} catch (IOException e) {
			throw e;
		}
	}
	
	public synchronized void sendPacket (byte[] packet)  {
		byte[] raw = null;
		try {
			raw = packetCodec.encode (packet);
			//Codec.UpdateEncodeKey (Data) ;
			/*
			System.out.printf ("[OUT:0x%08x, 0x%08x]:", Codec.EncodeKeyL[0], Codec.EncodeKeyL[1]) ;
			for (byte b : Data) {
				System.out.printf ("0x%02X ", b) ;
			}
			System.out.print ("\n") ;
			*/
			outStream.write (raw);
			outStream.flush ();
			
		} catch (SocketException e) {
			System.out.printf ("%s:%d-socket close\n", getIP (), getPort ());
			if (user.activePc != null) {
				user.activePc.save (); //offline
			}
			
		} catch (Exception e) {	
			e.printStackTrace ();
		}
	}
	
	//user session主要工作迴圈
	public void run () {

		firstPacket ();
		packetCodec.initKey ();
		
		System.out.printf ("thread-id:%d, priority:%d\n", getThreadId (), getThreadPriority ());
		
		while (true) {
			try {
				byte[] recvData = receivePacket (); //Get decoded data
				
				/* 處理客戶端封包  */
				packetHandle.process (recvData);
				
			} catch (SocketException s) {
				/* 連線中斷 */
				break;
				
			} catch (NegativeArraySizeException e) {
				System.out.printf ("socket ping detecttion:%s:%d\n", getIP (), getPort ());
				break;
				
			} catch (Exception e) {
				e.printStackTrace ();
				break;
			}
		} //while true
		
		/* 斷線後該做的事  */
		if (user != null) {
			try {
				if (user.activePc != null) {
					if (!user.activePc.isExit) { //若不是客戶端主動離線
						user.activePc.save ();
					}
				}
				
				user.activePc = null;
				
				user.updateLastLogin ();
				sock.close ();
				
				System.out.printf ("[DISCONNECT]IP:%s [HOST:%s]\n", 
						sock.getInetAddress().getHostAddress ().toString (),
						sock.getInetAddress ().getHostName ());
				
			} catch (Exception e) {
				e.printStackTrace ();
			}
			user = null;
		}
	}
	
	public SessionHandler (Socket _sock) {
		try {
			sock = _sock;
			
			inStream = sock.getInputStream ();
			outStream = new BufferedOutputStream(sock.getOutputStream ());
			//sock.setKeepAlive (true);
			
			packetCodec = new PacketCodec ();
			packetHandle = new PacketHandler (this);
			
			setName ("user_session:not login");
			
		} catch (Exception e) {
			e.printStackTrace ();
		}
	}
	
	public void setThreadName () {
		setThreadName ("user_session:" + user.name);
	}
	
	public void setThreadName (String threadName) {
		this.setName (threadName);
	}
	
	public long getThreadId () {
		return getId ();
	}
	
	public int getThreadPriority () {
		return getPriority ();
	}
	
	public String getIP () {
		return sock.getInetAddress ().getHostAddress ();
	}
	
	public String getHostName () {
		return sock.getInetAddress ().getHostName ();
	}
	
	public int getPort () {
		return sock.getPort ();
	}
	
	public PacketCodec getCodec () {
		return packetCodec;
	}

	public OutputStream getOutputStream () {
		return outStream;
	}
	
	public InputStream getInputStream () {
		return inStream;
	}
	
	public boolean isClosed () {
		return sock.isClosed ();
	}
	
	public void disconnect () {
		try {
			this.interrupt ();
			sock.close ();
			
		} catch (Exception e) {
			e.printStackTrace ();
		}
	}
}
