package laevatein.game.skill;

public class SkillId
{
	/* 
	 * 技能ID
	 * pc.hasSkill (id)判斷
	 */
	//LEVEL-1
	public static final int LESSER_HEAL = 1;
	public static final int LIGHT = 2;
	public static final int SHIELD = 3;
	public static final int ENERGY_BOLT = 4;
	public static final int TELEPORT = 5;
	public static final int ICE_DAGGER = 6;
	public static final int WIND_SHURIKEN = 7;
	public static final int HOLY_WEAPON = 8;
	
	//LEVEL-2
	public static final int CURE_POISON = 9;
	public static final int CHILL_TOUCH = 10;
	public static final int CURSE_POISON = 11;
	public static final int ENCHANT_WEAPON = 12;
	public static final int DETECTION = 13;
	public static final int DECREASE_WEIGHT = 14;
	public static final int FIRE_ARROW = 15;
	public static final int STALAC = 16;
	
	//LEVEL-3
	public static final int LIGHTNING = 17;
	public static final int TURN_UNDEAD = 18;
	public static final int HEAL = 19;
	public static final int CURSE_BLIND = 20;
	public static final int BLESSED_ARMOR = 21;
	public static final int FROZEN_CLOUD = 22;
	public static final int REVEAL_ELEMENTAL = 23;
	public static final int NOTUSED1 = 24;//NOT USED
	
	//LEVEL-4
	public static final int FIREBALL = 25;
	public static final int PHYSICAL_ENCHANT_DEX = 26;
	public static final int WEAPON_BREAK = 27;
	public static final int VAMPIRIC_TOUCH = 28;
	public static final int SLOW = 29;
	public static final int EARTH_JAIL = 30;
	public static final int COUNTER_MAGIC = 31;
	public static final int MEDITATION = 32;
	
	//LEVEL-5
	public static final int CURSE_PARALYZE = 33;
	public static final int CALL_LIGHTNING = 34;
	public static final int GREATER_HEAL = 35;
	public static final int TAME_MONSTER = 36;
	public static final int REMOVE_CURSE = 37;
	public static final int CONE_OF_COLD = 38;
	public static final int MANA_DRAIN = 39;
	public static final int DARKNESS = 40;
	
	//LEVEL-6
	public static final int CREATE_ZOMBIE = 41;
	public static final int PHYSICAL_ENCHANT_STR = 42;
	public static final int HASTE = 43;
	public static final int CANCEL_MAGIC = 44;
	public static final int ERUPTION = 45; //地烈
	public static final int SUNBURST = 46;
	public static final int WEAKNESS = 47;
	public static final int BLESS_WEAPON = 48;
	
	//LEVEL-7
	public static final int HEAL_PLEDGE = 49;
	public static final int FREEZE = 50;
	public static final int SUMMON_MONSTER = 51;
	public static final int HOLY_WALK = 52;
	public static final int TORNADO = 53;
	public static final int GREATER_HASTE = 54;
	public static final int BERSERKERS = 55;
	public static final int DESESASE = 56;
	
	//LEVEL-8
	public static final int FULL_HEAL = 57;
	public static final int FIREWALL = 58;
	public static final int BLIZZARD = 59;
	public static final int INVISIBILITY = 60;
	public static final int RESURECTION = 61;
	public static final int EARTHQUAKE = 62;
	public static final int LIFE_STREAM = 63;
	public static final int SILENCE = 64;
	
	//LEVEL-9
	public static final int LIGHTING_STORM = 65;
	public static final int FOR_OF_SLEEPING = 66;
	public static final int SHAPE_CHANGE = 67;
	public static final int IMMUNE_TO_HARM = 68;
	public static final int MASS_TELEPORT = 69;
	public static final int FIRE_STORM = 70;
	public static final int DECAY_POTION = 71;
	public static final int COUNTER_DETECTION = 72;

	//LEVEL-10
	public static final int CREATE_MAGICAL_WEAPON = 73;
	public static final int METEOR_STRIKE = 74;
	public static final int GREATER_RESURRECTION = 75;
	public static final int MASS_SLOW = 76;
	public static final int DESTROY = 77;
	public static final int ABSOLUTE_BARRIER = 78;
	public static final int ADVANCE_SPIRIT = 79;
	public static final int ICE_SPIKE = 80;
	
	/* 狀態編號 */
	public static final int STATUS_BRAVE = 1000; //勇敢藥水效果
	public static final int STATUS_HASTE = 1001; //加速藥水效果
	public static final int STATUS_BLUE_POTION = 1002; //藍水
	public static final int STATUS_BLESS_OF_EVA = 1003; //伊娃的祝福
	public static final int STATUS_WISDOM_POTION = 1004; //慎重藥水
	public static final int STATUS_CHAT_PROHIBITED = 1005; //沉默狀態
	public static final int STATUS_POISON = 1006;
	public static final int STATUS_POISON_SILENCE = 1007;
	public static final int STATUS_POISON_PARALYZING = 1008;
	public static final int STATUS_POISON_PARALYZED = 1009;
	public static final int STATUS_CURSE_PARALYZING = 1010;
	public static final int STATUS_CURSE_PARALYZED = 1011;
	public static final int STATUS_FLOATING_EYE = 1012;
	
	public static final int SKILL_TARGET_NONE = 0;
	public static final int SKILL_TARGET_ATTACK = 1;
	public static final int SKILL_TARGET_BUFF = 2;
}
