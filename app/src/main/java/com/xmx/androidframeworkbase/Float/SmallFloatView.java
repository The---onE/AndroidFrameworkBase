package com.xmx.androidframeworkbase.Float;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;

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
        edgeMode = EDGE_MODE_XY;
    }

    @Override
    public void onTouchStart(MotionEvent event) {

    }

    @Override
    public void onTouchMove(MotionEvent event, long deltaTime,
                            float deltaX, float deltaY, double distance) {

    }

    @Override
    public void onTouchEnd(MotionEvent event, long deltaTime,
                           float deltaX, float deltaY, double distance) {
        if (deltaTime < 200 && distance < 25) {
            FloatViewManager.getInstance().showFloatView(getContext(),
                    new FloatView(getContext()));
        }
    }
}
