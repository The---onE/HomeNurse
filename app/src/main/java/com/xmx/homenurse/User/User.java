package com.xmx.homenurse.User;

import android.content.ContentValues;
import android.database.Cursor;

import com.xmx.homenurse.Tools.Data.SQL.ISQLEntity;

import java.util.Date;

/**
 * Created by The_onE on 2016/6/9.
 */
public class User implements ISQLEntity {
    public long mId = -1;
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

    public User(long id, String name, String gender, Date birthday, double height, double weight,
                 String idNumber, String phone, String email, String address) {
        mId = id;
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
                "NAME text not null, " +
                "GENDER text not null, " +
                "BIRTHDAY integer not null default(0), " +
                "HEIGHT real not null default(0), " +
                "WEIGHT real not null default(0), " +
                "ID_NUMBER text, " +
                "PHONE text, " +
                "EMAIL text, " +
                "ADDRESS text, " +
                "TYPE integer default(0)";
    }

    @Override
    public ContentValues getContent() {
        ContentValues content = new ContentValues();
        if (mId > 0) {
            content.put("ID", mId);
        }
        content.put("NAME", mName);
        content.put("GENDER", mGender);
        content.put("BIRTHDAY", mBirthday.getTime());
        content.put("HEIGHT", mHeight);
        content.put("WEIGHT", mWeight);
        content.put("ID_NUMBER", mIdNumber);
        content.put("PHONE", mPhone);
        content.put("EMAIL", mEmail);
        content.put("ADDRESS", mAddress);
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
        return user;
    }
}
