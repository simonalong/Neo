package com.simonalong.neo.core;

import com.simonalong.neo.Columns;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.db.NeoPage;
import com.simonalong.neo.express.SearchExpress;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 填充默认线程池
 *
 * @author zhouzhenyong
 * @since 2019/8/18 下午5:59
 */
public abstract class AbstractDbAsync implements CqrsAsync{

    @Override
    public CompletableFuture<NeoMap> insertAsync(String tableName, NeoMap dataMap) {
        return insertAsync(tableName, dataMap, getExecutor());
    }

    @Override
    public <T> CompletableFuture<T> insertAsync(String tableName, T object) {
        return insertAsync(tableName, object, getExecutor());
    }

    @Override
    public CompletableFuture<NeoMap> insertOfUnExistAsync(String tableName, NeoMap dataMap, String... searchColumnKey) {
        return insertOfUnExistAsync(tableName, dataMap, getExecutor(), searchColumnKey);
    }

    @Override
    public <T> CompletableFuture<T> insertOfUnExistAsync(String tableName, T object, String... searchColumnKey) {
        return insertOfUnExistAsync(tableName, object, getExecutor(), searchColumnKey);
    }

    @Override
    public CompletableFuture<NeoMap> saveAsync(String tableName, NeoMap dataMap, String... searchColumnKey) {
        return saveAsync(tableName, dataMap, getExecutor(), searchColumnKey);
    }

    @Override
    public <T> CompletableFuture<T> saveAsync(String tableName, T object, String... searchColumnKey) {
        return saveAsync(tableName, object, getExecutor(), searchColumnKey);
    }

    @Override
    public CompletableFuture<Integer> deleteAsync(String tableName, NeoMap dataMap) {
        return deleteAsync(tableName, dataMap, getExecutor());
    }

    @Override
    public CompletableFuture<Integer> deleteAsync(String tableName, SearchExpress searchExpress) {
        return deleteAsync(tableName, searchExpress, getExecutor());
    }

    @Override
    public <T> CompletableFuture<Integer> deleteAsync(String tableName, T object) {
        return deleteAsync(tableName, object, getExecutor());
    }

    @Override
    public CompletableFuture<Integer> deleteAsync(String tableName, Number id) {
        return deleteAsync(tableName, id, getExecutor());
    }

    @Override
    public CompletableFuture<NeoMap> updateAsync(String tableName, NeoMap dataMap, NeoMap searchMap) {
        return updateAsync(tableName, dataMap, searchMap, getExecutor());
    }

    @Override
    public <T> CompletableFuture<T> updateAsync(String tableName, T setEntity, NeoMap searchMap) {
        return updateAsync(tableName, setEntity, searchMap, getExecutor());
    }

    @Override
    public <T> CompletableFuture<T> updateAsync(String tableName, T setEntity, T searchEntity) {
        return updateAsync(tableName, setEntity, searchEntity, getExecutor());
    }

    @Override
    public CompletableFuture<NeoMap> updateAsync(String tableName, NeoMap dataMap, Number id) {
        return updateAsync(tableName, dataMap, id, getExecutor());
    }

    @Override
    public <T> CompletableFuture<T> updateAsync(String tableName, T setEntity, Number id) {
        return updateAsync(tableName, setEntity, id, getExecutor());
    }

    @Override
    public CompletableFuture<NeoMap> updateAsync(String tableName, NeoMap dataMap, Columns columns) {
        return updateAsync(tableName, dataMap, columns, getExecutor());
    }

    @Override
    public <T> CompletableFuture<T> updateAsync(String tableName, T entity, Columns columns) {
        return updateAsync(tableName, entity, columns, getExecutor());
    }

    @Override
    public CompletableFuture<NeoMap> updateAsync(String tableName, NeoMap dataMap) {
        return updateAsync(tableName, dataMap, getExecutor());
    }

    @Override
    public <T> CompletableFuture<T> updateAsync(String tableName, T entity) {
        return updateAsync(tableName, entity, getExecutor());
    }

