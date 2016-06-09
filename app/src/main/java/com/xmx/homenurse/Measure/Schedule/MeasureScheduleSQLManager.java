package com.xmx.homenurse.Measure.Schedule;

import com.xmx.homenurse.Constants;
import com.xmx.homenurse.Tools.Data.SQL.BaseSQLEntityManager;

import java.util.List;

/**
 * Created by The_onE on 2015/10/23.
 */
public class MeasureScheduleSQLManager extends BaseSQLEntityManager<MeasureSchedule> {
    private static MeasureScheduleSQLManager instance;

    public synchronized static MeasureScheduleSQLManager getInstance() {
        if (null == instance) {
            instance = new MeasureScheduleSQLManager();
        }
        return instance;
    }
    private MeasureScheduleSQLManager() {
        tableName = "MEASURE_SCHEDULE";
        entityTemplate = new MeasureSchedule();
        openDatabase();
    }

    public MeasureSchedule getLatestSchedule() {
        if (!checkDatabase()) {
            return null;
        }
        return selectLatest("ACTUAL_TIME", true, "STATUS = " + Constants.STATUS_WAITING);
    }

    public void cancelSchedule(long id) {
        if (!checkDatabase()) {
            return;
        }
        updateDate(id, "STATUS = " + Constants.STATUS_CANCELED);
    }

    public boolean completeSchedule(long id) {
        MeasureSchedule entity = selectById(id);
        if (entity == null) {
            return false;
        }
        int type = entity.mType;
        switch (type) {
            case Constants.GENERAL_TYPE: {
                updateDate(id, "STATUS = " + Constants.STATUS_COMPLETE);
            }
            break;

            case Constants.DAILY_TYPE: {
                long planTime = entity.mPlanTime;
                long now = System.currentTimeMillis();
                long newTime = planTime;
                long delta = now - planTime;
                if (delta < -Constants.EARLIER_TIME) {
                    delta = -Constants.DAY_TIME;
                }
                newTime += (delta / Constants.DAY_TIME + 1) * Constants.DAY_TIME;

                int repeat = entity.mRepeat;
                if (repeat < 0) {
                    updateDate(id, "ACTUAL_TIME = " + newTime,
                            "PLAN_TIME = " + newTime);
                } else {
                    updateDate(id, "ACTUAL_TIME = " + newTime,
                            "PLAN_TIME = " + newTime, "REPEAT = 1");
                }
            }
            break;

            case Constants.PERIOD_TYPE: {
                long now = System.currentTimeMillis();
                long newTime = now + entity.mPeriod;

                int repeat = entity.mRepeat;
                if (repeat < 0) {
                    updateDate(id, "ACTUAL_TIME = " + newTime,
                            "PLAN_TIME = " + newTime);
                } else {
                    updateDate(id, "ACTUAL_TIME = " + newTime,
                            "PLAN_TIME = " + newTime, "REPEAT = 1");
                }
            }
            break;

            default:
                break;
        }
        return true;
    }

    public List<MeasureSchedule> selectFutureSchedule() {
        if (!checkDatabase()) {
            return null;
        }
        return selectByCondition("ACTUAL_TIME", true, "STATUS = " + Constants.STATUS_WAITING);
    }
}
