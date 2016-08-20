package com.xmx.androidframeworkbase.Data.SQL;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by The_onE on 2016/2/24.
 */
public class SQLManager {
    private static SQLManager instance;

    long sqlVersion = 0;
    long version = System.currentTimeMillis();
    List<SQL> sqlList = new ArrayList<>();

    public synchronized static SQLManager getInstance() {
        if (null == instance) {
            instance = new SQLManager();
        }
        return instance;
    }

    public List<SQL> getData() {
        return sqlList;
    }

    public long updateData() {
        SQLEntityManager sqlManager = SQLEntityManager.getInstance();
        if (sqlManager.getVersion() != sqlVersion) {
            sqlVersion = sqlManager.getVersion();

            sqlList.clear();
            sqlList = sqlManager.selectAll("Time", false);

            version++;
        }
        return version;
    }
}
