package laevatein.server;

import java.sql.ResultSet;

import laevatein.server.process_client.*;
import laevatein.server.database.*;
import laevatein.game.model.player.*;

public class Account
{
	private SessionHandler session;
	
	public String name;
	public String password;
	
	public PcInstance activePc = null;
	
	public Account () {
		//super ();
	}
	
	public Account (SessionHandler _session, String _account, String _password) {
		session = _session;
		name = _account;
		password = _password;
	}
	
	/* 載入帳號 */
	public int load () {
		ResultSet rs = null;
		ResultSet online = null;
	
		int loginResult = 0;
		try {
			rs = DatabaseCmds.loadAccount (name);
			
			if (!rs.next ()) {
				System.out.println ("Account:" + name + " NOT EXISTS.");
				loginResult = AccountOperation.ACCOUNT_ALREADY_EXISTS;
				
			} else {
				String pw = rs.getString ("password");
				if (password.equals (pw)) {
					loginResult = AccountOperation.LOGIN_OK;
				} else {
					loginResult = AccountOperation.ACCOUNT_PASSWORD_ERROR;
				}
				
				//檢查已有登入角色
				online = DatabaseCmds.checkCharacterOnline (name);
				if (online.next ()) {
					loginResult = AccountOperation.ACCOUNT_IN_USE;
				}
			}
		} catch (Exception e) {
			e.printStackTrace ();
			
		} finally {
			DatabaseUtil.close (rs);
			DatabaseUtil.close (online);
			
		}
		return loginResult;
	}
	
	public void updateLastLogin () {
		DatabaseCmds.updateAccountLoginTime (name, session.getIP (), session.getHostName ());
	}
}
