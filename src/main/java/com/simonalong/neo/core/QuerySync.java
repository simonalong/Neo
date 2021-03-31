package com.simonalong.neo.core;

import com.simonalong.neo.Columns;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.db.NeoPage;
import com.simonalong.neo.db.PageReq;
import com.simonalong.neo.db.PageRsp;
import com.simonalong.neo.express.SearchQuery;

import java.util.List;

/**
 * @author shizi
 * @since 2020/6/11 2:58 PM
 */
public interface QuerySync extends Sync {

    /**
     * 查询一行数据
     *
     * @param tableName 表名
     * @param columns   列名
     * @param searchMap 搜索条件
     * @return 一行数据对应的Map，key为列名，value为列对应的值
     */
    NeoMap one(String tableName, Columns columns, NeoMap searchMap);

    /**
     * 查询一行数据
     *
     * @param tableName     表名
     * @param columns       列名
     * @param searchQuery 搜索条件
     * @return 一行数据对应的Map，key为列名，value为列对应的值
     */
    NeoMap one(String tableName, Columns columns, SearchQuery searchQuery);

    /**
     * 查询一行数据
     *
     * @param tableName 表名
     * @param columns   列名
     * @param entity    搜索条件，实体也可以做搜索条件
     * @param <T>       查询的类型
     * @return 类型对应的值
     */
    <T> T one(String tableName, Columns columns, T entity);

    /**
     * 查询一行数据
     *
     * @param tableName 表名
     * @param columns   列名
     * @param key       主键
     * @return 返回一行对应的map
     */
    NeoMap one(String tableName, Columns columns, Number key);

    /**
     * 查询一行数据
     *
     * @param tableName 表名
     * @param searchMap 搜索条件
     * @return 一行数据对应的Map，key为列名，value为列对应的值
     */
    NeoMap one(String tableName, NeoMap searchMap);

    /**
     * 查询一行数据
     *
     * @param tableName 表名
     * @param entity    搜索实体
     * @param <T>       一行数据对应的类型
     * @return 返回对应的数据实体
     */
    <T> T one(String tableName, T entity);

    /**
     * 查询一行数据
     *
     * @param tableName 表名
     * @param id        主键
     * @return 一行数据对应的Map，key为列名，value为列对应的值
     */
    NeoMap one(String tableName, Number id);

    /**
     * 查询一行数据
     *
     * @param tableName     表名
     * @param searchQuery 复杂搜索条件
     * @return 一行数据对应的Map，key为列名，value为列对应的值
     */
    NeoMap one(String tableName, SearchQuery searchQuery);

    /**
     * 查询一行数据
     *
     * @param tClass    这行数据对应的类型
     * @param tableName 表名
     * @param columns   列名
     * @param searchMap 搜索条件
     * @param <T>       对应类型的泛型
     * @return 类型对应的值
     */
    default <T> T one(Class<T> tClass, String tableName, Columns columns, NeoMap searchMap) {
        NeoMap data = one(tableName, columns, searchMap);
        if (null == data) {
            return null;
        }
        return data.as(tClass);
    }

    /**
     * 查询一行数据
     *
     * @param tClass        这行数据对应的类型
     * @param tableName     表名
     * @param columns       列名
     * @param searchQuery 搜索条件
     * @param <T>           类型对应的泛型
     * @return 类型对应的值
     */
    default <T> T one(Class<T> tClass, String tableName, Columns columns, SearchQuery searchQuery) {
        NeoMap data = one(tableName, columns, searchQuery);
        if (null == data) {
            return null;
        }
        return data.as(tClass);
    }

    /**
     * 查询一行数据
     *
     * @param tClass    一行数据对应的类型
     * @param tableName 表名
     * @param columns   列名
     * @param key       主键
     * @param <T>       类型对应的发耐性
     * @return 类型对应的值
     */
    default <T> T one(Class<T> tClass, String tableName, Columns columns, Number key) {
        NeoMap data = one(tableName, columns, key);
        if (null == data) {
            return null;
        }
        return data.as(tClass);
    }

    /**
     * 查询一行数据
     *
     * @param tClass    一行数据对应的类型
     * @param tableName 表名
     * @param searchMap 搜索条件
     * @param <T>       类型对应的泛型
     * @return 类型对应的值
     */
    default <T> T one(Class<T> tClass, String tableName, NeoMap searchMap) {
        NeoMap data = one(tableName, searchMap);
        if (null == data) {
            return null;
        }
        return data.as(tClass);
    }

    /**
     * 查询一行数据
     *
     * @param tClass    一行数据对应的类型
     * @param tableName 表名
     * @param id        主键id
     * @param <T>       类型对应的泛型
     * @return 类型对应的值
     */
    default <T> T one(Class<T> tClass, String tableName, Number id) {
        NeoMap data = one(tableName, id);
        if (null == data) {
            return null;
        }
        return data.as(tClass);
    }

