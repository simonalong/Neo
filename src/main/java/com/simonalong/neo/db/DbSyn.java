package com.simonalong.neo.db;

import com.simonalong.neo.Columns;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.NeoMap.NamingChg;
import com.simonalong.neo.table.NeoPage;
import java.util.List;

/**
 * @author zhouzhenyong
 * @since 2019-08-17 17:07
 */
public interface DbSyn extends SyncNeo {

    NeoMap insert(String tableName, NeoMap dataMap);

    <T> T insert(String tableName, T object);

    <T> T insert(String tableName, T object, NamingChg naming);


    Integer delete(String tableName, NeoMap searchMap);

    <T> Integer delete(String tableName, T object);

    <T> Integer delete(String tableName, T object, NamingChg naming);

    Integer delete(String tableName, Number id);


    NeoMap update(String tableName, NeoMap dataMap, NeoMap searchMap);

    <T> T update(String tableName, T setEntity, NeoMap searchMap, NamingChg namingChg);

    <T> T update(String tableName, T setEntity, NeoMap searchMap);

    <T> T update(String tableName, T setEntity, T searchEntity);

    NeoMap update(String tableName, NeoMap dataMap, Columns columns);

    <T> T update(String tableName, T entity, Columns columns, NamingChg namingChg);

    <T> T update(String tableName, T entity, Columns columns);

    NeoMap update(String tableName, NeoMap dataMap);

    <T> T update(String tableName, T entity);


    NeoMap one(String tableName, Columns columns, NeoMap searchMap);

    <T> T one(String tableName, Columns columns, T entity);

    NeoMap one(String tableName, Columns columns, Number key);

    NeoMap one(String tableName, NeoMap searchMap);

    <T> T one(String tableName, T entity);

    NeoMap one(String tableName, Number id);


    List<NeoMap> list(String tableName, Columns columns, NeoMap searchMap);

    <T> List<T> list(String tableName, Columns columns, T entity);

    List<NeoMap> list(String tableName, NeoMap searchMap);

    <T> List<T> list(String tableName, T entity);


    <T> T value(String tableName, Class<T> tClass, String field, NeoMap searchMap);

    <T> T value(String tableName, Class<T> tClass, String field, Object entity);

    String value(String tableName, String field, NeoMap searchMap);

    String value(String tableName, String field, Object entity);

    String value(String tableName, String field, Number entity);


    <T> List<T> values(String tableName, Class<T> tClass, String field, NeoMap searchMap);

    <T> List<T> values(String tableName, Class<T> tClass, String field, Object entity);

    List<String> values(String tableName, String field, NeoMap searchMap);

    List<String> values(String tableName, String field, Object entity);

    List<String> values(String tableName, String field);


    List<NeoMap> page(String tableName, Columns columns, NeoMap searchMap, NeoPage page);

    List<NeoMap> page(String tableName, Columns columns, NeoMap searchMap);

    <T> List<T> page(String tableName, Columns columns, T entity, NeoPage page);

    List<NeoMap> page(String tableName, NeoMap searchMap, NeoPage page);

    <T> List<T> page(String tableName, T entity, NeoPage page);

    List<NeoMap> page(String tableName, Columns columns, NeoPage page);

    List<NeoMap> page(String tableName, NeoPage page);

    List<NeoMap> page(String tableName, NeoMap searchMap);


    Integer count(String tableName, NeoMap searchMap);

    Integer count(String tableName, Object entity);

    Integer count(String tableName);
}
