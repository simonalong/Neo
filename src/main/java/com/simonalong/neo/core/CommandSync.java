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

    NeoMap insert(String tableName, NeoMap dataMap);

    <T> T insert(String tableName, T object);


    /**
     * 不存在则新增，存在则忽略
     *
     * @param tableName 表名
     * @param dataMap 新增的实体
     * @param searchColumnKey 作为搜索条件的搜索的key
     * @return 插入后的数据
     */
    NeoMap insertOfUnExist(String tableName, NeoMap dataMap, String... searchColumnKey);

    /**
     * 不存在则新增，存在则忽略
     *
     * @param tableName 表名
     * @param object 新增的实体
     * @param searchColumnKey 作为搜索条件的数据的key，这个key是属性名转换为db字段的名字
     * @param <T> 对象类型
     * @return 插入后的数据
     */
    <T> T insertOfUnExist(String tableName, T object, String... searchColumnKey);


    /**
     * save功能
     * <p>
     *     数据如果存在，则更新，否则插入
     * @param tableName 表名
     * @param dataMap 待更新或者插入的数据
     * @param searchColumnKey 作为搜索条件的搜索的key
     * @return 更新或者插入后的数据
     */
    NeoMap save(String tableName, NeoMap dataMap, String... searchColumnKey);

    /**
     * save功能
     * <p>
     *     数据如果存在，则更新，否则插入
     * @param tableName 表名
     * @param object 待更新或者插入的数据
     * @param searchColumnKey 作为搜索条件的数据的key，这个key是属性名转换为db字段的名字
     * @param <T> 对象类型
     * @return 更新或者插入后的数据
     */
    <T> T save(String tableName, T object, String... searchColumnKey);


    Integer delete(String tableName, NeoMap searchMap);

    Integer delete(String tableName, SearchExpress searchExpress);

    <T> Integer delete(String tableName, T object);

    Integer delete(String tableName, Number id);


    NeoMap update(String tableName, NeoMap dataMap, NeoMap searchMap);

    <T> T update(String tableName, T setEntity, NeoMap searchMap);

    <T> T update(String tableName, T setEntity, T searchEntity);

    <T> NeoMap update(String tableName, NeoMap setMap, T searchEntity);

    NeoMap update(String tableName, NeoMap dataMap, Columns columns);

    <T> T update(String tableName, T setEntity, Number id);

    NeoMap update(String tableName, NeoMap setMap, Number id);

    <T> T update(String tableName, T entity, Columns columns);

    NeoMap update(String tableName, NeoMap dataMap);

    <T> T update(String tableName, T entity);


    NeoMap update(String tableName, NeoMap dataMap, SearchExpress searchExpress);

    <T> T update(String tableName, T setEntity, SearchExpress searchExpress);


    Integer batchInsert(String tableName, List<NeoMap> dataMapList);

    <T> Integer batchInsertEntity(String tableName, List<T> dataList);


    Integer batchUpdate(String tableName, List<NeoMap> dataList);

    Integer batchUpdate(String tableName, List<NeoMap> dataList, Columns columns);

    <T> Integer batchUpdateEntity(String tableName, List<T> dataList);

    <T> Integer batchUpdateEntity(String tableName, List<T> dataList, Columns columns);
}
