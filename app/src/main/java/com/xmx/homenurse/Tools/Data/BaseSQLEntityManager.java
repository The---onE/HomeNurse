package com.xmx.homenurse.Tools.Data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.xmx.homenurse.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseSQLEntityManager {
    private SQLiteDatabase database = null;
    long version = System.currentTimeMillis();
    protected boolean openFlag = false;
    protected String tableName = null;

    ISQLEntity entityTemplate;

    public long getVersion() {
        return version;
    }

    protected void openSQLFile() {
        String d = android.os.Environment.getExternalStorageDirectory() + Constants.DATABASE_DIR;
        File dir = new File(d);
        boolean flag = dir.exists() || dir.mkdirs();

        if (flag) {
            String sqlFile = android.os.Environment.getExternalStorageDirectory() + Constants.DATABASE_FILE;
            File file = new File(sqlFile);
            database = SQLiteDatabase.openOrCreateDatabase(file, null);
            if (database == null) {
                Log.e("DatabaseError", "创建文件失败");
            }
            version++;
        } else {
            Log.e("DatabaseError", "创建目录失败");
        }
    }

    protected boolean openDatabase() {
        openSQLFile();
        if (database != null) {
            String createSQL = "create table if not exists "+ tableName +"("
                    + entityTemplate.createTable() + ")";
            database.execSQL(createSQL);
            openFlag = true;
        } else {
            openFlag = false;
        }
        return openFlag;
    }

    protected boolean checkDatabase() {
        return (openFlag || openDatabase()) && (tableName != null);
    }

    protected void clearDatabase() {
        String clear = "delete from " + tableName;
        database.execSQL(clear);
        String zero = "delete from sqlite_sequence where NAME = '" + tableName + "'";
        database.execSQL(zero);

        version++;
    }

    protected long insertData(ISQLEntity entity) {
        version++;
        return database.insert(tableName, null, entity.insertContent());
    }

    protected void updateDate(long id, String... strings) {
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
        String update = "update " + tableName + " " + content + " where ID = " + id;
        database.execSQL(update);

        version++;
    }

    protected List<ISQLEntity>  convertToEntity(Cursor cursor) {
        List<ISQLEntity> entities = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                entities.add(entityTemplate.convert(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return entities;
    }

    protected List<ISQLEntity> selectAll() {
        Cursor cursor =  database.rawQuery("select * from " + tableName, null);
        return convertToEntity(cursor);
    }

    protected List<ISQLEntity> selectAll(String order, boolean ascFlag) {
        String asc;
        if (ascFlag) {
            asc = "asc";
        } else {
            asc = "desc";
        }
        Cursor cursor = database.rawQuery("select * from " + tableName + " order by " + order + " " + asc, null);
        return convertToEntity(cursor);
    }

    protected ISQLEntity selectById(long id) {
        Cursor cursor = database.rawQuery("select * from " + tableName + " where ID=" + id, null);
        return entityTemplate.convert(cursor);
    }

    protected List<ISQLEntity> selectAmount(String data, String min, String max) {
        Cursor cursor = database.rawQuery("select * from " + tableName + " where " + data +
                " between " + min + " and " + max, null);
        return convertToEntity(cursor);
    }

    protected ISQLEntity selectLatest(String order, boolean ascFlag, String... strings) {
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
        Cursor cursor = database.rawQuery("select * from " + tableName + " " + content + " order by " + order + " " + asc + " limit " + 1, null);
        return entityTemplate.convert(cursor);
    }

    protected List<ISQLEntity> selectByCondition(String order, String... strings) {
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
        Cursor cursor = database.rawQuery("select * from " + tableName + " " + content + " order by " + order, null);
        return convertToEntity(cursor);
    }
}
