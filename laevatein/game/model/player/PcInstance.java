package laevatein.game.model.player;

import java.sql.*;
import java.util.*;
import java.util.concurrent.*;

import laevatein.types.*;
import laevatein.config.*;

import laevatein.server.*;
import laevatein.server.database.*;
import laevatein.server.utility.*;
import laevatein.server.packet.*;
import laevatein.server.opcodes.*;
import laevatein.server.process_server.*;

import laevatein.game.*;
import laevatein.game.map.*;
import laevatein.game.model.*;
import laevatein.game.model.item.*;
import laevatein.game.skill.*;
import laevatein.game.routine_task.*;

public class PcInstance extends Objeto implements Moveable, ApAccessable, ItemProcessable
{
	private SessionHandler handle;
	public boolean isExit;
	
	public LaeMap map;
	
	public static final int TYPE_ROYAL   = 0;
	public static final int TYPE_KNIGHT  = 1;
	public static final int TYPE_ELF     = 2;
	public static final int TYPE_WIZARD  = 3;
	public static final int TYPE_DARKELF = 4;
	/* 角色職業類別 */
	public int type;
	
	public static final int SEX_MALE   = 0;
	public static final int SEX_FEMALE = 1;
	/* 角色性別 */
	public int sex;
	
	/* 飽食度 */
	public int satiation;
	
	/* 開發人員設定 */
	public boolean isRd = false;
	public boolean isGm = false;
	
	/* 持有道具 */
	public ConcurrentHashMap<Integer, ItemInstance> itemBag;
	
	/* 道具延遲效果 */
	public ConcurrentHashMap<Integer, Long> itemDelay;
	
	/* 人物裝備 */
	public Equipment equipment;
	
	/* A.P. */
	public AbilityParameter basicParameters;
	public AbilityParameter skillParameters;
	public AbilityParameter equipmentParameters;
	
	/* 視線內物件 <K, V> = <UUID, 實體> */
	public ConcurrentHashMap<Integer, PcInstance> pcsInsight;
	public ConcurrentHashMap<Integer, Objeto> modelsInsight;
	
	public SightUpdate sight;
	public HsTask hsTask;
	public LsTask lsTask;
	
	public PcInstance (SessionHandler _handle) {
		handle = _handle;
		
		basicParameters = new AbilityParameter (); //for setup basic
		loc = new Location ();
		
		itemDelay = new ConcurrentHashMap<Integer, Long> ();
		//skillDelay
		//skillBuffs
		
		pcsInsight = new ConcurrentHashMap<Integer, PcInstance> ();
		modelsInsight = new ConcurrentHashMap<Integer, Objeto> ();
	}
	
	public void setHandle (SessionHandler _handle) {
		handle = _handle;
	}
	
	public SessionHandler getHandle () {
		return handle;
	}
	
