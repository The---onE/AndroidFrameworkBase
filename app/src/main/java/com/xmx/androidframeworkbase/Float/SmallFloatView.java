package com.xmx.androidframeworkbase.Float;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.xmx.androidframeworkbase.MainActivity;
import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.Tools.Float.BaseFloatView;
import com.xmx.androidframeworkbase.Tools.Float.FloatViewManager;

/**
 * Created by The_onE on 2016/8/30.
 */
public class SmallFloatView extends BaseFloatView {

    public SmallFloatView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SmallFloatView(Context context) {
        this(context, null);
    }

    public SmallFloatView(final Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.layout_small_float, this);

        Button showButton = (Button) findViewById(R.id.btn_show);
        showButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                FloatViewManager.getInstance().showFloatView(context, new FloatView(context));
            }
        });
    }

    @Override
    public void onTouchStart(MotionEvent event) {

    }

    @Override
    public void onTouchEnd(MotionEvent event, long deltaTime) {

    }
}
