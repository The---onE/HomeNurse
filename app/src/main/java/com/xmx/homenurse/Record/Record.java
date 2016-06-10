package com.xmx.homenurse.Record;

import android.content.ContentValues;
import android.database.Cursor;

import com.avos.avoscloud.AVObject;
import com.xmx.homenurse.Tools.Data.Sync.ISyncEntity;

import java.util.Date;

/**
 * Created by The_onE on 2016/4/2.
 */
public class Record implements ISyncEntity {
    public long mId = -1;
    public String mTitle;
    public Date mTime;
    public String mText;
    public String mSuggestion;
    public int mStatus;
    public int mType;
    public String mCloudId;

    public Record() {
    }

    public Record(String title, Date time, String text, String suggestion, int status, int type) {
        mTitle = title;
        mTime = time;
        mText = text;
        mSuggestion = suggestion;
        mStatus = status;
        mType = type;
    }

    @Override
    public String tableFields() {
        return "ID integer not null primary key autoincrement, " +
                "TITLE text not null, " +
                "TIME integer not null default(0), " +
                "TEXT text, " +
                "SUGGESTION text, " +
                "STATUS integer default(0), " +
                "TYPE integer default(0), " +
                "CLOUD_ID text";
    }

    @Override
    public ContentValues getContent() {
        ContentValues content = new ContentValues();
        if (mId > 0) {
            content.put("ID", mId);
        }
        content.put("TITLE", mTitle);
        content.put("TIME", mTime.getTime());
        content.put("TEXT", mText);
        content.put("SUGGESTION", mSuggestion);
        content.put("STATUS", mStatus);
        content.put("TYPE", mType);
        content.put("CLOUD_ID", mCloudId);
        return content;
    }

    @Override
    public Record convertToEntity(Cursor c) {
        Record record = new Record();
        record.mId = c.getLong(0);
        record.mTitle = c.getString(1);
        record.mTime = new Date(c.getLong(2));
        record.mText = c.getString(3);
        record.mSuggestion = c.getString(4);
        record.mStatus = c.getInt(5);
        record.mType = c.getInt(6);
        record.mCloudId = c.getString(7);
        return record;
    }

    public AVObject getContent(String tableName) {
        AVObject object = new AVObject(tableName);
        object.put("title", mTitle);
        object.put("date", mTime);
        object.put("text", mText);
        object.put("suggestion", mSuggestion);
        object.put("type", mType);
        object.put("status", mStatus);
        return object;
    }

    @Override
    public Record convertToEntity(AVObject object) {
        Record c = new Record();
        c.mTitle = object.getString("title");
        c.mTime = object.getDate("date");
        c.mText = object.getString("text");
        c.mSuggestion = object.getString("suggestion");
        c.mType = object.getInt("type");
        c.mStatus = object.getInt("status");
        c.mCloudId = object.getObjectId();
        return c;
    }

    @Override
    public String getCloudId() {
        return mCloudId;
    }

    @Override
    public void setCloudId(String id) {
        mCloudId = id;
    }
}
