package com.xmx.androidframeworkbase;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.avos.avoscloud.AVObject;
import com.xmx.androidframeworkbase.Tools.ActivityBase.BaseActivity;
import com.xmx.androidframeworkbase.User.Callback.AutoLoginCallback;
import com.xmx.androidframeworkbase.User.LoginActivity;
import com.xmx.androidframeworkbase.User.UserManager;

public class SplashActivity extends BaseActivity {
    boolean loginFlag = false;
    boolean timeFlag = false;
    boolean notLoginFlag = false;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                timeFlag = true;
                if (loginFlag) {
                    startMainActivity();
                }
                if (notLoginFlag) {
                    startLoginActivity();
                }
            }
        }, Constants.SPLASH_TIME);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (loginFlag) {
                    startMainActivity();
                } else {
                    startLoginActivity();
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
            public void notLoggedIn() {
                notLoginFlag = true;
                if (timeFlag) {
                    startLoginActivity();
                }
            }

            @Override
            public void errorNetwork() {
                showToast(R.string.network_error);
                notLoginFlag = true;
                if (timeFlag) {
                    startLoginActivity();
                }
            }

            @Override
            public void errorUsername() {
                notLoginFlag = true;
                if (timeFlag) {
                    startLoginActivity();
                }
            }

            @Override
            public void errorChecksum() {
                notLoginFlag = true;
                if (timeFlag) {
                    startLoginActivity();
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
        Intent loginIntent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }

    void startMainActivity() {
        Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
}