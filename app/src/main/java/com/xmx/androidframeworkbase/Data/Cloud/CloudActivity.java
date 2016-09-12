package com.xmx.androidframeworkbase.Data.Cloud;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.Tools.ActivityBase.BaseTempActivity;
import com.xmx.androidframeworkbase.Tools.Data.DataConstants;
import com.xmx.androidframeworkbase.Tools.Data.Callback.DelCallback;
import com.xmx.androidframeworkbase.Tools.Data.Callback.InsertCallback;
import com.xmx.androidframeworkbase.Tools.Data.Callback.SelectCallback;
import com.xmx.androidframeworkbase.Tools.Data.Callback.UpdateCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class CloudActivity extends BaseTempActivity {

    CloudAdapter cloudAdapter;
    ListView cloudList;
    Button add;
    EditText text;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_cloud);

        cloudList = getViewById(R.id.list_cloud);
        cloudAdapter = new CloudAdapter(this, new ArrayList<Cloud>());
        cloudList.setAdapter(cloudAdapter);

        text = getViewById(R.id.edit_cloud);
        add = getViewById(R.id.btn_cloud);
    }

    @Override
    protected void setListener() {
        cloudList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Cloud cloud = (Cloud) cloudAdapter.getItem(i);

                AlertDialog.Builder builder = new AlertDialog
                        .Builder(CloudActivity.this, R.style.AppTheme);
                builder.setMessage("要更新该记录吗？");
                builder.setTitle("提示");
                builder.setNegativeButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        CloudEntityManager.getInstance().deleteFromCloud(cloud.mCloudId, new DelCallback() {
                            @Override
                            public void success(AVObject user) {
                                showToast(R.string.delete_success);
                                updateList();
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
                        CloudEntityManager.getInstance().updateToCloud(cloud.mCloudId, update,
                                new UpdateCallback() {
                                    @Override
                                    public void success(AVObject user) {
                                        showToast(R.string.update_success);
                                        updateList();
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
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = text.getText().toString();
                Cloud entity = new Cloud();
                entity.mData = data;
                entity.mTime = new Date();
                CloudEntityManager.getInstance().insertToCloud(entity, new InsertCallback() {
                    @Override
                    public void success(AVObject user, String objectId) {
                        showToast(R.string.add_success);
                        updateList();
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
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        updateList();
    }

    private void updateList() {
        CloudEntityManager.getInstance().selectByCondition(null,
                "Time", false,
                new SelectCallback<Cloud>() {
                    @Override
                    public void success(AVObject user, List<Cloud> clouds) {
                        cloudAdapter.updateList(clouds);
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
