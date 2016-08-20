package com.xmx.androidframeworkbase.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.xmx.androidframeworkbase.Data.Cloud.CloudActivity;
import com.xmx.androidframeworkbase.Data.Sync.SyncActivity;
import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.Data.SQL.SQLActivity;
import com.xmx.androidframeworkbase.Tools.FragmentBase.xUtilsFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

/**
 * A simple {@link Fragment} subclass.
 */
@ContentView(R.layout.fragment_data)
public class DataFragment extends xUtilsFragment {

    @Event(value = R.id.btn_sql)
    private void onSQLClick(View view) {
        startActivity(SQLActivity.class);
    }

    @Event(value = R.id.btn_cloud)
    private void onCloudClick(View view) {
        startActivity(CloudActivity.class);
    }

    @Event(value = R.id.btn_sync)
    private void onSyncClick(View view) {
        startActivity(SyncActivity.class);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }
}
