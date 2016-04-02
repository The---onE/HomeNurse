package com.xmx.homenurse.Tools.Data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.xmx.homenurse.Constants;

import java.io.File;

/**
 * Created by The_onE on 2016/3/22.
 */
public abstract class BaseSQLManager {
    private SQLiteDatabase database = null;
    long version = System.currentTimeMillis();
    protected boolean openFlag = false;

    public long getVersion() {
        return version;
    }

    protected SQLiteDatabase openSQLFile() {
        String d = android.os.Environment.getExternalStorageDirectory() + Constants.DATABASE_DIR;
        File dir = new File(d);
        boolean flag = dir.exists() || dir.mkdirs();

        if (flag) {
            String sqlFile = android.os.Environment.getExternalStorageDirectory() + Constants.DATABASE_FILE;
            File file = new File(sqlFile);
            database = SQLiteDatabase.openOrCreateDatabase(file, null);
            if (database == null) {
                Log.e("DatabaseError", "创建文件失败");
                return null;
            }
            version++;
            return database;
        } else {
            Log.e("DatabaseError", "创建目录失败");
            return null;
        }
    }

    protected abstract boolean openDatabase();

    protected boolean checkDatabase() {
        return openFlag || openDatabase();
    }

    protected void clearDatabase(String name) {
        String clear = "delete from " + name;
        database.execSQL(clear);
        String zero = "delete from sqlite_sequence where NAME = '" + name + "'";
        database.execSQL(zero);

        version++;
    }

    protected long insertData(String name, ContentValues content) {
        version++;
        return database.insert(name, null, content);
    }

    protected void updateDate(String name, long id, String... strings) {
        String content;
        if (strings.length > 0) {
            content = "set ";
            int i = 0;
            content += strings[i] + " = ";
            content += strings[++i] + " ";
            for (i++; i < strings.length; ++i) {
                content += ", " + strings[i] + " = ";
                content += strings[++i] + " ";
            }
        } else {
            content = "";
        }
        String update = "update " + name + " " + content + " where ID = " + id;
        database.execSQL(update);

        version++;
    }

    protected Cursor selectAll(String name) {
        return database.rawQuery("select * from " + name, null);
    }

    protected Cursor selectAll(String name, String order, boolean ascFlag) {
        String asc;
        if (ascFlag) {
            asc = "asc";
        } else {
            asc = "desc";
        }
        return database.rawQuery("select * from " + name + " order by " + order + " " + asc, null);

    }

    protected Cursor selectById(String name, long id) {
        return database.rawQuery("select * from " + name + " where ID=" + id, null);
    }

    protected Cursor selectAmount(String name, String data, String min, String max) {
        return database.rawQuery("select * from " + name + " where " + data +
                " between " + min + " and " + max, null);
    }

    protected Cursor selectLatest(String name, String order, boolean ascFlag, String... strings) {
        if (strings.length % 2 != 0) {
            return null;
        }
        String content;
        if (strings.length > 0) {
            content = "where ";
            int i = 0;
            content += strings[i] + " = ";
            content += strings[++i] + " ";
            for (i++; i < strings.length; ++i) {
                content += "and " + strings[i] + " = ";
                content += strings[++i] + " ";
            }
        } else {
            content = "";
        }
        String asc;
        if (ascFlag) {
            asc = "asc";
        } else {
            asc = "desc";
        }
        return database.rawQuery("select * from " + name + " " + content + " order by " + order + " " + asc + " limit " + 1, null);
    }

    protected Cursor selectByCondition(String name, String order, String... strings) {
        if (strings.length % 2 != 0) {
            return null;
        }
        String content;
        if (strings.length > 0) {
            content = "where ";
            int i = 0;
            content += strings[i] + " = ";
            content += strings[++i] + " ";
            for (i++; i < strings.length; ++i) {
                content += "and " + strings[i] + " = ";
                content += strings[++i] + " ";
            }
        } else {
            content = "";
        }
        return database.rawQuery("select * from " + name + " " + content + " order by " + order, null);
    }
}
