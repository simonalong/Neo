package com.simonalong.neo.core;

import com.simonalong.neo.Columns;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.db.NeoPage;
import com.simonalong.neo.express.SearchQuery;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 填充线程池参数
 *
 * @author zhouzhenyong
 * @since 2019/8/18 下午4:05
 */
public abstract class AbstractTableAsync implements TableAsync {

    @Override
    public CompletableFuture<NeoMap> insertAsync(NeoMap dataMap) {
        return insertAsync(dataMap, getExecutor());
    }

    @Override
    public <T> CompletableFuture<T> insertAsync(T object) {
        return insertAsync(object, getExecutor());
    }

    @Override
    public CompletableFuture<NeoMap> insertOfUnExistAsync(NeoMap dataMap, String... searchColumnKey) {
        return insertOfUnExistAsync(dataMap, getExecutor(), searchColumnKey);
    }

    @Override
    public <T> CompletableFuture<T> insertOfUnExistAsync(T object, String... searchColumnKey) {
        return insertOfUnExistAsync(object, getExecutor(), searchColumnKey);
    }

    @Override
    public CompletableFuture<NeoMap> saveAsync(NeoMap dataMap, String... searchColumnKey) {
        return saveAsync(dataMap, getExecutor(), searchColumnKey);
    }

    @Override
    public <T> CompletableFuture<T> saveAsync(T object, String... searchColumnKey) {
        return saveAsync(object, getExecutor(), searchColumnKey);
    }

    @Override
    public CompletableFuture<Integer> deleteAsync(NeoMap dataMap) {
        return deleteAsync(dataMap, getExecutor());
    }

    @Override
    public <T> CompletableFuture<Integer> deleteAsync(T object) {
        return deleteAsync(object, getExecutor());
    }

    @Override
    public CompletableFuture<Integer> deleteAsync(Number id) {
        return deleteAsync(id, getExecutor());
    }

    @Override
    public CompletableFuture<NeoMap> updateAsync(NeoMap dataMap, NeoMap searchMap) {
        return updateAsync(dataMap, searchMap, getExecutor());
    }

    @Override
    public CompletableFuture<NeoMap> updateAsync(NeoMap dataMap, SearchQuery searchQuery) {
        return updateAsync(dataMap, searchQuery, getExecutor());
    }

    @Override
    public <T> CompletableFuture<T> updateAsync(T setEntity, NeoMap searchMap) {
        return updateAsync(setEntity, searchMap, getExecutor());
    }

    @Override
    public <T> CompletableFuture<T> updateAsync(T setEntity, T searchEntity) {
        return updateAsync(setEntity, searchEntity, getExecutor());
    }

    @Override
    public CompletableFuture<NeoMap> updateAsync(NeoMap dataMap, Columns columns) {
        return updateAsync(dataMap, columns, getExecutor());
    }

    @Override
    public <T> CompletableFuture<T> updateAsync(T entity, Columns columns) {
        return updateAsync(entity, columns, getExecutor());
    }

    @Override
    public CompletableFuture<NeoMap> updateAsync(NeoMap dataMap) {
        return updateAsync(dataMap, getExecutor());
    }

    @Override
    public <T> CompletableFuture<T> updateAsync(T entity) {
        return updateAsync(entity, getExecutor());
    }

    @Override
    public CompletableFuture<NeoMap> oneAsync(Columns columns, NeoMap searchMap) {
        return oneAsync(columns, searchMap, getExecutor());
    }

    @Override
    public CompletableFuture<NeoMap> oneAsync(Columns columns, SearchQuery searchQuery) {
        return oneAsync(columns, searchQuery, getExecutor());
    }

    @Override
    public <T> CompletableFuture<T> oneAsync(Columns columns, T entity) {
        return oneAsync(columns, entity, getExecutor());
    }

    @Override
    public CompletableFuture<NeoMap> oneAsync(NeoMap searchMap) {
        return oneAsync(searchMap, getExecutor());
    }

    @Override
    public <T> CompletableFuture<T> oneAsync(T entity) {
        return oneAsync(entity, getExecutor());
    }

    @Override
    public CompletableFuture<NeoMap> oneAsync(Number id) {
        return oneAsync(id, getExecutor());
    }

    @Override
    public <T> CompletableFuture<T> oneAsync(Class<T> tClass, Columns columns, NeoMap searchMap) {
        return oneAsync(tClass, columns, searchMap, getExecutor());
    }

    @Override
    public <T> CompletableFuture<T> oneAsync(Class<T> tClass, Columns columns, SearchQuery searchQuery) {
        return oneAsync(tClass, columns, searchQuery, getExecutor());
    }

    @Override
    public <T> CompletableFuture<T> oneAsync(Class<T> tClass, NeoMap searchMap) {
        return oneAsync(tClass, searchMap, getExecutor());
    }

    @Override
    public <T> CompletableFuture<T> oneAsync(Class<T> tClass, SearchQuery searchQuery) {
        return oneAsync(tClass, searchQuery, getExecutor());
    }

