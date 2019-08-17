package com.simonalong.neo.db;

import com.simonalong.neo.Columns;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.NeoMap.NamingChg;
import com.simonalong.neo.table.NeoPage;
import java.util.List;

/**
 * @author zhouzhenyong
 * @since 2019-08-17 16:41
 */
public interface BaseTableNeo extends SyncNeo {

    NeoMap insert(NeoMap dataMap);

    <T> T insert(T object);

    <T> T insert(T object, NamingChg naming);


    Integer delete(NeoMap searchMap);

    <T> Integer delete(T object);

    <T> Integer delete(T object, NamingChg naming);

    Integer delete( Number id);


    NeoMap update(NeoMap dataMap, NeoMap searchMap);

    <T> T update(T setEntity, NeoMap searchMap, NamingChg namingChg);

    <T> T update(T setEntity, NeoMap searchMap);

    <T> T update(T setEntity, T searchEntity);

    NeoMap update(NeoMap dataMap, Columns columns);

    <T> T update(T entity, Columns columns, NamingChg namingChg);

    <T> T update(T entity, Columns columns);

    NeoMap update(NeoMap dataMap);

    <T> T update(T entity);


    NeoMap one(Columns columns, NeoMap searchMap);

    <T> T one(Columns columns, T entity);

    NeoMap one(Columns columns, Number key);

    NeoMap one(NeoMap searchMap);

    <T> T one(T entity);

    NeoMap one(Number id);


    List<NeoMap> list(Columns columns, NeoMap searchMap);

    <T> List<T> list(Columns columns, T entity);

    List<NeoMap> list(NeoMap searchMap);

    <T> List<T> list(T entity);


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

    List<NeoMap> page(Columns columns, NeoMap searchMap);

    <T> List<T> page(Columns columns, T entity, NeoPage page);

    List<NeoMap> page(NeoMap searchMap, NeoPage page);

    <T> List<T> page(T entity, NeoPage page);

    List<NeoMap> page(Columns columns, NeoPage page);

    List<NeoMap> page(NeoPage page);

    List<NeoMap> page(NeoMap searchMap);


    Integer count(NeoMap searchMap);

    Integer count(Object entity);

    Integer count(String tableName);
}
