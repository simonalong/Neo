package com.simonalong.neo.devide;

import com.simonalong.neo.Columns;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.core.DefaultExecutor;
import com.simonalong.neo.core.QuerySync;
import com.simonalong.neo.db.NeoPage;
import com.simonalong.neo.db.PageReq;
import com.simonalong.neo.db.PageRsp;
import com.simonalong.neo.express.SearchQuery;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author shizi
 * @since 2020/6/11 3:11 PM
 */
public abstract class AbstractBaseQuery extends AbstractQueryAsync implements QuerySync {

    @Override
    public Executor getExecutor() {
        return DefaultExecutor.getInstance().getExecutor();
    }

    @Override
    public CompletableFuture<NeoMap> oneAsync(String tableName, Columns columns, NeoMap searchMap, Executor executor){
        return CompletableFuture.supplyAsync(() -> one(tableName, columns, searchMap), executor);
    }

    @Override
    public CompletableFuture<NeoMap> oneAsync(String tableName, Columns columns, SearchQuery searchQuery, Executor executor){
        return CompletableFuture.supplyAsync(() -> one(tableName, columns, searchQuery), executor);
    }

    @Override
    public <T> CompletableFuture<T> oneAsync(String tableName, Columns columns, T entity, Executor executor){
        return CompletableFuture.supplyAsync(() -> one(tableName, columns, entity), executor);
    }

    @Override
    public CompletableFuture<NeoMap> oneAsync(String tableName, NeoMap searchMap, Executor executor){
        return CompletableFuture.supplyAsync(() -> one(tableName, searchMap), executor);
    }

    @Override
    public CompletableFuture<NeoMap> oneAsync(String tableName, SearchQuery searchQuery, Executor executor) {
        return CompletableFuture.supplyAsync(() -> one(tableName, searchQuery), executor);
    }

    @Override
    public <T> CompletableFuture<T> oneAsync(String tableName, T entity, Executor executor){
        return CompletableFuture.supplyAsync(() -> one(tableName, entity), executor);
    }

    @Override
    public CompletableFuture<NeoMap> oneAsync(String tableName, Number id, Executor executor){
        return CompletableFuture.supplyAsync(() -> one(tableName, id), executor);
    }

    @Override
    public <T> CompletableFuture<T> oneAsync(Class<T> tClass, String tableName, Columns columns, NeoMap searchMap, Executor executor) {
        return CompletableFuture.supplyAsync(() -> one(tClass, tableName, columns, searchMap), executor);
    }

    @Override
    public <T> CompletableFuture<T> oneAsync(Class<T> tClass, String tableName, Columns columns, SearchQuery searchQuery, Executor executor) {
        return CompletableFuture.supplyAsync(() -> one(tClass, tableName, columns, searchQuery), executor);
    }

    @Override
    public <T> CompletableFuture<T> oneAsync(Class<T> tClass, String tableName, NeoMap searchMap, Executor executor) {
        return CompletableFuture.supplyAsync(() -> one(tClass, tableName, searchMap), executor);
    }

    @Override
    public <T> CompletableFuture<T> oneAsync(Class<T> tClass, String tableName, SearchQuery searchQuery, Executor executor) {
        return CompletableFuture.supplyAsync(() -> one(tClass, tableName, searchQuery), executor);
    }

    @Override
    public <T> CompletableFuture<T> oneAsync(Class<T> tClass, String tableName, Number id, Executor executor) {
        return CompletableFuture.supplyAsync(() -> one(tClass, tableName, id), executor);
    }


    @Override
    public CompletableFuture<List<NeoMap>> listAsync(String tableName, Columns columns, NeoMap searchMap, Executor executor){
        return CompletableFuture.supplyAsync(() -> list(tableName, columns, searchMap), executor);
    }

    @Override
    public CompletableFuture<List<NeoMap>> listAsync(String tableName, Columns columns, SearchQuery searchQuery, Executor executor) {
        return CompletableFuture.supplyAsync(() -> list(tableName, columns, searchQuery), executor);
    }

    @Override
    public <T> CompletableFuture<List<T>> listAsync(String tableName, Columns columns, T entity, Executor executor){
        return CompletableFuture.supplyAsync(() -> list(tableName, columns, entity), executor);
    }

    @Override
    public CompletableFuture<List<NeoMap>> listAsync(String tableName, NeoMap searchMap, Executor executor){
        return CompletableFuture.supplyAsync(() -> list(tableName, searchMap), executor);
    }

