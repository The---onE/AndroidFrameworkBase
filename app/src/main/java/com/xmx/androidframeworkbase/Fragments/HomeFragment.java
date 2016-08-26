package com.xmx.androidframeworkbase.Fragments;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.xmx.androidframeworkbase.Float.FloatService;
import com.xmx.androidframeworkbase.QRCode.CreateQRCodeActivity;
import com.xmx.androidframeworkbase.QRCode.ScanQRCodeActivity;
import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.Tools.FragmentBase.xUtilsFragment;
import com.xmx.androidframeworkbase.Tools.VibratorManager;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
@ContentView(R.layout.fragment_home)
public class HomeFragment extends xUtilsFragment {

    @Event(value = R.id.btn_create_qr)
    private void onCilckCreateQRCode(View view) {
        startActivity(CreateQRCodeActivity.class);
    }

    @Event(value = R.id.btn_scan_qr)
    private void onCilckScanQRCode(View view) {
        startActivity(ScanQRCodeActivity.class);
    }

    @Event(value = R.id.btn_vibrate_once)
    private void onCilckVibrateOnce(View view) {
        VibratorManager.getInstance().vibrate(getContext(), 1000);
    }

    @Event(value = R.id.btn_vibrate_forever)
    private void onCilckVibrateForever(View view) {
        VibratorManager.getInstance().vibrate(getContext());
    }

    @Event(value = R.id.btn_cancel_vibrate)
    private void onCilckCancelVibrate(View view) {
        VibratorManager.getInstance().cancel(getContext());
    }

    @Event(value = R.id.btn_show_float)
    private void onCilckShowFloat(View view) {
        Intent service = new Intent(getContext(), FloatService.class);
        getContext().startService(service);
    }

    @Event(value = R.id.btn_hide_float)
    private void onCilckHideFloat(View view) {
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

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

}
