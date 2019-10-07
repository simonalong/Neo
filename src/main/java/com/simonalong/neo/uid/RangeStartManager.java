package com.simonalong.neo.uid;

import static com.simonalong.neo.uid.UidConstant.DEFAULT_STEP;
import static com.simonalong.neo.uid.UidConstant.REFRESH_RATIO;

import java.util.concurrent.atomic.AtomicLong;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * 缓存起点的管理器
 *
 * @author zhouzhenyong
 * @since 2019/5/2 上午9:59
 */
@Slf4j
public final class RangeStartManager {

    /**
     * 缓存1的全局起点
     */
    private volatile Long rangeStartOfBuf1;
    /**
     * 缓存1的全局起点
     */
    private volatile Long rangeEndOfBuf1;
    /**
     * 缓存2的全局终点
     */
    private volatile Long rangeStartOfBuf2;
    /**
     * 缓存2的全局终点
     */
    private volatile Long rangeEndOfBuf2;
    /**
     * 当前使用的全局起点
     */
    @Getter
    private volatile Long currentStart;

    /**
     * 初始化buf起点
     *
     * @param start 新的起点
     * @return 当前位置
     */
    Long initBufStart(Long start) {
        currentStart = start;
        rangeStartOfBuf1 = start;
        rangeEndOfBuf1 = start + DEFAULT_STEP;
        return start;
    }

    /**
     * 准备和刷新buf，如果满足刷新条件，则进行刷新二级buf
     *
     * @param uid 全局id
     * @return true:达到刷新条件，且未刷新，false:没有到达刷新条件，或者到达刷新条件，但是已经刷新
     */
    Boolean readyRefresh(long uid) {
        // 到达刷新位置，而且还没有刷新
        return uid >= currentStart + DEFAULT_STEP * REFRESH_RATIO / 100;
    }

    /**
     * 获取新的buf范围
     *
     * 将其中不是当前的另外一个buf设置为新的buf
     *
     * @param start 起点
     */
    void refreshRangeStart(Long start) {
        if (currentStart.equals(rangeStartOfBuf1)) {
            rangeStartOfBuf2 = start;
            rangeEndOfBuf2 = start + DEFAULT_STEP;
            return;
        }

        if (currentStart.equals(rangeStartOfBuf2)) {
            rangeStartOfBuf1 = start;
            rangeEndOfBuf1 = start + DEFAULT_STEP;
        }
    }

    /**
     * 到达其中一个buf的末尾部分
     *
     * @param uid 全局id
     * @return true：到达末尾，false：没有到末尾
     */
    Boolean reachBufEnd(Long uid) {
        Long start = rangeStartOfBuf1;
        Long end = rangeEndOfBuf1;
        if (currentStart.equals(rangeStartOfBuf2)) {
            start = rangeStartOfBuf2;
            end = rangeEndOfBuf2;
        }

        return uid < start || uid >= end;
    }

    /**
     * buf切换起点
     * <p>将current切换为另外的一个buf起点
     * @param uuidIndex 当前id的游标
     */
    void chgBufStart(AtomicLong uuidIndex) {
        if (currentStart.equals(rangeStartOfBuf1)) {
            log.debug("buf起点切换，currentStart = " + rangeStartOfBuf2);
            this.currentStart = rangeStartOfBuf2;
            uuidIndex.compareAndSet(rangeStartOfBuf1, rangeEndOfBuf2);
            return;
        }

        if (currentStart.equals(rangeStartOfBuf2)) {
            log.debug("buf起点切换，currentStart = " + rangeStartOfBuf1);
            this.currentStart = rangeStartOfBuf1;
            uuidIndex.compareAndSet(rangeStartOfBuf2, rangeEndOfBuf1);
        }
    }
}
