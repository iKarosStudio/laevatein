package laevatein.server;

import java.util.Calendar;

public class ServerTime implements Runnable
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
			cal = Calendar.getInstance ();
			time = cal.getTimeInMillis ();
			//time=System.currentTimeMillis ();
			date = cal.get (Calendar.DATE);
			
			int t = (cal.get (Calendar.HOUR) * 3600) + (cal.get (Calendar.MINUTE) * 60) + cal.get (Calendar.SECOND) ;
			
			t *= 6; //遊戲與現實時間比例
			hour = t / 3600 % 24;
			minute  = t / 60 % 60;
			second  = t % 60;
			
			/*
			System.out.printf ("time in game:%02d:%02d:%02d\n", hour, minute, second);
			System.out.printf ("time in real:%02d:%02d:%02d\n", 
					cal.get (Calendar.HOUR),
					cal.get (Calendar.MINUTE),
					cal.get (Calendar.SECOND));*/
			
		} catch (Exception e) {
			e.printStackTrace ();
		}
	}
	
	public ServerTime () {
		cal = Calendar.getInstance ();
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
