package com.github.simonalong.neo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zhouzhenyong
 * @since 2019/5/6 下午9:40
 */
@AllArgsConstructor
@Getter
public enum  EnumEntity {

    A1("a1"),
    A2("a2");

    private String name;
}