    /**
     * 查询一行数据
     *
     * @param tClass        一行数据对应的类型
     * @param tableName     表名
     * @param searchQuery 复杂搜索条件
     * @param <T>           类型对应的泛型
     * @return 类型对应的值
     */
    default <T> T one(Class<T> tClass, String tableName, SearchQuery searchQuery) {
        NeoMap data = one(tableName, searchQuery);
        if (null == data) {
            return null;
        }
        return data.as(tClass);
    }

    /**
     * 查询多行数据
     *
     * @param tableName 表名
     * @param columns   列名
     * @param searchMap 搜索条件
     * @return 多行数据
     */
    List<NeoMap> list(String tableName, Columns columns, NeoMap searchMap);

    /**
     * 查询多行数据
     *
     * @param tableName     表名
     * @param columns       列名
     * @param searchQuery 复杂搜索条件
     * @return 多行数据
     */
    List<NeoMap> list(String tableName, Columns columns, SearchQuery searchQuery);

    /**
     * 查询多行数据
     *
     * @param tableName 表名
     * @param columns   列名
     * @param entity    实体
     * @param <T>       搜索实体对应的泛型
     * @return 搜索实体对应的类型
     */
    <T> List<T> list(String tableName, Columns columns, T entity);

    /**
     * 查询多行数据
     *
     * @param tableName 表名
     * @param searchMap 搜索条件
     * @return 多行数据
     */
    List<NeoMap> list(String tableName, NeoMap searchMap);

    /**
     * 查询多行数据
     *
     * @param tableName 表名
     * @param entity    搜索实体
     * @param <T>       实体对应的泛型
     * @return 实体对应的集合
     */
    <T> List<T> list(String tableName, T entity);

    /**
     * 查询多行数据
     *
     * @param tableName 表名
     * @param columns   列名
     * @return 多行数据
     */
    List<NeoMap> list(String tableName, Columns columns);

    /**
     * 查询多行数据
     *
     * @param tableName     表名
     * @param searchQuery 搜索条件
     * @return 多行数据
     */
    List<NeoMap> list(String tableName, SearchQuery searchQuery);


    /**
     * 多行数据
     *
     * @param tClass    集合中的数据类型
     * @param tableName 表名
     * @param columns   列名
     * @param searchMap 搜索条件
     * @param <T>       数据类型对应的泛型
     * @return 多行数据
     */
    default <T> List<T> list(Class<T> tClass, String tableName, Columns columns, NeoMap searchMap) {
        return NeoMap.asArray(list(tableName, columns, searchMap), tClass);
    }

    /**
     * 多行数据
     *
     * @param tClass    集合中的数据类型
     * @param tableName 表名
     * @param columns   列名
     * @param express   复杂搜索条件
     * @param <T>       数据类型对应的泛型
     * @return 多行数据
     */
    default <T> List<T> list(Class<T> tClass, String tableName, Columns columns, SearchQuery express) {
        return NeoMap.asArray(list(tableName, columns, express), tClass);
    }

    /**
     * 多行数据
     *
     * @param tClass    集合中的数据类型
     * @param tableName 表名
     * @param searchMap 列名
     * @param <T>       数据类型对应的泛型
     * @return 多行数据
     */
    default <T> List<T> list(Class<T> tClass, String tableName, NeoMap searchMap) {
        return NeoMap.asArray(list(tableName, searchMap), tClass);
    }

    /**
     * 多行数据
     *
     * @param tClass    集合中对应的数据类型
     * @param tableName 表名
     * @param columns   列名
     * @param <T>       数据类型对应的泛型
     * @return 多行数据
     */
    default <T> List<T> list(Class<T> tClass, String tableName, Columns columns) {
        return NeoMap.asArray(list(tableName, columns), tClass);
    }

    /**
     * 多行数据
     *
     * @param tClass        集合中对应的数据类型
     * @param tableName     表名
     * @param searchQuery 列名
     * @param <T>           数据类型对应的泛型
     * @return 多行数据
     */
    default <T> List<T> list(Class<T> tClass, String tableName, SearchQuery searchQuery) {
        return NeoMap.asArray(list(tableName, searchQuery), tClass);
    }


    @Deprecated
    <T> T value(String tableName, Class<T> tClass, String field, NeoMap searchMap);

    @Deprecated
    <T> T value(String tableName, Class<T> tClass, String field, Object entity);

    /**
     * 查询某行某列的值
     *
     * @param tClass    某列对应的类型
     * @param tableName 表名
     * @param field     列名
     * @param searchMap 搜索条件
     * @param <T>       类型对应的泛型
     * @return 某行某列对应的某个值
     */
    <T> T value(Class<T> tClass, String tableName, String field, NeoMap searchMap);

    /**
     * 查询某行某列的值
     *
     * @param tClass        某列的值的对应类型
     * @param tableName     表名
     * @param field         列名
     * @param searchQuery 复杂搜索条件
     * @param <T>           类型对应的泛型
     * @return 某行某列的值
     */
    <T> T value(Class<T> tClass, String tableName, String field, SearchQuery searchQuery);

    /**
     * 查询某行某列的值
     *
     * @param tClass    数值对应的类型
     * @param tableName 表名
     * @param field     列名
     * @param entity    实体
     * @param <T>       类型对应的泛型
     * @return 某行某列的值
     */
    <T> T value(Class<T> tClass, String tableName, String field, Object entity);

