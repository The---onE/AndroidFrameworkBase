package com.xmx.androidframeworkbase.common.data.sql;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by The_onE on 2016/3/22.
 */
public interface ISQLEntity {
    String tableFields();

    ContentValues getContent();

    ISQLEntity convertToEntity(Cursor c);
}