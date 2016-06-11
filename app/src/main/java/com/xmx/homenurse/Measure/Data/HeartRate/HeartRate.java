package com.xmx.homenurse.Measure.Data.HeartRate;

import android.content.ContentValues;
import android.database.Cursor;

import com.avos.avoscloud.AVObject;
import com.xmx.homenurse.Tools.Data.Sync.ISyncEntity;

import java.util.Date;

/**
 * Created by The_onE on 2016/6/10.
 */
public class HeartRate implements ISyncEntity {
    public long mId = -1;
    public String mCloudId = null;
    public Date mTime;
    public int mYear;
    public int mMonth;
    public int mDay;
    public int mStatus;
    public double mRate;

    @Override
    public String getCloudId() {
        return mCloudId;
    }

    @Override
    public void setCloudId(String id) {
        mCloudId = id;
    }

    @Override
    public AVObject getContent(String tableName) {
        AVObject object = new AVObject(tableName);
        if (mCloudId != null) {
            object.setObjectId(mCloudId);
        }
        object.put("time", mTime);
        object.put("year", mYear);
        object.put("month", mMonth);
        object.put("day", mDay);
        object.put("status", mStatus);
        object.put("rate", mRate);
        return object;
    }

    @Override
    public HeartRate convertToEntity(AVObject object) {
        HeartRate entity = new HeartRate();
        entity.mCloudId = object.getObjectId();
        entity.mTime = object.getDate("time");
        entity.mYear = object.getInt("year");
        entity.mMonth = object.getInt("month");
        entity.mDay = object.getInt("day");
        entity.mStatus = object.getInt("status");
        entity.mRate = object.getDouble("rate");
        return entity;
    }

    @Override
    public String tableFields() {
        return "ID integer not null primary key autoincrement, " +
                "CLOUD_ID text, " +
                "Time integer not null default(0), " +
                "Year integer default(1900), " +
                "Month integer default(1), " +
                "Day integer default(1), " +
                "Status integer default(0), " +
                "Rate real";
    }

    @Override
    public ContentValues getContent() {
        ContentValues content = new ContentValues();
        if (mId > 0) {
            content.put("ID", mId);
        }
        content.put("CLOUD_ID", mCloudId);
        content.put("Time", mTime.getTime());
        content.put("Year", mYear);
        content.put("Month", mMonth);
        content.put("Day", mDay);
        content.put("Status", mStatus);
        content.put("Rate", mRate);
        return content;
    }

    @Override
    public HeartRate convertToEntity(Cursor c) {
        HeartRate entity = new HeartRate();
        entity.mId = c.getLong(0);
        entity.mCloudId = c.getString(1);
        entity.mTime = new Date(c.getLong(2));
        entity.mYear = c.getInt(3);
        entity.mMonth = c.getInt(4);
        entity.mDay = c.getInt(5);
        entity.mStatus = c.getInt(6);
        entity.mRate = c.getDouble(7);
        return entity;
    }
}
