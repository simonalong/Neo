package com.simonalong.neo.core;

import com.simonalong.neo.Columns;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.db.NeoPage;
import com.simonalong.neo.express.Express;

import java.util.List;

/**
 * @author zhouzhenyong
 * @since 2019-08-17 17:16
 */
public interface TableSync extends Sync {

    NeoMap insert(NeoMap dataMap);

    <T> T insert(T object);

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

    <T> Integer delete(T object);

    Integer delete( Number id);


    NeoMap update(NeoMap dataMap, NeoMap searchMap);

    NeoMap update(NeoMap dataMap, Express searchExpress);

    <T> T update(T setEntity, NeoMap searchMap);

    <T> T update(T setEntity, Express searchExpress);

    <T> T update(T setEntity, T searchEntity);

    NeoMap update(NeoMap dataMap, Columns columns);

    <T> NeoMap update(NeoMap setMap, T searchEntity);

    <T> T update(T entity, Columns columns);

    NeoMap update(NeoMap dataMap);

    <T> T update(T entity);


    NeoMap one(Columns columns, NeoMap searchMap);

    NeoMap one(Columns columns, Express searchExpress);

    <T> T one(Columns columns, T entity);

    NeoMap one(Columns columns, Number key);

    NeoMap one(NeoMap searchMap);

    <T> T one(T entity);

    NeoMap one(Number id);

    <T> T one(Class<T> tClass, Columns columns, NeoMap searchMap);

    <T> T one(Class<T> tClass, Columns columns, Express searchExpress);

    <T> T one(Class<T> tClass, Columns columns, Number key);

    <T> T one(Class<T> tClass, NeoMap searchMap);

    <T> T one(Class<T> tClass, Express searchExpress);

    <T> T one(Class<T> tClass, Number id);


    List<NeoMap> list(Columns columns, NeoMap searchMap);

    List<NeoMap> list(Columns columns, Express searchExpress);

    <T> List<T> list(Columns columns, T entity);

    List<NeoMap> list(NeoMap searchMap);

    List<NeoMap> list(Express searchExpress);

    <T> List<T> list(T entity);

    List<NeoMap> list(Columns columns);

    <T> List<T> list(Class<T> tClass, Columns columns, NeoMap searchMap);

    <T> List<T> list(Class<T> tClass, Columns columns, Express searchExpress);

    <T> List<T> list(Class<T> tClass, NeoMap searchMap);

    <T> List<T> list(Class<T> tClass, Columns columns);


    <T> T value(Class<T> tClass, String field, NeoMap searchMap);

    <T> T value(Class<T> tClass, String field, Express searchExpress);

    <T> T value(Class<T> tClass, String field, Object entity);

    String value(String field, NeoMap searchMap);

    String value(String field, Express searchExpress);

    String value(String field, Object entity);

    String value(String field, Number entity);

    <T> List<T> values(Class<T> tClass, String field, NeoMap searchMap);

    <T> List<T> values(Class<T> tClass, String field, Express searchExpress);

    <T> List<T> values(Class<T> tClass, String field, Object entity);

    List<String> values(String field, NeoMap searchMap);

    List<String> values(String field, Object entity);

    List<String> values(String field);


    List<NeoMap> page(Columns columns, NeoMap searchMap, NeoPage page);

    List<NeoMap> page(Columns columns, Express searchExpress, NeoPage page);

    <T> List<T> page(Columns columns, T entity, NeoPage page);

    List<NeoMap> page(NeoMap searchMap, NeoPage page);

    List<NeoMap> page(Express searchExpress, NeoPage page);

    <T> List<T> page(T entity, NeoPage page);

    List<NeoMap> page(Columns columns, NeoPage page);

    List<NeoMap> page(NeoPage page);

    <T> List<T> page(Class<T> tClass, Columns columns, NeoMap searchMap, NeoPage page);

    <T> List<T> page(Class<T> tClass, Columns columns, Express searchExpress, NeoPage page);

    <T> List<T> page(Class<T> tClass, NeoMap searchMap, NeoPage page);

    <T> List<T> page(Class<T> tClass, Express searchExpress, NeoPage page);

    <T> List<T> page(Class<T> tClass, Columns columns, NeoPage page);

    <T> List<T> page(Class<T> tClass, NeoPage page);


    Integer count(NeoMap searchMap);

    Integer count(Express searchExpress);

    Integer count(Object entity);

    Integer count();


    Boolean exist(NeoMap searchMap);

    Boolean exist(Express searchExpress);

    Boolean exist(Object entity);


    Integer batchInsert(List<NeoMap> dataMapList);

    <T> Integer batchInsertEntity(List<T> dataList);


    Integer batchUpdate(List<NeoMap> dataList);

    Integer batchUpdate(List<NeoMap> dataList, Columns searchColumns);

    <T> Integer batchUpdateEntity(List<T> dataList);

    <T> Integer batchUpdateEntity(List<T> dataList, Columns searchColumns);
}