    /**
     * 查询某行某列的值，返回String类型
     *
     * @param tableName 表名
     * @param field     列名
     * @param searchMap 搜索条件
     * @return 某行某列的值
     */
    String value(String tableName, String field, NeoMap searchMap);

    /**
     * 查询某行某列的值，返回String类型
     *
     * @param tableName     表名
     * @param field         列名
     * @param searchQuery 复杂搜索条件
     * @return 某列对应的值
     */
    String value(String tableName, String field, SearchQuery searchQuery);

    /**
     * 查询某行某列的值，返回String类型
     *
     * @param tableName 表名
     * @param field     列名
     * @param entity    搜索对应的实体
     * @return 某行某列的值，返回String类型
     */
    String value(String tableName, String field, Object entity);

    /**
     * 查询某个主键某列的值，返回String类型
     *
     * @param tableName 表名
     * @param field     列名
     * @param id        主键
     * @return 查询某个主键某列的值，返回String类型
     */
    String value(String tableName, String field, Number id);


    @Deprecated
    <T> List<T> values(String tableName, Class<T> tClass, String field, NeoMap searchMap);

    @Deprecated
    <T> List<T> values(String tableName, Class<T> tClass, String field, Object entity);

    /**
     * 查询某列的多个值
     *
     * @param tClass    值对应的类型
     * @param tableName 表名
     * @param field     列名
     * @param searchMap 搜索条件
     * @param <T>       类型对应的泛型
     * @return 某列值对应的集合
     */
    <T> List<T> values(Class<T> tClass, String tableName, String field, NeoMap searchMap);

    /**
     * 查询某列对应的集合
     *
     * @param tClass        值对应的类型
     * @param tableName     表名
     * @param field         列名
     * @param searchQuery 复杂查询条件
     * @param <T>           类型对应的泛型
     * @return 某列对应的集合
     */
    <T> List<T> values(Class<T> tClass, String tableName, String field, SearchQuery searchQuery);

    /**
     * 查询某列对应的集合
     *
     * @param tClass    某列对应的类型
     * @param tableName 表名
     * @param field     列名
     * @param entity    实体搜索条件
     * @param <T>       类型对应的泛型
     * @return 某列对应的集合
     */
    <T> List<T> values(Class<T> tClass, String tableName, String field, Object entity);

    /**
     * 查询某列对应的集合
     *
     * @param tableName 表名
     * @param field     列名
     * @param searchMap 搜索条件
     * @return 列对应的集合
     */
    List<String> values(String tableName, String field, NeoMap searchMap);

    /**
     * 某列对应的集合
     *
     * @param tableName     表名
     * @param field         列名
     * @param searchQuery 复杂搜索条件
     * @return 某列对应的集合，类型为String类型
     */
    List<String> values(String tableName, String field, SearchQuery searchQuery);

    /**
     * 某列对应的集合
     *
     * @param tableName 表名
     * @param field     列名
     * @param entity    实体对应的搜索条件
     * @return 某列对应的集合
     */
    List<String> values(String tableName, String field, Object entity);

    /**
     * 查询某列对应的集合
     *
     * @param tableName 表名
     * @param field     列名
     * @return 某列对应的集合
     */
    List<String> values(String tableName, String field);


    /**
     * 查询某列集合，列中的值去重
     *
     * @param tClass    类型
     * @param tableName 表名
     * @param field     属性名
     * @param searchMap 搜索条件
     * @param <T>       类型
     * @return 返回集合
     */
    <T> List<T> valuesOfDistinct(Class<T> tClass, String tableName, String field, NeoMap searchMap);

    /**
     * 查询某列集合，列中的值去重
     *
     * @param tClass        列值对应的类型
     * @param tableName     表名
     * @param field         列名
     * @param searchQuery 复杂搜索条件
     * @param <T>           类型对应的泛型
     * @return 去重后的某列的集合
     */
    <T> List<T> valuesOfDistinct(Class<T> tClass, String tableName, String field, SearchQuery searchQuery);

    /**
     * 查询某列集合，列中的值去重
     *
     * @param tClass    列值对应的类型
     * @param tableName 表名
     * @param field     列名
     * @param entity    实体搜索条件
     * @param <T>       类型对应的泛型
     * @return 去重后的某列的集合
     */
    <T> List<T> valuesOfDistinct(Class<T> tClass, String tableName, String field, Object entity);

    /**
     * 查询某列集合，列中的值去重
     *
     * @param tableName 表名
     * @param field     列名
     * @param searchMap 搜索条件
     * @return 去重后的某列的集合
     */
    List<String> valuesOfDistinct(String tableName, String field, NeoMap searchMap);

    /**
     * 查询某列集合，列中的值去重
     *
     * @param tableName     表名
     * @param field         列名
     * @param searchQuery 复杂搜索条件
     * @return 去重后的某列的集合
     */
    List<String> valuesOfDistinct(String tableName, String field, SearchQuery searchQuery);

    /**
     * 查询某列集合，列中的值去重
     *
     * @param tableName 表名
     * @param field     列名
     * @param entity    实体搜索条件
     * @return 去重后的某列的集合
     */
    List<String> valuesOfDistinct(String tableName, String field, Object entity);

