package com.simonalong.neo;

/**
 * @author shizi
 * @since 2020/3/22 下午2:42
 */
public interface Pair<K, V> {

    /**
     * 获取key
     * @return key
     */
    K getKey();

    /**
     * 获取value
     * @return value
     */
    V getValue();
}
