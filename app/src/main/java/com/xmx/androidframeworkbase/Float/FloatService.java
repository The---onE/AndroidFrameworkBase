package com.xmx.androidframeworkbase.Float;

import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;

import com.xmx.androidframeworkbase.MainActivity;
import com.xmx.androidframeworkbase.Tools.ServiceBase.BaseService;

public class FloatService extends BaseService {

    WindowManager wm;

    FloatView tv;

    @Override
    protected void processLogic() {
        wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams params = FloatView.params;

        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
                | WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;// 设置窗口类型为系统级
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;// 设置窗口焦点

        params.width = WindowManager.LayoutParams.FILL_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.alpha = 80;

        params.gravity = Gravity.LEFT | Gravity.TOP;
        // 以屏幕左上角为原点，设置x、y初始值,将悬浮窗口设置在屏幕中间的位置
        params.x = 0;
        params.y = wm.getDefaultDisplay().getHeight() / 2;
        tv = new FloatView(FloatService.this);
        tv.setTextSize(20);
        tv.setText("慢心してはダメ。全力で参りましょう");
        wm.addView(tv, params);
    }

    @Override
    public void onDestroy() {
        WindowManager wm = (WindowManager) getApplicationContext()
                .getSystemService(WINDOW_SERVICE);

        if (tv != null && tv.isShown()) {
            wm.removeView(tv);
        }
        super.onDestroy();
    }

    @Override
    protected void setForeground() {
        showForeground(MainActivity.class, "浮动窗口已打开");
    }
}
