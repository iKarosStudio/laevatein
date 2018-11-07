package laevatein.server.database;

import java.sql.Connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class HikariCP
{
	private static HikariCP instance;
	private static HikariDataSource datasource;
	private static Connection backupConnection;

	public static HikariCP getInstance () {
		if (instance == null) {
			instance = new HikariCP ();
		}
		return instance;
	}
	
	public HikariCP () {
		System.out.printf ("HikariCP Initializing...");

		try {			
			HikariConfig hikariCpConfig = new HikariConfig ("configs/hikari.properties");
			
			datasource = new HikariDataSource (hikariCpConfig);
			
			backupConnection = datasource.getConnection ();
			if (backupConnection.isValid (1000)) {
				System.out.println ("success");
			}			
		} catch (Exception e) {
			e.printStackTrace ();
			System.out.printf ("Database connect fail, Check hikariCP config\n");
			System.exit (666);
			
		}
	}
	
	public void Disconnect () {
		try {
			backupConnection.close ();
			datasource.close ();
			System.out.println ("HikariCP disconnected");
		} catch (Exception e) {
			e.printStackTrace ();
		}
	}
	
	public static Connection getConnection () {
		int retryCounter = 0;
		Connection con = null;
		
		do {
			try {
				con = datasource.getConnection ();
			} catch (Exception e) {
				System.out.printf ("[HikariCP] Connection used up\n");
				if (retryCounter < 3) {
					retryCounter++;
				} else {
					con = backupConnection;
					System.out.printf ("[HikariCP] Use backup connection\n");
				}
			}
		} while (con == null);
		
		return con;
	}
	
	public String getUrl () {
		return datasource.getJdbcUrl ();
	}
	
	public String getUser () {
		return datasource.getUsername ();
	}
	
	public String getPassword () {
		return datasource.getPassword ();
	}
}
