package com.xmx.androidframeworkbase.core;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

import com.xmx.androidframeworkbase.common.log.OperationLogEntityManager;

/**
 * Created by The_onE on 2016/10/2.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static CrashHandler instance;

    public synchronized static CrashHandler getInstance() {
        if (null == instance) {
            instance = new CrashHandler();
        }
        return instance;
    }

    private Context mContext;
    //系统默认的UncaughtException处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    public void init(Context context) {
        mContext = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Toast.makeText(mContext, "出现错误:" + e, Toast.LENGTH_SHORT).show();
            }
            //退出程序
            Application.getInstance().exit();
            //android.os.Process.killProcess(android.os.Process.myPid());
            //System.exit(1);
        }
    }

    private boolean handleException(final Throwable ex) {
        if (ex == null) {
            return false;
        }

        ex.printStackTrace();
        OperationLogEntityManager.getInstance().addLog("" + ex);

        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "出现错误" + ex, Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();

        return true;
    }
}