    /**
     * 查询某列集合，列中的值去重
     *
     * @param tableName 表名
     * @param field     列名
     * @return 去重后的某列的集合
     */
    List<String> valuesOfDistinct(String tableName, String field);

    /**
     * 查询分页数据
     *
     * @param tableName 表名
     * @param columns   多个列名
     * @param searchMap 搜索条件
     * @param page      分页信息
     * @return 查询的分页数据
     */
    @Deprecated
    List<NeoMap> page(String tableName, Columns columns, NeoMap searchMap, NeoPage page);

    /**
     * 查询分页数据
     *
     * @param tableName     表名
     * @param columns       多个列名
     * @param searchQuery 复杂搜索条件
     * @param page          分页信息
     * @return 查询的分页数据
     */
    @Deprecated
    List<NeoMap> page(String tableName, Columns columns, SearchQuery searchQuery, NeoPage page);

    /**
     * 查询分页数据
     *
     * @param tableName 表名
     * @param columns   多个列名
     * @param entity    实体搜索条件
     * @param page      分页信息
     * @param <T>       分页数据对应类型的泛型
     * @return 分页数据
     */
    @Deprecated
    <T> List<T> page(String tableName, Columns columns, T entity, NeoPage page);

    /**
     * 查询分页数据
     *
     * @param tableName 表名
     * @param searchMap 搜索条件
     * @param page      分页信息
     * @return 分页数据
     */
    @Deprecated
    List<NeoMap> page(String tableName, NeoMap searchMap, NeoPage page);

    /**
     * 查询分页数据
     *
     * @param tableName     表名
     * @param searchQuery 复杂搜索条件
     * @param page          分页信息
     * @return 查询的分页数据
     */
    @Deprecated
    List<NeoMap> page(String tableName, SearchQuery searchQuery, NeoPage page);

    /**
     * 查询分页数据
     *
     * @param tableName 表名
     * @param entity    实体搜索条件
     * @param page      分页信息
     * @param <T>       分页数据对应类型的泛型
     * @return 分页数据
     */
    @Deprecated
    <T> List<T> page(String tableName, T entity, NeoPage page);

    /**
     * 查询分页数据
     *
     * @param tableName 表名
     * @param columns   多个列名
     * @param page      分页信息
     * @return 分页数据
     */
    @Deprecated
    List<NeoMap> page(String tableName, Columns columns, NeoPage page);

    /**
     * 查询分页数据
     *
     * @param tableName 表名
     * @param page      分页信息
     * @return 分页数据
     */
    @Deprecated
    List<NeoMap> page(String tableName, NeoPage page);


    /**
     * 查询分页数据
     *
     * @param tableName 表名
     * @param columns   多个列名
     * @param searchMap 搜索条件
     * @param pageReq   分页请求
     * @return 查询的分页数据
     */
    List<NeoMap> page(String tableName, Columns columns, NeoMap searchMap, PageReq<?> pageReq);

    /**
     * 查询分页数据
     *
     * @param tableName     表名
     * @param columns       多个列名
     * @param searchQuery 复杂搜索条件
     * @param pageReq       分页请求
     * @return 查询的分页数据
     */
    List<NeoMap> page(String tableName, Columns columns, SearchQuery searchQuery, PageReq<?> pageReq);

    /**
     * 查询分页数据
     *
     * @param tableName 表名
     * @param columns   多个列名
     * @param entity    实体搜索条件
     * @param pageReq   分页请求
     * @param <T>       分页数据对应类型的泛型
     * @return 分页数据
     */
    <T> List<T> page(String tableName, Columns columns, T entity, PageReq<?> pageReq);

    /**
     * 查询分页数据
     *
     * @param tableName 表名
     * @param searchMap 搜索条件
     * @param pageReq   分页请求
     * @return 分页数据
     */
    List<NeoMap> page(String tableName, NeoMap searchMap, PageReq<?> pageReq);

    /**
     * 查询分页数据
     *
     * @param tableName     表名
     * @param searchQuery 复杂搜索条件
     * @param pageReq       分页请求
     * @return 查询的分页数据
     */
    List<NeoMap> page(String tableName, SearchQuery searchQuery, PageReq<?> pageReq);

    /**
     * 查询分页数据
     *
     * @param tableName 表名
     * @param entity    实体搜索条件
     * @param pageReq   分页请求
     * @param <T>       分页数据对应类型的泛型
     * @return 分页数据
     */
    <T> List<T> page(String tableName, T entity, PageReq<?> pageReq);

    /**
     * 查询分页数据
     *
     * @param tableName 表名
     * @param columns   多个列名
     * @param pageReq   分页请求
     * @return 分页数据
     */
    List<NeoMap> page(String tableName, Columns columns, PageReq<?> pageReq);

    /**
     * 查询分页数据
     *
     * @param tableName 表名
     * @param pageReq   分页请求
     * @return 分页数据
     */
    List<NeoMap> page(String tableName, PageReq<?> pageReq);


