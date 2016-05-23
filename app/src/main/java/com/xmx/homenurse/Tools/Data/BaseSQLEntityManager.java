package com.xmx.homenurse.Tools.Data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.xmx.homenurse.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseSQLEntityManager<Entity extends ISQLEntity> {
    private SQLiteDatabase database = null;
    long version = System.currentTimeMillis();
    protected boolean openFlag = false;

    //子类构造函数中初始化！初始化后再调用openDatabase方法
    protected String tableName = null;
    protected Entity entityTemplate;

    public long getVersion() {
        return version;
    }

    private void openSQLFile() {
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
            String createSQL = "create table if not exists " + tableName + "("
                    + entityTemplate.tableFields() + ")";
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

    public void clearDatabase() {
        if (!checkDatabase()) {
            return;
        }
        String clear = "delete from " + tableName;
        database.execSQL(clear);
        String zero = "delete from sqlite_sequence where NAME = '" + tableName + "'";
        database.execSQL(zero);

        version++;
    }

    public long insertData(Entity entity) {
        if (!checkDatabase()) {
            return -1;
        }
        version++;
        return database.insert(tableName, null, entity.getContent());
    }

    public void updateDate(long id, String... strings) {
        if (!checkDatabase()) {
            return;
        }
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

    private List<Entity> convertToEntities(Cursor cursor) {
        if (!checkDatabase()) {
            return null;
        }
        List<Entity> entities = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                entities.add((Entity) entityTemplate.convertToEntity(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return entities;
    }

    private Entity convertToEntity(Cursor cursor) {
        if (!checkDatabase()) {
            return null;
        }
        if (cursor.moveToFirst()) {
            return (Entity) entityTemplate.convertToEntity(cursor);
        } else {
            return null;
        }
    }

    public List<Entity> selectAll() {
        if (!checkDatabase()) {
            return null;
        }
        Cursor cursor = database.rawQuery("select * from " + tableName, null);
        return convertToEntities(cursor);
    }

    public List<Entity> selectAll(String order, boolean ascFlag) {
        if (!checkDatabase()) {
            return null;
        }
        String asc;
        if (ascFlag) {
            asc = "asc";
        } else {
            asc = "desc";
        }
        Cursor cursor = database.rawQuery("select * from " + tableName + " order by " + order + " " + asc, null);
        return convertToEntities(cursor);
    }

    public Entity selectById(long id) {
        if (!checkDatabase()) {
            return null;
        }
        Cursor cursor = database.rawQuery("select * from " + tableName + " where ID=" + id, null);
        return convertToEntity(cursor);
    }

    public List<Entity> selectAmount(String data, String min, String max) {
        if (!checkDatabase()) {
            return null;
        }
        Cursor cursor = database.rawQuery("select * from " + tableName + " where " + data +
                " between " + min + " and " + max, null);
        return convertToEntities(cursor);
    }

    public Entity selectLatest(String order, boolean ascFlag, String... strings) {
        if (!checkDatabase()) {
            return null;
        }
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
        return convertToEntity(cursor);
    }

    public List<Entity> selectByCondition(String order, String... strings) {
        if (!checkDatabase()) {
            return null;
        }
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
        return convertToEntities(cursor);
    }
}