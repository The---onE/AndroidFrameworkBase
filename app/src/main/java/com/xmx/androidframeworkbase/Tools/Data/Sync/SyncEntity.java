package com.xmx.androidframeworkbase.Tools.Data.Sync;

import com.xmx.androidframeworkbase.Tools.Data.Cloud.ICloudEntity;
import com.xmx.androidframeworkbase.Tools.Data.SQL.ISQLEntity;

/**
 * Created by The_onE on 2016/5/29.
 */
public interface SyncEntity extends ICloudEntity, ISQLEntity {
    public String getCloudId();
    public void setCloudId(String id);
}
