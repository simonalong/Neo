package com.simonalong.neo.core.join;

import com.simonalong.neo.Columns;
import com.simonalong.neo.TableMap;
import com.simonalong.neo.db.PageReq;
import com.simonalong.neo.db.PageRsp;
import com.simonalong.neo.db.TableJoinOn;
import com.simonalong.neo.db.NeoPage;
import com.simonalong.neo.express.SearchQuery;

import java.util.List;

/**
 * @author shizi
 * @since 2020/5/23 6:19 PM
 */
public interface JoinnerSync {

    /**
     * 查询一行数据对象
     * <p>
     *     后续弃用，请使用{@link JoinnerSync#one(Columns, TableMap)}
     * </p>
     *
     * @param columns 多表查询的数据
     * @param tableJoinOn 多表的关联关系
     * @param searchMap 多表的搜索条件
     * @return 数据对象
     */
    @Deprecated
    TableMap one(Columns columns, TableJoinOn tableJoinOn, TableMap searchMap);

    @Deprecated
    TableMap one(Columns columns, TableJoinOn tableJoinOn, SearchQuery searchQuery);

    @Deprecated
    <T> T one(Class<T> tClass, Columns columns, TableJoinOn tableJoinOn, TableMap searchMap);

    @Deprecated
    <T> T one(Class<T> tClass, Columns columns, TableJoinOn tableJoinOn, SearchQuery searchQuery);

    /**
     * 查询多行数据对象
     *
     * <p>
     *     后续弃用，请使用{@link JoinnerSync#list(Columns, TableMap)}
     * </p>
     *
     * @param columns 多表查询的数据
     * @param tableJoinOn 多表的关联关系
     * @param searchMap 多表的搜索条件
     * @return 数据对象
     */
    @Deprecated
    List<TableMap> list(Columns columns, TableJoinOn tableJoinOn, TableMap searchMap);

    @Deprecated
    List<TableMap> list(Columns columns, TableJoinOn tableJoinOn, SearchQuery searchQuery);

    @Deprecated
    <T> List<T> list(Class<T> tClass, Columns columns, TableJoinOn tableJoinOn, TableMap searchMap);

    @Deprecated
    <T> List<T> list(Class<T> tClass, Columns columns, TableJoinOn tableJoinOn, SearchQuery searchQuery);

    /**
     * 查询某个列的一个值
     *
     * <p>
     *     后续弃用，请使用{@link JoinnerSync#value(Columns, TableMap)}
     * </p>
     *
     * @param columns 多表中的某个表的某个列
     * @param tableJoinOn 多表的关联关系
     * @param searchMap 多表的搜索条件
     * @return 数据对象
     */
    @Deprecated
    String value(Columns columns, TableJoinOn tableJoinOn, TableMap searchMap);

    @Deprecated
    String value(Columns columns, TableJoinOn tableJoinOn, SearchQuery searchQuery);

    @Deprecated
    <T> T value(Class<T> tClass, Columns columns, TableJoinOn tableJoinOn, TableMap searchMap);

    @Deprecated
    <T> T value(Class<T> tClass, Columns columns, TableJoinOn tableJoinOn, SearchQuery searchQuery);

    /**
     * 查询某个列的多个值
     *
     * <p>
     *     后续弃用，请使用{@link JoinnerSync#values(Columns, TableMap)}
     * </p>
     *
     * @param columns 多表中的某个表的某个列
     * @param tableJoinOn 多表的关联关系
     * @param searchMap 多表的搜索条件
     * @return 数据对象
     */
    @Deprecated
    List<String> values(Columns columns, TableJoinOn tableJoinOn, TableMap searchMap);

    @Deprecated
    List<String> values(Columns columns, TableJoinOn tableJoinOn, SearchQuery searchQuery);

    @Deprecated
    <T> List<T> values(Class<T> tClass, Columns columns, TableJoinOn tableJoinOn, TableMap searchMap);

    @Deprecated
    <T> List<T> values(Class<T> tClass, Columns columns, TableJoinOn tableJoinOn, SearchQuery searchQuery);

    /**
     * 筛选唯一数值
     *
     * <p>
     *     后续弃用，请使用{@link JoinnerSync#valuesOfDistinct(Class, Columns, TableMap)}
     * </p>
     *
     * @param tClass 类型
     * @param columns 多表中的某个表的某个列
     * @param tableJoinOn 多表的关联关系
     * @param searchMap 多表的搜索条件
     * @param <T> 类型
     * @return 返回集合
     */
    @Deprecated
    <T> List<T> valuesOfDistinct(Class<T> tClass, Columns columns, TableJoinOn tableJoinOn, TableMap searchMap);

