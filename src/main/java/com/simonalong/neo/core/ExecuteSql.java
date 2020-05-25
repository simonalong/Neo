package com.simonalong.neo.core;

import com.simonalong.neo.TableMap;
import com.simonalong.neo.db.NeoPage;

import java.util.List;

/**
 * @author shizi
 * @since 2020/4/4 11:55 PM
 */
public interface ExecuteSql {


    TableMap exeOne(String sql, Object... parameters);

    <T> T exeOne(Class<T> tClass, String sql, Object... parameters);

    List<TableMap> exeList(String sql, Object... parameters);

    <T> List<T> exeList(Class<T> tClass, String sql, Object... parameters);

    <T> T exeValue(Class<T> tClass, String sql, Object... parameters);

    String exeValue(String sql, Object... parameters);

    <T> List<T> exeValues(Class<T> tClass, String sql, Object... parameters);

    List<String> exeValues(String sql, Object... parameters);

    List<TableMap> exePage(String sql, Integer startIndex, Integer pageSize, Object... parameters);

    <T> List<T> exePage(Class<T> tClass, String sql, Integer startIndex, Integer pageSize, Object... parameters);

    List<TableMap> exePage(String sql, NeoPage neoPage, Object... parameters);

    <T> List<T> exePage(Class<T> tClass, String sql, NeoPage neoPage, Object... parameters);

    Integer exeCount(String sql, Object... parameters);

    List<List<TableMap>> execute(String sql, Object... parameters);
}
