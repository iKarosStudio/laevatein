package laevatein.server;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.lang.management.OperatingSystemMXBean;

import laevatein.config.*;
import laevatein.gui.*;
import laevatein.game.*;

public class SystemMonitor extends Thread implements Runnable
{
	private static SystemMonitor instance; 
	
	ThreadMXBean mana = ManagementFactory.getThreadMXBean();
	OperatingSystemMXBean osmb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean ();
	
	public int threadCount;
	public float cpuUsage;
	public float memUsage;
	public String osName;
	public String cpuName = "unknown";
	public int cpuCount;
	public String pid;
	
	public void run () {
		Runtime processRuntime = Runtime.getRuntime ();
		memUsage = (processRuntime.totalMemory() - processRuntime.freeMemory()) / (1024 * 1024);
		cpuUsage = (float) osmb.getSystemLoadAverage () * 100;
		threadCount = mana.getThreadCount ();
		//System.out.printf ("cpu:%2.2f mem:%2.2f MB thread:%d\n", cpuUsage, memUsage, threadCount);
		
		if (Configurations.USE_GUI) {
			//SystemTab.getInstance ().update ();
			//ManagementTab.getInstance ().update ();
		}
		
		if (Laevatein.getInstance ().getOnlinePlayers () < Configurations.MAX_PLAYER) {
			if (!MainService.getInstance ().getLogin ()) {
				MainService.getInstance ().loginEnable ();
			}
		} else {
			if (MainService.getInstance ().getLogin ()) {
				MainService.getInstance ().loginDisable ();
			}
		}
	}
	
	//.maxMemory ()-> -Xmx
	//.totalMemory()-> -Xms
	public SystemMonitor () {		
		osName = osmb.getName () + "-" + osmb.getArch ();
		cpuCount = osmb.getAvailableProcessors ();
		//cpuCount = Runtime.getRuntime ().availableProcessors ();
		pid = ManagementFactory.getRuntimeMXBean().getName().split ("@")[0];
		
		System.out.println ("Xmx:" + Runtime.getRuntime ().maxMemory () / 1048576 + " MB");
		System.out.println ("Xms:" + Runtime.getRuntime ().totalMemory () / 1048576 + " MB");
		//System.out.println (osmb.getName ()); //os name
		//System.out.println (osmb.getArch ());
		
		if (osName.contains ("Windows")) {
			//
		}
		
		if (osName.contains ("Linux")) {
			//
		}
		
		//Set thread name
		this.setName ("SYSTEM MONITOR") ;
	}
	
	public static SystemMonitor getInstance () {
		if (instance == null) {
			instance = new SystemMonitor ();
		}
		
		return instance;
	}
	
	public int getMaxMemory () {
		return (int) (Runtime.getRuntime ().maxMemory () >> 24);
	}
}
