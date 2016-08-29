package com.xmx.androidframeworkbase.Tools.Float;

import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;

import com.xmx.androidframeworkbase.Float.FloatView;

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

    FloatView layout;

    public void showFloatView(Context context) {
        if (layout == null) {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            WindowManager.LayoutParams params = FloatView.params;

            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
                    | WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;// 设置窗口类型为系统级
            params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;// 设置窗口焦点

            params.width = WindowManager.LayoutParams.WRAP_CONTENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.alpha = 80;

            params.gravity = Gravity.LEFT | Gravity.TOP;
            // 以屏幕左上角为原点，设置x、y初始值,将悬浮窗口设置在屏幕中间的位置
            params.x = 0;
            params.y = wm.getDefaultDisplay().getHeight() / 2;

            layout = new FloatView(context);
            wm.addView(layout, params);
        }
    }

    public void hideFloatView(Context context) {
        if (layout != null && layout.isShown()) {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            wm.removeView(layout);
            layout = null;
        }
    }
}
