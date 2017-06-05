package com.xmx.androidframeworkbase.utils;

import android.content.Context;
import android.os.Vibrator;

/**
 * Created by The_onE on 2017/6/4.
 * 震动控制器
 */

public class VibratorUtil {
    /**
     * 获取系统震动器
     *
     * @param context 当前上下文
     * @return 系统震动器
     */
    private static Vibrator getVibrator(Context context) {
        return (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    /**
     * 震动一定时间
     *
     * @param context 当前上下文
     * @param time    震动时间
     */
    public static void vibrate(Context context, long time) {
        Vibrator vibrator = getVibrator(context);
        vibrator.vibrate(time);
    }

    /**
     * 持续震动
     *
     * @param context 当前上下文
     */
    public static void vibrate(Context context) {
        Vibrator vibrator = getVibrator(context);
        vibrator.vibrate(new long[]{0, 10000}, 0);
    }

    /**
     * 交替震动，先震动，后暂停，不断反复
     *
     * @param context     当前上下文
     * @param vibrateTime 震动时间
     * @param pauseTime   暂停时间
     * @param repeatTimes 重复次数，0则不断重复
     */
    public static void vibrate(Context context, long vibrateTime, long pauseTime, int repeatTimes) {
        Vibrator vibrator = getVibrator(context);
        if (repeatTimes <= 0) {
            // 不断重复
            vibrator.vibrate(new long[]{pauseTime, vibrateTime}, 0);
        } else {
            // 0, V, P, V, P, V, ...
            long[] array = new long[repeatTimes * 2];
            array[0] = 0;
            for (int i = 1; i < repeatTimes * 2; ++i) {
                if (i % 2 == 1) {
                    array[i] = vibrateTime;
                } else {
                    array[i] = pauseTime;
                }
            }
            vibrator.vibrate(array, -1);
        }
    }

    /**
     * 官方震动
     *
     * @param context 当前上下文
     * @param pattern 震动序列(P, V, P, V, ...)
     * @param repeat  从指定的下标开始重复震动，-1则不重复
     */
    public static void vibrate(Context context, long[] pattern, int repeat) {
        Vibrator vibrator = getVibrator(context);
        vibrator.vibrate(pattern, repeat);
    }

    /**
     * 取消震动
     *
     * @param context 当前上下文
     */
    public static void cancel(Context context) {
        Vibrator vibrator = getVibrator(context);
        vibrator.cancel();
    }
}
