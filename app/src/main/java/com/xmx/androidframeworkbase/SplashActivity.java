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

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                if (loginFlag) {
                    Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                } else {
                    Intent loginIntent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                    finish();
                }
            }
        }, Constants.SPLASH_TIME);



        UserManager.getInstance().autoLogin(new AutoLoginCallback() {
            @Override
            public void success(AVObject user) {
                loginFlag = true;
            }

            @Override
            public void notLoggedIn() {
            }

            @Override
            public void errorNetwork() {
                showToast(R.string.network_error);
            }

            @Override
            public void errorUsername() {
            }

            @Override
            public void errorChecksum() {
            }
        });
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }
}
