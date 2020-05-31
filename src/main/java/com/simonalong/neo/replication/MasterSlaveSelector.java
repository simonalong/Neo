package com.simonalong.neo.replication;

import com.simonalong.neo.core.AbstractBaseDb;

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
    AbstractBaseDb getMasterDb();

    /**
     * 获取从库db
     *
     * @return 从库db
     */
    AbstractBaseDb getSlaveDb();
}
