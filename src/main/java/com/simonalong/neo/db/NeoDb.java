package com.simonalong.neo.db;

import static com.simonalong.neo.NeoConstant.ALL_FIELD;
import static com.simonalong.neo.NeoConstant.LOG_PRE_NEO;

import com.simonalong.neo.Neo;
import com.simonalong.neo.Pair;
import com.simonalong.neo.db.NeoColumn.NeoInnerColumn;
import com.simonalong.neo.db.NeoTable.Table;
import com.simonalong.neo.db.TableIndex.Index;
import com.simonalong.neo.exception.NeoException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhouzhenyong
 * @since 2019/3/12 下午12:46
 */
@Slf4j
@EqualsAndHashCode(of = {"rdbmsName", "catalogName", "schemaToTableMap"})
public final class NeoDb {

    private Neo neo;
    /**
     * 数据库映射：一个系统可能有多个数据源, 为某个数据源的名字mysql，仅用于多数据库的区分
     */
    private String rdbmsName;
    /**
     * catalog 名字，目录，注意：在某些数据库中，这个就是库名，这个跟NeoDb对象是一对一的
     */
    private final String catalogName;
    /**
     * schema（模式）和数据表的映射，其中catalog和schema有些数据库是不支持的，比如mysql就没有schema，则默认为：default
     */
    private final Map<String, Map<String, NeoTable>> schemaToTableMap = new ConcurrentHashMap<>(1);

    private NeoDb(Neo neo, String rdbmsName, String catalogName) {
        this.neo = neo;
        this.rdbmsName = base(rdbmsName);
        this.catalogName = base(catalogName);
    }

    private NeoDb(String catalogName) {
        this.catalogName = base(catalogName);
    }

    public static NeoDb of(Neo neo, String rdbmsName, String catalogName, String... tablePres){
        return new NeoDb(neo, rdbmsName, catalogName).init(tablePres);
    }

    public static NeoDb of(Neo neo, String catalogName, String... tablePres){
        return NeoDb.of(neo, null, catalogName, tablePres);
    }

    public static NeoDb of(Neo neo, String... tablePres) {
        return NeoDb.of(neo, null, null, tablePres);
    }

    private NeoDb init(String... tablePres){
        getAllTables(tablePres).forEach(this::initTable);
        return this;
    }

    /**
     * 如果数据表有自增的键，则返回该列
     * @param schemaName 模式名
     * @param tableName 库中的表名
     * @return 自增的主键的列名，如果没有，则返回null
     */
    public Pair<String, ? extends Class<?>> getPrimaryKeyAutoIncNameAndType(String schemaName, String tableName){
        schemaName = base(schemaName);
        NeoTable neoTable = getTable(schemaName, tableName);
        if (null != neoTable) {
            return neoTable.getPrimaryKeyAutoIncNameAndType();
        }
        return null;
    }

    /**
     * 获取主键而且是自增的列名
     * @param tableName 表名
     * @return 自增的主键列名
     */
    public Pair<String, ? extends Class<?>> getPrimaryKeyAutoIncNameAndType(String tableName){
        return getPrimaryKeyAutoIncNameAndType(null, tableName);
    }

    /**
     * 返回表的主键列名
     * @param schemaName 模式名
     * @param tableName 库中的表名
     * @return 主键的列名，如果没有，则返回null
     */
    public String getPrimaryName(String schemaName, String tableName){
        NeoTable neoTable = getTable(base(schemaName), tableName);
        if (null != neoTable) {
            return neoTable.getPrimary();
        }
        throw new NeoException("表"+tableName+"不存在");
    }

    public String getPrimaryName(String tableName){
        return getPrimaryName(null, tableName);
    }

    public void addTable(Neo neo, Table table){
        addTable(neo, null, table);
    }

    public void addTable(String tableName){
        init(tableName);
    }

    public void addTable(Neo neo, String schema, Table table){
        schema = base(schema);
        schemaToTableMap.compute(schema, (k, v)->{
            if(null == v){
                Map<String, NeoTable> tableMap = new HashMap<>();
                tableMap.put(table.getTableName(), new NeoTable(neo, table));
                return tableMap;
            }else{
                v.compute(table.getTableName(), (name, neoTable)->{
                    if(null == neoTable){
                        return new NeoTable(neo, table);
                    }else{
                        neoTable.setTableMeta(table).setNeo(neo);
                        return neoTable;
                    }
                });
                return v;
            }
        });
    }

