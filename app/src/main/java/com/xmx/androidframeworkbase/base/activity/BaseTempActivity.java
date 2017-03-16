package com.xmx.androidframeworkbase.base.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.xmx.androidframeworkbase.R;

/**
 * Created by The_onE on 2016/1/1.
 * 临时Activity基类，增加右滑关闭功能，工具栏带有返回按钮
 * 布局文件中需添加 <include layout="@layout/tool_bar" />
 * AndroidManifest.xml中对应的Activity需添加 android:theme="@style/AppTheme.NoActionBar"
 */
public abstract class BaseTempActivity extends BaseActivity {
    // 手势识别
    GestureDetector mGestureDetector;

    private class TempOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        // 添加右滑关闭功能
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (velocityX > Math.abs(velocityY)) {
                onBackPressed();
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

    /**
     * 触摸时添加对手势的识别
     *
     * @param event 触摸事件
     * @return true:事件已处理完成，不向下传递 false:事件未处理完成，向下传递
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 初始化工具栏
        // 布局中必须要有 <include layout="@layout/tool_bar" />
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // 工具栏添加返回按钮
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mGestureDetector = new GestureDetector(this, new TempOnGestureListener());
    }
}
