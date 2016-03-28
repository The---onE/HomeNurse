package com.xmx.homenurse.Data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xmx.homenurse.Constants;

import java.util.Date;

/**
 * Created by The_onE on 2015/10/23.
 */
public class MeasureSQLManager extends BaseSQLManager {
    private static MeasureSQLManager instance;

    public synchronized static MeasureSQLManager getInstance() {
        if (null == instance) {
            instance = new MeasureSQLManager();
        }
        return instance;
    }

    private MeasureSQLManager() {
        openDatabase();
    }

    public static long getId(Cursor c) {
        return c.getLong(0);
    }

    public static String getTitle(Cursor c) {
        return c.getString(1);
    }

    public static String getText(Cursor c) {
        return c.getString(2);
    }

    public static long getActualTime(Cursor c) {
        return c.getLong(3);
    }

    public static long getPlanTime(Cursor c) {
        return c.getLong(5);
    }

    public static int getType(Cursor c) {
        return c.getInt(6);
    }

    public static int getPeriod(Cursor c) {
        return c.getInt(7);
    }

    public static int getRepeat(Cursor c) {
        return c.getInt(8);
    }

    protected boolean openDatabase() {
        SQLiteDatabase database = openSQLFile();
        if (database != null) {
            String createScheduleSQL = "create table if not exists MEASURE_SCHEDULE(" +
                    "ID integer not null primary key autoincrement, " +
                    "TITLE text not null, " +
                    "TEXT text, " +
                    "ACTUAL_TIME integer not null default(0), " +
                    "STATUS integer default(0), " +
                    "PLAN_TIME integer not null default(0), " +
                    "TYPE integer default(0), " +
                    "PERIOD integer default(0), " +
                    "REPEAT integer default(-1)" +
                    ")";
            database.execSQL(createScheduleSQL);
            openFlag = true;
        } else {
            openFlag = false;
        }
        return openFlag;
    }

    public boolean clearSchedule() {
        if (!checkDatabase()) {
            return false;
        } else {
            clearDatabase("MEASURE_SCHEDULE");
            return true;
        }
    }

    public long insertSchedule(String title, String text, Date date, int type, int repeat, int period) {
        if (!checkDatabase()) {
            return -1;
        }
        ContentValues content = new ContentValues();
        content.put("TITLE", title);
        content.put("TEXT", text);
        content.put("ACTUAL_TIME", date.getTime());
        content.put("PLAN_TIME", date.getTime());
        content.put("TYPE", type);
        content.put("PERIOD", period);
        content.put("REPEAT", repeat);
        content.put("STATUS", 0);

        return insertData("MEASURE_SCHEDULE", content);
    }

    public long insertSchedule(long id, String title, String text, long actualTime,
                               long planTime, int type, int repeat, int status, int period) {
        if (!checkDatabase()) {
            return -1;
        }
        ContentValues content = new ContentValues();
        content.put("ID", id);
        content.put("TITLE", title);
        content.put("TEXT", text);
        content.put("ACTUAL_TIME", actualTime);
        content.put("PLAN_TIME", planTime);
        content.put("TYPE", type);
        content.put("PERIOD", period);
        content.put("REPEAT", repeat);
        content.put("STATUS", status);

        insertData("MEASURE_SCHEDULE", content);

        return id;
    }

    public Cursor getLatestSchedule() {
        if (!checkDatabase()) {
            return null;
        }
        return selectLatest("MEASURE_SCHEDULE", "ACTUAL_TIME", true, "STATUS", "0");
    }

    public void cancelSchedule(long id) {
        if (!checkDatabase()) {
            return;
        }
        updateDate("MEASURE_SCHEDULE", id, "STATUS", "1");
    }

    public boolean completeSchedule(long id) {
        Cursor c = selectById(id);
        if (c == null) {
            return false;
        }
        if (c.moveToFirst()) {
            int type = getType(c);
            switch (type) {
                case Constants.GENERAL_TYPE: {
                    updateDate("MEASURE_SCHEDULE", id, "STATUS", "1");
                }
                break;

                case Constants.DAILY_TYPE: {
                    long planTime = getPlanTime(c);
                    long now = System.currentTimeMillis();
                    long newTime = planTime;
                    long delta = now - planTime;
                    if (delta < -Constants.EARLIER_TIME) {
                        delta = -Constants.DAY_TIME;
                    }
                    newTime += (delta / Constants.DAY_TIME + 1) * Constants.DAY_TIME;

                    int repeat = getRepeat(c);
                    if (repeat < 0) {
                        updateDate("MEASURE_SCHEDULE", id, "ACTUAL_TIME", "" + newTime,
                                "PLAN_TIME", "" + newTime);
                    } else {
                        updateDate("MEASURE_SCHEDULE", id, "ACTUAL_TIME", "" + newTime,
                                "PLAN_TIME", "" + newTime, "REPEAT", "1");
                    }
                }
                break;

                case Constants.PERIOD_TYPE: {
                    long now = System.currentTimeMillis();
                    long newTime = now + getPeriod(c);

                    int repeat = getRepeat(c);
                    if (repeat < 0) {
                        updateDate("MEASURE_SCHEDULE", id, "ACTUAL_TIME", "" + newTime,
                                "PLAN_TIME", "" + newTime);
                    } else {
                        updateDate("MEASURE_SCHEDULE", id, "ACTUAL_TIME", "" + newTime,
                                "PLAN_TIME", "" + newTime, "REPEAT", "1");
                    }
                }
                break;

                default:
                    break;
            }
            return true;
        } else {
            return false;
        }
    }

    public Cursor selectFutureSchedule() {
        if (!checkDatabase()) {
            return null;
        }
        return selectByCondition("MEASURE_SCHEDULE", "ACTUAL_TIME", "STATUS", "0");
    }

    public Cursor selectById(long id) {
        if (!checkDatabase()) {
            return null;
        }
        return selectById("MEASURE_SCHEDULE", id);
    }
}
