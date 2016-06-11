package com.xmx.homenurse.Prescription;

import android.content.ContentValues;
import android.database.Cursor;

import com.avos.avoscloud.AVObject;
import com.xmx.homenurse.Tools.Data.Sync.ISyncEntity;

import java.util.Date;
import java.util.List;

/**
 * Created by The_onE on 2016/6/9.
 */
public class Prescription implements ISyncEntity {
    public long mId = -1;
    public String mCloudId = null;
    public String[] mName = {"", "", "", "", ""};
    public int[] mCount = new int[5];
    public int mHour;
    public int mMinute;

    public Prescription() {
    }

    @Override
    public String tableFields() {
        return "ID integer not null primary key autoincrement, " +
                "Name1 text, " +
                "Count1 integer not null default(0), " +
                "Name2 text, " +
                "Count2 integer not null default(0), " +
                "Name3 text, " +
                "Count3 integer not null default(0), " +
                "Name4 text, " +
                "Count4 integer not null default(0), " +
                "Name5 text, " +
                "Count5 integer not null default(0), " +
                "Hour integer not null default(0), " +
                "Minute integer not null default(0), " +
                "CLOUD_ID text";
    }

    @Override
    public ContentValues getContent() {
        ContentValues content = new ContentValues();
        if (mId > 0) {
            content.put("ID", mId);
        }
        content.put("Name1", mName[0]);
        content.put("Count1", mCount[0]);
        content.put("Name2", mName[1]);
        content.put("Count2", mCount[1]);
        content.put("Name3", mName[2]);
        content.put("Count3", mCount[2]);
        content.put("Name4", mName[3]);
        content.put("Count4", mCount[3]);
        content.put("Name5", mName[4]);
        content.put("Count5", mCount[4]);
        content.put("Hour", mHour);
        content.put("Minute", mMinute);
        content.put("CLOUD_ID", mCloudId);
        return content;
    }

    @Override
    public Prescription convertToEntity(Cursor c) {
        Prescription entity = new Prescription();
        entity.mId = c.getLong(0);
        entity.mName = new String[5];
        entity.mCount = new int[5];
        for (int i = 0; i < 5; ++i) {
            entity.mName[i] = c.getString(2 * i + 1);
            entity.mCount[i] = c.getInt(2 * i + 2);
        }
        entity.mHour = c.getInt(11);
        entity.mMinute = c.getInt(12);
        entity.mCloudId = c.getString(13);
        return entity;
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
        AVObject post = new AVObject(tableName);
        if (mCloudId != null) {
            post.setObjectId(mCloudId);
        }
        post.put("name1", mName[0]);
        post.put("count1", mCount[0]);
        post.put("name2", mName[1]);
        post.put("count2", mCount[1]);
        post.put("name3", mName[2]);
        post.put("count3", mCount[2]);
        post.put("name4", mName[3]);
        post.put("count4", mCount[3]);
        post.put("name5", mName[4]);
        post.put("count5", mCount[4]);
        post.put("hour", mHour);
        post.put("minute", mMinute);
        return post;
    }

    @Override
    public Prescription convertToEntity(AVObject object) {
        Prescription entity = new Prescription();
        entity.mCloudId = object.getObjectId();
        entity.mName[0] = object.getString("name1");
        entity.mCount[0] = object.getInt("count1");
        entity.mName[1] = object.getString("name2");
        entity.mCount[1] = object.getInt("count2");
        entity.mName[2] = object.getString("name3");
        entity.mCount[2] = object.getInt("count3");
        entity.mName[3] = object.getString("name4");
        entity.mCount[3] = object.getInt("count4");
        entity.mName[4] = object.getString("name5");
        entity.mCount[4] = object.getInt("count5");
        entity.mHour = object.getInt("hour");
        entity.mMinute = object.getInt("minute");
        return entity;
    }
}
