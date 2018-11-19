package laevatein.game.template;

import laevatein.game.model.*;
import laevatein.game.skill.*;

/*
npc *****************--> 怪物的等級、血量、經驗(怪物能力都在裡面改)
npcid 編號
name 名稱
type 類型（L1Monster=怪物）
lvl 等級
hp 血量
mp 魔力
ac 防（影響你對牠的命中率）
str 力量（影響牠的物理攻擊力）
con 體質
dex 敏捷
wis 精神
intel 智力（影響牠的魔法攻擊力）
mr 魔防
exp 經驗值
lawful 正義值
size 大小類型
element 元素（不詳）
weak_water~earth 懼怕屬性（尚未實裝）
ranged 攻擊距離（1=近戰、2=兩格、3=三格…、10~13=拿弓）
agrososc 看穿隱身與否（0=看不見、1=看的見）
agrocoi 看穿變身與否（0=看不穿、1=看的穿）
tameable 迷魅與否（0=不可迷、1=可迷）
passispeed 移動速度（越低越快）
atkspeed 攻擊速度（越低越快）
agro 主動與否（0=被動、1=主動）
twocell （不詳）
area_atk 範圍攻擊（不確定代碼意義 推測：1=單體、 2=範圍內）
gfxid 使用圖檔
nameid (不詳)推測是魔法編號或是死亡後的圖像編號
candie （不詳）
undead 不死屬性（0=普通、1=不死係、2=惡魔係）
poison_atk 附加毒（0=無毒、1=扣血毒、2=禁言毒、4=麻痺毒）→例如：1=楊果裏恩、2=卡司特、4=食屍鬼。
paralysis_atk 附加麻痺效果（0=無、1=木乃伊、2=冰茅）→例如：1=梅杜莎、2=亞利安。
family 怪物所屬群組→例如：orc（妖魔）、kobold（地靈）
agrofamily 搭配family會幫打的
agrogfxid1 （不詳）
agrogfxid2 （不詳）
picupitem 撿道具（0=不會、1=會）
digestitem 消化掉道具（1000=1秒）→例如：史萊姆、布拉伯。
bravespeed 勇水狀態
hprinterval 回血時間（1000=1秒）
hpr 回血量
mprinterval 回魔時間（1000=1秒）
mpr 回魔量
teleport 瞬移到玩家身邊（0=不會、1=會）→例如：思克巴家族。
recall 將攻擊中的玩家招回怪物身邊（0=不會、1=會）
randomlevel 隨機等級（未實裝…）
recovery 復活（0=不會復活、1=復活一次）例如：多羅。
damage_reduction 傷害減免（數值=減免傷害值）
hard 硬皮怪（0=普通、1=硬皮）硬皮=會壞刀。
doppel 變身玩家（0=不變、1=會變）→例如：史萊姆狀變形怪。
*/

/*
 * 基本非玩家控制實體
 */
public class NpcTemplate //extends Objeto
{
	/* 通用唯一辨識編號 */
	public int uuid;
	
	/* 外型敘述 */
	public int gfx;
	public int actId;
	
	/* 經驗值&數量 */
	public int exp;
	
	/* 正義值 */
	public int lawful;
	
	/* 名稱敘述 */
	public String name;
	public String title;
	
	public int level;
	public int size;
	
	public String nameId;
	public String family;
	public String impl;
	public String note;

	/* 每 n ms執行一次 */
	public int moveInterval;
	public int attackInterval;
	public int majorSkillInterval;
	public int minorSkillInterval;
	
	public int digestItem;//幾ms消化道具
	
	public int _ranged;//攻擊距離
	public boolean isTamble; //可以被迷
	
	public int bowActId;
	
	//主動怪物
	public boolean agro;
	
	public boolean isUndead;
	
	public AbilityParameter basicParameters;
	
	public NpcTemplate () {
		System.out.println ("警告 不該被呼叫") ;
	}
	
	public NpcTemplate (
			int _npcId, //Template id
			String _name,
			String _nameId,
			String _note,
			String _impl, //NPC Type
			int _gfx, //shape
			int _level, int _hp, int _mp, int _ac,
			int _str, int _con, int _dex, int _wis, int _intel,
			int _mr, int _exp, int _lawful,
			String _size,
			int _weakWater, int _weakWind, int _weakFire, int _weakEarth,
			int _ranged, boolean _isTamble,
			int _moveSpeed, int _attackSpeed, int _attackSkillSpeed, int _attackSubSkillSpeed,
			int _undead, int _poisonAttack, int _paralyseAttack,
			int _agro, //主動被動設定
			int _agrososc, //看穿隱身
			int _agrocoi, //看穿變身
			String _family,
			int _argofamily, int _pickUpItem, int _digestItem, int _braveSpeed,
			int _hprInterval, int _hpr, int _mprInterval, int _mpr, 
			int _teleport,
			int _randomLevel, int _randomHp, int _randomMp, int _randomAc, int _randomExp, int _randomLawful,
			int _dmgReduction, int _isHard, int _doppel, int _isTu, int _isEarse,
			int _bowActId, int _karma, int _transformId, int _lightSize, int _amountFixed, int _attackExSpeed,
			int _attStatus, int _bowUseId, int _hasCastle, int _board) 
	{
		uuid = _npcId;
		name = _name;
		nameId = _nameId;
		family = _family;
		impl = _impl;
		note = _note;
		gfx = _gfx;
		actId = _attStatus;
		bowActId = _bowActId;
		
		basicParameters = new AbilityParameter ();
		basicParameters.str = _str; basicParameters.con = _con; basicParameters.dex = _dex;
		basicParameters.wis = _wis; basicParameters.cha =  0  ; basicParameters.intel = _intel;
		basicParameters.defWater = _weakWater; basicParameters.defWind = _weakWind;
		basicParameters.defEarth = _weakEarth; basicParameters.defFire = _weakFire;
		basicParameters.maxHp = _hp; basicParameters.maxMp = _mp;
		basicParameters.ac = _ac;
		
		level = _level;
		exp = _exp;
		if (_size.equalsIgnoreCase ("small")) {
			size = 0; //小型物件
		} else {
			size = 1; //大型物件
		}
		
		moveInterval = _moveSpeed;
		attackInterval = _attackSpeed;
		majorSkillInterval = _attackSkillSpeed;
		minorSkillInterval = _attackSubSkillSpeed;
		
		agro = (_agro > 0) ? true:false;
		
		isUndead = (_undead > 0) ? true : false;
	}
}
