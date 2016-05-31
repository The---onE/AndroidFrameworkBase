package com.xmx.androidframeworkbase.Cloud;

import com.xmx.androidframeworkbase.Tools.Data.Cloud.BaseCloudEntityManager;

/**
 * Created by The_onE on 2016/3/27.
 */
public class CloudEntityManager extends BaseCloudEntityManager<Cloud> {
    private static CloudEntityManager instance;

    public synchronized static CloudEntityManager getInstance() {
        if (null == instance) {
            instance = new CloudEntityManager();
        }
        return instance;
    }

    private CloudEntityManager() {
        tableName = "CloudTest";
        entityTemplate = new Cloud();
        userField = "User";
    }
}
