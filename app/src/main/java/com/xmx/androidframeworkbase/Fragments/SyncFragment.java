package com.xmx.androidframeworkbase.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.Sync.Sync;
import com.xmx.androidframeworkbase.Sync.SyncAdapter;
import com.xmx.androidframeworkbase.Sync.SyncEntityManager;
import com.xmx.androidframeworkbase.Sync.SyncManager;
import com.xmx.androidframeworkbase.Tools.Data.DataConstants;
import com.xmx.androidframeworkbase.Tools.Data.Callback.DelCallback;
import com.xmx.androidframeworkbase.Tools.Data.Callback.InsertCallback;
import com.xmx.androidframeworkbase.Tools.Data.Callback.SelectCallback;
import com.xmx.androidframeworkbase.Tools.Data.Callback.UpdateCallback;
import com.xmx.androidframeworkbase.Tools.FragmentBase.xUtilsFragment;

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
@ContentView(R.layout.fragment_sync)
public class SyncFragment extends xUtilsFragment {

    SyncAdapter syncAdapter;

    @ViewInject(R.id.list_sync)
    ListView syncList;

    @ViewInject(R.id.edit_sync)
    EditText text;

    @Event(value = R.id.list_sync, type = ListView.OnItemLongClickListener.class)
    private boolean onSyncLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        final Sync sync = (Sync) syncAdapter.getItem(i);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("要更新该记录吗？");
        builder.setTitle("提示");
        builder.setNegativeButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SyncEntityManager.getInstance().deleteData(sync.mCloudId, new DelCallback() {
                    @Override
                    public void success(AVObject user) {
                        showToast(R.string.delete_success);
                        SyncManager.getInstance().updateData();
                        syncAdapter.updateList();
                    }

                    @Override
                    public void syncError(int error) {
                        switch (error) {
                            case DataConstants.NOT_INIT:
                                showToast(R.string.failure);
                                break;
                            case DataConstants.NOT_LOGGED_IN:
                                showToast(R.string.not_loggedin);
                                break;
                            case DataConstants.USERNAME_ERROR:
                                showToast(R.string.username_error);
                                break;
                            case DataConstants.CHECKSUM_ERROR:
                                showToast(R.string.not_loggedin);
                                break;
                        }
                    }

                    @Override
                    public void syncError(AVException e) {
                        showToast(R.string.sync_failure);
                        filterException(e);
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
                            public void success(AVObject user) {
                                showToast(R.string.update_success);
                                SyncManager.getInstance().updateData();
                                syncAdapter.updateList();
                            }

                            @Override
                            public void syncError(int error) {
                                switch (error) {
                                    case DataConstants.NOT_INIT:
                                        showToast(R.string.failure);
                                        break;
                                    case DataConstants.NOT_LOGGED_IN:
                                        showToast(R.string.not_loggedin);
                                        break;
                                    case DataConstants.USERNAME_ERROR:
                                        showToast(R.string.username_error);
                                        break;
                                    case DataConstants.CHECKSUM_ERROR:
                                        showToast(R.string.not_loggedin);
                                        break;
                                }
                            }

                            @Override
                            public void syncError(AVException e) {
                                showToast(R.string.sync_failure);
                                filterException(e);
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
            public void success(AVObject user, String objectId) {
                showToast(R.string.add_success);
                SyncManager.getInstance().updateData();
                syncAdapter.updateList();
            }

            @Override
            public void syncError(int error) {
                switch (error) {
                    case DataConstants.NOT_INIT:
                        showToast(R.string.failure);
                        break;
                    case DataConstants.NOT_LOGGED_IN:
                        showToast(R.string.not_loggedin);
                        break;
                    case DataConstants.USERNAME_ERROR:
                        showToast(R.string.username_error);
                        break;
                    case DataConstants.CHECKSUM_ERROR:
                        showToast(R.string.not_loggedin);
                        break;
                }
            }

            @Override
            public void syncError(AVException e) {
                showToast(R.string.sync_failure);
                filterException(e);
            }
        });
        text.setText("");
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        SyncManager.getInstance().updateData();
        syncAdapter = new SyncAdapter(getContext());
        syncList.setAdapter(syncAdapter);

        SyncEntityManager.getInstance().syncFromCloud(null,
                new SelectCallback<Sync>() {
                    @Override
                    public void success(AVObject user, List<Sync> syncs) {
                        SyncManager.getInstance().updateData();
                        syncAdapter.updateList();
                        showToast(R.string.sync_success);
                    }

                    @Override
                    public void syncError(int error) {
                        switch (error) {
                            case DataConstants.NOT_INIT:
                                showToast(R.string.failure);
                                break;
                            case DataConstants.NOT_LOGGED_IN:
                                showToast(R.string.not_loggedin);
                                break;
                            case DataConstants.USERNAME_ERROR:
                                showToast(R.string.username_error);
                                break;
                            case DataConstants.CHECKSUM_ERROR:
                                showToast(R.string.not_loggedin);
                                break;
                        }
                    }

                    @Override
                    public void syncError(AVException e) {
                        showToast(R.string.sync_failure);
                        filterException(e);
                    }
                });
    }
}
