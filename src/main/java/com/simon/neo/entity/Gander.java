package com.simon.neo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 性别
 * @author robot
 */
@Getter
@AllArgsConstructor
public enum Gander {

    /**
     * 男
     */
    Y("Y"),
    /**
     * 女
     */
    N("N"),
;

    private String value;
}