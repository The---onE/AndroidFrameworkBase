package com.xmx.androidframeworkbase.SQL;

import com.xmx.androidframeworkbase.Constants;
import com.xmx.androidframeworkbase.Tools.Data.SQL.BaseSQLEntityManager;

/**
 * Created by The_onE on 2016/3/27.
 */
public class SQLEntityManager extends BaseSQLEntityManager<SQL> {
    private static SQLEntityManager instance;

    public synchronized static SQLEntityManager getInstance() {
        if (null == instance) {
            instance = new SQLEntityManager();
        }
        return instance;
    }

    private SQLEntityManager() {
        tableName = "SQLTest";
        entityTemplate = new SQL();
        openDatabase();
    }
}
