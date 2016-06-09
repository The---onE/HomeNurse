package com.xmx.homenurse.Measure.Schedule;

import android.content.ContentValues;
import android.database.Cursor;

import com.xmx.homenurse.Constants;
import com.xmx.homenurse.Tools.Data.SQL.ISQLEntity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by The_onE on 2016/1/30.
 */
public class MeasureSchedule implements ISQLEntity {
    public long mId = -1;
    public String mTitle;
    public String mText;
    public long mTime;
    public long mPlanTime;
    public int mType;
    public int mRepeat;
    public int mStatus = Constants.STATUS_WAITING;

    public long mBefore;
    public String mBeforeString;

    public boolean mRemindFlag;
    public boolean mDailyFlag;
    public int mPeriod;

    public MeasureSchedule() {
    }

    public MeasureSchedule(String title, String text, Date date, int type, int repeat, int period) {
        mTitle = title;
        mText = text;

        mTime = date.getTime();

        mType = type;
        mRepeat = repeat;
        mPeriod = period;
    }

    public String getTimeString() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(new Date(mTime));
    }

    public boolean checkBefore(long newBefore) {
        return mBefore != newBefore;
    }

    @Override
    public String tableFields() {
        return "ID integer not null primary key autoincrement, " +
                "TITLE text not null, " +
                "TEXT text, " +
                "ACTUAL_TIME integer not null default(0), " +
                "STATUS integer default(0), " +
                "PLAN_TIME integer not null default(0), " +
                "TYPE integer default(0), " +
                "PERIOD integer default(0), " +
                "REPEAT integer default(-1)";
    }

    @Override
    public ContentValues getContent() {
        ContentValues content = new ContentValues();
        if (mId > 0) {
            content.put("ID", mId);
        }
        content.put("TITLE", mTitle);
        content.put("TEXT", mText);
        content.put("ACTUAL_TIME", mTime);
        content.put("PLAN_TIME", mTime);
        content.put("TYPE", mType);
        content.put("PERIOD", mPeriod);
        content.put("REPEAT", mRepeat);
        content.put("STATUS", mStatus);
        return content;
    }

    @Override
    public MeasureSchedule convertToEntity(Cursor c) {
        MeasureSchedule entity = new MeasureSchedule();
        entity.mId = c.getLong(0);
        entity.mTitle = c.getString(1);
        entity.mText = c.getString(2);

        boolean remindFlag = false;
        int repeat = c.getInt(8);
        if (repeat > 0) {
            remindFlag = true;
        }
        entity.mRemindFlag = remindFlag;
        entity.mRepeat = repeat;

        boolean dailyFlag = false;
        int type = c.getInt(6);
        if (type == Constants.DAILY_TYPE) {
            dailyFlag = true;
        }
        entity.mDailyFlag = dailyFlag;
        entity.mType = type;
        entity.mTime = c.getLong(3);

        entity.mPeriod = c.getInt(7);

        entity.mStatus = c.getInt(4);
        entity.mPlanTime = c.getLong(5);
        return entity;
    }
}
