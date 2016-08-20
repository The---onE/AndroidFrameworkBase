package com.xmx.androidframeworkbase.Data.Sync;

import com.xmx.androidframeworkbase.Tools.Data.Sync.BaseSyncEntityManager;

/**
 * Created by The_onE on 2016/3/27.
 */
public class SyncEntityManager extends BaseSyncEntityManager<Sync> {
    private static SyncEntityManager instance;

    public synchronized static SyncEntityManager getInstance() {
        if (null == instance) {
            instance = new SyncEntityManager();
        }
        return instance;
    }

    private SyncEntityManager() {
        setTableName("SyncTest");
        setEntityTemplate(new Sync());
        setUserField("User");
    }
}
