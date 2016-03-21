package com.xmx.homenurse.Data;

import android.database.Cursor;

import com.xmx.homenurse.Constants;
import com.xmx.homenurse.Measure.MeasureSchedule;

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

        MeasureSQLManager sqlManager = MeasureSQLManager.getInstance();
        if (sqlManager.getVersion() != sqlVersion) {
            sqlVersion = sqlManager.getVersion();

            Cursor c = sqlManager.selectFutureSchedule();
            measureSchedules.clear();
            if (c.moveToFirst()) {
                do {
                    int id = MeasureSQLManager.getId(c);
                    String title = MeasureSQLManager.getTitle(c);
                    String text = MeasureSQLManager.getText(c);

                    boolean remindFlag = false;
                    boolean dailyFlag = false;

                    int repeat = MeasureSQLManager.getRepeat(c);
                    if (repeat > 0) {
                        remindFlag = true;
                    }

                    int type = MeasureSQLManager.getType(c);
                    if (type == Constants.DAILY_TYPE) {
                        dailyFlag = true;
                    }
                    long time = MeasureSQLManager.getActualTime(c);

                    int period = MeasureSQLManager.getPeriod(c);

                    MeasureSchedule p = new MeasureSchedule(id, title, text,  time, remindFlag, dailyFlag, period);
                    measureSchedules.add(p);
                } while (c.moveToNext());
            }
            changeFlag = true;
        }

        long now = System.currentTimeMillis();
        for (MeasureSchedule p : measureSchedules) {
            long pt = p.getTime();
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
                p.setBefore(newBefore);
                p.setBeforeString(newBeforeString);
            }
        }

        if (changeFlag) {
            version++;
        }
        return version;
    }
}
