package com.simon.neo.db;

import com.simon.neo.Columns;
import com.simon.neo.NeoMap;
import java.util.List;

/**
 * @author zhouzhenyong
 * @since 2019/4/20 下午9:46
 */
public class NeoJoiner {

    public NeoJoiner(String tableLeft, String tableRight) {
        //todo
    }

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
     * join核查中的查询一个数据
     *
     * @param leftColumns 左表的列名
     * @param rightColumns 右表的列名
     * @param searchMap 搜索条件
     * @param tailSql sql的尾部语句
     * @return join执行后的结果
     */
    public NeoMap one(Columns leftColumns, Columns rightColumns, NeoMap searchMap, String tailSql){
        // todo
        return null;
    }

    public NeoMap one(Columns leftColumns, Columns rightColumns, NeoMap searchMap){
        return one(leftColumns, rightColumns, searchMap, "");
    }

    public NeoMap one(Columns leftColumns, NeoMap searchMap, String tailSql){
        return one(leftColumns, Columns.of(), searchMap, tailSql);
    }

    public NeoMap one(Columns leftColumns, NeoMap searchMap){
        return one(leftColumns, Columns.of(), searchMap);
    }

    @SuppressWarnings("unchecked")
    public <T> T one(Columns leftColumns, Columns rightColumns, T entity, String tailSql){
        return one(leftColumns, rightColumns, NeoMap.of(entity), tailSql).as((Class<T>) entity.getClass());
    }

    @SuppressWarnings("unchecked")
    public <T> T one(Columns leftColumns, Columns rightColumns, T entity){
        return one(leftColumns, rightColumns, NeoMap.of(entity), "").as((Class<T>) entity.getClass());
    }

    @SuppressWarnings("unchecked")
    public <T> T one(Columns leftColumns, T entity, String tailSql){
        return one(leftColumns, Columns.of(), NeoMap.of(entity), tailSql).as((Class<T>) entity.getClass());
    }

    @SuppressWarnings("unchecked")
    public <T> T one(Columns leftColumns, T entity){
        return one(leftColumns, Columns.of(), NeoMap.of(entity)).as((Class<T>) entity.getClass());
    }

    public NeoMap one(Columns leftColumns, Columns rightColumns, String tailSql){
        return one(leftColumns, rightColumns, NeoMap.of(), tailSql);
    }

    public NeoMap one(Columns leftColumns, Columns rightColumns){
        return one(leftColumns, rightColumns, "");
    }

    public NeoMap one(Columns leftColumns, String tailSql){
        return one(leftColumns, Columns.of(), tailSql);
    }

    public NeoMap one(Columns leftColumns){
        return one(leftColumns, Columns.of());
    }

    /**
     * join核查中的查询一个数据
     *
     * @param leftColumns 左表的列名
     * @param rightColumns 右表的列名
     * @param searchMap 搜索条件
     * @param tailSql sql的尾部语句
     * @return join执行后的结果
     */
    public List<NeoMap> list(Columns leftColumns, Columns rightColumns, NeoMap searchMap, String tailSql){
        // todo
        return null;
    }

    public List<NeoMap> list(Columns leftColumns, Columns rightColumns, NeoMap searchMap){
        return list(leftColumns, rightColumns, searchMap, "");
    }

    public List<NeoMap> list(Columns leftColumns, NeoMap searchMap, String tailSql){
        return list(leftColumns, Columns.of(), searchMap, tailSql);
    }

