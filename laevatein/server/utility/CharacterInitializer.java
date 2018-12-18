package laevatein.server.utility;

import java.sql.*;

import laevatein.server.*;
import laevatein.server.database.*;
import laevatein.server.process_server.*;
import laevatein.game.model.player.*;

public class CharacterInitializer
{
	public static final int CLASSID_PRINCE = 0;
	public static final int CLASSID_PRINCESS = 1;
	public static final int CLASSID_KNIGHT_MALE = 61;
	public static final int CLASSID_KNIGHT_FEMALE = 48;
	public static final int CLASSID_ELF_MALE = 138;
	public static final int CLASSID_ELF_FEMALE = 37;
	public static final int CLASSID_WIZARD_MALE = 734;
	public static final int CLASSID_WIZARD_FEMALE = 1186;
	public static final int CLASSID_DARK_ELF_MALE = 2786;
	public static final int CLASSID_DARK_ELF_FEMALE = 2796;
	
	private static final short[] MALE_LIST = new short[] {0, 61, 138, 734, 2786};
	private static final short[] FEMALE_LIST = new short[] {1, 48, 37, 1186, 2796};
	
	PcInstance pc = null;
	
	public CharacterInitializer (SessionHandler handle,
		String name, int type, int sex,
		int str, int dex, int con, int wis, int cha, int intel
	) {
		pc = new PcInstance (handle);
		pc.setUuid (UuidGenerator.next ());
		pc.setName (name);
		pc.setTitle ("");
		pc.clanName = "";
		pc.setType (type);
		pc.setSex (sex);
		if (pc.isMale ()) {
			pc.originGfx = MALE_LIST[pc.getType ()];
			pc.gfx = pc.originGfx;
		} else { //Female
			pc.originGfx = FEMALE_LIST[pc.getType ()];
			pc.gfx = pc.originGfx;
		}
		//pc.gfxTemp = pc.gfx;
		pc.setLawful (0);
		pc.setLevel (1);
		pc.setExp (0);
		pc.basicParameters.setAc (10);
		pc.setSatiation (3);
		
		pc.basicParameters.setStr (str);
		pc.basicParameters.setCon (con);
		pc.basicParameters.setDex (dex);
		pc.basicParameters.setWis (wis);
		pc.basicParameters.setCha (cha);
		pc.basicParameters.setInt (intel);
		
		/* 出生地點 */
		pc.getLocation ().mapId = 0;
		pc.getLocation ().x = 32643;
		pc.getLocation ().y = 32960;
		
		if (pc.isRoyal ()) {
			pc.basicParameters.setHp (14);
			
			if (wis > 15) {
				pc.basicParameters.setMp (4);
			} else if (wis > 11) {
				pc.basicParameters.setMp (3);
			} else {
				pc.basicParameters.setMp (2);
			}
			
		} else if (pc.isKnight ()) {
			pc.basicParameters.setHp (16);
			
			if (wis > 11) {
				pc.basicParameters.setMp (2);
			} else {
				pc.basicParameters.setMp (1);
			}
			
		} else if (pc.isElf ()) {
			pc.basicParameters.setHp (15);
			
			if (wis > 15) {
				pc.basicParameters.setMp (6);
			} else {
				pc.basicParameters.setMp (4);
			}
			
		} else if (pc.isWizard ()) {
			pc.basicParameters.setHp (12);
			
			if (wis > 15) {
				pc.basicParameters.setMp (8);
			} else {
				pc.basicParameters.setMp (6);
			}
			
		} else if (pc.isDarkelf ()) {
			pc.basicParameters.setHp (12);
			
			if (wis > 15) {
				pc.basicParameters.setMp (6);
			} else if (wis > 11) {
				pc.basicParameters.setMp (4);
			} else {
				pc.basicParameters.setMp (3);
			}
			
		}
		pc.hp = pc.basicParameters.getHp () ;
		pc.mp = pc.basicParameters.getMp () ;
	}
	
	public void execute () {
		/* 寫入資料庫 */
		Connection conn = HikariCP.getConnection ();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement ("INSERT INTO characters SET account_name=?, objid=?, char_name=?, level=?, Exp=?, MaxHp=?, MaxMp=?, CurHp=?, CurMp=?, Ac=?, Str=?, Con=?, Dex=?, Cha=?, Intel=?, Wis=?, Status=?, Class=?, Sex=?, Type=?, Heading=?, LocX=?, LocY=?, MapID=?, Food=?, Lawful=?, Title=?, ClanID=?, Clanname=?;") ;
			ps.setString (1, pc.getHandle ().getUser ().getName ());
			ps.setInt (2, pc.getUuid ());
			ps.setString (3, pc.getName ());
			ps.setInt (4, pc.getLevel ());
			ps.setInt (5, pc.getExp ());
			ps.setInt (6, pc.basicParameters.getHp ());
			ps.setInt (7, pc.basicParameters.getMp ());
			ps.setInt (8, pc.hp);
			ps.setInt (9, pc.mp);
			ps.setInt (10, pc.basicParameters.getAc ());
			ps.setInt (11, pc.basicParameters.getStr ());
			ps.setInt (12, pc.basicParameters.getCon ());
			ps.setInt (13, pc.basicParameters.getDex ());
			ps.setInt (14, pc.basicParameters.getCha ());
			ps.setInt (15, pc.basicParameters.getInt ());
			ps.setInt (16, pc.basicParameters.getWis ());
			ps.setInt (17, pc.status);
			ps.setInt (18, pc.gfx);
			ps.setInt (19, pc.getSex ());
			ps.setInt (20, pc.getType ());
			ps.setInt (21, pc.heading);
			ps.setInt (22, pc.getLocation ().x);
			ps.setInt (23, pc.getLocation ().y);
			ps.setInt (24, pc.getLocation ().mapId);
			ps.setInt (25, pc.getSatiation ());
			ps.setInt (26, pc.getLawful ());
			ps.setString (27, pc.getTitle ());
			ps.setInt (28, pc.clanId);
			ps.setString (29, pc.clanName);
			
			ps.execute ();
			
			/*
			 * 給初始道具
			 */
			//
			
		} catch (Exception e) {
			e.printStackTrace ();
			
		} finally {
			DatabaseUtil.close (ps);
			DatabaseUtil.close (conn);
		}
		
		/*
		 * 更新客戶端角色顯示		
		 */
		pc.getHandle().sendPacket (new CharCreateResult (CharCreateResult.OK).getPacket ());
		pc.getHandle().sendPacket (new NewCharacterPack (pc).getPacket ());
	}
}
