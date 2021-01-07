package com.simonalong.neo.core;

import com.simonalong.neo.Columns;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.db.NeoPage;
import com.simonalong.neo.express.Express;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author shizi
 * @since 2020/6/11 3:02 PM
 */
public interface QueryAsync extends Async {

    CompletableFuture<NeoMap> oneAsync(String tableName, Columns columns, NeoMap searchMap, Executor executor);

    CompletableFuture<NeoMap> oneAsync(String tableName, Columns columns, NeoMap searchMap);

    CompletableFuture<NeoMap> oneAsync(String tableName, Columns columns, Express searchExpress, Executor executor);

    CompletableFuture<NeoMap> oneAsync(String tableName, Columns columns, Express searchExpress);

    <T> CompletableFuture<T> oneAsync(String tableName, Columns columns, T entity, Executor executor);

    <T> CompletableFuture<T> oneAsync(String tableName, Columns columns, T entity);

    CompletableFuture<NeoMap> oneAsync(String tableName, NeoMap searchMap, Executor executor);

    CompletableFuture<NeoMap> oneAsync(String tableName, NeoMap searchMap);

    CompletableFuture<NeoMap> oneAsync(String tableName, Express searchExpress, Executor executor);

    CompletableFuture<NeoMap> oneAsync(String tableName, Express searchExpress);

    <T> CompletableFuture<T> oneAsync(String tableName, T entity, Executor executor);

    <T> CompletableFuture<T> oneAsync(String tableName, T entity);

    CompletableFuture<NeoMap> oneAsync(String tableName, Number id, Executor executor);

    CompletableFuture<NeoMap> oneAsync(String tableName, Number id);

    <T> CompletableFuture<T> oneAsync(Class<T> tClass, String tableName, Columns columns, NeoMap searchMap, Executor executor);

    <T> CompletableFuture<T> oneAsync(Class<T> tClass, String tableName, Columns columns, NeoMap searchMap);

    <T> CompletableFuture<T> oneAsync(Class<T> tClass, String tableName, Columns columns, Express searchExpress, Executor executor);

    <T> CompletableFuture<T> oneAsync(Class<T> tClass, String tableName, Columns columns, Express searchExpress);

    <T> CompletableFuture<T> oneAsync(Class<T> tClass, String tableName, NeoMap searchMap, Executor executor);

    <T> CompletableFuture<T> oneAsync(Class<T> tClass, String tableName, NeoMap searchMap);

    <T> CompletableFuture<T> oneAsync(Class<T> tClass, String tableName, Express searchExpress, Executor executor);

    <T> CompletableFuture<T> oneAsync(Class<T> tClass, String tableName, Express searchExpress);

    <T> CompletableFuture<T> oneAsync(Class<T> tClass, String tableName, Number id, Executor executor);

    <T> CompletableFuture<T> oneAsync(Class<T> tClass, String tableName, Number id);


    CompletableFuture<List<NeoMap>> listAsync(String tableName, Columns columns, NeoMap searchMap, Executor executor);

    CompletableFuture<List<NeoMap>> listAsync(String tableName, Columns columns, NeoMap searchMap);

    CompletableFuture<List<NeoMap>> listAsync(String tableName, Columns columns, Express searchExpress, Executor executor);

    CompletableFuture<List<NeoMap>> listAsync(String tableName, Columns columns, Express searchExpress);

    <T> CompletableFuture<List<T>> listAsync(String tableName, Columns columns, T entity, Executor executor);

    <T> CompletableFuture<List<T>> listAsync(String tableName, Columns columns, T entity);

    CompletableFuture<List<NeoMap>> listAsync(String tableName, NeoMap searchMap, Executor executor);

    CompletableFuture<List<NeoMap>> listAsync(String tableName, NeoMap searchMap);

    CompletableFuture<List<NeoMap>> listAsync(String tableName, Express searchExpress, Executor executor);

    CompletableFuture<List<NeoMap>> listAsync(String tableName, Express searchExpress);

    <T> CompletableFuture<List<T>> listAsync(String tableName, T entity, Executor executor);

    <T> CompletableFuture<List<T>> listAsync(String tableName, T entity);

    <T> CompletableFuture<List<T>> listAsync(Class<T> tClass, String tableName, Columns columns, NeoMap searchMap, Executor executor);

    <T> CompletableFuture<List<T>> listAsync(Class<T> tClass, String tableName, Columns columns, NeoMap searchMap);

    <T> CompletableFuture<List<T>> listAsync(Class<T> tClass, String tableName, Columns columns, Express searchExpress, Executor executor);

