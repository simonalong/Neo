package com.simon.neo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhouzhenyong
 * @since 2019/3/12 下午12:46
 */
public class NeoDb {

    /**
     * 库映射：一个库可以有多个域
     */
    private static Map<String, List<NeoDb>> catalogMap = new ConcurrentHashMap<>();
    /**
     * 域映射：一个域里面会有多个表
     */
    private Map<String, List<NeoTable>> schemaMap = new ConcurrentHashMap<>();
    /**
     * 库名字，我们这里设定为数据库的名字，比如mysql, sqlLite, postGreSql
     */
    private String catalogName;
    /**
     * 域名字，对应系统中业务划分的不同的划分区域名字，一个区域里面包含多个表
     */
    private String schemaName;

    private NeoDb(String catalogName, String schemaName) {
        this.catalogName = (null == catalogName) ? "default" : catalogName;
        this.schemaName = (null == schemaName) ? "default" : schemaName;
        catalogMap.compute(catalogName, (k, v) -> {
            if (null == v) {
                return new ArrayList<>();
            } else {
                v.add(this);
                return v;
            }
        });
    }

    public static NeoDb of(String schemaName, String dbName){
        return new NeoDb(schemaName, dbName);
    }

    public static NeoDb of(String dbName){
        return NeoDb.of(null, dbName);
    }

    /**
     * 如果数据表有自增的键，则返回该列
     * @param schema db的上一层，用于表示当前是什么库，比如mysql，仅仅是用于命名
     * @param dbName 所属的库的名字
     * @param tableName 库中的表名
     * @return 自增的主键的列名，如果没有，则返回null
     */
    public String getAutoIncrementName(String schema, String dbName, String tableName){
        return null;
    }

    public String getAutoIncrementName(String dbName, String tableName){
        return getAutoIncrementName(catalogName, dbName, tableName);
    }

    public String getAutoIncrementName(String tableName){
        return getAutoIncrementName(catalogName, schemaName, tableName);
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }
}
