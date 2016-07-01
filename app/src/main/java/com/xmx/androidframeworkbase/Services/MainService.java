package com.xmx.androidframeworkbase.Services;

import com.xmx.androidframeworkbase.MainActivity;
import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.Tools.ServiceBase.BaseService;

public class MainService extends BaseService {

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setForeground() {
        setForeground(MainActivity.class, R.mipmap.ic_launcher,
                getString(R.string.app_name), "正在运行");
    }
}
