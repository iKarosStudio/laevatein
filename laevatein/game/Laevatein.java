package laevatein.game;

import java.util.*;
import java.util.concurrent.*;

import laevatein.config.*;

import laevatein.server.*;
import laevatein.server.ai_service.*;
import laevatein.server.threadpool.*;
import laevatein.server.utility.*;
import laevatein.server.process_server.*;

import laevatein.game.map.*;
import laevatein.game.model.*;
import laevatein.game.model.player.*;

public class Laevatein extends Thread
{
	private static Laevatein instance;
	
	/* 所有管理地圖 */
	private static ConcurrentHashMap<Integer, LaeMap> maps;
	
	/* 全局等級狀態 */
	private static int onlinePlayers = 0;
	public static ServerTime time = null;
	
	/* 世界天氣參數 */
	public static final byte WEATHER_SNOW = 0x00;
	public static final byte WEATHER_RAIN = 0x10;
	public static final byte WEATHER_DEGREE_NONE = 0x00;
	public static final byte WEATHER_DEGREE_LIGHT = 0x01;
	public static final byte WEATHER_DEGREE_MEDIUM = 0x02;
	public static final byte WEATHER_DEGREE_HEAVY = 0x03;
	private int weather = 0x00;
	
	public void run () {
		//
	}
	
	public int getOnlinePlayers () {
		return onlinePlayers;
	}
	
	public static Laevatein getInstance () {
		if (instance == null) {
			instance = new Laevatein ();
		}
		return instance;
	}
	
	public Laevatein () {
		System.out.println ("creating laevatein game instance in " + this);
		maps = new ConcurrentHashMap<Integer, LaeMap> ();
	}
	
	public void initialize () {
		try {
			/* 快取遊戲資料 */
			CacheData.getInstance ();
			
			/* 取得DB中最後一個UUID */
			UuidGenerator.getInstance ();
			
			/* 載入遊戲地圖 */
			MapInfo.getInstance ();
			new MapLoader (instance);
			
			/* AI工作隊列服務 */
			AiQueue.getInstance ();
			AiExecutor.getInstance ();
			
			/* Load npc */
			new NpcLoader (instance);
			System.out.println ();
			
			/* Load Door */
			//DoorGenerator.getInstance ();
			
			/* Generate monster */
			System.out.printf ("Monster generator initialize interval:%.1f Sec...", (float) Configurations.MONSTER_GENERATOR_UPDATE_RATE / 1000);
			maps.forEach ((Integer mapId, LaeMap map)->{
				
				
				//System.out.printf ("map:%d\n", mapId);
			});
			//maps.forEach ((Integer map_id, VidarMap map)->{
			//	map.monsterGenerator = new MonsterGenerator (map) ;
			//	KernelThreadPool.getInstance ().ScheduleAtFixedRate (map.monsterGenerator, 1000, Configurations.MONSTER_GENERATOR_UPDATE_RATE) ;
			//}) ;
			System.out.printf ("success\n");
			
			
			/* Generate Element Stone */
			if (maps.containsKey (4)) {
				System.out.println ("元素石生產引擎...");
				//TODO: element stone gen
			}
			
			/* Start server time */
			time = ServerTime.getInstance ();
			KernelThreadPool.getInstance ().ScheduleAtFixedRate (time, 0, 1000);
			
			//load boss
			
			//load special system
			
			/* Game boardcast message */
			//sysMessage = BoardcastMessage.getInstance () ;
			//KernelThreadPool.getInstance ().ScheduleAtFixedRate (sysMessage, 10000, 30000);
			
		} catch (Exception e) {
			e.printStackTrace ();
		}
	}
	
	public void setWeather (int mapId, int _weather) {
		weather = _weather & 0x0FF;
		getAllPlayer ().forEach ((PcInstance p)->{
			p.getHandle ().sendPacket (new ReportWeather (weather).getPacket ());
		});
	}
	
	public int getWeather (int mapId) {
		return weather;
	}
	
	public void addMap (LaeMap map) {
		if (maps.containsKey (map.id)) {
			System.out.printf ("[!]caution, map over loaded. mapid=%d\n", map.id);
		} else {
			maps.put (map.id, map);
		}
	}
	
	public LaeMap getMap (int mapId) {
		return maps.get (mapId);
	}
	
	public synchronized void addPlayer (PcInstance p) {
		System.out.printf ("0x%08X-%s enter laevatein\n", p.uuid, p.name);
		maps.get (p.loc.mapId).addPlayer (p);
		onlinePlayers++;
	}
	
	public synchronized void removePlayer (PcInstance p) {
		System.out.printf ("0x%08X-%s exit laevatein\n", p.uuid, p.name);
		maps.get (p.loc.mapId).removePlayer (p.uuid);
		onlinePlayers--;
	}
	
	public PcInstance getPlayer (int uuid) {
		PcInstance result = null;
		
		List<PcInstance> pcs = getAllPlayer ();
		for (PcInstance p : pcs) {
			if (p.uuid == uuid) {
				result = p;
				break;
			}
		}
		
		return result;
	}
	
	public PcInstance getPlayerByName (String _name) {
		PcInstance result = null;

		List<PcInstance> pcs = getAllPlayer ();
		for (PcInstance p : pcs) {
			if (p.name == _name) {
				result = p;
				break;
			}
		}
		
		return result;
	}
	
	public List<PcInstance> getAllPlayer () {
		List<PcInstance> result = new ArrayList <PcInstance> ();
		
		maps.forEachValue (Configurations.PARALLELISM_THRESHOLD, (LaeMap map)->{
			map.getPlayers ().forEach ((PcInstance p)->{result.add (p);});
			//map.getPlayers ().forEachValue (Configurations.PARALLELISM_THRESHOLD, (PcInstance p)->result.add (p));
		});
		
		return result;
	}
	
	public List<PcInstance> getPlayersInMap (int mapId) {
		List<PcInstance> result = new ArrayList <PcInstance> ();
		
		LaeMap map = maps.get (mapId);
		if (map != null) {
			map.getPlayers ().forEach ((PcInstance p)->result.add (p));
		}
		
		return result;
	}
	
	public void addModel (Objeto model) {
		maps.get (model.loc.mapId).addModel (model);
	}
}
