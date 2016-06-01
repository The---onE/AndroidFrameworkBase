package com.xmx.androidframeworkbase.Sync;

import com.xmx.androidframeworkbase.Tools.Data.Cloud.BaseCloudEntityManager;

/**
 * Created by The_onE on 2016/3/27.
 */
public class SyncCloudManager extends BaseCloudEntityManager<Sync> {
    private static SyncCloudManager instance;

    public synchronized static SyncCloudManager getInstance() {
        if (null == instance) {
            instance = new SyncCloudManager();
        }
        return instance;
    }

    private SyncCloudManager() {
        tableName = "SyncTest";
        entityTemplate = new Sync();
        userField = "User";
    }
}
