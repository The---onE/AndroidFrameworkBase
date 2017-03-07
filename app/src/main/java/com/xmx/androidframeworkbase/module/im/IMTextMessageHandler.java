package com.xmx.androidframeworkbase.module.im;

import android.content.Context;
import android.widget.Toast;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.xmx.androidframeworkbase.common.im.TextMessageHandler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeString = df.format(new Date(time));
        String info = from + ":" + text + "\n" + timeString;
        Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
        callback.receive(text, from, time, conversation, client);
    }
}
