package com.xmx.androidframeworkbase;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.xmx.androidframeworkbase.Tools.ActivityBase.BaseActivity;
import com.xmx.androidframeworkbase.User.LoginActivity;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;
import cn.bingoogolapple.bgabanner.BGABannerUtil;

public class SplashBannerActivity extends BaseActivity {

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_splash_banner);

        BGABanner mContentBanner = getViewById(R.id.banner_guide);

        List<View> views = new ArrayList<>();
        views.add(BGABannerUtil.getItemImageView(this, R.drawable.splash));
        views.add(BGABannerUtil.getItemImageView(this, R.drawable.login));
        views.add(BGABannerUtil.getItemImageView(this, R.drawable.splash));
// 如果你想实现少于3页的无限轮播，请不要用该方式初始化。用「方式1」初始化
        mContentBanner.setData(views);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startMainActivity();
            }
        }, Constants.LONGEST_SPLASH_TIME);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    void startLoginActivity() {
        Intent loginIntent = new Intent(SplashBannerActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }

    void startMainActivity() {
        Intent mainIntent = new Intent(SplashBannerActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
}