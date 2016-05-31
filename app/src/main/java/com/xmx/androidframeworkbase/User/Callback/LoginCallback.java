package com.xmx.androidframeworkbase.User.Callback;

import com.avos.avoscloud.AVObject;

/**
 * Created by The_onE on 2016/1/11.
 */
public abstract class LoginCallback {
    public LoginCallback() {
    }

    public abstract void success(AVObject user);

    public abstract void errorNetwork();

    public abstract void errorUsername();

    public abstract void errorPassword();
}