    @Override
    public <T> CompletableFuture<T> oneAsync(Class<T> tClass, Number id) {
        return oneAsync(tClass, id, getExecutor());
    }

    @Override
    public CompletableFuture<List<NeoMap>> listAsync(Columns columns, NeoMap searchMap) {
        return listAsync(columns, searchMap, getExecutor());
    }

    @Override
    public CompletableFuture<List<NeoMap>> listAsync(Columns columns, SearchQuery searchQuery) {
        return listAsync(columns, searchQuery, getExecutor());
    }

    @Override
    public <T> CompletableFuture<List<T>> listAsync(Columns columns, T entity) {
        return listAsync(columns, entity, getExecutor());
    }

    @Override
    public CompletableFuture<List<NeoMap>> listAsync(NeoMap searchMap) {
        return listAsync(searchMap, getExecutor());
    }

    @Override
    public CompletableFuture<List<NeoMap>> listAsync(SearchQuery searchQuery) {
        return listAsync(searchQuery, getExecutor());
    }

    @Override
    public <T> CompletableFuture<List<T>> listAsync(T entity) {
        return listAsync(entity, getExecutor());
    }

    @Override
    public <T> CompletableFuture<List<T>> listAsync(Class<T> tClass, Columns columns, NeoMap searchMap) {
        return listAsync(tClass, columns, searchMap, getExecutor());
    }

    @Override
    public <T> CompletableFuture<List<T>> listAsync(Class<T> tClass, Columns columns, SearchQuery searchQuery) {
        return listAsync(tClass, columns, searchQuery, getExecutor());
    }

    @Override
    public <T> CompletableFuture<List<T>> listAsync(Class<T> tClass, NeoMap searchMap) {
        return listAsync(tClass, searchMap, getExecutor());
    }

    @Override
    public <T> CompletableFuture<List<T>> valuesAsync(Class<T> tClass, String field, NeoMap searchMap) {
        return valuesAsync(tClass, field, searchMap, getExecutor());
    }

    @Override
    public <T> CompletableFuture<List<T>> valuesAsync(Class<T> tClass, String field, SearchQuery searchQuery) {
        return valuesAsync(tClass, field, searchQuery, getExecutor());
    }

    @Override
    public <T> CompletableFuture<List<T>> valuesAsync(Class<T> tClass, String field, Object entity) {
        return valuesAsync(tClass, field, entity, getExecutor());
    }

    @Override
    public CompletableFuture<List<String>> valuesAsync(String field, NeoMap searchMap) {
        return valuesAsync(field, searchMap, getExecutor());
    }

    @Override
    public CompletableFuture<List<String>> valuesAsync(String field, SearchQuery searchQuery) {
        return valuesAsync(field, searchQuery, getExecutor());
    }

    @Override
    public CompletableFuture<List<String>> valuesAsync(String field, Object entity) {
        return valuesAsync(field, entity, getExecutor());
    }

    @Override
    public CompletableFuture<List<String>> valuesAsync(String field) {
        return valuesAsync(field, getExecutor());
    }

    @Override
    public <T> CompletableFuture<List<T>> valuesOfDistinctAsync(Class<T> tClass, String field, NeoMap searchMap) {
        return valuesOfDistinctAsync(tClass, field, searchMap, getExecutor());
    }

    @Override
    public <T> CompletableFuture<List<T>> valuesOfDistinctAsync(Class<T> tClass, String field, SearchQuery searchQuery) {
        return valuesOfDistinctAsync(tClass, field, searchQuery, getExecutor());
    }

    @Override
    public <T> CompletableFuture<List<T>> valuesOfDistinctAsync(Class<T> tClass, String field, Object entity) {
        return valuesOfDistinctAsync(tClass, field, entity, getExecutor());
    }

    @Override
    public CompletableFuture<List<String>> valuesOfDistinctAsync(String field, NeoMap searchMap) {
        return valuesOfDistinctAsync(field, searchMap, getExecutor());
    }

    @Override
    public CompletableFuture<List<String>> valuesOfDistinctAsync(String field, SearchQuery searchQuery) {
        return valuesOfDistinctAsync(field, searchQuery, getExecutor());
    }

    @Override
    public CompletableFuture<List<String>> valuesOfDistinctAsync(String field, Object entity) {
        return valuesOfDistinctAsync(field, entity, getExecutor());
    }

    @Override
    public CompletableFuture<List<String>> valuesOfDistinctAsync(String field) {
        return valuesOfDistinctAsync(field, getExecutor());
    }

    @Override
    public <T> CompletableFuture<T> valueAsync(Class<T> tClass, String field, NeoMap searchMap) {
        return valueAsync(tClass, field, searchMap, getExecutor());
    }

    @Override
    public <T> CompletableFuture<T> valueAsync(Class<T> tClass, String field, SearchQuery searchQuery) {
        return valueAsync(tClass, field, searchQuery, getExecutor());
    }

    @Override
    public <T> CompletableFuture<T> valueAsync(Class<T> tClass, String field, Object entity) {
        return valueAsync(tClass, field, entity, getExecutor());
    }

