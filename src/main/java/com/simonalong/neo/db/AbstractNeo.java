package com.simonalong.neo.db;

/**
 * 通过装饰器模式来调用实际的处理方
 *
 * @author zhouzhenyong
 * @since 2019/5/13 下午12:01
 */
public abstract class AbstractNeo extends AbstractExecutorNeo implements ExecuteDbNeo, BaseDbNeo {

    /**
     * 获取库数据
     * @return 返回系统自定义的库
     */
    public abstract AbstractNeo getNeo();
}
