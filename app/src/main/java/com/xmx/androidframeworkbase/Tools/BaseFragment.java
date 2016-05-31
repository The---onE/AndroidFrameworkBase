package com.xmx.androidframeworkbase.Tools;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by The_onE on 2016/3/17.
 */
public abstract class BaseFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getContentView(inflater, container);
        initView(view);
        setListener();
        processLogic(savedInstanceState);

        return view;
    }

    protected abstract View getContentView(LayoutInflater inflater, ViewGroup container);

    protected abstract void initView(View view);

    protected abstract void setListener();

    protected abstract void processLogic(Bundle savedInstanceState);

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
