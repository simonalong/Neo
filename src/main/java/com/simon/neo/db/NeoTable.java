package com.simon.neo.db;

import com.simon.neo.Columns;
import com.simon.neo.Neo;
import com.simon.neo.NeoMap;
import com.simon.neo.db.TableIndex.Index;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author zhouzhenyong
 * @since 2019/3/12 下午12:46
 */
public class NeoTable {

    /**
     * 实际获取数据的对象
     */
    @Setter
    Neo neo;
    /**
     * 表名
     */
    @Getter
    private String tableName;
    /**
     * 索引
     */
    private TableIndex index = new TableIndex();
    /**
     * 列信息
     */
    @Getter
    @Setter
    private Set<NeoColumn> columnList = new HashSet<>();
    @Getter
    private Table tableMata;

    public NeoTable(Neo neo, Table tableMata){
        this.neo = neo;
        this.tableMata = tableMata;
        this.tableName = tableMata.getTableName();
    }

    public NeoTable(Neo neo, String tableName, Set<NeoColumn> columnList){
        this.neo = neo;
        this.tableName = tableName;
        this.columnList = columnList;
    }

    /**
     * 数据插入
     * @param neoMap 待插入的数据
     * @return 插入之后的返回值
     */
    public NeoMap insert(NeoMap neoMap) {
        return neo.insert(tableName, neoMap);
    }

    @SuppressWarnings("unchecked")
    public <T> T insert(T entity) {
        return neo.insert(tableName, entity);
    }

    /**
     * 数据删除
     * @param searchMap where 后面的条件数据
     * @return 插入之后的返回值
     */
    public Integer delete(NeoMap searchMap) {
        return neo.delete(tableName, searchMap);
    }

    public Integer delete(Object entity) {
        return delete(NeoMap.from(entity));
    }

    /**
     * 数据更新
     * @param dataMap set的更新的数据
     * @param searchMap where后面的语句条件数据
     * @return 更新之后的返回值
     */
    public NeoMap update(NeoMap dataMap, NeoMap searchMap) {
        return neo.update(tableName, dataMap, searchMap);
    }

    @SuppressWarnings("unchecked")
    public <T> T update(T setEntity, NeoMap searchMap) {
        return update(NeoMap.from(setEntity), searchMap).as((Class<T>) setEntity.getClass());
    }

    public <T> T update(T setEntity, T searchEntity) {
        return update(setEntity, NeoMap.from(searchEntity));
    }

    public <T> NeoMap update(NeoMap setMap, T searchEntity) {
        return update(setMap, NeoMap.from(searchEntity));
    }

    /**
     * 查询一行的数据
     * @param sql 只接收select 方式
     * @param parameters 参数
     * @return 一个结果Map
     */
    public NeoMap exeOne(String sql, Object... parameters){
        return neo.exeOne(sql, parameters);
    }

    public <T> T exeOne(Class<T> tClass, String sql, Object... parameters){
        return exeOne(sql, parameters).as(tClass);
    }

    /**
     * 查询一行实体数据
     * @param columns 列名
     * @param searchMap 搜索条件
     * @param tailSql sql尾部后缀
     * @return 返回一个实体的Map影射
     */
    public NeoMap one(Columns columns, NeoMap searchMap, String tailSql) {
        return neo.one(tableName, columns, searchMap, tailSql);
    }

    @SuppressWarnings("unchecked")
    public <T> T one(Columns columns, T entity, String tailSql){
        return one(columns, NeoMap.from(entity), tailSql).as((Class<T>) entity.getClass());
    }

    public NeoMap one(NeoMap searchMap, String tailSql){
        return one(null, searchMap, tailSql);
    }

    public <T> T one(T entity, String tailSql){
        return one(null, entity, tailSql);
    }

    public NeoMap one(Columns columns, NeoMap searchMap){
        return one(columns, searchMap, null);
    }

    public <T> T one(Columns columns, T entity){
        return one(columns, entity, null);
    }

    public NeoMap one(NeoMap searchMap){
        return one(null, searchMap);
    }

    public <T> T one(T entity){
        return one(null, entity);
    }

    /**
     * 查询一行的数据
     * @param sql 只接收select 方式
     * @param parameters 参数
     * @return 一个结果Map
     */
    public List<NeoMap> exeList(String sql, Object... parameters){
        return neo.exeList(sql, parameters);
    }

    public <T> List<T> exeList(Class<T> tClass, String sql, Object... parameters){
        return neo.exeList(tClass, sql, parameters);
    }

