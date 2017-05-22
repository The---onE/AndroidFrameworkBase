package com.xmx.androidframeworkbase.module.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.base.dialog.BaseDialog;

/**
 * Created by The_onE on 2017/5/22.
 */

public class TestDialog extends BaseDialog {
    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.dialog_test, container);
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {

    }

    @Override
    protected void setListener(View view) {
        view.findViewById(R.id.btnOK).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast(R.string.ok);
            }
        });

        view.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast(R.string.cancel);
            }
        });
    }

    @Override
    protected void processLogic(View view, Bundle savedInstanceState) {

    }
}
