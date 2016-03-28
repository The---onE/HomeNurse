package com.xmx.homenurse.Data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xmx.homenurse.Constants;

import java.util.Date;

/**
 * Created by The_onE on 2016/3/27.
 */
public class AppointmentSQLManager extends BaseSQLManager {
    private static AppointmentSQLManager instance;

    public synchronized static AppointmentSQLManager getInstance() {
        if (null == instance) {
            instance = new AppointmentSQLManager();
        }
        return instance;
    }

    private AppointmentSQLManager() {
        openDatabase();
    }

    public static long getId(Cursor c) {
        return c.getLong(0);
    }

    public static long getTime(Cursor c) {
        return c.getLong(1);
    }

    public static String getSymptom(Cursor c) {
        return c.getString(2);
    }

    public static int getStatus(Cursor c) {
        return c.getInt(3);
    }

    public static int getType(Cursor c) {
        return c.getInt(4);
    }

    public static long getAddTime(Cursor c) {
        return c.getLong(5);
    }

    protected boolean openDatabase() {
        SQLiteDatabase database = openSQLFile();
        if (database != null) {
            String createAppointmentSQL = "create table if not exists APPOINTMENT(" +
                    "ID integer not null primary key autoincrement, " +
                    "TIME integer not null default(0), " +
                    "SYMPTOM text, " +
                    "STATUS integer default(0), " +
                    "TYPE integer default(0), " +
                    "ADD_TIME integer not null default(0)" +
                    ")";
            database.execSQL(createAppointmentSQL);
            openFlag = true;
        } else {
            openFlag = false;
        }
        return openFlag;
    }

    public boolean clearAppointment() {
        if (!checkDatabase()) {
            return false;
        } else {
            clearDatabase("APPOINTMENT");
            return true;
        }
    }

    public long insertAppointment(Date date, int type, String symptom, Date add) {
        if (!checkDatabase()) {
            return -1;
        }
        ContentValues content = new ContentValues();
        content.put("TIME", date.getTime());
        content.put("TYPE", type);
        content.put("SYMPTOM", symptom);
        content.put("STATUS", Constants.STATUS_WAITING);
        content.put("ADD_TIME", add.getTime());

        return insertData("APPOINTMENT", content);
    }

    public void cancelAppointment(long id) {
        updateDate("APPOINTMENT", id, "STATUS", "" + Constants.STATUS_CANCELED);
    }

    public void deleteAppointment(long id) {
        updateDate("APPOINTMENT", id, "STATUS", "" + Constants.STATUS_DELETED);
    }

    public Cursor selectAllAppointment() {
        return selectAll("APPOINTMENT", "TIME", true);
    }
}
