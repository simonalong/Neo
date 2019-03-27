package com.simon.neo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 关注
 * @author robot
 */
@Getter
@AllArgsConstructor
public enum EnumDemo {

    /**
     * 可用
     */
    Y("Y"),
    /**
     * 不可用
     */
    N("N");

    private String val;
}