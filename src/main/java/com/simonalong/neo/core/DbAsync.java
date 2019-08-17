package com.simonalong.neo.core;

import com.simonalong.neo.Columns;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.NeoMap.NamingChg;
import com.simonalong.neo.table.NeoPage;
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

    <T> CompletableFuture<T> insertAsync(String tableName, T object, NamingChg naming, Executor executor);

    <T> CompletableFuture<T> insertAsync(String tableName, T object, NamingChg naming);


    CompletableFuture<Integer> deleteAsync(String tableName, NeoMap dataMap, Executor executor);

    CompletableFuture<Integer> deleteAsync(String tableName, NeoMap dataMap);

    <T> CompletableFuture<Integer> deleteAsync(String tableName, T object, Executor executor);

    <T> CompletableFuture<Integer> deleteAsync(String tableName, T object);

    <T> CompletableFuture<Integer> deleteAsync(String tableName, T entity, NamingChg naming, Executor executor);

    <T> CompletableFuture<Integer> deleteAsync(String tableName, T entity, NamingChg naming);

    CompletableFuture<Integer> deleteAsync(String tableName, Number id, Executor executor);

    CompletableFuture<Integer> deleteAsync(String tableName, Number id);


    CompletableFuture<NeoMap> updateAsync(String tableName, NeoMap dataMap, NeoMap searchMap, Executor executor);

    CompletableFuture<NeoMap> updateAsync(String tableName, NeoMap dataMap, NeoMap searchMap);

    <T> CompletableFuture<T> updateAsync(String tableName, T setEntity, NeoMap searchMap, NamingChg namingChg, Executor executor);

    <T> CompletableFuture<T> updateAsync(String tableName, T setEntity, NeoMap searchMap, NamingChg namingChg);

    <T> CompletableFuture<T> updateAsync(String tableName, T setEntity, NeoMap searchMap, Executor executor);

    <T> CompletableFuture<T> updateAsync(String tableName, T setEntity, NeoMap searchMap);

    <T> CompletableFuture<T> updateAsync(String tableName, T setEntity, T searchEntity, Executor executor);

    <T> CompletableFuture<T> updateAsync(String tableName, T setEntity, T searchEntity);

    CompletableFuture<NeoMap> updateAsync(String tableName, NeoMap dataMap, Columns columns, Executor executor);

    CompletableFuture<NeoMap> updateAsync(String tableName, NeoMap dataMap, Columns columns);

    <T> CompletableFuture<T> updateAsync(String tableName, T entity, Columns columns, NamingChg namingChg, Executor executor);

    <T> CompletableFuture<T> updateAsync(String tableName, T entity, Columns columns, NamingChg namingChg);

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


    CompletableFuture<List<NeoMap>> listAsync(String tableName, Columns columns, NeoMap searchMap, Executor executor);

    CompletableFuture<List<NeoMap>> listAsync(String tableName, Columns columns, NeoMap searchMap);

    <T> CompletableFuture<List<T>> listAsync(String tableName, Columns columns, T entity, Executor executor);

    <T> CompletableFuture<List<T>> listAsync(String tableName, Columns columns, T entity);

    CompletableFuture<List<NeoMap>> listAsync(String tableName, NeoMap searchMap, Executor executor);

    CompletableFuture<List<NeoMap>> listAsync(String tableName, NeoMap searchMap);

    <T> CompletableFuture<List<T>> listAsync(String tableName, T entity, Executor executor);

    <T> CompletableFuture<List<T>> listAsync(String tableName, T entity);


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

    CompletableFuture<List<NeoMap>> pageAsync(String tableName, Columns columns, NeoMap searchMap, Executor executor);

    CompletableFuture<List<NeoMap>> pageAsync(String tableName, Columns columns, NeoMap searchMap);

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

    CompletableFuture<List<NeoMap>> pageAsync(String tableName, NeoMap searchMap, Executor executor);

    CompletableFuture<List<NeoMap>> pageAsync(String tableName, NeoMap searchMap);


    CompletableFuture<Integer> countAsync(String tableName, NeoMap searchMap, Executor executor);

    CompletableFuture<Integer> countAsync(String tableName, NeoMap searchMap);

    CompletableFuture<Integer> countAsync(String tableName, Object entity, Executor executor);

    CompletableFuture<Integer> countAsync(String tableName, Object entity);

    CompletableFuture<Integer> countAsync(String tableName, Executor executor);

    CompletableFuture<Integer> countAsync(String tableName);
}
