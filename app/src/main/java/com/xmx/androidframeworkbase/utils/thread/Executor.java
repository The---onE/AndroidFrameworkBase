package com.xmx.androidframeworkbase.utils.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 线程池工具
 *
 * @author liushiquan, luxiaolei
 */
public class Executor {
    private static Logger logger = LoggerFactory.getLogger(Executor.class);
    private static ExecutorService executor = Executors.newCachedThreadPool();

    /**
     * 直接在公共线程池中执行线程
     *
     * @param runnable 可运行对象
     */
    public static void execute(Runnable runnable) {
        try {
            executor.execute(runnable);
        } catch (Exception e) {
            logger.error("Exception when running task: " + e, e);
        }
    }

    /**
     * 重启公共线程池
     */
    public static void restart() {
        executor.shutdownNow();
        executor = Executors.newCachedThreadPool();
    }

    /**
     * 新建一个线程池
     *
     * @param threadSize 同时执行的线程数大小
     * @return ExecutorService
     */
    public static ExecutorService newExecutor(int threadSize) {
        return Executors.newFixedThreadPool(threadSize);
    }

    /**
     * 获得一个新的线程池
     *
     * @return ExecutorService
     */
    public static ExecutorService newExecutor() {
        return Executors.newCachedThreadPool();
    }

    /**
     * 执行异步方法
     *
     * @param runnable 需要执行的方法体
     * @return 执行的方法体
     */
    public static BaseRunnable excAsync(final BaseRunnable runnable, boolean isDeamon) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                runnable.run();
            }
        };
        thread.setDaemon(isDeamon);
        thread.start();

        return runnable;
    }

    /**
     * 执行有返回值的异步方法<br/>
     * Future代表一个异步执行的操作，通过get()方法可以获得操作的结果，如果异步操作还没有完成，则，get()会使当前线程阻塞
     *
     * @return Future
     */
    public static <T> Future<T> execAsync(Callable<T> task) {
        return executor.submit(task);
    }

    /**
     * 新建一个CompletionService，调用其submit方法可以异步执行多个任务，最后调用take方法按照完成的顺序获得其结果。，若未完成，则会阻塞
     *
     * @return CompletionService
     */
    public static <T> CompletionService<T> newCompletionService() {
        return new ExecutorCompletionService<T>(executor);
    }

    /**
     * 新建一个CompletionService，调用其submit方法可以异步执行多个任务，最后调用take方法按照完成的顺序获得其结果。，若未完成，则会阻塞
     *
     * @return CompletionService
     */
    public static <T> CompletionService<T> newCompletionService(ExecutorService executor) {
        return new ExecutorCompletionService<T>(executor);
    }
}