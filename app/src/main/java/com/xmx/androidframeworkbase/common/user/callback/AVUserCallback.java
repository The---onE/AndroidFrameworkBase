package com.xmx.androidframeworkbase.common.user.callback;

import com.avos.avoscloud.AVException;
import com.xmx.androidframeworkbase.common.user.UserData;

/**
 * Created by The_onE on 2016/1/14.
 */
public abstract class AVUserCallback {

    public abstract void success();

    public abstract void error(AVException e);
}
