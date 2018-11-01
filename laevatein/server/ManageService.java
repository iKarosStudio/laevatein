package laevatein.server;

import java.net.*;
import java.lang.Thread;

import laevatein.config.*;
import laevatein.game.*;

public class ManageService extends Thread
{
	private static ManageService instance;
	private ServerSocket ServiceSocket;
	private boolean remoteManageEnable;
	
	Laevatein laevatein;
	
	/*
	 * 建立連接用session
	 */
	public void run () {		
		while (true) {
			try {
				if (remoteManageEnable) {
					Socket sock = ServiceSocket.accept ();
					//String ClientIp = Sock.getInetAddress ().getHostAddress ();
					
					/*
					 * 開始管理服務程式
					 */
				} else {
					//
				}
			} catch (SocketTimeoutException e) {
				//it's ok
				
			} catch (Exception e) {
				e.printStackTrace ();
			}
		}
	}
	
	public static ManageService getInstance () {
		if (instance == null) {
			instance = new ManageService ();
		}
		return instance;
	}
	
	public ManageService () {
		laevatein = Laevatein.getInstance ();
		
		/*
		 * 初始化端口監聽
		 */
		System.out.printf ("Management Port %d binding...", Configurations.MANAGE_PORT);
		try {
			ServiceSocket = new ServerSocket (Configurations.MANAGE_PORT);
			ServiceSocket.setSoTimeout (3000) ; //10s accept timeout
			System.out.printf ("success\n");
			
			remoteManageEnable = true;
			this.setName ("MANAGEMENT SERVICE");
			
		} catch (Exception e) {
			System.out.printf ("fail\n");
			System.out.printf ("[!] TCP/IP Listening fault->%s\n", e.getMessage ());
			e.printStackTrace ();
			System.exit (666);
			
		}
	}
}
