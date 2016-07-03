package com.xmx.androidframeworkbase.User.Callback;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;

/**
 * Created by The_onE on 2016/1/11.
 */
public abstract class AutoLoginCallback {

    public abstract void success(AVObject user);

    public abstract void error(int error);

    public abstract void error(AVException e);
}
