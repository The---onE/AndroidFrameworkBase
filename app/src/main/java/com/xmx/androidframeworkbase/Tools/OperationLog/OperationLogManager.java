package com.xmx.androidframeworkbase.Tools.OperationLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by The_onE on 2016/2/24.
 */
public class OperationLogManager {
    private static OperationLogManager instance;

    long sqlVersion = 0;
    long version = System.currentTimeMillis();
    List<OperationLog> sqlList = new ArrayList<>();

    public synchronized static OperationLogManager getInstance() {
        if (null == instance) {
            instance = new OperationLogManager();
        }
        return instance;
    }

    public List<OperationLog> getData() {
        return sqlList;
    }

    public long updateData() {
        OperationLogEntityManager logManager = OperationLogEntityManager.getInstance();
        if (logManager.getVersion() != sqlVersion) {
            sqlVersion = logManager.getVersion();

            sqlList.clear();
            sqlList = logManager.selectAll("Time", false);

            version++;
        }
        return version;
    }
}