    /**
     * 查询具体的数据列表
     * @param columns 列数据
     * @param searchMap 搜索条件
     * @param tailSql 尾部sql
     * @return 返回一列数据
     */
    public List<NeoMap> list(Columns columns, NeoMap searchMap, String tailSql) {
        return neo.list(tableName, columns, searchMap, tailSql);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> list(Columns columns, T entity, String tailSql){
        return neo.list(tableName, columns, entity, tailSql);
    }

    public List<NeoMap> list(NeoMap searchMap, String tailSql){
        return list(null, searchMap, tailSql);
    }

    public <T> List<T> list(T entity, String tailSql){
        return list(null, entity, tailSql);
    }

    public List<NeoMap> list(Columns columns, NeoMap searchMap){
        return list(columns, searchMap, null);
    }

    public <T> List<T> list(Columns columns, T entity){
        return list(columns, entity, null);
    }

    public List<NeoMap> list(NeoMap searchMap){
        return list(searchMap, null);
    }

    public <T> List<T> list(T entity){
        return list(entity, null);
    }

    /**
     * 查询一行的数据
     * <p>
     * @param tClass 目标类
     * @param sql 只接收select 方式
     * @param parameters 参数
     * @param <T> 目标类型
     * @return 一个结果Map
     */
    public <T> T exeValue(Class<T> tClass, String sql, Object... parameters) {
        return neo.exeValue(tClass, sql, parameters);
    }

    public String exeValue(String sql, Object... parameters){
        return exeValue(String.class, sql, parameters);
    }

    /**
     * 查询某行某列的值
     * @param tClass 返回值的类型
     * @param field 某个属性的名字
     * @param searchMap 搜索条件
     * @param tailSql 尾部sql，比如：order by `xxx`
     * @param <T> 目标类型
     * @return 指定的数据值
     */
    public <T> T value(Class<T> tClass, String field, NeoMap searchMap, String tailSql){
        return neo.value(tClass, tableName, field, searchMap, tailSql);
    }

    public <T> T value(Class<T> tClass, String field, Object entity, String tailSql) {
        return value(tClass, field, NeoMap.from(entity), tailSql);
    }

    public <T> T value(Class<T> tClass, String field, NeoMap searchMap) {
        return value(tClass, field, searchMap, null);
    }

    public <T> T value(Class<T> tClass, String field, Object entity) {
        return value(tClass, field, entity, null);
    }

    public String value(String field, NeoMap searchMap, String tailSql){
        return value(String.class, field, searchMap, tailSql);
    }

    public String value(String field, Object entity, String tailSql) {
        return value(String.class, field, entity, tailSql);
    }

    public String value(String field, NeoMap searchMap) {
        return value(field, searchMap, null);
    }

    public String value(String field, Object entity) {
        return value(field, entity, null);
    }

    /**
     * 查询一行的数据
     * @param tClass 数据实体的类
     * @param sql 查询一行的sql
     * @param parameters 查询的搜索参数
     * @param <T> 数据实体的类型
     * @return 查询到的数据实体，如果没有找到则返回null
     */
    public <T> List<T> exeValues(Class<T> tClass, String sql, Object... parameters) {
        return neo.exeValues(tClass, sql, parameters);
    }

    public List<String> exeValues(String sql, Object... parameters){
        return exeValues(String.class, sql, parameters);
    }

    /**
     * 查询一列的值
     * @param tClass 实体类的类
     * @param field 列名
     * @param searchMap 搜索条件
     * @param tailSql sql尾部，比如order by `xxx`
     * @param <T> 目标类型
     * @return 一列值
     */
    public <T> List<T> values(Class<T> tClass, String field, NeoMap searchMap, String tailSql){
        return neo.values(tClass, tableName, field, searchMap, tailSql);
    }

    public <T> List<T> values(Class<T> tClass, String field, Object entity, String tailSql) {
        return values(tClass, field, NeoMap.from(entity), tailSql);
    }

    public <T> List<T> values(Class<T> tClass, String field, NeoMap searchMap) {
        return values(tClass, field, searchMap, null);
    }

    public <T> List<T> values(Class<T> tClass, String field, Object entity) {
        return values(tClass, field, entity, null);
    }

    public List<String> values(String field, NeoMap searchMap, String tailSql) {
        return values(String.class, field, searchMap, tailSql);
    }

    public List<String> values(String field, Object entity, String tailSql) {
        return values(String.class, field, entity, tailSql);
    }

    public List<String> values(String field, NeoMap searchMap) {
        return values(String.class, field, searchMap);
    }

    public List<String> values(String field, Object entity) {
        return values(String.class, field, entity, null);
    }

    /**
     * 分组数据
     * @param columns   列的属性
     * @param searchMap 搜索条件
     * @param tailSql   sql后缀，比如：order by `xxx`
     * @param startIndex 分页起始
     * @param pageSize  分页大小
     * @return 分页对应的数据
     */
    public List<NeoMap> page(Columns columns, NeoMap searchMap, String tailSql, Integer startIndex,
        Integer pageSize) {
        return neo.page(tableName, columns, searchMap, tailSql, startIndex, pageSize);
    }

    public List<NeoMap> page(NeoMap searchMap, String tailSql, Integer startIndex, Integer pageSize){
        return page(null, searchMap, tailSql, startIndex, pageSize);
    }

    public List<NeoMap> page(Columns columns, NeoMap searchMap, Integer startIndex, Integer pageSize){
        return page(columns, searchMap, null, startIndex, pageSize);
    }

    public List<NeoMap> page(NeoMap searchMap, Integer startIndex, Integer pageSize){
        return page(null, searchMap, startIndex, pageSize);
    }

    public List<NeoMap> page(Columns columns, NeoMap searchMap, String tailSql, NeoPage page){
        return page(columns, searchMap, tailSql, page.startIndex(), page.pageSize());
    }

    public List<NeoMap> page(NeoMap searchMap, String tailSql, NeoPage page){
        return page(null, searchMap, tailSql, page.startIndex(), page.pageSize());
    }

    public List<NeoMap> page(Columns columns, NeoMap searchMap, NeoPage page){
        return page(columns, searchMap, null, page.startIndex(), page.pageSize());
    }

    public List<NeoMap> page(NeoMap searchMap, NeoPage page){
        return page(null, searchMap, page.startIndex(), page.pageSize());
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> page(Columns columns, T entity, String tailSql, Integer startIndex,
        Integer pageSize) {
        return neo.page(tableName, columns, entity, tailSql, startIndex, pageSize);
    }

    public <T> List<T> page(T entity, String tailSql, Integer startIndex, Integer pageSize){
        return page(null, entity, tailSql, startIndex, pageSize);
    }

    public <T> List<T> page(Columns columns, T entity, Integer startIndex, Integer pageSize){
        return page(columns, entity, null, startIndex, pageSize);
    }

    public <T> List<T> page(T entity, Integer startIndex, Integer pageSize){
        return page(null, entity, startIndex, pageSize);
    }

    public <T> List<T> page(Columns columns, T entity, String tailSql, NeoPage page){
        return page(columns, entity, tailSql, page.startIndex(), page.pageSize());
    }

    public <T> List<T> page(T entity, String tailSql, NeoPage page){
        return page(null, entity, tailSql, page.startIndex(), page.pageSize());
    }

    public <T> List<T> page(Columns columns, T entity, NeoPage page){
        return page(columns, entity, null, page.startIndex(), page.pageSize());
    }

    public <T> List<T> page(T entity, NeoPage page){
        return page(null, entity, page.startIndex(), page.pageSize());
    }

    /**
     * 查询一行的数据
     * @param sql 只接收select 方式
     * @param parameters 参数
     * @return 一个结果Map
     */
    public Integer exeCount(String sql, Object... parameters) {
        return neo.exeCount(sql, parameters);
    }

    public Integer count(NeoMap searchMap) {
        return neo.count(tableName, searchMap);
    }

    public Integer count(Object entity) {
        return count(NeoMap.from(entity));
    }

    public Integer count() {
        return count(NeoMap.of());
    }

    /**
     * 默认的join采用的是innerJoin
     *
     * @param rightTableName 右表表名
     * @return 做关联的关联器
     */
    public NeoJoiner join(String rightTableName){
        return neo.innerJoin(tableName, rightTableName);
    }

    /**
     * 左关联，只保留左表的信息
     *
     * @param rightTableName 右表表名
     * @return 做关联的关联器
     */
    public NeoJoiner leftJoin(String rightTableName){
        return neo.leftJoin(tableName, rightTableName);
    }

    /**
     * 左关联，只保留左表的信息
     *
     * @param rightTableName 右表表名
     * @return 做关联的关联器
     */
    public NeoJoiner rightJoin(String rightTableName){
        return neo.rightJoin(tableName, rightTableName);
    }

    /**
     * 左关联，只保留左表的信息
     *
     * @param rightTableName 右表表名
     * @return 做关联的关联器
     */
    public NeoJoiner innerJoin(String rightTableName){
        return neo.innerJoin(tableName, rightTableName);
    }

    /**
     * 左关联，只保留左表的信息
     *
     * @param rightTableName 右表表名
     * @return 做关联的关联器
     */
    public NeoJoiner outerJoin(String rightTableName){
        return neo.outerJoin(tableName, rightTableName);
    }

    /**
     * 左关联，只保留左表的信息
     *
     * @param rightTableName 右表表名
     * @return 做关联的关联器
     */
    public NeoJoiner leftJoinExceptInner(String rightTableName){
        return neo.leftJoinExceptInner(tableName, rightTableName);
    }

    /**
     * 左关联，只保留左表的信息
     *
     * @param rightTableName 右表表名
     * @return 做关联的关联器
     */
    public NeoJoiner rightJoinExceptInner(String rightTableName){
        return neo.rightJoinExceptInner(tableName, rightTableName);
    }

    /**
     * 左关联，只保留左表的信息
     *
     * @param rightTableName 右表表名
     * @return 做关联的关联器
     */
    public NeoJoiner outerJoinExceptInner(String rightTableName){
        return neo.outerJoinExceptInner(tableName, rightTableName);
    }

    public void initIndex(ResultSet resultSet){
        index.add(resultSet);
    }

    public List<String> getIndexNameList(){
        return index.getIndexNameList();
    }

    public List<Index> getIndexList(){
        return index.getIndexList();
    }

    public List<String> getColumnNameList(){
        return getColumnList().stream().map(NeoColumn::getColumnName).collect(Collectors.toList());
    }

    /**
     * 获取创建sql的语句
     * {@code
     * create table xxx{
     *     id xxxx;
     * } comment ='xxxx';
     * }
     * <p>
     * @return 表创建的sql语句
     */
    public String getTableCreate(){
        return (String) (neo.execute("show create table `" + tableName + "`").get(0).get(0).get("Create Table"));
    }

    /**
     * 获取表中的自增的主键名字
     * <p>
     * @return 主键且自增的列的名字
     */
    String getPrimaryKeyAutoIncName() {
        return columnList.stream().filter(NeoColumn::isPrimaryAndAutoInc).map(NeoColumn::getColumnName).findFirst()
            .orElse(null);
    }

    /**
     * 获取表中的主键名字
     * <p>
     * @return 主键的列名
     */
    public String getPrimary() {
        return columnList.stream().filter(NeoColumn::getIsPrimaryKey).map(NeoColumn::getColumnName).findFirst()
            .orElse(null);
    }

    public void setPrimary(String columnName) {
        for (NeoColumn column : columnList) {
            if(column.getColumnName().equals(columnName)){
                column.setIsPrimaryKey(true);
            }
        }
    }

    public NeoTable setTableMeta(Table tableMeta){
        this.tableMata = tableMeta;
        this.tableName = tableMeta.getTableName();
        return this;
    }

    @Override
    public int hashCode(){
        return tableName.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof NeoTable){
            NeoTable objTable = NeoTable.class.cast(obj);
            return tableName.equals(objTable.getTableName());
        }
        return false;
    }

    @Data
    @Accessors(chain = true)
    public static class Table{

        private static final String TABLE_CAT = "TABLE_CAT";
        private static final String TABLE_SCHEM = "TABLE_SCHEM";
        private static final String TABLE_NAME = "TABLE_NAME";
        private static final String TABLE_TYPE = "TABLE_TYPE";
        private static final String REMARKS = "REMARKS";
        private static final String TYPE_CAT = "TYPE_CAT";
        private static final String TYPE_SCHEM = "TYPE_SCHEM";
        private static final String TYPE_NAME = "TYPE_NAME";
        private static final String SELF_REFERENCING_COL_NAME = "SELF_REFERENCING_COL_NAME";
        private static final String REF_GENERATION = "REF_GENERATION";

        /**
         * String => table catalog（可以为null）
         */
        private String catalog;
        /**
         * String => table schema（可以为null）
         */
        private String schema;
        /**
         * String => table name
         */
        private String tableName;
        /**
         * String => table type。典型的类型是“TABLE”，“VIEW”，“SYSTEM TABLE”，“GLOBAL TEMPORARY”，“LOCAL TEMPORARY”，“ALIAS”，“SYNONYM”。
         */
        private String tableType;
        /**
         * String =>对表的解释性注释
         */
        private String remarks;
        /**
         * String =>类型目录（可以为null）
         */
        private String typeCatalog;
        /**
         * String =>类型模式（可以为null）
         */
        private String typeSchema;
        /**
         * String => type name（可以为null）
         */
        private String typeName;
        /**
         * String => name of指定的表的“identifier”列（可以为null）
         */
        private String selfReferencingColName;
        /**
         * String =>指定如何创建SELF_REFERENCING_COL_NAME中的值。值为“SYSTEM”，“USER”，“DERIVED”。（可能为null）
         */
        private String refGeneration;

        private Table(){}

        public static Table parse(ResultSet rs){
            try {
                return new Table()
                    .setCatalog(rs.getString(TABLE_CAT))
                    .setSchema(rs.getString(TABLE_SCHEM))
                    .setTableName(rs.getString(TABLE_NAME))
                    .setTableType(rs.getString(TABLE_TYPE))
                    .setRemarks(rs.getString(REMARKS))
                    .setTypeCatalog(rs.getString(TYPE_CAT))
                    .setTypeSchema(rs.getString(TYPE_SCHEM))
                    .setTypeName(rs.getString(TYPE_NAME))
                    .setSelfReferencingColName(rs.getString(SELF_REFERENCING_COL_NAME))
                    .setRefGeneration(rs.getString(REF_GENERATION));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return new Table();
        }
    }
}
