package com.xmx.androidframeworkbase.Float;

import com.xmx.androidframeworkbase.MainActivity;
import com.xmx.androidframeworkbase.Tools.Float.FloatViewManager;
import com.xmx.androidframeworkbase.Tools.ServiceBase.BaseService;

public class FloatService extends BaseService {

    @Override
    protected void processLogic() {
        FloatViewManager.getInstance().showFloatView(this);
    }

    @Override
    public void onDestroy() {
        FloatViewManager.getInstance().hideFloatView(this);
        super.onDestroy();
    }

    @Override
    protected void setForeground() {
        showForeground(MainActivity.class, "浮动窗口已打开");
    }
}
