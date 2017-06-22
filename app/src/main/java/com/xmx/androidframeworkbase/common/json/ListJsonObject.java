package com.xmx.androidframeworkbase.common.json;

import java.util.List;

/**
 * Created by The_onE on 2017/6/22.
 */

public class ListJsonObject<T extends IJsonEntity> {
    public String status;
    public String prompt;
    public List<T> entities;
}
