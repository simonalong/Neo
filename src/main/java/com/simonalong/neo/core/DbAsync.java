package com.simonalong.neo.core;

import com.simonalong.neo.Columns;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.TableMap;
import com.simonalong.neo.db.NeoPage;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author zhouzhenyong
 * @since 2019-08-17 18:09
 */
public interface DbAsync extends AsyncNeo {

    CompletableFuture<NeoMap> insertAsync(String tableName, NeoMap dataMap, Executor executor);

    CompletableFuture<NeoMap> insertAsync(String tableName, NeoMap dataMap);

    <T> CompletableFuture<T> insertAsync(String tableName, T object, Executor executor);

    <T> CompletableFuture<T> insertAsync(String tableName, T object);


    CompletableFuture<Integer> deleteAsync(String tableName, NeoMap dataMap, Executor executor);

    CompletableFuture<Integer> deleteAsync(String tableName, NeoMap dataMap);

    <T> CompletableFuture<Integer> deleteAsync(String tableName, T object, Executor executor);

    <T> CompletableFuture<Integer> deleteAsync(String tableName, T object);

    CompletableFuture<Integer> deleteAsync(String tableName, Number id, Executor executor);

    CompletableFuture<Integer> deleteAsync(String tableName, Number id);


    CompletableFuture<NeoMap> updateAsync(String tableName, NeoMap dataMap, NeoMap searchMap, Executor executor);

    CompletableFuture<NeoMap> updateAsync(String tableName, NeoMap dataMap, NeoMap searchMap);

    <T> CompletableFuture<T> updateAsync(String tableName, T setEntity, NeoMap searchMap, Executor executor);

    <T> CompletableFuture<T> updateAsync(String tableName, T setEntity, NeoMap searchMap);

    <T> CompletableFuture<T> updateAsync(String tableName, T setEntity, T searchEntity, Executor executor);

    <T> CompletableFuture<T> updateAsync(String tableName, T setEntity, T searchEntity);

    CompletableFuture<NeoMap> updateAsync(String tableName, NeoMap dataMap, Columns columns, Executor executor);

    CompletableFuture<NeoMap> updateAsync(String tableName, NeoMap dataMap, Columns columns);

    <T> CompletableFuture<T> updateAsync(String tableName, T entity, Columns columns, Executor executor);

    <T> CompletableFuture<T> updateAsync(String tableName, T entity, Columns columns);

    CompletableFuture<NeoMap> updateAsync(String tableName, NeoMap dataMap, Executor executor);

    CompletableFuture<NeoMap> updateAsync(String tableName, NeoMap dataMap);

    <T> CompletableFuture<T> updateAsync(String tableName, T entity, Executor executor);

    <T> CompletableFuture<T> updateAsync(String tableName, T entity);


    CompletableFuture<NeoMap> oneAsync(String tableName, Columns columns, NeoMap searchMap, Executor executor);

    CompletableFuture<NeoMap> oneAsync(String tableName, Columns columns, NeoMap searchMap);

    <T> CompletableFuture<T> oneAsync(String tableName, Columns columns, T entity, Executor executor);

    <T> CompletableFuture<T> oneAsync(String tableName, Columns columns, T entity);

    CompletableFuture<NeoMap> oneAsync(String tableName, NeoMap searchMap, Executor executor);

    CompletableFuture<NeoMap> oneAsync(String tableName, NeoMap searchMap);

    <T> CompletableFuture<T> oneAsync(String tableName, T entity, Executor executor);

    <T> CompletableFuture<T> oneAsync(String tableName, T entity);

    CompletableFuture<NeoMap> oneAsync(String tableName, Number id, Executor executor);

    CompletableFuture<NeoMap> oneAsync(String tableName, Number id);

    <T> CompletableFuture<T> oneAsync(Class<T> tClass, String tableName, Columns columns, NeoMap searchMap, Executor executor);

    <T> CompletableFuture<T> oneAsync(Class<T> tClass, String tableName, Columns columns, NeoMap searchMap);

    <T> CompletableFuture<T> oneAsync(Class<T> tClass, String tableName, NeoMap searchMap, Executor executor);

    <T> CompletableFuture<T> oneAsync(Class<T> tClass, String tableName, NeoMap searchMap);

    <T> CompletableFuture<T> oneAsync(Class<T> tClass, String tableName, Number id, Executor executor);

    <T> CompletableFuture<T> oneAsync(Class<T> tClass, String tableName, Number id);


    CompletableFuture<List<NeoMap>> listAsync(String tableName, Columns columns, NeoMap searchMap, Executor executor);

