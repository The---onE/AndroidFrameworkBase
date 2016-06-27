package com.xmx.androidframeworkbase.Tools.IM;

import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by The_onE on 2016/6/27.
 */
public class IMMessageHandlerManager {
    private static IMMessageHandlerManager imMessageHandlerManager;

    List<TextMessageHandler> textMessageHandlers = new ArrayList<>();

    public synchronized static IMMessageHandlerManager getInstance() {
        if (null == imMessageHandlerManager) {
            imMessageHandlerManager = new IMMessageHandlerManager();
        }
        return imMessageHandlerManager;
    }

    //添加文本消息处理器
    public void addTextMessageHandler(TextMessageHandler handler) {
        if (!textMessageHandlers.contains(handler)) {
            AVIMMessageManager.registerMessageHandler(AVIMTextMessage.class, handler);
            textMessageHandlers.add(handler);
        }
    }

    //移除文本消息处理器
    public void removeTextMessageHandler(TextMessageHandler handler) {
        if (textMessageHandlers.contains(handler)) {
            AVIMMessageManager.unregisterMessageHandler(AVIMTextMessage.class, handler);
            textMessageHandlers.remove(handler);
        }
    }

    //移除全部文本消息处理器
    public void removeAllTextMessageHandlers() {
        for (TextMessageHandler handler : textMessageHandlers) {
            AVIMMessageManager.unregisterMessageHandler(AVIMTextMessage.class, handler);
        }
        textMessageHandlers.clear();
    }
}
