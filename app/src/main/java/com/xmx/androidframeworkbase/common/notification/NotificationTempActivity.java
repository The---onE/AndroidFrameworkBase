package com.xmx.androidframeworkbase.common.notification;

import android.app.Activity;
import android.os.Bundle;

import com.xmx.androidframeworkbase.R;

public class NotificationTempActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_temp);

        int id = getIntent().getIntExtra("notificationId", 0);
        NotificationUtils.removeNotification(this, id);

        finish();
    }
}
