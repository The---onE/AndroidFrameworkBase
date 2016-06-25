package com.xmx.androidframeworkbase.Tools.IM.Callback;

import com.xmx.androidframeworkbase.Tools.IM.Message.TextMessage;

import java.util.List;

/**
 * Created by The_onE on 2016/6/26.
 */
public abstract class GetTextChatLogCallback {

    public abstract void success(List<TextMessage> messages);

    public abstract void failure(Exception e);

    public abstract void conversationError();
}
