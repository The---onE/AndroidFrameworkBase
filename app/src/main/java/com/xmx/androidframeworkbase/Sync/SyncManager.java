package com.xmx.androidframeworkbase.Sync;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by The_onE on 2016/2/24.
 */
public class SyncManager {
    private static SyncManager instance;

    long syncVersion = 0;
    long version = System.currentTimeMillis();
    List<Sync> syncList = new ArrayList<>();

    public synchronized static SyncManager getInstance() {
        if (null == instance) {
            instance = new SyncManager();
        }
        return instance;
    }

    public List<Sync> getData() {
        return syncList;
    }

    public long updateData() {
        SyncSQLManager sqlManager = SyncSQLManager.getInstance();
        if (sqlManager.getVersion() != syncVersion) {
            syncVersion = sqlManager.getVersion();

            syncList.clear();
            syncList = sqlManager.selectAll("Time", false);

            version++;
        }
        return version;
    }
}
