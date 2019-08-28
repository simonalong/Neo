package com.simonalong.neo.uid;

import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.WaitStrategies;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * @author zhouzhenyong
 * @since 2019-08-28 11:29
 */
public class RetryTask<T> {

    private Retryer<T> retry;

    private RetryTask() {
    }

    private static volatile RetryTask instance;

    /**
     * 重试单例
     *
     * @param time 重试间隔时间
     * @param timeUnit 时间单位
     * @param attemptNum 尝试次数
     */
    public static RetryTask getInstance(Integer time, TimeUnit timeUnit, Integer attemptNum) {
        if (null == instance) {
            synchronized (RetryTask.class) {
                if (null == instance) {
                    instance = new RetryTask();
                    instance.init(Long.valueOf(time), timeUnit, attemptNum);
                }
            }
        }
        return instance;
    }

    private void init(Long time, TimeUnit timeUnit, Integer attemptNum) {
        retry = RetryerBuilder
            .<T>newBuilder()
            //抛出runtime异常、checked异常时都会重试，但是抛出error不会重试。
            .retryIfException()
            //重调策略
            .withWaitStrategy(WaitStrategies.fixedWait(time, timeUnit))
            //尝试次数
            .withStopStrategy(StopStrategies.stopAfterAttempt(attemptNum))
            .build();
    }

    public void run(Runnable runnable) {
        try {
            retry.call(() -> {
                runnable.run();
                return null;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public T call(Callable<T> callable) {
        try {
            return retry.call(callable);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
