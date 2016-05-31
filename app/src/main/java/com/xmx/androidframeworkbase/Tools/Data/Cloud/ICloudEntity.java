package com.xmx.androidframeworkbase.Tools.Data.Cloud;

import com.avos.avoscloud.AVObject;

/**
 * Created by The_onE on 2016/5/29.
 */
public interface ICloudEntity {
    AVObject getContent(String tableName);

    ICloudEntity convertToEntity(AVObject object);
}
