package com.simonalong.neo.core.join;

import com.simonalong.neo.Columns;
import com.simonalong.neo.TableMap;
import com.simonalong.neo.core.Async;
import com.simonalong.neo.db.PageRsp;
import com.simonalong.neo.db.TableJoinOn;
import com.simonalong.neo.db.NeoPage;
import com.simonalong.neo.express.SearchExpress;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author shizi
 * @since 2020/5/23 6:32 PM
 */
public interface JoinnerAsync extends Async {

    CompletableFuture<TableMap> oneAsync(Columns joinColumns, TableJoinOn tableJoinOn, TableMap tableMap, Executor executor);

    CompletableFuture<TableMap> oneAsync(Columns joinColumns, TableJoinOn tableJoinOn, TableMap tableMap);

    <T> CompletableFuture<T> oneAsync(Class<T> tClass, Columns joinColumns, TableJoinOn tableJoinOn, TableMap tableMap, Executor executor);

    <T> CompletableFuture<T> oneAsync(Class<T> tClass, Columns joinColumns, TableJoinOn tableJoinOn, TableMap tableMap);


    CompletableFuture<List<TableMap>> listAsync(Columns joinColumns, TableJoinOn tableJoinOn, TableMap tableMap, Executor executor);

    CompletableFuture<List<TableMap>> listAsync(Columns joinColumns, TableJoinOn tableJoinOn, TableMap tableMap);

    <T> CompletableFuture<List<T>> listAsync(Class<T> tClass, Columns joinColumns, TableJoinOn tableJoinOn, TableMap tableMap, Executor executor);

    <T> CompletableFuture<List<T>> listAsync(Class<T> tClass, Columns joinColumns, TableJoinOn tableJoinOn, TableMap tableMap);


    CompletableFuture<String> valueAsync(Columns joinColumns, TableJoinOn tableJoinOn, TableMap tableMap, Executor executor);

    CompletableFuture<String> valueAsync(Columns joinColumns, TableJoinOn tableJoinOn, TableMap tableMap);

    <T> CompletableFuture<T> valueAsync(Class<T> tClass, Columns joinColumns, TableJoinOn tableJoinOn, TableMap tableMap, Executor executor);

    <T> CompletableFuture<T> valueAsync(Class<T> tClass, Columns joinColumns, TableJoinOn tableJoinOn, TableMap tableMap);


    CompletableFuture<List<String>> valuesAsync(Columns joinColumns, TableJoinOn tableJoinOn, TableMap tableMap, Executor executor);

    CompletableFuture<List<String>> valuesAsync(Columns joinColumns, TableJoinOn tableJoinOn, TableMap tableMap);

    <T> CompletableFuture<List<T>> valuesAsync(Class<T> tClass, Columns joinColumns, TableJoinOn tableJoinOn, TableMap tableMap, Executor executor);

    <T> CompletableFuture<List<T>> valuesAsync(Class<T> tClass, Columns joinColumns, TableJoinOn tableJoinOn, TableMap tableMap);


    <T> CompletableFuture<List<T>> valuesOfDistinctAsync(Class<T> tClass, Columns joinColumns, TableJoinOn tableJoinOn, TableMap tableMap, Executor executor);

    <T> CompletableFuture<List<T>> valuesOfDistinctAsync(Class<T> tClass, Columns joinColumns, TableJoinOn tableJoinOn, TableMap tableMap);

    <T> CompletableFuture<List<T>> valuesOfDistinctAsync(Class<T> tClass, Columns joinColumns, TableJoinOn tableJoinOn, SearchExpress searchExpress, Executor executor);

    <T> CompletableFuture<List<T>> valuesOfDistinctAsync(Class<T> tClass, Columns joinColumns, TableJoinOn tableJoinOn, SearchExpress searchExpress);

    CompletableFuture<List<String>> valuesOfDistinctAsync(Columns joinColumns, TableJoinOn tableJoinOn, TableMap tableMap, Executor executor);

    CompletableFuture<List<String>> valuesOfDistinctAsync(Columns joinColumns, TableJoinOn tableJoinOn, TableMap tableMap);

    CompletableFuture<List<String>> valuesOfDistinctAsync(Columns joinColumns, TableJoinOn tableJoinOn, SearchExpress searchExpress, Executor executor);

    CompletableFuture<List<String>> valuesOfDistinctAsync(Columns joinColumns, TableJoinOn tableJoinOn, SearchExpress searchExpress);


    CompletableFuture<List<TableMap>> pageAsync(Columns joinColumns, TableJoinOn tableJoinOn, TableMap tableMap, NeoPage neoPage, Executor executor);

    CompletableFuture<List<TableMap>> pageAsync(Columns joinColumns, TableJoinOn tableJoinOn, TableMap tableMap, NeoPage neoPage);

    <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, Columns joinColumns, TableJoinOn tableJoinOn, TableMap tableMap, NeoPage neoPage, Executor executor);

    <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, Columns joinColumns, TableJoinOn tableJoinOn, TableMap tableMap, NeoPage neoPage);


    CompletableFuture<PageRsp<TableMap>> getPageAsync(Columns joinColumns, TableJoinOn tableJoinOn, TableMap tableMap, NeoPage neoPage, Executor executor);

    CompletableFuture<PageRsp<TableMap>> getPageAsync(Columns joinColumns, TableJoinOn tableJoinOn, TableMap tableMap, NeoPage neoPage);

    <T> CompletableFuture<PageRsp<T>> getPageAsync(Class<T> tClass, Columns joinColumns, TableJoinOn tableJoinOn, TableMap tableMap, NeoPage neoPage, Executor executor);

    <T> CompletableFuture<PageRsp<T>> getPageAsync(Class<T> tClass, Columns joinColumns, TableJoinOn tableJoinOn, TableMap tableMap, NeoPage neoPage);


    CompletableFuture<Integer> countAsync(TableJoinOn tableJoinOn, TableMap tableMap, Executor executor);

    CompletableFuture<Integer> countAsync(TableJoinOn tableJoinOn, TableMap tableMap);
}
