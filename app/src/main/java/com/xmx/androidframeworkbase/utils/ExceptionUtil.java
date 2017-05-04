package com.xmx.androidframeworkbase.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.xmx.androidframeworkbase.common.log.OperationLogEntityManager;
import com.xmx.androidframeworkbase.core.Constants;

/**
 * Created by The_onE on 2016/11/7.
 * 异常处理类
 */

public class ExceptionUtil {

    /**
     * 轻微异常处理，记录日志，不影响程序
     *
     * @param e       异常信息
     * @param context 当前上下文
     */
    static public void normalException(Exception e, Context context) {
        // 记录错误日志
        OperationLogEntityManager.getInstance().addLog(e.toString());
        // 在调试状态显示错误信息
        if (Constants.EXCEPTION_DEBUG) {
            // 打印异常堆栈跟踪
            Log.e("Error:", Log.getStackTraceString(e));
            //e.printStackTrace();
            // 显示错误信息
            Toast.makeText(context, "出现异常:" + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    // 致命异常

    /**
     * 致命异常处理，记录日志，传递异常交由上层异常处理器处理
     *
     * @param e       异常信息
     * @param context 当前上下文
     */
    void fatalError(Exception e, Context context) throws Exception {
        // 记录错误日志
        OperationLogEntityManager.getInstance().addLog(e.toString());
        // 在调试状态显示错误信息
        if (Constants.EXCEPTION_DEBUG) {
            // 打印异常堆栈跟踪
            Log.e("Error:", Log.getStackTraceString(e));
            //e.printStackTrace();
            // 显示错误信息
            Toast.makeText(context, "致命异常:" + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
        // 传递异常，交由异常处理器处理
        throw e;
    }
}
