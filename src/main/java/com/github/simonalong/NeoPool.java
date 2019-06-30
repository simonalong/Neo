package com.github.simonalong;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.sql.DataSource;

/**
 * 多db管理对象
 * @author zhouzhenyong
 * @since 2019/6/21 下午3:48
 */
public final class NeoPool {

    private static Map<String, Neo> neoMap = new ConcurrentHashMap<>(8);
    private static final NeoPool INSTANCE = new NeoPool();

    public static NeoPool getInstance(){
        return INSTANCE;
    }

    /**
     * 添加db数据
     * @param alias 数据库别名
     * @param dataSource 数据库链接对象
     * @return pool对象
     */
    public NeoPool add(String alias, DataSource dataSource){
        neoMap.putIfAbsent(alias, Neo.connect(dataSource));
        return this;
    }

    /**
     * 添加db数据
     * @param alias 数据库别名
     * @param neo 数据库对象
     * @return pool对象
     */
    public NeoPool add(String alias, Neo neo){
        neoMap.putIfAbsent(alias, neo);
        return this;
    }

    /**
     * 获取对应的db数据
     * @param alias db别名
     * @return neo对象
     */
    public Neo get(String alias){
        return neoMap.get(alias);
    }
}