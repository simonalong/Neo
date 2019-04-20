package com.simon.neo.db;

import com.simon.neo.Columns;
import com.simon.neo.NeoMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author zhouzhenyong
 * @since 2019/4/20 下午9:46
 */
public class NeoJoiner {

    public NeoJoiner(String tableLeft, String tableRight) {}

    /**
     * join 的关联字段
     *
     * @param leftColumnName 左表的列名
     * @param rightColumnName 右表的列名
     * @return 返回关联之后的Joiner
     */
    public NeoJoiner on(String leftColumnName, String rightColumnName){
        // todo
        return this;
    }

    /**
     * 该函数查询是select * xxx，请尽量不要用，用具体的列即可
     * @param tableName 表名
     * @param entity 搜索的实体类型数据
     * @param tailSql sql的尾部sql填充
     * @param <T> 插入的对象类型
     * @return 插入的对象类型
     */
    public <T> T one(String tableName, T entity, String tailSql){
        return one(tableName, null, entity, tailSql);
    }

    public NeoMap one(String tableName, Columns columns, NeoMap searchMap){
        return one(tableName, columns, searchMap, null);
    }

    public <T> T one(String tableName, Columns columns, T entity){
        return one(tableName, columns, entity, null);
    }

    /**
     * 该函数查询是select * xxx，请尽量不要用，用具体的列即可
     * @param tableName 表名
     * @param searchMap 搜索的映射
     * @return NeoMap对象
     */
    public NeoMap one(String tableName, NeoMap searchMap){
        return one(tableName, null, searchMap);
    }

    /**
     * 该函数查询是select * xxx，请尽量不要用，用具体的列即可
     * @param tableName 表名
     * @param entity 查询的实体数据
     * @param <T> 实体的类型映射
     * @return 实体的类型
     */
    public <T> T one(String tableName, T entity){
        return one(tableName, null, entity);
    }

    /**
     * 查询具体的数据列表
     * @param tableName 表名
     * @param columns 列数据
     * @param searchMap 搜索条件
     * @param tailSql 尾部sql
     * @return 返回一列数据
     */
    public List<NeoMap> list(String tableName, Columns columns, NeoMap searchMap, String tailSql) {
        return execute(true, () -> generateListSqlPair(tableName, columns, searchMap, tailSql), this::executeList);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> list(String tableName, Columns columns, T entity, String tailSql){
        return NeoMap.asArray(list(tableName, columns, NeoMap.from(entity), tailSql), (Class<T>) entity.getClass());
    }

    public List<NeoMap> list(String tableName, NeoMap searchMap, String tailSql){
        return list(tableName, null, searchMap, tailSql);
    }

    public <T> List<T> list(String tableName, T entity, String tailSql){
        return list(tableName, null, entity, tailSql);
    }

    public List<NeoMap> list(String tableName, Columns columns, NeoMap searchMap){
        return list(tableName, columns, searchMap, null);
    }

    public <T> List<T> list(String tableName, Columns columns, T entity){
        return list(tableName, columns, entity, null);
    }

    public List<NeoMap> list(String tableName, NeoMap searchMap){
        return list(tableName, searchMap, null);
    }

    public <T> List<T> list(String tableName, T entity){
        return list(tableName, entity, null);
    }

    /**
     * 查询某行某列的值
     * @param tableName 表名
     * @param tClass 返回值的类型
     * @param field 某个属性的名字
     * @param searchMap 搜索条件
     * @param tailSql 尾部sql，比如：order by `xxx`
     * @param <T> 目标类型
     * @return 指定的数据值
     */
    public <T> T value(String tableName, Class<T> tClass, String field, NeoMap searchMap, String tailSql) {
        if (null != tClass && !NeoMap.isEmpty(searchMap)) {
            NeoMap result = execute(false, () -> generateValueSqlPair(tableName, field, searchMap, tailSql), this::executeOne);
            if (null != result) {
                Iterator<Object> it = result.values().iterator();
                return it.hasNext() ? asObject(tClass, it.next()) : null;
            }
        }
        return null;
    }

    public <T> T value(String tableName, Class<T> tClass, String field, Object entity, String tailSql) {
        return value(tableName, tClass, field, NeoMap.from(entity), tailSql);
    }

    public <T> T value(String tableName, Class<T> tClass, String field, NeoMap searchMap) {
        return value(tableName, tClass, field, searchMap, null);
    }

    public <T> T value(String tableName, Class<T> tClass, String field, Object entity) {
        return value(tableName, tClass, field, entity, null);
    }

    public String value(String tableName, String field, NeoMap searchMap, String tailSql){
        return value(tableName, String.class, field, searchMap, tailSql);
    }

    public String value(String tableName, String field, Object entity, String tailSql) {
        return value(tableName, String.class, field, entity, tailSql);
    }

    public String value(String tableName, String field, NeoMap searchMap) {
        return value(tableName, field, searchMap, null);
    }

    public String value(String tableName, String field, Object entity) {
        return value(tableName, field, entity, null);
    }

    /**
     * 查询一列的值
     * @param tableName 表名
     * @param tClass 实体类的类
     * @param field 列名
     * @param searchMap 搜索条件
     * @param tailSql sql尾部，比如order by `xxx`
     * @param <T> 目标类型
     * @return 一列值
     */
    public <T> List<T> values(String tableName, Class<T> tClass, String field, NeoMap searchMap, String tailSql){
        List<NeoMap> resultList = execute(false, () -> generateValuesSqlPair(tableName, field, searchMap, tailSql), this::executeList);

        if(null != resultList && !resultList.isEmpty()){
            return resultList.stream()
                .map(r -> asObject(tClass, r.get(field)))
                .filter(Objects::nonNull).distinct()
                .collect(Collectors.toList());
        }
        return null;
    }

    public <T> List<T> values(String tableName, Class<T> tClass, String field, Object entity, String tailSql) {
        return values(tableName, tClass, field, NeoMap.from(entity), tailSql);
    }

    public <T> List<T> values(String tableName, Class<T> tClass, String field, NeoMap searchMap) {
        return values(tableName, tClass, field, searchMap, null);
    }

    public <T> List<T> values(String tableName, Class<T> tClass, String field, Object entity) {
        return values(tableName, tClass, field, entity, null);
    }

    public List<String> values(String tableName, String field, NeoMap searchMap, String tailSql) {
        return values(tableName, String.class, field, searchMap, tailSql);
    }

    public List<String> values(String tableName, String field, Object entity, String tailSql) {
        return values(tableName, String.class, field, entity, tailSql);
    }

    public List<String> values(String tableName, String field, NeoMap searchMap) {
        return values(tableName, String.class, field, searchMap);
    }

    public List<String> values(String tableName, String field, Object entity) {
        return values(tableName, String.class, field, entity, null);
    }

}
