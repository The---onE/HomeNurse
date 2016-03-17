package com.xmx.homenurse;

/**
 * Created by The_onE on 2016/2/24.
 */
public class Constants {
    public static final String FILE_DIR = "/HomeNurse";
    public static final String DATABASE_DIR = FILE_DIR + "/Database";
    public static final String DATABASE_FILE = DATABASE_DIR + "/database.db";

    public static final long LONGEST_EXIT_TIME = 2000;

    public static final long DAY_TIME = 60 * 60 * 24 * 1000;
    public static final long HOUR_TIME = 60 * 60 * 1000;
    public static final long MINUTE_TIME = 60 * 1000;
    public static final long SECOND_TIME = 1000;

    public static final int[] DAYS_OF_MONTH = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    public static final String[] DAY_OF_WEEK = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};

    public static final String TOH_APP_KEY = "3cad08c00c2735add4ff8d0ff54349fb";
    public static final String DATE_APP_KEY = "8ac56625b7096982418a4852b79335f2";
}
