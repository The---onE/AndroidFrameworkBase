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

    protected Coordinate start = new Coordinate();
    protected int statusBarHeight;

    protected static final int EDGE_MODE_NO = 0;
    protected static final int EDGE_MODE_X = 1;
    protected static final int EDGE_MODE_Y = 2;
    protected static final int EDGE_MODE_XY = 3;
    protected int EDGE_MODE = EDGE_MODE_NO;

    public BaseFloatView(Context context, AttributeSet attrs, int defStyle) {
        super(context);
        wm = (WindowManager) getContext().getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        statusBarHeight = getStatusBarHeight();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 触摸点相对于屏幕左上角坐标
        Coordinate raw = new Coordinate();
        raw.x = event.getRawX();
        raw.y = event.getRawY() - statusBarHeight;

        Coordinate coo = new Coordinate();
        coo.x = event.getX();
        coo.y = event.getY();

        long now = new Date().getTime();
        Coordinate delta = coo.sub(start);
        double distance = delta.distance();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                start.x = event.getX();
                start.y = event.getY();
                startTime = new Date().getTime();
                onTouchStart(event);
                break;

            case MotionEvent.ACTION_MOVE:
                updatePosition(raw.sub(start));
                onTouchMove(event, now - startTime, delta.x, delta.y, distance);
                break;

            case MotionEvent.ACTION_UP:
                Coordinate change = raw.sub(start);

                switch (EDGE_MODE) {
                    case EDGE_MODE_X:
                        if (change.x < wm.getDefaultDisplay().getWidth() / 2) {
                            change.x = 0;
                        } else {
                            change.x = wm.getDefaultDisplay().getWidth();
                        }
                        break;
                    case EDGE_MODE_Y:
                        if (change.y < wm.getDefaultDisplay().getHeight() / 2) {
                            change.y = 0;
                        } else {
                            change.y = wm.getDefaultDisplay().getHeight();
                        }
                        break;
                    case EDGE_MODE_XY:
                        float dx = Math.min(raw.x, wm.getDefaultDisplay().getWidth() - raw.x);
                        float dy = Math.min(raw.y, wm.getDefaultDisplay().getHeight() - raw.y);
                        if (dx < dy) {
                            if (change.x < wm.getDefaultDisplay().getWidth() / 2) {
                                change.x = 0;
                            } else {
                                change.x = wm.getDefaultDisplay().getWidth();
                            }
                        } else {
                            if (change.y < wm.getDefaultDisplay().getHeight() / 2) {
                                change.y = 0;
                            } else {
                                change.y = wm.getDefaultDisplay().getHeight();
                            }
                        }
                        break;
                }
                updatePosition(change);
                onTouchEnd(event, now - startTime, delta.x, delta.y, distance);
                break;
        }
        return true;
    }

    // 更新浮动窗口位置参数
    protected void updatePosition(Coordinate coordinate) {
        // View的当前位置
        params.x = (int) coordinate.x;
        params.y = (int) coordinate.y;
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
