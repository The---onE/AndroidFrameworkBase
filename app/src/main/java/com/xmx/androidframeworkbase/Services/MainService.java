package com.xmx.androidframeworkbase.Services;

import com.xmx.androidframeworkbase.MainActivity;
import com.xmx.androidframeworkbase.Tools.ServiceBase.BaseService;
import com.xmx.androidframeworkbase.Tools.Timer;

public class MainService extends BaseService {

    long time = System.currentTimeMillis();
    Timer timer;

    @Override
    protected void processLogic() {
        timer = new Timer() {
            @Override
            public void timer() {
                long now = System.currentTimeMillis();
                showToast("服务已运行" + (now - time) + "毫秒");
            }
        };
        timer.start(5000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        timer.stop();
    }

    @Override
    protected void setForeground() {
        showForeground(MainActivity.class, "正在运行");
    }
}