    public void addColumn(String schema, String tableName, Set<NeoColumn> columnSet){
        schema = base(schema);
        schemaToTableMap.computeIfPresent(schema, (k, v) -> {
            NeoTable table = v.get(tableName);
            if(null != table) {
                table.setColumnSet(columnSet);
            }
            return v;
        });
    }

    public void addColumn(String tableName, Set<NeoColumn> columnSet){
        addColumn(null, tableName, columnSet);
    }

    public void setPrimaryKey(String schema, String tableName, String columnName){
        schema = base(schema);
        if(schemaToTableMap.containsKey(schema)){
            schemaToTableMap.get(schema).values().stream()
                .filter(t -> t.getTableName().equals(tableName)).findFirst()
                .ifPresent(t -> t.setPrimary(columnName));
        }
    }

    public NeoTable getTable(String schemaName, String tableName){
        schemaName = base(schemaName);
        NeoTable table = null;
        if (schemaToTableMap.containsKey(schemaName)) {
            table = schemaToTableMap.get(schemaName).values().stream().filter(t -> t.getTableName().equals(tableName))
                .findFirst().orElse(null);
        }

        if (null != table) {
            return table;
        }
        neo.checkDb(tableName);
        if (schemaToTableMap.containsKey(schemaName)) {
            table = schemaToTableMap.get(schemaName).values().stream().filter(t -> t.getTableName().equals(tableName))
                .findFirst().orElse(null);
        }

        if (null == table) {
            log.warn(LOG_PRE_NEO + "表" + tableName + "没有找到");
        }
        return table;
    }

    public NeoTable getTable(String tableName){
        return getTable(null, tableName);
    }

    public List<NeoTable> getTableList(String schema) {
        schema = base(schema);
        if (schemaToTableMap.containsKey(schema)) {
            return new ArrayList<>(schemaToTableMap.get(schema).values());
        }
        return Collections.emptyList();
    }

    public List<NeoTable> getTableList(){
        return getTableList(null);
    }

