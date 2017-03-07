package com.xmx.androidframeworkbase.utils;

import android.content.Context;
import android.os.Vibrator;

/**
 * Created by The_onE on 2016/8/16.
 */
public class VibratorManager {
    private static VibratorManager vibratorManager;

    public synchronized static VibratorManager getInstance() {
        if (null == vibratorManager) {
            vibratorManager = new VibratorManager();
        }
        return vibratorManager;
    }

    public void vibrate(Context context, long time) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(time);
    }

    public void vibrate(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(new long[]{0, 10000}, 0);
    }

    public void cancel(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.cancel();
    }
}