    /**
     * 查询分页数据
     *
     * @param tClass    分页数据对应的类型
     * @param tableName 表名
     * @param columns   多个列名
     * @param searchMap 搜索条件
     * @param page      分页信息
     * @param <T>       分页数据对应类型的泛型
     * @return 分页数据
     */
    @Deprecated
    default <T> List<T> page(Class<T> tClass, String tableName, Columns columns, NeoMap searchMap, NeoPage page) {
        return NeoMap.asArray(page(tableName, columns, searchMap, page), tClass);
    }

    /**
     * 查询分页数据
     *
     * @param tClass        分页数据对应的类型
     * @param tableName     表名
     * @param columns       多个列名
     * @param searchQuery 复杂搜索条件
     * @param page          分页信息
     * @param <T>           分页数据对应类型的泛型
     * @return 分页数据
     */
    @Deprecated
    default <T> List<T> page(Class<T> tClass, String tableName, Columns columns, SearchQuery searchQuery, NeoPage page) {
        return NeoMap.asArray(page(tableName, columns, searchQuery, page), tClass);
    }

    /**
     * 查询分页数据
     *
     * @param tClass        分页数据对应的类型
     * @param tableName     表名
     * @param searchQuery 复杂搜索条件
     * @param page          分页信息
     * @param <T>           分页数据对应的类型的泛型
     * @return 分页数据
     */
    @Deprecated
    default <T> List<T> page(Class<T> tClass, String tableName, SearchQuery searchQuery, NeoPage page) {
        return NeoMap.asArray(page(tableName, searchQuery, page), tClass);
    }

    /**
     * 查询分页数据
     *
     * @param tClass    分页数据对应的类型
     * @param tableName 表名
     * @param searchMap 搜索条件
     * @param page      分页信息
     * @param <T>       分页数据对应的类型的泛型
     * @return 分页数据
     */
    @Deprecated
    default <T> List<T> page(Class<T> tClass, String tableName, NeoMap searchMap, NeoPage page) {
        return NeoMap.asArray(page(tableName, searchMap, page), tClass);
    }

    /**
     * 查询分页数据
     *
     * @param tClass    分页数据对应的类型
     * @param tableName 表名
     * @param columns   多个列名
     * @param page      分页信息
     * @param <T>       分页数据对应的类型的泛型
     * @return 分页数据
     */
    @Deprecated
    default <T> List<T> page(Class<T> tClass, String tableName, Columns columns, NeoPage page) {
        return NeoMap.asArray(page(tableName, columns, page), tClass);
    }

    /**
     * 查询分页数据
     *
     * @param tClass    分页数据对应的类型
     * @param tableName 表名
     * @param page      分页信息
     * @param <T>       分页数据对应类型的泛型
     * @return 分页数据
     */
    @Deprecated
    default <T> List<T> page(Class<T> tClass, String tableName, NeoPage page) {
        return NeoMap.asArray(page(tableName, page), tClass);
    }

    /**
     * 查询分页数据
     *
     * @param tableName 表名
     * @param columns   多个列名
     * @param searchMap 搜索条件
     * @param page      分页信息
     * @return 分页数据
     */
    @Deprecated
    default PageRsp<NeoMap> getPage(String tableName, Columns columns, NeoMap searchMap, NeoPage page) {
        return new PageRsp<>(page(tableName, columns, searchMap, page), count(tableName, searchMap));
    }

    /**
     * 查询分页数据
     *
     * @param tableName     表名
     * @param columns       多个列名
     * @param searchQuery 复杂搜索条件
     * @param page          分页信息
     * @return 分页数据
     */
    @Deprecated
    default PageRsp<NeoMap> getPage(String tableName, Columns columns, SearchQuery searchQuery, NeoPage page) {
        return new PageRsp<>(page(tableName, columns, searchQuery, page), count(tableName, searchQuery));
    }

    /**
     * 查询分页数据
     *
     * @param tableName 表名
     * @param columns   多个列名
     * @param entity    实体搜索条件
     * @param page      分页地信息
     * @param <T>       实体对应的类型的泛型
     * @return 分页数据盘
     */
    @Deprecated
    default <T> PageRsp<T> getPage(String tableName, Columns columns, T entity, NeoPage page) {
        return new PageRsp<>(page(tableName, columns, entity, page), count(tableName, entity));
    }

    /**
     * 查询分页数据
     *
     * @param tableName 表名
     * @param searchMap 搜索条件
     * @param page      分页信息
     * @return 分页数据
     */
    @Deprecated
    default PageRsp<NeoMap> getPage(String tableName, NeoMap searchMap, NeoPage page) {
        return new PageRsp<>(page(tableName, searchMap, page), count(tableName, searchMap));
    }

    /**
     * 查询分页数据
     *
     * @param tableName     表名
     * @param searchQuery 复杂搜索条件
     * @param page          分页信息
     * @return 分页数据
     */
    @Deprecated
    default PageRsp<NeoMap> getPage(String tableName, SearchQuery searchQuery, NeoPage page) {
        return new PageRsp<>(page(tableName, searchQuery, page), count(tableName, searchQuery));
    }

