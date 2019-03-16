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
     * catalog 名字，目录，注意：在某些数据库中，这个就是库名
     */
    private String catalogName;
    /**
     * schema（模式）和数据表的映射，其中catalog和schema有些数据库是不支持的，比如mysql就没有schema，则默认为：_default_
     */
    private Map<String, List<NeoTable>> schemaToTableMap = new ConcurrentHashMap<>();

    private NeoDb(String catalogName, String schemaName, String tableName) {
        this.catalogName = (null == catalogName) ? "default" : catalogName;
        schemaName = (null == schemaName) ? "default" : schemaName;
        schemaToTableMap.compute(schemaName, (k,v)->{
           if(null == v){
               List<NeoTable> tableList = new ArrayList<>();
               tableList.add(NeoTable.of(tableName));
               return tableList;
           }else{
               v.add(NeoTable.of(tableName));
               return v;
           }
        });
    }

    private NeoDb(String catalogName) {
        this.catalogName = (null == catalogName) ? "default" : catalogName;
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
