package laevatein.game.template;

public class SkillTemplate
{
	public int skillLevel;
	public int skillId;
	public int skillNumber;
	public String name;
	public String nameId;
	public int id;
	public int gfx;
	
	public int costMp;
	public int costHp;
	public int costItemId;
	public int costItemAmount;
	
	public int delayTime;
	public int remainTime;
	
	/*
	 *  0:none(自身)   -> 沒有指定目標uuid(通常)
	 *  1:attack(攻擊) ->
	 *  2:buff(輔助)   ->
	 */
	public int target;
	
	/*
	 * 0:自己
	 * 1:PC(未確認)
	 * 2:NPC
	 * 4:血盟成員
	 * 8:團隊成員
	 * 16:寵物
	 * 32:場所
	 */
	public int targetTo;
	
	public int damageBase;
	public int damageDice;
	public int damageDiceCount;
	
	public int properbilityValue;
	public int properbilityDice;
	
	/* Element 0:none 1:Earth 2:Fire 4:Water 8:Wind */
	public int attr;
	public int actId;
	
	/*
	 * bit
	 * 0:特殊debuff
	 * 1:對目標/道具 buff
	 * 2:對目標debuff
	 * 3:x
	 * 4:回復技能
	 * 5:復活技能
	 * 6:攻擊技能
	 * 7:特殊技能(teleport, 召喚)
	 */
	public int type;
	public int lawful;
	public int range;
	public int area;
	
	public int msgIdStart;
	public int msgIdStop;
	public int msgIdFail;
	
	public boolean through;
	public boolean arrowType;
	
	public SkillTemplate (
		int _skillId,
		String _name,
		int _skillLevel,
		int _skillNumber,
		int _mpCost,
		int _hpCost,
		int _itemCostId,
		int _itemCostCount,
		int _delayTime,
		int _remainTime,
		String _target,
		int _targetTo,
		int _damageValue,
		int _damageDice,
		int _damageDiceCount,
		int _properbilityValue,
		int _properbilityDice,
		int _attr,
		int _actId,
		int _type,
		int _lawful,
		int _ranged,
		int _area,
		int _through,
		int _id,
		String _nameId,
		int _castGfx,
		int _msgIdStart,
		int _msgIdStop,
		int _msgIdFail,
		int _arrowType) {
		
		skillId = _skillId;
		name = _name;
		skillLevel = _skillLevel;
		skillNumber = _skillNumber;
		costMp = _mpCost;
		costHp = _hpCost;
		costItemId = _itemCostId;
		costItemAmount = _itemCostCount;
		delayTime = _delayTime;
		remainTime = _remainTime;
		
		//Target
		if (_target.contentEquals ("none")) {
			target = 0;
		} else if (_target.contentEquals ("attack")) {
			target = 1;
		} else if (_target.contentEquals ("buff") ) {
			target = 2;
		}
		
		targetTo = _targetTo;
		damageBase = _damageValue;
		damageDice = _damageDice;
		damageDiceCount = _damageDiceCount;
		properbilityValue = _properbilityValue;
		properbilityDice = _properbilityDice;
		attr = _attr;
		actId = _actId;
		type = _type;
		lawful = _lawful;
		range = _ranged;
		area = _area;
		//Through
		if (_through > 0) {
			through = true;
		} else {
			through = false;
		}
		id = _id;
		nameId = _nameId;
		gfx = _castGfx;
		msgIdStart = _msgIdStart;
		msgIdStop = _msgIdStop;
		msgIdFail = _msgIdFail;
		//Arrowtype
		if (_arrowType > 0) {
			arrowType = true;
		} else {
			arrowType = false;
		}
	}
}
