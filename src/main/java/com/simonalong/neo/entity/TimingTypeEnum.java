package com.simonalong.neo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 实效性
 * @author robot
 */
@Getter
@AllArgsConstructor
public enum TimingTypeEnum {

    /**
     * 指定时间失效
     */
    ASSIGN_UNAVAILABLE("ASSIGN_UNAVAILABLE"),
    /**
     * 永久有效
     */
    PERM_AVAILABLE("PERM_AVAILABLE"),
    /**
     * 调度周期失效
     */
    SCHEDULE_UNAVAILABLE("SCHEDULE_UNAVAILABLE"),
;

    private String value;
}