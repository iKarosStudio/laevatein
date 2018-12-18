package laevatein.server.process_client;

import java.sql.*;

import laevatein.server.*;
import laevatein.server.packet.*;
import laevatein.server.process_server.*;
import laevatein.server.database.*;
import laevatein.server.opcodes.*;
import laevatein.server.utility.*;
//import laevatein.callback.*;
import laevatein.game.*;
import laevatein.game.model.player.*;
import laevatein.game.routine_task.*;

public class CharacterOperation {
	
	public void login (SessionHandler handle, byte[] packet) {
		PacketReader packetReader = new PacketReader (packet);
		String charName = packetReader.readString ();
		
		//載入角色
		PcInstance pc = new PcInstance (handle);
		if (pc.load (charName)) {
			//開始回報角色登入資料
			handle.getUser().setActivePc (pc);
			
			new Unknown1 (handle);
			new Unknown2 (handle);
			
			//載入角色記憶座標
			
			pc.equipment = new Equipment (handle);
			pc.loadItemBag ();
			pc.loadSkills ();
					
			/* FIXME
			byte[] config = new SendClientConfig (Handle).getRaw () ;
			if (config.length > 0) {
				Handle.SendPacket (config) ;
			}
			*/
			
			handle.sendPacket (new GameTime().getPacket ());
			handle.sendPacket (new ModelStatus (pc).getPacketNoPadding ());
			handle.sendPacket (new MapId (pc.getLocation ().mapId).getPacket ());
			handle.sendPacket (pc.getPacket ());

			pc.loadBuffs ();
			pc.getCallback ().updateSpMr ();
			
			handle.sendPacket (new ReportTitle (handle).getPacket ());
			
			handle.sendPacket (new ReportWeather (Laevatein.getInstance ().getWeather (pc.getLocation ().mapId)).getPacket ());
			
			//Set Emblem here

			
			handle.sendPacket (new ModelStatus (pc).getPacketNoPadding ());
			
			//固定循環工作
			pc.hsTask = new HsTask (pc);
			pc.hsTask.start ();
			pc.lsTask = new LsTask (pc);
			pc.lsTask.start ();			
			
			pc.getCallback ().setEnable (true);
			
			//視距物件更新服務
			pc.sight.start ();
			
			//pc.updateCurrentMap ();
			Laevatein.getInstance ().addPlayer (pc);
			
			
		} else {
			//沒有角色ID, 非正常登入現象
			System.out.printf ("error charName login intention:%s form ip:%s\n", charName, handle.getIP ());
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
		 * 3: Wizard
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
			ps1.setString (1, handle.getUser().getName ());
			rsAmount = ps1.executeQuery ();
			if (rsAmount.next ()) {
				if (rsAmount.getInt ("cnt") > 4) {
					handle.sendPacket (new CharCreateResult (CharCreateResult.WRONG_AMOUNT).getPacket ());
					return;
				}
			}
			
			//檢查重複ID
			ps2 = conn.prepareStatement ("SELECT account_name FROM characters WHERE char_name=?;");
			ps2.setString (1, charName);
			rsIdRepeat = ps2.executeQuery ();
			if (rsIdRepeat.next ()) {
				handle.sendPacket (new CharCreateResult (CharCreateResult.ALREADY_EXIST).getPacket ());
				return;
			}
			
			//檢查數值總和
			if ((str + dex + con + wis + cha + intel) != 75) {
				handle.sendPacket (new CharCreateResult (CharCreateResult.WRONG_AMOUNT).getPacket ());
				return;
			}
			
			//Done, Write to Database
			CharacterInitializer pcCreate = new CharacterInitializer (handle, charName, type, sex, str, dex, con, wis, cha, intel);
			pcCreate.execute ();
			System.out.printf ("create character:%s\t From user:%s @ %s\n",
					charName,
					handle.getUser().getName (),
					handle.getIP ());
			
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
	
	public void delete (SessionHandler handle, byte[] packet) {
		PacketReader packetReader = new PacketReader (packet);
		
		String charName = packetReader.readString ();
		
		System.out.printf ("delete %s...", charName);
		PcInstance pc = new PcInstance (handle);
		if (pc.load (charName)) {			
			try {
				int uuid = pc.getUuid ();
				DatabaseCmds.deleteSkillEffects (uuid);
				DatabaseCmds.deletePcItem (uuid);
				DatabaseCmds.deletePcSkill (uuid);
				DatabaseCmds.deleteCharacter (uuid);
				
				PacketBuilder resultPacket = new PacketBuilder ();
				resultPacket.writeByte (ServerOpcodes.CHAR_DELETE);
				resultPacket.writeByte (0x05);
				
				handle.sendPacket (resultPacket.getPacket ());
				
				System.out.printf ("ok\n");
				
			} catch (Exception e) {
				e.printStackTrace ();
			}
			
		} else {
			System.out.printf ("not found\n");
		}
	}
}


