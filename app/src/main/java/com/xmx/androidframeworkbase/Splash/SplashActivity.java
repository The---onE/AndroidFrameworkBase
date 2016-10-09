package com.xmx.androidframeworkbase.Splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.xmx.androidframeworkbase.Constants;
import com.xmx.androidframeworkbase.MainActivity;
import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.Tools.ActivityBase.BaseActivity;
import com.xmx.androidframeworkbase.Tools.ActivityBase.BaseSplashActivity;
import com.xmx.androidframeworkbase.User.LoginActivity;

public class SplashActivity extends BaseSplashActivity {

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
}