package com.simonalong.neo.devide.strategy;

/**
 * @author shizi
 * @since 2020/6/10 6:26 PM
 */
public enum DevideTypeEnum {

    /**
     * 分库分表个数哈希
     */
    HASH,
    /**
     * 内置的全局id生成器的哈希
     */
    UUID_HASH;
}
