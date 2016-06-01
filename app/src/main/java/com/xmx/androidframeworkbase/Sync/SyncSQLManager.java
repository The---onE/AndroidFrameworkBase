package com.xmx.androidframeworkbase.Sync;

import com.xmx.androidframeworkbase.Tools.Data.SQL.BaseSQLEntityManager;

/**
 * Created by The_onE on 2016/3/27.
 */
public class SyncSQLManager extends BaseSQLEntityManager<Sync> {
    private static SyncSQLManager instance;

    public synchronized static SyncSQLManager getInstance() {
        if (null == instance) {
            instance = new SyncSQLManager();
        }
        return instance;
    }

    private SyncSQLManager() {
        tableName = "SyncTest";
        entityTemplate = new Sync();
        openDatabase();
    }
}
