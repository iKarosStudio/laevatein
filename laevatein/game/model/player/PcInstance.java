package laevatein.game.model.player;

import java.sql.*;
import java.util.*;
import java.util.concurrent.*;

import laevatein.types.*;
import laevatein.config.*;
import laevatein.callback.*;
import laevatein.constants.*;

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

/*
 * TODO:
 * 
 * 2018/12/11
 * 應加入角色各項參數更變時的callback function
 * 在裝備 藥水 升級時自動觸發更新,避免非同步bug產生 
 * 
 * 2018/12/12
 * 強迫使用getter/setter修改參數
 * 在setter中做封包回報的動作;
 * 使用道具換裝備技能更新升級
 * 一旦修改了腳色數值必須同步發送封包給客戶做更新
 * https://www.jianshu.com/p/67190bdce647
 * 
 * 2018/12/17
 * 重量快取問題優先建立統一流程
 * 道具增減統一介面已經實踐需再加入callback function
 * 
 */
public class PcInstance extends Objeto implements Moveable, ApAccessable, ItemProcessable, SkillAffect
{
	private SessionHandler handle;
	private Callback callback;
	public boolean isExit;
	
	
	LaeMap map;
	
	/* 角色職業類別 */
	int type;
	
	/* 角色性別 */
	int sex;
	
	/* 飽食度 0~29 29=100%*/
	int satiation;
	
	/* 開發人員設定 */
	public boolean isRd = false;
	public boolean isGm = false;
	
	/* 持有道具 */
	ConcurrentHashMap<Integer, ItemInstance> itemBag;
	int weight;
	int weightScale30; //負重程度 0~29 29=100%
	
	/* 道具延遲效果清單<k, v> = <道具ID, 時間戳記> */
	public ConcurrentHashMap<Integer, Long> itemDelay;
	
	/* 人物裝備 */
	public Equipment equipment;
	
	/* 技能BUFF狀態 */
	//public SkillEffectsContainer buffs;
	private ConcurrentHashMap<Integer, SkillEffect> buffs;
	
	/* A.P. (ability point)+ */
	public AbilityParameter basicParameters;
	public AbilityParameter skillParameters;
	public AbilityParameter equipmentParameters;
	
	/* 視線內物件 <K, V> = <UUID, 物件實例> */
	public ConcurrentHashMap<Integer, PcInstance> pcsInsight;
	public ConcurrentHashMap<Integer, Objeto> objectsInsight;
	
	/* 戰鬥狀態&移動狀態剩餘秒數 */
	public int battleCounter;
	public int moveCounter;
	
	/* 循環工作 */
	public SightUpdate sight;
	public HsTask hsTask;
	public LsTask lsTask;
	
