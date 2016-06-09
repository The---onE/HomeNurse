package com.xmx.homenurse.Measure.Schedule;

import com.xmx.homenurse.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by The_onE on 2016/2/24.
 */
public class MeasureScheduleManager {
    private static MeasureScheduleManager instance;

    long sqlVersion = 0;
    long version = System.currentTimeMillis();
    List<MeasureSchedule> measureSchedules = new ArrayList<>();

    public synchronized static MeasureScheduleManager getInstance() {
        if (null == instance) {
            instance = new MeasureScheduleManager();
        }
        return instance;
    }

    public List<MeasureSchedule> getMeasureSchedules() {
        return measureSchedules;
    }

    public long updateMeasureSchedules() {
        boolean changeFlag = false;

        MeasureScheduleSQLManager sqlManager = MeasureScheduleSQLManager.getInstance();
        if (sqlManager.getVersion() != sqlVersion) {
            sqlVersion = sqlManager.getVersion();

            measureSchedules = sqlManager.selectFutureSchedule();
            changeFlag = true;
        }

        long now = System.currentTimeMillis();
        for (MeasureSchedule p : measureSchedules) {
            long pt = p.mTime;
            long delta = pt - now;
            long newBefore = 0;
            String newBeforeString = "";
            if (delta / Constants.DAY_TIME > 0) {
                long day = delta / Constants.DAY_TIME;
                newBefore = day * Constants.DAY_TIME;
                newBeforeString = "还有" + day + "天";
            } else if (delta / Constants.HOUR_TIME > 0) {
                long hour = delta / Constants.HOUR_TIME;
                newBefore = hour * Constants.HOUR_TIME;
                newBeforeString = "还有" + hour + "小时";
            } else if (delta / Constants.MINUTE_TIME > 0) {
                long minute = delta / Constants.MINUTE_TIME;
                newBefore = minute * Constants.MINUTE_TIME;
                newBeforeString = "还有" + minute + "分钟";
            } else if (delta / Constants.SECOND_TIME > 0) {
                long second = delta / Constants.SECOND_TIME;
                newBefore = second * Constants.SECOND_TIME;
                newBeforeString = "还有" + second + "秒";
            }

            if (p.checkBefore(newBefore)) {
                changeFlag = true;
                p.mBefore = newBefore;
                p.mBeforeString = newBeforeString;
            }
        }

        if (changeFlag) {
            version++;
        }
        return version;
    }
}
