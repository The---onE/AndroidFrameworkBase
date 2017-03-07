package com.xmx.androidframeworkbase.module.floatwindow;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;

import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.common.floatwindow.BaseFloatView;
import com.xmx.androidframeworkbase.common.floatwindow.FloatViewManager;

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
    }

    @Override
    public void onLongClick() {

    }

    @Override
    public void onSingleClick() {
        FloatViewManager.getInstance().showFloatView(getContext(),
                new FloatView(getContext()));
    }

    @Override
    public void onDoubleClick() {

    }

    @Override
    public void onTripleClick() {

    }
}
