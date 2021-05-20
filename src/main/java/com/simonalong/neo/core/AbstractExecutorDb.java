package com.simonalong.neo.core;

import com.simonalong.neo.TableMap;
import com.simonalong.neo.db.NeoPage;
import com.simonalong.neo.db.PageReq;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author shizi
 * @since 2020/6/8 8:22 PM
 */
public abstract class AbstractExecutorDb extends AbstractBaseDb implements ExecuteSql {

    @Override
    public CompletableFuture<TableMap> exeOneAsync(String sql, Object... parameters) {
        return exeOneAsync(sql, getExecutor(), parameters);
    }

    @Override
    public <T> CompletableFuture<T> exeOneAsync(Class<T> tClass, String sql, Object... parameters){
        return exeOneAsync(tClass, sql, getExecutor(), parameters);
    }

    @Override
    public CompletableFuture<List<TableMap>> exeListAsync(String sql, Object... parameters) {
        return exeListAsync(sql, getExecutor(), parameters);
    }

    @Override
    public <T> CompletableFuture<List<T>> exeListAsync(Class<T> tClass, String sql, Object... parameters) {
        return exeListAsync(tClass, sql, getExecutor(), parameters);
    }

    @Override
    public <T> CompletableFuture<T> exeValueAsync(Class<T> tClass, String sql, Object... parameters) {
        return exeValueAsync(tClass, sql, getExecutor(), parameters);
    }

    @Override
    public CompletableFuture<String> exeValueAsync(String sql, Object... parameters) {
        return exeValueAsync(sql, getExecutor(), parameters);
    }

    @Override
    public <T> CompletableFuture<List<T>> exeValuesAsync(Class<T> tClass, String sql, Object... parameters) {
        return exeValuesAsync(tClass, sql, getExecutor(), parameters);
    }

    @Override
    public CompletableFuture<List<String>> exeValuesAsync(String sql, Object... parameters) {
        return exeValuesAsync(sql, getExecutor(), parameters);
    }

    @Override
    public CompletableFuture<List<TableMap>> exePageAsync(String sql, Integer startIndex, Integer pageSize, Object... parameters) {
        return exePageAsync(sql, startIndex, pageSize, getExecutor(), parameters);
    }

    @Override
    @Deprecated
    public CompletableFuture<List<TableMap>> exePageAsync(String sql, NeoPage neoPage, Object... parameters) {
        return exePageAsync(sql, neoPage, getExecutor(), parameters);
    }

    @Override
    public CompletableFuture<List<TableMap>> exePageAsync(String sql, PageReq<?> pageReq, Object... parameters) {
        return exePageAsync(sql, pageReq, getExecutor(), parameters);
    }

    @Override
    public CompletableFuture<Integer> exeCountAsync(String sql, Object... parameters) {
        return exeCountAsync(sql, getExecutor(), parameters);
    }

    @Override
    public CompletableFuture<List<List<TableMap>>> executeAsync(String sql, Object... parameters) {
        return executeAsync(sql, getExecutor(), parameters);
    }
}
