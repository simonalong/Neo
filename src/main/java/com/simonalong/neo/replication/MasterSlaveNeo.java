package com.simonalong.neo.replication;

import com.simonalong.neo.Neo;
import com.simonalong.neo.core.AbstractBaseDb;
import com.simonalong.neo.exception.NeoException;
import lombok.Data;

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

    private Map<String, InnerActiveDb> masterDbMap = new ConcurrentHashMap<>();
    private Map<String, InnerActiveDb> slaveDbMap = new ConcurrentHashMap<>();
    private InnerActiveDb currentMasterDb;
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

        InnerActiveDb newDb = new InnerActiveDb(db, alias, active);
        if (active) {
            currentMasterDb = newDb;
        }
        masterDbMap.put(alias, newDb);
    }

    public void addMasterDb(DataSource datasource, String alias, Boolean active) {
        addMasterDb(Neo.connect(datasource), alias, active);
    }

    public void addSlaveDb(Neo db, String alias) {
        if (null == db) {
            return;
        }
        slaveDbMap.put(alias, new InnerActiveDb(db, alias));
        slaveKeys.add(alias);
    }

    public void addSlaveDb(DataSource datasource, String alias) {
        addSlaveDb(Neo.connect(datasource), alias);
    }

    public void activeMaster(String alias) {
        if (!masterDbMap.containsKey(alias)) {
            throw new NeoException("没有找到别名为" + alias + "的库");
        }
        InnerActiveDb innerActiveDb = masterDbMap.get(alias);
        assert null != innerActiveDb;
        innerActiveDb.setActiveFlag(true);
        currentMasterDb = innerActiveDb;
    }

    /**
     * 设置主库不可用
     *
     * @param alias 主库的db别名
     */
    public void deActiveMasterDb(String alias) {
        if (!masterDbMap.containsKey(alias)) {
            throw new NeoException("没有找到别名为" + alias + "的库");
        }

        // 去激活的是当前正在使用的
        if (currentMasterDb.getName().equals(alias)) {
            currentMasterDb = getRandomMaster();
        }

        masterDbMap.get(alias).setActiveFlag(false);
    }

    public void activeSlave(String alias) {
        if (!slaveDbMap.containsKey(alias)) {
            throw new NeoException("没有找到别名为" + alias + "的库");
        }

        InnerActiveDb innerActiveDb = slaveDbMap.get(alias);
        assert null != innerActiveDb;
        innerActiveDb.setActiveFlag(true);
        if (!slaveKeys.contains(alias)) {
            slaveKeys.add(alias);
        }
    }

    /**
     * 设置某个从库中的db不可用
     *
     * @param alias 从库的db别名
     */
    public void deActiveSlaveDb(String alias) {
        if (!slaveDbMap.containsKey(alias)) {
            throw new NeoException("没有找到别名为" + alias + "的库");
        }

        InnerActiveDb innerActiveDb = slaveDbMap.get(alias);
        assert null != innerActiveDb;
        innerActiveDb.setActiveFlag(false);
        slaveKeys.remove(alias);
    }

    private Integer getIndex() {
        int keySize = slaveKeys.size();
        if (0 == keySize) {
            return null;
        }
        return slaveIndex.getAndAccumulate(1, (pre, incrementNum) -> {
            int index = pre + incrementNum;
            if (index >= keySize) {
                return 0;
            }
            return index;
        });
    }

    /**
     * 从内部找到可用的主库
     *
     * @return 可用的主库
     */
    private InnerActiveDb getRandomMaster() {
        InnerActiveDb master = masterDbMap.entrySet().stream().filter(e -> e.getValue().getActiveFlag()).map(Map.Entry::getValue).findFirst().orElse(null);
        if (null == master) {
            throw new NeoException("没有找到可用的主库");
        }
        return master;
    }

    /**
     * 获取主库db
     *
     * @return 主库db
     */
    @Override
    public AbstractBaseDb getMasterDb() {
        if (null != currentMasterDb) {
            return currentMasterDb.getDb();
        }

        throw new NeoException("主库没有设置或者不可用");
    }

    /**
     * 获取从库db
     *
     * @return 从库db
     */
    @Override
    public AbstractBaseDb getSlaveDb() {
        if (0 == slaveKeys.size()) {
            throw new NeoException("请先添加从库");
        }

        Integer index = getIndex();
        if (null != index) {
            InnerActiveDb innerActiveDb = slaveDbMap.get(slaveKeys.get(index));
            if (null == innerActiveDb) {
                throw new NeoException("从库获取失败");
            }
            return innerActiveDb.getDb();
        } else {
            // 从库都不可用，则走主库
            return getMasterDb();
        }
    }

    @Data
    static class InnerActiveDb {

        /**
         * 激活标示
         */
        private Boolean activeFlag = true;
        private String name;
        private Neo db;

        InnerActiveDb(Neo db, String name) {
            this.db = db;
            this.name = name;
        }

        InnerActiveDb(Neo db, String name, Boolean activeFlag) {
            this.db = db;
            this.name = name;
            this.activeFlag = activeFlag;
        }
    }
}
