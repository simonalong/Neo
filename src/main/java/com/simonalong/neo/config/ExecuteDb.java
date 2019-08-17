package com.simonalong.neo.config;

import com.simonalong.neo.NeoMap;
import com.simonalong.neo.table.NeoPage;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author zhouzhenyong
 * @since 2019-08-17 17:17
 */
public interface ExecuteDb extends DbSync{

    NeoMap exeOne(String sql, Object... parameters);

    <T> T exeOne(Class<T> tClass, String sql, Object... parameters);

    CompletableFuture<NeoMap> exeOneAsync(String sql, Executor executor, Object... parameters);

    <T> CompletableFuture<T> exeOneAsync(Class<T> tClass, String sql, Executor executor, Object... parameters);

    List<NeoMap> exeList(String sql, Object... parameters);

    <T> List<T> exeList(Class<T> tClass, String sql, Object... parameters);

    CompletableFuture<List<NeoMap>> exeListAsync(String sql, Executor executor, Object... parameters);

    <T> CompletableFuture<List<T>> exeListAsync(Class<T> tClass, String sql, Executor executor, Object... parameters);

    <T> T exeValue(Class<T> tClass, String sql, Object... parameters);

    String exeValue(String sql, Object... parameters);

    <T> CompletableFuture<T> exeValueAsync(Class<T> tClass, String sql, Executor executor, Object... parameters);

    CompletableFuture<String> exeValueAsync(String sql, Executor executor, Object... parameters);

    <T> List<T> exeValues(Class<T> tClass, String sql, Object... parameters);

    List<String> exeValues(String sql, Object... parameters);

    <T> CompletableFuture<List<T>> exeValuesAsync(Class<T> tClass, String sql, Executor executor, Object... parameters);

    CompletableFuture<List<String>> exeValuesAsync(String sql, Executor executor, Object... parameters);

    List<NeoMap> exePage(String sql, Integer startIndex, Integer pageSize, Object... parameters);

    List<NeoMap> exePage(String sql, NeoPage neoPage, Object... parameters);

    CompletableFuture<List<NeoMap>> exePageAsync(String sql, Integer startIndex, Integer pageSize, Executor executor, Object... parameters);

    CompletableFuture<List<NeoMap>> exePageAsync(String sql, NeoPage neoPage, Executor executor, Object... parameters);

    Integer exeCount(String sql, Object... parameters);

    CompletableFuture<Integer> exeCountAsync(String sql, Executor executor, Object... parameters);

    List<List<NeoMap>> execute(String sql, Object... parameters);

    CompletableFuture<List<List<NeoMap>>> executeAsync(String sql, Executor executor, Object... parameters);
}
