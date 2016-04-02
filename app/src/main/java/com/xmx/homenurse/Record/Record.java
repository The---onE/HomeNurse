package com.xmx.homenurse.Record;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by The_onE on 2016/4/2.
 */
public class Record {
    long mId;
    String mTitle;
    long mTime;
    String mText;
    String mSuggestion;
    int mStatus;
    int mType;

    public Record(long id, String title, long time, String text, String suggestion, int status, int type) {
        mId = id;
        mTitle = title;
        mTime = time;
        mText = text;
        mSuggestion = suggestion;
        mStatus = status;
        mType = type;
    }

    public long getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public Date getTime() {
        return new Date(mTime);
    }

    public String getText() {
        return mText;
    }

    public String getSuggestion() {
        return mSuggestion;
    }

    public int getStatus() {
        return mStatus;
    }

    public int getType() {
        return mType;
    }
}
