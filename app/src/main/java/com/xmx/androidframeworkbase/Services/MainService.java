package com.xmx.androidframeworkbase.Services;

import com.xmx.androidframeworkbase.MainActivity;
import com.xmx.androidframeworkbase.Tools.ServiceBase.BaseService;

public class MainService extends BaseService {

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setForeground() {
        showForeground(MainActivity.class, "正在运行");
    }
}
