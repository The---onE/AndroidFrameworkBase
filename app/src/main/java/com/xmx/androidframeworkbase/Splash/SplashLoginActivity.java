package com.xmx.androidframeworkbase.Splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.xmx.androidframeworkbase.Constants;
import com.xmx.androidframeworkbase.MainActivity;
import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.Tools.ActivityBase.BaseActivity;
import com.xmx.androidframeworkbase.User.Callback.AutoLoginCallback;
import com.xmx.androidframeworkbase.User.LoginActivity;
import com.xmx.androidframeworkbase.User.UserConstants;
import com.xmx.androidframeworkbase.User.UserManager;

public class SplashLoginActivity extends BaseActivity {
    boolean loginFlag = false;
    boolean timeFlag = false;
    boolean notLoginFlag = false;
    boolean startFlag = false;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_splash_login);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                timeFlag = true;
                if (!startFlag) {
                    if (loginFlag) {
                        startMainActivity();
                    }
                    if (notLoginFlag) {
                        startLoginActivity();
                    }
                }
            }
        }, Constants.SPLASH_TIME);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!startFlag) {
                    if (loginFlag) {
                        startMainActivity();
                    } else {
                        startLoginActivity();
                    }
                }
            }
        }, Constants.LONGEST_SPLASH_TIME);


        UserManager.getInstance().autoLogin(new AutoLoginCallback() {
            @Override
            public void success(AVObject user) {
                loginFlag = true;
                if (timeFlag) {
                    startMainActivity();
                }
            }

            @Override
            public void error(AVException e) {
                showToast(R.string.network_error);
                filterException(e);
                notLoginFlag = true;
                if (timeFlag) {
                    startLoginActivity();
                }
            }

            @Override
            public void error(int error) {
                switch (error) {
                    case UserConstants.NOT_LOGGED_IN:
                        notLoginFlag = true;
                        if (timeFlag) {
                            startLoginActivity();
                        }
                        break;
                    case UserConstants.USERNAME_ERROR:
                        notLoginFlag = true;
                        if (timeFlag) {
                            startLoginActivity();
                        }
                        break;
                    case UserConstants.CHECKSUM_ERROR:
                        notLoginFlag = true;
                        if (timeFlag) {
                            startLoginActivity();
                        }
                        break;
                }
            }
        });
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    void startLoginActivity() {
        startFlag = true;
        Intent loginIntent = new Intent(SplashLoginActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }

    void startMainActivity() {
        startFlag = true;
        Intent mainIntent = new Intent(SplashLoginActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
}