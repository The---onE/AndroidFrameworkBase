package com.xmx.androidframeworkbase.core.fragments;


import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;

import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.module.services.MainService;
import com.xmx.androidframeworkbase.base.fragment.xUtilsFragment;
import com.xmx.androidframeworkbase.common.notification.NotificationTempActivity;
import com.xmx.androidframeworkbase.common.notification.NotificationUtils;
import com.xmx.androidframeworkbase.common.log.OperationLogEntityManager;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
@ContentView(R.layout.fragment_notification)
public class NotificationFragment extends xUtilsFragment {

    @ViewInject(R.id.edit_id)
    private EditText idView;

    @ViewInject(R.id.edit_title)
    private EditText titleView;

    @ViewInject(R.id.edit_content)
    private EditText contentView;

    @Event(value = R.id.btn_remind)
    private void onRemindClick(View view) {
        String id = idView.getText().toString();
        int i = id.hashCode();
        String title = titleView.getText().toString();
        String content = contentView.getText().toString();

        Intent intent = new Intent(getContext(), NotificationTempActivity.class);
        intent.putExtra("notificationId", i);

        NotificationUtils.showNotification(getContext(), intent, i, title, content);
        showToast("已发送通知");
    }

    @Event(value = R.id.btn_notification)
    private void onNotificationClick(View view) {
        String id = idView.getText().toString();
        int i = id.hashCode();
        String title = titleView.getText().toString();
        String content = contentView.getText().toString();

        Intent intent = new Intent(getContext(), NotificationTempActivity.class);
        intent.putExtra("notificationId", i);

        NotificationUtils.showNotification(getContext(),
                intent, i, title, content, true, true);
        showToast("已发送持续通知");
    }

    @Event(value = R.id.btn_remove_notification)
    private void onRemoveClick(View view) {
        String id = idView.getText().toString();
        int i = id.hashCode();
        NotificationUtils.removeNotification(getContext(), i);
        showToast("已移除通知");
    }

    @Event(value = R.id.btn_start_service)
    private void onStartServiceClick(View view) {
        Intent service = new Intent(getContext(), MainService.class);
        getContext().startService(service);
        OperationLogEntityManager.getInstance().addLog("开启服务");
        showToast("已开启服务");
    }

    @Event(value = R.id.btn_end_service)
    private void onEndServiceClick(View view) {
        ActivityManager manager =
                (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        int defaultNum = 1000;
        List<ActivityManager.RunningServiceInfo> runServiceList = manager
                .getRunningServices(defaultNum);
        boolean flag = false;
        for (ActivityManager.RunningServiceInfo runServiceInfo : runServiceList) {
            if (runServiceInfo.foreground) {
                if (runServiceInfo.service
                        .getShortClassName().equals(".module.services.MainService")) {
                    Intent intent = new Intent();
                    intent.setComponent(runServiceInfo.service);
                    getContext().stopService(intent);
                    showToast("已关闭服务");
                    flag = true;
                }
            }
        }
        if (!flag) {
            showToast("服务未开启");
        }
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }
}
