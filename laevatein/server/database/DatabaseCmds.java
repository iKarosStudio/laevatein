package laevatein.server.database;

import java.sql.*;
import java.util.concurrent.*;

import laevatein.config.*;
import laevatein.game.model.player.*;
import laevatein.game.model.item.*;

public class DatabaseCmds
{
	public static ResultSet loadAccount (String user_account) {
		Connection con = HikariCP.getConnection ();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = con.prepareStatement ("SELECT * FROM accounts WHERE login=? LIMIT 1;") ;
			ps.setString (1, user_account) ;
			rs = ps.executeQuery () ;
		} catch (Exception e) {
			e.printStackTrace () ; 
		} finally {
			//DatabaseUtil.close (rs) ;
			DatabaseUtil.close (ps) ;
			DatabaseUtil.close (con) ;
		}
		return rs;
	}
	
	public static void createAccount (String user_account, String user_pw, String ip, String hostname) {
		Connection con = HikariCP.getConnection ();
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement ("INSERT INTO accounts SET login=?, password=?, lastactive=?, access_level=?, ip=?, host=?, banned=?;") ;
			ps.setString (1, user_account) ;
			ps.setString (2, user_pw) ;
			ps.setTimestamp (3, new Timestamp (System.currentTimeMillis () ) ) ;
			ps.setInt (4, 0) ; //access level
			ps.setString (5, ip) ;
			ps.setString (6, hostname) ;
			ps.setInt (7, 0) ;
			ps.execute () ;
			
		} catch (Exception e) {
			e.printStackTrace () ;
		} finally {
			DatabaseUtil.close (ps) ;
			DatabaseUtil.close (con) ;
		}
	}
	
	public static ResultSet checkCharacterOnline (String user_account) {
		Connection con = HikariCP.getConnection ();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = con.prepareStatement ("SELECT * FROM characters WHERE account_name=? AND OnlineStatus=?;") ;
			ps.setString (1, user_account) ;
			ps.setInt (2, 1) ;
			
			rs = ps.executeQuery () ;
		} catch (Exception e) {
			e.printStackTrace () ;
		} finally {
			//DatabaseUtil.close (rs) ;
			DatabaseUtil.close (ps) ;
			DatabaseUtil.close (con) ;
		}
		return rs;
	}
	
	public static int getCharacterAmount (String user_account) {
		Connection con = HikariCP.getConnection ();
		PreparedStatement ps = null;	
		ResultSet rs = null;
		int count = 0;
				
		try {
			ps = con.prepareStatement ("SELECT count(*) AS cnt FROM characters WHERE account_name=?;") ;
			ps.setString (1, user_account) ;
			
			rs = ps.executeQuery () ;
			if (rs.next () ) {
				count = rs.getInt ("cnt") ;
			}
		} catch (Exception e) {
			e.printStackTrace () ;
		} finally {
			DatabaseUtil.close (rs) ;
			DatabaseUtil.close (ps) ;
			DatabaseUtil.close (con) ;
		}
		
		return count;
	}
	
	public static ResultSet getAccountCharacters (String user_account) {
		Connection con = HikariCP.getConnection () ;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = con.prepareStatement ("SELECT * FROM characters WHERE account_name=? ORDER BY objid;") ;
			ps.setString (1, user_account) ;
			rs = ps.executeQuery () ;
		} catch (Exception e) {
			e.printStackTrace () ;
		} finally {
			//DatabaseUtil.close (rs) ;
			DatabaseUtil.close (ps) ;
			DatabaseUtil.close (con) ;
		}
		
		return rs;
	}
	
	public static void updateAccountLoginTime (String user_account, String ip, String hostname) {
		Connection con = HikariCP.getConnection ();
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement ("UPDATE accounts SET ip=?, host=?, lastactive=? WHERE login=?;") ;
			ps.setString (1, ip);
			ps.setString (2, hostname);
			ps.setTimestamp (3, new Timestamp (System.currentTimeMillis () ) ) ;
			ps.setString (4, user_account);
			
			ps.execute () ;
			
		} catch (Exception e) {
			e.printStackTrace () ;
		} finally {
			DatabaseUtil.close (ps) ;
			DatabaseUtil.close (con) ;
		}
	}
	
	public static ResultSet loadPcItems (int uuid) {
		Connection con = HikariCP.getConnection () ;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = con.prepareStatement ("SELECT * FROM character_items WHERE char_id=?;") ;
			ps.setInt (1, uuid) ;
			rs = ps.executeQuery () ;
			
		} catch (Exception e) {
			e.printStackTrace () ;
		} finally {
			//DatabaseUtil.close (rs) ;
			DatabaseUtil.close (ps) ;
			DatabaseUtil.close (con) ;
		}
		
		return rs;
	}
	
	public static void savePcItems (int pcUuid, ConcurrentHashMap<Integer, ItemInstance> itemBag) {
		Connection con = HikariCP.getConnection ();
		PreparedStatement ps = null;
		
		try {
			//remove all
			ps = con.prepareStatement ("DELETE FROM character_items WHERE char_id=?;");
			ps.setInt (1, pcUuid);
			ps.execute ();
			DatabaseUtil.close (ps);
		} catch (Exception e) {
			e.printStackTrace ();
		} finally {
			DatabaseUtil.close (ps);
		}
		
		//insert
		itemBag.forEachValue (Configurations.PARALLELISM_THRESHOLD, (ItemInstance i)->{
			PreparedStatement psi = null;
			try {
				psi = con.prepareStatement ("INSERT INTO character_items SET id=?, item_id=?, char_id=?, item_name=?, count=?, is_equipped=?, enchantlvl=?, is_id=?, durability=?, charge_count=?;");
				psi.setInt (1, i.uuid);
				psi.setInt (2, i.id);
				psi.setInt (3, i.ownerUuid);
				psi.setString (4, i.name);
				psi.setInt (5, i.count);
				psi.setInt (6, i.isUsing ? 1:0);
				psi.setInt (7, i.enchant);
				psi.setInt (8, i.isIdentified ? 1:0);
				psi.setInt (9, i.durability);
				psi.setInt (10,i.chargeCount);
				psi.execute ();
				
			} catch (Exception e) {
				e.printStackTrace ();
			} finally {
				DatabaseUtil.close (psi);
			}
		});
		
		DatabaseUtil.close (con);
	}
	
	public static ResultSet loadPcSkills (int uuid) {
		Connection con = HikariCP.getConnection () ;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = con.prepareStatement ("SELECT * FROM character_skills WHERE char_obj_id=?;") ;
			ps.setInt (1, uuid) ;
			rs = ps.executeQuery () ;
		} catch (Exception e) {
			e.printStackTrace () ;
		} finally {
			//DatabaseUtil.close (rs) ;
			DatabaseUtil.close (ps) ;
			DatabaseUtil.close (con) ;
		}
		
		return rs;		
	}
	
	public static void saveSkills (int uuid, int skill_id, String skill_name) {
		Connection con = HikariCP.getConnection () ;
		PreparedStatement ps = null;
		
		try {
			ps = con.prepareStatement ("INSERT INTO character_skills SET char_obj_id=?, skill_id=?, skill_name=?") ;
			ps.setInt (1, uuid) ;
			ps.setInt (2, skill_id) ;
			ps.setString (3, skill_name) ;
			ps.execute () ;
			
		} catch (Exception e) {
			e.printStackTrace () ;
		} finally {
			DatabaseUtil.close (ps) ;
			DatabaseUtil.close (con) ;
		}
	}
	
	public static boolean checkSkill (int uuid, int skill_id) {
		Connection con = HikariCP.getConnection () ;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean result = false;
		
		try {
			ps = con.prepareStatement ("SELECT * FROM character_skills WHERE char_obj_id=? AND skill_id=?;") ;
			ps.setInt (1, uuid) ;
			ps.setInt (2, skill_id) ;
			
			rs = ps.executeQuery () ;
			
			if (rs.next ()) {
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace () ;
		} finally {
			DatabaseUtil.close (rs) ;
			DatabaseUtil.close (ps) ;
			DatabaseUtil.close (con) ;
		}
		
		return result;
	}
	
	public static void insertSkillEffect (int uuid, int skillId, int remainTime, int polyId) {
		Connection con = HikariCP.getConnection ();
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement ("INSERT INTO character_buff SET char_obj_id=?, skill_id=?, remaining_time=?, poly_id=?;");
			ps.setInt (1, uuid);
			ps.setInt (2, skillId);
			ps.setInt (3, remainTime);
			ps.setInt (4, polyId);
			ps.execute ();
			
		} catch (Exception e) {
			e.printStackTrace ();
		} finally {
			DatabaseUtil.close (con);
			DatabaseUtil.close (ps);
		}
	}
	
	public static void updateSkillEffect (int uuid, int skillId, int remainTime, int polyId) {
		Connection con = HikariCP.getConnection ();
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement ("UPDATE character_buff SET remaining_time=?, poly_id=? WHERE char_obj_id=? AND skill_id=?;");
			ps.setInt (1, remainTime);
			ps.setInt (2, polyId);
			ps.setInt (3, uuid);
			ps.setInt (4, skillId);
			ps.execute ();
			
		} catch (Exception e) {
			e.printStackTrace ();
		} finally {
			DatabaseUtil.close (con);
			DatabaseUtil.close (ps);
		}
	}
	
	public static void deleteSkillEffects (int uuid) {
		Connection con = HikariCP.getConnection ();
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement ("DELETE FROM character_buff WHERE char_obj_id=?;");
			ps.setInt (1, uuid);
			ps.execute ();
			
		} catch (Exception e) {
			e.printStackTrace ();
		} finally {
			DatabaseUtil.close (con);
			DatabaseUtil.close (ps);
		}
	}
	
	public static ResultSet loadSkillEffects (int uuid) {
		Connection con = HikariCP.getConnection ();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = con.prepareStatement ("SELECT * FROM character_buff WHERE char_obj_id=?;");
			ps.setInt (1, uuid);
			rs = ps.executeQuery ();
			
		} catch (Exception e) {
			e.printStackTrace ();
			
		} finally {
			DatabaseUtil.close (ps);
			DatabaseUtil.close (con);
			
		}
		
		return rs;
	}
	
	public static ResultSet loadTeleportBookmark (int uuid) {
		Connection con = HikariCP.getConnection ();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = con.prepareStatement ("SELECT * FROM charater_teleport WHERE char_id=? ORDER BY name ASC;");
			ps.setInt (1, uuid);
			rs = ps.executeQuery ();
			
		} catch (Exception e) {
			e.printStackTrace ();
		} finally {
			//DatabaseUtil.close (rs);
			DatabaseUtil.close (ps);
			DatabaseUtil.close (con);
		}
		
		return rs;
	}
	
	public static void setItemEquip (int uuid, int isEquip) {
		//
	}
	
	/*
	public static void insertItem (ItemInstance item) {
		Connection con = HikariCP.getConnection ();
		PreparedStatement ps = null;
		
		try {
			ps = con.prepareStatement ("INSERT INTO character_items SET id=?, item_id=?, char_id=?, item_name=?, count=?, is_equipped=?, enchantlvl=?, is_id=?, durability=? ,charge_count=?;");
			ps.setInt (1, item.uuid);
			ps.setInt (2, item.id);
			ps.setInt (3, item.uuidOwner);
			ps.setString (4, item.name);
			ps.setInt (5, item.count);
			ps.setInt (6, (item.isUsing) ? 1:0);
			ps.setInt (7, item.enchant);
			ps.setInt (8, (item.isIdentified) ? 1:0);
			ps.setInt (9, item.durability);
			ps.setInt (10,item.chargeCount);
			ps.execute ();
			
		} catch (Exception e) {
			e.printStackTrace ();
		} finally {
			DatabaseUtil.close (ps);
			DatabaseUtil.close (con);
		}
	}
	*/
	
	public static void updateItem (ItemInstance item) {
		Connection con = HikariCP.getConnection ();
		PreparedStatement ps = null;
		
		try {
			ps = con.prepareStatement ("UPDATE character_items SET item_id=?, char_id=?, item_name=?, count=?, is_equipped=?, enchantlvl=?, is_id=?, durability=? ,charge_count=? where id=?;");
			ps.setInt (1, item.id);
			ps.setInt (2, item.ownerUuid);
			ps.setString (3, item.name);
			ps.setInt (4, item.count);
			ps.setInt (5, (item.isUsing) ? 1:0);
			ps.setInt (6, item.enchant);
			ps.setInt (7, (item.isIdentified) ? 1:0);
			ps.setInt (8, item.durability);
			ps.setInt (9, item.chargeCount);
			ps.setInt (10,item.uuid);
			
			ps.execute ();
		} catch (Exception e) {
			e.printStackTrace () ;
		} finally {
			DatabaseUtil.close (ps);
			DatabaseUtil.close (con);
		}
	}
	
	
	public static void deleteItem (ItemInstance item) {
		Connection con = HikariCP.getConnection ();
		PreparedStatement ps = null;
		
		try {
			ps = con.prepareStatement ("DELETE FROM character_items WHERE id=?;");
			ps.setInt (1, item.uuid);
			ps.execute ();
		} catch (Exception e) {
			e.printStackTrace ();
		} finally {
			DatabaseUtil.close (ps);
			DatabaseUtil.close (con);
		}
	}
	
	public static void savePc (PcInstance p) {
		Connection con = HikariCP.getConnection ();
		PreparedStatement ps = null;
		
		try {
			ps = con.prepareStatement ("UPDATE characters SET level=?, Exp=?, MaxHp=?, CurHp=?, MaxMp=?, CurMp=?, Ac=?, Status=?, LocX=?, LocY=?, Heading=?, MapID=? WHERE objid=?;");
			ps.setInt (1, p.level);
			ps.setInt (2, p.exp);
			ps.setInt (3, p.basicParameters.maxHp);
			ps.setInt (4, p.hp);
			ps.setInt (5, p.basicParameters.maxMp);
			ps.setInt (6, p.mp);
			ps.setInt (7, p.basicParameters.ac + p.equipmentParameters.ac);
			ps.setInt (8, p.status);
			ps.setInt (9, p.loc.p.x);
			ps.setInt (10, p.loc.p.y);
			ps.setInt (11, p.heading);
			ps.setInt (12, p.loc.mapId);
			//補上 clanid, clanname
			ps.setInt (13, p.uuid);
			ps.execute ();
			
		} catch (Exception e) {
			e.printStackTrace ();
			
		} finally {
			DatabaseUtil.close (ps);
			DatabaseUtil.close (con);
		}
	}
	
	public static ResultSet doorSpawnList (int mapid) {
		Connection con = HikariCP.getConnection () ;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = con.prepareStatement ("SELECT * FROM spawnlist_door WHERE mapid=?;") ;
			ps.setInt (1, mapid) ;
			
			rs = ps.executeQuery () ;
		} catch (Exception e) {
			e.printStackTrace () ;
		} finally {
			//DatabaseUtil.close (rs) ;
			DatabaseUtil.close (ps) ;
			DatabaseUtil.close (con) ;
		}
		
		return rs;
	}
	
	public static ResultSet mobSpawnList (int mapid) {
		Connection con = HikariCP.getConnection () ;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = con.prepareStatement ("SELECT * FROM spawnlist WHERE mapid=?;") ;
			ps.setInt (1, mapid) ;
			rs = ps.executeQuery () ;
		} catch (Exception e) {
			e.printStackTrace () ;
		} finally {
			//DatabaseUtil.close (rs) ;
			DatabaseUtil.close (ps) ;
			DatabaseUtil.close (con) ;
		}
		
		return rs;
	}
	
	public static ResultSet mobDropList (int mob_id) {
		Connection con = HikariCP.getConnection () ;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = con.prepareStatement ("SELECT * FROM droplist WHERE mobId=?;") ;
			ps.setInt (1, mob_id) ;
			rs = ps.executeQuery () ;
		} catch (Exception e) {
			e.printStackTrace () ;
		} finally {
			//DatabaseUtil.close (rs) ;
			DatabaseUtil.close (ps) ;
			DatabaseUtil.close (con) ;
		}
		
		return rs;
	}
}
