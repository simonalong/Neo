package com.simonalong.neo.core.join;

import com.simonalong.neo.Columns;
import com.simonalong.neo.TableMap;
import com.simonalong.neo.db.PageRsp;
import com.simonalong.neo.db.TableJoinOn;
import com.simonalong.neo.db.NeoPage;
import com.simonalong.neo.express.Express;

import java.util.List;

/**
 * @author shizi
 * @since 2020/5/23 6:19 PM
 */
public interface JoinnerSync {

    /**
     * 查询一行数据对象
     *
     * @param columns 多表查询的数据
     * @param tableJoinOn 多表的关联关系
     * @param searchMap 多表的搜索条件
     * @return 数据对象
     */
    TableMap one(Columns columns, TableJoinOn tableJoinOn, TableMap searchMap);

    TableMap one(Columns columns, TableJoinOn tableJoinOn, Express searchExpress);

    <T> T one(Class<T> tClass, Columns columns, TableJoinOn tableJoinOn, TableMap searchMap);

    <T> T one(Class<T> tClass, Columns columns, TableJoinOn tableJoinOn, Express searchExpress);

    /**
     * 查询多行数据对象
     *
     * @param columns 多表查询的数据
     * @param tableJoinOn 多表的关联关系
     * @param searchMap 多表的搜索条件
     * @return 数据对象
     */
    List<TableMap> list(Columns columns, TableJoinOn tableJoinOn, TableMap searchMap);

    List<TableMap> list(Columns columns, TableJoinOn tableJoinOn, Express searchExpress);

    <T> List<T> list(Class<T> tClass, Columns columns, TableJoinOn tableJoinOn, TableMap searchMap);

    <T> List<T> list(Class<T> tClass, Columns columns, TableJoinOn tableJoinOn, Express searchExpress);

    /**
     * 查询某个列的一个值
     *
     * @param columns 多表中的某个表的某个列
     * @param tableJoinOn 多表的关联关系
     * @param searchMap 多表的搜索条件
     * @return 数据对象
     */
    String value(Columns columns, TableJoinOn tableJoinOn, TableMap searchMap);

    String value(Columns columns, TableJoinOn tableJoinOn, Express searchExpress);

    <T> T value(Class<T> tClass, Columns columns, TableJoinOn tableJoinOn, TableMap searchMap);

    <T> T value(Class<T> tClass, Columns columns, TableJoinOn tableJoinOn, Express searchExpress);

    /**
     * 查询某个列的多个值
     *
     * @param columns 多表中的某个表的某个列
     * @param tableJoinOn 多表的关联关系
     * @param searchMap 多表的搜索条件
     * @return 数据对象
     */
    List<String> values(Columns columns, TableJoinOn tableJoinOn, TableMap searchMap);

    List<String> values(Columns columns, TableJoinOn tableJoinOn, Express searchExpress);

    <T> List<T> values(Class<T> tClass, Columns columns, TableJoinOn tableJoinOn, TableMap searchMap);

    <T> List<T> values(Class<T> tClass, Columns columns, TableJoinOn tableJoinOn, Express searchExpress);

    /**
     * 筛选唯一数值
     *
     * @param tClass 类型
     * @param columns 多表中的某个表的某个列
     * @param tableJoinOn 多表的关联关系
     * @param searchMap 多表的搜索条件
     * @param <T> 类型
     * @return 返回集合
     */
    <T> List<T> valuesOfDistinct(Class<T> tClass, Columns columns, TableJoinOn tableJoinOn, TableMap searchMap);

    <T> List<T> valuesOfDistinct(Class<T> tClass, Columns columns, TableJoinOn tableJoinOn, Express searchExpress);

    List<String> valuesOfDistinct(Columns columns, TableJoinOn tableJoinOn, TableMap searchMap);

    List<String> valuesOfDistinct(Columns columns, TableJoinOn tableJoinOn, Express searchExpress);

    /**
     * 根据分页，查询多行数据对象
     *
     * @param columns 多表查询的数据
     * @param tableJoinOn 多表的关联关系
     * @param searchMap 多表的搜索条件
     * @param neoPage 分页数据
     * @return 数据对象
     */
    List<TableMap> page(Columns columns, TableJoinOn tableJoinOn, TableMap searchMap, NeoPage neoPage);

    List<TableMap> page(Columns columns, TableJoinOn tableJoinOn, Express searchExpress, NeoPage neoPage);

    <T> List<T> page(Class<T> tClass, Columns columns, TableJoinOn tableJoinOn, TableMap searchMap, NeoPage neoPage);

    <T> List<T> page(Class<T> tClass, Columns columns, TableJoinOn tableJoinOn, Express searchExpress, NeoPage neoPage);

    default PageRsp<TableMap> getPage(Columns columns, TableJoinOn tableJoinOn, TableMap searchMap, NeoPage neoPage) {
        return new PageRsp<>(page(columns, tableJoinOn, searchMap, neoPage), count(tableJoinOn, searchMap));
    }

    default PageRsp<TableMap> getPage(Columns columns, TableJoinOn tableJoinOn, Express searchExpress, NeoPage neoPage) {
        return new PageRsp<>(page(columns, tableJoinOn, searchExpress, neoPage), count(tableJoinOn, searchExpress));
    }

    default <T> PageRsp<T>  getPage(Class<T> tClass, Columns columns, TableJoinOn tableJoinOn, TableMap searchMap, NeoPage neoPage) {
        return new PageRsp<>(page(tClass, columns, tableJoinOn, searchMap, neoPage), count(tableJoinOn, searchMap));
    }

    default <T> PageRsp<T> getPage(Class<T> tClass, Columns columns, TableJoinOn tableJoinOn, Express searchExpress, NeoPage neoPage) {
        return new PageRsp<>(page(tClass, columns, tableJoinOn, searchExpress, neoPage), count(tableJoinOn, searchExpress));
    }

    /**
     * 查询多表关联后的个数
     *
     * @param tableJoinOn 多表的关联关系
     * @param searchMap 多表的搜索条件
     * @return 数据对象
     */
    Integer count(TableJoinOn tableJoinOn, TableMap searchMap);

    Integer count(TableJoinOn tableJoinOn, Express searchExpress);
}
