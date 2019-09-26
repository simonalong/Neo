package com.simonalong.neo.uid;

import static com.simonalong.neo.uid.UidConstant.DEFAULT_STEP;
import static com.simonalong.neo.uid.UidConstant.TABLE_ID;
import static com.simonalong.neo.uid.UidConstant.UUID_TABLE;

import com.simonalong.neo.Neo;
import com.simonalong.neo.NeoMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
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
     * 范围管理器
     */
    private RangeStartManager rangeManager;
    /**
     * 每200毫秒重试一次，重试5次
     */
    private final RetryTask<Long> retryTask = new RetryTask<>(200, TimeUnit.MILLISECONDS, 5);
    /**
     * 全局id
     */
    private volatile AtomicLong uuidIndex = new AtomicLong();
    /**
     * id生成表是否已经初始化
     */
    private volatile Boolean tableInitFlag = false;
    /**
     * buffer刷新标识。0：无buffer刷新，1：准备刷新buffer，2：buffer刷新完成
     */
    private AtomicInteger bufferRefreshFlag = new AtomicInteger(0);
    /**
     * 二级buffer刷新锁
     */
    private ReentrantLock lock = new ReentrantLock();
    /**
     * 数据刷新完毕
     */
    private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private static volatile UidGenerator instance;

    private UidGenerator(){}

    /**
     * 全局id生成器的构造函数
     *
     * @param neo 数据库对象
     * @return 全局id生成器对象
     */
    public static UidGenerator getInstance(Neo neo) {
        if (null == instance) {
            synchronized (UidGenerator.class) {
                if (null == instance) {
                    instance = new UidGenerator();
                    instance.neo = neo;
                    instance.init();
                }
            }
        }
        return instance;
    }

    public Long getUuid() {
        return retryTask.call(() -> {
            Long uid = uuidIndex.getAndIncrement();
            freshSecondBuf(uid);
            if (rangeManager.reachBufEnd(uid)) {
                chgNewBuffer();
                return getUuid();
            }
            return uid;
        });
    }

    private void freshSecondBuf(Long uid){
        // 到达刷新比率
        if (rangeManager.readyRefresh(uid)) {
            lock.lock();
            try {
                if (rangeManager.readyRefresh(uid)) {
                    // 只让其中一个业务线程进行刷新，不阻塞其他业务线程，让其他线程继续执行后面逻辑
                    if(0 == bufferRefreshFlag.get()) {
                        log.info("fresh second buf");
                        // 同步化获取新的范围
                        WriteLock writeLock = readWriteLock.writeLock();
                        writeLock.lock();

                        bufferRefreshFlag.set(1);
                        lock.unlock();
                        try {
                            rangeManager.refreshRangeStart(allocStart());
                            bufferRefreshFlag.set(2);
                        } finally {
                            writeLock.unlock();
                        }
                    }
                }
            } finally {
                if (lock.isHeldByCurrentThread() && lock.isLocked()) {
                    lock.unlock();
                }
            }
        }
    }

    private void chgNewBuffer() {
        synchronized (this) {
            if (0 != bufferRefreshFlag.get()) {
                ReadLock readLock = readWriteLock.readLock();
                readLock.lock();
                try {
                    rangeManager.chgBufStart(uuidIndex);
                    log.info("second buf reach finish newBufStart:{}", rangeManager.getCurrentStart());
                    bufferRefreshFlag.set(0);
                } finally {
                    readLock.unlock();
                }
            }
        }
    }

    private void init() {
        rangeManager = new RangeStartManager();
        tableInitPreHandle();
        uuidIndex.set(rangeManager.initBufStart(allocStart()));
    }

    /**
     * 数据库分配新的范围起点
     *
     * @return 返回数据库最新分配的值
     */
    private Long allocStart() {
        return neo.tx(() -> {
            Long value = neo.exeValue(Long.class, "select `uuid` from %s where `id` = ? for update", UUID_TABLE, TABLE_ID);
            neo.execute("update %s set `uuid` = `uuid` + ? where `id` = ?", UUID_TABLE, DEFAULT_STEP, TABLE_ID);
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
