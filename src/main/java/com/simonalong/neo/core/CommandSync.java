package com.simonalong.neo.core;

import com.simonalong.neo.Columns;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.express.SearchExpress;

import java.util.List;

/**
 * @author shizi
 * @since 2020/6/11 2:58 PM
 */
public interface CommandSync extends Sync {

    /**
     * 数据插入
     *
     * @param tableName 表名
     * @param dataMap   待插入的数据
     * @return 插入后的数据
     */
    NeoMap insert(String tableName, NeoMap dataMap);

    /**
     * 数据插入
     *
     * @param tableName 表名
     * @param object    待插入的数据
     * @param <T>       数据的类型
     * @return 插入后的数据
     */
    <T> T insert(String tableName, T object);


    /**
     * 不存在则新增，存在则忽略
     *
     * @param tableName       表名
     * @param dataMap         新增的实体
     * @param searchColumnKey 作为搜索条件的搜索的key
     * @return 插入后的数据
     */
    NeoMap insertOfUnExist(String tableName, NeoMap dataMap, String... searchColumnKey);

    /**
     * 不存在则新增，存在则忽略
     *
     * @param tableName       表名
     * @param object          新增的实体
     * @param searchColumnKey 作为搜索条件的数据的key，这个key是属性名转换为db字段的名字
     * @return 插入后的数据
     */
    <T> T insertOfUnExist(String tableName, T object, String... searchColumnKey);


    /**
     * save功能
     * <p>
     * 数据如果存在，则更新，否则插入
     *
     * @param tableName       表名
     * @param dataMap         待更新或者插入的数据
     * @param searchColumnKey 作为搜索条件的搜索的key
     * @return 更新或者插入后的数据
     */
    NeoMap save(String tableName, NeoMap dataMap, String... searchColumnKey);

    /**
     * save功能
     * <p>
     * 数据如果存在，则更新，否则插入
     *
     * @param tableName       表名
     * @param object          待更新或者插入的数据
     * @param searchColumnKey 作为搜索条件的数据的key，这个key是属性名转换为db字段的名字
     * @return 更新或者插入后的数据
     */
    <T> T save(String tableName, T object, String... searchColumnKey);

    /**
     * 数据删除
     *
     * @param tableName 表名
     * @param searchMap 搜索条件
     * @return 影响的条数
     */
    Integer delete(String tableName, NeoMap searchMap);

    /**
     * 数据删除
     *
     * @param tableName     表名
     * @param searchExpress 复杂搜索条件
     * @return 影响的条数
     */
    Integer delete(String tableName, SearchExpress searchExpress);

    /**
     * 数据删除
     *
     * @param tableName 表名
     * @param object    实体搜索条件
     * @param <T>       实体对应的泛型
     * @return 影响的条数
     */
    <T> Integer delete(String tableName, T object);

    /**
     * 数据删除
     *
     * @param tableName 表名
     * @param id        主键
     * @return 影响的条数
     */
    Integer delete(String tableName, Number id);


    /**
     * 数据更新
     *
     * @param tableName 表名
     * @param dataMap   待更新的数据
     * @param searchMap 搜索条件
     * @return 更新后的数据
     */
    NeoMap update(String tableName, NeoMap dataMap, NeoMap searchMap);

    /**
     * 数据更新
     *
     * @param tableName 表名
     * @param setEntity 待更新的数据
     * @param searchMap 搜索条件
     * @param <T>       待更新数据的泛型
     * @return 更新后的数据
     */
    <T> T update(String tableName, T setEntity, NeoMap searchMap);

    /**
     * 数据更新
     *
     * @param tableName    表名
     * @param setEntity    待更新的数据
     * @param searchEntity 实体搜索条件
     * @param <T>          实体类型对应的泛型
     * @return 更新后的数据
     */
    <T> T update(String tableName, T setEntity, T searchEntity);

    /**
     * 数据更新
     *
     * @param tableName    表名
     * @param setMap       待更新的数据
     * @param searchEntity 搜索条件
     * @param <T>          搜索条件类型对应的泛型
     * @return 更新后的数据
     */
    <T> NeoMap update(String tableName, NeoMap setMap, T searchEntity);

