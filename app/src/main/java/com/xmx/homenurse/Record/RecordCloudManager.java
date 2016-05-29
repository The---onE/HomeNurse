package com.xmx.homenurse.Record;

import com.xmx.homenurse.Tools.Data.Cloud.BaseCloudEntityManager;

/**
 * Created by The_onE on 2016/5/29.
 */
public class RecordCloudManager extends BaseCloudEntityManager<Record> {
    private static RecordCloudManager instance;

    public synchronized static RecordCloudManager getInstance() {
        if (null == instance) {
            instance = new RecordCloudManager();
        }
        return instance;
    }

    private RecordCloudManager() {
        tableName = "Prescription";
        entityTemplate = new Record();
        userField = "patient";
    }
}