    @Override
    public CompletableFuture<NeoMap> updateAsync(String tableName, NeoMap dataMap, SearchExpress searchExpress) {
        return updateAsync(tableName, dataMap, searchExpress, getExecutor());
    }

    @Override
    public <T> CompletableFuture<T> updateAsync(String tableName, T setEntity, SearchExpress searchExpress) {
        return updateAsync(tableName, setEntity, searchExpress, getExecutor());
    }


    @Override
    public CompletableFuture<NeoMap> oneAsync(String tableName, Columns columns, NeoMap searchMap) {
        return oneAsync(tableName, columns, searchMap, getExecutor());
    }

    @Override
    public CompletableFuture<NeoMap> oneAsync(String tableName, Columns columns, SearchExpress searchExpress) {
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
    public CompletableFuture<NeoMap> oneAsync(String tableName, SearchExpress searchExpress) {
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
    public <T> CompletableFuture<T> oneAsync(Class<T> tClass, String tableName, Columns columns, SearchExpress searchExpress) {
        return oneAsync(tClass, tableName, columns, searchExpress, getExecutor());
    }

    @Override
    public <T> CompletableFuture<T> oneAsync(Class<T> tClass, String tableName, NeoMap searchMap) {
        return oneAsync(tClass, tableName, searchMap, getExecutor());
    }

    @Override
    public <T> CompletableFuture<T> oneAsync(Class<T> tClass, String tableName, SearchExpress searchExpress) {
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
    public CompletableFuture<List<NeoMap>> listAsync(String tableName, Columns columns, SearchExpress searchExpress) {
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
    public CompletableFuture<List<NeoMap>> listAsync(String tableName, SearchExpress searchExpress) {
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
    public <T> CompletableFuture<List<T>> listAsync(Class<T> tClass, String tableName, Columns columns, SearchExpress searchExpress) {
        return listAsync(tClass, tableName, columns, searchExpress, getExecutor());
    }

    @Override
    public <T> CompletableFuture<List<T>> listAsync(Class<T> tClass, String tableName, NeoMap searchMap) {
        return listAsync(tClass, tableName, searchMap, getExecutor());
    }

    @Override
    public <T> CompletableFuture<List<T>> listAsync(Class<T> tClass, String tableName, SearchExpress searchExpress) {
        return listAsync(tClass, tableName, searchExpress, getExecutor());
    }


    @Override
    public <T> CompletableFuture<List<T>> valuesAsync(String tableName, Class<T> tClass, String field, NeoMap searchMap) {
        return valuesAsync(tableName, tClass, field, searchMap, getExecutor());
    }

    @Override
    public <T> CompletableFuture<List<T>> valuesAsync(String tableName, Class<T> tClass, String field, SearchExpress searchExpress) {
        return valuesAsync(tableName, tClass, field, searchExpress, getExecutor());
    }

    @Override
    public <T> CompletableFuture<List<T>> valuesAsync(String tableName, Class<T> tClass, String field, Object entity) {
        return valuesAsync(tableName, tClass, field, entity, getExecutor());
    }


    @Override
    public <T> CompletableFuture<List<T>> valuesAsync(Class<T> tClass, String tableName, String field, NeoMap searchMap) {
        return valuesAsync(tClass, tableName, field, searchMap, getExecutor());
    }

    @Override
    public <T> CompletableFuture<List<T>> valuesAsync(Class<T> tClass, String tableName, String field, SearchExpress searchExpress) {
        return valuesAsync(tClass, tableName, field, getExecutor());
    }

    @Override
    public <T> CompletableFuture<List<T>> valuesAsync(Class<T> tClass, String tableName, String field, Object entity) {
        return valuesAsync(tClass, tableName, field, entity, getExecutor());
    }


    @Override
    public CompletableFuture<List<String>> valuesAsync(String tableName, String field, NeoMap searchMap) {
        return valuesAsync(tableName, field, searchMap, getExecutor());
    }

    @Override
    public CompletableFuture<List<String>> valuesAsync(String tableName, String field, SearchExpress searchExpress) {
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
    public <T> CompletableFuture<List<T>> valuesOfDistinctAsync(String tableName, Class<T> tClass, String field, SearchExpress searchExpress) {
        return valuesOfDistinctAsync(tableName, tClass, field, searchExpress, getExecutor());
    }

    @Override
    public <T> CompletableFuture<List<T>> valuesOfDistinctAsync(String tableName, Class<T> tClass, String field, Object entity) {
        return valuesOfDistinctAsync(tableName, tClass, field, entity, getExecutor());
    }


    @Override
    public <T> CompletableFuture<List<T>> valuesOfDistinctAsync(Class<T> tClass, String tableName, String field, NeoMap searchMap) {
        return valuesOfDistinctAsync(tClass, tableName, field, searchMap, getExecutor());
    }

    @Override
    public <T> CompletableFuture<List<T>> valuesOfDistinctAsync(Class<T> tClass, String tableName, String field, SearchExpress searchExpress) {
        return valuesOfDistinctAsync(tClass, tableName, field, searchExpress, getExecutor());
    }

    @Override
    public <T> CompletableFuture<List<T>> valuesOfDistinctAsync(Class<T> tClass, String tableName, String field, Object entity) {
        return valuesOfDistinctAsync(tClass, tableName, field, entity, getExecutor());
    }


    @Override
    public CompletableFuture<List<String>> valuesOfDistinctAsync(String tableName, String field, NeoMap searchMap) {
        return valuesOfDistinctAsync(tableName, field, searchMap, getExecutor());
    }

    @Override
    public CompletableFuture<List<String>> valuesOfDistinctAsync(String tableName, String field, SearchExpress searchExpress) {
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
    public <T> CompletableFuture<T> valueAsync(String tableName, Class<T> tClass, String field, SearchExpress searchExpress) {
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
    public CompletableFuture<String> valueAsync(String tableName, String field, SearchExpress searchExpress) {
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
    public CompletableFuture<List<NeoMap>> pageAsync(String tableName, Columns columns, SearchExpress searchExpress, NeoPage page) {
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
    public CompletableFuture<List<NeoMap>> pageAsync(String tableName, SearchExpress searchExpress, NeoPage page) {
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
    public <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, Columns columns, SearchExpress searchExpress, NeoPage page) {
        return pageAsync(tClass, tableName, columns, searchExpress, page, getExecutor());
    }

    @Override
    public <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, NeoMap searchMap, NeoPage page) {
        return pageAsync(tClass, tableName, searchMap, page, getExecutor());
    }

    @Override
    public <T> CompletableFuture<List<T>> pageAsync(Class<T> tClass, String tableName, SearchExpress searchExpress, NeoPage page) {
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
    public CompletableFuture<Integer> countAsync(String tableName, SearchExpress searchExpress) {
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
    public CompletableFuture<Boolean> existAsync(String tableName, SearchExpress searchExpress) {
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


    @Override
    public CompletableFuture<Integer> batchInsertAsync(String tableName, List<NeoMap> dataMapList) {
        return batchInsertAsync(tableName, dataMapList, getExecutor());
    }

    @Override
    public <T> CompletableFuture<Integer> batchInsertEntityAsync(String tableName, List<T> dataList) {
        return batchInsertEntityAsync(tableName, dataList, getExecutor());
    }


    @Override
    public CompletableFuture<Integer> batchUpdateAsync(String tableName, List<NeoMap> dataList) {
        return batchUpdateAsync(tableName, dataList, getExecutor());
    }

    @Override
    public CompletableFuture<Integer> batchUpdateAsync(String tableName, List<NeoMap> dataList, Columns columns) {
        return batchUpdateAsync(tableName, dataList, getExecutor());
    }

    @Override
    public <T> CompletableFuture<Integer> batchUpdateEntityAsync(String tableName, List<T> dataList) {
        return batchUpdateEntityAsync(tableName, dataList, getExecutor());
    }

    @Override
    public <T> CompletableFuture<Integer> batchUpdateEntityAsync(String tableName, List<T> dataList, Columns columns) {
        return batchUpdateEntityAsync(tableName, dataList, columns, getExecutor());
    }
}
