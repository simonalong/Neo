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


    /**
     * 获取一行数据
     *
     * @param sql        查询一行数据对应的sql
     * @param parameters 占位符对应的参数
     * @return 某个表的一行数据
     */
    TableMap exeOne(String sql, Object... parameters);

    /**
     * 执行一行数据
     *
     * @param tClass     一行数据对应的类型
     * @param sql        查询一行数据对应的sql
     * @param parameters 占位符对应的参数
     * @param <T>        对应类型的泛型
     * @return 一行数据实体
     */
    <T> T exeOne(Class<T> tClass, String sql, Object... parameters);

    /**
     * 获取多行数据
     *
     * @param sql        查询多行数据对应的包含占位符"？"sql
     * @param parameters 占位符sql对应的参数
     * @return 某个表的多行数据
     */
    List<TableMap> exeList(String sql, Object... parameters);

    /**
     * 获取多行数据
     *
     * @param tClass     对应的实体的类型
     * @param sql        包含占位符"？"对应的sql
     * @param parameters 占位符"？"对应的参数
     * @param <T>        实体对应的类型
     * @return 实体对应的多行数据
     */
    <T> List<T> exeList(Class<T> tClass, String sql, Object... parameters);

    /**
     * 获取某行数据的某个列的值
     *
     * @param tClass     对应值的类型
     * @param sql        包含占位符"？"sql
     * @param parameters 占位符sql对应的参数
     * @param <T>        对应值的类型
     * @return 某个对应类型的值
     */
    <T> T exeValue(Class<T> tClass, String sql, Object... parameters);

    /**
     * 获取某行牟烈数据的值，返回String类型
     *
     * @param sql        包含占位符"？"的sql
     * @param parameters 占位符"？"对应的参数
     * @return 某个类型对应的值，String类型
     */
    String exeValue(String sql, Object... parameters);

    /**
     * 获取某列值，集合类型
     *
     * @param tClass     值对应的类型
     * @param sql        包含占位符的sql
     * @param parameters 占位符对应的参数
     * @param <T>        值对应的泛型类型
     * @return 某列值的集合
     */
    <T> List<T> exeValues(Class<T> tClass, String sql, Object... parameters);

    /**
     * 获取某列值，集合类型，内部为String类型
     *
     * @param sql        包含占位符的sql
     * @param parameters 占位符对应的参数
     * @return 某列值的集合
     */
    List<String> exeValues(String sql, Object... parameters);

    /**
     * 执行分页，返回值为某个分页对应的集合数据
     *
     * @param sql        包含占位符的sql
     * @param startIndex 分页的开始位置
     * @param pageSize   分页的页面大小
     * @param parameters 占位符对应的参数
     * @return 某个对应集合的数据
     */
    List<TableMap> exePage(String sql, Integer startIndex, Integer pageSize, Object... parameters);

    /**
     * 执行分页，返回值为某个分页对应的集合数据
     *
     * @param tClass     分页中数据对应的类型
     * @param sql        包含占位对应的sql
     * @param startIndex 分页的开始位置
     * @param pageSize   分页的页面大小
     * @param parameters 占位符对应的参数
     * @param <T>        返回值对应的类型
     * @return 某个对应集合的数据
     */
    <T> List<T> exePage(Class<T> tClass, String sql, Integer startIndex, Integer pageSize, Object... parameters);

    /**
     * 执行分页，返回值为某个分页对应的集合数据
     *
     * @param sql        包含占位对应的sql
     * @param neoPage    分页对应的配置
     * @param parameters 占位符对应的参数
     * @return 某个对应集合的数据
     */
    List<TableMap> exePage(String sql, NeoPage neoPage, Object... parameters);

    /**
     * 执行分页，返回值为某个分页对应的集合数据
     *
     * @param tClass     集合中对应的类型
     * @param sql        包含占位对应的sql
     * @param neoPage    分页对应的配置
     * @param parameters 占位符对应的参数
     * @param <T>        返回值对应的类型
     * @return 某个对应集合的数据
     */
    <T> List<T> exePage(Class<T> tClass, String sql, NeoPage neoPage, Object... parameters);

    /**
     * 获取对应行的个数
     *
     * @param sql        包含占位对应的sql
     * @param parameters 占位符对应的参数
     * @return 行的个数
     */
    Integer exeCount(String sql, Object... parameters);

    /**
     * 执行某个sql
     *
     * @param sql        可以包含占位符对应的sql
     * @param parameters 如果包含占位符，则占位符对应的参数
     * @return sql执行之后对应的结果，如果只有一个数据，则默认为第一个值，其中{@link TableMap}对应的可以调用{@link TableMap#getFirst()}即可获取其中一个
     */
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
