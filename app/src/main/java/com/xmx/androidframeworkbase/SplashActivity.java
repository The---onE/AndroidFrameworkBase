package com.xmx.androidframeworkbase;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.xmx.androidframeworkbase.Tools.ActivityBase.BaseActivity;
import com.xmx.androidframeworkbase.User.Callback.AutoLoginCallback;
import com.xmx.androidframeworkbase.User.LoginActivity;
import com.xmx.androidframeworkbase.User.UserConstants;
import com.xmx.androidframeworkbase.User.UserManager;

public class SplashActivity extends BaseActivity {

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startMainActivity();
            }
        }, Constants.SPLASH_TIME);
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