	public PcInstance (SessionHandler handle) {
		this.handle = handle;
		
		Callback tempCb = new Callback (this);
		basicParameters = new AbilityParameter (tempCb); //for setup basic
		loc = new Location ();
		
		itemDelay = new ConcurrentHashMap<Integer, Long> ();
		
		pcsInsight = new ConcurrentHashMap<Integer, PcInstance> ();
		objectsInsight = new ConcurrentHashMap<Integer, Objeto> ();
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
				loc.x = rs.getInt ("LocX");
				loc.y = rs.getInt ("LocY");
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
				
				if (callback != null) {
					callback = null;
				}
				callback = new Callback (this);
				
				basicParameters = new AbilityParameter (callback);
				skillParameters = new AbilityParameter (callback);
				equipmentParameters = new AbilityParameter (callback);
				
				basicParameters.setStr (rs.getByte ("Str"));
				basicParameters.setCon (rs.getByte ("Con"));
				basicParameters.setDex (rs.getByte ("Dex"));
				basicParameters.setWis (rs.getByte ("Wis"));
				basicParameters.setCha (rs.getByte ("Cha"));
				basicParameters.setInt (rs.getByte ("Intel"));
				
				//load bonus parameters
				basicParameters.setAc (Utility.calcAcBonusFromDex (level, basicParameters.getDex()));
				basicParameters.setSp (Utility.calcSp (type, level, basicParameters.getInt ()));
				basicParameters.setMr (Utility.calcMr (type, level, basicParameters.getWis ()));
				
				basicParameters.setHpr (Utility.calcHpr (basicParameters.getCon ()));
				basicParameters.setMpr (Utility.calcMpr (basicParameters.getWis ()));
				
				hp = rs.getInt ("CurHp");
				mp = rs.getInt ("CurMp");
				basicParameters.setHp (rs.getInt ("MaxHp"));
				basicParameters.setMp (rs.getInt ("MaxMp"));
				
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
			
			//init weight
			
			handle.sendPacket (new ReportItemBag (itemBag).getPacket ());
			
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
	
	//FIXME
	/*
	public void updateWeightCache () {
		int prevW30 = weightScale30;
		
		weight = getWeight ();
		//weightScale30 = getWeightInScale30 ();
		
		if (prevW30 != weightScale30) {
			//TODO:改用s_op:62更新
			//handle.sendPacket (new ModelStatus (this).getPacketNoPadding ());
			//callback.updateWeightScale ();
		}
	}*/
	
	//回報全部道具重量
	public int getWeight () {
		int totalWeight = 0;
		
		
		Iterator<ItemInstance> weights = itemBag.values ().iterator ();
		while (weights.hasNext ()) {
			totalWeight += ((ItemInstance) weights.next ()).getWeight ();
		}
		
		this.weight = totalWeight;
		
		return totalWeight;
	}
	
	//回報可負載重量
	public int getMaxWeight () {
		int maxWeight = 1500 + (((getStr () + getCon () - 18) >> 1) * 150);
		//TODO:apply skill effect
		//TODO:負重強化
		
		//TODO:apply equip effect
		//TODO:多羅皮帶, 歐吉皮帶, 泰坦腰帶
		
		//apply doll effect
		
		return maxWeight * 1000;
	}
	
	/*
	public int getWeightInScale30 () {
		return (getWeight() * 100) / (int) (getMaxWeight() * 3.4);
	}*/
	
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
		handle.sendPacket (new ReportSpMr (getSp(), getMr()).getPacket ());
	}
	
	public void setArrow (int aUuid) {
		equipment.setArrow (itemBag.get (aUuid));
	}
	
	public void setSting (int sUuid) {
		equipment.setSting (itemBag.get (sUuid));
	}
	
	public boolean isMale () {
		return (sex == Sex.MALE);
	}
	
	public boolean isFemale () {
		return (sex == Sex.FEMALE);
	}
	
	public boolean isRoyal () {
		return (type == PlayerType.ROYAL);
	}
	
	public boolean isKnight () {
		return (type == PlayerType.KNIGHT);
	}
	
	public boolean isElf () {
		return (type == PlayerType.ELF);
	}
	
	public boolean isWizard () {
		return (type == PlayerType.WIZARD);
	}
	
	public boolean isDarkelf () {
		return (type == PlayerType.DARKELF);
	}

	public boolean isFaceTo (Location _loc) {
		return false;
	}
	
	@Override
	public byte[] getPacket () {
		PacketBuilder packet = new PacketBuilder ();
		
		packet.writeByte (ServerOpcodes.MODEL_PACK);
		packet.writeWord (loc.x);
		packet.writeWord (loc.y);
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
		int x = loc.x;
		int y = loc.y;
		
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
		byte[] movePacket = new ModelMove (uuid, loc.x, loc.y, heading).getPacket ();
		boardcastPcInsight (movePacket);
		
		//檢查是不是在傳送位址
		if (map.isTpEntrance (x, y)) {
			Location dest = map.getTpDestination (x, y);
			new Teleport (this, dest, false);
			return;
		}
		
		//更新自身位置
		loc.x = x;
		loc.y = y;
		map.setOccupied (x, y, true);
		
		moveCounter = 0x10; //check for 16s
	}

	@Override
	public int getStr () {
		return basicParameters.getStr () + skillParameters.getStr () + equipmentParameters.getStr ();
	}

	@Override
	public int getCon () {
		return basicParameters.getCon () + skillParameters.getCon () + equipmentParameters.getCon ();
	}

	@Override
	public int getDex () {
		return basicParameters.getDex () + skillParameters.getDex () + equipmentParameters.getDex ();
	}

	@Override
	public int getWis () {
		return basicParameters.getWis () + skillParameters.getWis () + equipmentParameters.getWis ();
	}

	@Override
	public int getCha () {
		return basicParameters.getCha () + skillParameters.getCha () + equipmentParameters.getCha ();
	}