    @Override
    public CompletableFuture<List<NeoMap>> listAsync(String tableName, SearchQuery searchQuery, Executor executor) {
        return CompletableFuture.supplyAsync(() -> list(tableName, searchQuery), executor);
    }

    @Override
    public <T> CompletableFuture<List<T>> listAsync(String tableName, T entity, Executor executor){
        return CompletableFuture.supplyAsync(() -> list(tableName, entity), executor);
    }

    @Override
    public <T> CompletableFuture<List<T>> listAsync(Class<T> tClass, String tableName, Columns columns, NeoMap searchMap, Executor executor) {
        return CompletableFuture.supplyAsync(() -> list(tClass, tableName, columns, searchMap), executor);
    }

    @Override
    public <T> CompletableFuture<List<T>> listAsync(Class<T> tClass, String tableName, Columns columns, SearchQuery searchQuery, Executor executor) {
        return CompletableFuture.supplyAsync(() -> list(tClass, tableName, columns, searchQuery), executor);
    }

    @Override
    public <T> CompletableFuture<List<T>> listAsync(Class<T> tClass, String tableName, NeoMap searchMap, Executor executor) {
        return CompletableFuture.supplyAsync(() -> list(tClass, tableName, searchMap), executor);
    }

    @Override
    public <T> CompletableFuture<List<T>> listAsync(Class<T> tClass, String tableName, SearchQuery searchQuery, Executor executor) {
        return CompletableFuture.supplyAsync(() -> list(tClass, tableName, searchQuery), executor);
    }

    @Override
    public <T> CompletableFuture<List<T>> valuesAsync(String tableName, Class<T> tClass, String field, NeoMap searchMap, Executor executor){
        return CompletableFuture.supplyAsync(() -> values(tClass, tableName, field, searchMap), executor);
    }

    @Override
    public <T> CompletableFuture<List<T>> valuesAsync(String tableName, Class<T> tClass, String field, SearchQuery searchQuery, Executor executor) {
        return CompletableFuture.supplyAsync(() -> values(tClass, tableName, field, searchQuery), executor);
    }

    @Override
    public <T> CompletableFuture<List<T>> valuesAsync(String tableName, Class<T> tClass, String field, Object entity, Executor executor){
        return CompletableFuture.supplyAsync(() -> values(tClass, tableName, field, entity), executor);
    }

    @Override
    public CompletableFuture<List<String>> valuesAsync(String tableName, String field, NeoMap searchMap, Executor executor){
        return CompletableFuture.supplyAsync(() -> values(tableName, field, searchMap), executor);
    }

    @Override
    public CompletableFuture<List<String>> valuesAsync(String tableName, String field, SearchQuery searchQuery, Executor executor) {
        return CompletableFuture.supplyAsync(() -> values(tableName, field, searchQuery), executor);
    }


    @Override
    public <T> CompletableFuture<List<T>> valuesAsync(Class<T> tClass, String tableName, String field, NeoMap searchMap, Executor executor) {
        return CompletableFuture.supplyAsync(() -> values(tClass, tableName, field, searchMap), executor);
    }

    @Override
    public <T> CompletableFuture<List<T>> valuesAsync(Class<T> tClass, String tableName, String field, SearchQuery searchQuery, Executor executor) {
        return CompletableFuture.supplyAsync(() -> values(tClass, tableName, field, searchQuery), executor);
    }

    @Override
    public <T> CompletableFuture<List<T>> valuesAsync(Class<T> tClass, String tableName, String field, Object entity, Executor executor) {
        return CompletableFuture.supplyAsync(() -> values(tClass, tableName, field, entity), executor);
    }

    @Override
    public CompletableFuture<List<String>> valuesAsync(String tableName, String field, Object entity, Executor executor){
        return CompletableFuture.supplyAsync(() -> values(tableName, field, entity), executor);
    }

    @Override
    public CompletableFuture<List<String>> valuesAsync(String tableName, String field, Executor executor){
        return CompletableFuture.supplyAsync(() -> values(tableName, field), executor);
    }

    @Override
    public <T> CompletableFuture<List<T>> valuesOfDistinctAsync(String tableName, Class<T> tClass, String field, NeoMap searchMap, Executor executor) {
        return CompletableFuture.supplyAsync(() -> valuesOfDistinct(tClass, tableName, field, searchMap), executor);
    }

