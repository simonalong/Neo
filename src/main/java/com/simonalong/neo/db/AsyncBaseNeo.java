package com.simonalong.neo.db;

import com.simonalong.neo.Columns;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.NeoMap.NamingChg;
import com.simonalong.neo.table.NeoPage;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

/**
 *
 * @author zhouzhenyong
 * @since 2019-08-17 15:36
 */
// todo delte
    dfs
interface AsyncBaseNeo {

    ExecutorService getExecutor();

    CompletableFuture<NeoMap> insertAsync(NeoMap dataMap, Executor executor);

    <T> CompletableFuture<T> insertAsync(T object, Executor executor);

    <T> CompletableFuture<T> insertAsync(T object, NamingChg naming, Executor executor);


    CompletableFuture<Integer> deleteAsync(NeoMap dataMap, Executor executor);

    <T> CompletableFuture<Integer> deleteAsync(T object, Executor executor);

    <T> CompletableFuture<Integer> deleteAsync(T entity, NamingChg naming, Executor executor);

    CompletableFuture<Integer> deleteAsync(Number id, Executor executor);


    CompletableFuture<NeoMap> updateAsync(NeoMap dataMap, NeoMap searchMap, Executor executor);

    <T> CompletableFuture<T> updateAsync(T setEntity, NeoMap searchMap, NamingChg namingChg, Executor executor);

    <T> CompletableFuture<T> updateAsync(T setEntity, NeoMap searchMap, Executor executor);

    <T> CompletableFuture<T> updateAsync(T setEntity, T searchEntity, Executor executor);

    CompletableFuture<NeoMap> updateAsync(NeoMap dataMap, Columns columns, Executor executor);

    <T> CompletableFuture<T> updateAsync(T entity, Columns columns, NamingChg namingChg, Executor executor);

    <T> CompletableFuture<T> updateAsync(T entity, Columns columns, Executor executor);

    CompletableFuture<NeoMap> updateAsync(NeoMap dataMap, Executor executor);

    <T> CompletableFuture<T> updateAsync(T entity, Executor executor);


    CompletableFuture<NeoMap> oneAsync(Columns columns, NeoMap searchMap, Executor executor);

    <T> CompletableFuture<T> oneAsync(Columns columns, T entity, Executor executor);

    CompletableFuture<NeoMap> oneAsync(NeoMap searchMap, Executor executor);

    <T> CompletableFuture<T> oneAsync(T entity, Executor executor);

    CompletableFuture<NeoMap> oneAsync(Number id, Executor executor);


    CompletableFuture<List<NeoMap>> listAsync(Columns columns, NeoMap searchMap, Executor executor);

    <T> CompletableFuture<List<T>> listAsync(Columns columns, T entity, Executor executor);

    CompletableFuture<List<NeoMap>> listAsync(NeoMap searchMap, Executor executor);

    <T> CompletableFuture<List<T>> listAsync(T entity, Executor executor);
    

    <T> CompletableFuture<List<T>> valuesAsync(Class<T> tClass, String field, NeoMap searchMap, Executor executor);

    <T> CompletableFuture<List<T>> valuesAsync(Class<T> tClass, String field, Object entity, Executor executor);

    CompletableFuture<List<String>> valuesAsync(String field, NeoMap searchMap, Executor executor);

    CompletableFuture<List<String>> valuesAsync(String field, Object entity, Executor executor);

    CompletableFuture<List<String>> valuesAsync(String field, Executor executor);


    <T> CompletableFuture<T> valueAsync(Class<T> tClass, String field, NeoMap searchMap, Executor executor);

    <T> CompletableFuture<T> valueAsync(Class<T> tClass, String field, Object entity, Executor executor);

    CompletableFuture<String> valueAsync(String field, NeoMap searchMap, Executor executor);

    CompletableFuture<String> valueAsync(String field, Object entity, Executor executor);


    CompletableFuture<List<NeoMap>> pageAsync(Columns columns, NeoMap searchMap, NeoPage page, Executor executor);

    CompletableFuture<List<NeoMap>> pageAsync(Columns columns, NeoMap searchMap, Executor executor);

    <T> CompletableFuture<List<T>> pageAsync(Columns columns, T entity, NeoPage page, Executor executor);

    CompletableFuture<List<NeoMap>> pageAsync(NeoMap searchMap, NeoPage page, Executor executor);

    <T> CompletableFuture<List<T>> pageAsync(T entity, NeoPage page, Executor executor);

    CompletableFuture<List<NeoMap>> pageAsync(Columns columns, NeoPage page, Executor executor);

    CompletableFuture<List<NeoMap>> pageAsync(NeoPage page, Executor executor);

    CompletableFuture<List<NeoMap>> pageAsync(NeoMap searchMap, Executor executor);


    CompletableFuture<Integer> countAsync(NeoMap searchMap, Executor executor);

    CompletableFuture<Integer> countAsync(Object entity, Executor executor);

    CompletableFuture<Integer> countAsync(Executor executor);
}