    public List<NeoMap> list(Columns leftColumns, NeoMap searchMap){
        return list(leftColumns, Columns.of(), searchMap);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> list(Columns leftColumns, Columns rightColumns, T entity, String tailSql){
        return NeoMap.asArray(list(leftColumns, rightColumns, NeoMap.of(entity), tailSql), (Class<T>) entity.getClass());
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> list(Columns leftColumns, Columns rightColumns, T entity){
        return NeoMap.asArray(list(leftColumns, rightColumns, NeoMap.of(entity), ""), (Class<T>) entity.getClass());
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> list(Columns leftColumns, T entity, String tailSql){
        return NeoMap.asArray(list(leftColumns, Columns.of(), NeoMap.of(entity), tailSql), (Class<T>) entity.getClass());
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> list(Columns leftColumns, T entity){
        return NeoMap.asArray(list(leftColumns, Columns.of(), NeoMap.of(entity)), (Class<T>) entity.getClass());
    }

    public List<NeoMap> list(Columns leftColumns, Columns rightColumns, String tailSql){
        return list(leftColumns, rightColumns, NeoMap.of(), tailSql);
    }

    public List<NeoMap> list(Columns leftColumns, Columns rightColumns){
        return list(leftColumns, rightColumns, "");
    }

    public List<NeoMap> list(Columns leftColumns, String tailSql){
        return list(leftColumns, Columns.of(), tailSql);
    }

    public List<NeoMap> list(Columns leftColumns){
        return list(leftColumns, Columns.of());
    }

    /**
     * join核查中的查询一个数据
     *
     * @param searchMap 搜索条件
     * @param tailSql sql的尾部语句
     * @return join执行后的结果
     */
    @SuppressWarnings("unchecked")
    public <T> T value(String columnName, Class<T> tClass, NeoMap searchMap, String tailSql){
        // todo
        return null;
    }

    @SuppressWarnings("unchecked")
    public <T> T value(String columnName, Class<T> tClass, NeoMap searchMap){
        return value(columnName, tClass, searchMap, "");
    }

    public String value(String columnName, NeoMap searchMap, String tailSql){
        return value(columnName, String.class, searchMap, tailSql);
    }

    public String value(String columnName, NeoMap searchMap){
        return value(columnName, searchMap, "");
    }

    @SuppressWarnings("unchecked")
    public <T> T value(String columnName, Class<T> tClass, Object entity, String tailSql){
        return value(columnName, tClass, NeoMap.from(entity), tailSql);
    }

    @SuppressWarnings("unchecked")
    public <T> T value(String columnName, Class<T> tClass, Object entity){
        return value(columnName, tClass, NeoMap.from(entity), "");
    }

    public String value(String columnName, Object entity, String tailSql){
        return value(columnName, String.class, NeoMap.from(entity), tailSql);
    }

    public String value(String columnName, Object entity){
        return value(columnName, NeoMap.from(entity), "");
    }

    @SuppressWarnings("unchecked")
    public <T> T value(String columnName, Class<T> tClass, String tailSql){
        return value(columnName, tClass, NeoMap.of(), tailSql);
    }

    @SuppressWarnings("unchecked")
    public <T> T value(String columnName, Class<T> tClass){
        return value(columnName, tClass, "");
    }

    public String value(String columnName, String tailSql){
        return value(columnName, String.class, tailSql);
    }

    public String value(String columnName){
        return value(columnName, "");
    }

    /**
     * join核查中的查询一个数据
     *
     * @param searchMap 搜索条件
     * @param tailSql sql的尾部语句
     * @return join执行后的结果
     */
    @SuppressWarnings("unchecked")
    public <T> T values(String columnName, Class<T> tClass, NeoMap searchMap, String tailSql){
        // todo
        return null;
    }

    @SuppressWarnings("unchecked")
    public <T> T values(String columnName, Class<T> tClass, NeoMap searchMap){
        return values(columnName, tClass, searchMap, "");
    }

    public String values(String columnName, NeoMap searchMap, String tailSql){
        return values(columnName, String.class, searchMap, tailSql);
    }

    public String values(String columnName, NeoMap searchMap){
        return values(columnName, searchMap, "");
    }

    @SuppressWarnings("unchecked")
    public <T> T values(String columnName, Class<T> tClass, Object entity, String tailSql){
        return values(columnName, tClass, NeoMap.from(entity), tailSql);
    }

    @SuppressWarnings("unchecked")
    public <T> T values(String columnName, Class<T> tClass, Object entity){
        return values(columnName, tClass, NeoMap.from(entity), "");
    }

    public String values(String columnName, Object entity, String tailSql){
        return values(columnName, String.class, NeoMap.from(entity), tailSql);
    }

    public String values(String columnName, Object entity){
        return values(columnName, NeoMap.from(entity), "");
    }

    @SuppressWarnings("unchecked")
    public <T> T values(String columnName, Class<T> tClass, String tailSql){
        return value(columnName, tClass, NeoMap.of(), tailSql);
    }

    @SuppressWarnings("unchecked")
    public <T> T values(String columnName, Class<T> tClass){
        return values(columnName, tClass, "");
    }

    public String values(String columnName, String tailSql){
        return values(columnName, String.class, tailSql);
    }

    public String values(String columnName){
        return values(columnName, "");
    }
}
