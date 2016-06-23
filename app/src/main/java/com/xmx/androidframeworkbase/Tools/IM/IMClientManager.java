package com.xmx.androidframeworkbase.Tools.IM;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.xmx.androidframeworkbase.Tools.IM.Callback.CreateConversationCallback;
import com.xmx.androidframeworkbase.Tools.IM.Callback.FindConversationCallback;
import com.xmx.androidframeworkbase.Tools.IM.Callback.JoinConversationCallback;
import com.xmx.androidframeworkbase.Tools.IM.Callback.QuitConversationCallback;
import com.xmx.androidframeworkbase.Tools.IM.Callback.SendMessageCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wli on 15/8/13.
 */
public class IMClientManager {
    private static IMClientManager imClientManager;

    private AVIMClient client;
    private String username;
    private boolean openFlag = false;
    AVIMConversation currentConversation = null;

    public synchronized static IMClientManager getInstance() {
        if (null == imClientManager) {
            imClientManager = new IMClientManager();
        }
        return imClientManager;
    }

    boolean checkClient() {
        return openFlag && client != null && username != null && username.length() > 0;
    }

    public void openClient(String username, AVIMClientCallback callback) {
        this.username = username;
        client = AVIMClient.getInstance(username);
        client.open(callback);
        openFlag = true;
    }

    public void closeClient(AVIMClientCallback callback) {
        if (checkClient()) {
            client.close(callback);
            openFlag = false;
            client = null;
            username = "";
        }
    }

    public void createConversation(final String name, final CreateConversationCallback callback) {
        if (checkClient()) {
            AVIMConversationQuery query = client.getQuery();
            query.whereEqualTo("name", name);
            query.findInBackground(new AVIMConversationQueryCallback() {
                @Override
                public void done(List<AVIMConversation> convs, AVIMException e) {
                    if (e == null) {
                        if (convs != null && !convs.isEmpty()) {
                            AVIMConversation conversation = convs.get(0);
                            callback.exist(conversation);
                        } else {
                            client.createConversation(new ArrayList<String>(), name, null,
                                    new AVIMConversationCreatedCallback() {
                                        @Override
                                        public void done(AVIMConversation avimConversation, AVIMException e) {
                                            if (e == null) {
                                                callback.success(avimConversation);
                                            } else {
                                                callback.failure(e);
                                            }
                                        }
                                    });
                        }
                    } else {
                        callback.failure(e);
                    }
                }
            });
        } else {
            callback.clientError();
        }
    }

    public void findConversation(String name, final FindConversationCallback callback) {
        if (checkClient()) {
            AVIMConversationQuery query = client.getQuery();
            query.whereEqualTo("name", name);
            query.findInBackground(new AVIMConversationQueryCallback() {
                @Override
                public void done(List<AVIMConversation> convs, AVIMException e) {
                    if (e == null) {
                        if (convs != null && !convs.isEmpty()) {
                            callback.found(convs.get(0));
                        } else {
                            callback.notFound();
                        }
                    } else {
                        callback.error(e);
                    }
                }
            });
        } else {
            callback.clientError();
        }
    }

    public void joinConversation(final AVIMConversation conversation, final JoinConversationCallback callback) {
        if (checkClient()) {
            if (!conversation.getMembers().contains(username)) {
                conversation.join(new AVIMConversationCallback() {
                    @Override
                    public void done(AVIMException e) {
                        if (e == null) {
                            callback.success(conversation);
                            currentConversation = conversation;
                        } else {
                            callback.failure(e);
                        }
                    }
                });
            } else {
                callback.success(conversation);
                currentConversation = conversation;
            }
        } else {
            callback.clientError();
        }
    }

    public void quitConversation(AVIMConversation conversation, final QuitConversationCallback callback) {
        if (checkClient() && conversation != null) {
            conversation.quit(new AVIMConversationCallback() {
                @Override
                public void done(AVIMException e) {
                    if (e == null) {
                        callback.success();
                        currentConversation = null;
                    } else {
                        callback.failure(e);
                    }
                }
            });
        } else {
            callback.clientError();
        }
    }

    public void sendText(String text, final SendMessageCallback callback) {
        if (currentConversation != null) {
            AVIMTextMessage message = new AVIMTextMessage();
            message.setText(text);
            currentConversation.sendMessage(message, new AVIMConversationCallback() {
                @Override
                public void done(AVIMException e) {
                    if (e == null) {
                        callback.success();
                    } else {
                        callback.failure(e);
                    }
                }
            });
        } else {
            callback.conversationError();
        }
    }
}
