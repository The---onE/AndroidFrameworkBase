package com.xmx.androidframeworkbase.module.floatwindow;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by The_onE on 2016/8/28.
 */
public class ScrollTextView extends TextView {

    private String text;

    private float one = 0.0f;
    private float two = 0.01f;

    Paint p = new Paint();

    public ScrollTextView(Context context) {
        this(context, null);
    }
    public ScrollTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        updateTextThread.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float len = getTextSize() * text.length();
        //渲染歌词，前四个参数表示从哪里渲染到哪里，
        // 第5个参数为渲染的两种不同颜色，
        // 第6个参数表示渲染的相对位置，范围从0到1
        // 第7个参数表示模式
        Shader shader = new LinearGradient(0, 0, len, 0,
                new int[]{Color.YELLOW, Color.RED},
                new float[]{one, two},
                Shader.TileMode.CLAMP);
        p.setShader(shader);
        p.setTextSize(getTextSize());
        canvas.drawText(text, 0, getTextSize(), p);
    }

    //通过一个异步线程来控制歌词渲染的速度
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
                    e.printStackTrace();
                }
            }
        }
    };

    public void setText(String text) {
        this.text = text;
    }
}
