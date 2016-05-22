package com.xmx.homenurse.Tools.Data;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by The_onE on 2016/3/22.
 */
public interface ISQLEntity {
    String createTable();
    ContentValues insertContent();
    ISQLEntity convert(Cursor c);
}