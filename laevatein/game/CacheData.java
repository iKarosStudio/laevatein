package laevatein.game;

import java.util.concurrent.*;
import java.sql.*;
import org.w3c.dom.*;

import laevatein.server.database.*;
import laevatein.game.template.*;
import laevatein.game.model.npc.*;

public class CacheData
{
	private static CacheData instance;
	
	public static ConcurrentHashMap<Integer, NpcTemplate> npcs;
	public static ConcurrentHashMap<String, Document> npcAction;
	public static ConcurrentHashMap<Integer, NpcTalkData> npcTalkData;
	public static ConcurrentHashMap<Integer, ItemTemplate> item;
	public static ConcurrentHashMap<Integer, WeaponTemplate> weapon;
	public static ConcurrentHashMap<Integer, ArmorTemplate> armor;
	public static ConcurrentHashMap<Integer, SkillTemplate> skill;
	public static ConcurrentHashMap<Integer, NpcShop> npcShop;
	public static ConcurrentHashMap<Integer, PolyTemplate> poly;
	public static ConcurrentHashMap<String, Integer> polyIds;
	
	public static CacheData getInstance () {
		if (instance == null) {
			instance = new CacheData ();
		}
		return instance;
	}
	
	public CacheData () {
		System.out.print ("Load Game data...\n");
		
		System.out.print ("\t-> Load npc cache data...");
		loadNpcCache ();
		
		System.out.print ("\t-> Load npc action cache data...");
		loadNpcActionCache ();
		
		System.out.print ("\t-> Load npc talk cache data...");
		loadNpcTalkDataCache ();
		
		System.out.print ("\t-> Load items cache data...");
		loadItemCache ();
		
		System.out.print ("\t-> Load skill cache data...");
		loadSkillCache ();
		
		System.out.print ("\t-> Load shop cache data...");
		loadShopCache ();
		
		System.out.print ("\t-> Load weapons cache data...");
		loadWeaponCache ();
		
		System.out.print ("\t-> Load armor cache data...");
		loadArmorCache ();
		
		System.out.print ("\t-> Load poly cache data...");
		loadPolyCache ();
				
		//cache pets types
		
		System.out.println ();

	}
	
