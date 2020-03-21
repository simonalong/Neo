package com.simonalong.neo.uid;

import static com.simonalong.neo.uid.UidConstant.UUID_TABLE;
import static com.simonalong.neo.uid.UidConstant.UUID_TABLE_NAMESPACE;

import com.simonalong.neo.Neo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.simonalong.neo.NeoMap;
import com.simonalong.neo.exception.UuidException;
import com.simonalong.neo.uid.snowflake.exception.SnowflakeException;
import com.simonalong.neo.uid.snowflake.splicer.DefaultUuidSplicer;
import com.simonalong.neo.uid.snowflake.splicer.UUidSplicer;
import lombok.extern.slf4j.Slf4j;

/**
 * 分布式全局id生成器
 *
 * @author zhouzhenyong
 * @since 2019/5/1 下午10:22
 */
@Slf4j
public final class UuidGenerator {

    private Neo neo;
    private static volatile UuidGenerator instance;
    /**
     * key为对应业务命名空间，value为uuid的序列构造器
     */
    private Map<String, UUidSplicer> uUidBuilderMap = new HashMap<>();

    private UuidGenerator() {}

    /**
     * 全局id生成器的构造函数
     *
     * @param neo 数据库对象
     * @return 全局id生成器对象
     */
    public static UuidGenerator getInstance(Neo neo) {
        if (null == instance) {
            synchronized (UuidGenerator.class) {
                if (null == instance) {
                    instance = new UuidGenerator();
                    instance.neo = neo;
                    instance.init();
                }
            }
        }
        return instance;
    }

    private void init() {
        if (null == neo) {
            throw new UuidException("数据库对象为空");
        }
        if (!neo.tableExist(UUID_TABLE)) {
            throw new UuidException("数据库uuid表不存在，请创建表 neo_uuid_generator");
        }
    }

    public void addNamespaces(String... namespaces) {
        Arrays.stream(namespaces).peek(this::checkNamespace).forEach(n -> addUUidSplicer(n, new DefaultUuidSplicer(n, neo)));
    }

    /**
     * 核查配置的业务空间是否存在
     *
     * @param namespace 业务的命名空间
     */
    private void checkNamespace(String namespace) {
        if (null == neo.one(UUID_TABLE, NeoMap.of(UUID_TABLE_NAMESPACE, namespace))) {
            throw new SnowflakeException("业务空间" + namespace + "不存在，请先添加该命名空间");
        }
    }

    /**
     * 添加对应业务命名空间的uuid构造器
     *
     * @param namespace          业务命名空间
     * @param defaultUUidSplicer uuid构造器
     */
    private void addUUidSplicer(String namespace, UUidSplicer defaultUUidSplicer) {
        uUidBuilderMap.putIfAbsent(namespace, defaultUUidSplicer);
    }

    /**
     * 获取对应命名空间的全局id
     *
     * @param namespace 业务的命名空间
     * @return 全局id生成器
     */
    public long getUUid(String namespace) {
        return getUUidSplicer(namespace).splice();
    }