    /**
     * 查询分页数据
     *
     * @param tableName 表名
     * @param entity    实体搜索条件
     * @param page      分页信息
     * @param <T>       分页数据对应类型的泛型
     * @return 分页数据
     */
    @Deprecated
    default <T> PageRsp<T> getPage(String tableName, T entity, NeoPage page) {
        return new PageRsp<>(page(tableName, entity, page), count(tableName, entity));
    }

    /**
     * 查询分页数据
     *
     * @param tableName 表名
     * @param columns   多个列名
     * @param page      分页信息
     * @return 分页数据
     */
    @Deprecated
    default PageRsp<NeoMap> getPage(String tableName, Columns columns, NeoPage page) {
        return new PageRsp<>(page(tableName, columns, page), count(tableName, columns));
    }

    /**
     * 查询分页数据
     *
     * @param tableName 表名
     * @param page      分页信息
     * @return 分页数据
     */
    @Deprecated
    default PageRsp<NeoMap> getPage(String tableName, NeoPage page) {
        return new PageRsp<>(page(tableName, page), count(tableName));
    }

    /**
     * 查询分页数据
     *
     * @param tClass    分页数据对应的类型
     * @param tableName 表名
     * @param columns   多个列名
     * @param searchMap 搜索条件
     * @param page      分页信息
     * @param <T>       分页数据对应类型的泛型
     * @return 分页数据
     */
    @Deprecated
    default <T> PageRsp<T> getPage(Class<T> tClass, String tableName, Columns columns, NeoMap searchMap, NeoPage page) {
        return new PageRsp<>(page(tClass, tableName, columns, searchMap, page), count(tableName, searchMap));
    }

    /**
     * 查询分页数据
     *
     * @param tClass        分页数据对应的类型
     * @param tableName     表名
     * @param columns       列名
     * @param searchQuery 复杂搜索条件
     * @param page          分页信息
     * @param <T>           分页数据对应类型的泛型
     * @return 分页数据
     */
    @Deprecated
    default <T> PageRsp<T> getPage(Class<T> tClass, String tableName, Columns columns, SearchQuery searchQuery, NeoPage page) {
        return new PageRsp<>(page(tClass, tableName, columns, searchQuery, page), count(tableName, searchQuery));
    }


    /**
     * 查询分页数据
     *
     * @param tClass    分页数据对应的类型
     * @param tableName 表名
     * @param searchMap 搜索条件
     * @param page      分页信息
     * @param <T>       分页数据对应类型的泛型
     * @return 分页数据
     */
    @Deprecated
    default <T> PageRsp<T> getPage(Class<T> tClass, String tableName, NeoMap searchMap, NeoPage page) {
        return new PageRsp<>(page(tClass, tableName, searchMap, page), count(tableName, searchMap));
    }

    /**
     * 查询分页数据
     *
     * @param tClass        分页数据对应的类型
     * @param tableName     表名
     * @param searchQuery 复杂搜索条件
     * @param page          分页信息
     * @param <T>           分页数据对应类型的泛型
     * @return 分页数据
     */
    @Deprecated
    default <T> PageRsp<T> getPage(Class<T> tClass, String tableName, SearchQuery searchQuery, NeoPage page) {
        return new PageRsp<>(page(tClass, tableName, searchQuery, page), count(tableName, searchQuery));
    }

    /**
     * 查询分页数据
     *
     * @param tClass    分页数据对应的类型
     * @param tableName 表名
     * @param columns   多个列名
     * @param page      分页信息
     * @param <T>       分页数据对应类型的泛型
     * @return 分页数据
     */
    @Deprecated
    default <T> PageRsp<T> getPage(Class<T> tClass, String tableName, Columns columns, NeoPage page) {
        return new PageRsp<>(page(tClass, tableName, columns, page), count(tableName));
    }

    /**
     * 查询分页数据
     *
     * @param tClass    分页数据对应的类型
     * @param tableName 表名
     * @param page      分页信息
     * @param <T>       分页数据对应类型的泛型
     * @return 分页数据
     */
    @Deprecated
    default <T> PageRsp<T> getPage(Class<T> tClass, String tableName, NeoPage page) {
        return new PageRsp<>(page(tClass, tableName, page), count(tableName));
    }


    /**
     * 查询分页数据
     *
     * @param tClass    分页数据对应的类型
     * @param tableName 表名
     * @param columns   多个列名
     * @param searchMap 搜索条件
     * @param pageReq   分页信息
     * @param <T>       分页数据对应类型的泛型
     * @return 分页数据
     */
    default <T> List<T> page(Class<T> tClass, String tableName, Columns columns, NeoMap searchMap, PageReq<?> pageReq) {
        return NeoMap.asArray(page(tableName, columns, searchMap, pageReq), tClass);
    }

    /**
     * 查询分页数据
     *
     * @param tClass        分页数据对应的类型
     * @param tableName     表名
     * @param columns       多个列名
     * @param searchQuery 复杂搜索条件
     * @param pageReq       分页信息
     * @param <T>           分页数据对应类型的泛型
     * @return 分页数据
     */
    default <T> List<T> page(Class<T> tClass, String tableName, Columns columns, SearchQuery searchQuery, PageReq<?> pageReq) {
        return NeoMap.asArray(page(tableName, columns, searchQuery, pageReq), tClass);
    }

