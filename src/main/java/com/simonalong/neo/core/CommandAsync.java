package com.simonalong.neo.core;

import com.simonalong.neo.Columns;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.express.SearchExpress;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author shizi
 * @since 2020/6/11 3:02 PM
 */
public interface CommandAsync extends Async {

    CompletableFuture<NeoMap> insertAsync(String tableName, NeoMap dataMap, Executor executor);

    CompletableFuture<NeoMap> insertAsync(String tableName, NeoMap dataMap);

    <T> CompletableFuture<T> insertAsync(String tableName, T object, Executor executor);

    <T> CompletableFuture<T> insertAsync(String tableName, T object);


    CompletableFuture<NeoMap> insertOfUnExistAsync(String tableName, NeoMap dataMap, Executor executor, String... searchColumnKey);

    CompletableFuture<NeoMap> insertOfUnExistAsync(String tableName, NeoMap dataMap, String... searchColumnKey);

    <T> CompletableFuture<T> insertOfUnExistAsync(String tableName, T object, Executor executor, String... searchColumnKey);

    <T> CompletableFuture<T> insertOfUnExistAsync(String tableName, T object, String... searchColumnKey);


    CompletableFuture<NeoMap> saveAsync(String tableName, NeoMap dataMap, Executor executor, String... searchColumnKey);

    CompletableFuture<NeoMap> saveAsync(String tableName, NeoMap dataMap, String... searchColumnKey);

    <T> CompletableFuture<T> saveAsync(String tableName, T object, Executor executor, String... searchColumnKey);

    <T> CompletableFuture<T> saveAsync(String tableName, T object, String... searchColumnKey);


    CompletableFuture<Integer> deleteAsync(String tableName, NeoMap dataMap, Executor executor);

    CompletableFuture<Integer> deleteAsync(String tableName, NeoMap dataMap);

    CompletableFuture<Integer> deleteAsync(String tableName, SearchExpress searchExpress, Executor executor);

    CompletableFuture<Integer> deleteAsync(String tableName, SearchExpress searchExpress);

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

    CompletableFuture<NeoMap> updateAsync(String tableName, NeoMap dataMap, Number id, Executor executor);

    CompletableFuture<NeoMap> updateAsync(String tableName, NeoMap dataMap, Number id);

    <T> CompletableFuture<T> updateAsync(String tableName, T setEntity, Number id, Executor executor);

    <T> CompletableFuture<T> updateAsync(String tableName, T setEntity, Number id);

    CompletableFuture<NeoMap> updateAsync(String tableName, NeoMap dataMap, Columns columns, Executor executor);

    CompletableFuture<NeoMap> updateAsync(String tableName, NeoMap dataMap, Columns columns);

    <T> CompletableFuture<T> updateAsync(String tableName, T entity, Columns columns, Executor executor);

    <T> CompletableFuture<T> updateAsync(String tableName, T entity, Columns columns);

    CompletableFuture<NeoMap> updateAsync(String tableName, NeoMap dataMap, Executor executor);

    CompletableFuture<NeoMap> updateAsync(String tableName, NeoMap dataMap);

    <T> CompletableFuture<T> updateAsync(String tableName, T entity, Executor executor);

    <T> CompletableFuture<T> updateAsync(String tableName, T entity);


    CompletableFuture<NeoMap> updateAsync(String tableName, NeoMap dataMap, SearchExpress searchExpress, Executor executor);

    CompletableFuture<NeoMap> updateAsync(String tableName, NeoMap dataMap, SearchExpress searchExpress);

    <T> CompletableFuture<T> updateAsync(String tableName, T setEntity, SearchExpress searchExpress, Executor executor);

    <T> CompletableFuture<T> updateAsync(String tableName, T setEntity, SearchExpress searchExpress);


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
}
