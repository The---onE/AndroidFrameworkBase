package com.xmx.androidframeworkbase;

/**
 * Created by The_onE on 2016/2/24.
 */
public class Constants {
    public static final boolean EXCEPTION_DEBUG = true;

    public static final String APP_ID = "vBb01A8F2LI63zJBqkQWiuWc-gzGzoHsz";
    public static final String APP_KEY = "tfMtJ1uRTmRre40QhxT46yVl";

    public static final String USER_INFO_TABLE = "UserInfo";
    public static final String USER_DATA_TABLE = "UserData";
    public static final String LOGIN_LOG_TABLE = "LoginLog";

    public static final String FILE_DIR = "/Framework";
    public static final String DATABASE_DIR = FILE_DIR + "/Database";
    public static final String DATABASE_FILE = DATABASE_DIR + "/database.db";

    public static final long LONGEST_EXIT_TIME = 2000;
    public static final long SPLASH_TIME = 2000;
    public static final long LONGEST_SPLASH_TIME = 6000;

    public static final int CHOOSE_ALBUM = 1;
    public static final int CHOOSE_PHOTO = 2;
    public static final int TAKE_PHOTO = 3;

    public static final long DAY_TIME = 60 * 60 * 24 * 1000;
    public static final long HOUR_TIME = 60 * 60 * 1000;
    public static final long MINUTE_TIME = 60 * 1000;
    public static final long SECOND_TIME = 1000;

    public static final int[] DAYS_OF_MONTH = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    public static final String[] DAY_OF_WEEK = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};

    public static final int STATUS_WAITING = 0;
    public static final int STATUS_CANCELED = -1;
    public static final int STATUS_DELETED = -2;
    public static final int STATUS_COMPLETE = 1;
}
