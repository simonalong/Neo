package com.simonalong.neo.devide.strategy;

import com.simonalong.neo.Neo;

import java.util.List;

/**
 * 分库分表策略
 *
 * @author shizi
 * @since 2020/4/3 5:47 PM
 */
public interface DevideStrategy {

    /**
     * 获取实际的DB
     *
     * @param neoList db集合
     * @param value   对应分库的值
     * @return 分库后的db
     */
    Neo getDb(List<Neo> neoList, Object value);

    /**
     * 获取实际的表名
     *
     * @param logicTableName 逻辑表名
     * @param value          分表对应的劣质
     * @return 实际的表名
     */
    String getTable(String logicTableName, Object value);
}