    <T> CompletableFuture<List<T>> listAsync(Class<T> tClass, String tableName, Columns columns, Express searchExpress);

    <T> CompletableFuture<List<T>> listAsync(Class<T> tClass, String tableName, NeoMap searchMap, Executor executor);

    <T> CompletableFuture<List<T>> listAsync(Class<T> tClass, String tableName, NeoMap searchMap);

    <T> CompletableFuture<List<T>> listAsync(Class<T> tClass, String tableName, Express searchExpress, Executor executor);

    <T> CompletableFuture<List<T>> listAsync(Class<T> tClass, String tableName, Express searchExpress);


    <T> CompletableFuture<T> valueAsync(String tableName, Class<T> tClass, String field, NeoMap searchMap, Executor executor);

    <T> CompletableFuture<T> valueAsync(String tableName, Class<T> tClass, String field, NeoMap searchMap);

    <T> CompletableFuture<T> valueAsync(String tableName, Class<T> tClass, String field, Express searchExpress, Executor executor);

    <T> CompletableFuture<T> valueAsync(String tableName, Class<T> tClass, String field, Express searchExpress);

    <T> CompletableFuture<T> valueAsync(String tableName, Class<T> tClass, String field, Object entity, Executor executor);

    <T> CompletableFuture<T> valueAsync(String tableName, Class<T> tClass, String field, Object entity);

    CompletableFuture<String> valueAsync(String tableName, String field, NeoMap searchMap, Executor executor);

    CompletableFuture<String> valueAsync(String tableName, String field, NeoMap searchMap);

    CompletableFuture<String> valueAsync(String tableName, String field, Express searchExpress, Executor executor);

    CompletableFuture<String> valueAsync(String tableName, String field, Express searchExpress);

    CompletableFuture<String> valueAsync(String tableName, String field, Object entity, Executor executor);

    CompletableFuture<String> valueAsync(String tableName, String field, Object entity);


    <T> CompletableFuture<List<T>> valuesAsync(String tableName, Class<T> tClass, String field, NeoMap searchMap, Executor executor);

    <T> CompletableFuture<List<T>> valuesAsync(String tableName, Class<T> tClass, String field, NeoMap searchMap);

    <T> CompletableFuture<List<T>> valuesAsync(String tableName, Class<T> tClass, String field, Express searchExpress, Executor executor);

    <T> CompletableFuture<List<T>> valuesAsync(String tableName, Class<T> tClass, String field, Express searchExpress);

    <T> CompletableFuture<List<T>> valuesAsync(String tableName, Class<T> tClass, String field, Object entity, Executor executor);

    <T> CompletableFuture<List<T>> valuesAsync(String tableName, Class<T> tClass, String field, Object entity);

    CompletableFuture<List<String>> valuesAsync(String tableName, String field, NeoMap searchMap, Executor executor);

    CompletableFuture<List<String>> valuesAsync(String tableName, String field, NeoMap searchMap);

    CompletableFuture<List<String>> valuesAsync(String tableName, String field, Express searchExpress, Executor executor);

    CompletableFuture<List<String>> valuesAsync(String tableName, String field, Express searchExpress);

    CompletableFuture<List<String>> valuesAsync(String tableName, String field, Object entity, Executor executor);

    CompletableFuture<List<String>> valuesAsync(String tableName, String field, Object entity);

    CompletableFuture<List<String>> valuesAsync(String tableName, String field, Executor executor);

    CompletableFuture<List<String>> valuesAsync(String tableName, String field);


    <T> CompletableFuture<List<T>> valuesOfDistinctAsync(String tableName, Class<T> tClass, String field, NeoMap searchMap, Executor executor);

    <T> CompletableFuture<List<T>> valuesOfDistinctAsync(String tableName, Class<T> tClass, String field, NeoMap searchMap);

    <T> CompletableFuture<List<T>> valuesOfDistinctAsync(String tableName, Class<T> tClass, String field, Express searchExpress, Executor executor);

    <T> CompletableFuture<List<T>> valuesOfDistinctAsync(String tableName, Class<T> tClass, String field, Express searchExpress);

    <T> CompletableFuture<List<T>> valuesOfDistinctAsync(String tableName, Class<T> tClass, String field, Object entity, Executor executor);

    <T> CompletableFuture<List<T>> valuesOfDistinctAsync(String tableName, Class<T> tClass, String field, Object entity);

    CompletableFuture<List<String>> valuesOfDistinctAsync(String tableName, String field, NeoMap searchMap, Executor executor);

    CompletableFuture<List<String>> valuesOfDistinctAsync(String tableName, String field, NeoMap searchMap);

