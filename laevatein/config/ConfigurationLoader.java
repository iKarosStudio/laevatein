package laevatein.config;

import java.util.Properties;
import java.io.*;

public class ConfigurationLoader
{
private static ConfigurationLoader instance;
	
	public static ConfigurationLoader getInstance () 
	{
		if (instance == null) {
			instance = new ConfigurationLoader ();
		}
		return instance;
	}
	
	public ConfigurationLoader () 
	{
		System.out.printf ("Load server config...");
		Properties p = new Properties ();
		
		try {
			p.load (new FileInputStream ("configs/laevatein.properties"));
			Configurations.RateExp = Integer.valueOf (p.getProperty ("RATE_EXP"));
			Configurations.RatePetExp = Integer.valueOf (p.getProperty ("RATE_PET_EXP"));
			Configurations.RateDropItem = Integer.valueOf (p.getProperty ("RATE_DROP_ITEM"));
			Configurations.RateDropGold = Integer.valueOf (p.getProperty ("RATE_DROP_GOLD"));
			Configurations.RateLawful = Integer.valueOf (p.getProperty ("RATE_LAWFUL"));
			Configurations.RateMaxWeight = Integer.valueOf (p.getProperty ("RATE_MAX_WEIGHT"));
			Configurations.RateMaxPetWeight = Integer.valueOf (p.getProperty ("RATE_PET_MAX_WEIGHT"));
			
			Configurations.MAX_HP_ROYAL = Integer.valueOf (p.getProperty ("MAXHP_ROYAL"));
			Configurations.MAX_MP_ROYAL = Integer.valueOf (p.getProperty ("MAXMP_ROYAL"));
			Configurations.MAX_HP_KNIGHT =Integer.valueOf (p.getProperty ("MAXHP_KNIGHT"));
			Configurations.MAX_MP_KNIGHT =Integer.valueOf (p.getProperty ("MAXMP_KNIGHT"));
			Configurations.MAX_HP_ELF   = Integer.valueOf (p.getProperty ("MAXHP_ELF"));
			Configurations.MAX_MP_ELF   = Integer.valueOf (p.getProperty ("MAXMP_ELF"));
			Configurations.MAX_HP_MAGE  = Integer.valueOf (p.getProperty ("MAXHP_MAGE"));
			Configurations.MAX_MP_MAGE  = Integer.valueOf (p.getProperty ("MAXMP_MAGE")); 
			Configurations.MAX_HP_DARKELF=Integer.valueOf (p.getProperty ("MAXHP_DARKELF"));
			Configurations.MAX_MP_DARKELF=Integer.valueOf (p.getProperty ("MAXMP_DARKELF"));
			
			Configurations.ALLOW_DROP_ITEM=Boolean.valueOf(p.getProperty ("ALLOW_DROP_ITEM"));
			Configurations.DISPLAY_CHAT=Boolean.valueOf(p.getProperty ("DISPLAY_CHAT"));
			Configurations.LOG_CHAT=Boolean.valueOf(p.getProperty ("LOG_CHAT"));
			
			p.load (new FileInputStream ("configs/server.properties"));
			Configurations.SERVER_PORT = Integer.valueOf (p.getProperty ("ServerPort"));
			Configurations.MANAGE_PORT = Integer.valueOf (p.getProperty ("AdminPort"));
			Configurations.MAX_PLAYER  = Integer.valueOf (p.getProperty ("MaxPlayer"));
			Configurations.USE_BLOCKCHAIN = Boolean.valueOf (p.getProperty ("UseBlockchain"));
			Configurations.USE_GUI = Boolean.valueOf (p.getProperty ("UseLocalGui")) ;
			Configurations.SIGHT_RAGNE = Integer.valueOf (p.getProperty ("SightRange"));
			Configurations.CASINO = Boolean.valueOf (p.getProperty ("CasinoSystem"));
			Configurations.MONSTER_GENERATOR_UPDATE_RATE = Integer.valueOf (p.getProperty ("MonsterGeneratorUpdateRate"));
			Configurations.MONSTER_AI_UPDATE_RATE = Integer.valueOf (p.getProperty ("MonsterAiUpdateRate"));
			Configurations.DEFAULT_MOVEMENT_RANGE = Integer.valueOf (p.getProperty ("DefaultMovementRange"));
			Configurations.PARALLELISM_THRESHOLD = Integer.valueOf (p.getProperty ("ParallelismThreshold"));
			System.out.printf ("success\n");
			
		} catch (Exception e) {
			e.printStackTrace ();
		}
		
	}
	
	public void update () 
	{
		//
	}
}
