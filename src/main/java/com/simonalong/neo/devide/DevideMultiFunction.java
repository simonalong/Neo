package com.simonalong.neo.devide;

import com.simonalong.neo.Neo;

/**
 * 多参数函数式处理
 *
 * @author shizi
 * @since 2020/6/13 11:56 AM
 */
@FunctionalInterface
public interface DevideMultiFunction<T> {

    /**
     * 执行
     *
     * @param db         数据库
     * @param tableName  表名
     * @return 结果
     */
    T apply(Neo db, String tableName);
}
