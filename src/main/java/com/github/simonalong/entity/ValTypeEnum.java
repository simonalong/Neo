package com.github.simonalong.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 值类型
 * @author robot
 */
@Getter
@AllArgsConstructor
public enum ValTypeEnum {

    /**
     * groovy脚本
     */
    GROOVY("GROOVY"),
    /**
     * 字符串
     */
    STRING("STRING"),
    /**
     * 配置类型
     */
    PROPERTY("PROPERTY"),
    /**
     * yaml类型
     */
    YML("YML"),
    /**
     * json类型
     */
    JSON("JSON"),
;

    private String value;
}