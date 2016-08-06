package com.xmx.androidframeworkbase.Tools;

import android.os.HandlerThread;

/**
 * Created by The_onE on 2016/8/6.
 */
public abstract class NewThread {
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            process();
        }
    };

    public abstract void process();

    void start() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                process();
            }
        };
        thread.start();
    }
}
