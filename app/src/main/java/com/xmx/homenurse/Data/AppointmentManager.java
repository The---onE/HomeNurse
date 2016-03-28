package com.xmx.homenurse.Data;

import android.database.Cursor;

import com.xmx.homenurse.Appointment.Appointment;
import com.xmx.homenurse.Constants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by The_onE on 2016/2/24.
 */
public class AppointmentManager {
    private static AppointmentManager instance;

    long sqlVersion = 0;
    long version = System.currentTimeMillis();
    List<Appointment> appointments = new ArrayList<>();

    public synchronized static AppointmentManager getInstance() {
        if (null == instance) {
            instance = new AppointmentManager();
        }
        return instance;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public long updateAppointments() {
        boolean changeFlag = false;

        AppointmentSQLManager sqlManager = AppointmentSQLManager.getInstance();
        if (sqlManager.getVersion() != sqlVersion) {
            sqlVersion = sqlManager.getVersion();

            Cursor c = sqlManager.selectAllAppointment();
            appointments.clear();
            if (c.moveToFirst()) {
                do {
                    int status = AppointmentSQLManager.getStatus(c);
                    if (status != Constants.STATUS_DELETED) {
                        long id = AppointmentSQLManager.getId(c);
                        long time = AppointmentSQLManager.getTime(c);
                        int type = AppointmentSQLManager.getType(c);
                        String symptom = AppointmentSQLManager.getSymptom(c);
                        long addTime = AppointmentSQLManager.getAddTime(c);

                        Appointment a = new Appointment(id, new Date(time), type, symptom,
                                new Date(addTime), status);
                        appointments.add(a);
                    }
                } while (c.moveToNext());
            }
            changeFlag = true;
        }

        if (changeFlag) {
            version++;
        }
        return version;
    }
}
