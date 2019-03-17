package com.simon.neo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;

/**
 * @author zhouzhenyong
 * @since 2019/3/12 下午12:46
 */
public class NeoTable {

    /**
     * 实际获取数据的对象
     */
    Neo neo;
    /**
     * 表名
     */
    @Getter
    private String tableName;
    /**
     * 表详解
     */
    private String tableDesc;
    /**
     * 主键
     */
    private TablePrimaryKey primary;
    /**
     * 外键
     */
    private List<TableForeignKey> foreignColumnList;
    /**
     * 索引
     */
    private List<TableIndex> tableIndexList;
    /**
     * 列信息
     */
    private Set<NeoColumn> columnList = new HashSet<>();

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
    public Long delete(NeoMap searchMap) {
        return neo.delete(tableName, searchMap);
    }

    public Long delete(Object entity) {
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
     * @param sql 只接收select 方式
     * @param parameters 参数
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
     * @return 指定的数据值
     */
    public <T> T value(Class<T> tClass, String field, NeoMap searchMap, String tailSql){
        return neo.value(tableName, tClass, field, searchMap, tailSql);
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
     * @return 一列值
     */
    public <T> List<T> values(Class<T> tClass, String field, NeoMap searchMap, String tailSql){
        return neo.values(tableName, tClass, field, searchMap, tailSql);
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
     * 获取表中的自增的主键名字
     */
    String getPrimaryKeyAutoIncName() {
        return columnList.stream().filter(NeoColumn::isPrimaryAndAutoInc).map(NeoColumn::getColumnName).findFirst()
            .orElse(null);
    }

    public void setPrimary(String columnName) {
        for (NeoColumn column : columnList) {
            if(column.getColumnName().equals(columnName)){
                column.setIsPrimaryKey(true);
            }
        }
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
}
