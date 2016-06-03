package com.xmx.androidframeworkbase.Tools.Data.Callback;

import com.avos.avoscloud.AVException;
import com.xmx.androidframeworkbase.Tools.Data.Cloud.ICloudEntity;

import java.util.List;

/**
 * Created by The_onE on 2016/5/29.
 */
public abstract class SelectCallback<Entity extends ICloudEntity> {

    public abstract void success(List<Entity> entities);

    public abstract void notInit();

    public abstract void syncError(AVException e);

    public abstract void notLoggedIn();

    public abstract void errorNetwork();

    public abstract void errorUsername();

    public abstract void errorChecksum();
}
