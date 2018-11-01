package laevatein.server;

import java.lang.Thread;
import java.util.Calendar;

public class ServerTime extends Thread implements Runnable
{
	private static ServerTime instance; 
	private static Calendar cal = null;
	
	private long time = 0;
	private int date = 0; //sun~mon
	private int hour = 0;
	private int minute = 0;
	private int second = 0;
	
	public void run () {
		//System.out.println ("\t-> System time : " + cal.getTime ().toString () ) ;
		try {
			//while (true) {
				//cal = Calendar.getInstance () ;
				time = cal.getTimeInMillis () ;
				date = cal.get (Calendar.DATE) ;
				
				int t = (cal.get (Calendar.HOUR) * 3600) + (cal.get (Calendar.MINUTE) * 60) + cal.get (Calendar.SECOND) ;
				
				t *= 6; //遊戲與現實時間比例
				hour = t / 3600 % 24;
				minute  = t / 60 % 60;
				second  = t % 60;
				//sleep (1000) ; //1s
			//}
		} catch (Exception e) {
			e.printStackTrace ();
		}
	}
	
	public ServerTime () {
		cal = Calendar.getInstance ();
		//System.out.println ("Server Time start") ;
		this.setName ("SERVER TIME") ;
	}
	
	public static ServerTime getInstance () {
		if (instance == null) {
			instance = new ServerTime ();
		}
		return instance;
	}
	
	public static String getTimeString () {
		return Calendar.getInstance ().getTime ().toString ();
	}
	
	public int getTime () {
		/* 60 -> 00:01 */
		int t = (int) (time / 1000) % 86400;
		
		return t + 28800; //GMT+8 ->3600 * 8 = 28800
	}
	
	public int getDate () {
		return date;
	}
	
	public int getHour () {
		return hour;
	}
	
	public int getMin () {
		return minute;
	}
	
	public int getSec () {
		return second;
	}
}