    CompletableFuture<List<NeoMap>> listAsync(String tableName, Columns columns, NeoMap searchMap);

    <T> CompletableFuture<List<T>> listAsync(String tableName, Columns columns, T entity, Executor executor);

    <T> CompletableFuture<List<T>> listAsync(String tableName, Columns columns, T entity);

    CompletableFuture<List<NeoMap>> listAsync(String tableName, NeoMap searchMap, Executor executor);

    CompletableFuture<List<NeoMap>> listAsync(String tableName, NeoMap searchMap);

    <T> CompletableFuture<List<T>> listAsync(String tableName, T entity, Executor executor);

    <T> CompletableFuture<List<T>> listAsync(String tableName, T entity);

    <T> CompletableFuture<List<T>> listAsync(Class<T> tClass, String tableName, Columns columns, NeoMap searchMap, Executor executor);

    <T> CompletableFuture<List<T>> listAsync(Class<T> tClass, String tableName, Columns columns, NeoMap searchMap);

    <T> CompletableFuture<List<T>> listAsync(Class<T> tClass, String tableName, NeoMap searchMap, Executor executor);

    <T> CompletableFuture<List<T>> listAsync(Class<T> tClass, String tableName, NeoMap searchMap);


    <T> CompletableFuture<List<T>> valuesAsync(String tableName, Class<T> tClass, String field, NeoMap searchMap, Executor executor);

    <T> CompletableFuture<List<T>> valuesAsync(String tableName, Class<T> tClass, String field, NeoMap searchMap);

    <T> CompletableFuture<List<T>> valuesAsync(String tableName, Class<T> tClass, String field, Object entity, Executor executor);

    <T> CompletableFuture<List<T>> valuesAsync(String tableName, Class<T> tClass, String field, Object entity);

    CompletableFuture<List<String>> valuesAsync(String tableName, String field, NeoMap searchMap, Executor executor);

    CompletableFuture<List<String>> valuesAsync(String tableName, String field, NeoMap searchMap);

    CompletableFuture<List<String>> valuesAsync(String tableName, String field, Object entity, Executor executor);

    CompletableFuture<List<String>> valuesAsync(String tableName, String field, Object entity);

    CompletableFuture<List<String>> valuesAsync(String tableName, String field, Executor executor);

    CompletableFuture<List<String>> valuesAsync(String tableName, String field);


    <T> CompletableFuture<T> valueAsync(String tableName, Class<T> tClass, String field, NeoMap searchMap, Executor executor);

    <T> CompletableFuture<T> valueAsync(String tableName, Class<T> tClass, String field, NeoMap searchMap);

    <T> CompletableFuture<T> valueAsync(String tableName, Class<T> tClass, String field, Object entity, Executor executor);

    <T> CompletableFuture<T> valueAsync(String tableName, Class<T> tClass, String field, Object entity);

    CompletableFuture<String> valueAsync(String tableName, String field, NeoMap searchMap, Executor executor);

    CompletableFuture<String> valueAsync(String tableName, String field, NeoMap searchMap);

    CompletableFuture<String> valueAsync(String tableName, String field, Object entity, Executor executor);

    CompletableFuture<String> valueAsync(String tableName, String field, Object entity);


    CompletableFuture<List<NeoMap>> pageAsync(String tableName, Columns columns, NeoMap searchMap, NeoPage page, Executor executor);

    CompletableFuture<List<NeoMap>> pageAsync(String tableName, Columns columns, NeoMap searchMap, NeoPage page);

    <T> CompletableFuture<List<T>> pageAsync(String tableName, Columns columns, T entity, NeoPage page, Executor executor);

    <T> CompletableFuture<List<T>> pageAsync(String tableName, Columns columns, T entity, NeoPage page);

    CompletableFuture<List<NeoMap>> pageAsync(String tableName, NeoMap searchMap, NeoPage page, Executor executor);

    CompletableFuture<List<NeoMap>> pageAsync(String tableName, NeoMap searchMap, NeoPage page);

    <T> CompletableFuture<List<T>> pageAsync(String tableName, T entity, NeoPage page, Executor executor);

    <T> CompletableFuture<List<T>> pageAsync(String tableName, T entity, NeoPage page);

    CompletableFuture<List<NeoMap>> pageAsync(String tableName, Columns columns, NeoPage page, Executor executor);

    CompletableFuture<List<NeoMap>> pageAsync(String tableName, Columns columns, NeoPage page);

    CompletableFuture<List<NeoMap>> pageAsync(String tableName, NeoPage page, Executor executor);