    @Override
    public <T> CompletableFuture<List<T>> valuesOfDistinctAsync(String tableName, Class<T> tClass, String field, SearchQuery searchQuery, Executor executor) {
        return CompletableFuture.supplyAsync(() -> valuesOfDistinct(tClass, tableName, field, searchQuery), executor);
    }

    @Override
    public <T> CompletableFuture<List<T>> valuesOfDistinctAsync(String tableName, Class<T> tClass, String field, Object entity, Executor executor) {
        return CompletableFuture.supplyAsync(() -> valuesOfDistinct(tClass, tableName, field, entity), executor);
    }

    @Override
    public <T> CompletableFuture<List<T>> valuesOfDistinctAsync(Class<T> tClass, String tableName, String field, NeoMap searchMap, Executor executor) {
        return CompletableFuture.supplyAsync(() -> valuesOfDistinct(tClass, tableName, field, searchMap), executor);
    }

    @Override
    public <T> CompletableFuture<List<T>> valuesOfDistinctAsync(Class<T> tClass, String tableName, String field, SearchQuery searchQuery, Executor executor) {
        return CompletableFuture.supplyAsync(() -> valuesOfDistinct(tClass, tableName, field, searchQuery), executor);
    }

    @Override
    public <T> CompletableFuture<List<T>> valuesOfDistinctAsync(Class<T> tClass, String tableName, String field, Object entity, Executor executor) {
        return CompletableFuture.supplyAsync(() -> valuesOfDistinct(tClass, tableName, field, entity), executor);
    }


    @Override
    public CompletableFuture<List<String>> valuesOfDistinctAsync(String tableName, String field, NeoMap searchMap, Executor executor) {
        return CompletableFuture.supplyAsync(() -> valuesOfDistinct(tableName, field, searchMap), executor);
    }

    @Override
    public CompletableFuture<List<String>> valuesOfDistinctAsync(String tableName, String field, SearchQuery searchQuery, Executor executor) {
        return CompletableFuture.supplyAsync(() -> valuesOfDistinct(tableName, field, searchQuery), executor);
    }

    @Override
    public CompletableFuture<List<String>> valuesOfDistinctAsync(String tableName, String field, Object entity, Executor executor) {
        return CompletableFuture.supplyAsync(() -> valuesOfDistinct(tableName, field, entity), executor);
    }

    @Override
    public CompletableFuture<List<String>> valuesOfDistinctAsync(String tableName, String field, Executor executor) {
        return CompletableFuture.supplyAsync(() -> valuesOfDistinct(tableName, field), executor);
    }

    @Override
    public <T> CompletableFuture<T> valueAsync(String tableName, Class<T> tClass, String field, NeoMap searchMap, Executor executor){
        return CompletableFuture.supplyAsync(() -> value(tClass, tableName, field, searchMap), executor);
    }

    @Override
    public <T> CompletableFuture<T> valueAsync(String tableName, Class<T> tClass, String field, SearchQuery searchQuery, Executor executor) {
        return CompletableFuture.supplyAsync(() -> value(tClass, tableName, field, searchQuery), executor);
    }

    @Override
    public <T> CompletableFuture<T> valueAsync(String tableName, Class<T> tClass, String field, Object entity, Executor executor){
        return CompletableFuture.supplyAsync(() -> value(tClass, tableName, field, entity), executor);
    }

    @Override
    public CompletableFuture<String> valueAsync(String tableName, String field, NeoMap searchMap, Executor executor){
        return CompletableFuture.supplyAsync(() -> value(tableName, field, searchMap), executor);
    }

    @Override
    public CompletableFuture<String> valueAsync(String tableName, String field, SearchQuery searchQuery, Executor executor) {
        return CompletableFuture.supplyAsync(() -> value(tableName, field, searchQuery), executor);
    }

    @Override
    public CompletableFuture<String> valueAsync(String tableName, String field, Object entity, Executor executor){
        return CompletableFuture.supplyAsync(() -> value(tableName, field, entity), executor);
    }


    @Override
    @Deprecated
    public CompletableFuture<List<NeoMap>> pageAsync(String tableName, Columns columns, NeoMap searchMap, NeoPage page, Executor executor){
        return CompletableFuture.supplyAsync(() -> page(tableName, columns, searchMap, page), executor);
    }

    @Override
    @Deprecated
    public CompletableFuture<List<NeoMap>> pageAsync(String tableName, Columns columns, SearchQuery searchQuery, NeoPage page, Executor executor) {
        return CompletableFuture.supplyAsync(() -> page(tableName, columns, searchQuery, page), executor);
    }