    CompletableFuture<List<String>> valuesOfDistinctAsync(String tableName, String field, Express searchExpress, Executor executor);

    CompletableFuture<List<String>> valuesOfDistinctAsync(String tableName, String field, Express searchExpress);

    CompletableFuture<List<String>> valuesOfDistinctAsync(String tableName, String field, Object entity, Executor executor);

    CompletableFuture<List<String>> valuesOfDistinctAsync(String tableName, String field, Object entity);

    CompletableFuture<List<String>> valuesOfDistinctAsync(String tableName, String field, Executor executor);

    CompletableFuture<List<String>> valuesOfDistinctAsync(String tableName, String field);


    CompletableFuture<List<NeoMap>> pageAsync(String tableName, Columns columns, NeoMap searchMap, NeoPage page, Executor executor);

    CompletableFuture<List<NeoMap>> pageAsync(String tableName, Columns columns, NeoMap searchMap, NeoPage page);

    CompletableFuture<List<NeoMap>> pageAsync(String tableName, Columns columns, Express searchExpress, NeoPage page, Executor executor);

    CompletableFuture<List<NeoMap>> pageAsync(String tableName, Columns columns, Express searchExpress, NeoPage page);

    <T> CompletableFuture<List<T>> pageAsync(String tableName, Columns columns, T entity, NeoPage page, Executor executor);

    <T> CompletableFuture<List<T>> pageAsync(String tableName, Columns columns, T entity, NeoPage page);

    CompletableFuture<List<NeoMap>> pageAsync(String tableName, NeoMap searchMap, NeoPage page, Executor executor);

    CompletableFuture<List<NeoMap>> pageAsync(String tableName, NeoMap searchMap, NeoPage page);

    CompletableFuture<List<NeoMap>> pageAsync(String tableName, Express searchExpress, NeoPage page, Executor executor);

    CompletableFuture<List<NeoMap>> pageAsync(String tableName, Express searchExpress, NeoPage page);

    <T> CompletableFuture<List<T>> pageAsync(String tableName, T entity, NeoPage page, Executor executor);

    <T> CompletableFuture<List<T>> pageAsync(String tableName, T entity, NeoPage page);

    CompletableFuture<List<NeoMap>> pageAsync(String tableName, Columns columns, NeoPage page, Executor executor);

    CompletableFuture<List<NeoMap>> pageAsync(String tableName, Columns columns, NeoPage page);

    CompletableFuture<List<NeoMap>> pageAsync(String tableName, NeoPage page, Executor executor);

    CompletableFuture<List<NeoMap>> pageAsync(String tableName, NeoPage page);

    <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, Columns columns, NeoMap searchMap, NeoPage page, Executor executor);

    <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, Columns columns, NeoMap searchMap, NeoPage page);

    <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, Columns columns, Express searchExpress, NeoPage page, Executor executor);

    <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, Columns columns, Express searchExpress, NeoPage page);

    <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, NeoMap searchMap, NeoPage page, Executor executor);

    <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, NeoMap searchMap, NeoPage page);

    <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, Express searchExpress, NeoPage page, Executor executor);

    <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, Express searchExpress, NeoPage page);

    <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, Columns columns, NeoPage page, Executor executor);

    <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, Columns columns, NeoPage page);

    <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, NeoPage page, Executor executor);

    <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, NeoPage page);


    CompletableFuture<Integer> countAsync(String tableName, NeoMap searchMap, Executor executor);

    CompletableFuture<Integer> countAsync(String tableName, NeoMap searchMap);

    CompletableFuture<Integer> countAsync(String tableName, Express searchExpress, Executor executor);

    CompletableFuture<Integer> countAsync(String tableName, Express searchExpress);

    CompletableFuture<Integer> countAsync(String tableName, Object entity, Executor executor);

    CompletableFuture<Integer> countAsync(String tableName, Object entity);

    CompletableFuture<Integer> countAsync(String tableName, Executor executor);

    CompletableFuture<Integer> countAsync(String tableName);


    CompletableFuture<Boolean> existAsync(String tableName, NeoMap searchMap, Executor executor);

    CompletableFuture<Boolean> existAsync(String tableName, NeoMap searchMap);

    CompletableFuture<Boolean> existAsync(String tableName, Express searchExpress, Executor executor);

    CompletableFuture<Boolean> existAsync(String tableName, Express searchExpress);

    CompletableFuture<Boolean> existAsync(String tableName, Object entity, Executor executor);

    CompletableFuture<Boolean> existAsync(String tableName, Object entity);

    CompletableFuture<Boolean> existAsync(String tableName, Number id, Executor executor);

    CompletableFuture<Boolean> existAsync(String tableName, Number id);
}