	public void loadNpcCache () {
		npcs = new ConcurrentHashMap<Integer, NpcTemplate> ();
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = HikariCP.getConnection().prepareStatement ("SELECT * FROM npc;");
			rs = ps.executeQuery ();
			
			int validCounter = 0;
			long timeStarts = System.currentTimeMillis ();
			while (rs.next ()) {
				NpcTemplate npcData = new NpcTemplate (
						rs.getInt ("npcid"), 
						rs.getString ("name"),
						rs.getString ("nameid"),
						rs.getString ("note"),
						rs.getString ("impl"),
						rs.getInt ("gfxid"), 
						rs.getInt ("lvl"),
						rs.getInt ("hp"),
						rs.getInt ("mp"), 
						rs.getInt ("ac"), 
						rs.getInt ("str"),
						rs.getInt ("con"),
						rs.getInt ("dex"),
						rs.getInt ("wis"),
						rs.getInt ("intel"),
						rs.getInt ("mr"),
						rs.getInt ("exp"),
						rs.getInt ("lawful"),
						rs.getString ("size"),
						rs.getInt ("weak_water"),
						rs.getInt ("weak_wind"), 
						rs.getInt ("weak_fire"),
						rs.getInt ("weak_earth"), 
						rs.getInt ("ranged"),
						rs.getBoolean ("tamable"),
						rs.getInt ("passispeed"),
						rs.getInt ("atkspeed"),
						rs.getInt ("atk_magic_speed"),
						rs.getInt ("sub_magic_speed"),
						rs.getInt ("undead"),
						rs.getInt ("poison_atk"),
						rs.getInt ("paralysis_atk"),
						rs.getInt ("agro"),
						rs.getInt ("agrososc"),
						rs.getInt ("agrocoi"),
						rs.getString ("family"),
						rs.getInt ("agrofamily"),
						rs.getInt ("picupitem"), 
						rs.getInt ("digestitem"),
						rs.getInt ("bravespeed"),
						rs.getInt ("hprinterval"), 
						rs.getInt ("hpr"),
						rs.getInt ("mprinterval"),
						rs.getInt ("mpr"),
						rs.getInt ("teleport"),
						rs.getInt ("randomlevel"),
						rs.getInt ("randomhp"),
						rs.getInt ("randommp"),
						rs.getInt ("randomac"),
						rs.getInt ("randomexp"),
						rs.getInt ("randomlawful"),
						rs.getInt ("damage_reduction"),
						rs.getInt ("hard"), 
						rs.getInt ("doppel"),
						rs.getInt ("IsTU"),
						rs.getInt ("IsErase"),
						rs.getInt ("bowActId"), 
						rs.getInt ("karma"),
						rs.getInt ("transform_id"),
						rs.getInt ("light_size"),
						rs.getInt ("amount_fixed"),
						rs.getInt ("atkexspeed"),
						rs.getInt ("attStatus"),
						rs.getInt ("bowUseId"),
						rs.getInt ("hascastle"),
						rs.getInt ("broad"));
				
				npcs.put (npcData.uuid, npcData);
				validCounter ++;
			}
			long timeEnds = System.currentTimeMillis ();
			float usedTime = (float) (timeEnds - timeStarts) / 1000;
			System.out.printf ("\t\t" + validCounter + " npc cached in\t%.3f s\n", usedTime);
			
		} catch (Exception e) {
			e.printStackTrace ();
		} finally {
			DatabaseUtil.close (rs);
			DatabaseUtil.close (ps);
			//DatabaseUtil.close (con) ;
		}	
	}
	
	public static void loadNpcActionCache () {
		npcAction = new ConcurrentHashMap<String, Document> ();
		long timeStart = System.currentTimeMillis ();
		
		NpcActionXmlLoader xmlActionFileLoader = new NpcActionXmlLoader ();
		xmlActionFileLoader.load (npcAction);
		
		long timeEnd = System.currentTimeMillis ();
		float usedTime = (float) (timeEnd - timeStart) / 1000;
		System.out.printf ("\tnpc xml cached in\t%.3f s\n", usedTime);
	}
	
	public void loadNpcTalkDataCache () {
		npcTalkData = new ConcurrentHashMap<Integer, NpcTalkData> ();
		
		Connection con = HikariCP.getConnection ();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = con.prepareStatement ("SELECT * FROM npcaction;") ;
			rs = ps.executeQuery ();
			
			int validCounter = 0;
			long timeStarts = System.currentTimeMillis ();
			while (rs.next ()) {
				NpcTalkData talkData = new NpcTalkData (
						rs.getInt ("npcid"),
						rs.getString ("normal_action"),
						rs.getString ("caotic_action"),
						rs.getString ("teleport_url"),
						rs.getString ("teleport_urla"));
				
				npcTalkData.put (talkData.npcId, talkData);
				
				validCounter ++;
			}
			long timeEnds = System.currentTimeMillis ();
			float usedTime = (float) (timeEnds - timeStarts) / 1000;
			System.out.printf ("\t\t" + validCounter + " npc talk cached in\t%.3f s\n", usedTime);
		} catch (Exception e) {
			e.printStackTrace ();
		} finally {
			DatabaseUtil.close (rs);
			DatabaseUtil.close (ps);
			DatabaseUtil.close (con);
		}
	}
	
	public void loadItemCache () {
		item = new ConcurrentHashMap<Integer, ItemTemplate> ();
		
		Connection con = HikariCP.getConnection ();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = con.prepareStatement ("SELECT * FROM etcitem;");
			rs = ps.executeQuery ();
			
			int counter = 0;
			long timeStarts = System.currentTimeMillis ();
			while (rs.next ()) {
				ItemTemplate i = new ItemTemplate (
					rs.getInt ("item_id"),
					rs.getString ("name"),
					rs.getString ("name_id"),
					rs.getString ("item_type"),
					rs.getString ("use_type"),
					rs.getString ("material"),
					rs.getInt ("weight"),
					rs.getInt ("invgfx"),
					rs.getInt ("grdgfx"),
					rs.getInt ("itemdesc_id"),
					rs.getBoolean ("stackable"),
					rs.getInt ("max_charge_count"),
					rs.getInt ("dmg_small"),
					rs.getInt ("dmg_large"),
					rs.getInt ("min_lvl"),
					rs.getInt ("max_lvl"),
					rs.getInt ("bless"),
					!rs.getBoolean ("trade"),
					rs.getInt ("delay_id"),
					rs.getInt ("delay_time"),
					rs.getInt ("food_volume"),
					rs.getBoolean ("save_at_once"));
				
				item.put (rs.getInt ("item_id"), i);
				
				counter ++;
			}
			long timeEnds = System.currentTimeMillis () ;
			float usedTime = (float) (timeEnds - timeStarts) / 1000;
			System.out.printf ("\t\t" + counter + " items cached in\t%.3f s\n", usedTime);
		} catch (Exception e) {
			e.printStackTrace ();
		} finally {
			DatabaseUtil.close (rs);
			DatabaseUtil.close (ps);
			DatabaseUtil.close (con);
		}
	}
	
	public static void loadSkillCache () {
		skill = new ConcurrentHashMap<Integer, SkillTemplate> ();
		
		Connection con = HikariCP.getConnection ();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = con.prepareStatement ("SELECT * FROM skills;");
			rs = ps.executeQuery (); 
					
			int counter = 0;
			long timeStarts = System.currentTimeMillis ();
			while (rs.next ()) {
				SkillTemplate template = new SkillTemplate (
					rs.getInt ("skill_id"),
					rs.getString ("name"),
					rs.getInt ("skill_level"),
					rs.getInt ("skill_number"),
					rs.getInt ("mpConsume"),
					rs.getInt ("hpConsume"),
					rs.getInt ("itemConsumeId"),
					rs.getInt ("itemConsumeCount"),
					rs.getInt ("reuseDelay"),
					rs.getInt ("buffDuration"),
					rs.getString ("target"),
					rs.getInt ("target_to"),
					rs.getInt ("damage_value"),
					rs.getInt ("damage_dice"),
					rs.getInt ("damage_dice_count"),
					rs.getInt ("probability_value"),
					rs.getInt ("probability_dice"),
					rs.getInt ("attr"),
					rs.getInt ("actid"),
					rs.getInt ("type"),
					rs.getInt ("lawful"),
					rs.getInt ("ranged"),
					rs.getInt ("area"),
					rs.getInt ("through"),
					rs.getInt ("id"),
					rs.getString ("nameid"),
					rs.getInt ("castgfx"),
					rs.getInt ("sysmsgID_happen"),
					rs.getInt ("sysmsgID_stop"),
					rs.getInt ("sysmsgID_fail"),
					rs.getInt ("arrowType")	) ;
				
				skill.put (template.skillId, template);
				if (template.type == 128) {
					//System.out.printf ("\nskill[%2d]%s:\t\t target:%02d type:%02d", template.skillId, template.name, template.targetTo, template.type);
				}
				counter++;
			}
			long timeEnds = System.currentTimeMillis ();
			float usedTime = (float) (timeEnds - timeStarts) / 1000;
			System.out.printf ("\t\t" + counter + " skills cached in\t%.3f s\n", usedTime);
		} catch (Exception e) {
			e.printStackTrace ();
		} finally {
			DatabaseUtil.close (rs);
			DatabaseUtil.close (ps);
			DatabaseUtil.close (con);
		}
	}
	
	public static void loadShopCache () {
		npcShop = new ConcurrentHashMap<Integer, NpcShop> ();
		
		Connection con = HikariCP.getConnection ();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = con.prepareStatement ("SELECT * FROM shop;");
			rs = ps.executeQuery ();
			
			int counter = 0;
			long timeStarts = System.currentTimeMillis ();
			while (rs.next ()) {
				int npcId = rs.getInt ("npc_id");
				int itemId = rs.getInt ("item_id");
				int orderId = rs.getInt ("order_id");
				int sellingPrice = rs.getInt ("selling_price");
				int packCount = rs.getInt ("pack_count");
				int purchasingPrice = rs.getInt ("purchasing_price");
				
				if (!npcShop.containsKey (npcId)) {
					NpcShop npc_shop = new NpcShop (npcId);
					npcShop.put (npcId, npc_shop);
				}
				
				NpcShop ns = npcShop.get (npcId) ;
				NpcShopMenu item = new NpcShopMenu (npcId, itemId, orderId, sellingPrice, packCount, purchasingPrice);
				
				ns.addMenuItem (item);
				//Item _i = CacheData.ItemCache.get (item.ItemId) ;
				//if (_i == null) {
				//	System.out.printf ("no cache data : %d\n", item.ItemId) ;
				//}
				//System.out.println (_i.Name) ;
				
				counter ++;
			}
			long timeEnds = System.currentTimeMillis () ;
			float usedTime = (float) (timeEnds - timeStarts) / 1000;
			System.out.printf ("\t\t" + counter + " shop cached in\t%.3f s\n", usedTime) ;
			
		} catch (Exception e) {
			e.printStackTrace ();
		} finally {
			DatabaseUtil.close (rs);
			DatabaseUtil.close (ps);
			DatabaseUtil.close (con);
		}
	}
	
	public void loadWeaponCache () {
		weapon = new ConcurrentHashMap<Integer, WeaponTemplate> ();
		
		Connection con = HikariCP.getConnection ();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = con.prepareStatement ("SELECT * FROM weapon;");
			rs = ps.executeQuery ();
			
			int counter = 0;
			long timeStarts = System.currentTimeMillis () ;
			while (rs.next ()) {
				int itemId = rs.getInt ("item_id");
				WeaponTemplate w = new WeaponTemplate (
					itemId,
					rs.getString ("name"),
					rs.getString ("name_id"),
					rs.getString ("type"),
					rs.getString ("material"),
					rs.getInt ("weight"),
					rs.getInt ("invgfx"),
					rs.getInt ("grdgfx"),
					rs.getInt ("itemdesc_id"),
					rs.getInt ("dmg_small"),
					rs.getInt ("dmg_large"),
					rs.getInt ("safenchant"),
					rs.getBoolean ("use_royal"),
					rs.getBoolean ("use_knight"),
					rs.getBoolean ("use_mage"),
					rs.getBoolean ("use_elf"),
					rs.getBoolean ("use_darkelf"),
					rs.getInt ("hitmodifier"),
					rs.getInt ("dmgmodifier"),
					rs.getInt ("add_str"),
					rs.getInt ("add_con"),
					rs.getInt ("add_dex"),
					rs.getInt ("add_int"),
					rs.getInt ("add_wis"),
					rs.getInt ("add_cha"),
					rs.getInt ("add_hp"),
					rs.getInt ("add_mp"),
					rs.getInt ("add_hpr"),
					rs.getInt ("add_mpr"),
					rs.getInt ("add_sp"),
					rs.getInt ("m_def"), //mr
					rs.getBoolean ("haste_item"),
					rs.getInt ("magicdmgmodifier"),
					rs.getBoolean ("canbedmg"),
					rs.getInt ("min_lvl"),
					rs.getInt ("max_lvl"),
					rs.getInt ("bless"),
					!rs.getBoolean ("trade"),
					rs.getBoolean ("mana_item") ) ;
				
				weapon.putIfAbsent (itemId, w) ;
				counter ++;
			}
			long timeEnds = System.currentTimeMillis ();
			float usedTime = (float) (timeEnds - timeStarts) / 1000;
			System.out.printf ("\t\t" + counter + " weapons cached in\t%.3f s\n", usedTime);
		} catch (Exception e) {
			e.printStackTrace ();
			
		} finally {
			DatabaseUtil.close (rs);
			DatabaseUtil.close (ps);
			DatabaseUtil.close (con);
			
		}
	}
	
	public void loadArmorCache () {
		armor = new ConcurrentHashMap<Integer, ArmorTemplate> ();
		
		Connection con = HikariCP.getConnection ();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = con.prepareStatement ("SELECT * FROM armor;");
			rs = ps.executeQuery ();
			
			int counter = 0;
			long timeStarts = System.currentTimeMillis ();
			while (rs.next ()) {
				int itemId = rs.getInt ("item_id");
				
				ArmorTemplate a = new ArmorTemplate (
					itemId,
					rs.getString ("name"),
					rs.getString ("name_id"),
					rs.getString ("type"),
					rs.getString ("material"),
					rs.getInt ("weight"),
					rs.getInt ("invgfx"),
					rs.getInt ("grdgfx"),
					rs.getInt ("itemdesc_id"),
					rs.getInt ("ac"),
					rs.getInt ("safenchant"),
					rs.getBoolean ("use_royal"),
					rs.getBoolean ("use_knight"),
					rs.getBoolean ("use_mage"),
					rs.getBoolean ("use_elf"),
					rs.getBoolean ("use_darkelf"),
					rs.getInt ("add_str"),
					rs.getInt ("add_con"),
					rs.getInt ("add_dex"),
					rs.getInt ("add_int"),
					rs.getInt ("add_wis"),
					rs.getInt ("add_cha"),
					rs.getInt ("add_hp"),
					rs.getInt ("add_mp"),
					rs.getInt ("add_hpr"),
					rs.getInt ("add_mpr"),
					rs.getInt ("add_sp"),
					rs.getInt ("min_lvl"),
					rs.getInt ("max_lvl"),
					rs.getInt ("m_def"),
					rs.getBoolean ("haste_item"),
					rs.getInt ("damage_reduction"),
					rs.getInt ("weight_reduction"),
					rs.getInt ("bow_hit_rate"),
					rs.getInt ("bless"),
					!rs.getBoolean ("trade"),
					rs.getInt ("defense_water"),
					rs.getInt ("defense_wind"),
					rs.getInt ("defense_fire"),
					rs.getInt ("defense_earth"),
					rs.getInt ("regist_stan"),
					rs.getInt ("regist_stone"),
					rs.getInt ("regist_sleep"),
					rs.getInt ("regist_freeze"));
				
				armor.putIfAbsent (itemId, a);
				counter ++;
			}
			long timeEnds = System.currentTimeMillis ();
			float usedTime = (float) (timeEnds - timeStarts) / 1000;
			System.out.printf ("\t\t" + counter + " armors cached in \t%.3f s\n", usedTime);
		} catch (Exception e) {
			e.printStackTrace ();
			
		} finally {
			DatabaseUtil.close (rs);
			DatabaseUtil.close (ps);
			DatabaseUtil.close (con);
		}
	}
	
	public void loadPolyCache () {
		poly = new ConcurrentHashMap<Integer, PolyTemplate> ();
		polyIds = new ConcurrentHashMap <String, Integer> ();
		
		Connection con = HikariCP.getConnection ();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = con.prepareStatement ("SELECT * FROM polymorphs;");
			rs = ps.executeQuery ();
			
			int counter = 0;
			long timeStarts = System.currentTimeMillis ();
			while (rs.next ()) {
				PolyTemplate p = new PolyTemplate (
						rs.getInt ("id"),
						rs.getString ("name"),
						rs.getInt ("polyid"),
						rs.getInt ("minlevel"),
						rs.getInt ("weaponequip"),
						rs.getInt ("armorequip"),
						rs.getBoolean ("isSkillUse"));
				
				poly.putIfAbsent (p.id, p);
				polyIds.put (p.name, p.id);
				
				counter ++;
			}
			long timeEnds = System.currentTimeMillis ();
			float usedTime = (float) (timeEnds - timeStarts) / 1000;
			System.out.printf ("\t\t" + counter + " poly cached in \t%.3f s\n", usedTime);
		} catch (Exception e) {
			e.printStackTrace ();
			
		} finally {
			DatabaseUtil.close (rs);
			DatabaseUtil.close (ps);
			DatabaseUtil.close (con);
		}
	}
	
}
