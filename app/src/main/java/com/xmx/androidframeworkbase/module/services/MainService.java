package com.xmx.androidframeworkbase.module.services;

import android.content.Intent;

import com.xmx.androidframeworkbase.core.activity.MainActivity;
import com.xmx.androidframeworkbase.common.log.OperationLogEntityManager;
import com.xmx.androidframeworkbase.base.service.BaseService;
import com.xmx.androidframeworkbase.utils.Timer;

public class MainService extends BaseService {

    long time = System.currentTimeMillis();
    Timer timer;

    @Override
    protected void processLogic(Intent intent) {
        timer = new Timer() {
            @Override
            public void timer() {
                long now = System.currentTimeMillis();
                showToast("服务已运行" + (now - time) + "毫秒");
                OperationLogEntityManager.getInstance().addLog("服务已运行" + (now - time) + "毫秒");
            }
        };
        timer.start(5000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        OperationLogEntityManager.getInstance().addLog("服务停止");

        timer.stop();
    }

    @Override
    protected void setForeground(Intent intent) {
        showForeground(MainActivity.class, "正在运行");
    }
}
