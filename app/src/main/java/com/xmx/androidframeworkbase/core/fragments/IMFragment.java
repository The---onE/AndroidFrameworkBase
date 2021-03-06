package com.xmx.androidframeworkbase.core.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.xmx.androidframeworkbase.common.user.IUserManager;
import com.xmx.androidframeworkbase.common.user.UserData;
import com.xmx.androidframeworkbase.module.im.IMAdapter;
import com.xmx.androidframeworkbase.module.im.IMTextMessageHandler;
import com.xmx.androidframeworkbase.module.im.OnReceiveCallback;
import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.base.fragment.xUtilsFragment;
import com.xmx.androidframeworkbase.common.im.Callback.CreateConversationCallback;
import com.xmx.androidframeworkbase.common.im.Callback.GetTextChatLogCallback;
import com.xmx.androidframeworkbase.common.im.Callback.JoinConversationCallback;
import com.xmx.androidframeworkbase.common.im.Callback.SendMessageCallback;
import com.xmx.androidframeworkbase.common.im.IMClientManager;
import com.xmx.androidframeworkbase.common.im.IMMessageHandlerManager;
import com.xmx.androidframeworkbase.common.user.callback.AutoLoginCallback;
import com.xmx.androidframeworkbase.common.user.UserConstants;
import com.xmx.androidframeworkbase.common.user.UserManager;
import com.xmx.androidframeworkbase.utils.ExceptionUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
@ContentView(R.layout.fragment_im)
public class IMFragment extends xUtilsFragment {

    IMAdapter imAdapter;

    @ViewInject(R.id.edit_im)
    private EditText text;

    @ViewInject(R.id.list_im)
    private ListView imList;

    private IUserManager userManager = UserManager.getInstance();

    @Event(value = R.id.btn_open_client)
    private void onOpenClick(View view) {
        userManager.checkLogin(new AutoLoginCallback() {
            @Override
            public void success(UserData user) {
                showToast("IM客户端打开中……");

                IMClientManager.getInstance().openClient(user.username,
                        new AVIMClientCallback() {
                            @Override
                            public void done(AVIMClient avimClient, AVIMException e) {
                                showToast("IM客户端打开成功");
                            }
                        });
            }

            @Override
            public void error(AVException e) {
                showToast(R.string.network_error);
                ExceptionUtil.normalException(e, getContext());
            }

            @Override
            public void error(int error) {
                switch (error) {
                    case UserConstants.NOT_LOGGED_IN:
                    case UserConstants.CANNOT_CHECK_LOGIN:
                        showToast(R.string.not_loggedin);
                        break;

                    case UserConstants.USERNAME_ERROR:
                        showToast(R.string.username_error);
                        break;

                    case UserConstants.CHECKSUM_ERROR:
                        showToast(R.string.not_loggedin);
                        break;
                }
            }
        });
    }

    @Event(value = R.id.btn_join_conversation)
    private void onJoinClick(View view) {
        IMClientManager.getInstance().createConversation("test", new CreateConversationCallback() {
            @Override
            public void success(AVIMConversation conversation) {
                showToast("创建对话成功");
                IMClientManager.getInstance().joinConversation(conversation, new JoinConversationCallback() {
                    @Override
                    public void success(AVIMConversation conversation) {
                        showToast("加入对话成功");
                        updateList();
                    }

                    @Override
                    public void failure(Exception e) {
                        showToast("加入对话失败");
                        ExceptionUtil.normalException(e, getContext());
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
                ExceptionUtil.normalException(e, getContext());
            }

            @Override
            public void exist(AVIMConversation conversation) {
                showToast("对话已存在");
                IMClientManager.getInstance().joinConversation(conversation, new JoinConversationCallback() {
                    @Override
                    public void success(AVIMConversation conversation) {
                        showToast("加入对话成功");
                        updateList();
                    }

                    @Override
                    public void failure(Exception e) {
                        showToast("加入对话失败");
                        ExceptionUtil.normalException(e, getContext());
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

    @Event(value = R.id.btn_send_message)
    private void onSendClick(View view) {
        String data = text.getText().toString();
        text.setText("");
        IMClientManager.getInstance().sendText(data, new SendMessageCallback() {
            @Override
            public void success() {
                showToast("发送成功");
                updateList();
            }

            @Override
            public void failure(Exception e) {
                showToast("发送失败");
                ExceptionUtil.normalException(e, getContext());
            }

            @Override
            public void conversationError() {
                showToast("对话未建立");
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        imAdapter = new IMAdapter(getContext(), new ArrayList<AVIMTextMessage>());
        imList.setAdapter(imAdapter);

        IMTextMessageHandler handler = new IMTextMessageHandler(getContext(), new OnReceiveCallback() {
            @Override
            public void receive(String text, String from, long time, AVIMConversation conversation, AVIMClient client) {
                updateList();
            }
        });
        IMMessageHandlerManager.getInstance().addTextMessageHandler(handler);
    }

    public void updateList() {
        IMClientManager.getInstance().getTextChatLog(new GetTextChatLogCallback() {
            @Override
            public void success(List<AVIMTextMessage> messages) {
                imAdapter.updateList(messages);
            }

            @Override
            public void failure(Exception e) {
                showToast("获取聊天记录失败");
                ExceptionUtil.normalException(e, getContext());
            }

            @Override
            public void conversationError() {
                showToast("对话未建立");
            }
        });
    }

}
