package com.xmx.androidframeworkbase.User.Callback;

/**
 * Created by The_onE on 2016/1/14.
 */
public abstract class RegisterCallback {
    public RegisterCallback() {
    }

    public abstract void success();

    public abstract void usernameExist();

    public abstract void nicknameExist();

    public abstract void errorNetwork();
}
