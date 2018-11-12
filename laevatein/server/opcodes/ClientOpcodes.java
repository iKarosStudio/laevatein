package laevatein.server.opcodes;

public class ClientOpcodes
{
	public static final int ITEM_USE = 0;
	public static final int CLIENT_CONFIG = 5;
	public static final int TS = 7;
	public static final int RST = 9;
	public static final int MOVE = 12;
	public static final int GLOBAL_TALK = 15;//全頻聊天
	public static final int CLIENT_VERSION = 17;
	public static final int LOGIN_PACKET = 23;
	public static final int CLIENT_BEAT = 30; //KEEPALIVE
	public static final int CREATE_CHARACTER = 31;
	public static final int CLIENT_TICK = 35;
	public static final int REMOTE_ATTACK = 41;
	public static final int ITEM_DELETE = 49;
	public static final int SKILL_BUY = 51;
	public static final int SKILL_USE = 55;
	public static final int ITEM_PICK = 59;
	public static final int REQUEST_NPC = 61;
	public static final int LIST_CHARACTER = 65;
	public static final int ATTACK = 68;
	public static final int WHO = 75;
	public static final int DELETE_CHARACTER = 87;
	public static final int ACTION_NPC = 88;
	public static final int DOOR_TOUCH = 94;
	public static final int EXIT_GAME = 98;
	public static final int TALK = 113; //一般聊天(隊, 盟, 大喊)
	public static final int RESTART = 117;
	public static final int ATTR = 112;
	public static final int ITEM_DROP = 115;
	public static final int SKILL_BUY_ORDER = 118;
	public static final int UPDATE_HEADING = 122;
	public static final int LOGIN_TO_SERVER = 125;
	public static final int LOGIN_TO_SERVER_DONE = 127;
	public static final int ACCESS_NPC = 129;
}