    public List<String> getTableNameList(String schema){
        List<NeoTable> tableList = getTableList(schema);
        if (!tableList.isEmpty()){
            return tableList.stream().map(NeoTable::getTableName).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public List<String> getTableNameList(){
        return getTableNameList(null);
    }

    public Boolean containTable(String tableName){
        return getTableNameList().contains(tableName);
    }

    public List<NeoColumn> getColumnList(String tableName){
        NeoTable neoTable = getTable(tableName);
        if (null == neoTable) {
            return Collections.emptyList();
        }
        return new ArrayList<>(neoTable.getColumnSet());
    }

    public List<Index> getIndexList(String tableName){
        NeoTable neoTable = getTable(tableName);
        if (null == neoTable) {
            return Collections.emptyList();
        }
        return neoTable.getIndexList();
    }

    public List<String> getIndexNameList(String tableName){
        NeoTable neoTable = getTable(tableName);
        if (null == neoTable) {
            return Collections.emptyList();
        }
        return neoTable.getIndexNameList();
    }

    private String base(String data){
        return (null == data) ? "default" : data;
    }


    /**
     * 获取table的信息
     */
    private Set<String> getAllTables(String... tablePres) {
        Set<String> tableSet = new HashSet<>();
        try (Connection con = neo.getConnection()) {
            getAllTables(con, tableSet, con.getCatalog(), tablePres);
        } catch (SQLException e) {
            if (e instanceof SQLFeatureNotSupportedException) {
                try (Connection con = neo.getConnection()) {
                    getAllTables(con, tableSet, null, tablePres);
                } catch (SQLException ex) {
                    log.error(LOG_PRE_NEO + "getAllTables error", e);
                }
            } else {
                log.error(LOG_PRE_NEO + "getAllTables error", e);
            }
        }
        return tableSet;
    }

    private void getAllTables(Connection con, Set<String> tableSet, String catalog, String... tablePres) throws SQLException {
        DatabaseMetaData dbMeta = con.getMetaData();
        ResultSet rs = dbMeta.getTables(catalog, null, null, new String[]{"TABLE"});
        List<String> tablePreList = Arrays.asList(tablePres);
        while (rs.next()) {
            Table table = Table.parse(neo, rs);
            if (concernTable(tablePreList, table.getTableName())){
                tableSet.add(table.getTableName());
                addTable(neo, table);
            }
        }
    }

    /**
     * 判断当前的表名是否是关心的表
     * @param concernTablePreList 关心的表前缀列表
     * @param tableName 待校验的表名
     * @return true：当前表关心
     */
    private Boolean concernTable(List<String> concernTablePreList, String tableName){
        // 若没有配置关心表前缀，则默认关心所有的表
        if (concernTablePreList.isEmpty()) {
            return true;
        }

        if (concernTablePreList.size() == 1 && concernTablePreList.get(0).equals(ALL_FIELD)) {
            return true;
        }
        return concernTablePreList.stream().anyMatch(tableName::startsWith);
    }

    private void initTable(String tableName){
        // 初始化表中的列信息
        initColumnMeta(tableName);
        // 初始化表的主键、外键和索引
        initPrimary(tableName);
        // 初始化索引
        initIndex(tableName);
    }

    /**
     * 初始化表中的列信息
     * @param tableName 表名
     */
    private void initColumnMeta(String tableName) {
        try (Connection con = neo.getConnection()) {
            try {
                Set<NeoColumn> columnSet = new HashSet<>();
                // 最后一个参数表示是否要求结果的准确性，倒数第二个表示是否唯一索引
                ResultSet rs = con.getMetaData().getColumns(con.getCatalog(), null, tableName, null);
                while (rs.next()) {
                    columnSet.add(NeoColumn.from(NeoInnerColumn.parse(neo, rs)));
                }

                addColumn(tableName, columnSet);
            } catch (SQLException e) {
                log.error(LOG_PRE_NEO + "generateColumnMetaMap error", e);
            }
        } catch (SQLException e) {
            log.error(LOG_PRE_NEO + "initColumnMeta error", e);
        }
    }

    /**
     * 生成列的元数据map
     *
     * @return key为列名，value为Column
     */
    private Map<String, NeoInnerColumn> generateColumnMetaMap(Connection conn, String tableName){
        Map<String, NeoInnerColumn> columnMap = new HashMap<>(16);
        try {
            // 最后一个参数表示是否要求结果的准确性，倒数第二个表示是否唯一索引
            ResultSet rs = conn.getMetaData().getColumns(conn.getCatalog(), null, tableName, null);
            while (rs.next()) {
                NeoInnerColumn innerColumn = NeoInnerColumn.parse(neo, rs);
                columnMap.put(innerColumn.getColumnName(), innerColumn);
            }
        } catch (SQLException e) {
            log.error(LOG_PRE_NEO + "generateColumnMetaMap error", e);
        }
        return columnMap;
    }

    /**
     * 主要是初始化表的一些信息：主键，外键，索引：这里先添加主键，其他的后面再说
     */
    private void initPrimary(String tableName) {
        try (Connection con = neo.getConnection()) {
            initPrimary(con, tableName, con.getCatalog(), con.getSchema());
        } catch (SQLException e) {
            if (e instanceof SQLFeatureNotSupportedException) {
                try (Connection con = neo.getConnection()) {
                    initPrimary(con, tableName, null, null);
                } catch (SQLException ex) {
                    log.error(LOG_PRE_NEO + "initPrimary error", ex);
                }
            } else {
                log.error(LOG_PRE_NEO + "initPrimary error", e);
            }
        }
    }

    private void initPrimary(Connection con, String tableName, String catalog, String schema) throws SQLException {
        DatabaseMetaData dbMeta = con.getMetaData();
        ResultSet rs = dbMeta.getPrimaryKeys(catalog, schema, tableName);
        if (rs.next()) {
            setPrimaryKey(schema, tableName, rs.getString("COLUMN_NAME"));
        }
    }

    /**
     * 主要是初始化表的索引信息：索引
     */
    private void initIndex(String tableName) {
        try (Connection con = neo.getConnection()) {
            initIndex(con, tableName, con.getCatalog(), con.getSchema());
        } catch (SQLException e) {
            if (e instanceof SQLFeatureNotSupportedException) {
                try (Connection con = neo.getConnection()) {
                    initIndex(con, tableName, null, null);
                } catch (SQLException ex) {
                    log.error(LOG_PRE_NEO + "initIndex error", ex);
                }
            } else {
                log.error(LOG_PRE_NEO + "initIndex error", e);
            }
        }
    }

    private void initIndex(Connection con, String tableName, String catalog, String schema) throws SQLException {
        DatabaseMetaData dbMeta = con.getMetaData();
        // 最后一个参数表示是否要求结果的准确性，倒数第二个表示是否唯一索引
        ResultSet rs = dbMeta.getIndexInfo(catalog, schema, tableName, false, true);
        while (rs.next()) {
            NeoTable table = getTable(tableName);
            if(null == table) {
                log.warn(LOG_PRE_NEO + "表" + tableName + "没有找到");
            }else{
                table.initIndex(rs);
            }
        }
    }
}
