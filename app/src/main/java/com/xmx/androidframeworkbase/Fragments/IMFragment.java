package com.xmx.androidframeworkbase.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.xmx.androidframeworkbase.IM.IMAdapter;
import com.xmx.androidframeworkbase.IM.IMTextMessageHandler;
import com.xmx.androidframeworkbase.IM.Message;
import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.Tools.BaseFragment;
import com.xmx.androidframeworkbase.Tools.IM.Callback.CreateConversationCallback;
import com.xmx.androidframeworkbase.Tools.IM.Callback.JoinConversationCallback;
import com.xmx.androidframeworkbase.Tools.IM.Callback.SendMessageCallback;
import com.xmx.androidframeworkbase.Tools.IM.IMClientManager;
import com.xmx.androidframeworkbase.User.Callback.AutoLoginCallback;
import com.xmx.androidframeworkbase.User.UserManager;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class IMFragment extends BaseFragment {

    EditText text;
    IMAdapter imAdapter;
    ListView imList;
    AVIMConversation currentConversation;

    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_im, container, false);
    }

    @Override
    protected void initView(View view) {
        text = (EditText) view.findViewById(R.id.edit_im);
        imList = (ListView) view.findViewById(R.id.list_im);
        imAdapter = new IMAdapter(getContext(), new ArrayList<Message>());
        imList.setAdapter(imAdapter);

        IMTextMessageHandler handler = new IMTextMessageHandler(getContext());
        IMClientManager.getInstance().addTextMessageHandler(handler);
    }

    @Override
    protected void setListener(View view) {
        Button open = (Button) view.findViewById(R.id.btn_open_client);
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserManager.getInstance().checkLogin(new AutoLoginCallback() {
                    @Override
                    public void success(AVObject user) {
                        showToast("IM客户端打开中……");

                        IMClientManager.getInstance().openClient(user.getString("username"),
                                new AVIMClientCallback() {
                                    @Override
                                    public void done(AVIMClient avimClient, AVIMException e) {
                                        showToast("IM客户端打开成功");
                                    }
                                });
                    }

                    @Override
                    public void notLoggedIn() {
                        showToast(R.string.not_loggedin);
                    }

                    @Override
                    public void errorNetwork() {
                        showToast(R.string.network_error);
                    }

                    @Override
                    public void errorUsername() {
                        showToast(R.string.username_error);
                    }

                    @Override
                    public void errorChecksum() {
                        showToast(R.string.not_loggedin);
                    }
                });
            }
        });

        Button join = (Button) view.findViewById(R.id.btn_join_conversation);
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IMClientManager.getInstance().createConversation("test", new CreateConversationCallback() {
                    @Override
                    public void success(AVIMConversation conversation) {
                        showToast("创建对话成功");
                        IMClientManager.getInstance().joinConversation(conversation, new JoinConversationCallback() {
                            @Override
                            public void success(AVIMConversation conversation) {
                                showToast("加入对话成功");
                                updateList(conversation);
                                currentConversation = conversation;
                            }

                            @Override
                            public void failure(Exception e) {
                                showToast("加入对话失败");
                            }

                            @Override
                            public void clientError() {
                                showToast("IM客户端未打开");
                            }
                        });
                    }

                    @Override
                    public void failure(Exception e) {
                        showToast("创建对话失败");
                    }

                    @Override
                    public void exist(AVIMConversation conversation) {
                        showToast("对话已存在");
                        IMClientManager.getInstance().joinConversation(conversation, new JoinConversationCallback() {
                            @Override
                            public void success(AVIMConversation conversation) {
                                showToast("加入对话成功");
                                updateList(conversation);
                                currentConversation = conversation;
                            }

                            @Override
                            public void failure(Exception e) {
                                showToast("加入对话失败");
                            }

                            @Override
                            public void clientError() {
                                showToast("IM客户端未打开");
                            }
                        });
                    }

                    @Override
                    public void clientError() {
                        showToast("IM客户端未打开");
                    }
                });
            }
        });

        Button send = (Button) view.findViewById(R.id.btn_send_message);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = text.getText().toString();
                text.setText("");
                IMClientManager.getInstance().sendText(data, new SendMessageCallback() {
                    @Override
                    public void success() {
                        showToast("发送成功");
                        updateList(currentConversation);
                    }

                    @Override
                    public void failure(Exception e) {
                        showToast("发送失败");
                    }

                    @Override
                    public void conversationError() {
                        showToast("对话未建立");
                    }
                });
            }
        });
    }

    @Override
    protected void processLogic(View view, Bundle savedInstanceState) {

    }

    public void updateList(AVIMConversation conversation) {
        conversation.queryMessages(new AVIMMessagesQueryCallback() {
            @Override
            public void done(List<AVIMMessage> messages, AVIMException e) {
                if (e == null) {
                    List<Message> ms = new ArrayList<>();
                    for (AVIMMessage message : messages) {
                        if (message instanceof AVIMTextMessage) {
                            AVIMTextMessage tm = (AVIMTextMessage) message;
                            Message m = new Message();
                            m.mText = tm.getText();
                            m.mFrom = tm.getFrom();
                            m.mTime = tm.getTimestamp();

                            ms.add(0, m);
                        }
                    }
                    imAdapter.updateList(ms);
                }
            }
        });
    }

}
