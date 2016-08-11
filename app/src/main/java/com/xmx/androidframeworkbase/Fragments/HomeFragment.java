package com.xmx.androidframeworkbase.Fragments;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;

import com.xmx.androidframeworkbase.Constants;
import com.xmx.androidframeworkbase.QRCode.CreateQRCodeActivity;
import com.xmx.androidframeworkbase.QRCode.ScanQRCodeActivity;
import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.Services.MainService;
import com.xmx.androidframeworkbase.Tools.ChoosePhoto.AlbumActivity;
import com.xmx.androidframeworkbase.Tools.FragmentBase.xUtilsFragment;
import com.xmx.androidframeworkbase.Tools.Notification.NotificationTempActivity;
import com.xmx.androidframeworkbase.Tools.Notification.NotificationUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

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

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

}
