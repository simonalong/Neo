package com.simonalong.neo.uid;

import com.simonalong.neo.Neo;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.exception.RefreshRatioException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;
import lombok.extern.slf4j.Slf4j;

/**
 * 分布式全局id生成器
 * @author zhouzhenyong
 * @since 2019/5/1 下午10:22
 */
@Slf4j
public final class UidGenerator {

    private Neo neo;
    /**
     * 步长
     */
    private Integer stepSize;
    /**
     * 全局id生成器的表名
     */
    private static final String UUID_TABLE = "neo_id_generator";
    /**
     * 全局id生成器表的唯一id
     */
    private static final Integer TABLE_ID = 1;
    /**
     * id生成表是否已经初始化
     */
    private volatile Boolean tableInitFlag = false;
    /**
     * 全局id
     */
    private volatile AtomicLong uuidIndex = new AtomicLong();
    /**
     * 范围管理器
     */
    private RangeStartManager rangeManager;
    /**
     * buffer切换标识，只有在buffer切换成功之后才设置为true
     */
    private volatile Boolean haveChanged = false;
    /**
     * 二级buffer刷新锁
     */
    private ReentrantLock lock = new ReentrantLock();
    /**
     * 数据刷新完毕
     */
    private ReentrantReadWriteLock secondBufRefredLock = new ReentrantReadWriteLock();
    /**
     * 每两秒重试一次，重试3次
     */
    private final RetryTask retryTask = RetryTask.getInstance(2, TimeUnit.SECONDS, 3);

    private static volatile UidGenerator instance;

    private UidGenerator(){}

    /**
     * 全局id生成器的构造函数
     *
     * @param neo 数据库对象
     * @param stepSize 步长
     * @param refreshRatio 刷新第二缓存的比率，用于在到达一定长度时候设置新的全局id起点，范围：0~1.0
     * @return 全局id生成器对象
     */
    public static UidGenerator getInstance(Neo neo, Integer stepSize, Float refreshRatio){
        if (refreshRatio < 0.0 || refreshRatio > 1.0) {
            throw new RefreshRatioException("参数：refreshRation不合法，为" + refreshRatio);
        }
        if (null == instance) {
            synchronized (UidGenerator.class) {
                if (null == instance) {
                    instance = new UidGenerator();
                    instance.neo = neo;
                    instance.stepSize = stepSize;
                    instance.init(neo, stepSize, refreshRatio);
                }
            }
        }
        return instance;
    }

    public Long getUid() {
        Long uid = uuidIndex.getAndIncrement();
        // 到达刷新buf的位置则进行刷新二级缓存
        if (rangeManager.readyRefresh(uid)) {
            lock.lock();
            try {
                if (rangeManager.readyRefresh(uid)) {
                    log.info("thread:{} refresh second buffer", Thread.currentThread().getName());
                    rangeManager.setRefreshFinish();
                    lock.unlock();

                    // 异步化获取新的范围
                    CompletableFuture.runAsync(() -> {
                        WriteLock writeLock = secondBufRefredLock.writeLock();
                        writeLock.lock();
                        try {
                            retryTask.run(() -> {
                                rangeManager.refreshRangeStart(allocStart());
                                // 刷新新buffer之后，需要设置buffer切换标识
                                haveChanged = false;
                            });
                        } finally {
                            writeLock.unlock();
                        }
                    });
                }
            } finally {
                if (lock.isLocked()) {
                    lock.unlock();
                }
            }
        }

        if (rangeManager.reachBufEnd(uid) != 0 && !haveChanged) {
            synchronized (UidGenerator.class) {
                if (rangeManager.reachBufEnd(uid) != 0 && !haveChanged) {
                    log.info("thread:{} buffer chg ", Thread.currentThread().getName());
                    haveChanged = true;

                    // 二级缓存切换
                    ReadLock readLock = secondBufRefredLock.readLock();
                    readLock.lock();
                    try {
                        uuidIndex.set(rangeManager.chgBufStart());
                    } finally {
                        readLock.unlock();
                    }
                }
            }
            return uuidIndex.getAndIncrement();
        }
        return uid;
    }

    private void init(Neo neo, Integer stepSize, Float refreshRatio) {
        rangeManager = new RangeStartManager(neo, stepSize, getRefreshBufSize(stepSize, refreshRatio));
        tableInitPreHandle();
        this.uuidIndex.set(rangeManager.initBufStart(allocStart()));
    }

    /**
     * 设置刷新尺寸
     * @param stepSize 步长
     * @param refreshRatio float类型的刷新比率
     */
    private Integer getRefreshBufSize(Integer stepSize, Float refreshRatio) {
        return (int) (stepSize * refreshRatio);
    }

    /**
     * 数据库分配新的范围起点
     *
     * @return 返回数据库最新分配的值
     */
    private Long allocStart() {
        return neo.tx(() -> {
            Long value = neo.value(UUID_TABLE, Long.class, "uuid", NeoMap.of("id", TABLE_ID));
            neo.execute("update %s set `uuid` = `uuid` + ? where `id` = ?", UUID_TABLE, stepSize, TABLE_ID);
            return value;
        });
    }

    /**
     * 用于全局表的初始化，若全局表没有创建，则创建
     */
    private void tableInitPreHandle() {
        if (!tableInitFlag) {
            synchronized (UidGenerator.class) {
                if (!tableInitFlag) {
                    initTable();
                    tableInitFlag = true;
                }
            }
        }
    }

    private void initTable() {
        if (!neo.tableExist(UUID_TABLE)) {
            neo.execute(uidTableCreateSql());
            neo.initDb();
            neo.insert(UUID_TABLE, NeoMap.of("id", TABLE_ID, "uuid", 1));
        }
    }

    private String uidTableCreateSql() {
        return "create table `" + UUID_TABLE + "` (\n"
            + "  `id` int(11) not null,\n"
            + "  `uuid` bigint(20) not null default 0,\n"
            + "  primary key (`id`)\n"
            + ") ENGINE=InnoDB DEFAULT CHARSET=utf8";
    }
}
