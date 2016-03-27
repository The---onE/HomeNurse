package com.xmx.homenurse.Data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;

/**
 * Created by The_onE on 2015/10/23.
 */
public class RecordSQLManager extends BaseSQLManager {
    private static RecordSQLManager instance;

    public synchronized static RecordSQLManager getInstance() {
        if (null == instance) {
            instance = new RecordSQLManager();
        }
        return instance;
    }

    private RecordSQLManager() {
        openDatabase();
    }

    public static long getId(Cursor c) {
        return c.getInt(0);
    }

    public static String getTitle(Cursor c) {
        return c.getString(1);
    }

    public static long getTime(Cursor c) {
        return c.getLong(2);
    }

    public static String getText(Cursor c) {
        return c.getString(3);
    }

    public static String getSuggestion(Cursor c) {
        return c.getString(4);
    }

    public static int getStatus(Cursor c) {
        return c.getInt(5);
    }

    public static int getType(Cursor c) {
        return c.getInt(6);
    }

    private boolean openDatabase() {
        SQLiteDatabase database = openSQLFile();
        if (database != null) {
            String createRecordSQL = "create table if not exists RECORD(" +
                    "ID integer not null primary key autoincrement, " +
                    "TITLE text not null, " +
                    "TIME integer not null default(0), " +
                    "TEXT text, " +
                    "SUGGESTION text, " +
                    "STATUS integer default(0), " +
                    "TYPE integer default(0)" +
                    ")";
            database.execSQL(createRecordSQL);
            openFlag = true;
        } else {
            openFlag = false;
        }
        return openFlag;
    }

    private boolean checkDatabase() {
        return openFlag || openDatabase();
    }

    public boolean clearRecord() {
        if (!checkDatabase()) {
            return false;
        } else {
            clearDatabase("RECORD");

            version++;
            return true;
        }
    }

    public long insertRecord(String title, Date date, String text, String suggestion, int type) {
        if (!checkDatabase()) {
            return -1;
        }
        ContentValues content = new ContentValues();
        content.put("TITLE", title);
        content.put("TIME", date.getTime());
        content.put("TEXT", text);
        content.put("SUGGESTION", suggestion);
        content.put("STATUS", 0);
        content.put("TYPE", type);

        long id = insertData("RECORD", content);

        version++;

        return id;
    }

    public Cursor getLatestRecord() {
        if (!checkDatabase()) {
            return null;
        }
        return selectLatest("RECORD", "TIME", false);
    }

    public Cursor selectAllRecord() {
        if (!checkDatabase()) {
            return null;
        }
        return selectAll("RECORD");
    }
}