    /**
     * 查询分页数据
     *
     * @param tClass        分页数据对应的类型
     * @param tableName     表名
     * @param searchQuery 复杂搜索条件
     * @param pageReq       分页信息
     * @param <T>           分页数据对应的类型的泛型
     * @return 分页数据
     */
    default <T> List<T> page(Class<T> tClass, String tableName, SearchQuery searchQuery, PageReq<?> pageReq) {
        return NeoMap.asArray(page(tableName, searchQuery, pageReq), tClass);
    }

    /**
     * 查询分页数据
     *
     * @param tClass    分页数据对应的类型
     * @param tableName 表名
     * @param searchMap 搜索条件
     * @param pageReq   分页信息
     * @param <T>       分页数据对应的类型的泛型
     * @return 分页数据
     */
    default <T> List<T> page(Class<T> tClass, String tableName, NeoMap searchMap, PageReq<?> pageReq) {
        return NeoMap.asArray(page(tableName, searchMap, pageReq), tClass);
    }

    /**
     * 查询分页数据
     *
     * @param tClass    分页数据对应的类型
     * @param tableName 表名
     * @param columns   多个列名
     * @param pageReq   分页信息
     * @param <T>       分页数据对应的类型的泛型
     * @return 分页数据
     */
    default <T> List<T> page(Class<T> tClass, String tableName, Columns columns, PageReq<?> pageReq) {
        return NeoMap.asArray(page(tableName, columns, pageReq), tClass);
    }

    /**
     * 查询分页数据
     *
     * @param tClass    分页数据对应的类型
     * @param tableName 表名
     * @param pageReq   分页信息
     * @param <T>       分页数据对应类型的泛型
     * @return 分页数据
     */
    default <T> List<T> page(Class<T> tClass, String tableName, PageReq<?> pageReq) {
        return NeoMap.asArray(page(tableName, pageReq), tClass);
    }

    /**
     * 查询分页数据
     *
     * @param tableName 表名
     * @param columns   多个列名
     * @param searchMap 搜索条件
     * @param pageReq   分页信息
     * @return 分页数据
     */
    default PageRsp<NeoMap> getPage(String tableName, Columns columns, NeoMap searchMap, PageReq<?> pageReq) {
        return new PageRsp<>(page(tableName, columns, searchMap, pageReq), count(tableName, searchMap));
    }

    /**
     * 查询分页数据
     *
     * @param tableName     表名
     * @param columns       多个列名
     * @param searchQuery 复杂搜索条件
     * @param pageReq       分页信息
     * @return 分页数据
     */
    default PageRsp<NeoMap> getPage(String tableName, Columns columns, SearchQuery searchQuery, PageReq<?> pageReq) {
        return new PageRsp<>(page(tableName, columns, searchQuery, pageReq), count(tableName, searchQuery));
    }

    /**
     * 查询分页数据
     *
     * @param tableName 表名
     * @param columns   多个列名
     * @param entity    实体搜索条件
     * @param pageReq   分页信息
     * @param <T>       实体对应的类型的泛型
     * @return 分页数据盘
     */
    default <T> PageRsp<T> getPage(String tableName, Columns columns, T entity, PageReq<?> pageReq) {
        return new PageRsp<>(page(tableName, columns, entity, pageReq), count(tableName, entity));
    }

    /**
     * 查询分页数据
     *
     * @param tableName 表名
     * @param searchMap 搜索条件
     * @param pageReq   分页信息
     * @return 分页数据
     */
    default PageRsp<NeoMap> getPage(String tableName, NeoMap searchMap, PageReq<?> pageReq) {
        return new PageRsp<>(page(tableName, searchMap, pageReq), count(tableName, searchMap));
    }

    /**
     * 查询分页数据
     *
     * @param tableName     表名
     * @param searchQuery 复杂搜索条件
     * @param pageReq       分页信息
     * @return 分页数据
     */
    default PageRsp<NeoMap> getPage(String tableName, SearchQuery searchQuery, PageReq<?> pageReq) {
        return new PageRsp<>(page(tableName, searchQuery, pageReq), count(tableName, searchQuery));
    }

    /**
     * 查询分页数据
     *
     * @param tableName 表名
     * @param entity    实体搜索条件
     * @param pageReq   分页信息
     * @param <T>       分页数据对应类型的泛型
     * @return 分页数据
     */
    default <T> PageRsp<T> getPage(String tableName, T entity, PageReq<?> pageReq) {
        return new PageRsp<>(page(tableName, entity, pageReq), count(tableName, entity));
    }

    /**
     * 查询分页数据
     *
     * @param tableName 表名
     * @param columns   多个列名
     * @param pageReq   分页信息
     * @return 分页数据
     */
    default PageRsp<NeoMap> getPage(String tableName, Columns columns, PageReq<?> pageReq) {
        return new PageRsp<>(page(tableName, columns, pageReq), count(tableName, columns));
    }

