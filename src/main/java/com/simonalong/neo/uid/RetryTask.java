package com.simonalong.neo.uid;

import static com.simonalong.neo.NeoConstant.LOG_PRE;

import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.WaitStrategies;
import com.simonalong.neo.exception.UuidException;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhouzhenyong
 * @since 2019-08-28 11:29
 */
@Slf4j
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
    public RetryTask(Integer time, TimeUnit timeUnit, Integer attemptNum) {
        retry = RetryerBuilder
            .<T>newBuilder()
            //抛出runtime异常、checked异常时都会重试，但是抛出error不会重试。
            .retryIfException()
            // 出现异常重试
            .retryIfExceptionOfType(UuidException.class)
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
            log.error(LOG_PRE + "run重试异常", e);
        }
    }

    public T call(Callable<T> callable) {
        try {
            return retry.call(callable);
        } catch (Exception e) {
            log.error(LOG_PRE + "call重试异常", e);
            return null;
        }
    }
}
