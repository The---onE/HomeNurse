package com.xmx.homenurse.Appointment;

import com.xmx.homenurse.Constants;

import java.util.ArrayList;
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

            appointments.clear();
            appointments = sqlManager.selectByCondition("TIME", true, "STATUS !="
                    + Constants.STATUS_DELETED);

            changeFlag = true;
        }

        if (changeFlag) {
            version++;
        }
        return version;
    }
}