    /**
     * 查询分页数据
     *
     * @param tableName 表名
     * @param pageReq   分页信息
     * @return 分页数据
     */
    default PageRsp<NeoMap> getPage(String tableName, PageReq<?> pageReq) {
        return new PageRsp<>(page(tableName, pageReq), count(tableName));
    }

    /**
     * 查询分页数据
     *
     * @param tClass    分页数据对应的类型
     * @param tableName 表名
     * @param columns   多个列名
     * @param searchMap 搜索条件
     * @param pageReq   分页信息
     * @param <T>       分页数据对应类型的泛型
     * @return 分页数据
     */
    default <T> PageRsp<T> getPage(Class<T> tClass, String tableName, Columns columns, NeoMap searchMap, PageReq<?> pageReq) {
        return new PageRsp<>(page(tClass, tableName, columns, searchMap, pageReq), count(tableName, searchMap));
    }

    /**
     * 查询分页数据
     *
     * @param tClass        分页数据对应的类型
     * @param tableName     表名
     * @param columns       列名
     * @param searchQuery 复杂搜索条件
     * @param pageReq       分页信息
     * @param <T>           分页数据对应类型的泛型
     * @return 分页数据
     */
    default <T> PageRsp<T> getPage(Class<T> tClass, String tableName, Columns columns, SearchQuery searchQuery, PageReq<?> pageReq) {
        return new PageRsp<>(page(tClass, tableName, columns, searchQuery, pageReq), count(tableName, searchQuery));
    }


    /**
     * 查询分页数据
     *
     * @param tClass    分页数据对应的类型
     * @param tableName 表名
     * @param searchMap 搜索条件
     * @param pageReq   分页信息
     * @param <T>       分页数据对应类型的泛型
     * @return 分页数据
     */
    default <T> PageRsp<T> getPage(Class<T> tClass, String tableName, NeoMap searchMap, PageReq<?> pageReq) {
        return new PageRsp<>(page(tClass, tableName, searchMap, pageReq), count(tableName, searchMap));
    }

    /**
     * 查询分页数据
     *
     * @param tClass        分页数据对应的类型
     * @param tableName     表名
     * @param searchQuery 复杂搜索条件
     * @param pageReq       分页信息
     * @param <T>           分页数据对应类型的泛型
     * @return 分页数据
     */
    default <T> PageRsp<T> getPage(Class<T> tClass, String tableName, SearchQuery searchQuery, PageReq<?> pageReq) {
        return new PageRsp<>(page(tClass, tableName, searchQuery, pageReq), count(tableName, searchQuery));
    }

    /**
     * 查询分页数据
     *
     * @param tClass    分页数据对应的类型
     * @param tableName 表名
     * @param columns   多个列名
     * @param pageReq   分页信息
     * @param <T>       分页数据对应类型的泛型
     * @return 分页数据
     */
    default <T> PageRsp<T> getPage(Class<T> tClass, String tableName, Columns columns, PageReq<?> pageReq) {
        return new PageRsp<>(page(tClass, tableName, columns, pageReq), count(tableName));
    }

    /**
     * 查询分页数据
     *
     * @param tClass    分页数据对应的类型
     * @param tableName 表名
     * @param pageReq   分页信息
     * @param <T>       分页数据对应类型的泛型
     * @return 分页数据
     */
    default <T> PageRsp<T> getPage(Class<T> tClass, String tableName, PageReq<?> pageReq) {
        return new PageRsp<>(page(tClass, tableName, pageReq), count(tableName));
    }


    /**
     * 查询个数
     *
     * @param tableName 表名
     * @param searchMap 搜索条件
     * @return 个数
     */
    Integer count(String tableName, NeoMap searchMap);

    /**
     * 查询个数
     *
     * @param tableName     表名
     * @param searchQuery 复杂搜索条件
     * @return 个数
     */
    Integer count(String tableName, SearchQuery searchQuery);

    /**
     * 查询个数
     *
     * @param tableName 表名
     * @param entity    搜索实体
     * @return 个数
     */
    Integer count(String tableName, Object entity);

    /**
     * 查询个数
     *
     * @param tableName 表名
     * @return 个数
     */
    Integer count(String tableName);

    /**
     * 查询是否存在
     *
     * @param tableName 表名
     * @param searchMap 搜索条件
     * @return 存在与否
     */
    default Boolean exist(String tableName, NeoMap searchMap) {
        return 0 != count(tableName, searchMap);
    }

    /**
     * 查询是否存在
     *
     * @param tableName     表名
     * @param searchQuery 复杂搜索条件
     * @return 存在与否
     */
    default Boolean exist(String tableName, SearchQuery searchQuery) {
        return 0 != count(tableName, searchQuery);
    }

    /**
     * 查询是否存在
     *
     * @param tableName 表名
     * @param entity    搜索实体
     * @return 存在与否
     */
    default Boolean exist(String tableName, Object entity) {
        return null != one(tableName, entity);
    }

    /**
     * 查询是否存在
     *
     * @param tableName 表名
     * @param id        主键
     * @return 存在与否
     */
    default Boolean exist(String tableName, Number id) {
        return null != one(tableName, id);
    }
}
