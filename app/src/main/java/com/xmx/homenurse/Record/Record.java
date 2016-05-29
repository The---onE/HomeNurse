package com.xmx.homenurse.Record;

import android.content.ContentValues;
import android.database.Cursor;

import com.avos.avoscloud.AVObject;
import com.xmx.homenurse.Constants;
import com.xmx.homenurse.Tools.Data.Cloud.ICloudEntity;
import com.xmx.homenurse.Tools.Data.ISQLEntity;

import java.util.Date;

/**
 * Created by The_onE on 2016/4/2.
 */
public class Record implements ISQLEntity, ICloudEntity {
    public long mId = -1;
    public String mTitle;
    public long mTime;
    public String mText;
    public String mSuggestion;
    public int mStatus;
    public int mType;
    public String mCloudId;

    public Record() {
    }

    public Record(long id, String title, long time, String text, String suggestion, int status, int type) {
        mId = id;
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
        content.put("TIME", mTime);
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
        record.mTime = c.getLong(2);
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
        object.put("date", new Date(mTime));
        object.put("text", mText);
        object.put("suggestion", mSuggestion);
        object.put("type", mType);
        object.put("status", mStatus);
        return object;
    }

    @Override
    public Record convertToEntity(AVObject object) {
        int id = Math.abs(object.getObjectId().hashCode());
        Record c = RecordSQLManager.getInstance().selectById(id);
        if (c == null) {
            String title = object.getString("title");
            long time = object.getDate("date").getTime();
            String text = object.getString("text");
            String suggestion = object.getString("suggestion");
            int type = object.getInt("type");
            c = new Record(id, title, time, text, suggestion, 0, type);
        }
        c.mCloudId = object.getObjectId();
        object.put("status", Constants.STATUS_COMPLETE);
        object.saveInBackground();
        return c;
    }
}
