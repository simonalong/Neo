package com.simonalong.neo.core;

import com.simonalong.neo.Columns;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.db.NeoPage;
import com.simonalong.neo.db.PageReq;
import com.simonalong.neo.db.PageRsp;
import com.simonalong.neo.express.SearchQuery;

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

    CompletableFuture<NeoMap> oneAsync(String tableName, Columns columns, SearchQuery searchQuery, Executor executor);

    CompletableFuture<NeoMap> oneAsync(String tableName, Columns columns, SearchQuery searchQuery);

    <T> CompletableFuture<T> oneAsync(String tableName, Columns columns, T entity, Executor executor);

    <T> CompletableFuture<T> oneAsync(String tableName, Columns columns, T entity);

    CompletableFuture<NeoMap> oneAsync(String tableName, NeoMap searchMap, Executor executor);

    CompletableFuture<NeoMap> oneAsync(String tableName, NeoMap searchMap);

    CompletableFuture<NeoMap> oneAsync(String tableName, SearchQuery searchQuery, Executor executor);

    CompletableFuture<NeoMap> oneAsync(String tableName, SearchQuery searchQuery);

    <T> CompletableFuture<T> oneAsync(String tableName, T entity, Executor executor);

    <T> CompletableFuture<T> oneAsync(String tableName, T entity);

    CompletableFuture<NeoMap> oneAsync(String tableName, Number id, Executor executor);

    CompletableFuture<NeoMap> oneAsync(String tableName, Number id);

    <T> CompletableFuture<T> oneAsync(Class<T> tClass, String tableName, Columns columns, NeoMap searchMap, Executor executor);

    <T> CompletableFuture<T> oneAsync(Class<T> tClass, String tableName, Columns columns, NeoMap searchMap);

    <T> CompletableFuture<T> oneAsync(Class<T> tClass, String tableName, Columns columns, SearchQuery searchQuery, Executor executor);

    <T> CompletableFuture<T> oneAsync(Class<T> tClass, String tableName, Columns columns, SearchQuery searchQuery);

    <T> CompletableFuture<T> oneAsync(Class<T> tClass, String tableName, NeoMap searchMap, Executor executor);

    <T> CompletableFuture<T> oneAsync(Class<T> tClass, String tableName, NeoMap searchMap);

    <T> CompletableFuture<T> oneAsync(Class<T> tClass, String tableName, SearchQuery searchQuery, Executor executor);

    <T> CompletableFuture<T> oneAsync(Class<T> tClass, String tableName, SearchQuery searchQuery);

    <T> CompletableFuture<T> oneAsync(Class<T> tClass, String tableName, Number id, Executor executor);

    <T> CompletableFuture<T> oneAsync(Class<T> tClass, String tableName, Number id);


    CompletableFuture<List<NeoMap>> listAsync(String tableName, Columns columns, NeoMap searchMap, Executor executor);

    CompletableFuture<List<NeoMap>> listAsync(String tableName, Columns columns, NeoMap searchMap);

    CompletableFuture<List<NeoMap>> listAsync(String tableName, Columns columns, SearchQuery searchQuery, Executor executor);

    CompletableFuture<List<NeoMap>> listAsync(String tableName, Columns columns, SearchQuery searchQuery);

    <T> CompletableFuture<List<T>> listAsync(String tableName, Columns columns, T entity, Executor executor);

    <T> CompletableFuture<List<T>> listAsync(String tableName, Columns columns, T entity);

    CompletableFuture<List<NeoMap>> listAsync(String tableName, NeoMap searchMap, Executor executor);

    CompletableFuture<List<NeoMap>> listAsync(String tableName, NeoMap searchMap);

    CompletableFuture<List<NeoMap>> listAsync(String tableName, SearchQuery searchQuery, Executor executor);

    CompletableFuture<List<NeoMap>> listAsync(String tableName, SearchQuery searchQuery);

    <T> CompletableFuture<List<T>> listAsync(String tableName, T entity, Executor executor);

    <T> CompletableFuture<List<T>> listAsync(String tableName, T entity);

    <T> CompletableFuture<List<T>> listAsync(Class<T> tClass, String tableName, Columns columns, NeoMap searchMap, Executor executor);

    <T> CompletableFuture<List<T>> listAsync(Class<T> tClass, String tableName, Columns columns, NeoMap searchMap);

    <T> CompletableFuture<List<T>> listAsync(Class<T> tClass, String tableName, Columns columns, SearchQuery searchQuery, Executor executor);

    <T> CompletableFuture<List<T>> listAsync(Class<T> tClass, String tableName, Columns columns, SearchQuery searchQuery);

    <T> CompletableFuture<List<T>> listAsync(Class<T> tClass, String tableName, NeoMap searchMap, Executor executor);

    <T> CompletableFuture<List<T>> listAsync(Class<T> tClass, String tableName, NeoMap searchMap);

    <T> CompletableFuture<List<T>> listAsync(Class<T> tClass, String tableName, SearchQuery searchQuery, Executor executor);

    <T> CompletableFuture<List<T>> listAsync(Class<T> tClass, String tableName, SearchQuery searchQuery);


    <T> CompletableFuture<T> valueAsync(String tableName, Class<T> tClass, String field, NeoMap searchMap, Executor executor);

    <T> CompletableFuture<T> valueAsync(String tableName, Class<T> tClass, String field, NeoMap searchMap);

    <T> CompletableFuture<T> valueAsync(String tableName, Class<T> tClass, String field, SearchQuery searchQuery, Executor executor);

    <T> CompletableFuture<T> valueAsync(String tableName, Class<T> tClass, String field, SearchQuery searchQuery);

    <T> CompletableFuture<T> valueAsync(String tableName, Class<T> tClass, String field, Object entity, Executor executor);

    <T> CompletableFuture<T> valueAsync(String tableName, Class<T> tClass, String field, Object entity);

    CompletableFuture<String> valueAsync(String tableName, String field, NeoMap searchMap, Executor executor);

    CompletableFuture<String> valueAsync(String tableName, String field, NeoMap searchMap);

    CompletableFuture<String> valueAsync(String tableName, String field, SearchQuery searchQuery, Executor executor);

    CompletableFuture<String> valueAsync(String tableName, String field, SearchQuery searchQuery);

    CompletableFuture<String> valueAsync(String tableName, String field, Object entity, Executor executor);

    CompletableFuture<String> valueAsync(String tableName, String field, Object entity);


    @Deprecated
    <T> CompletableFuture<List<T>> valuesAsync(String tableName, Class<T> tClass, String field, NeoMap searchMap, Executor executor);

    @Deprecated
    <T> CompletableFuture<List<T>> valuesAsync(String tableName, Class<T> tClass, String field, NeoMap searchMap);

    @Deprecated
    <T> CompletableFuture<List<T>> valuesAsync(String tableName, Class<T> tClass, String field, SearchQuery searchQuery, Executor executor);

    @Deprecated
    <T> CompletableFuture<List<T>> valuesAsync(String tableName, Class<T> tClass, String field, SearchQuery searchQuery);

    @Deprecated
    <T> CompletableFuture<List<T>> valuesAsync(String tableName, Class<T> tClass, String field, Object entity, Executor executor);

    @Deprecated
    <T> CompletableFuture<List<T>> valuesAsync(String tableName, Class<T> tClass, String field, Object entity);


    <T> CompletableFuture<List<T>> valuesAsync(Class<T> tClass, String tableName, String field, NeoMap searchMap, Executor executor);

    <T> CompletableFuture<List<T>> valuesAsync(Class<T> tClass, String tableName, String field, NeoMap searchMap);

    <T> CompletableFuture<List<T>> valuesAsync(Class<T> tClass, String tableName, String field, SearchQuery searchQuery, Executor executor);

    <T> CompletableFuture<List<T>> valuesAsync(Class<T> tClass, String tableName, String field, SearchQuery searchQuery);

    <T> CompletableFuture<List<T>> valuesAsync(Class<T> tClass, String tableName, String field, Object entity, Executor executor);

    <T> CompletableFuture<List<T>> valuesAsync(Class<T> tClass, String tableName, String field, Object entity);


    CompletableFuture<List<String>> valuesAsync(String tableName, String field, NeoMap searchMap, Executor executor);

    CompletableFuture<List<String>> valuesAsync(String tableName, String field, NeoMap searchMap);

    CompletableFuture<List<String>> valuesAsync(String tableName, String field, SearchQuery searchQuery, Executor executor);

    CompletableFuture<List<String>> valuesAsync(String tableName, String field, SearchQuery searchQuery);

    CompletableFuture<List<String>> valuesAsync(String tableName, String field, Object entity, Executor executor);

    CompletableFuture<List<String>> valuesAsync(String tableName, String field, Object entity);

    CompletableFuture<List<String>> valuesAsync(String tableName, String field, Executor executor);

    CompletableFuture<List<String>> valuesAsync(String tableName, String field);

    @Deprecated
    <T> CompletableFuture<List<T>> valuesOfDistinctAsync(String tableName, Class<T> tClass, String field, NeoMap searchMap, Executor executor);

    @Deprecated
    <T> CompletableFuture<List<T>> valuesOfDistinctAsync(String tableName, Class<T> tClass, String field, NeoMap searchMap);

    @Deprecated
    <T> CompletableFuture<List<T>> valuesOfDistinctAsync(String tableName, Class<T> tClass, String field, SearchQuery searchQuery, Executor executor);

    @Deprecated
    <T> CompletableFuture<List<T>> valuesOfDistinctAsync(String tableName, Class<T> tClass, String field, SearchQuery searchQuery);

    @Deprecated
    <T> CompletableFuture<List<T>> valuesOfDistinctAsync(String tableName, Class<T> tClass, String field, Object entity, Executor executor);

    @Deprecated
    <T> CompletableFuture<List<T>> valuesOfDistinctAsync(String tableName, Class<T> tClass, String field, Object entity);


    <T> CompletableFuture<List<T>> valuesOfDistinctAsync(Class<T> tClass, String tableName, String field, NeoMap searchMap, Executor executor);

    <T> CompletableFuture<List<T>> valuesOfDistinctAsync(Class<T> tClass, String tableName, String field, NeoMap searchMap);

    <T> CompletableFuture<List<T>> valuesOfDistinctAsync(Class<T> tClass, String tableName, String field, SearchQuery searchQuery, Executor executor);

    <T> CompletableFuture<List<T>> valuesOfDistinctAsync(Class<T> tClass, String tableName, String field, SearchQuery searchQuery);

    <T> CompletableFuture<List<T>> valuesOfDistinctAsync(Class<T> tClass, String tableName, String field, Object entity, Executor executor);

    <T> CompletableFuture<List<T>> valuesOfDistinctAsync(Class<T> tClass, String tableName, String field, Object entity);

    CompletableFuture<List<String>> valuesOfDistinctAsync(String tableName, String field, NeoMap searchMap, Executor executor);

    CompletableFuture<List<String>> valuesOfDistinctAsync(String tableName, String field, NeoMap searchMap);

    CompletableFuture<List<String>> valuesOfDistinctAsync(String tableName, String field, SearchQuery searchQuery, Executor executor);

    CompletableFuture<List<String>> valuesOfDistinctAsync(String tableName, String field, SearchQuery searchQuery);

    CompletableFuture<List<String>> valuesOfDistinctAsync(String tableName, String field, Object entity, Executor executor);

    CompletableFuture<List<String>> valuesOfDistinctAsync(String tableName, String field, Object entity);

    CompletableFuture<List<String>> valuesOfDistinctAsync(String tableName, String field, Executor executor);

    CompletableFuture<List<String>> valuesOfDistinctAsync(String tableName, String field);


    /**
     * 弃用，其中参数NeoPage弃用，变更为{@link PageReq}
     *
     * @param tableName 表名
     * @param columns   列
     * @param searchMap 搜索条件
     * @param page      分页
     * @param executor  线程池
     * @return 异步的分页列表
     */
    @Deprecated
    CompletableFuture<List<NeoMap>> pageAsync(String tableName, Columns columns, NeoMap searchMap, NeoPage page, Executor executor);

    @Deprecated
    CompletableFuture<List<NeoMap>> pageAsync(String tableName, Columns columns, NeoMap searchMap, NeoPage page);

    @Deprecated
    CompletableFuture<List<NeoMap>> pageAsync(String tableName, Columns columns, SearchQuery searchQuery, NeoPage page, Executor executor);

    @Deprecated
    CompletableFuture<List<NeoMap>> pageAsync(String tableName, Columns columns, SearchQuery searchQuery, NeoPage page);

    @Deprecated
    <T> CompletableFuture<List<T>> pageAsync(String tableName, Columns columns, T entity, NeoPage page, Executor executor);

    @Deprecated
    <T> CompletableFuture<List<T>> pageAsync(String tableName, Columns columns, T entity, NeoPage page);

    @Deprecated
    CompletableFuture<List<NeoMap>> pageAsync(String tableName, NeoMap searchMap, NeoPage page, Executor executor);

    @Deprecated
    CompletableFuture<List<NeoMap>> pageAsync(String tableName, NeoMap searchMap, NeoPage page);

    @Deprecated
    CompletableFuture<List<NeoMap>> pageAsync(String tableName, SearchQuery searchQuery, NeoPage page, Executor executor);

    @Deprecated
    CompletableFuture<List<NeoMap>> pageAsync(String tableName, SearchQuery searchQuery, NeoPage page);

    @Deprecated
    <T> CompletableFuture<List<T>> pageAsync(String tableName, T entity, NeoPage page, Executor executor);

    @Deprecated
    <T> CompletableFuture<List<T>> pageAsync(String tableName, T entity, NeoPage page);

    @Deprecated
    CompletableFuture<List<NeoMap>> pageAsync(String tableName, Columns columns, NeoPage page, Executor executor);

    @Deprecated
    CompletableFuture<List<NeoMap>> pageAsync(String tableName, Columns columns, NeoPage page);

    @Deprecated
    CompletableFuture<List<NeoMap>> pageAsync(String tableName, NeoPage page, Executor executor);

    @Deprecated
    CompletableFuture<List<NeoMap>> pageAsync(String tableName, NeoPage page);


    CompletableFuture<List<NeoMap>> pageAsync(String tableName, Columns columns, NeoMap searchMap, PageReq<?> pageReq, Executor executor);

    CompletableFuture<List<NeoMap>> pageAsync(String tableName, Columns columns, NeoMap searchMap, PageReq<?> pageReq);

    CompletableFuture<List<NeoMap>> pageAsync(String tableName, Columns columns, SearchQuery searchQuery, PageReq<?> pageReq, Executor executor);

    CompletableFuture<List<NeoMap>> pageAsync(String tableName, Columns columns, SearchQuery searchQuery, PageReq<?> pageReq);

    <T> CompletableFuture<List<T>> pageAsync(String tableName, Columns columns, T entity, PageReq<?> pageReq, Executor executor);

    <T> CompletableFuture<List<T>> pageAsync(String tableName, Columns columns, T entity, PageReq<?> pageReq);

    CompletableFuture<List<NeoMap>> pageAsync(String tableName, NeoMap searchMap, PageReq<?> pageReq, Executor executor);

    CompletableFuture<List<NeoMap>> pageAsync(String tableName, NeoMap searchMap, PageReq<?> pageReq);

    CompletableFuture<List<NeoMap>> pageAsync(String tableName, SearchQuery searchQuery, PageReq<?> pageReq, Executor executor);

    CompletableFuture<List<NeoMap>> pageAsync(String tableName, SearchQuery searchQuery, PageReq<?> pageReq);

    <T> CompletableFuture<List<T>> pageAsync(String tableName, T entity, PageReq<?> pageReq, Executor executor);

    <T> CompletableFuture<List<T>> pageAsync(String tableName, T entity, PageReq<?> pageReq);

    CompletableFuture<List<NeoMap>> pageAsync(String tableName, Columns columns, PageReq<?> pageReq, Executor executor);

    CompletableFuture<List<NeoMap>> pageAsync(String tableName, Columns columns, PageReq<?> pageReq);

    CompletableFuture<List<NeoMap>> pageAsync(String tableName, PageReq<?> pageReq, Executor executor);

    CompletableFuture<List<NeoMap>> pageAsync(String tableName, PageReq<?> pageReq);


    /**
     * pageAsync弃用，其中参数NeoPage弃用，修改为{@link PageReq}
     *
     * @param tClass    返回的类型
     * @param tableName 表名
     * @param columns   列
     * @param searchMap 搜索条件
     * @param page      分页数据
     * @param executor  执行器
     * @param <T>       具体类型
     * @return 返回数据
     */
    @Deprecated
    <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, Columns columns, NeoMap searchMap, NeoPage page, Executor executor);

    @Deprecated
    <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, Columns columns, NeoMap searchMap, NeoPage page);

    @Deprecated
    <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, Columns columns, SearchQuery searchQuery, NeoPage page, Executor executor);

    @Deprecated
    <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, Columns columns, SearchQuery searchQuery, NeoPage page);

    @Deprecated
    <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, NeoMap searchMap, NeoPage page, Executor executor);

    @Deprecated
    <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, NeoMap searchMap, NeoPage page);

    @Deprecated
    <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, SearchQuery searchQuery, NeoPage page, Executor executor);

    @Deprecated
    <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, SearchQuery searchQuery, NeoPage page);

    @Deprecated
    <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, Columns columns, NeoPage page, Executor executor);

    @Deprecated
    <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, Columns columns, NeoPage page);

    @Deprecated
    <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, NeoPage page, Executor executor);

    @Deprecated
    <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, NeoPage page);


    <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, Columns columns, NeoMap searchMap, PageReq<?> pageReq, Executor executor);

    <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, Columns columns, NeoMap searchMap, PageReq<?> pageReq);

    <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, Columns columns, SearchQuery searchQuery, PageReq<?> pageReq, Executor executor);

    <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, Columns columns, SearchQuery searchQuery, PageReq<?> pageReq);

    <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, NeoMap searchMap, PageReq<?> pageReq, Executor executor);

    <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, NeoMap searchMap, PageReq<?> pageReq);

    <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, SearchQuery searchQuery, PageReq<?> pageReq, Executor executor);

    <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, SearchQuery searchQuery, PageReq<?> pageReq);

    <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, Columns columns, PageReq<?> pageReq, Executor executor);

    <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, Columns columns, PageReq<?> pageReq);

    <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, PageReq<?> pageReq, Executor executor);

    <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, PageReq<?> pageReq);


    CompletableFuture<PageRsp<NeoMap>> getPageAsync(String tableName, Columns columns, NeoMap searchMap, PageReq<?> pageReq, Executor executor);

    CompletableFuture<PageRsp<NeoMap>> getPageAsync(String tableName, Columns columns, NeoMap searchMap, PageReq<?> pageReq);

    CompletableFuture<PageRsp<NeoMap>> getPageAsync(String tableName, Columns columns, SearchQuery searchQuery, PageReq<?> pageReq, Executor executor);

    CompletableFuture<PageRsp<NeoMap>> getPageAsync(String tableName, Columns columns, SearchQuery searchQuery, PageReq<?> pageReq);

    <T> CompletableFuture<PageRsp<T>> getPageAsync(String tableName, Columns columns, T entity, PageReq<?> pageReq, Executor executor);

    <T> CompletableFuture<PageRsp<T>> getPageAsync(String tableName, Columns columns, T entity, PageReq<?> pageReq);

    CompletableFuture<PageRsp<NeoMap>> getPageAsync(String tableName, NeoMap searchMap, PageReq<?> pageReq, Executor executor);

    CompletableFuture<PageRsp<NeoMap>> getPageAsync(String tableName, NeoMap searchMap, PageReq<?> pageReq);

    CompletableFuture<PageRsp<NeoMap>> getPageAsync(String tableName, SearchQuery searchQuery, PageReq<?> pageReq, Executor executor);

    CompletableFuture<PageRsp<NeoMap>> getPageAsync(String tableName, SearchQuery searchQuery, PageReq<?> pageReq);

    <T> CompletableFuture<PageRsp<T>> getPageAsync(String tableName, T entity, PageReq<?> pageReq, Executor executor);

    <T> CompletableFuture<PageRsp<T>> getPageAsync(String tableName, T entity, PageReq<?> pageReq);

    CompletableFuture<PageRsp<NeoMap>> getPageAsync(String tableName, Columns columns, PageReq<?> pageReq, Executor executor);

    CompletableFuture<PageRsp<NeoMap>> getPageAsync(String tableName, Columns columns, PageReq<?> pageReq);

    CompletableFuture<PageRsp<NeoMap>> getPageAsync(String tableName, PageReq<?> pageReq, Executor executor);

    CompletableFuture<PageRsp<NeoMap>> getPageAsync(String tableName, PageReq<?> pageReq);


    <T> CompletableFuture<PageRsp<T>> getPageAsync(Class<T> tClass, String tableName, Columns columns, NeoMap searchMap, PageReq<?> pageReq, Executor executor);

    <T> CompletableFuture<PageRsp<T>> getPageAsync(Class<T> tClass, String tableName, Columns columns, NeoMap searchMap, PageReq<?> pageReq);

    <T> CompletableFuture<PageRsp<T>> getPageAsync(Class<T> tClass, String tableName, Columns columns, SearchQuery searchQuery, PageReq<?> pageReq, Executor executor);

    <T> CompletableFuture<PageRsp<T>> getPageAsync(Class<T> tClass, String tableName, Columns columns, SearchQuery searchQuery, PageReq<?> pageReq);

    <T> CompletableFuture<PageRsp<T>> getPageAsync(Class<T> tClass, String tableName, NeoMap searchMap, PageReq<?> pageReq, Executor executor);

    <T> CompletableFuture<PageRsp<T>> getPageAsync(Class<T> tClass, String tableName, NeoMap searchMap, PageReq<?> pageReq);

    <T> CompletableFuture<PageRsp<T>> getPageAsync(Class<T> tClass, String tableName, SearchQuery searchQuery, PageReq<?> pageReq, Executor executor);

    <T> CompletableFuture<PageRsp<T>> getPageAsync(Class<T> tClass, String tableName, SearchQuery searchQuery, PageReq<?> pageReq);

    <T> CompletableFuture<PageRsp<T>> getPageAsync(Class<T> tClass, String tableName, Columns columns, PageReq<?> pageReq, Executor executor);

    <T> CompletableFuture<PageRsp<T>> getPageAsync(Class<T> tClass, String tableName, Columns columns, PageReq<?> pageReq);

    <T> CompletableFuture<PageRsp<T>> getPageAsync(Class<T> tClass, String tableName, PageReq<?> pageReq, Executor executor);

    <T> CompletableFuture<PageRsp<T>> getPageAsync(Class<T> tClass, String tableName, PageReq<?> pageReq);


    CompletableFuture<Integer> countAsync(String tableName, NeoMap searchMap, Executor executor);

    CompletableFuture<Integer> countAsync(String tableName, NeoMap searchMap);

    CompletableFuture<Integer> countAsync(String tableName, SearchQuery searchQuery, Executor executor);

    CompletableFuture<Integer> countAsync(String tableName, SearchQuery searchQuery);

    CompletableFuture<Integer> countAsync(String tableName, Object entity, Executor executor);

    CompletableFuture<Integer> countAsync(String tableName, Object entity);

    CompletableFuture<Integer> countAsync(String tableName, Executor executor);

    CompletableFuture<Integer> countAsync(String tableName);


    CompletableFuture<Boolean> existAsync(String tableName, NeoMap searchMap, Executor executor);

    CompletableFuture<Boolean> existAsync(String tableName, NeoMap searchMap);

    CompletableFuture<Boolean> existAsync(String tableName, SearchQuery searchQuery, Executor executor);

    CompletableFuture<Boolean> existAsync(String tableName, SearchQuery searchQuery);

    CompletableFuture<Boolean> existAsync(String tableName, Object entity, Executor executor);

    CompletableFuture<Boolean> existAsync(String tableName, Object entity);

    CompletableFuture<Boolean> existAsync(String tableName, Number id, Executor executor);

    CompletableFuture<Boolean> existAsync(String tableName, Number id);
}
