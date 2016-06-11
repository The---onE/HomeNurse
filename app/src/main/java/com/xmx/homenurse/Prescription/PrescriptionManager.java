package com.xmx.homenurse.Prescription;

import com.xmx.homenurse.Tools.Data.Callback.UpdateCallback;
import com.xmx.homenurse.Tools.Data.Sync.BaseSyncEntityManager;
import com.xmx.homenurse.User.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by The_onE on 2016/3/26.
 */
public class PrescriptionManager extends BaseSyncEntityManager<Prescription> {
    private static PrescriptionManager instance;

    public synchronized static PrescriptionManager getInstance() {
        if (null == instance) {
            instance = new PrescriptionManager();
        }
        return instance;
    }

    private PrescriptionManager() {
        setTableName("Prescription");
        setEntityTemplate(new Prescription());
        setUserField("patient");
    }
}
