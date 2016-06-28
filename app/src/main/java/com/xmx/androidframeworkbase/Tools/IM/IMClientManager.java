package com.xmx.androidframeworkbase.Tools.IM;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.xmx.androidframeworkbase.Tools.IM.Callback.CreateConversationCallback;
import com.xmx.androidframeworkbase.Tools.IM.Callback.FindConversationCallback;
import com.xmx.androidframeworkbase.Tools.IM.Callback.GetTextChatLogCallback;
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

    //获取用户名，IM客户端以用户名为唯一标识
    public String getUsername() {
        return username;
    }

    boolean checkClient() {
        return openFlag && client != null && username != null && username.length() > 0;
    }

    //打开客户端，所有操作必须在打开客户端之后进行
    public void openClient(String username,
                           AVIMClientCallback callback) {
        this.username = username;
        client = AVIMClient.getInstance(username);
        client.open(callback);
        openFlag = true;
    }

    //关闭客户端
    public void closeClient(AVIMClientCallback callback) {
        if (checkClient()) {
            client.close(callback);
            openFlag = false;
            client = null;
            username = "";
        }
    }

    //创建对话，若同名对话已存在不创建，回调函数中返回对应对话
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

    //查找对话，回调函数中返回找到的对话
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

    //加入对话，在创建或查找到对话后调用
    public void joinConversation(final AVIMConversation conversation, final JoinConversationCallback callback) {
        if (checkClient()) {
            if (!conversation.getMembers().contains(username)) {
                conversation.join(new AVIMConversationCallback() {
                    @Override
                    public void done(AVIMException e) {
                        if (e == null) {
                            currentConversation = conversation;
                            callback.success(conversation);
                        } else {
                            callback.failure(e);
                        }
                    }
                });
            } else {
                currentConversation = conversation;
                callback.success(conversation);
            }
        } else {
            callback.clientError();
        }
    }

    //退出对话,在创建或查找到对话后调用
    public void quitConversation(final AVIMConversation conversation,
                                 final QuitConversationCallback callback) {
        if (checkClient() && conversation != null) {
            conversation.quit(new AVIMConversationCallback() {
                @Override
                public void done(AVIMException e) {
                    if (e == null) {
                        callback.success(conversation);
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

    //退出对话，退出最近加入的对话
    public void quitConversation(final QuitConversationCallback callback) {
        if (checkClient() && currentConversation != null) {
            currentConversation.quit(new AVIMConversationCallback() {
                @Override
                public void done(AVIMException e) {
                    if (e == null) {
                        callback.success(currentConversation);
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

    //发送文本，发送到最近加入的对话中
    public void sendText(String text, final SendMessageCallback callback) {
        if (checkClient() && currentConversation != null) {
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

    //发送文本，发送到对应的对话中
    public void sendText(AVIMConversation conversation,
                         String text, final SendMessageCallback callback) {
        if (checkClient() && conversation != null) {
            AVIMTextMessage message = new AVIMTextMessage();
            message.setText(text);
            conversation.sendMessage(message, new AVIMConversationCallback() {
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

    //获取最近加入的对话的文本聊天记录
    public void getTextChatLog(final GetTextChatLogCallback callback) {
        if (checkClient() && currentConversation != null) {
            currentConversation.queryMessages(new AVIMMessagesQueryCallback() {
                @Override
                public void done(List<AVIMMessage> messages, AVIMException e) {
                    if (e == null) {
                        List<AVIMTextMessage> ms = new ArrayList<>();
                        for (int i = messages.size() - 1; i >= 0; --i) {
                            AVIMMessage message = messages.get(i);
                            if (message instanceof AVIMTextMessage) {
                                ms.add((AVIMTextMessage) message);
                            }
                        }
                        callback.success(ms);
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
