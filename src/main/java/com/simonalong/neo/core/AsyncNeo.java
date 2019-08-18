package com.simonalong.neo.core;

import java.util.concurrent.Executor;

/**
 * @author zhouzhenyong
 * @since 2019-08-17 17:15
 */
public interface AsyncNeo {

    /**
     * 在异步处理情况下获取线程执行器
     * @return 对应的执行器子类
     */
    Executor getExecutor();
}
