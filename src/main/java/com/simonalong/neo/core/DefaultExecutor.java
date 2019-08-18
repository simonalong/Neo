package com.simonalong.neo.core;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
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
     * <p>创建核心线程池为单机本身可用的cpu个数2倍，最大为100倍cpu个数，core~mac中间的线程存活时间设定为5秒，队列为1000倍的cpu个数队列，拒绝策略采用直接运行方式
     * <p>用户可以继承后并重写该线程池分配策略
     */
    @Override
    public Executor getExecutor() {
        int processNum = Runtime.getRuntime().availableProcessors();
        return new ThreadPoolExecutor(
            2 * processNum,
            100 * processNum,
            5, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(processNum * 1000),
            new CountThreadFactory(),
            new BlockRejectedExecutionHandler());
    }

    /**
     * 重写拒绝策略，用于在任务量超大情况下任务的阻塞提交
     */
    private class BlockRejectedExecutionHandler implements RejectedExecutionHandler {

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            try {
                executor.getQueue().put(r);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 指定名字的线程工厂
     */
    private class CountThreadFactory implements ThreadFactory {

        private AtomicInteger threadNum = new AtomicInteger(0);
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "Thread-Neo-async-Call-" + threadNum.getAndIncrement());
        }
    }
}