    @Deprecated
    <T> List<T> valuesOfDistinct(Class<T> tClass, Columns columns, TableJoinOn tableJoinOn, SearchQuery searchQuery);

    @Deprecated
    List<String> valuesOfDistinct(Columns columns, TableJoinOn tableJoinOn, TableMap searchMap);

    @Deprecated
    List<String> valuesOfDistinct(Columns columns, TableJoinOn tableJoinOn, SearchQuery searchQuery);

    /**
     * 根据分页，查询多行数据对象
     *
     * <p>
     *     后续弃用，请使用{@link JoinnerSync#page(Class, Columns, TableMap, PageReq)}
     * </p>
     *
     * @param columns 多表查询的数据
     * @param tableJoinOn 多表的关联关系
     * @param searchMap 多表的搜索条件
     * @param neoPage 分页数据
     * @return 数据对象
     */
    @Deprecated
    List<TableMap> page(Columns columns, TableJoinOn tableJoinOn, TableMap searchMap, NeoPage neoPage);

    @Deprecated
    List<TableMap> page(Columns columns, TableJoinOn tableJoinOn, SearchQuery searchQuery, NeoPage neoPage);

    @Deprecated
    <T> List<T> page(Class<T> tClass, Columns columns, TableJoinOn tableJoinOn, TableMap searchMap, NeoPage neoPage);

    @Deprecated
    <T> List<T> page(Class<T> tClass, Columns columns, TableJoinOn tableJoinOn, SearchQuery searchQuery, NeoPage neoPage);

    @Deprecated
    List<TableMap> page(Columns columns, TableJoinOn tableJoinOn, TableMap searchMap, PageReq<?> pageReq);

    @Deprecated
    List<TableMap> page(Columns columns, TableJoinOn tableJoinOn, SearchQuery searchQuery, PageReq<?> pageReq);

    @Deprecated
    <T> List<T> page(Class<T> tClass, Columns columns, TableJoinOn tableJoinOn, TableMap searchMap, PageReq<?> pageReq);

    @Deprecated
    <T> List<T> page(Class<T> tClass, Columns columns, TableJoinOn tableJoinOn, SearchQuery searchQuery, PageReq<?> pageReq);

    @Deprecated
    default PageRsp<TableMap> getPage(Columns columns, TableJoinOn tableJoinOn, TableMap searchMap, NeoPage neoPage) {
        return new PageRsp<>(page(columns, tableJoinOn, searchMap, neoPage), count(tableJoinOn, searchMap));
    }

    @Deprecated
    default PageRsp<TableMap> getPage(Columns columns, TableJoinOn tableJoinOn, SearchQuery searchQuery, NeoPage neoPage) {
        return new PageRsp<>(page(columns, tableJoinOn, searchQuery, neoPage), count(tableJoinOn, searchQuery));
    }

    @Deprecated
    default <T> PageRsp<T>  getPage(Class<T> tClass, Columns columns, TableJoinOn tableJoinOn, TableMap searchMap, NeoPage neoPage) {
        return new PageRsp<>(page(tClass, columns, tableJoinOn, searchMap, neoPage), count(tableJoinOn, searchMap));
    }

    @Deprecated
    default <T> PageRsp<T>  getPage(Class<T> tClass, Columns columns, TableJoinOn tableJoinOn, SearchQuery searchQuery, NeoPage neoPage) {
        return new PageRsp<>(page(tClass, columns, tableJoinOn, searchQuery, neoPage), count(tableJoinOn, searchQuery));
    }


    @Deprecated
    default PageRsp<TableMap> getPage(Columns columns, TableJoinOn tableJoinOn, TableMap searchMap, PageReq<?> pageReq) {
        return new PageRsp<>(page(columns, tableJoinOn, searchMap, pageReq), count(tableJoinOn, searchMap));
    }

    @Deprecated
    default PageRsp<TableMap> getPage(Columns columns, TableJoinOn tableJoinOn, SearchQuery searchQuery, PageReq<?> pageReq) {
        return new PageRsp<>(page(columns, tableJoinOn, searchQuery, pageReq), count(tableJoinOn, searchQuery));
    }

    @Deprecated
    default <T> PageRsp<T>  getPage(Class<T> tClass, Columns columns, TableJoinOn tableJoinOn, TableMap searchMap, PageReq<?> pageReq) {
        return new PageRsp<>(page(tClass, columns, tableJoinOn, searchMap, pageReq), count(tableJoinOn, searchMap));
    }

    @Deprecated
    default <T> PageRsp<T>  getPage(Class<T> tClass, Columns columns, TableJoinOn tableJoinOn, SearchQuery searchQuery, PageReq<?> pageReq) {
        return new PageRsp<>(page(tClass, columns, tableJoinOn, searchQuery, pageReq), count(tableJoinOn, searchQuery));
    }

