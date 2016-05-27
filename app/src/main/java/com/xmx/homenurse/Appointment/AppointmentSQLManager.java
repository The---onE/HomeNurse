package com.xmx.homenurse.Appointment;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xmx.homenurse.Constants;
import com.xmx.homenurse.Tools.Data.BaseSQLEntityManager;
import com.xmx.homenurse.Tools.Data.BaseSQLManager;

import java.util.Date;

/**
 * Created by The_onE on 2016/3/27.
 */
public class AppointmentSQLManager extends BaseSQLEntityManager<Appointment> {
    private static AppointmentSQLManager instance;

    public synchronized static AppointmentSQLManager getInstance() {
        if (null == instance) {
            instance = new AppointmentSQLManager();
        }
        return instance;
    }

    private AppointmentSQLManager() {
        tableName = "APPOINTMENT";
        entityTemplate = new Appointment();
        openDatabase();
    }

    public void resumeAppointment(long id) {
        updateDate(id, "STATUS = " + Constants.STATUS_WAITING);
    }

    public void cancelAppointment(long id) {
        updateDate(id, "STATUS = " + Constants.STATUS_CANCELED);
    }

    public void deleteAppointment(long id) {
        updateDate(id, "STATUS = " + Constants.STATUS_DELETED);
    }
}
