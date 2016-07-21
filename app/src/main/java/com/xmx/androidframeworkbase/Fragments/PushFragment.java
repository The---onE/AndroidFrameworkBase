package com.xmx.androidframeworkbase.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.SaveCallback;
import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.Tools.FragmentBase.xUtilsFragment;
import com.xmx.androidframeworkbase.Tools.PushMessage.PushItemMessageActivity;
import com.xmx.androidframeworkbase.Tools.PushMessage.ReceiveMessageActivity;
import com.xmx.androidframeworkbase.User.Callback.AutoLoginCallback;
import com.xmx.androidframeworkbase.User.UserConstants;
import com.xmx.androidframeworkbase.User.UserManager;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
@ContentView(R.layout.fragment_push)
public class PushFragment extends xUtilsFragment {
    String title = "test";

    @Event(value = R.id.btn_subscribe)
    private void onSubscribeClick(View view) {
        UserManager.getInstance().checkLogin(new AutoLoginCallback() {
            @Override
            public void success(AVObject user) {
                List<String> subscribing = user.getList("subscribing");
                if (subscribing == null) {
                    subscribing = new ArrayList<>();
                }

                if (subscribing.contains(title)) {
                    showToast("已经关注过了");
                    PushService.subscribe(getContext(), UserManager.getSHA(title), ReceiveMessageActivity.class);
                    AVInstallation.getCurrentInstallation().saveInBackground();
                } else {
                    subscribing.add(title);
                    user.put("subscribing", subscribing);
                    user.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                PushService.subscribe(getContext(), UserManager.getSHA(title), ReceiveMessageActivity.class);
                                AVInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(AVException e) {
                                        if (e == null) {
                                            showToast("关注成功");
                                        } else {
                                            filterException(e);
                                        }
                                    }
                                });
                            } else {
                                filterException(e);
                            }
                        }
                    });
                }
            }

            @Override
            public void error(AVException e) {
                showToast(R.string.network_error);
                filterException(e);
            }

            @Override
            public void error(int error) {
                switch (error) {
                    case UserConstants.NOT_LOGGED_IN:
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

    @Event(value = R.id.btn_unsubscribe)
    private void onUnsubscribeClick(View view) {
        PushService.unsubscribe(getContext(), UserManager.getSHA(title));
        AVInstallation.getCurrentInstallation().saveInBackground();
        UserManager.getInstance().checkLogin(new AutoLoginCallback() {
            @Override
            public void success(AVObject user) {
                List<String> subscribing = user.getList("subscribing");
                if (subscribing == null) {
                    showToast("没有关注过");
                } else {
                    if (!subscribing.contains(title)) {
                        showToast("没有关注过");
                    } else {
                        subscribing.remove(title);
                        user.put("subscribing", subscribing);
                        user.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                if (e == null) {
                                    showToast("取关成功");
                                } else {
                                    filterException(e);
                                }
                            }
                        });
                    }
                }
            }

            @Override
            public void error(AVException e) {
                showToast(R.string.network_error);
                filterException(e);
            }

            @Override
            public void error(int error) {
                switch (error) {
                    case UserConstants.NOT_LOGGED_IN:
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

    @Event(value = R.id.btn_push)
    private void onPushClick(View view) {
        startActivity(PushItemMessageActivity.class,
                "title", title);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

}