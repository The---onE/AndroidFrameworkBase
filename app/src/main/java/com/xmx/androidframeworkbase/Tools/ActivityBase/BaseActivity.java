package com.xmx.androidframeworkbase.Tools.ActivityBase;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.xutils.x;

/**
 * Created by The_onE on 2015/12/27.
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        x.view().inject(this);

        TAG = this.getClass().getSimpleName();
        initView(savedInstanceState);
        setListener();
        processLogic(savedInstanceState);
    }

    protected <VT extends View> VT getViewById(@IdRes int id) {
        return (VT) findViewById(id);
    }

    protected abstract void initView(Bundle savedInstanceState);

    protected abstract void setListener();

    protected abstract void processLogic(Bundle savedInstanceState);

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        onViewCreated();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        onViewCreated();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        onViewCreated();
    }

    protected void onViewCreated() {
    }

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
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

    protected void showLog(String tag, String msg) {
        Log.e(tag, msg);
    }

    protected void showLog(String tag, int i) {
        Log.e(tag, "" + i);
    }

    protected void startActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    protected void startActivity(Class<?> cls, String... objs) {
        Intent intent = new Intent(this, cls);
        for (int i = 0; i < objs.length; i++) {
            intent.putExtra(objs[i], objs[++i]);
        }
        startActivity(intent);
    }
}
