package com.simon.neo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhouzhenyong
 * @since 2019/3/12 下午12:46
 */
public class NeoDb {

    private Map<String, NeoTable> tableMap = new ConcurrentHashMap<>();
}
