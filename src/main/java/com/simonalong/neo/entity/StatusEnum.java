package com.simonalong.neo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 状态
 * @author robot
 */
@Getter
@AllArgsConstructor
public enum StatusEnum {

    /**
     * 禁用
     */
    N("N"),
    /**
     * 启用
     */
    Y("Y"),
;

    private String value;
}