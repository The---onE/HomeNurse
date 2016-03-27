package com.xmx.homenurse.Appointment;

import java.util.Date;

/**
 * Created by The_onE on 2016/3/27.
 */
public class Appointment {
    long mId;
    Date mTime;
    int mType;
    String mSymptom;
    Date mAddTime;
    int mStatus;

    public Appointment(long id, Date time, int type, String symptom, Date addTime, int status) {
        mId = id;
        mTime = time;
        mType = type;
        mSymptom = symptom;
        mAddTime = addTime;
        mStatus = status;
    }

    public long getId() {
        return mId;
    }

    public Date getTime() {
        return mTime;
    }

    public int getType() {
        return mType;
    }

    public String getSymptom() {
        return mSymptom;
    }

    public Date getAddTime() {
        return mAddTime;
    }

    public int getStatus() {
        return mStatus;
    }
}
