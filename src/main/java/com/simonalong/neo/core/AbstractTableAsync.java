package com.simonalong.neo.core;

import com.simonalong.neo.Columns;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.db.NeoPage;
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
    public CompletableFuture<List<NeoMap>> listAsync(Columns columns, NeoMap searchMap) {
        return listAsync(columns, searchMap, getExecutor());
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
    public <T> CompletableFuture<List<T>> listAsync(T entity) {
        return listAsync(entity, getExecutor());
    }

    @Override
    public <T> CompletableFuture<List<T>> valuesAsync(Class<T> tClass, String field, NeoMap searchMap) {
        return valuesAsync(tClass, field, searchMap, getExecutor());
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
    public CompletableFuture<List<String>> valuesAsync(String field, Object entity) {
        return valuesAsync(field, entity, getExecutor());
    }

    @Override
    public CompletableFuture<List<String>> valuesAsync(String field) {
        return valuesAsync(field, getExecutor());
    }

    @Override
    public <T> CompletableFuture<T> valueAsync(Class<T> tClass, String field, NeoMap searchMap) {
        return valueAsync(tClass, field, searchMap, getExecutor());
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
    public CompletableFuture<String> valueAsync(String field, Object entity) {
        return valueAsync(field, entity, getExecutor());
    }

    @Override
    public CompletableFuture<List<NeoMap>> pageAsync(Columns columns, NeoMap searchMap, NeoPage page) {
        return pageAsync(columns, searchMap, page, getExecutor());
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
    public CompletableFuture<Integer> countAsync(NeoMap searchMap) {
        return countAsync(searchMap, getExecutor());
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
