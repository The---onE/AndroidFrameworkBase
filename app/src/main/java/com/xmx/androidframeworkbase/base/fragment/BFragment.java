package com.xmx.androidframeworkbase.base.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.xmx.androidframeworkbase.core.Constants;
import com.xmx.androidframeworkbase.utils.StrUtil;

/**
 * Created by The_onE on 2016/3/17.
 * Fragment基类，提供基础Fragment和xUtilsFragment的基础功能
 */
public abstract class BFragment extends Fragment {

    /**
     * 显示提示信息
     * @param str 要显示的字符串信息
     */
    protected void showToast(String str) {
        StrUtil.showToast(getContext(), str);
    }

    /**
     * 显示提示信息
     * @param resId 要显示的字符串在strings文件中的ID
     */
    protected void showToast(int resId) {
        StrUtil.showToast(getContext(), resId);
    }

    /**
     * 打印日志
     * @param tag 日志标签
     * @param msg 日志信息
     */
    protected void showLog(String tag, String msg) {
        StrUtil.showLog(tag, msg);
    }

    /**
     * 打印日志
     * @param tag 日志标签
     * @param i 数字作为日志信息
     */
    protected void showLog(String tag, int i) {
        StrUtil.showLog(tag, i);
    }

    /**
     * 启动新Activity
     * @param cls 要启动的Activity类
     */
    protected void startActivity(Class<?> cls) {
        Intent intent = new Intent(getContext(), cls);
        startActivity(intent);
    }

    /**
     * 带参数启动新Activity
     * @param cls 要启动的Activity类
     * @param objs 向Activity传递的参数，奇数项为键，偶数项为值
     */
    protected void startActivity(Class<?> cls, String... objs) {
        Intent intent = new Intent(getContext(), cls);
        for (int i = 0; i < objs.length; i++) {
            intent.putExtra(objs[i], objs[++i]);
        }
        startActivity(intent);
    }
}
