package com.xmx.androidframeworkbase.module.floatwindow;

import android.content.Intent;

import com.xmx.androidframeworkbase.core.activity.MainActivity;
import com.xmx.androidframeworkbase.common.floatwindow.FloatViewManager;
import com.xmx.androidframeworkbase.base.service.BaseService;

public class FloatService extends BaseService {

    @Override
    protected void processLogic(Intent intent) {
        FloatViewManager.getInstance().showFloatView(this, new FloatView(getApplicationContext()));
    }

    @Override
    public void onDestroy() {
        FloatViewManager.getInstance().hideFloatView(this);
        super.onDestroy();
    }

    @Override
    protected void setForeground(Intent intent) {
        showForeground(MainActivity.class, "浮动窗口已打开");
    }
}
