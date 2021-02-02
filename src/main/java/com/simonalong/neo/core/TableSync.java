package com.simonalong.neo.core;

import com.simonalong.neo.Columns;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.db.NeoPage;
import com.simonalong.neo.db.PageRsp;
import com.simonalong.neo.express.SearchExpress;

import java.util.List;

/**
 * @author zhouzhenyong
 * @since 2019-08-17 17:16
 */
public interface TableSync extends Sync {

    NeoMap insert(NeoMap dataMap);

    <T> T insert(T object);


    /**
     * 不存在则新增，存在则忽略
     *
     * @param dataMap 新增的实体
     * @param searchColumnKey 作为搜索条件的搜索的key
     * @return 插入后的数据
     */
    NeoMap insertOfUnExist(NeoMap dataMap, String... searchColumnKey);

    /**
     * 不存在则新增，存在则忽略
     *
     * @param object 新增的实体
     * @param searchColumnKey 作为搜索条件的数据的key，这个key是属性名转换为db字段的名字
     * @return 插入后的数据
     */
    <T> T insertOfUnExist(T object, String... searchColumnKey);


    /**
     * save功能
     * <p>
     *     数据如果存在，则更新，否则插入
     * @param dataMap 待更新或者插入的数据
     * @param searchColumnKey 作为搜索条件的搜索的key
     * @return 更新或者插入后的数据
     */
    NeoMap save(NeoMap dataMap, String... searchColumnKey);

    /**
     * save功能
     * <p>
     *     数据如果存在，则更新，否则插入
     * @param object 待更新或者插入的数据
     * @param searchColumnKey 作为搜索条件的数据的key，这个key是属性名转换为db字段的名字
     * @return 更新或者插入后的数据
     */
    <T> T save(T object, String... searchColumnKey);


    Integer delete(NeoMap searchMap);

    Integer delete(SearchExpress searchExpress);

    <T> Integer delete(T object);

    Integer delete( Number id);


    NeoMap update(NeoMap dataMap, NeoMap searchMap);

    NeoMap update(NeoMap dataMap, SearchExpress searchExpress);

    <T> T update(T setEntity, NeoMap searchMap);

    <T> T update(T setEntity, SearchExpress searchExpress);

    <T> T update(T setEntity, T searchEntity);

    NeoMap update(NeoMap dataMap, Columns columns);

    <T> NeoMap update(NeoMap setMap, T searchEntity);

    <T> T update(T entity, Columns columns);

    NeoMap update(NeoMap dataMap);

    <T> T update(T entity);


    NeoMap one(Columns columns, NeoMap searchMap);

    NeoMap one(Columns columns, SearchExpress searchExpress);

    <T> T one(Columns columns, T entity);

    NeoMap one(Columns columns, Number key);

    NeoMap one(NeoMap searchMap);

    NeoMap one(SearchExpress searchExpress);

    <T> T one(T entity);

    NeoMap one(Number id);

    <T> T one(Class<T> tClass, Columns columns, NeoMap searchMap);

    <T> T one(Class<T> tClass, Columns columns, SearchExpress searchExpress);

    <T> T one(Class<T> tClass, Columns columns, Number key);

    <T> T one(Class<T> tClass, NeoMap searchMap);

    <T> T one(Class<T> tClass, SearchExpress searchExpress);

    <T> T one(Class<T> tClass, Number id);


    List<NeoMap> list(Columns columns, NeoMap searchMap);

    List<NeoMap> list(Columns columns, SearchExpress searchExpress);

    <T> List<T> list(Columns columns, T entity);

    List<NeoMap> list(NeoMap searchMap);

    List<NeoMap> list(SearchExpress searchExpress);

    <T> List<T> list(T entity);

    List<NeoMap> list(Columns columns);

    <T> List<T> list(Class<T> tClass, Columns columns, NeoMap searchMap);

    <T> List<T> list(Class<T> tClass, Columns columns, SearchExpress searchExpress);

    <T> List<T> list(Class<T> tClass, NeoMap searchMap);

    <T> List<T> list(Class<T> tClass, SearchExpress searchExpress);

    <T> List<T> list(Class<T> tClass, Columns columns);


    <T> T value(Class<T> tClass, String field, NeoMap searchMap);

    <T> T value(Class<T> tClass, String field, SearchExpress searchExpress);

    <T> T value(Class<T> tClass, String field, Object entity);

    String value(String field, NeoMap searchMap);

    String value(String field, SearchExpress searchExpress);

    String value(String field, Object entity);

    String value(String field, Number entity);


    <T> List<T> values(Class<T> tClass, String field, NeoMap searchMap);

    <T> List<T> values(Class<T> tClass, String field, SearchExpress searchExpress);

    <T> List<T> values(Class<T> tClass, String field, Object entity);

    List<String> values(String field, NeoMap searchMap);

    List<String> values(String field, SearchExpress searchExpress);

    List<String> values(String field, Object entity);

    List<String> values(String field);


