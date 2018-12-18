package laevatein.game.model;

import laevatein.config.*;
import laevatein.types.*;
import laevatein.server.utility.*;
import laevatein.game.skill.*;

public abstract class Objeto
{
	/* 通用唯一辨識編號 */
	protected int uuid;
	
	/* 位置敘述 */
	protected Location loc;
	
	/* 外型敘述 */
	public int originGfx;
	public int gfx = 0;
	public int actId = 0;
	
	/* 面對方向 */
	public int heading = 0;
	
	/* 光罩範圍 */
	public int light = 1;
	
	/* 移動速度 */
	public int moveSpeed = 0;

	/* 等級 */
	protected int level;
	
	/* 經驗值/地面數量 */
	protected int exp;
	
	/* 正義值 */
	protected int lawful;
	
	/* 名稱敘述 */
	protected String name;
	protected String title;
	
	/* 額外狀態 */
	public int status;
	
	/* 血盟敘述 */
	public int clanId = 0;
	public String clanName = null;
	
	/* 血條&等級強度 */
	public int hpScale = (byte) 0xFF;
	public int levelScale = (byte) 0;
	
	//model packet non-essetial data
	public boolean isDead = false;
	public volatile int hp;
	public volatile int mp;
	public int size;
	
	public boolean isPoison () {
		return (status & StatusId.STATUS_POISON) > 0;
	}
	
	public boolean isInvisible () {
		return (status & StatusId.STATUS_INVISIBLE) > 0;
	}
	
	public boolean isPc () {
		return (status & StatusId.STATUS_PC) > 0;
	}
	
	public boolean isFreeze () {
		return (status & StatusId.STATUS_FROZEN) > 0;
	}
	
	public boolean isBraved () { //勇水狀態
		return (status & StatusId.STATUS_BRAVE) > 0;
	}
	
	public boolean isElfBraved () { //精餅狀態
		return (status & StatusId.STATUS_ELF_BRAVE) > 0;
	}
	
	public boolean isFastMove () {
		return (status & StatusId.STATUS_FASTMOVE) > 0;
	}
	
	public boolean isGhost () {
		return (status & StatusId.STATUS_GHOST) > 0;
	}
	
	public int getDirection (int x, int y) {
		byte directionFace = 0;

		if (loc.x == x && loc.y == y) {
			return heading;
		} else {
			if ((x != loc.x) && (y != loc.y)) {
				directionFace |= 0x01;
			}
			
			if (((x > loc.x) && !(y < loc.y)) || ((x < loc.x) && !(y > loc.y))) {
				directionFace |= 0x02;
			}
			
			if (((x == loc.x) && (y > loc.y)) || (x < loc.x)) {
				directionFace |= 0x04;
			}
		}
		
		return directionFace & 0x0FF;
	}
	
	public int getDistanceTo (int x, int y) {
		int dx = LaeMath.abs (x - loc.x);
		int dy = LaeMath.abs (y - loc.y);
		
		return LaeMath.sqrt ((dx * dx) + (dy * dy));
	}
	
	public boolean isInsight (Location _loc) {
		if (loc.mapId != _loc.mapId) {
			return false;
		} else {
			return (getDistanceTo (_loc.x, _loc.y) < Configurations.SIGHT_RAGNE);
		}
	}
	
	//回報敘述封包
	public abstract byte[] getPacket ();
	
	//廣播封包給視距內玩家
	public abstract void boardcastPcInsight (byte[] packet);
	
	//接收攻擊動作
	public abstract void receiveAttack (NormalAttack attack); //通用接受攻擊介面
	
	//接受攻擊技能動作
	//public abstract void receiveSkillAttack (SkillAttack sAttack);
	
	//接受技能施放動作
	//public abstract void receiveSkillBuff (SkillBuff sBuff);
	
	//死亡表現
	public abstract void die ();//通用死亡表現

	
	public int getUuid () {
		return uuid;
	}
	
	public void setUuid (int uuid) {
		this.uuid = uuid;
	}
	
	public Location getLocation () {
		return loc;
	}
	
	public int getLevel () {
		return level;
	}
	
	public void setLevel (int level) {
		this.level = level;
	}
	
	public int getExp () {
		return exp;
	}
	
	public void setExp (int exp) {
		this.exp = exp;
	}
	
	public int getLawful () {
		return lawful;
	}
	
	public void setLawful (int lawful) {
		this.lawful = lawful;
	}
	
	public String getName () {
		return name;
	}
	
	public void setName (String name) {
		this.name = name;
	}
	
	public String getTitle () {
		return title;
	}
	
	public void setTitle (String title) {
		this.title = title;
	}
}
