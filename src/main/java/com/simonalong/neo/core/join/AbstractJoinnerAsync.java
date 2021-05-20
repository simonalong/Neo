package com.simonalong.neo.core.join;

import com.simonalong.neo.Columns;
import com.simonalong.neo.TableMap;
import com.simonalong.neo.db.PageReq;
import com.simonalong.neo.db.PageRsp;
import com.simonalong.neo.db.TableJoinOn;
import com.simonalong.neo.db.NeoPage;
import com.simonalong.neo.express.SearchQuery;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author shizi
 * @since 2020/5/23 6:42 PM
 */
public abstract class AbstractJoinnerAsync implements JoinnerAsync {

    @Override
    public CompletableFuture<TableMap> oneAsync(Columns columns, TableJoinOn tableJoinOn, TableMap searchMap) {
        return oneAsync(columns, tableJoinOn, searchMap, getExecutor());
    }

    @Override
    public <T> CompletableFuture<T> oneAsync(Class<T> tClass, Columns columns, TableJoinOn tableJoinOn, TableMap searchMap) {
        return oneAsync(tClass, columns, tableJoinOn, searchMap, getExecutor());
    }

    @Override
    public CompletableFuture<List<TableMap>> listAsync(Columns columns, TableJoinOn tableJoinOn, TableMap searchMap) {
        return listAsync(columns, tableJoinOn, searchMap, getExecutor());
    }

    @Override
    public <T> CompletableFuture<List<T>> listAsync(Class<T> tClass, Columns columns, TableJoinOn tableJoinOn, TableMap searchMap) {
        return listAsync(tClass, columns, tableJoinOn, searchMap, getExecutor());
    }

    @Override
    public CompletableFuture<String> valueAsync(Columns columns, TableJoinOn tableJoinOn, TableMap searchMap) {
        return valueAsync(columns, tableJoinOn, searchMap, getExecutor());
    }

    @Override
    public <T> CompletableFuture<T> valueAsync(Class<T> tClass, Columns columns, TableJoinOn tableJoinOn, TableMap searchMap) {
        return valueAsync(tClass, columns, tableJoinOn, searchMap, getExecutor());
    }

    @Override
    public CompletableFuture<List<String>> valuesAsync(Columns columns, TableJoinOn tableJoinOn, TableMap searchMap) {
        return valuesAsync(columns, tableJoinOn, searchMap, getExecutor());
    }

    @Override
    public <T> CompletableFuture<List<T>> valuesAsync(Class<T> tClass, Columns columns, TableJoinOn tableJoinOn, TableMap searchMap) {
        return valuesAsync(tClass, columns, tableJoinOn, searchMap, getExecutor());
    }

    @Override
    public <T> CompletableFuture<List<T>> valuesOfDistinctAsync(Class<T> tClass, Columns joinColumns, TableJoinOn tableJoinOn, TableMap tableMap) {
        return valuesOfDistinctAsync(tClass, joinColumns, tableJoinOn, tableMap, getExecutor());
    }

    @Override
    public <T> CompletableFuture<List<T>> valuesOfDistinctAsync(Class<T> tClass, Columns joinColumns, TableJoinOn tableJoinOn, SearchQuery searchQuery) {
        return valuesOfDistinctAsync(tClass, joinColumns, tableJoinOn, searchQuery, getExecutor());
    }

    @Override
    public CompletableFuture<List<String>> valuesOfDistinctAsync(Columns joinColumns, TableJoinOn tableJoinOn, TableMap tableMap) {
        return valuesOfDistinctAsync(joinColumns, tableJoinOn, tableMap, getExecutor());
    }

    @Override
    public CompletableFuture<List<String>> valuesOfDistinctAsync(Columns joinColumns, TableJoinOn tableJoinOn, SearchQuery searchQuery) {
        return valuesOfDistinctAsync(joinColumns, tableJoinOn, searchQuery, getExecutor());
    }


    @Override
    @Deprecated
    public CompletableFuture<List<TableMap>> pageAsync(Columns columns, TableJoinOn tableJoinOn, TableMap searchMap, NeoPage neoPage) {
        return pageAsync(columns, tableJoinOn, searchMap, neoPage, getExecutor());
    }

    @Override
    @Deprecated
    public <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, Columns columns, TableJoinOn tableJoinOn, TableMap searchMap, NeoPage neoPage) {
        return pageAsync(tClass, columns, tableJoinOn, searchMap, neoPage, getExecutor());
    }

    @Override
    @Deprecated
    public CompletableFuture<PageRsp<TableMap>> getPageAsync(Columns joinColumns, TableJoinOn tableJoinOn, TableMap tableMap, NeoPage neoPage) {
        return getPageAsync(joinColumns, tableJoinOn, tableMap, neoPage, getExecutor());
    }

    @Override
    @Deprecated
    public <T> CompletableFuture<PageRsp<T>> getPageAsync(Class<T> tClass, Columns joinColumns, TableJoinOn tableJoinOn, TableMap tableMap, NeoPage neoPage) {
        return getPageAsync(tClass, joinColumns, tableJoinOn, tableMap, neoPage, getExecutor());
    }


    @Override
    public CompletableFuture<List<TableMap>> pageAsync(Columns columns, TableJoinOn tableJoinOn, TableMap searchMap, PageReq<?> pageReq) {
        return pageAsync(columns, tableJoinOn, searchMap, pageReq, getExecutor());
    }

    @Override
    public <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, Columns columns, TableJoinOn tableJoinOn, TableMap searchMap, PageReq<?> pageReq) {
        return pageAsync(tClass, columns, tableJoinOn, searchMap, pageReq, getExecutor());
    }

    @Override
    public CompletableFuture<PageRsp<TableMap>> getPageAsync(Columns joinColumns, TableJoinOn tableJoinOn, TableMap tableMap, PageReq<?> pageReq) {
        return getPageAsync(joinColumns, tableJoinOn, tableMap, pageReq, getExecutor());
    }

    @Override
    public <T> CompletableFuture<PageRsp<T>> getPageAsync(Class<T> tClass, Columns joinColumns, TableJoinOn tableJoinOn, TableMap tableMap, PageReq<?> pageReq) {
        return getPageAsync(tClass, joinColumns, tableJoinOn, tableMap, pageReq, getExecutor());
    }

    @Override
    public CompletableFuture<Integer> countAsync(TableJoinOn tableJoinOn, TableMap searchMap) {
        return countAsync(tableJoinOn, searchMap, getExecutor());
    }
}
