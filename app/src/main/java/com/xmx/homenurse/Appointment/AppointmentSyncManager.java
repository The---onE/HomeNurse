package com.xmx.homenurse.Appointment;

import com.xmx.homenurse.Constants;
import com.xmx.homenurse.Tools.Data.Callback.UpdateCallback;
import com.xmx.homenurse.Tools.Data.Sync.BaseSyncEntityManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by The_onE on 2016/3/27.
 */
public class AppointmentSyncManager extends BaseSyncEntityManager<Appointment> {
    private static AppointmentSyncManager instance;

    public synchronized static AppointmentSyncManager getInstance() {
        if (null == instance) {
            instance = new AppointmentSyncManager();
        }
        return instance;
    }

    private AppointmentSyncManager() {
        setTableName("Appointment");
        setEntityTemplate(new Appointment());
        setUserField("Patient");
    }

    public void resumeAppointment(String cloudId, UpdateCallback callback) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", Constants.STATUS_WAITING);
        updateData(cloudId, map, callback);
    }

    public void cancelAppointment(String cloudId, UpdateCallback callback) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", Constants.STATUS_CANCELED);
        updateData(cloudId, map, callback);
    }

    public void deleteAppointment(String cloudId, UpdateCallback callback) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", Constants.STATUS_DELETED);
        updateData(cloudId, map, callback);
    }
}
