package com.xmx.androidframeworkbase.User.Callback;

import com.avos.avoscloud.AVObject;

/**
 * Created by The_onE on 2016/6/16.
 */
public abstract class LogoutCallback {
    public LogoutCallback() {
    }

    public abstract void logout(AVObject user);
}
