package com.xmx.androidframeworkbase.common.floatwindow;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.Date;

/**
 * Created by The_onE on 2016/8/27.
 */
public abstract class BaseFloatView extends RelativeLayout {
    protected static WindowManager.LayoutParams params = new WindowManager.LayoutParams();
    protected WindowManager wm;
    private long startTime;

    //起始触摸点相对布局左上角坐标
    protected Coordinate start = new Coordinate();
    protected int statusBarHeight;

    //起始触摸点相对屏幕左上角坐标
    protected Coordinate startRaw = new Coordinate();

    protected static final int EDGE_MODE_NO = 0;
    protected static final int EDGE_MODE_X = 1;
    protected static final int EDGE_MODE_Y = 2;
    protected static final int EDGE_MODE_XY = 3;
    protected int edgeMode = EDGE_MODE_NO;
    protected boolean edgeStrict = false;

    protected static final int CLICK_DISTANCE = 25;
    protected static final long LONG_CLICK_TIME = 500;
    protected static final long DOUBLE_CLICK_TIME = 300;
    protected static final long TRIPLE_CLICK_TIME = 300;

    private boolean doubleClickFlag = false;
    private boolean tripleClickFlag = false;

    public BaseFloatView(Context context, AttributeSet attrs, int defStyle) {
        super(context);
        wm = (WindowManager) getContext().getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        statusBarHeight = getStatusBarHeight();
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updatePosition();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        long now = new Date().getTime();

        //触摸点相对于屏幕左上角坐标
        Coordinate raw = new Coordinate();
        raw.x = event.getRawX();
        raw.y = event.getRawY() - statusBarHeight;

        //触摸点相对起始点的偏移
        Coordinate delta = raw.sub(startRaw);
        double distance = delta.distance();

        //布局左上角应在的位置
        Coordinate pos = raw.sub(start);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //起始触摸点相对布局左上角坐标
                start.x = event.getX();
                start.y = event.getY();
                startRaw.x = raw.x;
                startRaw.y = raw.y;
                startTime = new Date().getTime();
                onTouchStart(event);
                break;

            case MotionEvent.ACTION_MOVE:
                if (edgeStrict) {
                    pos = getCoordinateNearEdge(pos);
                }
                updatePosition(pos);
                onTouchMove(event, now - startTime, delta.x, delta.y, distance);
                break;

            case MotionEvent.ACTION_UP:
                pos = getCoordinateNearEdge(pos);
                updatePosition(pos);
                long deltaTime = now - startTime;
                onTouchEnd(event, deltaTime, delta.x, delta.y, distance);
                if (deltaTime > LONG_CLICK_TIME && distance < CLICK_DISTANCE) { //长按
                    onLongClick();
                } else if (deltaTime > 0 && distance < CLICK_DISTANCE) { //短按
                    if (!doubleClickFlag && !tripleClickFlag) { //首次短按
                        doubleClickFlag = true; //等待第二次按键
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (doubleClickFlag) { //超时未按下第二次
                                    //执行单按逻辑
                                    onSingleClick();
                                }
                                doubleClickFlag = false;
                            }
                        }, DOUBLE_CLICK_TIME);
                    } else if (!tripleClickFlag){ //双按
                        doubleClickFlag = false;
                        tripleClickFlag = true; //等待第三次按键
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (tripleClickFlag) { //超时未按下第三次
                                    //执行双按逻辑
                                    onDoubleClick();
                                }
                                tripleClickFlag = false;
                            }
                        }, TRIPLE_CLICK_TIME);
                    } else { //三按
                        //执行三按逻辑
                        onTripleClick();
                        doubleClickFlag = false;
                        tripleClickFlag = false;
                    }
                }
                break;
        }
        return true;
    }

    // 更新浮动窗口位置参数
    public void updatePosition() {
        float x = params.x;
        float y = params.y;
        Coordinate co = getCoordinateNearEdge(new Coordinate(x, y));
        updatePosition(co);
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

    Coordinate getCoordinateNearEdge(Coordinate source) {
        Coordinate pos = new Coordinate(source);

        Coordinate leftTop = new Coordinate(source);

        Coordinate rightTop = new Coordinate(source);
        rightTop.x += getWidth();

        Coordinate leftBottom = new Coordinate(source);
        leftBottom.y += getHeight();

        Coordinate rightBottom = new Coordinate(source);
        rightBottom.x += getWidth();
        rightBottom.y += getHeight();

        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight() - statusBarHeight;

        switch (edgeMode) {
            case EDGE_MODE_X:
                if (leftTop.x < width - rightTop.x) {
                    pos.x = 0;
                } else {
                    pos.x = width;
                }
                break;
            case EDGE_MODE_Y:
                if (leftTop.y < height - leftBottom.y) {
                    pos.y = 0;
                } else {
                    pos.y = height;
                }
                break;
            case EDGE_MODE_XY:
                float dx = Math.min(leftTop.x, width - rightTop.x);
                float dy = Math.min(leftTop.y, height - leftBottom.y);
                if (dx < dy) {
                    if (leftTop.x < width - rightTop.x) {
                        pos.x = 0;
                    } else {
                        pos.x = width;
                    }
                } else {
                    if (leftTop.y < height - leftBottom.y) {
                        pos.y = 0;
                    } else {
                        pos.y = height;
                    }
                }
                break;
        }
        return pos;
    }

    protected <VT extends View> VT getViewById(@IdRes int id) {
        return (VT) findViewById(id);
    }

    protected void showToast(String str) {
        Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(int resId) {
        Toast.makeText(getContext(), resId, Toast.LENGTH_SHORT).show();
    }

    protected void showLog(String tag, String msg) {
        Log.e(tag, msg);
    }

    protected void showLog(String tag, int i) {
        Log.e(tag, "" + i);
    }

    protected void startActivity(Class<?> cls) {
        Intent intent = new Intent(getContext(), cls);
        getContext().startActivity(intent);
    }

    protected void startActivity(Class<?> cls, String... objs) {
        Intent intent = new Intent(getContext(), cls);
        for (int i = 0; i < objs.length; i++) {
            intent.putExtra(objs[i], objs[++i]);
        }
        getContext().startActivity(intent);
    }

    abstract public void onTouchStart(MotionEvent event);

    abstract public void onTouchMove(MotionEvent event, long deltaTime,
                                     float deltaX, float deltaY, double distance);

    abstract public void onTouchEnd(MotionEvent event, long deltaTime,
                                    float deltaX, float deltaY, double distance);

    abstract public void onLongClick();

    abstract public void onSingleClick();

    abstract public void onDoubleClick();

    abstract public void onTripleClick();
}
