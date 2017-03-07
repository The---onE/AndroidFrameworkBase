package com.xmx.androidframeworkbase.common.floatwindow;

import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;

/**
 * Created by The_onE on 2016/8/29.
 */
public class FloatViewManager {
    private static FloatViewManager instance;

    public synchronized static FloatViewManager getInstance() {
        if (null == instance) {
            instance = new FloatViewManager();
        }
        return instance;
    }

    BaseFloatView layout;
    boolean floatFlag = false;

    Coordinate pos = new Coordinate();

    public void showFloatView(Context context, BaseFloatView view) {
        if (floatFlag) {
            hideFloatView(context);
        }

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams params = BaseFloatView.params;

        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
                | WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;// 设置窗口类型为系统级
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;// 设置窗口焦点

        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.alpha = 80;

        params.gravity = Gravity.LEFT | Gravity.TOP;
        // 以屏幕左上角为原点，设置x、y初始值
        params.x = (int) pos.x;
        params.y = (int) pos.y;

        layout = view;
        wm.addView(layout, params);
        floatFlag = true;
        layout.updatePosition();
    }

    public void hideFloatView(Context context) {
        if (floatFlag && layout != null && layout.isShown()) {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            pos = new Coordinate(BaseFloatView.params.x, BaseFloatView.params.y);
            wm.removeView(layout);
            layout = null;
            floatFlag = false;
        }
    }
}
