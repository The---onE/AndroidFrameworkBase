package com.xmx.androidframeworkbase.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.Tools.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class TestFragment extends BaseFragment {

    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_test, container, false);
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void setListener(View view) {

    }

    @Override
    protected void processLogic(View view, Bundle savedInstanceState) {

    }

}
