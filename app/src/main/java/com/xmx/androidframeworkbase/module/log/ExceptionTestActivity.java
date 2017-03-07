package com.xmx.androidframeworkbase.module.log;

import android.os.Bundle;
import android.view.View;

import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.base.activity.BaseTempActivity;

/**
 * Created by The_onE on 2016/10/3.
 */
public class ExceptionTestActivity extends BaseTempActivity {
    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_exception_test);
    }

    @Override
    protected void setListener() {
        getViewById(R.id.btn_divide_by_zero).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int e = 1 / 0;
            }
        });

        getViewById(R.id.btn_null_pointer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String e = null;
                e.contains("e");
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }
}
