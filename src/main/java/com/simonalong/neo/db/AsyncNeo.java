package com.simonalong.neo.db;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

/**
 * 异步调用接口
 *
 * @author zhouzhenyong
 * @since 2019-08-17 16:35
 */
public interface AsyncNeo {

    Executor getExecutor();
}
