package com.xmx.androidframeworkbase.core.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.base.fragment.xUtilsFragment;
import com.xmx.androidframeworkbase.module.net.GetRequestActivity;
import com.xmx.androidframeworkbase.module.net.JsonActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

/**
 * A simple {@link Fragment} subclass.
 */
@ContentView(R.layout.fragment_net)
public class NetFragment extends xUtilsFragment {

    @Event(value = R.id.btn_get)
    private void onGetClick(View view) {
        startActivity(GetRequestActivity.class);
    }

    @Event(value = R.id.btn_json)
    private void onJsonClick(View view) {
        startActivity(JsonActivity.class);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }
}
