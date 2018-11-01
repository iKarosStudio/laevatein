package laevatein;

import laevatein.server.database.*;

public class Shutdown extends Thread implements Runnable
{
	public void run () {
		System.out.println ("\ninitiating shutdown process...") ;
		
		//stop login control
		
		//close socket
		
		//kick all players
		
		//store data
		
		//kill server instance
		
		//rebuild server instance
		
		HikariCP.getInstance ().Disconnect ();
	}
	
	public Shutdown () {
		System.out.println ("hook shutdown process");
	}
}
