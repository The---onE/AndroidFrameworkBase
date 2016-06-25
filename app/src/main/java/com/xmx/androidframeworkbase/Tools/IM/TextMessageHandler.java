package com.xmx.androidframeworkbase.Tools.IM;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMTypedMessageHandler;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;

/**
 * Created by The_onE on 2016/6/24.
 */
public abstract class TextMessageHandler extends AVIMTypedMessageHandler<AVIMTextMessage> {
    @Override
    public void onMessage(AVIMTextMessage message, AVIMConversation conversation, AVIMClient client) {
        super.onMessage(message, conversation, client);
        try {
            String username = IMClientManager.getInstance().getUsername();
            if (client.getClientId().equals(username)) {
                String text = message.getText();
                String from = message.getFrom();
                long time = message.getTimestamp();
                onReceiveText(text, from, time, conversation, client);
            } else {
                client.close(null);
            }
        } catch (IllegalStateException e) {
            client.close(null);
        }
    }

    public abstract void onReceiveText(String text, String from, long time,
                                       AVIMConversation conversation, AVIMClient client);
}