    public UUidSplicer getUUidSplicer(String namespace) {
        if (!uUidBuilderMap.containsKey(namespace)) {
            throw new SnowflakeException("命名空间" + namespace + "不存在，请确认是否注册或者查看mode是否匹配");
        }
        return uUidBuilderMap.get(namespace);
    }






    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //    /**
    //     * 范围管理器
    //     */
    //    private RangeStartManager rangeManager;
    //    /**
    //     * 每200毫秒重试一次，重试5次
    //     */
    //    private final RetryTask<Long> retryTask = new RetryTask<>(200, TimeUnit.MILLISECONDS, 5);
    //    /**
    //     * 全局id
    //     */
    //    private volatile AtomicLong uuidIndex = new AtomicLong();
    //    /**
    //     * id生成表是否已经初始化
    //     */
    //    private volatile Boolean tableInitFlag = false;
    //    /**
    //     * buffer刷新标识。0：无buffer刷新，1：准备刷新buffer，2：buffer刷新完成
    //     */
    //    private AtomicInteger bufferRefreshFlag = new AtomicInteger(0);
    //    /**
    //     * 二级buffer刷新锁
    //     */
    //    private ReentrantLock lock = new ReentrantLock();
    //    /**
    //     * 数据刷新完毕
    //     */
    //    private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    //
    //
    //    public Long getUUid() {
    //        return retryTask.call(() -> {
    //            Long uid = uuidIndex.getAndIncrement();
    //            freshSecondBuf(uid);
    //            if (rangeManager.reachBufEnd(uid)) {
    //                chgNewBuffer();
    //                return getUUid();
    //            }
    //            return uid;
    //        });
    //    }
    //
    //    private void freshSecondBuf(Long uid) {
    //        // 到达刷新比率
    //        if (rangeManager.readyRefresh(uid)) {
    //            lock.lock();
    //            try {
    //                if (rangeManager.readyRefresh(uid)) {
    //                    // 只让其中一个业务线程进行刷新，不阻塞其他业务线程，让其他线程继续执行后面逻辑
    //                    if(0 == bufferRefreshFlag.get()) {
    //                        log.info("fresh second buf");
    //                        // 同步化获取新的范围
    //                        WriteLock writeLock = readWriteLock.writeLock();
    //                        writeLock.lock();
    //
    //                        bufferRefreshFlag.set(1);
    //                        lock.unlock();
    //                        try {
    //                            rangeManager.refreshRangeStart(allocStart());
    //                            bufferRefreshFlag.set(2);
    //                        } finally {
    //                            writeLock.unlock();
    //                        }
    //                    }
    //                }
    //            } finally {
    //                if (lock.isHeldByCurrentThread() && lock.isLocked()) {
    //                    lock.unlock();
    //                }
    //            }
    //        }
    //    }
    //
    //    private void chgNewBuffer() {
    //        synchronized (this) {
    //            if (0 != bufferRefreshFlag.get()) {
    //                ReadLock readLock = readWriteLock.readLock();
    //                readLock.lock();
    //                try {
    //                    rangeManager.chgBufStart(uuidIndex);
    //                    log.info("second buf reach finish newBufStart:{}", rangeManager.getCurrentStart());
    //                    bufferRefreshFlag.set(0);
    //                } finally {
    //                    readLock.unlock();
    //                }
    //            }
    //        }
    //    }
    //

    //
    //    /**
    //     * 数据库分配新的范围起点
    //     *
    //     * @return 返回数据库最新分配的值
    //     */
    //    private Long allocStart() {
    //        return neo.tx(() -> {
    //            Long value = neo.exeValue(Long.class, "select `uuid` from %s where `id` = ? for update", UUID_TABLE, TABLE_ID);
    //            neo.execute("update %s set `uuid` = `uuid` + ? where `id` = ?", UUID_TABLE, DEFAULT_STEP, TABLE_ID);
    //            return value;
    //        });
    //    }
    //
    //    /**
    //     * 用于全局表的初始化，若全局表没有创建，则创建
    //     */
    //    private void tableInitPreHandle() {
    //        if (!tableInitFlag) {
    //            synchronized (UuidGenerator.class) {
    //                if (!tableInitFlag) {
    //                    initTable();
    //                    tableInitFlag = true;
    //                }
    //            }
    //        }
    //    }
    //
    //    private void initTable() {
    //        if (!neo.tableExist(UUID_TABLE)) {
    //            throw new NeoException("全局id生成器表" + UUID_TABLE + "没有创建，请先创建");
    //        }
    //
    //        neo.initDb();
    //        if (NeoMap.isEmpty(neo.one(UUID_TABLE, TABLE_ID))) {
    //            neo.insert(UUID_TABLE, NeoMap.of("id", TABLE_ID, "uuid", 1));
    //        }
    //    }
}
