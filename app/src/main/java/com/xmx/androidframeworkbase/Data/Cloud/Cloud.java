package com.xmx.androidframeworkbase.Data.Cloud;

import com.avos.avoscloud.AVObject;
import com.xmx.androidframeworkbase.Tools.Data.Cloud.ICloudEntity;

import java.util.Date;

/**
 * Created by The_onE on 2016/3/27.
 */
public class Cloud implements ICloudEntity {
    public String mCloudId;
    public String mData;
    public Date mTime;

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
    public Cloud convertToEntity(AVObject object) {
        Cloud cloud = new Cloud();
        cloud.mCloudId = object.getObjectId();
        cloud.mData = object.getString("Data");
        cloud.mTime = object.getDate("Time");
        return cloud;
    }
}