    @Override
    @Deprecated
    public <T> CompletableFuture<List<T>> pageAsync(String tableName, Columns columns, T entity, NeoPage page, Executor executor){
        return CompletableFuture.supplyAsync(() -> page(tableName, columns, entity, page), executor);
    }

    @Override
    @Deprecated
    public CompletableFuture<List<NeoMap>> pageAsync(String tableName, NeoMap searchMap, NeoPage page, Executor executor){
        return CompletableFuture.supplyAsync(() -> page(tableName, searchMap, page), executor);
    }

    @Override
    @Deprecated
    public CompletableFuture<List<NeoMap>> pageAsync(String tableName, SearchQuery searchQuery, NeoPage page, Executor executor) {
        return CompletableFuture.supplyAsync(() -> page(tableName, searchQuery, page), executor);
    }

    @Override
    @Deprecated
    public <T> CompletableFuture<List<T>> pageAsync(String tableName, T entity, NeoPage page, Executor executor){
        return CompletableFuture.supplyAsync(() -> page(tableName, entity, page), executor);
    }

    @Override
    @Deprecated
    public CompletableFuture<List<NeoMap>> pageAsync(String tableName, Columns columns, NeoPage page, Executor executor){
        return CompletableFuture.supplyAsync(() -> page(tableName, columns, page), executor);
    }

    @Override
    @Deprecated
    public CompletableFuture<List<NeoMap>> pageAsync(String tableName, NeoPage page, Executor executor){
        return CompletableFuture.supplyAsync(() -> page(tableName, page), executor);
    }

