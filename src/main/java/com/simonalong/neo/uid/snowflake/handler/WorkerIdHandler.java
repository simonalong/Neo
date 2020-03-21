package com.simonalong.neo.uid.snowflake.handler;

/**
 * 节点 worker_xx 读取和更新管理器
 *
 * @author shizi
 * @since 2020/2/7 1:02 上午
 */
public interface WorkerIdHandler
{

    /**
     * 当前工作节点的下次失效时间
     */
    Long getLastExpireTime();


    /**
     * 获取节点的下表索引
     */
    Integer getWorkerId();
}
