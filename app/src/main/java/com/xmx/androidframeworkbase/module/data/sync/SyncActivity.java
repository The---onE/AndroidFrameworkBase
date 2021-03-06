package com.xmx.androidframeworkbase.module.data.sync;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.avos.avoscloud.AVException;
import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.base.activity.BaseTempActivity;
import com.xmx.androidframeworkbase.common.data.callback.SelectLoginCallback;
import com.xmx.androidframeworkbase.common.data.callback.DelCallback;
import com.xmx.androidframeworkbase.common.data.callback.InsertCallback;
import com.xmx.androidframeworkbase.common.data.callback.UpdateCallback;
import com.xmx.androidframeworkbase.common.user.UserData;
import com.xmx.androidframeworkbase.utils.ExceptionUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xmx on 2016/6/1.
 */
@ContentView(R.layout.activity_sync)
public class SyncActivity extends BaseTempActivity {

    SyncAdapter syncAdapter;

    @ViewInject(R.id.list_sync)
    ListView syncList;

    @ViewInject(R.id.edit_sync)
    EditText text;

    @Event(value = R.id.list_sync, type = ListView.OnItemLongClickListener.class)
    private boolean onSyncLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        final Sync sync = (Sync) syncAdapter.getItem(i);

        AlertDialog.Builder builder = new AlertDialog
                .Builder(SyncActivity.this);
        builder.setMessage("要更新该记录吗？");
        builder.setTitle("提示");
        builder.setNegativeButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SyncEntityManager.getInstance().deleteData(sync.mCloudId, new DelCallback() {
                    @Override
                    public void success(UserData user) {
                        showToast(R.string.delete_success);
                        SyncManager.getInstance().updateData();
                        syncAdapter.updateList(SyncManager.getInstance().getData());
                    }

                    @Override
                    public void syncError(int error) {
                        SyncEntityManager.defaultError(error, getBaseContext());
                    }

                    @Override
                    public void syncError(AVException e) {
                        showToast(R.string.sync_failure);
                        ExceptionUtil.normalException(e, getBaseContext());
                    }
                });
            }
        });
        builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Map<String, Object> update = new HashMap<>();
                update.put("Time", new Date());
                SyncEntityManager.getInstance().updateData(sync.mCloudId, update,
                        new UpdateCallback() {
                            @Override
                            public void success(UserData user) {
                                showToast(R.string.update_success);
                                SyncManager.getInstance().updateData();
                                syncAdapter.updateList(SyncManager.getInstance().getData());
                            }

                            @Override
                            public void syncError(int error) {
                                SyncEntityManager.defaultError(error, getBaseContext());
                            }

                            @Override
                            public void syncError(AVException e) {
                                showToast(R.string.sync_failure);
                                ExceptionUtil.normalException(e, getBaseContext());
                            }
                        });
            }
        });
        builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
        return false;
    }

    @Event(value = R.id.btn_sync)
    private void onAddClick(View view) {
        String data = text.getText().toString();
        final Sync entity = new Sync();
        entity.mData = data;
        entity.mTime = new Date();
        SyncEntityManager.getInstance().insertData(entity, new InsertCallback() {
            @Override
            public void success(UserData user, String objectId) {
                showToast(R.string.add_success);
                SyncManager.getInstance().updateData();
                syncAdapter.updateList(SyncManager.getInstance().getData());
            }

            @Override
            public void syncError(int error) {
                SyncEntityManager.defaultError(error, getBaseContext());
            }

            @Override
            public void syncError(AVException e) {
                showToast(R.string.sync_failure);
                ExceptionUtil.normalException(e, getBaseContext());
            }
        });
        text.setText("");
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        SyncManager.getInstance().updateData();
        syncAdapter = new SyncAdapter(this, SyncManager.getInstance().getData());
        syncList.setAdapter(syncAdapter);

        SyncEntityManager.getInstance().syncFromCloud(null,
                new SelectLoginCallback<Sync>() {
                    @Override
                    public void success(UserData user, List<Sync> syncs) {
                        SyncManager.getInstance().updateData();
                        syncAdapter.updateList(SyncManager.getInstance().getData());
                        showToast(R.string.sync_success);
                    }

                    @Override
                    public void syncError(int error) {
                        SyncEntityManager.defaultError(error, getBaseContext());
                    }

                    @Override
                    public void syncError(AVException e) {
                        showToast(R.string.sync_failure);
                        ExceptionUtil.normalException(e, getBaseContext());
                    }
                });
    }
}
