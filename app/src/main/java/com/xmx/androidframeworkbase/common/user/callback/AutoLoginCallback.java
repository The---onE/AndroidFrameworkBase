package com.xmx.androidframeworkbase.common.user.callback;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.xmx.androidframeworkbase.common.user.UserData;

/**
 * Created by The_onE on 2016/1/11.
 */
public abstract class AutoLoginCallback {

    public abstract void success(UserData user);

    public abstract void error(int error);

    public abstract void error(AVException e);
}
