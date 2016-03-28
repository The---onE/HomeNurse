package com.xmx.homenurse.Tools.Data;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;

import com.xmx.homenurse.User.UserSQLManager;

/**
 * Created by The_onE on 2016/2/21.
 */
public class DataManager {
    private static DataManager instance;

    Context mContext;
    SharedPreferences mData;

    public synchronized static DataManager getInstance() {
        if (null == instance) {
            instance = new DataManager();
        }
        return instance;
    }

    public void setContext(Context context) {
        mContext = context;
        mData = context.getSharedPreferences("DATA", Context.MODE_PRIVATE);
    }

    public long getVersion() {
        return mData.getLong("version", -1);
    }

    public void setVersion(long value) {
        SharedPreferences.Editor editor = mData.edit();
        editor.putLong("version", value);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return mData.getBoolean("logged_in", false);
    }

    public void login() {
        SharedPreferences.Editor editor = mData.edit();
        editor.putBoolean("logged_in", true);
        Cursor c = UserSQLManager.getInstance().getUser();
        if (c.moveToFirst()) {
            editor.putLong("id", UserSQLManager.getId(c));
        }
        editor.apply();
    }

    public void logout() {
        SharedPreferences.Editor editor = mData.edit();
        editor.putBoolean("logged_in", false);
        editor.putLong("id", -1);
        editor.apply();
    }

    public void setId(long id) {
        SharedPreferences.Editor editor = mData.edit();
        editor.putLong("id", id);
        editor.apply();
    }

    public long getId() {
        return mData.getLong("id", -1);
    }
}
