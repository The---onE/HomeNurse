package com.xmx.homenurse.Measure.HeartRate;

import com.xmx.homenurse.Tools.Data.BaseSQLEntityManager;

/**
 * Created by The_onE on 2016/5/23.
 */
public class HeartRateManager extends BaseSQLEntityManager<HeartRate> {
    private static HeartRateManager instance;

    public synchronized static HeartRateManager getInstance() {
        if (null == instance) {
            instance = new HeartRateManager();
        }
        return instance;
    }

    private HeartRateManager() {
        tableName = "HEART_RATE";
        entityTemplate = new HeartRate(0, 0);
        openDatabase();
    }
}
