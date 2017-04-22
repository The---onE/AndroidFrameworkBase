package com.xmx.androidframeworkbase.core.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.avos.avoscloud.AVException;
import com.xmx.androidframeworkbase.common.user.IUserManager;
import com.xmx.androidframeworkbase.common.user.LoginEvent;
import com.xmx.androidframeworkbase.common.user.UserConstants;
import com.xmx.androidframeworkbase.common.user.UserData;
import com.xmx.androidframeworkbase.common.user.UserManager;
import com.xmx.androidframeworkbase.common.user.callback.AutoLoginCallback;
import com.xmx.androidframeworkbase.core.Constants;
import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.base.activity.BaseSplashActivity;
import com.xmx.androidframeworkbase.utils.ExceptionUtil;
import com.xmx.androidframeworkbase.utils.Timer;

import org.greenrobot.eventbus.EventBus;

public class SplashActivity extends BaseSplashActivity {

    Timer timer;

    private IUserManager userManager = UserManager.getInstance();

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void setListener() {
        getViewById(R.id.btn_skip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.stop();
                timer.execute();
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        timer = new Timer() {
            @Override
            public void timer() {
                startMainActivity();
            }
        };
        timer.start(Constants.SPLASH_TIME, true);
        // 使用设备保存的数据自动登录
        userManager.autoLogin(new AutoLoginCallback() {
            @Override
            public void success(final UserData user) {
                EventBus.getDefault().post(new LoginEvent());
            }

            @Override
            public void error(AVException e) {
                ExceptionUtil.normalException(e, getBaseContext());
            }

            @Override
            public void error(int error) {
                switch (error) {
                    case UserConstants.NOT_LOGGED_IN:
                        showToast("请在侧边栏中选择登录");
                        break;
                    case UserConstants.USERNAME_ERROR:
                        showToast("请在侧边栏中选择登录");
                        break;
                    case UserConstants.CHECKSUM_ERROR:
                        showToast("登录过期，请在侧边栏中重新登录");
                        break;
                }
            }
        });
    }
}