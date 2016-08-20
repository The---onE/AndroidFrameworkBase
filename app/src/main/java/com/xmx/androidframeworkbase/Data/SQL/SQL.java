package com.xmx.androidframeworkbase.Data.SQL;

import android.content.ContentValues;
import android.database.Cursor;

import com.xmx.androidframeworkbase.Tools.Data.SQL.ISQLEntity;

import java.util.Date;

/**
 * Created by The_onE on 2016/3/27.
 */
public class SQL implements ISQLEntity {
    public long mId = -1;
    public String mData;
    public Date mTime;

    @Override
    public String tableFields() {
        return "ID integer not null primary key autoincrement, " +
                "Data text, " +
                "Time integer not null default(0)";

    }

    @Override
    public ContentValues getContent() {
        ContentValues content = new ContentValues();
        if (mId > 0) {
            content.put("ID", mId);
        }
        content.put("Data", mData);
        content.put("Time", mTime.getTime());
        return content;
    }

    @Override
    public ISQLEntity convertToEntity(Cursor c) {
        SQL entity = new SQL();
        entity.mId = c.getLong(0);
        entity.mData = c.getString(1);
        entity.mTime = new Date(c.getLong(2));

        return entity;
    }
}
