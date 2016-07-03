package com.xmx.androidframeworkbase.Tools.FragmentBase;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.xutils.x;

/**
 * Created by The_onE on 2016/3/17.
 */
public abstract class BaseFragment extends Fragment {

    //要使用xUtils框架需设置该标记为真，并在Fragment前加@ContentView(R.layout.layout_name)注解
    //使用xUtils框架后控件必须由@ViewInject(R.id.widgets_id)注解绑定
    //使用xUtils框架后事件必须由@Event(value=R.id.widgets_id, type=View.EventListener.class)注解绑定
    protected boolean xUtilsFlag = false;
    private boolean injected = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getContentView(inflater, container);
        initView(view);
        setListener(view);
        processLogic(view, savedInstanceState);

        if (xUtilsFlag) {
            injected = true;
            return x.view().inject(this, inflater, container);
        } else {
            return view;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (xUtilsFlag && !injected) {
            x.view().inject(this, this.getView());
        }
    }

    protected abstract View getContentView(LayoutInflater inflater, ViewGroup container);

    protected abstract void initView(View view);

    protected abstract void setListener(View view);

    protected abstract void processLogic(View view, Bundle savedInstanceState);

    protected boolean filterException(Exception e) {
        if (e != null) {
            e.printStackTrace();
            showToast(e.getMessage());
            return false;
        } else {
            return true;
        }
    }

    protected void showToast(String str) {
        Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(int resId) {
        Toast.makeText(getContext(), resId, Toast.LENGTH_SHORT).show();
    }

    protected void showLog(String tag, String msg) {
        Log.e(tag, msg);
    }

    protected void showLog(String tag, int i) {
        Log.e(tag, "" + i);
    }

    protected void startActivity(Class<?> cls) {
        Intent intent = new Intent(getContext(), cls);
        startActivity(intent);
    }

    protected void startActivity(Class<?> cls, String... objs) {
        Intent intent = new Intent(getContext(), cls);
        for (int i = 0; i < objs.length; i++) {
            intent.putExtra(objs[i], objs[++i]);
        }
        startActivity(intent);
    }
}
