package com.simonalong.neo.core;

import com.simonalong.neo.Columns;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.db.NeoPage;
import com.simonalong.neo.express.SearchQuery;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * <ul>
 * <li>1.提供默认的线程池</li>
 * <li>2.异步接口实现，通过调用同步函数完成</li>
 *</ul>
 *
 * @author zhouzhenyong
 * @since 2019-08-17 17:17
 */
public abstract class AbstractBaseDb extends AbstractDbAsync implements DbSync {

    @Override
    public Executor getExecutor() {
        return DefaultExecutor.getInstance().getExecutor();
    }

    @Override
    public CompletableFuture<NeoMap> insertAsync(String tableName, NeoMap dataMap, Executor executor){
        return CompletableFuture.supplyAsync(() -> insert(tableName, dataMap), executor);
    }

    @Override
    public CompletableFuture<NeoMap> insertOfUnExistAsync(String tableName, NeoMap dataMap, Executor executor, String... searchColumnKey) {
        return CompletableFuture.supplyAsync(() -> insertOfUnExist(tableName, dataMap, searchColumnKey), executor);
    }

    @Override
    public <T> CompletableFuture<T> insertOfUnExistAsync(String tableName, T object, Executor executor, String... searchColumnKey) {
        return CompletableFuture.supplyAsync(() -> insertOfUnExist(tableName, object, searchColumnKey), executor);
    }

    @Override
    public CompletableFuture<NeoMap> saveAsync(String tableName, NeoMap dataMap, Executor executor, String... searchColumnKey) {
        return CompletableFuture.supplyAsync(() -> save(tableName, dataMap, searchColumnKey), executor);
    }

    @Override
    public <T> CompletableFuture<T> saveAsync(String tableName, T object, Executor executor, String... searchColumnKey) {
        return CompletableFuture.supplyAsync(() -> save(tableName, object, searchColumnKey), executor);
    }

    @Override
    public <T> CompletableFuture<T> insertAsync(String tableName, T object, Executor executor){
        return CompletableFuture.supplyAsync(() -> insert(tableName, object), executor);
    }

    @Override
    public CompletableFuture<Integer> deleteAsync(String tableName, NeoMap dataMap, Executor executor){
        return CompletableFuture.supplyAsync(() -> delete(tableName, dataMap), executor);
    }

    @Override
    public CompletableFuture<Integer> deleteAsync(String tableName, SearchQuery searchQuery, Executor executor) {
        return CompletableFuture.supplyAsync(() -> delete(tableName, searchQuery), executor);
    }

    @Override
    public <T> CompletableFuture<Integer> deleteAsync(String tableName, T object, Executor executor){
        return CompletableFuture.supplyAsync(() -> delete(tableName, object), executor);
    }

    @Override
    public CompletableFuture<Integer> deleteAsync(String tableName, Number id, Executor executor){
        return CompletableFuture.supplyAsync(() -> delete(tableName, id), executor);
    }

    @Override
    public CompletableFuture<NeoMap> updateAsync(String tableName, NeoMap dataMap, NeoMap searchMap, Executor executor){
        return CompletableFuture.supplyAsync(() -> update(tableName, dataMap, searchMap), executor);
    }

    @Override
    public <T> CompletableFuture<T> updateAsync(String tableName, T setEntity, NeoMap searchMap, Executor executor){
        return CompletableFuture.supplyAsync(() -> update(tableName, setEntity, searchMap), executor);
    }

    @Override
    public <T> CompletableFuture<T> updateAsync(String tableName, T setEntity, T searchEntity, Executor executor){
        return CompletableFuture.supplyAsync(() -> update(tableName, setEntity, searchEntity), executor);
    }

    @Override
    public CompletableFuture<NeoMap> updateAsync(String tableName, NeoMap dataMap, Number id, Executor executor) {
        return CompletableFuture.supplyAsync(() -> update(tableName, dataMap, id), executor);
    }

    @Override
    public <T> CompletableFuture<T> updateAsync(String tableName, T setEntity, Number id, Executor executor) {
        return CompletableFuture.supplyAsync(() -> update(tableName, setEntity, id), executor);
    }

    @Override
    public CompletableFuture<NeoMap> updateAsync(String tableName, NeoMap dataMap, Columns columns, Executor executor){
        return CompletableFuture.supplyAsync(() -> update(tableName, dataMap, columns), executor);
    }

    @Override
    public <T> CompletableFuture<T> updateAsync(String tableName, T entity, Columns columns, Executor executor){
        return CompletableFuture.supplyAsync(() -> update(tableName, entity, columns), executor);
    }

    @Override
    public CompletableFuture<NeoMap> updateAsync(String tableName, NeoMap dataMap, Executor executor){
        return CompletableFuture.supplyAsync(() -> update(tableName, dataMap), executor);
    }

    @Override
    public <T> CompletableFuture<T> updateAsync(String tableName, T entity, Executor executor){
        return CompletableFuture.supplyAsync(() -> update(tableName, entity), executor);
    }

    @Override
    public CompletableFuture<NeoMap> updateAsync(String tableName, NeoMap dataMap, SearchQuery searchQuery, Executor executor) {
        return CompletableFuture.supplyAsync(() -> update(tableName, dataMap, searchQuery), executor);
    }

