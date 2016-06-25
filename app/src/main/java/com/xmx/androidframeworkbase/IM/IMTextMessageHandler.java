package com.xmx.androidframeworkbase.IM;

import android.content.Context;
import android.widget.Toast;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.xmx.androidframeworkbase.Tools.IM.TextMessageHandler;

import java.util.Date;

/**
 * Created by The_onE on 2016/6/24.
 */
public class IMTextMessageHandler extends TextMessageHandler {
    Context context;
    OnReceiveCallback callback;
    public IMTextMessageHandler(Context context, OnReceiveCallback callback) {
        this.context = context;
        this.callback = callback;
    }
    @Override
    public void onReceiveText(String text, String from, long time,
                              AVIMConversation conversation, AVIMClient client) {
        Date date = new Date(time);
        String info = from + ":" + text + "\n" + date.toLocaleString();
        Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
        callback.receive(text, from, time, conversation, client);
    }
}
