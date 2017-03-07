package com.xmx.androidframeworkbase.common.im.Callback;

import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;

import java.util.List;

/**
 * Created by The_onE on 2016/6/26.
 */
public abstract class GetTextChatLogCallback {

    public abstract void success(List<AVIMTextMessage> messages);

    public abstract void failure(Exception e);

    public abstract void conversationError();
}
