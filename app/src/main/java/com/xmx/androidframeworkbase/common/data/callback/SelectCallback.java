package com.xmx.androidframeworkbase.common.data.callback;

import com.avos.avoscloud.AVException;
import com.xmx.androidframeworkbase.common.data.cloud.ICloudEntity;

import java.util.List;

/**
 * Created by The_onE on 2016/5/29.
 */
public abstract class SelectCallback<Entity extends ICloudEntity> {

    public abstract void success(List<Entity> entities);

    public abstract void syncError(int error);

    public abstract void syncError(AVException e);
}
