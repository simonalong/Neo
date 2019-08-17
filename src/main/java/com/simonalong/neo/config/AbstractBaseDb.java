package com.simonalong.neo.config;

import com.simonalong.neo.Columns;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.NeoMap.NamingChg;
import com.simonalong.neo.table.NeoPage;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author zhouzhenyong
 * @since 2019-08-17 17:17
 */
public abstract class AbstractBaseDb extends AbstractBaseNeo implements DbSync, DbAsync, ExeDb{

    private AsyncNeo async = DefaultExecutor.getInstance();

    public CompletableFuture<NeoMap> insertAsync(String tableName, NeoMap dataMap) {
        return insertAsync(tableName, dataMap, async.getExecutor());
    }

    public <T> CompletableFuture<T> insertAsync(String tableName, T object){
        return insertAsync(tableName, object, async.getExecutor());
    }

    public <T> CompletableFuture<T> insertAsync(String tableName, T object, NamingChg naming){
        return insertAsync(tableName, object, naming, async.getExecutor());
    }

    public CompletableFuture<Integer> deleteAsync(String tableName, NeoMap dataMap){
        return deleteAsync(tableName, dataMap, async.getExecutor());
    }

    public <T> CompletableFuture<Integer> deleteAsync(String tableName, T object){
        return deleteAsync(tableName, object, async.getExecutor());
    }


    public <T> CompletableFuture<Integer> deleteAsync(String tableName, T entity, NamingChg naming){
        return deleteAsync(tableName, entity, naming, async.getExecutor());
    }

    public CompletableFuture<Integer> deleteAsync(String tableName, Number id){
        return deleteAsync(tableName, id, async.getExecutor());
    }

    public CompletableFuture<NeoMap> updateAsync(String tableName, NeoMap dataMap, NeoMap searchMap){
        return updateAsync(tableName, dataMap, searchMap, async.getExecutor());
    }

    public <T> CompletableFuture<T> updateAsync(String tableName, T setEntity, NeoMap searchMap, NamingChg namingChg){
        return updateAsync(tableName, setEntity, searchMap, namingChg, async.getExecutor());
    }

    public <T> CompletableFuture<T> updateAsync(String tableName, T setEntity, NeoMap searchMap){
        return updateAsync(tableName, setEntity, searchMap, async.getExecutor());
    }

    public <T> CompletableFuture<T> updateAsync(String tableName, T setEntity, T searchEntity){
        return updateAsync(tableName, setEntity, searchEntity, async.getExecutor());
    }

    public CompletableFuture<NeoMap> updateAsync(String tableName, NeoMap dataMap, Columns columns){
        return updateAsync(tableName, dataMap, columns, async.getExecutor());
    }

    public <T> CompletableFuture<T> updateAsync(String tableName, T entity, Columns columns, NamingChg namingChg){
        return updateAsync(tableName, entity, columns, namingChg, async.getExecutor());
    }

    public <T> CompletableFuture<T> updateAsync(String tableName, T entity, Columns columns){
        return updateAsync(tableName, entity, columns, async.getExecutor());
    }

    public CompletableFuture<NeoMap> updateAsync(String tableName, NeoMap dataMap){
        return updateAsync(tableName, dataMap, async.getExecutor());
    }

    public <T> CompletableFuture<T> updateAsync(String tableName, T entity){
        return updateAsync(tableName, entity, async.getExecutor());
    }

    public CompletableFuture<NeoMap> oneAsync(String tableName, Columns columns, NeoMap searchMap){
        return oneAsync(tableName, columns, searchMap, async.getExecutor());
    }

    public <T> CompletableFuture<T> oneAsync(String tableName, Columns columns, T entity){
        return oneAsync(tableName, columns, entity, async.getExecutor());
    }

    public CompletableFuture<NeoMap> oneAsync(String tableName, NeoMap searchMap){
        return oneAsync(tableName, searchMap, async.getExecutor());
    }

    public <T> CompletableFuture<T> oneAsync(String tableName, T entity){
        return oneAsync(tableName, entity, async.getExecutor());
    }

    public CompletableFuture<NeoMap> oneAsync(String tableName, Number id){
        return oneAsync(tableName, id, async.getExecutor());
    }

    public CompletableFuture<List<NeoMap>> listAsync(String tableName, Columns columns, NeoMap searchMap){
        return listAsync(tableName, columns, searchMap, async.getExecutor());
    }


    public <T> CompletableFuture<List<T>> listAsync(String tableName, Columns columns, T entity){
        return listAsync(tableName, columns, entity, async.getExecutor());
    }

    public CompletableFuture<List<NeoMap>> listAsync(String tableName, NeoMap searchMap){
        return listAsync(tableName, searchMap, async.getExecutor());
    }

    public <T> CompletableFuture<List<T>> listAsync(String tableName, T entity){
        return listAsync(tableName, entity, async.getExecutor());
    }

    public <T> CompletableFuture<List<T>> valuesAsync(String tableName, Class<T> tClass, String field, NeoMap searchMap){
        return valuesAsync(tableName, tClass, field, searchMap, async.getExecutor());
    }

    public <T> CompletableFuture<List<T>> valuesAsync(String tableName, Class<T> tClass, String field, Object entity){
        return valuesAsync(tableName, tClass, field, entity, async.getExecutor());
    }

