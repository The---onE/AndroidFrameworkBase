package com.xmx.androidframeworkbase.base.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by The_onE on 2016/7/4.
 */
public abstract class BaseFragment extends BFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getContentView(inflater, container);
        initView(view);
        setListener(view);
        processLogic(view, savedInstanceState);

        return view;
    }

    protected abstract View getContentView(LayoutInflater inflater, ViewGroup container);

    protected abstract void initView(View view);

    protected abstract void setListener(View view);

    protected abstract void processLogic(View view, Bundle savedInstanceState);
}