	@Override
	public int getIntel () {
		return basicParameters.getInt () + skillParameters.getInt () + equipmentParameters.getInt ();
	}

	@Override
	public int getMaxHp () {
		return basicParameters.getHp () + skillParameters.getHp () + equipmentParameters.getHp ();
	}

	@Override
	public int getMaxMp () {
		return basicParameters.getMp () + skillParameters.getMp () + equipmentParameters.getMp ();
	}
	
	@Override
	public int getHpR () {
		return basicParameters.getHpr () + skillParameters.getHpr () + equipmentParameters.getHpr ();
	}

	@Override
	public int getMpR () {
		return basicParameters.getMpr () + skillParameters.getMpr () + equipmentParameters.getMpr ();
	}

	@Override
	public int getSp () {
		return basicParameters.getSp () + skillParameters.getSp () + equipmentParameters.getSp ();
	}

	@Override
	public int getMr () {
		return basicParameters.getMr () + skillParameters.getMr () + equipmentParameters.getMr ();
	}

	@Override
	public int getAc () {
		return basicParameters.getAc () + skillParameters.getAc () + equipmentParameters.getAc ();
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
			
			byte[] actionPacket = new ModelAction (ActionId.PICK_UP, uuid, heading).getPacket ();
			byte[] removeObjPacket = new RemoveModel (pick.getUuid ()).getPacket ();
			
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
				handle.sendPacket (new GameMessage (210, item.getName ()).getPacket ());
				return;
			}
			
			if (item.isUsing) {
				handle.sendPacket (new GameMessage (125, item.getName ()).getPacket ());
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
			
			drop.getLocation ().mapId = loc.mapId;
			drop.getLocation ().x = x;
			drop.getLocation ().y = y;
			
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
			handle.sendPacket (new UpdateItemStatus (i).getPacket ());
		} else {
			itemBag.put (item.uuid, item);
			
			handle.sendPacket (new ItemInsert (item).getPacket ());
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
				handle.sendPacket (new UpdateItemStatus (item).getPacket ());
			} else {
				ItemInstance newItem = new ItemInstance (itemId, UuidGenerator.next (), uuid, item.enchant, amount, item.durability, item.chargeCount, false, false);
				itemBag.put (newItem.uuid, newItem);
				
				handle.sendPacket (new ItemInsert (newItem).getPacket ());
			}
			
		} else { //not found, create new
			ItemInstance item = new ItemInstance (40308, UuidGenerator.next(), uuid, 0, amount, 0, 0, false, true);
			itemBag.put (item.uuid, item);
			
			handle.sendPacket (new ItemInsert (item).getPacket ());
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
				handle.sendPacket (new UpdateItemStatus (item).getPacket ());
				
			} else { //全數清除
				itemBag.remove (item.uuid);
				
				DatabaseCmds.deleteItem (item);
				handle.sendPacket (new ItemRemove (item).getPacket ());
			}
			
			//更新重量快取
			updateWeightCache ();
		}
	}
	
	public void deleteItem (ItemInstance i) {
		if (i.id == 40308) {
			handle.sendPacket (new GameMessage (992).getPacket ()); //金幣不可刪除
			return;
		}
		
		if (i.isUsing) {
			handle.sendPacket (new GameMessage (GameMessageId.$125).getPacket ());
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

	
	
	
	public SessionHandler getHandle () {
		return handle;
	}
	
	public void setHandle (SessionHandler handle) {
		this.handle = handle;
	}
	
	public Callback getCallback () {
		return callback;
	}
	
	public void setCallback (Callback callback) {
		this.callback = callback;
	}
	
	public LaeMap getMap () {
		return map;
	}
	
	public void setMap (LaeMap map) {
		this.map = map;
	}
	
	public int getType () {
		return type;
	}
	
	public void setType (int type) {
		this.type = type;
	}
	
	public int getSex () {
		return sex;
	}
	
	public void setSex (int sex) {
		this.sex = sex;
	}
	
	public int getSatiation () {
		return satiation;
	}
	
	public void setSatiation (int satiation) {
		this.satiation = satiation;
	}
	
	public ConcurrentHashMap<Integer, ItemInstance> getItemBag () {
		return itemBag;
	}
	
	public int getWeightScale30 () {
		return weightScale30;
	}
	
}
