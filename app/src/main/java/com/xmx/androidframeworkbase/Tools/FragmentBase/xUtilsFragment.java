package com.xmx.androidframeworkbase.Tools.FragmentBase;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.xutils.x;

/**
 * Created by The_onE on 2016/7/4.
 * 继承本类的Fragment需
 * 1.在Fragment前加@ContentView(R.layout.fragment_file)注解
 * 2.使用@ViewInject(R.id.view_id)注解绑定控件
 * 3.使用@Event(value=R.id.view_id, type=View.EventListener.class)注解绑定控件事件
 * 4.绑定的控件和方法必须为private访问权限
 */
public abstract class xUtilsFragment extends BFragment {

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
            x.view().inject(this, this.getView());
        }
        processLogic(savedInstanceState);
    }

    protected abstract void processLogic(Bundle savedInstanceState);
}
