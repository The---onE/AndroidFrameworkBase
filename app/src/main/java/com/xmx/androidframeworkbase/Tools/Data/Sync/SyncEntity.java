package com.xmx.androidframeworkbase.Tools.Data.Sync;

import com.xmx.androidframeworkbase.Tools.Data.Cloud.ICloudEntity;
import com.xmx.androidframeworkbase.Tools.Data.SQL.ISQLEntity;

/**
 * Created by The_onE on 2016/5/29.
 */
public abstract class SyncEntity implements ICloudEntity, ISQLEntity {
    public long mId = -1;
    public String mCloudId = null;
}
