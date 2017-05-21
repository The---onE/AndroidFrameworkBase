package com.xmx.androidframeworkbase.base.dialog;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xmx.androidframeworkbase.utils.StrUtil;

/**
 * Created by The_onE on 2017/5/19.
 * Dialog基类，声明业务接口，提供常用功能
 */

public abstract class BaseDialog extends DialogFragment {
    protected Context mContext;

    /**
     * 初始化Dialog，可在子类中重载
     * @param context 当前上下文
     */
    public void initDialog(Context context) {
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return getContentView(inflater, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mContext == null) {
            showToast("请先初始化Dialog");
            return;
        }
        // 初始化View
        initView(view, savedInstanceState);
        // 声明事件监听
        setListener(view);
        // 处理业务逻辑
        processLogic(view, savedInstanceState);
    }

    /**
     * 设置Fragment对应布局接口
     * 实现中return inflater.inflate(R.layout.Dialog布局, container);
     *
     * @param inflater  用于加载Fragment的布局
     * @param container 存放Fragment的layout的ViewGroup
     * @return Fragment对应的布局
     */
    protected abstract View getContentView(LayoutInflater inflater, ViewGroup container);

    /**
     * 初始化View接口
     *
     * @param view               Fragment对应布局的View
     * @param savedInstanceState [onViewCreated]方法中的实例状态
     */
    protected abstract void initView(View view, Bundle savedInstanceState);

    /**
     * 声明事件监听接口
     *
     * @param view Fragment对应布局的View
     */
    protected abstract void setListener(View view);

    /**
     * 处理业务逻辑接口
     *
     * @param view               Fragment对应布局的View
     * @param savedInstanceState [onViewCreated]方法中的实例状态
     */
    protected abstract void processLogic(View view, Bundle savedInstanceState);

    /**
     * 显示提示信息
     *
     * @param str 要显示的字符串信息
     */
    protected void showToast(String str) {
        StrUtil.showToast(mContext, str);
    }

    /**
     * 显示提示信息
     *
     * @param resId 要显示的字符串在strings文件中的ID
     */
    protected void showToast(int resId) {
        StrUtil.showToast(mContext, resId);
    }

    /**
     * 打印日志
     *
     * @param tag 日志标签
     * @param msg 日志信息
     */
    protected void showLog(String tag, String msg) {
        StrUtil.showLog(tag, msg);
    }

    /**
     * 打印日志
     *
     * @param tag 日志标签
     * @param i   数字作为日志信息
     */
    protected void showLog(String tag, int i) {
        StrUtil.showLog(tag, i);
    }
}
