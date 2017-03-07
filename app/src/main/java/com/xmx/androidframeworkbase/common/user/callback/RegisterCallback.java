package com.xmx.androidframeworkbase.common.user.callback;

import com.avos.avoscloud.AVException;

/**
 * Created by The_onE on 2016/1/14.
 */
public abstract class RegisterCallback {

    public abstract void success();

    public abstract void error(int error);

    public abstract void error(AVException e);
}
