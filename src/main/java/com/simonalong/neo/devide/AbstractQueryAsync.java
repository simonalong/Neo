package com.simonalong.neo.devide;

import com.simonalong.neo.Columns;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.core.QueryAsync;
import com.simonalong.neo.db.NeoPage;
import com.simonalong.neo.express.Express;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author shizi
 * @since 2020/6/11 3:09 PM
 */
public abstract class AbstractQueryAsync implements QueryAsync {


    @Override
    public CompletableFuture<NeoMap> oneAsync(String tableName, Columns columns, NeoMap searchMap) {
        return oneAsync(tableName, columns, searchMap, getExecutor());
    }

    @Override
    public CompletableFuture<NeoMap> oneAsync(String tableName, Columns columns, Express searchExpress) {
        return oneAsync(tableName, columns, searchExpress, getExecutor());
    }

    @Override
    public <T> CompletableFuture<T> oneAsync(String tableName, Columns columns, T entity) {
        return oneAsync(tableName, columns, entity, getExecutor());
    }

    @Override
    public CompletableFuture<NeoMap> oneAsync(String tableName, NeoMap searchMap) {
        return oneAsync(tableName, searchMap, getExecutor());
    }

    @Override
    public CompletableFuture<NeoMap> oneAsync(String tableName, Express searchExpress) {
        return oneAsync(tableName, searchExpress, getExecutor());
    }

    @Override
    public <T> CompletableFuture<T> oneAsync(String tableName, T entity) {
        return oneAsync(tableName, entity, getExecutor());
    }

    @Override
    public CompletableFuture<NeoMap> oneAsync(String tableName, Number id) {
        return oneAsync(tableName, id, getExecutor());
    }

    @Override
    public <T> CompletableFuture<T> oneAsync(Class<T> tClass, String tableName, Columns columns, NeoMap searchMap) {
        return oneAsync(tClass, tableName, columns, searchMap, getExecutor());
    }

    @Override
    public <T> CompletableFuture<T> oneAsync(Class<T> tClass, String tableName, Columns columns, Express searchExpress) {
        return oneAsync(tClass, tableName, columns, searchExpress, getExecutor());
    }

    @Override
    public <T> CompletableFuture<T> oneAsync(Class<T> tClass, String tableName, NeoMap searchMap) {
        return oneAsync(tClass, tableName, searchMap, getExecutor());
    }

    @Override
    public <T> CompletableFuture<T> oneAsync(Class<T> tClass, String tableName, Express searchExpress) {
        return oneAsync(tClass, tableName, searchExpress, getExecutor());
    }

    @Override
    public <T> CompletableFuture<T> oneAsync(Class<T> tClass, String tableName, Number id) {
        return oneAsync(tClass, tableName, id, getExecutor());
    }


    @Override
    public CompletableFuture<List<NeoMap>> listAsync(String tableName, Columns columns, NeoMap searchMap) {
        return listAsync(tableName, columns, searchMap, getExecutor());
    }

    @Override
    public CompletableFuture<List<NeoMap>> listAsync(String tableName, Columns columns, Express searchExpress) {
        return listAsync(tableName, columns, searchExpress, getExecutor());
    }

    @Override
    public <T> CompletableFuture<List<T>> listAsync(String tableName, Columns columns, T entity) {
        return listAsync(tableName, columns, entity, getExecutor());
    }

    @Override
    public CompletableFuture<List<NeoMap>> listAsync(String tableName, NeoMap searchMap) {
        return listAsync(tableName, searchMap, getExecutor());
    }

    @Override
    public CompletableFuture<List<NeoMap>> listAsync(String tableName, Express searchExpress) {
        return listAsync(tableName, searchExpress, getExecutor());
    }

    @Override
    public <T> CompletableFuture<List<T>> listAsync(String tableName, T entity) {
        return listAsync(tableName, entity, getExecutor());
    }


    @Override
    public <T> CompletableFuture<List<T>> listAsync(Class<T> tClass, String tableName, Columns columns, NeoMap searchMap) {
        return listAsync(tClass, tableName, columns, searchMap, getExecutor());
    }

    @Override
    public <T> CompletableFuture<List<T>> listAsync(Class<T> tClass, String tableName, Columns columns, Express searchExpress) {
        return listAsync(tClass, tableName, columns, searchExpress, getExecutor());
    }

