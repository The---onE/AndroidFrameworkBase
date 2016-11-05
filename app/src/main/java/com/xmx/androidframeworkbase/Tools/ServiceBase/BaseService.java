package com.xmx.androidframeworkbase.Tools.ServiceBase;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.xmx.androidframeworkbase.Constants;
import com.xmx.androidframeworkbase.MainActivity;
import com.xmx.androidframeworkbase.R;

/**
 * Created by The_onE on 2016/7/1.
 */
public abstract class BaseService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

//        processLogic();
//        setForeground();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        processLogic(intent);
        setForeground(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    protected abstract void processLogic(Intent intent);

    protected abstract void setForeground(Intent intent);

    public void showForeground(Class<?> iActivity, String content) {
        showForeground(iActivity, R.mipmap.ic_launcher, getString(R.string.app_name), content);
    }

    public void showForeground(Class<?> iActivity, String title, String content) {
        showForeground(iActivity, R.mipmap.ic_launcher, title, content);
    }

    public void showForeground(Class<?> iActivity, int sIcon,
                                 String title, String content) {
        int notificationId = -1;
        Intent notificationIntent = new Intent(this, iActivity);
        PendingIntent contentIntent = PendingIntent.getActivity(this, notificationId,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(sIcon)
                        .setContentTitle(title)
                        .setContentText(content)
                        .setContentIntent(contentIntent)
                        .setWhen(0);
        Notification notification = mBuilder.build();
        startForeground(notificationId, notification);
    }

    protected boolean filterException(Exception e) {
        if (e != null && Constants.EXCEPTION_DEBUG) {
            e.printStackTrace();
            showToast(e.getMessage());
            return false;
        } else {
            return true;
        }
    }

    protected void showToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

    protected void showLog(String tag, String msg) {
        Log.e(tag, msg);
    }

    protected void showLog(String tag, int i) {
        Log.e(tag, "" + i);
    }

    protected void startActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    protected void startActivity(Class<?> cls, String... objs) {
        Intent intent = new Intent(this, cls);
        for (int i = 0; i < objs.length; i++) {
            intent.putExtra(objs[i], objs[++i]);
        }
        startActivity(intent);
    }
}
