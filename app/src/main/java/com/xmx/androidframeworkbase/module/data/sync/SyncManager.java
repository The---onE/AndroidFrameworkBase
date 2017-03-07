package com.xmx.androidframeworkbase.module.data.sync;

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
        SyncEntityManager entityManager = SyncEntityManager.getInstance();
        if (entityManager.getSQLManager().getVersion() != syncVersion) {
            syncVersion = entityManager.getSQLManager().getVersion();

            syncList.clear();
            syncList = entityManager.getSQLManager().selectAll("Time", false);

            version++;
        }
        return version;
    }
}
