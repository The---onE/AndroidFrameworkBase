package com.xmx.androidframeworkbase.base.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.xmx.androidframeworkbase.core.Application;
import com.xmx.androidframeworkbase.core.Constants;
import com.xmx.androidframeworkbase.common.log.OperationLogEntityManager;
import com.xmx.androidframeworkbase.utils.StrUtil;

import org.xutils.x;

/**
 * Created by The_onE on 2015/12/27.
 * Activity基类，声明业务接口，提供常用功能
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 注册xUtils
        x.view().inject(this);

        // 在Application中集中管理
        TAG = this.getClass().getSimpleName();
        Application.getInstance().addActivity(this);

        // 初始化View
        initView(savedInstanceState);
        // 声明事件监听
        setListener();
        // 处理业务逻辑
        processLogic(savedInstanceState);
    }

    /**
     * 获取控件对象
     *
     * @param id   控件ID
     * @param <VT> 控件类型
     * @return 控件对象
     */
    protected <VT extends View> VT getViewById(@IdRes int id) {
        return (VT) findViewById(id);
    }

    /**
     * 初始化View接口
     *
     * @param savedInstanceState [onCreate]方法中的实例状态
     */
    protected abstract void initView(Bundle savedInstanceState);

    /**
     * 声明事件监听接口
     */
    protected abstract void setListener();

    /**
     * 处理业务逻辑接口
     *
     * @param savedInstanceState [onCreate]方法中的实例状态
     */
    protected abstract void processLogic(Bundle savedInstanceState);

    /**
     * 设置Activity对应layout
     *
     * @param layoutResID Activity对应的layout文件ID
     */
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        onViewCreated();
    }

    /**
     * 设置Activity对应layout
     *
     * @param view Activity对应的View
     */
    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        onViewCreated();
    }

    /**
     * 设置Activity对应layout
     *
     * @param view   Activity对应的View
     * @param params view的属性
     */
    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        onViewCreated();
    }

    /**
     * 设置layout文件后的默认处理
     */
    protected void onViewCreated() {
    }

    /**
     * 显示提示信息
     *
     * @param str 要显示的字符串信息
     */
    protected void showToast(String str) {
        StrUtil.showToast(this, str);
    }

    /**
     * 显示提示信息
     *
     * @param resId 要显示的字符串在strings文件中的ID
     */
    protected void showToast(int resId) {
        StrUtil.showToast(this, resId);
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

    /**
     * 启动新Activity
     *
     * @param cls 要启动的Activity类
     */
    protected void startActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    /**
     * 带参数启动新Activity
     *
     * @param cls  要启动的Activity类
     * @param objs 向Activity传递的参数，奇数项为键，偶数项为值
     */
    protected void startActivity(Class<?> cls, String... objs) {
        Intent intent = new Intent(this, cls);
        for (int i = 0; i < objs.length; i++) {
            intent.putExtra(objs[i], objs[++i]);
        }
        startActivity(intent);
    }
}
