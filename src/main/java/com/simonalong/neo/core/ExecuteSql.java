package com.simonalong.neo.core;

import com.simonalong.neo.TableMap;
import com.simonalong.neo.db.NeoPage;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author shizi
 * @since 2020/4/4 11:55 PM
 */
public interface ExecuteSql {


    TableMap exeOne(String sql, Object... parameters);

    <T> T exeOne(Class<T> tClass, String sql, Object... parameters);

    List<TableMap> exeList(String sql, Object... parameters);

    <T> List<T> exeList(Class<T> tClass, String sql, Object... parameters);

    <T> T exeValue(Class<T> tClass, String sql, Object... parameters);

    String exeValue(String sql, Object... parameters);

    <T> List<T> exeValues(Class<T> tClass, String sql, Object... parameters);

    List<String> exeValues(String sql, Object... parameters);

    List<TableMap> exePage(String sql, Integer startIndex, Integer pageSize, Object... parameters);

    <T> List<T> exePage(Class<T> tClass, String sql, Integer startIndex, Integer pageSize, Object... parameters);

    List<TableMap> exePage(String sql, NeoPage neoPage, Object... parameters);

    <T> List<T> exePage(Class<T> tClass, String sql, NeoPage neoPage, Object... parameters);

    Integer exeCount(String sql, Object... parameters);

    List<List<TableMap>> execute(String sql, Object... parameters);


    default CompletableFuture<TableMap> exeOneAsync(String sql, Executor executor, Object... parameters) {
        return CompletableFuture.supplyAsync(() -> exeOne(sql, parameters), executor);
    }

    CompletableFuture<TableMap> exeOneAsync(String sql, Object... parameters);

    default <T> CompletableFuture<T> exeOneAsync(Class<T> tClass, String sql, Executor executor, Object... parameters) {
        return CompletableFuture.supplyAsync(() -> exeOne(tClass, sql, parameters), executor);
    }

    <T> CompletableFuture<T> exeOneAsync(Class<T> tClass, String sql, Object... parameters);


    default CompletableFuture<List<TableMap>> exeListAsync(String sql, Executor executor, Object... parameters) {
        return CompletableFuture.supplyAsync(() -> exeList(sql, parameters), executor);
    }

    CompletableFuture<List<TableMap>> exeListAsync(String sql, Object... parameters);

    default <T> CompletableFuture<List<T>> exeListAsync(Class<T> tClass, String sql, Executor executor, Object... parameters) {
        return CompletableFuture.supplyAsync(() -> exeList(tClass, sql, parameters), executor);
    }

    <T> CompletableFuture<List<T>> exeListAsync(Class<T> tClass, String sql, Object... parameters);


    default <T> CompletableFuture<T> exeValueAsync(Class<T> tClass, String sql, Executor executor, Object... parameters) {
        return CompletableFuture.supplyAsync(() -> exeValue(tClass, sql, parameters), executor);
    }

    <T> CompletableFuture<T> exeValueAsync(Class<T> tClass, String sql, Object... parameters);


    default CompletableFuture<String> exeValueAsync(String sql, Executor executor, Object... parameters) {
        return CompletableFuture.supplyAsync(() -> exeValue(sql, parameters), executor);
    }

    CompletableFuture<String> exeValueAsync(String sql, Object... parameters);


    default <T> CompletableFuture<List<T>> exeValuesAsync(Class<T> tClass, String sql, Executor executor, Object... parameters) {
        return CompletableFuture.supplyAsync(() -> exeValues(tClass, sql, parameters, parameters), executor);
    }

    <T> CompletableFuture<List<T>> exeValuesAsync(Class<T> tClass, String sql, Object... parameters);


    default CompletableFuture<List<String>> exeValuesAsync(String sql, Executor executor, Object... parameters) {
        return CompletableFuture.supplyAsync(() -> exeValues(sql, parameters, parameters), executor);
    }

    CompletableFuture<List<String>> exeValuesAsync(String sql, Object... parameters);


    default CompletableFuture<List<TableMap>> exePageAsync(String sql, Integer startIndex, Integer pageSize, Executor executor, Object... parameters) {
        return CompletableFuture.supplyAsync(() -> exePage(sql, startIndex, pageSize, parameters), executor);
    }

    CompletableFuture<List<TableMap>> exePageAsync(String sql, Integer startIndex, Integer pageSize, Object... parameters);


    default CompletableFuture<List<TableMap>> exePageAsync(String sql, NeoPage neoPage, Executor executor, Object... parameters) {
        return CompletableFuture.supplyAsync(() -> exePage(sql, neoPage, parameters), executor);
    }

    CompletableFuture<List<TableMap>> exePageAsync(String sql, NeoPage neoPage, Object... parameters);


    default CompletableFuture<Integer> exeCountAsync(String sql, Executor executor, Object... parameters) {
        return CompletableFuture.supplyAsync(() -> exeCount(sql, parameters), executor);
    }

    CompletableFuture<Integer> exeCountAsync(String sql, Object... parameters);

    default CompletableFuture<List<List<TableMap>>> executeAsync(String sql, Executor executor, Object... parameters) {
        return CompletableFuture.supplyAsync(() -> execute(sql, parameters), executor);
    }

    CompletableFuture<List<List<TableMap>>> executeAsync(String sql, Object... parameters);
}