    @Override
    @Deprecated
    public <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, Columns columns, NeoMap searchMap, NeoPage page, Executor executor) {
        return CompletableFuture.supplyAsync(() -> page(tClass, tableName, columns, searchMap, page), executor);
    }

    @Override
    @Deprecated
    public <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, Columns columns, SearchQuery searchQuery, NeoPage page, Executor executor) {
        return CompletableFuture.supplyAsync(() -> page(tClass, tableName, columns, searchQuery, page), executor);
    }

    @Override
    @Deprecated
    public <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, NeoMap searchMap, NeoPage page, Executor executor) {
        return CompletableFuture.supplyAsync(() -> page(tClass, tableName, searchMap, page), executor);
    }

    @Override
    @Deprecated
    public <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, SearchQuery searchQuery, NeoPage page, Executor executor) {
        return CompletableFuture.supplyAsync(() -> page(tClass, tableName, searchQuery, page), executor);
    }

    @Override
    @Deprecated
    public <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, Columns columns, NeoPage page, Executor executor) {
        return CompletableFuture.supplyAsync(() -> page(tClass, tableName, columns, page), executor);
    }

    @Override
    @Deprecated
    public <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, NeoPage page, Executor executor) {
        return CompletableFuture.supplyAsync(() -> page(tClass, tableName, page), executor);
    }


    @Override
    public CompletableFuture<List<NeoMap>> pageAsync(String tableName, Columns columns, NeoMap searchMap, PageReq<?> pageReq, Executor executor){
        return CompletableFuture.supplyAsync(() -> page(tableName, columns, searchMap, pageReq), executor);
    }

    @Override
    public CompletableFuture<List<NeoMap>> pageAsync(String tableName, Columns columns, SearchQuery searchQuery, PageReq<?> pageReq, Executor executor) {
        return CompletableFuture.supplyAsync(() -> page(tableName, columns, searchQuery, pageReq), executor);
    }

    @Override
    public <T> CompletableFuture<List<T>> pageAsync(String tableName, Columns columns, T entity, PageReq<?> pageReq, Executor executor){
        return CompletableFuture.supplyAsync(() -> page(tableName, columns, entity, pageReq), executor);
    }

    @Override
    public CompletableFuture<List<NeoMap>> pageAsync(String tableName, NeoMap searchMap, PageReq<?> pageReq, Executor executor){
        return CompletableFuture.supplyAsync(() -> page(tableName, searchMap, pageReq), executor);
    }

    @Override
    public CompletableFuture<List<NeoMap>> pageAsync(String tableName, SearchQuery searchQuery, PageReq<?> pageReq, Executor executor) {
        return CompletableFuture.supplyAsync(() -> page(tableName, searchQuery, pageReq), executor);
    }

    @Override
    public <T> CompletableFuture<List<T>> pageAsync(String tableName, T entity, PageReq<?> pageReq, Executor executor){
        return CompletableFuture.supplyAsync(() -> page(tableName, entity, pageReq), executor);
    }

    @Override
    public CompletableFuture<List<NeoMap>> pageAsync(String tableName, Columns columns, PageReq<?> pageReq, Executor executor){
        return CompletableFuture.supplyAsync(() -> page(tableName, columns, pageReq), executor);
    }

    @Override
    public CompletableFuture<List<NeoMap>> pageAsync(String tableName, PageReq<?> pageReq, Executor executor){
        return CompletableFuture.supplyAsync(() -> page(tableName, pageReq), executor);
    }

    @Override
    public <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, Columns columns, NeoMap searchMap, PageReq<?> pageReq, Executor executor) {
        return CompletableFuture.supplyAsync(() -> page(tClass, tableName, columns, searchMap, pageReq), executor);
    }

    @Override
    public <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, Columns columns, SearchQuery searchQuery, PageReq<?> pageReq, Executor executor) {
        return CompletableFuture.supplyAsync(() -> page(tClass, tableName, columns, searchQuery, pageReq), executor);
    }

    @Override
    public <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, NeoMap searchMap, PageReq<?> pageReq, Executor executor) {
        return CompletableFuture.supplyAsync(() -> page(tClass, tableName, searchMap, pageReq), executor);
    }

    @Override
    public <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, SearchQuery searchQuery, PageReq<?> pageReq, Executor executor) {
        return CompletableFuture.supplyAsync(() -> page(tClass, tableName, searchQuery, pageReq), executor);
    }

    @Override
    public <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, Columns columns, PageReq<?> pageReq, Executor executor) {
        return CompletableFuture.supplyAsync(() -> page(tClass, tableName, columns, pageReq), executor);
    }

    @Override
    public <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, PageReq<?> pageReq, Executor executor) {
        return CompletableFuture.supplyAsync(() -> page(tClass, tableName, pageReq), executor);
    }


    @Override
    public CompletableFuture<PageRsp<NeoMap>> getPageAsync(String tableName, Columns columns, NeoMap searchMap, PageReq<?> pageReq, Executor executor) {
        return CompletableFuture.supplyAsync(() -> getPage(tableName, columns, searchMap, pageReq), executor);
    }

    @Override
    public CompletableFuture<PageRsp<NeoMap>> getPageAsync(String tableName, Columns columns, SearchQuery searchQuery, PageReq<?> pageReq, Executor executor) {
        return CompletableFuture.supplyAsync(() -> getPage(tableName, columns, searchQuery, pageReq), executor);
    }

    @Override
    public <T> CompletableFuture<PageRsp<T>> getPageAsync(String tableName, Columns columns, T entity, PageReq<?> pageReq, Executor executor) {
        return CompletableFuture.supplyAsync(() -> getPage(tableName, columns, entity, pageReq), executor);
    }

    @Override
    public CompletableFuture<PageRsp<NeoMap>> getPageAsync(String tableName, NeoMap searchMap, PageReq<?> pageReq, Executor executor){
        return CompletableFuture.supplyAsync(() -> getPage(tableName, searchMap, pageReq), executor);
    }

    @Override
    public CompletableFuture<PageRsp<NeoMap>> getPageAsync(String tableName, SearchQuery searchQuery, PageReq<?> pageReq, Executor executor) {
        return CompletableFuture.supplyAsync(() -> getPage(tableName, searchQuery, pageReq), executor);
    }

    @Override
    public <T> CompletableFuture<PageRsp<T>> getPageAsync(String tableName, T entity, PageReq<?> pageReq, Executor executor){
        return CompletableFuture.supplyAsync(() -> getPage(tableName, entity, pageReq), executor);
    }

    @Override
    public CompletableFuture<PageRsp<NeoMap>> getPageAsync(String tableName, Columns columns, PageReq<?> pageReq, Executor executor) {
        return CompletableFuture.supplyAsync(() -> getPage(tableName, columns, pageReq), executor);
    }

    @Override
    public CompletableFuture<PageRsp<NeoMap>> getPageAsync(String tableName, PageReq<?> pageReq, Executor executor) {
        return CompletableFuture.supplyAsync(() -> getPage(tableName, pageReq), executor);
    }


    @Override
    public <T> CompletableFuture<PageRsp<T>> getPageAsync(Class<T> tClass, String tableName, Columns columns, NeoMap searchMap, PageReq<?> pageReq, Executor executor) {
        return CompletableFuture.supplyAsync(() -> getPage(tClass, tableName, columns, searchMap, pageReq), executor);
    }

    @Override
    public <T> CompletableFuture<PageRsp<T>> getPageAsync(Class<T> tClass, String tableName, Columns columns, SearchQuery searchQuery, PageReq<?> pageReq, Executor executor) {
        return CompletableFuture.supplyAsync(() -> getPage(tClass, tableName, columns, searchQuery, pageReq), executor);
    }

    @Override
    public <T> CompletableFuture<PageRsp<T>> getPageAsync(Class<T> tClass, String tableName, NeoMap searchMap, PageReq<?> pageReq, Executor executor) {
        return CompletableFuture.supplyAsync(() -> getPage(tClass, tableName, searchMap, pageReq), executor);
    }

    @Override
    public <T> CompletableFuture<PageRsp<T>> getPageAsync(Class<T> tClass, String tableName, SearchQuery searchQuery, PageReq<?> pageReq, Executor executor) {
        return CompletableFuture.supplyAsync(() -> getPage(tClass, tableName, searchQuery, pageReq), executor);
    }

    @Override
    public <T> CompletableFuture<PageRsp<T>> getPageAsync(Class<T> tClass, String tableName, Columns columns, PageReq<?> pageReq, Executor executor) {
        return CompletableFuture.supplyAsync(() -> getPage(tClass, tableName, columns, pageReq), executor);
    }

    @Override
    public <T> CompletableFuture<PageRsp<T>> getPageAsync(Class<T> tClass, String tableName, PageReq<?> pageReq, Executor executor) {
        return CompletableFuture.supplyAsync(() -> getPage(tClass, tableName, pageReq), executor);
    }


    @Override
    public CompletableFuture<Integer> countAsync(String tableName, NeoMap searchMap, Executor executor){
        return CompletableFuture.supplyAsync(() -> count(tableName, searchMap), executor);
    }

    @Override
    public CompletableFuture<Integer> countAsync(String tableName, SearchQuery searchQuery, Executor executor) {
        return CompletableFuture.supplyAsync(() -> count(tableName, searchQuery), executor);
    }

    @Override
    public CompletableFuture<Integer> countAsync(String tableName, Object entity, Executor executor){
        return CompletableFuture.supplyAsync(() -> count(tableName, entity), executor);
    }

    @Override
    public CompletableFuture<Boolean> existAsync(String tableName, NeoMap searchMap, Executor executor) {
        return CompletableFuture.supplyAsync(() -> exist(tableName, searchMap), executor);
    }

    @Override
    public CompletableFuture<Boolean> existAsync(String tableName, SearchQuery searchQuery, Executor executor) {
        return CompletableFuture.supplyAsync(() -> exist(tableName, searchQuery), executor);
    }

    @Override
    public CompletableFuture<Boolean> existAsync(String tableName, Object entity, Executor executor) {
        return CompletableFuture.supplyAsync(() -> exist(tableName, entity), executor);
    }

    @Override
    public CompletableFuture<Boolean> existAsync(String tableName, Number id, Executor executor) {
        return CompletableFuture.supplyAsync(() -> exist(tableName, id), executor);
    }

    @Override
    public CompletableFuture<Integer> countAsync(String tableName, Executor executor){
        return CompletableFuture.supplyAsync(() -> count(tableName), executor);
    }

    @Override
    public <T> T one(Class<T> tClass, String tableName, Columns columns, NeoMap searchMap) {
        NeoMap data = one(tableName, columns, searchMap);
        if(null == data){
            return null;
        }
        return data.as(tClass);
    }

    @Override
    public <T> T one(Class<T> tClass, String tableName, Columns columns, Number key) {
        NeoMap data = one(tableName, columns, key);
        if(null == data){
            return null;
        }
        return data.as(tClass);
    }

    @Override
    public <T> T one(Class<T> tClass, String tableName, NeoMap searchMap) {
        NeoMap data = one(tableName, searchMap);
        if(null == data){
            return null;
        }
        return data.as(tClass);
    }

    @Override
    public <T> T one(Class<T> tClass, String tableName, Number id) {
        NeoMap data = one(tableName, id);
        if(null == data){
            return null;
        }
        return data.as(tClass);
    }
}
