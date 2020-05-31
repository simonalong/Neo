package com.simonalong.neo.replication;

import com.simonalong.neo.Neo;
import com.simonalong.neo.core.AbstractBaseDb;
import com.simonalong.neo.exception.NeoException;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author shizi
 * @since 2020/5/31 5:54 PM
 */
public class MasterSlaveNeo extends AbstractMasterSlaveDb {

    private Map<String, Neo> masterDbMap = new ConcurrentHashMap<>();
    private Map<String, Neo> slaveDbMap = new ConcurrentHashMap<>();
    private Neo activeMaster;
    private AtomicInteger slaveIndex = new AtomicInteger(0);
    private List<String> slaveKeys = new ArrayList<>();

    public void addMasterDb(Neo db, String alias) {
        addMasterDb(db, alias, true);
    }

    public void addMasterDb(DataSource datasource, String alias) {
        addMasterDb(datasource, alias, true);
    }

    public void addMasterDb(Neo db, String alias, Boolean active) {
        if (null == db || null == active) {
            return;
        }
        if (active) {
            activeMaster = db;
        }
        masterDbMap.put(alias, db);
    }

    public void addMasterDb(DataSource datasource, String alias, Boolean active) {
        addMasterDb(Neo.connect(datasource), alias, active);
    }

    public void addSlaveDb(Neo db, String alias) {
        if (null == db) {
            return;
        }
        slaveDbMap.put(alias, db);
        slaveKeys.add(alias);
    }

    public void setActiveMaster(Neo db, String alias) {
        activeMaster = db;
        masterDbMap.put(alias, db);
    }

    public void setActiveMaster(String dbAlias) {
        if (!masterDbMap.containsKey(dbAlias)) {
            throw new NeoException("没有找到别名为" + dbAlias + "的库");
        }
        activeMaster = masterDbMap.get(dbAlias);
    }

    public void addSlaveDb(DataSource datasource, String alias) {
        addSlaveDb(Neo.connect(datasource), alias);
    }

    @Override
    public AbstractBaseDb getMasterDb() {
        if (null != activeMaster) {
            return activeMaster;
        }

        // todo 主从在某个从库不可用情况下切换到另一个从库
        throw new NeoException("请先设置主库");
    }

    @Override
    public AbstractBaseDb getSlaveDb() {
        if (0 == slaveKeys.size()) {
            throw new NeoException("请先添加从库");
        }
        // todo 主从在某个从库不可用情况下切换到另一个从库
        return slaveDbMap.get(slaveKeys.get(getIndex()));
    }

    private int getIndex(){
        int keySize = slaveKeys.size();
        int index = slaveIndex.getAndIncrement();
        if (index >= keySize) {
            slaveIndex.set(0);
            return index % keySize;
        }
        return index;
    }
}
