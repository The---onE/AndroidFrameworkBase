package com.xmx.androidframeworkbase.Tools.IM.Callback;

import com.avos.avoscloud.im.v2.AVIMConversation;

/**
 * Created by The_onE on 2016/1/13.
 */
public abstract class FindConversationCallback {

    public abstract void found(AVIMConversation conversation);

    public abstract void notFound();

    public abstract void error(Exception e);

    public abstract void clientError();
}
