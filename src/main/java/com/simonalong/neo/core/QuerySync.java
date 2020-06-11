package com.simonalong.neo.core;

import com.simonalong.neo.Columns;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.db.NeoPage;

import java.util.List;

/**
 * @author shizi
 * @since 2020/6/11 2:58 PM
 */
public interface QuerySync extends Sync {

    NeoMap one(String tableName, Columns columns, NeoMap searchMap);

    <T> T one(String tableName, Columns columns, T entity);

    NeoMap one(String tableName, Columns columns, Number key);

    NeoMap one(String tableName, NeoMap searchMap);

    <T> T one(String tableName, T entity);

    NeoMap one(String tableName, Number id);

    <T> T one(Class<T> tClass, String tableName, Columns columns, NeoMap searchMap);

    <T> T  one(Class<T> tClass, String tableName, Columns columns, Number key);

    <T> T  one(Class<T> tClass, String tableName, NeoMap searchMap);

    <T> T  one(Class<T> tClass, String tableName, Number id);


    List<NeoMap> list(String tableName, Columns columns, NeoMap searchMap);

    <T> List<T> list(String tableName, Columns columns, T entity);

    List<NeoMap> list(String tableName, NeoMap searchMap);

    <T> List<T> list(String tableName, T entity);

    List<NeoMap> list(String tableName, Columns columns);

    <T> List<T> list(Class<T> tClass, String tableName, Columns columns, NeoMap searchMap);

    <T> List<T> list(Class<T> tClass, String tableName, NeoMap searchMap);

    <T> List<T> list(Class<T> tClass, String tableName, Columns columns);


    @Deprecated
    <T> T value(String tableName, Class<T> tClass, String field, NeoMap searchMap);

    @Deprecated
    <T> T value(String tableName, Class<T> tClass, String field, Object entity);

    <T> T value(Class<T> tClass, String tableName, String field, NeoMap searchMap);

    <T> T value(Class<T> tClass, String tableName, String field, Object entity);

    String value(String tableName, String field, NeoMap searchMap);

    String value(String tableName, String field, Object entity);

    String value(String tableName, String field, Number id);


    @Deprecated
    <T> List<T> values(String tableName, Class<T> tClass, String field, NeoMap searchMap);

    @Deprecated
    <T> List<T> values(String tableName, Class<T> tClass, String field, Object entity);

    <T> List<T> values(Class<T> tClass, String tableName, String field, NeoMap searchMap);

    <T> List<T> values(Class<T> tClass, String tableName, String field, Object entity);

    List<String> values(String tableName, String field, NeoMap searchMap);

    List<String> values(String tableName, String field, Object entity);

    List<String> values(String tableName, String field);


    List<NeoMap> page(String tableName, Columns columns, NeoMap searchMap, NeoPage page);

    <T> List<T> page(String tableName, Columns columns, T entity, NeoPage page);

    List<NeoMap> page(String tableName, NeoMap searchMap, NeoPage page);

    <T> List<T> page(String tableName, T entity, NeoPage page);

    List<NeoMap> page(String tableName, Columns columns, NeoPage page);

    List<NeoMap> page(String tableName, NeoPage page);

    <T> List<T> page(Class<T> tClass, String tableName, Columns columns, NeoMap searchMap, NeoPage page);

    <T> List<T> page(Class<T> tClass, String tableName, NeoMap searchMap, NeoPage page);

    <T> List<T> page(Class<T> tClass, String tableName, Columns columns, NeoPage page);

    <T> List<T> page(Class<T> tClass, String tableName, NeoPage page);


    Integer count(String tableName, NeoMap searchMap);

    Integer count(String tableName, Object entity);

    Integer count(String tableName);


    Boolean exist(String tableName, NeoMap searchMap);

    Boolean exist(String tableName, Object entity);
}
