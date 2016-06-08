package com.xmx.homenurse.Measure.HeartRate;

import android.content.ContentValues;
import android.database.Cursor;

import com.xmx.homenurse.Tools.Data.SQL.ISQLEntity;

import java.util.Date;

/**
 * Created by The_onE on 2016/5/23.
 */
public class HeartRate implements ISQLEntity {
    Date time;
    double heartRate;

    public Date getTime() {
        return time;
    }

    public double getHeartRate() {
        return heartRate;
    }

    public HeartRate(long t, double hr) {
        time = new Date(t);
        heartRate = hr;
    }

    @Override
    public String tableFields() {
        return "ID integer not null primary key autoincrement, " +
                "TIME integer not null default(0), " +
                "HEART_RATE real";
    }

    @Override
    public ContentValues getContent() {
        ContentValues value = new ContentValues();
        value.put("TIME", time.getTime());
        value.put("HEART_RATE", heartRate);
        return value;
    }

    @Override
    public HeartRate convertToEntity(Cursor c) {
        long time = c.getLong(1);
        double heartRate = c.getDouble(2);
        return new HeartRate(time, heartRate);
    }
}
