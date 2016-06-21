package com.xmx.androidframeworkbase.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.Tools.BaseFragment;
import com.xmx.androidframeworkbase.Tools.IM.Callback.CreateConversationCallback;
import com.xmx.androidframeworkbase.Tools.IM.IMClientManager;
import com.xmx.androidframeworkbase.User.Callback.AutoLoginCallback;
import com.xmx.androidframeworkbase.User.UserManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class TestFragment extends BaseFragment {

    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_test, container, false);
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void setListener(View view) {
        Button im = (Button) view.findViewById(R.id.btn_im);
        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserManager.getInstance().checkLogin(new AutoLoginCallback() {
                    @Override
                    public void success(AVObject user) {
                        showToast("IM客户端打开中……");
                        final IMClientManager im = IMClientManager.getInstance();
                        im.openClient(user.getString("username"),
                                new AVIMClientCallback() {
                                    @Override
                                    public void done(AVIMClient avimClient, AVIMException e) {
                                        showToast("IM客户端打开成功");
                                        im.createConversation("test", new CreateConversationCallback() {
                                            @Override
                                            public void success(AVIMConversation conversation) {
                                                showToast("创建对话成功");
                                            }

                                            @Override
                                            public void failure(Exception e) {
                                                showToast("创建对话失败");
                                            }

                                            @Override
                                            public void clientError() {
                                                showToast("IM客户端打开失败");
                                            }
                                        });
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
    }

    @Override
    protected void processLogic(View view, Bundle savedInstanceState) {

    }

}
