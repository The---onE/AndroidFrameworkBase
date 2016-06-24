package com.xmx.androidframeworkbase.IM;

import android.content.Context;
import android.widget.Toast;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.xmx.androidframeworkbase.Tools.IM.TextMessageHandler;

/**
 * Created by The_onE on 2016/6/24.
 */
public class IMTextMessageHandler extends TextMessageHandler {
    Context context;
    public IMTextMessageHandler(Context context) {
        this.context = context;
    }
    @Override
    public void onReceiveText(String text, String from, AVIMConversation conversation, AVIMClient client) {
        String info = from + ":" + text;
        Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
    }
}
