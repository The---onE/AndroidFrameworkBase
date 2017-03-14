package com.xmx.androidframeworkbase.base.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.xmx.androidframeworkbase.core.Constants;
import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.utils.StrUtil;

/**
 * Created by The_onE on 2016/7/1.
 * Service基类，声明业务接口，提供常用功能
 */
public abstract class BaseService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 处理业务逻辑
        processLogic(intent);
        // 设置服务运行于前台
        // 使用showForeground方法设置为前台服务
        setForeground(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 处理业务逻辑接口
     * @param intent 启动Service传递的Intent
     */
    protected abstract void processLogic(Intent intent);

    /**
     * 设置前台服务接口
     * 若需要设置Service为前台服务则调用[showForeground]方法
     * @param intent 启动Service传递的Intent
     */
    protected abstract void setForeground(Intent intent);

    /**
     * 设置为前台服务，提高优先级，持续显示通知
     * @param iActivity 点击通知时打开的Activity
     * @param content 通知显示的内容
     */
    public void showForeground(Class<?> iActivity, String content) {
        showForeground(iActivity, R.mipmap.ic_launcher, getString(R.string.app_name), content);
    }

    /**
     * 设置为前台服务，提高优先级，持续显示通知
     * @param iActivity 点击通知时打开的Activity
     * @param content 通知显示的内容
     * @param title 通知显示的标题
     */
    public void showForeground(Class<?> iActivity, String title, String content) {
        showForeground(iActivity, R.mipmap.ic_launcher, title, content);
    }

    /**
     * 设置为前台服务，提高优先级，持续显示通知
     * @param iActivity 点击通知时打开的Activity
     * @param content 通知显示的内容
     * @param title 通知显示的标题
     * @param sIcon 通知显示的图标ID
     */
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

    /**
     * 显示提示信息
     * @param str 要显示的字符串信息
     */
    protected void showToast(String str) {
        StrUtil.showToast(this, str);
    }

    /**
     * 显示提示信息
     * @param resId 要显示的字符串在strings文件中的ID
     */
    protected void showToast(int resId) {
        StrUtil.showToast(this, resId);
    }

    /**
     * 打印日志
     * @param tag 日志标签
     * @param msg 日志信息
     */
    protected void showLog(String tag, String msg) {
        StrUtil.showLog(tag, msg);
    }

    /**
     * 打印日志
     * @param tag 日志标签
     * @param i 数字作为日志信息
     */
    protected void showLog(String tag, int i) {
        StrUtil.showLog(tag, i);
    }

    /**
     * 启动新Activity
     * @param cls 要启动的Activity类
     */
    protected void startActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    /**
     * 带参数启动新Activity
     * @param cls 要启动的Activity类
     * @param objs 向Activity传递的参数，奇数项为键，偶数项为值
     */
    protected void startActivity(Class<?> cls, String... objs) {
        Intent intent = new Intent(this, cls);
        for (int i = 0; i < objs.length; i++) {
            intent.putExtra(objs[i], objs[++i]);
        }
        startActivity(intent);
    }
}
