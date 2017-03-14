package com.xmx.androidframeworkbase.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.xutils.x;

/**
 * Created by The_onE on 2016/7/4.
 * 使用xUtils的Fragment基类，使用注解绑定控件对象
 * 继承本类的Fragment需
 * 1.在Fragment前加@ContentView(R.layout.fragment_file)注解
 * 2.使用@ViewInject(R.id.view_id)注解绑定控件
 * 3.使用@Event(value=R.id.view_id, type=View.EventListener.class)注解绑定控件事件
 * 4.绑定的控件和方法必须为private访问权限
 */
public abstract class xUtilsFragment extends BFragment {

    // 是否已注册xUtils
    private boolean injected = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        injected = true;
        return x.view().inject(this, inflater, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if  (!injected) {
            // 注册xUtils
            x.view().inject(this, this.getView());
        }
        // 处理业务逻辑
        processLogic(savedInstanceState);
    }

    /**
     * 处理业务逻辑接口
     * @param savedInstanceState [onViewCreated]方法中的实例状态
     */
    protected abstract void processLogic(Bundle savedInstanceState);
}
