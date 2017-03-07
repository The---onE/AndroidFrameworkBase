package com.xmx.androidframeworkbase.common.log;

import android.content.ContentValues;
import android.database.Cursor;

import com.xmx.androidframeworkbase.common.data.sql.ISQLEntity;

import java.util.Date;

/**
 * Created by The_onE on 2016/9/4.
 */
public class OperationLog implements ISQLEntity {
    public long mId = -1;
    public String mOperation;
    public Date mTime;

    @Override
    public String tableFields() {
        return "ID integer not null primary key autoincrement, " +
                "Operation text, " +
                "Time integer not null default(0)";
    }

    @Override
    public ContentValues getContent() {
        ContentValues content = new ContentValues();
        if (mId > 0) {
            content.put("ID", mId);
        }
        content.put("Operation", mOperation);
        content.put("Time", mTime.getTime());
        return content;
    }

    @Override
    public OperationLog convertToEntity(Cursor c) {
        OperationLog entity = new OperationLog();
        entity.mId = c.getLong(0);
        entity.mOperation = c.getString(1);
        entity.mTime = new Date(c.getLong(2));

        return entity;
    }
}