	public void updateOnlineStatus (boolean isOnline) {
		Connection con = HikariCP.getConnection ();
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement ("UPDATE characters SET OnlineStatus=? WHERE objid=?;");
			ps.setInt (1, (isOnline) ? 1:0);
			ps.setInt (2, uuid);
			
			ps.execute ();	
		} catch (Exception e) {
			e.printStackTrace ();
		} finally {
			DatabaseUtil.close (ps);
			DatabaseUtil.close (con);
		}
	}
	
	public boolean load (String _name) {
		boolean result = false;
		
		Connection con = HikariCP.getConnection ();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = con.prepareStatement ("SELECT * FROM characters WHERE char_name=?");
			ps.setString (1, _name);
			
			rs = ps.executeQuery ();
			if (rs.next ()) {
				loc.mapId = rs.getInt ("MapID");
				loc.p.x = rs.getInt ("LocX");
				loc.p.y = rs.getInt ("LocY");
				heading = rs.getInt ("Heading");
				if (loc.mapId > MapLoader.MAPID_LIMIT) {
					loc.mapId = 0;
				}
				
				map = Laevatein.getInstance ().getMap (loc.mapId);
				
				uuid = rs.getInt ("objid");
				name = rs.getString ("char_name");
				title = rs.getString ("Title");
				clanId = rs.getInt ("ClanID");
				if (clanId > 0) {
					clanName = rs.getString ("Clanname");
				}
				
				satiation = rs.getInt ("Food");	
				
				status = rs.getInt ("Status");
				status |= StatusId.STATUS_PC;
				
				level = rs.getInt ("level");
				exp = rs.getInt ("Exp");
				//expRes = rs.getInt ("ExpRes");
				
				sex = rs.getInt ("Sex");
				type = rs.getInt ("Type");
				gfx = rs.getInt ("Class");
				
				basicParameters = new AbilityParameter ();
				skillParameters = new AbilityParameter ();
				equipmentParameters = new AbilityParameter ();
				
				basicParameters.str = rs.getByte ("Str");
				basicParameters.con = rs.getByte ("Con");
				basicParameters.dex = rs.getByte ("Dex");
				basicParameters.wis = rs.getByte ("Wis");
				basicParameters.cha = rs.getByte ("Cha");
				basicParameters.intel = rs.getByte ("Intel");
				
				//load bonus parameters
				basicParameters.ac = Utility.calcAcBonusFromDex (level, basicParameters.dex);
				basicParameters.mr = Utility.calcMr (type, level, basicParameters.wis);
				basicParameters.sp = Utility.calcSp (type, level, basicParameters.intel);
				
				basicParameters.hpR = Utility.calcHpr (10);				
				basicParameters.mpR = Utility.calcMpr (10);
				
				hp = rs.getInt ("CurHp");
				mp = rs.getInt ("CurMp");
				basicParameters.maxHp = rs.getInt ("MaxHp");
				basicParameters.maxMp = rs.getInt ("MaxMp");
				
				equipment = new Equipment ();
				
				rs.getInt ("PKcount");
				
				//routineTask = new (this);
				sight = new SightUpdate (this);
				hsTask = new HsTask (this);
				lsTask = new LsTask (this);
				
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace ();
		} finally {
			DatabaseUtil.close (rs);
			DatabaseUtil.close (ps);
			DatabaseUtil.close (con);
		} //end of try
		
		return result;
	}
	
	public void save () {
		//save skill effect
		
		//save item bag
		
		//save pc basic
		
		sight.stop ();
		hsTask.stop ();
		lsTask.stop ();
		
		Laevatein.getInstance ().removePlayer (this);
		
		saveItemBag ();
		saveSkills ();
		saveBuffs ();
		
		DatabaseCmds.savePc (this);
	}
	
	public void loadItemBag () {
		System.out.println ("load item bag");
		itemBag = new ConcurrentHashMap<Integer, ItemInstance> ();
		ResultSet rs = null;
		try {
			rs = DatabaseCmds.loadPcItems (uuid);
			while (rs.next ()) {
				int itemId = rs.getInt ("item_id");
				int itemUuid = rs.getInt ("id") ;
				int itemOwnerUuid = rs.getInt ("char_id");
				int itemCount = rs.getInt ("count");
				int itemEnchant = rs.getInt ("enchantlvl");
				int itemDurability = rs.getInt ("durability");
				int itemChargeCount = rs.getInt ("charge_count");
				boolean itemIsUsing = rs.getBoolean ("is_equipped");
				boolean itemIsIdentified = rs.getBoolean ("is_id");

				//生成ItemInstace, WeaponInstance, ArmorInstance
				ItemInstance item = new ItemInstance (itemId, itemUuid, itemOwnerUuid, itemEnchant, itemCount, itemDurability, itemChargeCount, itemIsUsing, itemIsIdentified);
				itemBag.put (item.uuid, item);
				
				if (item.isWeapon () && item.isUsing) {
					//
				} else if (item.isArmor () && item.isUsing) {
					//
				} else if (item.isArrow () && item.isUsing) {
					//
				}
			}
			
			handle.sendPacket (new ReportItemBag (itemBag).getRaw ());
			
		} catch (Exception e) {
			e.printStackTrace ();
		}
	}
	
	public void saveItemBag () {
		System.out.println ("save item bag");
		DatabaseCmds.savePcItems (uuid, itemBag);
	}
	
	public void loadSkills () {
		System.out.println ("load skills");
	}
	
	public void saveSkills () {
		System.out.println ("save skills");
	}
	
	public void loadBuffs () {
		System.out.println ("load buffs");
	}
	
	public void saveBuffs () {
		System.out.println ("save buffs");
	}
	
	//檢查是否超過可負載重量
	public boolean canCarryWeight (int weight) {
		return true;
	}
	
	//回報全部道具重量
	public int getWeight () {
		int totalWeight = 0;
		ArrayList<Integer> allItemsWeight = new ArrayList<Integer> ();

		itemBag.forEachValue (Configurations.PARALLELISM_THRESHOLD, (ItemInstance i)->{
			//System.out.printf ("%s weight:%d\n", i.getName (), i.weight);
			allItemsWeight.add (i.weight);
		});
		
		for (int w : allItemsWeight) {
			totalWeight += w;
		}
		
		return totalWeight;
	}
	
	//回報可負載重量
	public int getMaxWeight () {
		int maxWeight = 1500 + (((getStr () + getCon () - 18) >> 1) * 150);
		//apply skill effect
		//apply equip effect
		//apply doll effect
		
		return maxWeight * 1000;
	}
	
	public int getWeightInScale30 () {
		return (getWeight() * 100) / (int) (getMaxWeight() * 3.4);
	}
	
	public void setWeapon (int wUuid) {
		equipment.setWeapon (itemBag.get (wUuid));
	}
	
	public void setArmor (int aUuid) {
		equipment.setArmor (itemBag.get (aUuid));
	}
	
	public void setArrow (int aUuid) {
		equipment.setArrow (itemBag.get (aUuid));
		//$452 + arg1 :/f1 arg1 被選擇了
	}
	
	public void setSting (int sUuid) {
		equipment.setSting (itemBag.get (sUuid));
	}
	
	public boolean isRoyal () {
		return (type == TYPE_ROYAL);
	}
	
	public boolean isKnight () {
		return (type == TYPE_KNIGHT);
	}
	
	public boolean isElf () {
		return (type == TYPE_ELF);
	}
	
	public boolean isWizard () {
		return (type == TYPE_WIZARD);
	}
	
	public boolean isDarkelf () {
		return (type == TYPE_DARKELF);
	}

	
	@Override
	public byte[] getPacket () {
		PacketBuilder packet = new PacketBuilder ();
		
		packet.writeByte (ServerOpcodes.MODEL_PACK);
		packet.writeWord (loc.p.x);
		packet.writeWord (loc.p.y);
		packet.writeDoubleWord (uuid);
		packet.writeWord (gfx); //外型
		//packet.writeByte (actId); //動作
		packet.writeByte (4);
		packet.writeByte (heading); //方向
		packet.writeByte (light);
		packet.writeByte (moveSpeed);
		packet.writeDoubleWord (exp);
		packet.writeWord (lawful);
		packet.writeString (name);
		packet.writeString (title);
		packet.writeByte (status);
		packet.writeDoubleWord (clanId);
		packet.writeString (clanName);
		packet.writeString (null);
		packet.writeByte (0x00);
		packet.writeByte (hpScale); //血條百分比
		packet.writeByte (0x00);
		packet.writeByte (levelScale);
		packet.writeByte (0x00);
		packet.writeByte (0xFF);
		packet.writeByte (0xFF);
		
		return packet.getPacket ();
	}

	@Override
	public void boardcastPcInsight (byte[] packet) {
		pcsInsight.forEachValue (Configurations.PARALLELISM_THRESHOLD, (PcInstance p)->{
			p.handle.sendPacket (packet);
		});
	}

	@Override
	public void receiveAttack (NormalAttack attack) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void move (int x, int y, int _heading) {
		moveToHeading (_heading);
	}

	@Override
	public void moveToHeading (int _heading) {
		int x = loc.p.x;
		int y = loc.p.y;
		
		map.setOccupied (x, y, false); //離開原本座標
		switch (_heading) {
		case 0:
		case 73:
			heading = 0; y--;
			break;
			
		case 1:
		case 72:
			heading = 1; x++; y--;
			break;
			
		case 2:
		case 75:
			heading = 2; x++;
			break;
			
		case 3:
		case 74:
			heading = 3; x++; y++;
			break;
			
		case 4:
		case 77:
			heading = 4; y++;
			break;	
			
		case 5:
		case 76:
			heading = 5; x--; y++;
			break;
			
		case 6:
		case 79:
			heading = 6; x--;
			break;
			
		case 7:
		case 78:
			heading = 7; x--; y--;
			break;

		default : break;
		}
		
		//廣播移動訊息(x, y)
		byte[] movePacket = new ModelMove (uuid, loc.p.x, loc.p.y, _heading).getRaw ();
		boardcastPcInsight (movePacket);
		
		//檢查是不是在傳送位址
		if (map.isTpEntrance (x, y)) {
			Location dest = map.getTpDestination (x, y);
			new Teleport (this, dest, false);
			return;
		}
		
		//更新自身位置
		loc.p.x = x;
		loc.p.y = y;
		map.setOccupied (x, y, true);
		
	}

	@Override
	public int getStr () {
		return basicParameters.str + skillParameters.str + equipmentParameters.str;
	}

	@Override
	public int getCon () {
		return basicParameters.con + skillParameters.con + equipmentParameters.con;
	}

	@Override
	public int getDex () {
		return basicParameters.dex + skillParameters.dex + equipmentParameters.dex;
	}

	@Override
	public int getWis () {
		return basicParameters.wis + skillParameters.wis + equipmentParameters.wis;
	}

	@Override
	public int getCha () {
		return basicParameters.cha + skillParameters.cha + equipmentParameters.cha;
	}

	@Override
	public int getIntel () {
		return basicParameters.intel + skillParameters.intel + equipmentParameters.intel;
	}

	@Override
	public int getMaxHp () {
		return basicParameters.maxHp + skillParameters.maxHp + equipmentParameters.maxHp;
	}

	@Override
	public int getMaxMp () {
		return basicParameters.maxMp + skillParameters.maxMp + equipmentParameters.maxMp;
	}

	@Override
	public int getSp () {
		return basicParameters.sp + skillParameters.sp + equipmentParameters.sp;
	}

	@Override
	public int getMr () {
		return basicParameters.mr + skillParameters.mr + equipmentParameters.mr;
	}

	@Override
	public int getAc () {
		return basicParameters.ac + skillParameters.ac + equipmentParameters.ac;
	}

	public List<ItemInstance> findItemById (int itemId) {
		List<ItemInstance> result = new ArrayList<ItemInstance> ();
		itemBag.forEachValue (Configurations.PARALLELISM_THRESHOLD, (ItemInstance i)->{
			if (i.id == itemId) {
				result.add (i);
			}
		});
		
		return result;
	}
	
	public int getMoney () {
		int money = 0;
		List<ItemInstance> find = findItemById (40308);
		if (find.size () > 0) {
			money = find.get (0).count;
		}
		return money;
	}
	
	public void addMoney (int amount) {
		addItem (40308, amount);
	}
	
	@Override
	public void pickItem (int itemUuid, int count, int x, int y) {
		
		DropInstance pick = (DropInstance) map.getModel (itemUuid);
		if (pick != null) {
			heading = getDirection (x, y);
			
			byte[] actionPacket = new ModelAction (ActionId.PICK_UP, uuid, heading).getRaw ();
			byte[] removeObjPacket = new RemoveModel (pick.uuid).getRaw ();
			
			handle.sendPacket (actionPacket);
			handle.sendPacket (removeObjPacket);
			boardcastPcInsight (removeObjPacket);
			boardcastPcInsight (actionPacket);
			
			ItemInstance i = pick.getItemInstance ();
			map.removeModel (i.uuid);
			i.ownerUuid = uuid;
			addItem (i);
		}
	}

	@Override
	public void dropItem (int itemUuid, int count, int x, int y) {		
		if (itemBag.containsKey (itemUuid)) {
			ItemInstance item = itemBag.get (itemUuid);
			ItemInstance dropItem = null;
			
			removeItem (itemUuid, count);
			
			if (item.count > count) { //丟出數量小於持有數量
				dropItem = new ItemInstance (item.id, UuidGenerator.next(), 0, item.enchant, count, item.durability, item.chargeCount, false, false);
				
			} else { //丟出數量大於等於持有數量
				dropItem = item;
				dropItem.ownerUuid = 0;
				
			}
			//create instance on ground
			DropInstance drop = new DropInstance (dropItem);
			
			drop.loc.mapId = loc.mapId;
			drop.loc.p.x = x;
			drop.loc.p.y = y;
			
			map.addModel (drop);
			
			handle.sendPacket (new ModelStatus (this).getRaw ());
		} //if contains item
	}

	@Override
	public void giveItem () {
		// TODO Auto-generated method stub
	}

	@Override
	public void recvItem () {
		// TODO Auto-generated method stub
	}

	@Override
	public synchronized void addItem (ItemInstance item) {
		List<ItemInstance> foundItems = findItemById (item.id);
		
		if (item.isStackable && (foundItems.size () > 0)) {
			ItemInstance i = itemBag.get (foundItems.get (0).uuid);
			i.count += item.count;
			
			handle.sendPacket (new UpdateItemAmount (i).getRaw ());
			handle.sendPacket (new UpdateItemName (i).getRaw ());
		} else {
			itemBag.put (item.uuid, item);
			
			handle.sendPacket (new ItemInsert (item).getRaw ());
		}
	}

	@Override
	public synchronized void addItem (int itemId, int amount) {
		List<ItemInstance> foundItems = findItemById (itemId);
		
		if (foundItems.size () > 0) { //found!
			ItemInstance item = itemBag.get (foundItems.get (0).uuid);
			if (item.isStackable) {
				item.count += amount;
				
				handle.sendPacket (new UpdateItemAmount (item).getRaw ());
				handle.sendPacket (new UpdateItemName (item).getRaw ());
			} else {
				ItemInstance newItem = new ItemInstance (itemId, UuidGenerator.next (), uuid, item.enchant, amount, item.durability, item.chargeCount, false, false);
				itemBag.put (newItem.uuid, newItem);
				
				handle.sendPacket (new ItemInsert (newItem).getRaw ());
			}
			
		} else { //not found, create new
			ItemInstance item = new ItemInstance (40308, UuidGenerator.next(), uuid, 0, amount, 0, 0, false, true);
			itemBag.put (item.uuid, item);
			
			handle.sendPacket (new ItemInsert (item).getRaw ());
		}
	}

	public void removeItemById (int itemId, int amount) {
		ItemInstance item = itemBag.searchValues (Configurations.PARALLELISM_THRESHOLD, (ItemInstance i)->{
			return (i.id == itemId) ? i:null;
		});
		removeItem (item.uuid, amount);
	}
	
	@Override
	public synchronized void removeItem (ItemInstance item) {
		removeItem (item.uuid, item.count);
	}

	@Override
	public synchronized void removeItem (int itemUuid, int amount) {
		if (itemBag.containsKey (itemUuid)) {
			ItemInstance item = itemBag.get (itemUuid);
			if (item.count > amount) { //還有剩餘道具
				item.count -= amount;
				
				DatabaseCmds.updateItem (item);
				handle.sendPacket (new UpdateItemAmount (item).getRaw ());
				handle.sendPacket (new UpdateItemName (item).getRaw ());
				
			} else { //全數清除
				itemBag.remove (item.uuid);
				
				DatabaseCmds.deleteItem (item);
				handle.sendPacket (new ItemRemove (item).getRaw ());
			}
			
		}
	}

	@Override
	public long getItemDelay (int itemId, long nowTime) {
		long res;
		if (itemDelay.containsKey (itemId)) {
			res = nowTime - itemDelay.get (itemId);
		} else {
			res = Long.MAX_VALUE;
		}
		return res;
	}

	@Override
	public void setItemDelay (int itemId, long nowTime) {
		itemDelay.put (itemId, nowTime);
	}
	
}
