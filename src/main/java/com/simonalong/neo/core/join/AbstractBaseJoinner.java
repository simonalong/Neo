package com.simonalong.neo.core.join;

import com.simonalong.neo.Columns;
import com.simonalong.neo.TableMap;
import com.simonalong.neo.core.DefaultExecutor;
import com.simonalong.neo.db.PageRsp;
import com.simonalong.neo.db.TableJoinOn;
import com.simonalong.neo.db.NeoPage;
import com.simonalong.neo.express.SearchQuery;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author shizi
 * @since 2020/5/23 6:49 PM
 */
public abstract class AbstractBaseJoinner extends AbstractJoinnerAsync implements JoinnerSync {

    @Override
    public Executor getExecutor() {
        return DefaultExecutor.getInstance().getExecutor();
    }

    @Override
    public CompletableFuture<TableMap> oneAsync(Columns columns, TableJoinOn tableJoinOn, TableMap searchMap, Executor executor) {
        return CompletableFuture.supplyAsync(() -> one(columns, tableJoinOn, searchMap), executor);
    }

    @Override
    public <T> CompletableFuture<T> oneAsync(Class<T> tClass, Columns columns, TableJoinOn tableJoinOn, TableMap searchMap, Executor executor) {
        return CompletableFuture.supplyAsync(() -> one(tClass, columns, tableJoinOn, searchMap), executor);
    }

    @Override
    public CompletableFuture<List<TableMap>> listAsync(Columns columns, TableJoinOn tableJoinOn, TableMap searchMap, Executor executor) {
        return CompletableFuture.supplyAsync(() -> list(columns, tableJoinOn, searchMap), executor);
    }

    @Override
    public <T> CompletableFuture<List<T>> listAsync(Class<T> tClass, Columns columns, TableJoinOn tableJoinOn, TableMap searchMap, Executor executor) {
        return CompletableFuture.supplyAsync(() -> list(tClass, columns, tableJoinOn, searchMap), executor);
    }

    @Override
    public CompletableFuture<String> valueAsync(Columns columns, TableJoinOn tableJoinOn, TableMap searchMap, Executor executor) {
        return CompletableFuture.supplyAsync(() -> value(columns, tableJoinOn, searchMap), executor);
    }

    @Override
    public <T> CompletableFuture<T> valueAsync(Class<T> tClass, Columns columns, TableJoinOn tableJoinOn, TableMap searchMap, Executor executor) {
        return CompletableFuture.supplyAsync(() -> value(tClass, columns, tableJoinOn, searchMap), executor);
    }

    @Override
    public CompletableFuture<List<String>> valuesAsync(Columns columns, TableJoinOn tableJoinOn, TableMap searchMap, Executor executor) {
        return CompletableFuture.supplyAsync(() -> values(columns, tableJoinOn, searchMap), executor);
    }

    @Override
    public <T> CompletableFuture<List<T>> valuesAsync(Class<T> tClass, Columns columns, TableJoinOn tableJoinOn, TableMap searchMap, Executor executor) {
        return CompletableFuture.supplyAsync(() -> values(tClass, columns, tableJoinOn, searchMap), executor);
    }

    @Override
    public <T> CompletableFuture<List<T>> valuesOfDistinctAsync(Class<T> tClass, Columns joinColumns, TableJoinOn tableJoinOn, TableMap tableMap, Executor executor) {
        return CompletableFuture.supplyAsync(() -> valuesOfDistinct(tClass, joinColumns, tableJoinOn, tableMap), executor);
    }

    @Override
    public <T> CompletableFuture<List<T>> valuesOfDistinctAsync(Class<T> tClass, Columns joinColumns, TableJoinOn tableJoinOn, SearchQuery searchQuery, Executor executor) {
        return CompletableFuture.supplyAsync(() -> valuesOfDistinct(tClass, joinColumns, tableJoinOn, searchQuery), executor);
    }


    @Override
    public CompletableFuture<List<String>> valuesOfDistinctAsync(Columns joinColumns, TableJoinOn tableJoinOn, TableMap tableMap, Executor executor) {
        return CompletableFuture.supplyAsync(() -> valuesOfDistinct(joinColumns, tableJoinOn, tableMap), executor);
    }

    @Override
    public CompletableFuture<List<String>> valuesOfDistinctAsync(Columns joinColumns, TableJoinOn tableJoinOn, SearchQuery searchQuery, Executor executor) {
        return CompletableFuture.supplyAsync(() -> valuesOfDistinct(joinColumns, tableJoinOn, searchQuery), executor);
    }


    @Override
    public CompletableFuture<List<TableMap>> pageAsync(Columns columns, TableJoinOn tableJoinOn, TableMap searchMap, NeoPage neoPage, Executor executor) {
        return CompletableFuture.supplyAsync(() -> page(columns, tableJoinOn, searchMap, neoPage), executor);
    }

    @Override
    public <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, Columns columns, TableJoinOn tableJoinOn, TableMap searchMap, NeoPage neoPage,
        Executor executor) {
        return CompletableFuture.supplyAsync(() -> page(tClass, columns, tableJoinOn, searchMap, neoPage), executor);
    }

    @Override
    public CompletableFuture<PageRsp<TableMap>> getPageAsync(Columns joinColumns, TableJoinOn tableJoinOn, TableMap tableMap, NeoPage neoPage, Executor executor) {
        return CompletableFuture.supplyAsync(() -> getPage(joinColumns, tableJoinOn, tableMap, neoPage), executor);
    }

    @Override
    public <T> CompletableFuture<PageRsp<T>> getPageAsync(Class<T> tClass, Columns joinColumns, TableJoinOn tableJoinOn, TableMap tableMap, NeoPage neoPage, Executor executor) {
        return CompletableFuture.supplyAsync(() -> getPage(tClass, joinColumns, tableJoinOn, tableMap, neoPage), executor);
    }

    @Override
    public CompletableFuture<Integer> countAsync(TableJoinOn tableJoinOn, TableMap searchMap, Executor executor) {
        return CompletableFuture.supplyAsync(() -> count(tableJoinOn, searchMap), executor);
    }
}