    public CompletableFuture<List<String>> valuesAsync(String tableName, String field, NeoMap searchMap){
        return valuesAsync(tableName, field, searchMap, async.getExecutor());
    }

    public CompletableFuture<List<String>> valuesAsync(String tableName, String field, Object entity){
        return valuesAsync(tableName, field, entity, async.getExecutor());
    }

    public CompletableFuture<List<String>> valuesAsync(String tableName, String field){
        return valuesAsync(tableName, field, async.getExecutor());
    }

    public <T> CompletableFuture<T> valueAsync(String tableName, Class<T> tClass, String field, NeoMap searchMap){
        return valueAsync(tableName, tClass, field, searchMap, async.getExecutor());
    }

    public <T> CompletableFuture<T> valueAsync(String tableName, Class<T> tClass, String field, Object entity){
        return valueAsync(tableName, tClass, field, entity, async.getExecutor());
    }

    public CompletableFuture<String> valueAsync(String tableName, String field, NeoMap searchMap){
        return valueAsync(tableName, field, searchMap, async.getExecutor());
    }

    public CompletableFuture<String> valueAsync(String tableName, String field, Object entity){
        return valueAsync(tableName, field, entity, async.getExecutor());
    }


    public CompletableFuture<List<NeoMap>> pageAsync(String tableName, Columns columns, NeoMap searchMap, NeoPage page){
        return pageAsync(tableName, columns, searchMap, page, async.getExecutor());
    }

    public CompletableFuture<List<NeoMap>> pageAsync(String tableName, Columns columns, NeoMap searchMap){
        return pageAsync(tableName, columns, searchMap, async.getExecutor());
    }

    public <T> CompletableFuture<List<T>> pageAsync(String tableName, Columns columns, T entity, NeoPage page){
        return pageAsync(tableName, columns, entity, page, async.getExecutor());
    }


    public CompletableFuture<List<NeoMap>> pageAsync(String tableName, NeoMap searchMap, NeoPage page){
        return pageAsync(tableName, searchMap, page, async.getExecutor());
    }


    public <T> CompletableFuture<List<T>> pageAsync(String tableName, T entity, NeoPage page){
        return pageAsync(tableName, entity, page, async.getExecutor());
    }

    public CompletableFuture<List<NeoMap>> pageAsync(String tableName, Columns columns, NeoPage page){
        return pageAsync(tableName, columns, page, async.getExecutor());
    }

    public CompletableFuture<List<NeoMap>> pageAsync(String tableName, NeoPage page){
        return pageAsync(tableName, page, async.getExecutor());
    }

    public CompletableFuture<List<NeoMap>> pageAsync(String tableName, NeoMap searchMap){
        return pageAsync(tableName, searchMap, async.getExecutor());
    }


    public CompletableFuture<Integer> countAsync(String tableName, NeoMap searchMap){
        return countAsync(tableName, searchMap, async.getExecutor());
    }

    public CompletableFuture<Integer> countAsync(String tableName, Object entity){
        return countAsync(tableName, entity, async.getExecutor());
    }

    public CompletableFuture<Integer> countAsync(String tableName){
        return countAsync(tableName, async.getExecutor());
    }

    public CompletableFuture<NeoMap> exeOneAsync(String sql, Object... parameters){
        return exeOneAsync(sql, async.getExecutor(), parameters);
    }


    public <T> CompletableFuture<T> exeOneAsync(Class<T> tClass, String sql, Object... parameters){
        return exeOneAsync(tClass, sql, async.getExecutor(), parameters);
    }

    public CompletableFuture<List<NeoMap>> exeListAsync(String sql, Object... parameters){
        return exeListAsync(sql, async.getExecutor(), parameters);
    }

    public <T> CompletableFuture<List<T>> exeListAsync(Class<T> tClass, String sql, Object... parameters){
        return exeListAsync(tClass, sql, async.getExecutor(), parameters);
    }

    public <T> CompletableFuture<T> exeValueAsync(Class<T> tClass, String sql, Object... parameters){
        return exeValueAsync(tClass, sql, async.getExecutor(), parameters);
    }

    public CompletableFuture<String> exeValueAsync(String sql, Object... parameters){
        return exeValueAsync(sql, async.getExecutor(), parameters);
    }

    public <T> CompletableFuture<List<T>> exeValuesAsync(Class<T> tClass, String sql, Object... parameters){
        return exeValuesAsync(tClass, sql, async.getExecutor(), parameters);
    }

    public CompletableFuture<List<String>> exeValuesAsync(String sql, Object... parameters){
        return exeValuesAsync(sql, async.getExecutor(), parameters);
    }

    public CompletableFuture<List<NeoMap>> exePageAsync(String sql, Integer startIndex, Integer pageSize, Object... parameters){
        return exePageAsync(sql, startIndex, pageSize, async.getExecutor(), parameters);
    }


    public CompletableFuture<List<NeoMap>> exePageAsync(String sql, NeoPage neoPage, Object... parameters){
        return exePageAsync(sql, neoPage, async.getExecutor(), parameters);
    }

    public CompletableFuture<Integer> exeCountAsync(String sql, Object... parameters){
        return exeCountAsync(sql, async.getExecutor(), parameters);
    }

    public CompletableFuture<List<List<NeoMap>>> executeAsync(String sql, Object... parameters){
        return executeAsync(sql, async.getExecutor(), parameters);
    }
}