    CompletableFuture<List<NeoMap>> pageAsync(String tableName, NeoPage page);


    CompletableFuture<Integer> countAsync(String tableName, NeoMap searchMap, Executor executor);

    CompletableFuture<Integer> countAsync(String tableName, NeoMap searchMap);

    CompletableFuture<Integer> countAsync(String tableName, Object entity, Executor executor);

    CompletableFuture<Integer> countAsync(String tableName, Object entity);

    CompletableFuture<Integer> countAsync(String tableName, Executor executor);

    CompletableFuture<Integer> countAsync(String tableName);


    CompletableFuture<Integer> batchInsertAsync(String tableName, List<NeoMap> dataMapList);

    CompletableFuture<Integer> batchInsertAsync(String tableName, List<NeoMap> dataMapList, Executor executor);

    <T> CompletableFuture<Integer> batchInsertEntityAsync(String tableName, List<T> dataList);

    <T> CompletableFuture<Integer> batchInsertEntityAsync(String tableName, List<T> dataList, Executor executor);


    CompletableFuture<Integer> batchUpdateAsync(String tableName, List<NeoMap> dataList);

    CompletableFuture<Integer> batchUpdateAsync(String tableName, List<NeoMap> dataList, Executor executor);

    CompletableFuture<Integer> batchUpdateAsync(String tableName, List<NeoMap> dataList, Columns columns);

    CompletableFuture<Integer> batchUpdateAsync(String tableName, List<NeoMap> dataList, Columns columns, Executor executor);

    <T> CompletableFuture<Integer> batchUpdateEntityAsync(String tableName, List<T> dataList);

    <T> CompletableFuture<Integer> batchUpdateEntityAsync(String tableName, List<T> dataList, Executor executor);

    <T> CompletableFuture<Integer> batchUpdateEntityAsync(String tableName, List<T> dataList, Columns columns);

    <T> CompletableFuture<Integer> batchUpdateEntityAsync(String tableName, List<T> dataList, Columns columns, Executor executor);


    CompletableFuture<TableMap> exeOneAsync(String sql, Executor executor, Object... parameters);

    CompletableFuture<TableMap> exeOneAsync(String sql, Object... parameters);

    <T> CompletableFuture<T> exeOneAsync(Class<T> tClass, String sql, Executor executor, Object... parameters);

    <T> CompletableFuture<T> exeOneAsync(Class<T> tClass, String sql, Object... parameters);


    CompletableFuture<List<TableMap>> exeListAsync(String sql, Executor executor, Object... parameters);

    CompletableFuture<List<TableMap>> exeListAsync(String sql, Object... parameters);

    <T> CompletableFuture<List<T>> exeListAsync(Class<T> tClass, String sql, Executor executor, Object... parameters);

    <T> CompletableFuture<List<T>> exeListAsync(Class<T> tClass, String sql, Object... parameters);


    <T> CompletableFuture<T> exeValueAsync(Class<T> tClass, String sql, Executor executor, Object... parameters);

    <T> CompletableFuture<T> exeValueAsync(Class<T> tClass, String sql, Object... parameters);

    CompletableFuture<String> exeValueAsync(String sql, Executor executor, Object... parameters);

    CompletableFuture<String> exeValueAsync(String sql, Object... parameters);


    <T> CompletableFuture<List<T>> exeValuesAsync(Class<T> tClass, String sql, Executor executor, Object... parameters);

    <T> CompletableFuture<List<T>> exeValuesAsync(Class<T> tClass, String sql, Object... parameters);

    CompletableFuture<List<String>> exeValuesAsync(String sql, Executor executor, Object... parameters);

    CompletableFuture<List<String>> exeValuesAsync(String sql, Object... parameters);


    CompletableFuture<List<TableMap>> exePageAsync(String sql, Integer startIndex, Integer pageSize, Executor executor, Object... parameters);

    CompletableFuture<List<TableMap>> exePageAsync(String sql, Integer startIndex, Integer pageSize, Object... parameters);

    CompletableFuture<List<TableMap>> exePageAsync(String sql, NeoPage neoPage, Executor executor, Object... parameters);

    CompletableFuture<List<TableMap>> exePageAsync(String sql, NeoPage neoPage, Object... parameters);


    CompletableFuture<Integer> exeCountAsync(String sql, Executor executor, Object... parameters);

    CompletableFuture<Integer> exeCountAsync(String sql, Object... parameters);


    CompletableFuture<List<List<TableMap>>> executeAsync(String sql, Executor executor, Object... parameters);

    CompletableFuture<List<List<TableMap>>> executeAsync(String sql, Object... parameters);
}
