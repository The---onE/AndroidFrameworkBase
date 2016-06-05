package com.xmx.androidframeworkbase.Sync;

import android.content.ContentValues;
import android.database.Cursor;

import com.avos.avoscloud.AVObject;
import com.xmx.androidframeworkbase.Tools.Data.Sync.SyncEntity;

import java.util.Date;

/**
 * Created by xmx on 2016/6/1.
 */
public class Sync implements SyncEntity {

    public long mId = -1;
    public String mData;
    public Date mTime;
    public String mCloudId = null;

    @Override
    public AVObject getContent(String tableName) {
        AVObject object = new AVObject(tableName);
        if (mCloudId != null) {
            object.setObjectId(mCloudId);
        }
        object.put("Data", mData);
        object.put("Time", mTime);
        return object;
    }

    @Override
    public Sync convertToEntity(AVObject object) {
        Sync entity = new Sync();
        entity.mCloudId = object.getObjectId();
        entity.mData = object.getString("Data");
        entity.mTime = object.getDate("Time");
        return entity;
    }

    @Override
    public String tableFields() {
        return "ID integer not null primary key autoincrement, " +
                "CLOUD_ID text not null, " +
                "Data text, " +
                "Time integer not null default(0)";
    }

    @Override
    public ContentValues getContent() {
        ContentValues content = new ContentValues();
        if (mId > 0) {
            content.put("ID", mId);
        }
        content.put("CLOUD_ID", mCloudId);
        content.put("Data", mData);
        content.put("Time", mTime.getTime());
        return content;
    }

    @Override
    public Sync convertToEntity(Cursor c) {
        Sync entity = new Sync();
        entity.mId = c.getLong(0);
        entity.mCloudId = c.getString(1);
        entity.mData = c.getString(2);
        entity.mTime = new Date(c.getLong(3));

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
}
