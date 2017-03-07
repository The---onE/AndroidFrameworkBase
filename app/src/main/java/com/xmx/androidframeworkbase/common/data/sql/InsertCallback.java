package com.xmx.androidframeworkbase.common.data.sql;

/**
 * Created by The_onE on 2016/1/11.
 */
public abstract class InsertCallback {

    public abstract void proceeding(int index);

    public abstract void success(int total);
}
