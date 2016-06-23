package com.xmx.androidframeworkbase.Tools.IM.Callback;

/**
 * Created by The_onE on 2016/6/23.
 */
public abstract class SendMessageCallback {

    public abstract void success();

    public abstract void failure(Exception e);

    public abstract void conversationError();
}
