package com.xmx.androidframeworkbase.Float;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * Created by The_onE on 2016/8/26.
 */
public class FloatView extends TextView {

    private static final String TAG = FloatView.class.getSimpleName();

    public static WindowManager.LayoutParams params = new WindowManager.LayoutParams();
    private float startX;
    private float startY;
    private float one = 0.0f;
    private float two = 0.01f;

    private WindowManager wm;
    private String text;
    private int statusBarHeight;

    public FloatView(Context context) {
        super(context);
        // handler.post(update);
        wm = (WindowManager) getContext().getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        updateTextThread.start();
        statusBarHeight = getStatusBarHeight();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 触摸点相对于屏幕左上角坐标
        float x = event.getRawX();
        float y = event.getRawY() - statusBarHeight;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                Log.w(TAG, "x::" + startX + ",y::" + startY);
                Log.w(TAG, "rawx::" + x + ",rawy::" + y);
            case MotionEvent.ACTION_UP:
                updatePosition(x - startX, y - startY);
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        float len = getTextSize() * text.length();
		/*
		 * 渲染歌词，前四个参数表示从哪里渲染到哪里，第5个参数为渲染的两种不同颜色， 第6个参数表示渲染的相对位置，范围从0到1 第7个参数表示模式
		 */
        Shader shader = new LinearGradient(0, 0, len, 0, new int[] {
                Color.YELLOW, Color.RED }, new float[] { one, two },
                Shader.TileMode.CLAMP);
        Paint p = new Paint();
        p.setShader(shader);
        p.setTextSize(getTextSize());
        canvas.drawText(text, 0, getTextSize(), p);
    }

    // 通过一个异步线程来控制歌词渲染的速度
    private Thread updateTextThread = new Thread() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            super.run();
            while (true) {
                one += 0.001f;
                two += 0.001f;
                if (two > 1.0) {
                    one = 0.0f;
                    two = 0.01f;
                }
                postInvalidate();
                try {
                    Thread.sleep(10);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        }

    };

    // 更新浮动窗口位置参数
    private void updatePosition(float x, float y) {
        // View的当前位置
        params.x = (int) x;
        params.y = (int) y;
        wm.updateViewLayout(this, params);
    }

    // 获得状态栏高度
    private int getStatusBarHeight() {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
            return 75;
        }
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
