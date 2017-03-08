package com.xmx.androidframeworkbase.core.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.xmx.androidframeworkbase.core.Constants;
import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.base.activity.BaseSplashActivity;
import com.xmx.androidframeworkbase.utils.Timer;

public class SplashActivity extends BaseSplashActivity {

    Timer timer;

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
    }
}