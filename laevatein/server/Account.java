package laevatein.server;

import java.sql.ResultSet;

import laevatein.server.process_client.*;
import laevatein.server.database.*;
import laevatein.game.model.player.*;

public class Account
{
	SessionHandler session;
	String name;
	String password;
	PcInstance activePc;
	
	public Account (SessionHandler session, String account, String password) {
		this.session = session;
		this.name = account;
		this.password = password;
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
	
	public void setName (String account) {
		this.name = account;
	}
	
	public String getName () {
		return name;
	}
	
	public void setPassword (String pw) {
		this.password = pw;
	}
	
	public String getPassword () {
		return password;
	}
	
	public void setActivePc (PcInstance pc) {
		this.activePc = pc;
	}
	
	public PcInstance getActivePc () {
		return activePc;
	}
}
