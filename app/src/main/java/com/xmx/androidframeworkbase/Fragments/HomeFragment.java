package com.xmx.androidframeworkbase.Fragments;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.Services.MainService;
import com.xmx.androidframeworkbase.Tools.FragmentBase.BaseFragment;
import com.xmx.androidframeworkbase.Tools.Notification.NotificationTempActivity;
import com.xmx.androidframeworkbase.Tools.Notification.NotificationUtils;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment {

    EditText idView;
    EditText titleView;
    EditText contentView;

    Intent service = null;

    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    protected void initView(View view) {
        idView = (EditText) view.findViewById(R.id.edit_id);
        titleView = (EditText) view.findViewById(R.id.edit_title);
        contentView = (EditText) view.findViewById(R.id.edit_content);
    }

    @Override
    protected void setListener(View view) {
        Button remind = (Button) view.findViewById(R.id.btn_remind);
        remind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = idView.getText().toString();
                int i = id.hashCode();
                String title = titleView.getText().toString();
                String content = contentView.getText().toString();

                Intent intent = new Intent(getContext(), NotificationTempActivity.class);
                intent.putExtra("notificationId", i);

                NotificationUtils.showNotification(getContext(), intent, i, title, content);
                showToast("已发送通知");
            }
        });

        Button notification = (Button) view.findViewById(R.id.btn_notification);
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        });

        Button remove = (Button) view.findViewById(R.id.btn_remove_notification);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = idView.getText().toString();
                int i = id.hashCode();
                NotificationUtils.removeNotification(getContext(), i);
                showToast("已移除通知");
            }
        });

        Button startService = (Button) view.findViewById(R.id.btn_start_service);
        startService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                service = new Intent(getContext(), MainService.class);
                getContext().startService(service);
                showToast("已开启服务");
            }
        });

        Button endService = (Button) view.findViewById(R.id.btn_end_service);
        endService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManager manager =
                        (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
                int defaultNum = 1000;
                List<ActivityManager.RunningServiceInfo> runServiceList = manager
                        .getRunningServices(defaultNum);
                boolean flag = false;
                for (ActivityManager.RunningServiceInfo runServiceInfo : runServiceList) {
                    if (runServiceInfo.foreground) {
                        if (runServiceInfo.service
                                .getShortClassName().equals(".Services.MainService")) {
                            Intent intent = new Intent();
                            intent.setComponent(runServiceInfo.service);
                            getContext().stopService(intent);
                            showToast("已关闭服务");
                            flag = true;
                            break;
                        }
                    }
                }
                if (!flag) {
                    showToast("服务未开启");
                }
            }
        });
    }

    @Override
    protected void processLogic(View view, Bundle savedInstanceState) {

    }

}
