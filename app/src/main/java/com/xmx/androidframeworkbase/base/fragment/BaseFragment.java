package com.xmx.androidframeworkbase.base.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by The_onE on 2016/7/4.
 * 基础Fragment，声明业务接口，提供常用功能
 */
public abstract class BaseFragment extends BFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 设置Fragment对应布局
        View view = getContentView(inflater, container);

        return view;
    }

    @Override
    public View onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 初始化View
        initView(view, savedInstanceState);
        // 声明事件监听
        setListener(view);
        // 处理业务逻辑
        processLogic(view, savedInstanceState);

        return view;
    }

    /**
     * 设置Fragment对应布局接口
     * 实现中return inflater.inflate(R.layout.Fragment布局, container, false);
     * @param inflater 用于加载Fragment的布局
     * @param container 存放Fragment的layout的ViewGroup
     * @return Fragment对应的布局
     */
    protected abstract View getContentView(LayoutInflater inflater, ViewGroup container);

    /**
     * 初始化View接口
     * @param view Fragment对应布局的View
     * @param savedInstanceState [onViewCreated]方法中的实例状态
     */
    protected abstract void initView(View view, Bundle savedInstanceState);

    /**
     * 声明事件监听接口
     * @param view Fragment对应布局的View
     */
    protected abstract void setListener(View view);

    /**
     * 处理业务逻辑接口
     * @param view Fragment对应布局的View
     * @param savedInstanceState [onViewCreated]方法中的实例状态
     */
    protected abstract void processLogic(View view, Bundle savedInstanceState);
}
