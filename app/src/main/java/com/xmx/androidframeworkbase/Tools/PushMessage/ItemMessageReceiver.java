package com.xmx.androidframeworkbase.Tools.PushMessage;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.avos.avospush.notification.NotificationCompat;
import com.xmx.androidframeworkbase.R;

import org.json.JSONObject;

import java.util.Random;

/**
 * Created by The_onE on 2016/1/27.
 */
public class ItemMessageReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int notificationId = new Random().nextInt();

        try {
            String data = intent.getExtras().getString("com.avos.avoscloud.Data");
            JSONObject json = new JSONObject(data);

            String item = json.getString("item");
            String title = json.getString("alert");

            intent.setClass(context, ReceiveMessageActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(context, notificationId, intent, 0);

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle(item)
                            .setAutoCancel(true)
                            .setContentIntent(contentIntent)
                            .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                            .setContentText(title);
            NotificationManager manager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notification = mBuilder.build();
            manager.notify(notificationId, notification);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}