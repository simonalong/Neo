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
     * 数据库映射：一个系统可能有多个数据源, key为某个数据源的名字mysql，仅用于多数据库的区分
     */
    private Map<String, List<NeoDb>> rdbmsCatalogMap = new ConcurrentHashMap<>(3);
    /**
     * 域映射：一个域里面会有多个表, key为：NeoDb中的schema
     */
    private Map<String, List<NeoTable>> schemaMap = new ConcurrentHashMap<>();
    private Map<Neo, >

    private static DbManager instance = new DbManager();
    private DbManager(){}

    public static DbManager getInstance(){
        return instance;
    }

    /**
     *
     * @param neo Neo对象
     * @param rdbmsName 关系型数据库表示
     * @param catalog 目录
     * @param schema 模式
     */
    public DbManager add(Neo neo, String rdbmsName, String catalog, String schema){

    }

    public DbManager add(Neo neo, String rdbmsName, String cataloge){
        return add(neo, rdbmsName, cataloge, null);
    }

    public String getAutoIncrementName(String tableName){

    }


}
