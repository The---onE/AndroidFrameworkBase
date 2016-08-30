package com.xmx.androidframeworkbase.Tools.Float;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import java.lang.reflect.Field;
import java.util.Date;

/**
 * Created by The_onE on 2016/8/27.
 */
public abstract class BaseFloatView extends RelativeLayout {
    protected static WindowManager.LayoutParams params = new WindowManager.LayoutParams();
    protected WindowManager wm;
    private long startTime;

    protected float startX;
    protected float startY;
    protected int statusBarHeight;

    public BaseFloatView(Context context, AttributeSet attrs, int defStyle) {
        super(context);
        wm = (WindowManager) getContext().getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        statusBarHeight = getStatusBarHeight();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 触摸点相对于屏幕左上角坐标
        float x = event.getRawX();
        float y = event.getRawY() - statusBarHeight;

        long now = new Date().getTime();
        float deltaX = event.getX() - startX;
        float deltaY = event.getY() - startY;
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                onTouchStart(event);
                startTime = new Date().getTime();
                break;

            case MotionEvent.ACTION_MOVE:
                updatePosition(x - startX, y - startY);
                onTouchMove(event, now - startTime, deltaX, deltaY, distance);
                break;

            case MotionEvent.ACTION_UP:
                updatePosition(x - startX, y - startY);
                onTouchEnd(event, now - startTime, deltaX, deltaY, distance);
                break;
        }
        return true;
    }

    // 更新浮动窗口位置参数
    protected void updatePosition(float x, float y) {
        // View的当前位置
        params.x = (int) x;
        params.y = (int) y;
        wm.updateViewLayout(this, params);
    }

    // 获得状态栏高度
    private int getStatusBarHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
            return 75;
        }
    }

    abstract public void onTouchStart(MotionEvent event);

    abstract public void onTouchMove(MotionEvent event, long deltaTime,
                                     float deltaX, float deltaY, double distance);

    abstract public void onTouchEnd(MotionEvent event, long deltaTime,
                                    float deltaX, float deltaY, double distance);
}
