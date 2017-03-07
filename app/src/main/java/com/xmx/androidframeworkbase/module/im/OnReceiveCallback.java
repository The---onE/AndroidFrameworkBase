package com.xmx.androidframeworkbase.module.im;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;

/**
 * Created by The_onE on 2016/6/25.
 */
public abstract class OnReceiveCallback {

    public abstract void receive(String text, String from, long time,
                                 AVIMConversation conversation, AVIMClient client);

}