    @Override
    public <T> CompletableFuture<List<T>> listAsync(Class<T> tClass, String tableName, NeoMap searchMap) {
        return listAsync(tClass, tableName, searchMap, getExecutor());
    }

    @Override
    public <T> CompletableFuture<List<T>> listAsync(Class<T> tClass, String tableName, Express searchExpress) {
        return listAsync(tClass, tableName, searchExpress, getExecutor());
    }

    @Override
    public <T> CompletableFuture<List<T>> valuesAsync(String tableName, Class<T> tClass, String field, NeoMap searchMap) {
        return valuesAsync(tableName, tClass, field, searchMap, getExecutor());
    }

    @Override
    public <T> CompletableFuture<List<T>> valuesAsync(String tableName, Class<T> tClass, String field, Express searchExpress) {
        return valuesAsync(tableName, tClass, field, searchExpress, getExecutor());
    }

    @Override
    public <T> CompletableFuture<List<T>> valuesAsync(String tableName, Class<T> tClass, String field, Object entity) {
        return valuesAsync(tableName, tClass, field, entity, getExecutor());
    }

    @Override
    public CompletableFuture<List<String>> valuesAsync(String tableName, String field, NeoMap searchMap) {
        return valuesAsync(tableName, field, searchMap, getExecutor());
    }

    @Override
    public CompletableFuture<List<String>> valuesAsync(String tableName, String field, Express searchExpress) {
        return valuesAsync(tableName, field, searchExpress, getExecutor());
    }

    @Override
    public CompletableFuture<List<String>> valuesAsync(String tableName, String field, Object entity) {
        return valuesAsync(tableName, field, entity, getExecutor());
    }

    @Override
    public CompletableFuture<List<String>> valuesAsync(String tableName, String field) {
        return valuesAsync(tableName, field, getExecutor());
    }

    @Override
    public <T> CompletableFuture<List<T>> valuesOfDistinctAsync(String tableName, Class<T> tClass, String field, NeoMap searchMap) {
        return valuesOfDistinctAsync(tableName, tClass, field, searchMap, getExecutor());
    }

    @Override
    public <T> CompletableFuture<List<T>> valuesOfDistinctAsync(String tableName, Class<T> tClass, String field, Express searchExpress) {
        return valuesOfDistinctAsync(tableName, tClass, field, searchExpress, getExecutor());
    }

    @Override
    public <T> CompletableFuture<List<T>> valuesOfDistinctAsync(String tableName, Class<T> tClass, String field, Object entity) {
        return valuesOfDistinctAsync(tableName, tClass, field, entity, getExecutor());
    }

    @Override
    public CompletableFuture<List<String>> valuesOfDistinctAsync(String tableName, String field, NeoMap searchMap) {
        return valuesOfDistinctAsync(tableName, field, searchMap, getExecutor());
    }

    @Override
    public CompletableFuture<List<String>> valuesOfDistinctAsync(String tableName, String field, Express searchExpress) {
        return valuesOfDistinctAsync(tableName, field, searchExpress, getExecutor());
    }

    @Override
    public CompletableFuture<List<String>> valuesOfDistinctAsync(String tableName, String field, Object entity) {
        return valuesOfDistinctAsync(tableName, field, entity, getExecutor());
    }

    @Override
    public CompletableFuture<List<String>> valuesOfDistinctAsync(String tableName, String field) {
        return valuesOfDistinctAsync(tableName, field, getExecutor());
    }

    @Override
    public <T> CompletableFuture<T> valueAsync(String tableName, Class<T> tClass, String field, NeoMap searchMap) {
        return valueAsync(tableName, tClass, field, searchMap, getExecutor());
    }

    @Override
    public <T> CompletableFuture<T> valueAsync(String tableName, Class<T> tClass, String field, Express searchExpress) {
        return valueAsync(tableName, tClass, field, searchExpress, getExecutor());
    }

    @Override
    public <T> CompletableFuture<T> valueAsync(String tableName, Class<T> tClass, String field, Object entity) {
        return valueAsync(tableName, tClass, field, entity, getExecutor());
    }

    @Override
    public CompletableFuture<String> valueAsync(String tableName, String field, NeoMap searchMap) {
        return valueAsync(tableName, field, searchMap, getExecutor());
    }

