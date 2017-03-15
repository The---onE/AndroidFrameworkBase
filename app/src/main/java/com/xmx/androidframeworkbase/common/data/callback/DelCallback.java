package com.xmx.androidframeworkbase.common.data.callback;

import com.avos.avoscloud.AVException;
import com.xmx.androidframeworkbase.common.user.UserData;

/**
 * Created by The_onE on 2016/5/31.
 */
public abstract class DelCallback {

    public abstract void success(UserData user);

    public abstract void syncError(int error);

    public abstract void syncError(AVException e);
}
