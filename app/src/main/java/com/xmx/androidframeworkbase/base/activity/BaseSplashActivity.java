package com.xmx.androidframeworkbase.base.activity;

import android.content.Intent;

import com.xmx.androidframeworkbase.core.activity.MainActivity;
import com.xmx.androidframeworkbase.common.user.LoginActivity;

/**
 * Created by The_onE on 2016/10/8.
 * 启动Activity基类，APP启动页，预处理部分数据后跳转至内容页
 */
public abstract class BaseSplashActivity extends BaseActivity {
    /**
     * 跳转至登录页
     */
    protected void startLoginActivity() {
        Intent loginIntent = new Intent(BaseSplashActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }

    /**
     * 跳转至主页
     */
    protected void startMainActivity() {
        Intent mainIntent = new Intent(BaseSplashActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
}
