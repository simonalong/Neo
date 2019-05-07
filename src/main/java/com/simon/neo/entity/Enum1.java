package com.simon.neo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 性别
 * @author robot
 */
@Getter
@AllArgsConstructor
public enum Enum1 {

    b("b"),
    a("a"),
;

    private String value;
}