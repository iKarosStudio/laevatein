package laevatein.server;

import java.net.*;
import java.sql.*;
import java.lang.Thread;

import laevatein.config.*;
import laevatein.server.threadpool.*;
import laevatein.server.database.*;

public class MainService extends Thread
{
	private static MainService instance;
	private ServerSocket serviceSocket;
	private boolean loginEnable;
	
	/* 建立連接用session */
	public void run () {		
		while (true) {
			try {
				if (loginEnable) {
					Socket clientSock = serviceSocket.accept ();
					
					/* 敏感IP 捕抓 */
					/*
					String clientIP = clientSock.getInetAddress ().getHostAddress ();
					if (ClientIp.equalsIgnoreCase ("127.0.0.1") ) {
						System.out.printf ("捕捉目標IP:%s\n", ClientIp) ;
					}
					*/ 
					
					/* 開始遊戲服務進程 */
					SessionHandler clientSession = new SessionHandler (clientSock);
					ServiceThreadPool.getInstance().execute (clientSession);
				} else {
					Thread.sleep (500);
				}
			} catch (SocketTimeoutException e) {
				//it's ok
			} catch (Exception e) {
				e.printStackTrace ();
			}
		}
	}
	
	public static MainService getInstance () {
		if (instance == null) {
			instance = new MainService ();
		}
		return instance;
	}
	
	/* 建立伺服器socket */
	public MainService () {
		/* 初始化端口監聽 */
		System.out.printf ("Socket Port %d binding...", Configurations.SERVER_PORT);
		try {
			serviceSocket = new ServerSocket (Configurations.SERVER_PORT);
			serviceSocket.setSoTimeout (10000); //10s accept timeout
			System.out.printf ("success\n");
			
			this.setName ("LOGIN SERVICE");
			
			/* 清空所有人物上線狀態 */
			PreparedStatement ps = HikariCP.getConnection ().prepareStatement ("UPDATE characters SET OnlineStatus=? WHERE OnlineStatus=?;");
			ps.setInt (1, 0);
			ps.setInt (2, 1);
			ps.execute ();
			
			loginEnable = false;
			
		} catch (BindException e) {
			System.out.printf ("bind Port:%d fail\n", Configurations.SERVER_PORT);
			System.exit (666);
			
		} catch (Exception e) {
			System.out.printf ("fail\n") ;
			System.out.printf ("[!] TCP/IP Listening fault->%s\n", e.getMessage ());
			e.printStackTrace ();
			System.exit (666);
		}
	}
	
	public boolean getLogin () {
		return loginEnable;
	}
	
	public void loginEnable () {
		loginEnable = true;
		System.out.println ("login enable");
	}
	
	public void loginDisable () {
		loginEnable = false;
		System.out.println ("login disable");
	}
}
