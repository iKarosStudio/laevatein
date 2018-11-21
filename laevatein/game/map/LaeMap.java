package laevatein.game.map;

import java.util.*;
import java.util.concurrent.*;

import laevatein.types.*;
import laevatein.config.*;

import laevatein.game.model.*;
import laevatein.game.model.player.*;

public class LaeMap
{
	public static final int IS_OCCUPIED = (byte)0x80;
	public static final int IS_ARROW_PASSABLE_FROM_X = (byte)0x08;
	public static final int IS_ARROW_PASSABLE_FROM_Y = (byte)0x04;
	public static final int IS_PASSABLE_FROM_X = (byte)0x02;
	public static final int IS_PASSABLE_FROM_Y = (byte)0x01;
	
	private byte[][] tile;
	
	public final int id;
	
	private final int startX;
	private final int endX;
	private final int startY;
	private final int endY;
	
	public final int sizeX;
	public final int sizeY;
	
	private Random random = new Random (System.currentTimeMillis ());
	
	/* 生怪控制器, 數量邏輯控制 */
	//public MonsterGenerator monsterGenerator = null;
	
	/* 傳送點列表 */
	public ConcurrentHashMap<Integer, Location> tpEntry;
	
	/* 線上使用者實體 */
	//private ConcurrentHashMap<Integer, PcInstance> pcs;
	
	/* 地圖物件實體 */
	private ConcurrentHashMap<Integer, Objeto> objs;
	
	public LaeMap (int _mapId, int _startX, int _endX, int _startY, int _endY) {
		id = (short) _mapId;
		startX = _startX;
		endX = _endX;
		startY = _startY;
		endY = _endY;
		
		sizeX = endX - startX + 1;
		sizeY = endY - startY + 1;
		tile = new byte[sizeX][sizeY];
		for (int x = 0; x < sizeX; x++) {
			for (int y = 0; y < sizeY; y++) {
				tile[x][y] = 0;
			}
		}
		
		tpEntry = new ConcurrentHashMap<Integer, Location> () ;
		
		//pcs = new ConcurrentHashMap<Integer, PcInstance> ();
		objs = new ConcurrentHashMap<Integer, Objeto> ();
		
		//aiDistributor = new MonsterAiDistributor (this);
		//aiDistributor.start ();
	}
	
	public List<PcInstance> getPlayers () {
		List<PcInstance> result = new ArrayList<PcInstance> ();
		
		objs.forEach ((Integer uuid, Objeto obj)->{
			if (obj.isPc ()) {
				result.add ((PcInstance) obj);
			}
		});
		
		return result;
	}
	
	public synchronized void addPlayer (PcInstance p) {
		//pcs.putIfAbsent (p.uuid, p);
		objs.putIfAbsent (p.uuid, p);
	}
	
	public synchronized void removePlayer (int uuid) {
		//pcs.remove (uuid);
		objs.remove (uuid);
	}
	
	//could be null
	public PcInstance getPlayer (int uuid) {
		PcInstance result = null;
		if (objs.containsKey (uuid)) {
			result = (PcInstance) objs.get (uuid);
		}
		
		return result;
	}
	
	public List<PcInstance> getPcsInsight (Coordinate p) {
		List<PcInstance> result = getPcsInRange (p, Configurations.SIGHT_RAGNE);
		
		return result;
	}
	
	public List<PcInstance> getPcsInRange (Coordinate p, int range) {
		List<PcInstance> result = new ArrayList<PcInstance> ();
		
		objs.forEach ((Integer uuid, Objeto obj)->{
			if ((obj.getDistanceTo (p) < range) && (obj.isPc ())) {
				result.add ((PcInstance) obj);
			}
		});
		
		return result;
	}
	
	public synchronized void addModel (Objeto m) {
		objs.put (m.uuid, m);
	}
	
	public synchronized void removeModel (int uuid) {
		objs.remove (uuid);
	}
	
	public Objeto getModel (int uuid) {
		return objs.get (uuid);
	}
	
	public List<Objeto> getObjsInsight (Coordinate p) {
		List<Objeto> result = getObjsInRange (p, Configurations.SIGHT_RAGNE);
		return result;
	}
	
	public List<Objeto> getObjsInRange (Coordinate p, int range) {
		List<Objeto> result = new ArrayList<Objeto> ();
		objs.forEachValue (Configurations.PARALLELISM_THRESHOLD, (Objeto obj)->{
			if (obj.getDistanceTo (p) < range) {
				result.add (obj);
			}
		});
		return result;
	}	
	
	public boolean isOccupied (int x, int y) {
		return (getTile (x, y) & IS_OCCUPIED) > 0;
	}
	
	public void setOccupied (int x, int y, boolean occupied) {
		if (occupied) {
			tile[x-startX][y-startY] |= IS_OCCUPIED;
		} else {
			tile[x-startX][y-startY] &= ~IS_OCCUPIED;
		}
	}
	
	public void setTile (int x, int y, byte _tile) {
		tile[x][y] = _tile;
	}
	
	public byte getTile (int x, int y) {
		byte tmpTile = 0;
		try {
			tmpTile = tile[x - startX][y - startY];
		} catch (Exception e) {
			e.printStackTrace ();
			tmpTile = 0;
		}
		
		return tmpTile;
	}
	
	public byte getHeadingTile (int x, int y, int heading) {
		byte result = 0;
		switch (heading) {
		case 0:
			if (y == startY) {
				return 0;
			}
			result = getTile (x, y-1);
			break;
			
		case 1:
			if ((y == startY) || (x == endX)) {
				return 0;
			}
			result = getTile (x+1, y-1);
			break;
			
		case 2:
			if (x == endX) {
				return 0;
			}
			result = getTile (x+1, y);
			break;
			
		case 3:
			if ((x == endX) || (y == endX)) {
				return 0;
			}
			result = getTile (x+1, y+1);
			break;
			
		case 4:
			if (y == endX) {
				return 0;
			}
			result = getTile (x, y+1);
			break;
			
		case 5:
			if ((y == endY) || (x == startX)) {
				return 0;
			}
			result = getTile (x-1, y+1);
			break;
			
		case 6:
			if (x == startX) {
				return 0;
			}
			result = getTile (x-1, y);
			break;
			
		case 7:
			if ((x == startX) || (y == startY)) {
				return 0;
			}
			result = getTile (x-1, y-1);
			break;
			
		default:
			break;
		}
		
		return result;
	}
	
	public Location getRandomLocation () {
		Location dest = new Location (id, 0, 0);
		
		do {
			dest.p.x = startX + random.nextInt (sizeX) ;
			dest.p.y = startY + random.nextInt (sizeY) ;
		} while (getTile (dest.p.x, dest.p.y) == 0);
		
		return dest;
	}
	
	public boolean isNormalZone (Coordinate p) {
		return (getTile (p.x, p.y) & 0x30) == 0x00;
	}
	
	public boolean isSafeZone (Coordinate p) {
		return (getTile (p.x, p.y) & 0x30) == 0x10;
	}
	
	public boolean isCombatZone (Coordinate p) {
		return (getTile (p.x, p.y) & 0x30) == 0x20;
	}
	
	
	public boolean isTpEntrance (int x, int y) {
		int pos = (x << 16) | y;
		return tpEntry.containsKey (pos);	
	}
	
	public Location getTpDestination (int srcX, int srcY) {
		int src = (srcX << 16) | srcY;
		return tpEntry.get (src);
	}
	
	public void addTpLocation (int srcX, int srcY, Location dest) {
		int src = (srcX << 16) | srcY;
		tpEntry.put (src, dest);
	}
}
