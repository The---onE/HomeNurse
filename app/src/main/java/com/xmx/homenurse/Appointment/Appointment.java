package com.xmx.homenurse.Appointment;

import android.content.ContentValues;
import android.database.Cursor;

import com.xmx.homenurse.Constants;
import com.xmx.homenurse.Tools.Data.SQL.ISQLEntity;

import java.util.Date;

/**
 * Created by The_onE on 2016/3/27.
 */
public class Appointment implements ISQLEntity {
    public long mId = -1;
    public Date mTime;
    public int mType;
    public String mSymptom;
    public Date mAddTime;
    public int mStatus;
    public String mCloudId;

    public Appointment() {
    }

    public Appointment(String cloud, Date date, int type, String symptom, Date add) {
        mCloudId = cloud;
        mTime = date;
        mType = type;
        mSymptom = symptom;
        mAddTime = add;
    }

    public Appointment(long id, String cloud, Date date, int type, String symptom, Date add) {
        mId = id;
        mCloudId = cloud;
        mTime = date;
        mType = type;
        mSymptom = symptom;
        mAddTime = add;
    }

    public Appointment(long id, Date time, int type, String symptom, Date addTime, int status) {
        mId = id;
        mTime = time;
        mType = type;
        mSymptom = symptom;
        mAddTime = addTime;
        mStatus = status;
    }

    @Override
    public String tableFields() {
        return "ID integer not null primary key autoincrement, " +
                "TIME integer not null default(0), " +
                "SYMPTOM text, " +
                "STATUS integer default(0), " +
                "TYPE integer default(0), " +
                "ADD_TIME integer not null default(0), " +
                "CLOUD_ID text";
    }

    @Override
    public ContentValues getContent() {
        ContentValues content = new ContentValues();
        if (mId > 0) {
            content.put("ID", mId);
        }
        content.put("TIME", mTime.getTime());
        content.put("TYPE", mType);
        content.put("SYMPTOM", mSymptom);
        content.put("STATUS", Constants.STATUS_WAITING);
        content.put("ADD_TIME", mAddTime.getTime());
        content.put("CLOUD_ID", mCloudId);
        return content;
    }

    @Override
    public ISQLEntity convertToEntity(Cursor c) {
        Appointment appointment = new Appointment();
        appointment.mId = c.getLong(0);
        appointment.mTime = new Date(c.getLong(1));
        appointment.mSymptom = c.getString(2);
        appointment.mStatus = c.getInt(3);
        appointment.mType = c.getInt(4);
        appointment.mAddTime = new Date(c.getLong(5));
        appointment.mCloudId = c.getString(6);
        return appointment;
    }
}
