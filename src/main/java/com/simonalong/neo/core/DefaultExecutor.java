package com.simonalong.neo.core;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 默认线程池执行器
 *
 * @author zhouzhenyong
 * @since 2019-08-17 17:16
 */
public class DefaultExecutor implements AsyncNeo {

    private static volatile DefaultExecutor instance = null;

    private DefaultExecutor() {
    }

    public static DefaultExecutor getInstance() {
        if (null == instance) {
            synchronized (DefaultExecutor.class) {
                if (null == instance) {
                    instance = new DefaultExecutor();
                }
            }
        }
        return instance;
    }

    /**
     * 创建默认的异步回调的线程池
     *
     * <p>创建核心线程池为单机本身可用的cpu个数，最大为2倍cpu个数，core~mac中间的线程存活时间设定为5秒，队列为1000倍的cpu个数队列，拒绝策略采用直接运行方式
     * <p>用户可以继承后并重写该线程池分配策略
     */
    @Override
    public Executor getExecutor() {
        int processNum = Runtime.getRuntime().availableProcessors();
        return new ThreadPoolExecutor(processNum, processNum * 2, 5, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(processNum * 100), new ThreadFactory() {
            private AtomicInteger threadNum = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "Thread-Neo-async-Call-" + threadNum.getAndIncrement());
            }
        }, new CallerRunsPolicy());
    }
}
