package com.xmx.androidframeworkbase.Tools.Data.Callback;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;

/**
 * Created by The_onE on 2016/5/31.
 */
public abstract class DelCallback {

    public abstract void success(AVObject user);

    public abstract void syncError(int error);

    public abstract void syncError(AVException e);
}
