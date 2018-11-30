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
import laevatein.game.template.*;

public class PcInstance 
	extends Objeto 
	implements Moveable, ApAccessable, ItemProcessable, SkillAffect
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
	
	/* 飽食度 0~29 29=100%*/
	public int satiation;
	
	/* 開發人員設定 */
	public boolean isRd = false;
	public boolean isGm = false;
	
	/* 持有道具 */
	public ConcurrentHashMap<Integer, ItemInstance> itemBag;
	public int weight;
	public int weightScale30; //for cache 0-29
	
	/* 道具延遲效果 */
	public ConcurrentHashMap<Integer, Long> itemDelay;
	
	/* 人物裝備 */
	public Equipment equipment;
	
	/* 技能BUFF狀態 */
	//public SkillEffectsContainer buffs;
	private ConcurrentHashMap<Integer, SkillEffect> buffs;
	
	/* A.P. */
	public AbilityParameter basicParameters;
	public AbilityParameter skillParameters;
	public AbilityParameter equipmentParameters;
	
	/* 視線內物件 <K, V> = <UUID, 實體> */
	public ConcurrentHashMap<Integer, PcInstance> pcsInsight;
	public ConcurrentHashMap<Integer, Objeto> objectsInsight;
	
	/* 戰鬥狀態&移動狀態剩餘秒數 */
	public int battleCounter;
	public int moveCounter;
	
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
		objectsInsight = new ConcurrentHashMap<Integer, Objeto> ();
	}
	
	public void setHandle (SessionHandler _handle) {
		handle = _handle;
	}
	
	public SessionHandler getHandle () {
		return handle;
	}
	
	public void sendPacket (byte[] packet) {
		handle.sendPacket (packet);
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
				originGfx = rs.getInt ("Class");
				gfx = originGfx;
				
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
				
				basicParameters.hpR = Utility.calcHpr (basicParameters.con);				
				basicParameters.mpR = Utility.calcMpr (basicParameters.wis);
				
				hp = rs.getInt ("CurHp");
				mp = rs.getInt ("CurMp");
				basicParameters.maxHp = rs.getInt ("MaxHp");
				basicParameters.maxMp = rs.getInt ("MaxMp");
				
				//buffs = new SkillEffectsContainer (this);
				buffs = new ConcurrentHashMap<Integer, SkillEffect> ();
				
				rs.getInt ("PKcount");
				
				//routineTask = new (this);
				sight = new SightUpdate (this);
				
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
		//System.out.println ("load item bag");
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
			}
			
			itemBag.forEach ((Integer iid, ItemInstance e)->{
				if (e.isWeapon () && e.isUsing) {
					setWeapon (iid);
				}				
				
				if (e.isArmor () && e.isUsing) {
					equipment.set (e);
				}
				
				if (e.isArrow () && e.isUsing) {
					equipment.set (e);
				}
			});
			
			equipmentParameters = null;
			equipmentParameters = equipment.getAbilities ();
			
			updateWeightCache ();
			
			handle.sendPacket (new ReportItemBag (itemBag).getRaw ());
			
		} catch (Exception e) {
			e.printStackTrace ();
		}
	}
	
	public void saveItemBag () {
		//System.out.println ("save item bag");
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
		/*
		buffs.forEachKey (Configurations.PARALLELISM_THRESHOLD, (Integer skillId)->{
			removeSkillEffect (skillId);
		});*/
		//buffs.clear ();
		
		ResultSet rs = null;
		try {
			rs = DatabaseCmds.loadSkillEffects (uuid);
			while (rs.next ()) {
				int skillId = rs.getInt ("skill_id");
				int remainTime = rs.getInt ("remaining_time");
				int polyGfx = rs.getInt ("poly_id");
				
				addSkillEffect (skillId, remainTime, polyGfx);
			}
		} catch (Exception e) {
			e.printStackTrace ();
		} finally {
			DatabaseUtil.close (rs);
		}
	}
	
	public void saveBuffs () {
		System.out.println ("save buffs");
		DatabaseCmds.deleteSkillEffects (uuid);//清空全部記錄
		buffs.forEach ((Integer skillId, SkillEffect buff)->{
			DatabaseCmds.insertSkillEffect (uuid, skillId, buff.remainTime, buff.polyGfx);
		});
	}
	
	//檢查是否超過可負載重量
	public boolean canCarryWeight (int _weight) {
		return (weight + _weight) < getMaxWeight ();
	}
	
	public void updateWeightCache () {
		int prevW30 = weightScale30;
		
		weight = getWeight ();
		weightScale30 = getWeightInScale30 ();
		
		if (prevW30 != weightScale30) {
			//TODO:改用s_op:62更新
			handle.sendPacket (new ModelStatus (this).getRaw ());
		}
	}
	
	public void updateSpMr () {
		handle.sendPacket (new ReportSpMr (getSp (), getMr ()).getRaw ());
	}
	
	public void updateAc () {
		handle.sendPacket (new UpdateAc (getAc ()).getRaw ());
	}
	
	public void updateLevelExp () {
		handle.sendPacket (new UpdateExp (this).getRaw ());
	}
	
	//回報全部道具重量
	public int getWeight () {
		int totalWeight = 0;
		
		Iterator<ItemInstance> weights = itemBag.values ().iterator ();
		while (weights.hasNext ()) {
			totalWeight += ((ItemInstance) weights.next ()).getWeight ();
		}
		
		return totalWeight;
	}
	
	//回報可負載重量
	public int getMaxWeight () {
		int maxWeight = 1500 + (((getStr () + getCon () - 18) >> 1) * 150);
		//TODO:apply skill effect
		//TODL:負重強化
		
		//TODO:apply equip effect
		//TODO:多羅皮帶, 歐吉皮帶, 泰坦腰帶
		
		//apply doll effect
		
		return maxWeight * 1000;
	}
	
	public int getWeightInScale30 () {
		return (getWeight() * 100) / (int) (getMaxWeight() * 3.4);
	}
	
	public void setWeapon (int wUuid) {
		ItemInstance w = itemBag.get (wUuid);
		
		//檢查變身時不能用的武器
		if (hasSkillEffect (SkillId.SHAPE_CHANGE)) {
			int polyId = buffs.get (SkillId.SHAPE_CHANGE).polyGfx;
			PolyTemplate poly = CacheData.poly.get (polyId);
			if (!poly.weaponType.get (w.minorType)) {
				return; //變身時不能用的武器
			}
		}
		
		equipment.setWeapon (w);
		equipmentParameters = null;
		equipmentParameters = equipment.getAbilities ();
		
		updateWeightCache ();
	}
	
	public void setArmor (int aUuid) {
		ItemInstance a = itemBag.get (aUuid);
		
		//檢查變身時不能用的裝備
		if (hasSkillEffect (SkillId.SHAPE_CHANGE)) {
			int polyId = buffs.get (SkillId.SHAPE_CHANGE).polyGfx;
			PolyTemplate poly = CacheData.poly.get (polyId);
			if (!poly.armorType.get (a.minorType)) {
				return; //變身時不能用的裝備
			}
		}
		
		equipment.setEquipment (a);
		equipmentParameters = null;
		equipmentParameters = equipment.getAbilities ();
		
		updateWeightCache ();
		//updateAc ()
		handle.sendPacket (new ReportSpMr (getSp(), getMr()).getRaw ());
	}
	
	public void setArrow (int aUuid) {
		equipment.setArrow (itemBag.get (aUuid));
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

	public boolean isFaceTo (Location _loc) {
		return false;
	}
	
	@Override
	public byte[] getPacket () {
		PacketBuilder packet = new PacketBuilder ();
		
		packet.writeByte (ServerOpcodes.MODEL_PACK);
		packet.writeWord (loc.p.x);
		packet.writeWord (loc.p.y);
		packet.writeDoubleWord (uuid);
		packet.writeWord (gfx); //外型
		packet.writeByte (actId); //動作
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
		battleCounter = 0x20; //check for 32s
	}

	@Override
	public void receiveSkillAttack (SkillAttack sAtk) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void receiveSkillBuff (SkillBuff sBuff) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void die () {
		//TODO
		isDead = true;
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
		case 0: case 73:
			heading = 0; y--;
			break;
			
		case 1: case 72:
			heading = 1; x++; y--;
			break;
			
		case 2: case 75:
			heading = 2; x++;
			break;
			
		case 3: case 74:
			heading = 3; x++; y++;
			break;
			
		case 4: case 77:
			heading = 4; y++;
			break;	
			
		case 5: case 76:
			heading = 5; x--; y++;
			break;
			
		case 6: case 79:
			heading = 6; x--;
			break;
			
		case 7: case 78:
			heading = 7; x--; y--;
			break;

		default : break;
		}
		
		//廣播移動訊息(x, y)
		byte[] movePacket = new ModelMove (uuid, loc.p.x, loc.p.y, heading).getRaw ();
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
		
		moveCounter = 0x10; //check for 16s
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
	public int getHpR () {
		return basicParameters.hpR + skillParameters.hpR + equipmentParameters.hpR;
	}

	@Override
	public int getMpR () {
		return basicParameters.mpR + skillParameters.mpR + equipmentParameters.mpR;
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
			
			if (!item.isTradeable) { //不可轉移
				handle.sendPacket (new GameMessage (210, item.getName ()).getRaw ());
				return;
			}
			
			if (item.isUsing) {
				handle.sendPacket (new GameMessage (125, item.getName ()).getRaw ());
				return;
			}
			
			ItemInstance dropItem = null;
			
			if (item.count > count) { //丟出數量小於持有數量
				removeItem (itemUuid, count);
				dropItem = new ItemInstance (item.id, UuidGenerator.next(), 0, item.enchant, count, item.durability, item.chargeCount, false, false);
				
			} else { //丟出數量大於等於持有數量
				removeItem (itemUuid, count);
				dropItem = item;
				dropItem.ownerUuid = 0;
				
			}
			
			//create instance on ground
			DropInstance drop = new DropInstance (dropItem);
			
			drop.loc.mapId = loc.mapId;
			drop.loc.p.x = x;
			drop.loc.p.y = y;
			
			drop.boardcastPcInsight (drop.getPacket ());
			map.addModel (drop);
			
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
			
			//handle.sendPacket (new UpdateItemAmount (i).getRaw ());
			//handle.sendPacket (new UpdateItemName (i).getRaw ());
			handle.sendPacket (new UpdateItemStatus (i).getRaw ());
		} else {
			itemBag.put (item.uuid, item);
			
			handle.sendPacket (new ItemInsert (item).getRaw ());
		}
		
		//更新重量快取
		updateWeightCache ();
	}

	@Override
	public synchronized void addItem (int itemId, int amount) {
		List<ItemInstance> foundItems = findItemById (itemId);
		
		if (foundItems.size () > 0) { //found!
			ItemInstance item = itemBag.get (foundItems.get (0).uuid);
			if (item.isStackable) {
				item.count += amount;
				
				//handle.sendPacket (new UpdateItemAmount (item).getRaw ());
				//handle.sendPacket (new UpdateItemName (item).getRaw ());
				handle.sendPacket (new UpdateItemStatus (item).getRaw ());
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
		
		//更新重量快取
		updateWeightCache ();
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
				
				//handle.sendPacket (new UpdateItemAmount (item).getRaw ());
				//handle.sendPacket (new UpdateItemName (item).getRaw ());
				DatabaseCmds.updateItem (item);
				handle.sendPacket (new UpdateItemStatus (item).getRaw ());
				
			} else { //全數清除
				itemBag.remove (item.uuid);
				
				DatabaseCmds.deleteItem (item);
				handle.sendPacket (new ItemRemove (item).getRaw ());
			}
			
			//更新重量快取
			updateWeightCache ();
		}
	}
	
	public void deleteItem (ItemInstance i) {
		if (i.id == 40308) {
			handle.sendPacket (new GameMessage (992).getRaw ()); //金幣不可刪除
			return;
		}
		
		if (i.isUsing) {
			handle.sendPacket (new GameMessage (GameMessageId.$125).getRaw ());
		} else {
			removeItem (i);
		}
	}

	/*
		取得itemid上的時間跟stamp差多少時間(ms)
	 */
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

	/*
		給itemid打上當前time stamp
	 */
	@Override
	public void setItemDelay (int itemId, long nowTime) {
		itemDelay.put (itemId, nowTime);
	}
	
	//
	//skill effect interface
	//

	@Override
	public boolean hasSkillEffect (int skillId) {
		return buffs.containsKey (skillId);
	}

	@Override
	public void addSkillEffect (int skillId, int time) {
		addSkillEffect (skillId, time, 0);
	}

	@Override
	public void addSkillEffect (int skillId, int time, int polyGfx) {
		//TODO:更新變身
		if (buffs.containsKey (skillId)) {//updates
			SkillEffect buff = buffs.get (skillId);
			buff.remainTime = time;
			buff.polyGfx = polyGfx;
			//buff.addSkillEffect (this);
			buff.sendSkillIcon (this);
		} else { //add new
			SkillEffect buff = new SkillEffect (skillId, time, polyGfx);
			buff.addSkillEffect (this);
			buffs.put (skillId, buff);
		}
	}
	
	@Override
	public void removeSkillEffect (int skillId) {
		if (buffs.containsKey (skillId)) {
			buffs.get (skillId).removeSkillEffect (this);
			buffs.remove (skillId);
		}
	}
	
	@Override
	public void updateBuffTime () {//一定要1s interval執行
		buffs.forEach ((Integer skillId, SkillEffect buff)->{
			if (buff.remainTime == 0xFFFF) { //永久技能效果
				return;
				
			} else if (buff.remainTime > 0) { //減少持續時間
				buff.remainTime--;
				
			} else { //持續時間為零, 停止技能效果
				removeSkillEffect (skillId);
				
			}
		});
	}

}
