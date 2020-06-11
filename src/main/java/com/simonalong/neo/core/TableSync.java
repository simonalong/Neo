package com.simonalong.neo.core;

import com.simonalong.neo.Columns;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.db.NeoPage;
import java.util.List;

/**
 * @author zhouzhenyong
 * @since 2019-08-17 17:16
 */
public interface TableSync extends Sync {

    NeoMap insert(NeoMap dataMap);

    <T> T insert(T object);


    Integer delete(NeoMap searchMap);

    <T> Integer delete(T object);

    Integer delete( Number id);


    NeoMap update(NeoMap dataMap, NeoMap searchMap);

    <T> T update(T setEntity, NeoMap searchMap);

    <T> T update(T setEntity, T searchEntity);

    NeoMap update(NeoMap dataMap, Columns columns);

    <T> NeoMap update(NeoMap setMap, T searchEntity);

    <T> T update(T entity, Columns columns);

    NeoMap update(NeoMap dataMap);

    <T> T update(T entity);


    NeoMap one(Columns columns, NeoMap searchMap);

    <T> T one(Columns columns, T entity);

    NeoMap one(Columns columns, Number key);

    NeoMap one(NeoMap searchMap);

    <T> T one(T entity);

    NeoMap one(Number id);

    <T> T one(Class<T> tClass, Columns columns, NeoMap searchMap);

    <T> T one(Class<T> tClass, Columns columns, Number key);

    <T> T one(Class<T> tClass, NeoMap searchMap);

    <T> T one(Class<T> tClass, Number id);


    List<NeoMap> list(Columns columns, NeoMap searchMap);

    <T> List<T> list(Columns columns, T entity);

    List<NeoMap> list(NeoMap searchMap);

    <T> List<T> list(T entity);

    List<NeoMap> list(Columns columns);

    <T> List<T> list(Class<T> tClass, Columns columns, NeoMap searchMap);

    <T> List<T> list(Class<T> tClass, NeoMap searchMap);

    <T> List<T> list(Class<T> tClass, Columns columns);


    <T> T value(Class<T> tClass, String field, NeoMap searchMap);

    <T> T value(Class<T> tClass, String field, Object entity);

    String value(String field, NeoMap searchMap);

    String value(String field, Object entity);

    String value(String field, Number entity);

    <T> List<T> values(Class<T> tClass, String field, NeoMap searchMap);

    <T> List<T> values(Class<T> tClass, String field, Object entity);

    List<String> values(String field, NeoMap searchMap);

    List<String> values(String field, Object entity);

    List<String> values(String field);


    List<NeoMap> page(Columns columns, NeoMap searchMap, NeoPage page);

    <T> List<T> page(Columns columns, T entity, NeoPage page);

    List<NeoMap> page(NeoMap searchMap, NeoPage page);

    <T> List<T> page(T entity, NeoPage page);

    List<NeoMap> page(Columns columns, NeoPage page);

    List<NeoMap> page(NeoPage page);

    <T> List<T> page(Class<T> tClass, Columns columns, NeoMap searchMap, NeoPage page);

    <T> List<T> page(Class<T> tClass, NeoMap searchMap, NeoPage page);

    <T> List<T> page(Class<T> tClass, Columns columns, NeoPage page);

    <T> List<T> page(Class<T> tClass, NeoPage page);


    Integer count(NeoMap searchMap);

    Integer count(Object entity);

    Integer count();


    Boolean exist(NeoMap searchMap);

    Boolean exist(Object entity);


    Integer batchInsert(List<NeoMap> dataMapList);

    <T> Integer batchInsertEntity(List<T> dataList);


    Integer batchUpdate(List<NeoMap> dataList);

    Integer batchUpdate(List<NeoMap> dataList, Columns columns);

    <T> Integer batchUpdateEntity(List<T> dataList);

    <T> Integer batchUpdateEntity(List<T> dataList, Columns columns);
}
