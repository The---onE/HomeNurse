package com.xmx.homenurse.Data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.xmx.homenurse.Constants;

import java.io.File;
import java.util.Date;

/**
 * Created by The_onE on 2015/10/23.
 */
public class SQLManager {
    private static SQLManager instance;

    SQLiteDatabase mDatabase = null;
    long mVersion = System.currentTimeMillis();

    public synchronized static SQLManager getInstance() {
        if (null == instance) {
            instance = new SQLManager();
        }
        return instance;
    }

    private SQLManager() {
        openDatabase();
    }

    public long getVersion() {
        return mVersion;
    }

    public static int getId(Cursor c) {
        return c.getInt(0);
    }

    private boolean openDatabase() {
        String d = android.os.Environment.getExternalStorageDirectory() + Constants.DATABASE_DIR;
        File dir = new File(d);
        boolean flag = dir.exists() || dir.mkdirs();

        if (flag) {
            String sqlFile = android.os.Environment.getExternalStorageDirectory()
                    + Constants.DATABASE_FILE;
            File file = new File(sqlFile);
            mDatabase = SQLiteDatabase.openOrCreateDatabase(file, null);
            if (mDatabase == null) {
                Log.e("DatabaseError", "创建文件失败");
                return false;
            }
            // ID TITLE TEXT PHOTO TIME
            String createScheduleSQL = "create table if not exists HOME_NURSE(" +
                    "ID integer not null primary key autoincrement, " +
                    "STATUS integer default(0), " +
                    "TITLE text not null, " +
                    "TEXT text, " +
                    "TIME integer not null default(0), " +
                    "TYPE integer default(0)" +
                    ")";
            mDatabase.execSQL(createScheduleSQL);
        } else {
            Log.e("DatabaseError", "创建目录失败");
            return false;
        }
        return mDatabase != null;
    }

    private boolean checkDatabase() {
        return mDatabase != null || openDatabase();
    }

    public boolean clearDatabase(String name) {
        if (!checkDatabase()) {
            return false;
        }
        String clear = "delete from " + name;
        mDatabase.execSQL(clear);
        String zero = "delete from sqlite_sequence where NAME = '" + name + "'";
        mDatabase.execSQL(zero);

        mVersion++;
        return true;
    }

    public long insertSchedule(String title, String text, Date date, int type) {
        if (!checkDatabase()) {
            return -1;
        }
        ContentValues content = new ContentValues();
        content.put("STATUS", 0);
        content.put("TITLE", title);
        content.put("TEXT", text);
        content.put("TIME", date.getTime());
        content.put("TYPE", type);

        long id = mDatabase.insert("SCHEDULE", null, content);

        mVersion++;

        return id;
    }

    public boolean cancelSchedule(String name, int id) {
        Cursor c = selectById(name, id);
        if (c == null) {
            return false;
        }
        if (c.moveToFirst()) {
            String update = "update SCHEDULE set STATUS = 1 where ID = " + id;
            mDatabase.execSQL(update);
            mVersion++;
            return true;
        } else {
            return false;
        }
    }

    public Cursor selectById(String name, int id) {
        if (!checkDatabase()) {
            return null;
        }
        return mDatabase.rawQuery("select * from " + name + " where ID=" + id, null);
    }
}
