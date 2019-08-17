package com.simonalong.neo.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhouzhenyong
 * @since 2019-08-17 17:16
 */
public class DefaultExecutor implements AsyncNeo{

    private static volatile DefaultExecutor instance = null;

    private DefaultExecutor(){}

    public static DefaultExecutor getInstance(){
        if(null == instance){
            synchronized (DefaultExecutor.class){
                if(null == instance){
                    instance = new DefaultExecutor();
                }
            }
        }
        return instance;
    }

    /**
     * 创建默认的异步回调的线程池
     *
     * <p>创建核心线程池为单机本身可用的cpu个数，最大为2倍cpu个数，队列为100倍的cpu处理个数队列，拒绝策略采用直接运行方式
     * <p>用户可以继承后并重写该线程池分配策略
     */
    @Override
    public ExecutorService getExecutor(){
        int processNum =  Runtime.getRuntime().availableProcessors();
        return new ThreadPoolExecutor(processNum, processNum * 2, 0, TimeUnit.NANOSECONDS,
            new LinkedBlockingQueue<>(processNum * 100),  new ThreadFactory() {
            private AtomicInteger threadNum = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "Thread-Neo-async-Call-" + threadNum.getAndIncrement());
            }
        }, new CallerRunsPolicy());
    }
}
