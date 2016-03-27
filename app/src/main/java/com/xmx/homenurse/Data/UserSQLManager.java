package com.xmx.homenurse.Data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;

/**
 * Created by The_onE on 2016/3/26.
 */
public class UserSQLManager extends BaseSQLManager {
    private static UserSQLManager instance;

    public synchronized static UserSQLManager getInstance() {
        if (null == instance) {
            instance = new UserSQLManager();
        }
        return instance;
    }

    private UserSQLManager() {
        openDatabase();
    }

    public static int getId(Cursor c) {
        return c.getInt(0);
    }

    public static String getName(Cursor c) {
        return c.getString(1);
    }

    public static String getGender(Cursor c) {
        return c.getString(2);
    }

    public static long getBirthday(Cursor c) {
        return c.getLong(3);
    }

    public static float getHeight(Cursor c) {
        return c.getFloat(4);
    }

    public static float getWeight(Cursor c) {
        return c.getFloat(5);
    }

    public static String getIdNumber(Cursor c) {
        return c.getString(6);
    }

    public static String getPhont(Cursor c) {
        return c.getString(7);
    }

    public static String getEmail(Cursor c) {
        return c.getString(8);
    }

    public static String getAddress(Cursor c) {
        return c.getString(9);
    }

    protected boolean openDatabase() {
        SQLiteDatabase database = openSQLFile();
        if (database != null) {
            String createUserSQL = "create table if not exists USER(" +
                    "ID integer not null primary key autoincrement, " +
                    "NAME text not null, " +
                    "GENDER text not null, " +
                    "BIRTHDAY integer not null default(0), " +
                    "HEIGHT real not null default(0), " +
                    "WEIGHT real not null default(0), " +
                    "ID_NUMBER text, " +
                    "PHONE text, " +
                    "EMAIL text, " +
                    "ADDRESS text, " +
                    "TYPE integer default(0)" +
                    ")";
            database.execSQL(createUserSQL);
            openFlag = true;
        } else {
            openFlag = false;
        }
        return openFlag;
    }

    public boolean clearUser() {
        if (!checkDatabase()) {
            return false;
        } else {
            clearDatabase("USER");

            version++;
            return true;
        }
    }

    public long insertUser(String name, String gender, Date birthday, float height, float weight,
                           String idNumber, String phone, String email, String address) {
        if (!checkDatabase()) {
            return -1;
        }
        ContentValues content = new ContentValues();
        content.put("NAME", name);
        content.put("GENDER", gender);
        content.put("BIRTHDAY", birthday.getTime());
        content.put("HEIGHT", height);
        content.put("WEIGHT", weight);
        content.put("ID_NUMBER", idNumber);
        content.put("PHONE", phone);
        content.put("EMAIL", email);
        content.put("ADDRESS", address);

        long id = insertData("USER", content);

        version++;

        return id;
    }

    public Cursor getUserById(long id) {
        if (!checkDatabase()) {
            return null;
        }
        return selectById("USER", id);
    }

    public void updateUser(long id, String name, String gender, Date birthday, float height
            , float weight, String idNumber, String phone, String email, String address) {
        if (!checkDatabase()) {
            return;
        }
        updateDate("USER", id, "NAME", name, "GENDER", gender, "BIRTHDAY", "" + birthday.getTime(),
                "HEIGHT", "" + height, "WEIGHT", "" + weight, "ID_NUMBER", idNumber, "PHONE", phone,
                "EMAIL", email, "ADDRESS", address);
    }

    public Cursor getUser() {
        if (!checkDatabase()) {
            return null;
        }
        return selectLatest("USER", "ID", false);
    }
}
