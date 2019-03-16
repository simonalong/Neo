package com.simon.neo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhouzhenyong
 * @since 2019/3/16 下午10:50
 */
public class DbManager {

    /**
     * 数据库映射：一个系统可能有多个
     */
    private Map<String, List<String>> dbMap = new ConcurrentHashMap<>();
    /**
     * 库映射：一个库可以有多个域
     */
    private static Map<String, List<NeoDb>> catalogMap = new ConcurrentHashMap<>();
    /**
     * 域映射：一个域里面会有多个表
     */
    private Map<String, List<NeoTable>> schemaMap = new ConcurrentHashMap<>();
}
