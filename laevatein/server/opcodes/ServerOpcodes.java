package laevatein.server.opcodes;

public abstract class ServerOpcodes
{
	public static final int OBJ_ACTION = 3; //指定uuid執行actid動作
	public static final int USE_MAP = 5; //開地圖
	public static final int ITEM_UPDATE_STATUS = 6; //更新uuid道具的各種屬性
	public static final int SKILL_BUY_RESULT = 17; //回應購買的技能
	public static final int UPDATE_HP = 19; //更新玩家HP/最大HP
	public static final int NEW_CHARACTER_PACK = 22; //在選角色畫面新增一個角色
	public static final int ITEM_INSERT = 24; //加入一個道具到角色背包
	public static final int MAP_ID = 27; //要求更換地圖畫面
	public static final int CHAR_DELETE = 29; //刪除腳色結果
	public static final int REMOVE_OBJECT = 34; //移除指定uuid的物件
	public static final int SERVER_VERSION = 35;
	public static final int LOGIN_WELCOME_MSG = 36;
	public static final int ITEM_UPDATE_NAME = 38;
	public static final int ITEM_IDENTIFY = 42;
	public static final int POISON = 45; //0:正常顏色 1:綠色 2:灰色
	public static final int BOOKMARK = 49;
	public static final int MOVE_NODE = 55;
	public static final int NPC_BUY_LIST = 57;
	public static final int MODEL_ACTION = 59;
	public static final int ITEM_UPDATE_AMOUNT = 61;
	public static final int PACKET_BOX = 62;
	//public static final int LOGIN_START = 62; //active spells
	public static final int FUNCTION_KEY = 62;
	public static final int SKILL_ICON = 62;
	public static final int UNKNOWN1 = 66;
	public static final int MATK_MRST = 67;
	public static final int SYS_TICK = 69;
	public static final int SYSTEM_MSG = 71;
	public static final int SERVER_MSG = 72;
	public static final int MODEL_PACK = 74;
	public static final int SKILL_HASTE = 77;
	public static final int SKILL_TABLE = 78;
	public static final int VISUAL_EFFECT = 80;
	public static final int DOOR_DETAIL = 81;
	public static final int ITEM_LIST = 82;
	public static final int UPDATE_MODEL_ACTID = 84; //更新UUID物件閒置ACTID
	public static final int UPDATE_MODEL_AC = 85;
	public static final int DISCONNECT = 88; //強制客戶端離線
	public static final int UPDATE_MODEL_STATUS = 89;
	public static final int UPDATE_EXP = 91;
	public static final int NPC_RESULT = 92;
	public static final int LOGIN_RESULT = 95;
	public static final int CHAR_CREATE_RESULT = 98;
	public static final int NPC_SELL_LIST = 99;
	public static final int NORMAL_TALKS = 103;
	public static final int UNKNOWN2 = 105;
	public static final int SET_HEADING = 105;
	public static final int CHAR_TITLE = 109;
	public static final int UPDATE_MODEL_GFX = 112; //更新UUID物件外觀為GFX
	public static final int SKILL_SHIELD = 114;
	public static final int CHAR_AMOUNT = 117;
	public static final int CHAR_LIST = 118;
	public static final int UPDATE_MP = 119;
	public static final int ITEM_REMOVE = 120;
	public static final int ITEM_UPDATE_BLESS = 121;
	public static final int WEATHER = 122;
	public static final int SKILL_BRAVE = 124;
	public static final int NPC_MSG = 127;
}
