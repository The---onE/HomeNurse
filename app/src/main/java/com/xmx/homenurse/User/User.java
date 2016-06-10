package com.xmx.homenurse.User;

import android.content.ContentValues;
import android.database.Cursor;

import com.avos.avoscloud.AVObject;
import com.xmx.homenurse.Tools.Data.Cloud.ICloudEntity;
import com.xmx.homenurse.Tools.Data.SQL.ISQLEntity;
import com.xmx.homenurse.Tools.Data.Sync.ISyncEntity;

import java.util.Date;

/**
 * Created by The_onE on 2016/6/9.
 */
public class User implements ISyncEntity {
    public long mId = -1;
    public String mCloudId = null;
    public String mName;
    public String mGender;
    public Date mBirthday;
    public double mHeight;
    public double mWeight;
    public String mIdNumber;
    public String mPhone;
    public String mEmail;
    public String mAddress;

    public User() {
    }

    public User(String name, String gender, Date birthday, double height, double weight,
                String idNumber, String phone, String email, String address) {
        mName = name;
        mGender = gender;
        mBirthday = birthday;
        mHeight = height;
        mWeight = weight;
        mIdNumber = idNumber;
        mPhone = phone;
        mEmail = email;
        mAddress = address;
    }

    @Override
    public String tableFields() {
        return "ID integer not null primary key autoincrement, " +
                "name text not null, " +
                "gender text not null, " +
                "birthday integer not null default(0), " +
                "height real not null default(0), " +
                "weight real not null default(0), " +
                "idNumber text, " +
                "phone text, " +
                "email text, " +
                "address text, " +
                "type integer default(0), " +
                "CLOUD_ID text";
    }

    @Override
    public ContentValues getContent() {
        ContentValues content = new ContentValues();
        if (mId > 0) {
            content.put("ID", mId);
        }
        content.put("name", mName);
        content.put("gender", mGender);
        content.put("birthday", mBirthday.getTime());
        content.put("height", mHeight);
        content.put("weight", mWeight);
        content.put("idNumber", mIdNumber);
        content.put("phone", mPhone);
        content.put("email", mEmail);
        content.put("address", mAddress);
        content.put("CLOUD_ID", mCloudId);
        return content;
    }

    @Override
    public User convertToEntity(Cursor c) {
        User user = new User();
        user.mId = c.getInt(0);
        user.mName = c.getString(1);
        user.mGender = c.getString(2);
        user.mBirthday = new Date(c.getLong(3));
        user.mHeight = c.getDouble(4);
        user.mWeight = c.getDouble(5);
        user.mIdNumber = c.getString(6);
        user.mPhone = c.getString(7);
        user.mEmail = c.getString(8);
        user.mAddress = c.getString(9);
        user.mCloudId = c.getString(11);
        return user;
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
        post.put("name", mName);
        post.put("gender", mGender);
        post.put("birthday", mBirthday);
        post.put("height", mHeight);
        post.put("weight", mWeight);
        post.put("idNumber", mIdNumber);
        post.put("phone", mPhone);
        post.put("email", mEmail);
        post.put("address", mAddress);
        return post;
    }

    @Override
    public User convertToEntity(AVObject object) {
        User entity = new User();
        entity.mCloudId = object.getObjectId();
        entity.mName = object.getString("name");
        entity.mGender = object.getString("gender");
        entity.mBirthday = object.getDate("birthday");
        entity.mHeight = object.getDouble("height");
        entity.mWeight = object.getDouble("weight");
        entity.mIdNumber = object.getString("idNumber");
        entity.mPhone = object.getString("phone");
        entity.mEmail = object.getString("email");
        entity.mAddress = object.getString("address");
        return entity;
    }
}
