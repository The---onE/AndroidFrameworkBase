package com.xmx.androidframeworkbase.common.choosephoto.entities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import android.graphics.Movie;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.util.LruCache;

public class GifLoader {

    private class ImgBeanHolder {
        GifImageView imageView;
        String path;
        Movie gif;
    }

    /**
     * 图片缓存的核心类
     */
    private LruCache<String, Movie> mLruCache;
    /**
     * 线程池
     */
    private ExecutorService mThreadPool;
    /**
     * 线程池的线程数量，默认为1
     */
    private int mThreadCount = 1;
    /**
     * 队列的调度方式
     */
    private Type mType = Type.LIFO;
    /**
     * 任务队列
     */
    private LinkedList<Runnable> mTasks;
    /**
     * 轮询的线程
     */
    private Thread mPoolThread;
    private Handler mPoolThreadHander;

    /**
     * 运行在UI线程的handler，用于给ImageView设置图片
     */
    private LoadImageHandler mHandler;

    /**
     * 引入一个值为1的信号量，防止mPoolThreadHander未初始化完成
     */
    private volatile Semaphore mSemaphore = new Semaphore(0);

    /**
     * 引入一个值为1的信号量，由于线程池内部也有一个阻塞线程，防止加入任务的速度过快，使LIFO效果不明显
     */
    private volatile Semaphore mPoolSemaphore;

    private static GifLoader mInstance;

    /**
     * 队列的调度方式
     *
     * @author zhy
     */
    public enum Type {
        FIFO, LIFO
    }

    private GifLoader(int threadCount, Type type) {
        init(threadCount, type);
    }

    /**
     * 单例获得该实例对象
     *
     * @return
     */
    public static GifLoader getInstance() {
        if (mInstance == null) {
            synchronized (GifLoader.class) {
                if (mInstance == null) {
                    mInstance = new GifLoader(1, Type.LIFO);
                }
            }
        }
        return mInstance;
    }

    /**
     * 单例获得该实例对象
     *
     * @return
     */
    public static GifLoader getInstance(int threadCount, Type type) {
        if (mInstance == null) {
            synchronized (GifLoader.class) {
                if (mInstance == null) {
                    mInstance = new GifLoader(threadCount, type);
                }
            }
        }
        return mInstance;
    }

    private void init(int threadCount, Type type) {
        // loop thread
        mPoolThread = new Thread() {
            @Override
            public void run() {
                Looper.prepare();

                mPoolThreadHander = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        mThreadPool.execute(getTask());
                        try {
                            mPoolSemaphore.acquire();
                        } catch (InterruptedException e) {
                        }
                    }
                };
                // 释放一个信号量
                mSemaphore.release();
                Looper.loop();
            }
        };
        mPoolThread.start();

        // 获取应用程序最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;
        mLruCache = new LruCache<String, Movie>(cacheSize) {
            @Override
            protected int sizeOf(String key, Movie value) {
                if (value != null) {
                    try {
                        File f = new File(key);
                        FileInputStream fis = new FileInputStream(f);
                        return fis.available();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return 0;
            }
        };

        mThreadPool = Executors.newFixedThreadPool(threadCount);
        mPoolSemaphore = new Semaphore(threadCount);
        mTasks = new LinkedList<>();
        mType = type == null ? Type.LIFO : type;

    }

    static class LoadImageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ImgBeanHolder holder = (ImgBeanHolder) msg.obj;
            GifImageView imageView = holder.imageView;
            String path = holder.path;
            Movie gif = holder.gif;
            if (imageView.getTag().toString().equals(path)) {
                if (gif != null) {
                    imageView.setImageMovie(gif);
                }
            }
        }
    }

    /**
     * 加载图片
     *
     * @param path
     * @param imageView
     */
    public void loadImage(final String path, final GifImageView imageView) {
        // set tag
        imageView.setTag(path);
        // UI线程
        if (mHandler == null) {
            mHandler = new LoadImageHandler();
        }

        Movie gif = getGifFromLruCache(path);
        if (gif != null) {
            ImgBeanHolder holder = new ImgBeanHolder();
            holder.gif = gif;
            holder.imageView = imageView;
            holder.path = path;
            Message message = Message.obtain();
            message.obj = holder;
            mHandler.sendMessage(message);
        } else {
            addTask(new Runnable() {
                @Override
                public void run() {
                    Movie gif = Movie.decodeFile(path);

                    if (gif != null) {
                        addGifToLruCache(path, gif);
                        ImgBeanHolder holder = new ImgBeanHolder();
                        holder.gif = getGifFromLruCache(path);
                        holder.imageView = imageView;
                        holder.path = path;
                        Message message = Message.obtain();
                        message.obj = holder;
                        mHandler.sendMessage(message);
                        mPoolSemaphore.release();
                    }

                }
            });
        }
    }

    /**
     * 添加一个任务
     *
     * @param runnable
     */
    private synchronized void addTask(Runnable runnable) {
        try {
            // 请求信号量，防止mPoolThreadHander为null
            if (mPoolThreadHander == null)
                mSemaphore.acquire();
        } catch (InterruptedException e) {
        }
        mTasks.add(runnable);

        mPoolThreadHander.sendEmptyMessage(0x110);
    }

    /**
     * 取出一个任务
     *
     * @return
     */
    private synchronized Runnable getTask() {
        if (mType == Type.FIFO) {
            return mTasks.removeFirst();
        } else if (mType == Type.LIFO) {
            return mTasks.removeLast();
        }
        return null;
    }

    /**
     * 从LruCache中获取一张图片，如果不存在就返回null。
     */
    private Movie getGifFromLruCache(String key) {
        return mLruCache.get(key);
    }

    /**
     * 往LruCache中添加一张图片
     *
     * @param key
     * @param gif
     */
    private void addGifToLruCache(String key, Movie gif) {
        if (getGifFromLruCache(key) == null) {
            if (gif != null)
                mLruCache.put(key, gif);
        }
    }
}
