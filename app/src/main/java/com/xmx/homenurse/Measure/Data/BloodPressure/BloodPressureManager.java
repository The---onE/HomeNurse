package com.xmx.homenurse.Measure.Data.BloodPressure;

import com.xmx.homenurse.Tools.Data.Sync.BaseSyncEntityManager;

import java.util.Date;
import java.util.List;

/**
 * Created by The_onE on 2016/6/10.
 */
public class BloodPressureManager extends BaseSyncEntityManager<BloodPressure> {
    private static BloodPressureManager instance;

    public synchronized static BloodPressureManager getInstance() {
        if (null == instance) {
            instance = new BloodPressureManager();
        }
        return instance;
    }

    private BloodPressureManager() {
        setTableName("BloodPressure");
        setEntityTemplate(new BloodPressure());
        setUserField("Patient");
    }

    public BloodPressure selectByDate(int year, int month, int day) {
        List<BloodPressure> list = getSQLManager().selectByCondition("Time", false,
                "Year = " + year, "Month = " + month,
                "Day = " + day);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public BloodPressure selectToday() {
        Date time = new Date();
        return selectByDate(time.getYear()+1900, time.getMonth()+1, time.getDate());
    }
}
