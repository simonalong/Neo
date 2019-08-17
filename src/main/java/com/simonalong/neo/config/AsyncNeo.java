package com.simonalong.neo.config;

import java.util.concurrent.Executor;

/**
 * @author zhouzhenyong
 * @since 2019-08-17 17:15
 */
public interface AsyncNeo {

    Executor getExecutor();
}