    @Override
    public <T> CompletableFuture<T> updateAsync(String tableName, T setEntity, SearchQuery searchQuery, Executor executor) {
        return CompletableFuture.supplyAsync(() -> update(tableName, setEntity, searchQuery), executor);
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
    public CompletableFuture<List<String>> valuesAsync(String tableName, String field, NeoMap searchMap, Executor executor){
        return CompletableFuture.supplyAsync(() -> values(tableName, field, searchMap), executor);
    }

    @Override
    public CompletableFuture<List<String>> valuesAsync(String tableName, String field, SearchQuery searchQuery, Executor executor) {
        return CompletableFuture.supplyAsync(() -> values(tableName, field, searchQuery), executor);
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
    public CompletableFuture<List<NeoMap>> pageAsync(String tableName, Columns columns, NeoMap searchMap, NeoPage page, Executor executor){
        return CompletableFuture.supplyAsync(() -> page(tableName, columns, searchMap, page), executor);
    }

    @Override
    public CompletableFuture<List<NeoMap>> pageAsync(String tableName, Columns columns, SearchQuery searchQuery, NeoPage page, Executor executor) {
        return CompletableFuture.supplyAsync(() -> page(tableName, columns, searchQuery, page), executor);
    }

    @Override
    public <T> CompletableFuture<List<T>> pageAsync(String tableName, Columns columns, T entity, NeoPage page, Executor executor){
        return CompletableFuture.supplyAsync(() -> page(tableName, columns, entity, page), executor);
    }

    @Override
    public CompletableFuture<List<NeoMap>> pageAsync(String tableName, NeoMap searchMap, NeoPage page, Executor executor){
        return CompletableFuture.supplyAsync(() -> page(tableName, searchMap, page), executor);
    }

    @Override
    public CompletableFuture<List<NeoMap>> pageAsync(String tableName, SearchQuery searchQuery, NeoPage page, Executor executor) {
        return CompletableFuture.supplyAsync(() -> page(tableName, searchQuery, page), executor);
    }

    @Override
    public <T> CompletableFuture<List<T>> pageAsync(String tableName, T entity, NeoPage page, Executor executor){
        return CompletableFuture.supplyAsync(() -> page(tableName, entity, page), executor);
    }

    @Override
    public CompletableFuture<List<NeoMap>> pageAsync(String tableName, Columns columns, NeoPage page, Executor executor){
        return CompletableFuture.supplyAsync(() -> page(tableName, columns, page), executor);
    }

    @Override
    public CompletableFuture<List<NeoMap>> pageAsync(String tableName, NeoPage page, Executor executor){
        return CompletableFuture.supplyAsync(() -> page(tableName, page), executor);
    }

    @Override
    public <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, Columns columns, NeoMap searchMap, NeoPage page, Executor executor) {
        return CompletableFuture.supplyAsync(() -> page(tClass, tableName, columns, searchMap, page), executor);
    }

    @Override
    public <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, Columns columns, SearchQuery searchQuery, NeoPage page, Executor executor) {
        return CompletableFuture.supplyAsync(() -> page(tClass, tableName, columns, searchQuery, page), executor);
    }

    @Override
    public <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, NeoMap searchMap, NeoPage page, Executor executor) {
        return CompletableFuture.supplyAsync(() -> page(tClass, tableName, searchMap, page), executor);
    }

    @Override
    public <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, SearchQuery searchQuery, NeoPage page, Executor executor) {
        return CompletableFuture.supplyAsync(() -> page(tClass, tableName, searchQuery, page), executor);
    }

    @Override
    public <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, Columns columns, NeoPage page, Executor executor) {
        return CompletableFuture.supplyAsync(() -> page(tClass, tableName, columns, page), executor);
    }

    @Override
    public <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, NeoPage page, Executor executor) {
        return CompletableFuture.supplyAsync(() -> page(tClass, tableName, page), executor);
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
    public CompletableFuture<Integer> batchInsertAsync(String tableName, List<NeoMap> dataMapList, Executor executor){
        return CompletableFuture.supplyAsync(() -> batchInsert(tableName, dataMapList), executor);
    }

    @Override
    public <T> CompletableFuture<Integer> batchInsertEntityAsync(String tableName, List<T> dataList, Executor executor){
        return CompletableFuture.supplyAsync(() -> batchInsertEntity(tableName, dataList), executor);
    }

    @Override
    public CompletableFuture<Integer> batchUpdateAsync(String tableName, List<NeoMap> dataList, Executor executor){
        return CompletableFuture.supplyAsync(() -> batchUpdate(tableName, dataList), executor);
    }

    @Override
    public CompletableFuture<Integer> batchUpdateAsync(String tableName, List<NeoMap> dataList, Columns columns, Executor executor){
        return CompletableFuture.supplyAsync(() -> batchUpdate(tableName, dataList, columns), executor);
    }

    @Override
    public <T> CompletableFuture<Integer> batchUpdateEntityAsync(String tableName, List<T> dataList, Executor executor){
        return CompletableFuture.supplyAsync(() -> batchUpdateEntity(tableName, dataList), executor);
    }

    @Override
    public <T> CompletableFuture<Integer> batchUpdateEntityAsync(String tableName, List<T> dataList, Columns columns, Executor executor){
        return CompletableFuture.supplyAsync(() -> batchUpdateEntity(tableName, dataList, columns), executor);
    }
}
