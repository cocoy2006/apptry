package molab.main.java.util;

public class Status {
	
	/**
	 * common states*/
	public static final int SUCCESS = 1;
	public static final int ERROR = -1;
	public static final int ERROR_SESSION_ATTR_LOST = 97;
	public static final int ERROR_SESSION_LOST = 98;
	public static final int ERROR_SYSTEM = 99;
	
	/**
	 * states used in DISPATCHER and DEPLOYMENT */
	public static final int START = 0;
	public static final int RUNNING = 1;
	public static final int END = 2;
	public static final int EXCEPTION = 9;
	
	/**
	 * states used in DEPLOYMENT */
	public static final int INSTALL_FAILED_ALREADY_EXISTS = 10; //搴旂敤绋嬪簭宸茬粡瀛樺湪
	public static final int INSTALL_FAILED_MISSING_SHARED_LIBRARY = 11; //缂哄皯鍏变韩閾炬帴搴�
	public static final int INSTALL_FAILED_INSUFFICIENT_STORAGE = 12; //鐩爣鐩綍绌洪棿涓嶈冻
	public static final int INSTALL_FAILED_OLDER_SDK = 13; //SDK鐗堟湰杩囦綆
	public static final int INSTALL_FAILED_INVALID_APK = 14; //APK閿欒
	public static final int INSTALL_PARSE_FAILED_INCONSISTENT_CERTIFICATES = 15; //闇�鎵嬪姩鍗歌浇鍘熺▼搴忥紝-r瑙ｅ喅涓嶄簡
	public static final int LOAD_FAILED_PROCESS_NOT_FOUND = 20; //杩涚▼鏈壘鍒�
	public static final int SCREENSHOT_FAILED = 30; //鎴睆寮傚父
	public static final int LOGIN_FAILED = 40; //鐧诲綍寮傚父
	public static final int UNINSTALL_FAILED = 90; //鍗歌浇寮傚父
	
	/**
	 * emulator states*/
	public static final int IDLE = 1;
	public static final int BUSY = 2;
	public static final int OTHER = 9;
	
	/**
	 * for login page*/
	public static final int USER_UNACTIVED = 0;
	public static final int USER_NORMAL = 1;
	public static final int USER_NOT_EXIST = 2;
	public static final int PASSWORD_NOT_MATCH = 3;
	
	/**
	 * for register page*/
	public static final int USER_EXIST = 2;
	
	/**
	 * for password reset page*/
	public static final int USERNAME_ERROR = 2;
	public static final int EMAIL_ERROR = 3;
	
	public static final int TRUE = 1;
	public static final int FALSE = 0;
	
	public static final int DEFAULT_LEFT_CLICKS = 10;
	public static final long DEFAULT_EXPIRATION = 32489436216541L;
	
	/**
	 * for mer page
	 * */
	public static final int MER_NORMAL = 1; // default
	public static final int MER_REMOVE = 0;
	
	/**
	 * for order page
	 * */
	public static final int ORDER_WAITING = 0;
	public static final int ORDER_SUCCESS = 1;
	public static final int ORDER_FAILURE = 2;
	public static final int ORDER_CANCEL = 3;
	
}