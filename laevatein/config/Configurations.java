package laevatein.config;

public class Configurations
{
	public static final int MAJOR_VERSION = 0;
	public static final int MINOR_VERSION = 0;
	public static final String AUTHOR = "R.Lin";
	
	
	/* 系統連線設定 */
	public static int SERVER_PORT = 2000;
	public static int MANAGE_PORT = 2001;
	public static int MAX_PLAYER = 200;
	
	/* 系統設定 */
	public static boolean USE_BLOCKCHAIN = false;
	public static boolean USE_GUI = false;
	public static int SIGHT_RAGNE = 10;
	public static boolean CASINO = false;
	public static int MONSTER_GENERATOR_UPDATE_RATE = 1000;
	public static int MONSTER_AI_UPDATE_RATE = 1000;
	public static int DEFAULT_MOVEMENT_RANGE = 0;
	public static int PARALLELISM_THRESHOLD = 1;
	
	public static boolean DISPLAY_CHAT = true;
	public static boolean LOG_CHAT = false;
	public static boolean ALLOW_DROP_ITEM = true;
	
	/* 遊戲倍率設定 */
	public static int RateExp = 1;
	public static int RatePetExp = 1;
	public static int RateDropItem = 1;
	public static int RateDropGold = 1;
	public static int RateLawful = 1;
	
	public static int RateMaxWeight = 1;
	public static int RateMaxPetWeight = 1;
	
	/* 最高血量設定 */
	public static int MAX_HP_ROYAL = 1000;
	public static int MAX_MP_ROYAL = 1000;
	public static int MAX_HP_KNIGHT = 1000;
	public static int MAX_MP_KNIGHT = 1000;
	public static int MAX_HP_ELF = 1000;
	public static int MAX_MP_ELF = 1000;
	public static int MAX_HP_MAGE = 1000;
	public static int MAX_MP_MAGE = 1000;
	public static int MAX_HP_DARKELF = 1000;
	public static int MAX_MP_DARKELF = 1000;
}