    @Override
    public CompletableFuture<String> valueAsync(String field, NeoMap searchMap) {
        return valueAsync(field, searchMap, getExecutor());
    }

    @Override
    public CompletableFuture<String> valueAsync(String field, SearchQuery searchQuery) {
        return valueAsync(field, searchQuery, getExecutor());
    }

    @Override
    public CompletableFuture<String> valueAsync(String field, Object entity) {
        return valueAsync(field, entity, getExecutor());
    }

    @Override
    public CompletableFuture<List<NeoMap>> pageAsync(Columns columns, NeoMap searchMap, NeoPage page) {
        return pageAsync(columns, searchMap, page, getExecutor());
    }

    @Override
    public CompletableFuture<List<NeoMap>> pageAsync(Columns columns, SearchQuery searchQuery, NeoPage page) {
        return pageAsync(columns, searchQuery, page, getExecutor());
    }

    @Override
    public <T> CompletableFuture<List<T>> pageAsync(Columns columns, T entity, NeoPage page) {
        return pageAsync(columns, entity, page, getExecutor());
    }

    @Override
    public CompletableFuture<List<NeoMap>> pageAsync(NeoMap searchMap, NeoPage page) {
        return pageAsync(searchMap, page, getExecutor());
    }

    @Override
    public CompletableFuture<List<NeoMap>> pageAsync(SearchQuery searchQuery, NeoPage page) {
        return pageAsync(searchQuery, page, getExecutor());
    }

    @Override
    public <T> CompletableFuture<List<T>> pageAsync(T entity, NeoPage page) {
        return pageAsync(entity, page, getExecutor());
    }

    @Override
    public CompletableFuture<List<NeoMap>> pageAsync(Columns columns, NeoPage page) {
        return pageAsync(columns, page, getExecutor());
    }

    @Override
    public CompletableFuture<List<NeoMap>> pageAsync(NeoPage page) {
        return pageAsync(page, getExecutor());
    }

    @Override
    public <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, Columns columns, NeoMap searchMap, NeoPage page) {
        return pageAsync(tClass, columns, searchMap, page, getExecutor());
    }

    @Override
    public <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, Columns columns, SearchQuery searchQuery, NeoPage page) {
        return pageAsync(tClass, columns, searchQuery, page, getExecutor());
    }

    @Override
    public <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, NeoMap searchMap, NeoPage page) {
        return pageAsync(tClass, searchMap, page, getExecutor());
    }

    @Override
    public <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, SearchQuery searchQuery, NeoPage page) {
        return pageAsync(tClass, searchQuery, page, getExecutor());
    }

    @Override
    public <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, Columns columns, NeoPage page) {
        return pageAsync(tClass, columns, page, getExecutor());
    }

    @Override
    public <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, NeoPage page) {
        return pageAsync(tClass, page, getExecutor());
    }

    @Override
    public CompletableFuture<Integer> countAsync(NeoMap searchMap) {
        return countAsync(searchMap, getExecutor());
    }

    @Override
    public CompletableFuture<Integer> countAsync(SearchQuery searchQuery) {
        return countAsync(searchQuery, getExecutor());
    }

    @Override
    public CompletableFuture<Integer> countAsync(Object entity) {
        return countAsync(entity, getExecutor());
    }

    @Override
    public CompletableFuture<Integer> countAsync() {
        return countAsync(getExecutor());
    }


    @Override
    public CompletableFuture<Boolean> existAsync(NeoMap searchMap) {
        return existAsync(searchMap, getExecutor());
    }

    @Override
    public CompletableFuture<Boolean> existAsync(SearchQuery searchQuery) {
        return existAsync(searchQuery, getExecutor());
    }

    @Override
    public CompletableFuture<Boolean> existAsync(Object entity) {
        return existAsync(entity, getExecutor());
    }

    @Override
    public CompletableFuture<Boolean> existAsync(Number id) {
        return existAsync(id, getExecutor());
    }


    @Override
    public CompletableFuture<Integer> batchInsertAsync(List<NeoMap> dataMapList) {
        return batchInsertAsync(dataMapList, getExecutor());
    }

    @Override
    public <T> CompletableFuture<Integer> batchInsertEntityAsync(List<T> dataList) {
        return batchInsertEntityAsync(dataList, getExecutor());
    }


    @Override
    public CompletableFuture<Integer> batchUpdateAsync(List<NeoMap> dataList) {
        return batchUpdateAsync(dataList, getExecutor());
    }

    @Override
    public CompletableFuture<Integer> batchUpdateAsync(List<NeoMap> dataList, Columns columns) {
        return batchUpdateAsync(dataList, columns, getExecutor());
    }

    @Override
    public <T> CompletableFuture<Integer> batchUpdateEntityAsync(List<T> dataList) {
        return batchUpdateEntityAsync(dataList, getExecutor());
    }

    @Override
    public <T> CompletableFuture<Integer> batchUpdateEntityAsync(List<T> dataList, Columns columns) {
        return batchUpdateEntityAsync(dataList, columns, getExecutor());
    }
}
