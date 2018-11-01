package laevatein.server.process_client;

import java.sql.*;

import laevatein.server.*;
import laevatein.server.packet.*;
import laevatein.server.process_server.*;
import laevatein.server.database.*;
import laevatein.server.utility.*;
import laevatein.game.*;
import laevatein.game.model.player.*;

public class CharacterOperation {
	
	public void login (SessionHandler handle, byte[] packet) {
		PacketReader packetReader = new PacketReader (packet);
		String charName = packetReader.readString ();
		
		/* 載入角色 */
		PcInstance pc = new PcInstance (handle);
		if (pc.load (charName)) {
			/* 開始回報角色登入資料 */
			handle.user.activePc = pc;
			
			new Unknown1 (handle);
			new Unknown2 (handle);
			
			//載入角色記憶座標
			
			pc.loadItemBag ();
			pc.loadSkills ();
			pc.loadBuffs ();
					
			/* fix
			byte[] config = new SendClientConfig (Handle).getRaw () ;
			if (config.length > 0) {
				Handle.SendPacket (config) ;
			}
			*/
			
			handle.sendPacket (new GameTime().getRaw ());

			handle.sendPacket (new MapId (pc.loc.mapId).getRaw ());
			handle.sendPacket (new ModelStatus (pc).getRaw ());
			handle.sendPacket (pc.getPacket ());
			handle.sendPacket (new ReportSpMr (handle).getRaw ());
			handle.sendPacket (new ReportTitle (handle).getRaw ());
			
			handle.sendPacket (new ReportWeather (Laevatein.getInstance ().weather).getRaw ());
			
			//Set Emblem here

			handle.sendPacket (new ModelStatus (pc).getRaw ());
			
			/* 固定循環工作 */
			pc.hsTask.start ();
			pc.lsTask.start ();
			//pc.routineTasks.start ();
			//pc.skillBuffs.start ();
			//pc.hpMonitor.start ();
			
			//開始經驗值監測
			//pc.expMonitor.start ();
			
			
			//視距物件更新服務
			pc.sight.start ();
			
			//pc.updateCurrentMap ();
			Laevatein.getInstance ().addPlayer (pc);
			
			
		} else {
			//沒有角色ID, 非正常登入現象
			System.out.printf ("不正常角色登入 %s form ip:%s\n", charName, handle.getIP ());
			handle.disconnect ();
		}
	}
	
	/* 參考C_CreateChar.java */
	public void create (SessionHandler handle, byte[] data) {
		PacketReader packetReader = new PacketReader (data);
		
		String charName = packetReader.readString ();
		
		/* 
		 * 0: Royal
		 * 1: Knight
		 * 2: Elf
		 * 3: Mage
		 * 4: Darkelf
		 */
		int type = packetReader.readByte ();
		
		/* 
		 * 0:Male
		 * 1:Female
		 */
		int sex =  packetReader.readByte (); 
		
		int str =  packetReader.readByte ();
		int dex =  packetReader.readByte ();
		int con =  packetReader.readByte ();
		int wis =  packetReader.readByte ();
		int cha =  packetReader.readByte ();
		int intel= packetReader.readByte ();
		
		Connection conn = HikariCP.getConnection ();
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		ResultSet rsAmount    = null;
		ResultSet rsIdRepeat  = null;
		
		try {
			//檢查帳號角色數量
			ps1 = conn.prepareStatement ("SELECT count(*) as cnt FROM characters WHERE account_name=?;");
			ps1.setString (1, handle.user.name);
			rsAmount = ps1.executeQuery ();
			if (rsAmount.next ()) {
				if (rsAmount.getInt ("cnt") > 4) {
					handle.sendPacket (new CharCreateResult (CharCreateResult.WRONG_AMOUNT).getRaw ());
					return;
				}
			}
			
			//檢查重複ID
			ps2 = conn.prepareStatement ("SELECT account_name FROM characters WHERE char_name=?;");
			ps2.setString (1, charName);
			rsIdRepeat = ps2.executeQuery ();
			if (rsIdRepeat.next ()) {
				handle.sendPacket (new CharCreateResult (CharCreateResult.ALREADY_EXIST).getRaw ());
				return;
			}
			
			//檢查數值總和
			if ((str + dex + con + wis + cha + intel) != 75) {
				handle.sendPacket (new CharCreateResult (CharCreateResult.WRONG_AMOUNT).getRaw ());
				return;
			}
			
			//Done, Write to Database
			CharacterInitializer pcCreate = new CharacterInitializer (handle, charName, type, sex, str, dex, con, wis, cha, intel);
			pcCreate.execute ();
			System.out.printf ("Create Character:%s\t From Account:%s @ %s\n", charName, handle.user.name, handle.getIP ());
			
		} catch (Exception e) {
			e.printStackTrace ();
			
		} finally {
			DatabaseUtil.close (rsAmount);
			DatabaseUtil.close (rsIdRepeat);
			DatabaseUtil.close (ps1);
			DatabaseUtil.close (ps2);
			DatabaseUtil.close (conn);
		}
		
	}
	
	public void delete (SessionHandler handle, byte[] data) {
		//刪除角色
	}
}


