package com.simonalong.neo.express;

/**
 * @author shizi
 * @since 2021-11-05 01:51:07
 */
public interface MultiFunction<T, K, P, R> {

    R apply(T t, K k, P p);
}