    /**
     * 查询多表关联后的个数
     *
     * <p>
     *     后续弃用，请使用{@link JoinnerSync#count(TableMap)}
     * </p>
     *
     * @param tableJoinOn 多表的关联关系
     * @param searchMap 多表的搜索条件
     * @return 数据对象
     */
    @Deprecated
    Integer count(TableJoinOn tableJoinOn, TableMap searchMap);

    @Deprecated
    Integer count(TableJoinOn tableJoinOn, SearchQuery searchQuery);


    /**
     * 查询一行数据对象
     *
     * @param columns 多表查询的数据
     * @param searchMap 多表的搜索条件
     * @return 数据对象
     */
    TableMap one(Columns columns, TableMap searchMap);

    TableMap one(Columns columns, SearchQuery searchQuery);

    <T> T one(Class<T> tClass, Columns columns, TableMap searchMap);

    <T> T one(Class<T> tClass, Columns columns, SearchQuery searchQuery);

    /**
     * 查询多行数据对象
     *
     * @param columns 多表查询的数据
     * @param searchMap 多表的搜索条件
     * @return 数据对象
     */
    List<TableMap> list(Columns columns, TableMap searchMap);

    List<TableMap> list(Columns columns, SearchQuery searchQuery);

    <T> List<T> list(Class<T> tClass, Columns columns, TableMap searchMap);

    <T> List<T> list(Class<T> tClass, Columns columns, SearchQuery searchQuery);

    /**
     * 查询某个列的一个值
     *
     * @param columns 多表中的某个表的某个列
     * @param searchMap 多表的搜索条件
     * @return 数据对象
     */
    String value(Columns columns, TableMap searchMap);

    String value(Columns columns, SearchQuery searchQuery);

    <T> T value(Class<T> tClass, Columns columns, TableMap searchMap);

    <T> T value(Class<T> tClass, Columns columns, SearchQuery searchQuery);

    /**
     * 查询某个列的多个值
     *
     * @param columns 多表中的某个表的某个列
     * @param searchMap 多表的搜索条件
     * @return 数据对象
     */
    List<String> values(Columns columns, TableMap searchMap);

    List<String> values(Columns columns, SearchQuery searchQuery);

    <T> List<T> values(Class<T> tClass, Columns columns, TableMap searchMap);

    <T> List<T> values(Class<T> tClass, Columns columns, SearchQuery searchQuery);

    /**
     * 筛选唯一数值
     *
     * @param tClass 类型
     * @param columns 多表中的某个表的某个列
     * @param searchMap 多表的搜索条件
     * @param <T> 类型
     * @return 返回集合
     */
    <T> List<T> valuesOfDistinct(Class<T> tClass, Columns columns, TableMap searchMap);

    <T> List<T> valuesOfDistinct(Class<T> tClass, Columns columns, SearchQuery searchQuery);

    List<String> valuesOfDistinct(Columns columns, TableMap searchMap);

    List<String> valuesOfDistinct(Columns columns, SearchQuery searchQuery);


    List<TableMap> page(Columns columns, TableMap searchMap, PageReq<?> pageReq);

    List<TableMap> page(Columns columns, SearchQuery searchQuery, PageReq<?> pageReq);

    <T> List<T> page(Class<T> tClass, Columns columns, TableMap searchMap, PageReq<?> pageReq);

    <T> List<T> page(Class<T> tClass, Columns columns, SearchQuery searchQuery, PageReq<?> pageReq);

    default PageRsp<TableMap> getPage(Columns columns, TableMap searchMap, PageReq<?> pageReq) {
        return new PageRsp<>(page(columns, searchMap, pageReq), count(searchMap));
    }

    default PageRsp<TableMap> getPage(Columns columns, SearchQuery searchQuery, PageReq<?> pageReq) {
        return new PageRsp<>(page(columns, searchQuery, pageReq), count(searchQuery));
    }

    default <T> PageRsp<T>  getPage(Class<T> tClass, Columns columns, TableMap searchMap, PageReq<?> pageReq) {
        return new PageRsp<>(page(tClass, columns, searchMap, pageReq), count(searchMap));
    }

    default <T> PageRsp<T>  getPage(Class<T> tClass, Columns columns, SearchQuery searchQuery, PageReq<?> pageReq) {
        return new PageRsp<>(page(tClass, columns, searchQuery, pageReq), count(searchQuery));
    }

    /**
     * 查询多表关联后的个数
     *
     * @param searchMap 多表的搜索条件
     * @return 数据对象
     */
    Integer count(TableMap searchMap);

    Integer count(SearchQuery searchQuery);
}
