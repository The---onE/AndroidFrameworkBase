package com.xmx.androidframeworkbase.Tools;

import android.os.Handler;

/**
 * Created by The_onE on 2016/3/23.
 */
public abstract class Timer {
    long delay = 1000;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            timer();
            handler.postDelayed(this, delay);
        }
    };

    public abstract void timer();

    public void start(long d) {
        delay = d;
        handler.postDelayed(runnable, delay);
    }

    public void stop() {
        handler.removeCallbacks(runnable);
    }

    public void execute() {
        timer();
    }
}
