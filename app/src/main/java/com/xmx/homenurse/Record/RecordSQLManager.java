package com.xmx.homenurse.Record;

import com.xmx.homenurse.Constants;
import com.xmx.homenurse.Tools.Data.BaseSQLEntityManager;

import java.util.Date;
import java.util.List;

/**
 * Created by The_onE on 2015/10/23.
 */
public class RecordSQLManager extends BaseSQLEntityManager<Record> {
    private static RecordSQLManager instance;

    public synchronized static RecordSQLManager getInstance() {
        if (null == instance) {
            instance = new RecordSQLManager();
        }
        return instance;
    }

    private RecordSQLManager() {
        tableName = "RECORD";
        entityTemplate = new Record();
        openDatabase();
    }

    public List<Record> selectByDay(int year, int month, int day) {
        if (!checkDatabase()) {
            return null;
        }
        Date day1 = new Date(0);
        day1.setYear(year - 1900);
        day1.setMonth(month - 1);
        day1.setDate(day);
        return selectAmount("TIME", "" + day1.getTime(),
                "" + (day1.getTime() + Constants.DAY_TIME));
    }
}