    <T> List<T> valuesOfDistinct(Class<T> tClass, String field, NeoMap searchMap);

    <T> List<T> valuesOfDistinct(Class<T> tClass, String field, SearchExpress searchExpress);

    <T> List<T> valuesOfDistinct(Class<T> tClass, String field, Object entity);

    List<String> valuesOfDistinct(String field, NeoMap searchMap);

    List<String> valuesOfDistinct(String field, SearchExpress searchExpress);

    List<String> valuesOfDistinct(String field, Object entity);

    List<String> valuesOfDistinct(String field);


    List<NeoMap> page(Columns columns, NeoMap searchMap, NeoPage page);

    List<NeoMap> page(Columns columns, SearchExpress searchExpress, NeoPage page);

    <T> List<T> page(Columns columns, T entity, NeoPage page);

    List<NeoMap> page(NeoMap searchMap, NeoPage page);

    List<NeoMap> page(SearchExpress searchExpress, NeoPage page);

    <T> List<T> page(T entity, NeoPage page);

    List<NeoMap> page(Columns columns, NeoPage page);

    List<NeoMap> page(NeoPage page);

    <T> List<T> page(Class<T> tClass, Columns columns, NeoMap searchMap, NeoPage page);

    <T> List<T> page(Class<T> tClass, Columns columns, SearchExpress searchExpress, NeoPage page);

    <T> List<T> page(Class<T> tClass, NeoMap searchMap, NeoPage page);

    <T> List<T> page(Class<T> tClass, SearchExpress searchExpress, NeoPage page);

    <T> List<T> page(Class<T> tClass, Columns columns, NeoPage page);

    <T> List<T> page(Class<T> tClass, NeoPage page);


    default PageRsp<NeoMap> getPage(Columns columns, NeoMap searchMap, NeoPage page) {
        return new PageRsp<>(page(columns, searchMap, page), count(searchMap));
    }

    default PageRsp<NeoMap> getPage(Columns columns, SearchExpress searchExpress, NeoPage page) {
        return new PageRsp<>(page(columns, searchExpress, page), count(searchExpress));
    }

    default <T> PageRsp<T> getPage(Columns columns, T entity, NeoPage page) {
        return new PageRsp<>(page(columns, entity, page), count(entity));
    }

    default PageRsp<NeoMap> getPage(NeoMap searchMap, NeoPage page) {
        return new PageRsp<>(page(searchMap, page), count(searchMap));
    }

    default PageRsp<NeoMap> getPage(SearchExpress searchExpress, NeoPage page) {
        return new PageRsp<>(page(searchExpress, page), count(searchExpress));
    }

    default <T> PageRsp<T> getPage(T entity, NeoPage page) {
        return new PageRsp<>(page(entity, page), count(entity));
    }

    default PageRsp<NeoMap> getPage(Columns columns, NeoPage page) {
        return new PageRsp<>(page(columns, page), count(columns));
    }

    default PageRsp<NeoMap> getPage(NeoPage page) {
        return new PageRsp<>(page(page), count());
    }

    default <T> PageRsp<T> getPage(Class<T> tClass, Columns columns, NeoMap searchMap, NeoPage page) {
        return new PageRsp<>(page(tClass, columns, searchMap, page), count(searchMap));
    }

    default <T> PageRsp<T> getPage(Class<T> tClass, Columns columns, SearchExpress searchExpress, NeoPage page) {
        return new PageRsp<>(page(tClass, columns, searchExpress, page), count(searchExpress));
    }

    default <T> PageRsp<T> getPage(Class<T> tClass, NeoMap searchMap, NeoPage page) {
        return new PageRsp<>(page(tClass, searchMap, page), count(searchMap));
    }

    default <T> PageRsp<T> getPage(Class<T> tClass, SearchExpress searchExpress, NeoPage page) {
        return new PageRsp<>(page(tClass, searchExpress, page), count(searchExpress));
    }

    default <T> PageRsp<T> getPage(Class<T> tClass, Columns columns, NeoPage page) {
        return new PageRsp<>(page(tClass, columns, page), count());
    }

    default <T> PageRsp<T> getPage(Class<T> tClass, NeoPage page) {
        return new PageRsp<>(page(tClass, page), count());
    }


    Integer count(NeoMap searchMap);

    Integer count(SearchExpress searchExpress);

    Integer count(Object entity);

    Integer count();


    Boolean exist(NeoMap searchMap);

    Boolean exist(SearchExpress searchExpress);

    Boolean exist(Object entity);

    Boolean exist(Number id);


    Integer batchInsert(List<NeoMap> dataMapList);

    <T> Integer batchInsertEntity(List<T> dataList);


    Integer batchUpdate(List<NeoMap> dataList);

    Integer batchUpdate(List<NeoMap> dataList, Columns searchColumns);

    <T> Integer batchUpdateEntity(List<T> dataList);

    <T> Integer batchUpdateEntity(List<T> dataList, Columns searchColumns);
}
