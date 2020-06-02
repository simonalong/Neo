package com.simonalong.neo.replication;

import com.simonalong.neo.Neo;
import com.simonalong.neo.exception.NeoException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.simonalong.neo.NeoConstant.LOG_PRE;

/**
 * @author shizi
 * @since 2020/5/31 5:54 PM
 */
@Slf4j
public class MasterSlaveNeo extends AbstractMasterSlaveDb {

    private static final String MS_LOG_PRE = LOG_PRE + "[master-slave]";
    private Map<String, InnerActiveDb> masterDbMap = new ConcurrentHashMap<>();
    private Map<String, InnerActiveDb> slaveDbMap = new ConcurrentHashMap<>();
    /**
     * 临时从库，用于在从库都不可用情况下的添加的主库
     */
    private Map<String, InnerActiveDb> slaveDbMapTem = new ConcurrentHashMap<>();
    private InnerActiveDb currentMasterDb;
    private AtomicInteger slaveIndex = new AtomicInteger(0);
    private List<String> slaveKeys = new ArrayList<>();
    /**
     * 库断开后重连任务
     */
    private Executor restoreTask = new ThreadPoolExecutor(0, 1, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1), new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, "Neo-Restore-Db");
            thread.setDaemon(true);
            return thread;
        }
    }, new ThreadPoolExecutor.DiscardPolicy());

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

    private void addSlaveDbTem(InnerActiveDb innerActiveDb) {
        if (null != innerActiveDb) {
            slaveDbMapTem.put(innerActiveDb.getName(), innerActiveDb);
            slaveKeys.add(innerActiveDb.getName());
            startRestore();
        }
    }

    /**
     * 设置主库不可用
     *
     * @param alias 主库的db别名
     */
    @Override
    public void deActiveMaster(String alias) {
        if (!masterDbMap.containsKey(alias)) {
            throw new NeoException("没有找到别名为" + alias + "的库");
        }

        masterDbMap.get(alias).setActiveFlag(false);

        // 去激活的是当前正在使用的
        if (currentMasterDb.getName().equals(alias)) {
            currentMasterDb = getRandomMaster();
        }

        // 启动恢复线程
        startRestore();
    }

    /**
     * 设置某个从库中的db不可用
     *
     * @param alias 从库的db别名
     */
    @Override
    public void deActiveSlave(String alias) {
        if (!slaveDbMap.containsKey(alias)) {
            throw new NeoException("没有找到别名为" + alias + "的库");
        }

        InnerActiveDb innerActiveDb = slaveDbMap.get(alias);
        assert null != innerActiveDb;
        innerActiveDb.setActiveFlag(false);
        slaveKeys.remove(alias);
        slaveIndex.set(0);

        // 启动恢复线程
        startRestore();
    }

    private Integer getNextIndex() {
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
     * 启动恢复程序
     */
    private void startRestore() {
        restoreTask.execute(() -> {
            while (haveUnActiveDb()) {
                try {
                    Thread.sleep(10 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                doRestore(true, masterDbMap.values().stream().filter(e -> !e.getActiveFlag()).collect(Collectors.toList()));
                doRestore(false, slaveDbMap.values().stream().filter(e -> !e.getActiveFlag()).collect(Collectors.toList()));
            }
        });
    }

    private Boolean haveUnActiveDb() {
        return masterDbMap.values().stream().anyMatch(e -> !e.getActiveFlag()) || slaveDbMap.values().stream().anyMatch(e -> !e.getActiveFlag());
    }

    private void doRestore(Boolean masterOrSlave, List<InnerActiveDb> unActiveDbs) {
        if (!unActiveDbs.isEmpty()) {
            for (InnerActiveDb innerActiveDb : unActiveDbs) {
                String dbAlias = innerActiveDb.getName();
                try {
                    innerActiveDb.getDb().test();
                    // 从库如果有恢复的，则之前用主库作为从库使用的，重新作为主库使用
                    if (masterOrSlave) {
                        log.warn(MS_LOG_PRE + "主库{}回复正常", dbAlias);
                    } else {
                        // 激活从库
                        activeSlave(dbAlias);

                        if (slaveDbMapTem.isEmpty()) {
                            log.warn(MS_LOG_PRE + "从库[{}]恢复正常", dbAlias);
                        } else {
                            // 主库不空，则说明当前恢复的是从库中的第一个库
                            List<String> masterNameList = slaveDbMapTem.values().stream().map(InnerActiveDb::getName).collect(Collectors.toList());
                            cleanSlaveTemOfMaster(masterNameList);

                            log.warn(MS_LOG_PRE + "从库[{}]恢复正常，主库[{}]不再担任读取职责", dbAlias, masterNameList);
                        }
                    }
                } catch (Throwable ignore) {
                    if (masterOrSlave) {
                        log.info(MS_LOG_PRE + "尝试恢复主库{}失败", dbAlias);
                    } else {
                        log.info(MS_LOG_PRE + "尝试恢复从库{}失败", dbAlias);
                    }
                }
            }
        }
    }

    /**
     * 清理主库对应的临时从库
     */
    private void cleanSlaveTemOfMaster(List<String> masterNameList) {
        slaveKeys.removeAll(masterNameList);
        slaveIndex.set(0);
        slaveDbMapTem.clear();
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
    public InnerActiveDb selectMasterDb() {
        if (null != currentMasterDb) {
            return currentMasterDb;
        }

        throw new NeoException("主库没有设置或者不可用");
    }

    /**
     * 获取从库db
     *
     * <ul>
     * <li>1.首先从"从库"中查看从库数据</li>
     * <li>2.如果从库中没有数据，则会将"主库"中的库放到临时从库</li>
     * </ul>
     *
     * @return 从库db
     */
    @Override
    public InnerActiveDb selectSlaveDb() {
        Integer index = getNextIndex();
        if (null != index) {
            String dbAlias = slaveKeys.get(index);
            InnerActiveDb innerActiveDb;
            if (slaveDbMap.containsKey(dbAlias)) {
                innerActiveDb = slaveDbMap.get(dbAlias);
            } else if (slaveDbMapTem.containsKey(dbAlias)) {
                innerActiveDb = slaveDbMapTem.get(dbAlias);
            } else {
                throw new NeoException("从库获取失败");
            }
            return innerActiveDb;
        } else {
            // 从库都不可用，则采用主库，将主库添加到临时从库中
            InnerActiveDb db = selectMasterDb();
            if (null != db) {
                log.error(MS_LOG_PRE + "从库都不可用，走主库{}", db.getName());
                addSlaveDbTem(db);
                return selectSlaveDb();
            }
            throw new NeoException("没有可用的库");
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
