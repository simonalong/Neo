package com.simonalong.neo.replication;

/**
 * @author shizi
 * @since 2020/5/31 5:54 PM
 */
public interface MasterSlaveSelector {

    /**
     * 获取主库db
     *
     * @return 主库db
     */
    MasterSlaveNeo.InnerActiveDb selectMasterDb();

    /**
     * 获取从库db
     *
     * @return 从库db
     */
    MasterSlaveNeo.InnerActiveDb selectSlaveDb();

    void deActiveMaster(String alias);

    void deActiveSlave(String alias);
}
