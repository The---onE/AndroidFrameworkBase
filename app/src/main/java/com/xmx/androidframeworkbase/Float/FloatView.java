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

/**
 * Created by The_onE on 2016/8/26.
 */
public class FloatView extends BaseFloatView {

    ScrollTextView floatText;
    Button floatButton;

    public FloatView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatView(Context context) {
        this(context, null);
    }

    public FloatView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.layout_float, this);
        floatText = (ScrollTextView) findViewById(R.id.tv_float);
        floatText.setText("慢心してはダメ。全力で参りましょう");

        floatButton = (Button) findViewById(R.id.btn_float);
        floatButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            }
        });
    }

    @Override
    public void onTouchStart(MotionEvent event) {

    }

    @Override
    public void onTouchEnd(MotionEvent event, long deltaTime) {

    }

    public void setText(String text) {
        floatText.setText(text);
    }
}