    /**
     * 数据更新
     *
     * @param tableName 表名
     * @param setEntity 待更新的数据
     * @param id        主键
     * @param <T>       更新数据对应的泛型
     * @return 更新后的数据
     */
    <T> T update(String tableName, T setEntity, Number id);

    /**
     * 数据更新
     *
     * @param tableName 表名
     * @param setMap    待设置的数据
     * @param id        主键
     * @return 更新后的数据
     */
    NeoMap update(String tableName, NeoMap setMap, Number id);

    /**
     * 数据更新
     *
     * @param tableName 表名
     * @param dataMap   待更新的数据
     * @param columns   该列是待更新数据中的列名，这样指定的列名对应的value会作为搜索条件
     * @return 更新后的数据
     */
    NeoMap update(String tableName, NeoMap dataMap, Columns columns);

    /**
     * 数据更新
     *
     * @param tableName 表名
     * @param entity    待更新的数据
     * @param columns   该列是待更新数据中的属性名，这样指定的列名对应的value会作为搜索条件，其中key为
     * @param <T>       待更新的数据的泛型类型
     * @return 更新后的数据
     */
    <T> T update(String tableName, T entity, Columns columns);

    /**
     * 数据更新
     *
     * @param tableName 表名
     * @param dataMap   待更新的数据
     * @return 更新后的数据
     */
    NeoMap update(String tableName, NeoMap dataMap);

    /**
     * 数据更新
     *
     * @param tableName 表名
     * @param entity    待更新的数据，默认情况下会使用其中的主键，如果没有主键，则会用所有的列作为搜索条件
     * @param <T>       待更新数据的类型的泛型
     * @return 更新后的数据
     */
    <T> T update(String tableName, T entity);

    /**
     * 数据更新
     *
     * @param tableName     表名
     * @param dataMap       待更新的数据
     * @param searchExpress 复杂搜索条件
     * @return 更新后的数据
     */
    NeoMap update(String tableName, NeoMap dataMap, SearchExpress searchExpress);

    /**
     * 数据更新
     *
     * @param tableName     表名
     * @param setEntity     待更新的数据
     * @param searchExpress 复杂搜索条件
     * @param <T>           待更新数据类型的泛型
     * @return 更新后的数据
     */
    <T> T update(String tableName, T setEntity, SearchExpress searchExpress);


    /**
     * 批量插入
     *
     * @param tableName   表名
     * @param dataMapList 待插入的数据
     * @return 插入成功的条数
     */
    Integer batchInsert(String tableName, List<NeoMap> dataMapList);

    /**
     * 批量插入
     *
     * @param tableName 表名
     * @param dataList  待插入的数据
     * @param <T>       待插入数据类型的泛型
     * @return 插入成功的条数
     */
    <T> Integer batchInsertEntity(String tableName, List<T> dataList);

    /**
     * 批量更新
     *
     * @param tableName 表名
     * @param dataList  待更新的批量数据
     * @return 更新成功的条数
     */
    Integer batchUpdate(String tableName, List<NeoMap> dataList);

    /**
     * 批量更新
     *
     * @param tableName 表名
     * @param dataList  待更新的批量数据
     * @param columns   将待更新的批量数据中的Key对应的value作为搜索条件的列
     * @return 更新成功的条数
     */
    Integer batchUpdate(String tableName, List<NeoMap> dataList, Columns columns);

    /**
     * 批量更新
     *
     * @param tableName 表名
     * @param dataList  待更新的数据集合
     * @param <T>       待更新的数据类型对应的泛型
     * @return 更新成功的条数
     */
    <T> Integer batchUpdateEntity(String tableName, List<T> dataList);

    /**
     * 批量跟新
     *
     * @param tableName 表名
     * @param dataList  待更新的数据
     * @param columns   将待更新的批量数据中的key对应的value作为搜索条件的列
     * @param <T>       待更新的批量数据类型中的泛型
     * @return 更新成功的条数
     */
    <T> Integer batchUpdateEntity(String tableName, List<T> dataList, Columns columns);
}
