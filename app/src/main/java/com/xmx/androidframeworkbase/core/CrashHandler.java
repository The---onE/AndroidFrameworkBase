package com.xmx.androidframeworkbase.core;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.xmx.androidframeworkbase.common.log.OperationLogEntityManager;

/**
 * Created by The_onE on 2016/10/2.
 * 自定义异常处理器，单例对象
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

    /**
     * 初始化，在Application中调用
     * 调用后会替代系统默认异常处理器
     *
     * @param context 当前上下文
     */
    public void init(Context context) {
        mContext = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 处理用户未处理的异常
     * 调试模式记录异常信息并关闭程序
     * 非调试模式记录异常信息并重启程序
     *
     * @param thread 产生异常的线程
     * @param ex     异常信息
     */
    @Override
    public void uncaughtException(Thread thread, final Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                if (Constants.EXCEPTION_DEBUG) {
                    // 在新线程中显示错误信息
                    new Thread() {
                        @Override
                        public void run() {
                            Looper.prepare();
                            Toast.makeText(mContext, "出现错误:" + ex.getMessage()
                                            + ",程序即将结束",
                                    Toast.LENGTH_LONG).show();
                            Looper.loop();
                        }
                    }.start();
                    // 等待处理完成
                    Thread.sleep(3000);
                    // 结束程序
                    Application.getInstance().exit();
                } else {
                    // 在新线程中显示错误信息
                    new Thread() {
                        @Override
                        public void run() {
                            Looper.prepare();
                            Toast.makeText(mContext, "出现意料之外的错误,程序即将重启",
                                    Toast.LENGTH_LONG).show();
                            Looper.loop();
                        }
                    }.start();
                    // 等待处理完成
                    Thread.sleep(3000);
                    // 重启程序
                    System.exit(1);
                }
            } catch (Exception e) {
                // 处理再次出现错误
                Toast.makeText(mContext, "处理出现错误:" + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
                Application.getInstance().exit();
            }
        }
    }

    /**
     * 自定义异常处理
     *
     * @param ex 异常信息
     */
    private boolean handleException(final Throwable ex) {
        if (ex == null) {
            return false;
        }

        if (Constants.EXCEPTION_DEBUG) {
            // 打印异常堆栈跟踪
            Log.e("Error:", Log.getStackTraceString(ex));
        }
        // 记录错误日志
        OperationLogEntityManager.getInstance().addLog(ex.toString());
        // 已进行处理
        return true;
    }
}
