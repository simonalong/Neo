package com.simon.neo;

import com.simon.neo.TableIndex.Index;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author zhouzhenyong
 * @since 2019/3/12 下午12:46
 */
public class NeoDb {

    private Neo neo;
    /**
     * 数据库映射：一个系统可能有多个数据源, 为某个数据源的名字mysql，仅用于多数据库的区分
     */
    private String rdbmsName;
    /**
     * catalog 名字，目录，注意：在某些数据库中，这个就是库名，这个跟NeoDb对象是一对一的
     */
    private String catalogName;
    /**
     * schema（模式）和数据表的映射，其中catalog和schema有些数据库是不支持的，比如mysql就没有schema，则默认为：default
     */
    private Map<String, Set<NeoTable>> schemaToTableMap = new ConcurrentHashMap<>(1);

    private static final String DEFAULT = "default";

    private NeoDb(Neo neo, String rdbmsName, String catalogName) {
        this.neo = neo;
        this.rdbmsName = base(rdbmsName);
        this.catalogName = base(catalogName);
    }

    private NeoDb(String catalogName) {
        this.catalogName = base(catalogName);
    }

    public static NeoDb of(Neo neo, String rdbmsName, String catalogName){
        return new NeoDb(neo, rdbmsName, catalogName);
    }

    public static NeoDb of(Neo neo, String catalogName){
        return NeoDb.of(neo, null, catalogName);
    }

    /**
     * 如果数据表有自增的键，则返回该列
     * @param schemaName 模式名
     * @param tableName 库中的表名
     * @return 自增的主键的列名，如果没有，则返回null
     */

    public String getPrimaryAndAutoIncName(String schemaName, String tableName){
        schemaName = base(schemaName);
        NeoTable neoTable = getTable(schemaName, tableName);
        if (null != neoTable) {
            return neoTable.getPrimaryKeyAutoIncName();
        }
        return null;
    }

    public String getPrimaryAndAutoIncName(String tableName){
        return getPrimaryAndAutoIncName(null, tableName);
    }

    public void addColumn(Neo neo, String schema, String tableName, Set<NeoColumn> columnList){
        schema = base(schema);
        schemaToTableMap.compute(schema, (k,v)->{
           if(null == v){
               Set<NeoTable> tableSet = new HashSet<>();
               tableSet.add(new NeoTable(neo, tableName, columnList));
               return tableSet;
           }else{
               v.add(new NeoTable(neo, tableName, columnList));
               return v;
           }
        });
    }

    public void addColumn(Neo neo, String tableName, Set<NeoColumn> columnList){
        addColumn(neo, null, tableName, columnList);
    }

    public void setPrimaryKey(String schema, String tableName, String columnName){
        schema = base(schema);
        if(schemaToTableMap.containsKey(schema)){
            schemaToTableMap.get(schema).stream()
                .filter(t -> t.getTableName().equals(tableName)).findFirst()
                .ifPresent(t -> t.setPrimary(columnName));
        }
    }

    public NeoTable getTable(String schemaName, String tableName){
        schemaName = base(schemaName);
        if(schemaToTableMap.containsKey(schemaName)){
            return schemaToTableMap.get(schemaName).stream().filter(t -> t.getTableName().equals(tableName)).findFirst()
                .orElse(null);
        }
        return null;
    }

    public NeoTable getTable(String tableName){
        return getTable(null, tableName);
    }

    public Set<NeoTable> getTableList(String schema){
        schema = base(schema);
        return schemaToTableMap.get(schema);
    }

    public Set<NeoTable> getTableList(){
        return getTableList(null);
    }

    public List<String> getTableNameList(String schema){
        Set<NeoTable> tableList = getTableList(schema);
        if (null != tableList && !tableList.isEmpty()){
            return tableList.stream().map(NeoTable::getTableName).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public List<String> getTableNameList(){
        return getTableNameList(null);
    }

    public List<NeoColumn> getColumnList(String tableName){
        return new ArrayList<>(getTable(tableName).getColumnList());
    }

    public List<Index> getIndexList(String tableName){
        return getTable(tableName).getIndexList();
    }

    public List<String> getIndexNameList(String tableName){
        return getTable(tableName).getIndexNameList();
    }


    private String base(String data){
        return (null == data) ? "default" : data;
    }
}
