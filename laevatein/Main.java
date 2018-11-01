package laevatein;

import laevatein.config.*;
import laevatein.gui.*;

import laevatein.server.*;
import laevatein.server.database.*;
import laevatein.server.threadpool.*;

import laevatein.game.*;

/* JVM 啟動參數

	-Xincgc -server

 */

public class Main
{
	public static void main (String[] args) throws InterruptedException {
		System.out.println ("<LAEVATEIN>");
		//System.out.printf ("KERNEL AUTHOR:%s\n", Configurations.AUTHOR);
		//System.out.printf ("SERVER OS:%s-%s\n", System.getProperty ("os.name"), System.getProperty ("os.arch"));
		
		/* 載入參數設定 */
		ConfigurationLoader.getInstance ();
		
		/* 建立GUI管理介面 */
		if (Configurations.USE_GUI) {
			GuiMain.getInstance ();
		}
		
		/* 建立資料庫連結 */
		HikariCP.getInstance ();
		
		/* 建立系統執行緒池 */
		KernelThreadPool.getInstance ();
		ServiceThreadPool.getInstance ();
		
		/* 建立遊戲世界 */
		Laevatein lae = Laevatein.getInstance ();
		lae.initialize ();
		
		/* 建立客戶用TCP/IP端口 */
		MainService loginService = MainService.getInstance ();
		loginService.start ();
		
		/* 建立管理用TCP/IP端口 */
		//ManageService manageService = ManageService.getInstance ();
		//manageService.start ();
		
		SystemMonitor systemMonitor = SystemMonitor.getInstance ();
		KernelThreadPool.getInstance ().ScheduleAtFixedRate (systemMonitor, 500, 1000);
			
		
		/* 實作賭場系統 */
		System.out.println ("implement casino:" + Configurations.CASINO);
		if (Configurations.CASINO) {
			//
		}
		
		/* Server platform info */
		System.out.printf ("cpu core  :%d\n", SystemMonitor.getInstance ().cpuCount);
		System.out.printf ("system_pid:%s\n", SystemMonitor.getInstance ().pid);
		System.out.printf ("run on:%s\n", System.getProperty("java.vm.name"));

		/* 掛載關機程序 */
		Runtime.getRuntime().addShutdownHook (new Shutdown ());
		
		/* 本地控制台 */
		while (true) {
			Thread.sleep (Long.MAX_VALUE); //120S
		}
	}	
}
