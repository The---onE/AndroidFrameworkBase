package com.xmx.androidframeworkbase.core.fragments;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.xmx.androidframeworkbase.module.floatwindow.FloatService;
import com.xmx.androidframeworkbase.module.log.ExceptionTestActivity;
import com.xmx.androidframeworkbase.module.log.OperationLogActivity;
import com.xmx.androidframeworkbase.module.qr.CreateQRCodeActivity;
import com.xmx.androidframeworkbase.module.qr.ScanQRCodeActivity;
import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.base.fragment.xUtilsFragment;
import com.xmx.androidframeworkbase.utils.VibratorManager;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
@ContentView(R.layout.fragment_home)
public class HomeFragment extends xUtilsFragment {

    @Event(value = R.id.btn_create_qr)
    private void onClickCreateQRCode(View view) {
        startActivity(CreateQRCodeActivity.class);
    }

    @Event(value = R.id.btn_scan_qr)
    private void onClickScanQRCode(View view) {
        startActivity(ScanQRCodeActivity.class);
    }

    @Event(value = R.id.btn_vibrate_once)
    private void onClickVibrateOnce(View view) {
        VibratorManager.getInstance().vibrate(getContext(), 1000);
    }

    @Event(value = R.id.btn_vibrate_forever)
    private void onClickVibrateForever(View view) {
        VibratorManager.getInstance().vibrate(getContext());
    }

    @Event(value = R.id.btn_cancel_vibrate)
    private void onClickCancelVibrate(View view) {
        VibratorManager.getInstance().cancel(getContext());
    }

    @Event(value = R.id.btn_show_float)
    private void onClickShowFloat(View view) {
        Intent service = new Intent(getContext(), FloatService.class);
        getContext().startService(service);
    }

    @Event(value = R.id.btn_hide_float)
    private void onClickHideFloat(View view) {
        ActivityManager manager =
                (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        int defaultNum = 1000;
        List<ActivityManager.RunningServiceInfo> runServiceList = manager
                .getRunningServices(defaultNum);
        boolean flag = false;
        for (ActivityManager.RunningServiceInfo runServiceInfo : runServiceList) {
            if (runServiceInfo.foreground) {
                if (runServiceInfo.service
                        .getShortClassName().equals(".Float.FloatService")) {
                    Intent intent = new Intent();
                    intent.setComponent(runServiceInfo.service);
                    getContext().stopService(intent);
                    showToast("已关闭浮动窗口");
                    flag = true;
                }
            }
        }
        if (!flag) {
            showToast("浮动窗口未开启");
        }
    }

    @Event(value = R.id.btn_show_log)
    private void onClickShowLog(View view) {
        startActivity(OperationLogActivity.class);
    }

    @Event(value = R.id.btn_exception_test)
    private void onClickExceptionTest(View view) {
        startActivity(ExceptionTestActivity.class);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

}
