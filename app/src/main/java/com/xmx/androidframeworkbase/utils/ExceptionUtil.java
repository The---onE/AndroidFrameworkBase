package com.xmx.androidframeworkbase.utils;

import com.xmx.androidframeworkbase.core.Constants;
import com.xmx.androidframeworkbase.common.log.OperationLogEntityManager;

/**
 * Created by The_onE on 2016/11/7.
 */

public class ExceptionUtil {
    public static boolean filterException(Exception e) {
        if (e != null && Constants.EXCEPTION_DEBUG) {
            e.printStackTrace();
            OperationLogEntityManager.getInstance().addLog("" + e);
            return false;
        } else {
            return true;
        }
    }
}
