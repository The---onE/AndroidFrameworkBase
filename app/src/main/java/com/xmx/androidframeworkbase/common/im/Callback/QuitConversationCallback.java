package com.xmx.androidframeworkbase.common.im.Callback;

import com.avos.avoscloud.im.v2.AVIMConversation;

/**
 * Created by The_onE on 2016/1/13.
 */
public abstract class QuitConversationCallback {

    public abstract void success(AVIMConversation conversation);

    public abstract void failure(Exception e);

    public abstract void clientError();
}
