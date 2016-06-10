package com.xmx.homenurse.Appointment;

import android.content.ContentValues;
import android.database.Cursor;

import com.avos.avoscloud.AVObject;
import com.xmx.homenurse.Constants;
import com.xmx.homenurse.Tools.Data.Cloud.ICloudEntity;
import com.xmx.homenurse.Tools.Data.SQL.ISQLEntity;
import com.xmx.homenurse.Tools.Data.Sync.ISyncEntity;

import java.util.Date;

/**
 * Created by The_onE on 2016/3/27.
 */
public class Appointment implements ISyncEntity {
    public long mId = -1;
    public Date mTime;
    public int mType;
    public String mSymptom;
    public Date mAddTime;
    public int mStatus = Constants.STATUS_WAITING;
    public String mPatientName;
    public String mCloudId;

    public Appointment() {
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
        content.put("STATUS", mStatus);
        content.put("ADD_TIME", mAddTime.getTime());
        content.put("CLOUD_ID", mCloudId);
        return content;
    }

    @Override
    public Appointment convertToEntity(Cursor c) {
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
        object.put("type", mType);
        object.put("symptom", mSymptom);
        object.put("status", mStatus);
        object.put("addTime", mAddTime);
        object.put("patientName", mPatientName);
        return object;
    }

    @Override
    public Appointment convertToEntity(AVObject object) {
        Appointment appointment = new Appointment();
        appointment.mCloudId = object.getObjectId();
        appointment.mTime = object.getDate("time");
        appointment.mSymptom = object.getString("symptom");
        appointment.mAddTime = object.getDate("addTime");
        appointment.mType = object.getInt("type");
        appointment.mStatus = object.getInt("status");
        return appointment;
    }
}