    @Override
    public CompletableFuture<String> valueAsync(String tableName, String field, Express searchExpress) {
        return valueAsync(tableName, field, searchExpress, getExecutor());
    }

    @Override
    public CompletableFuture<String> valueAsync(String tableName, String field, Object entity) {
        return valueAsync(tableName, field, entity, getExecutor());
    }

    @Override
    public CompletableFuture<List<NeoMap>> pageAsync(String tableName, Columns columns, NeoMap searchMap, NeoPage page) {
        return pageAsync(tableName, columns, searchMap, page, getExecutor());
    }

    @Override
    public CompletableFuture<List<NeoMap>> pageAsync(String tableName, Columns columns, Express searchExpress, NeoPage page) {
        return pageAsync(tableName, columns, searchExpress, page, getExecutor());
    }

    @Override
    public <T> CompletableFuture<List<T>> pageAsync(String tableName, Columns columns, T entity, NeoPage page) {
        return pageAsync(tableName, columns, entity, page, getExecutor());
    }

    @Override
    public CompletableFuture<List<NeoMap>> pageAsync(String tableName, NeoMap searchMap, NeoPage page) {
        return pageAsync(tableName, searchMap, page, getExecutor());
    }

    @Override
    public CompletableFuture<List<NeoMap>> pageAsync(String tableName, Express searchExpress, NeoPage page) {
        return pageAsync(tableName, searchExpress, page, getExecutor());
    }

    @Override
    public <T> CompletableFuture<List<T>> pageAsync(String tableName, T entity, NeoPage page) {
        return pageAsync(tableName, entity, page, getExecutor());
    }

    @Override
    public CompletableFuture<List<NeoMap>> pageAsync(String tableName, Columns columns, NeoPage page) {
        return pageAsync(tableName, columns, page, getExecutor());
    }

    @Override
    public CompletableFuture<List<NeoMap>> pageAsync(String tableName, NeoPage page) {
        return pageAsync(tableName, page, getExecutor());
    }

    @Override
    public <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, Columns columns, NeoMap searchMap, NeoPage page) {
        return pageAsync(tClass, tableName, columns, searchMap, page, getExecutor());
    }

    @Override
    public <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, Columns columns, Express searchExpress, NeoPage page) {
        return pageAsync(tClass, tableName, columns, searchExpress, page, getExecutor());
    }

    @Override
    public <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, NeoMap searchMap, NeoPage page) {
        return pageAsync(tClass, tableName, searchMap, page, getExecutor());
    }

    @Override
    public <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, Express searchExpress, NeoPage page) {
        return pageAsync(tClass, tableName, searchExpress, page, getExecutor());
    }

    @Override
    public <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, Columns columns, NeoPage page) {
        return pageAsync(tClass, tableName, columns, page, getExecutor());
    }

    @Override
    public <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, NeoPage page) {
        return pageAsync(tClass, tableName, page, getExecutor());
    }

    @Override
    public CompletableFuture<Integer> countAsync(String tableName, NeoMap searchMap) {
        return countAsync(tableName, searchMap, getExecutor());
    }

    @Override
    public CompletableFuture<Integer> countAsync(String tableName, Express searchExpress) {
        return countAsync(tableName, searchExpress, getExecutor());
    }

    @Override
    public CompletableFuture<Integer> countAsync(String tableName, Object entity) {
        return countAsync(tableName, entity, getExecutor());
    }

    @Override
    public CompletableFuture<Integer> countAsync(String tableName) {
        return countAsync(tableName, getExecutor());
    }

    @Override
    public CompletableFuture<Boolean> existAsync(String tableName, NeoMap searchMap){
        return existAsync(tableName, searchMap, getExecutor());
    }

    @Override
    public CompletableFuture<Boolean> existAsync(String tableName, Express searchExpress) {
        return existAsync(tableName, searchExpress, getExecutor());
    }

    @Override
    public CompletableFuture<Boolean> existAsync(String tableName, Object entity){
        return existAsync(tableName, entity, getExecutor());
    }

    @Override
    public CompletableFuture<Boolean> existAsync(String tableName, Number id) {
        return existAsync(tableName, id, getExecutor());
    }
}
