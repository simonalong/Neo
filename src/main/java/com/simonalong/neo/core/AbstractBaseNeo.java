package com.simonalong.neo.core;

import com.simonalong.neo.Columns;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.NeoMap.NamingChg;
import com.simonalong.neo.table.NeoPage;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author zhouzhenyong
 * @since 2019-08-17 17:17
 */
public abstract class AbstractBaseNeo implements TableSync, TableAsync{

    private AsyncNeo async = DefaultExecutor.getInstance();

    public abstract AbstractBaseNeo getAbsNeo();


    public CompletableFuture<NeoMap> insertAsync(NeoMap dataMap){
        return insertAsync(dataMap, async.getExecutor());
    }


    public <T> CompletableFuture<T> insertAsync(T object){
        return insertAsync(object, async.getExecutor());
    }


    public <T> CompletableFuture<T> insertAsync(T object, NamingChg naming){
        return insertAsync(object, naming, async.getExecutor());
    }


    public CompletableFuture<Integer> deleteAsync(NeoMap dataMap){
        return deleteAsync(dataMap, async.getExecutor());
    }

    public <T> CompletableFuture<Integer> deleteAsync(T object){
        return deleteAsync(object, async.getExecutor());
    }


    public <T> CompletableFuture<Integer> deleteAsync(T entity, NamingChg naming){
        return deleteAsync(entity, naming, async.getExecutor());
    }

    public CompletableFuture<Integer> deleteAsync(Number id){
        return deleteAsync(id, async.getExecutor());
    }


    public CompletableFuture<NeoMap> updateAsync(NeoMap dataMap, NeoMap searchMap){
        return updateAsync(dataMap, searchMap, async.getExecutor());
    }

    public <T> CompletableFuture<T> updateAsync(T setEntity, NeoMap searchMap, NamingChg namingChg){
        return updateAsync(setEntity, searchMap, namingChg, async.getExecutor());
    }

    public <T> CompletableFuture<T> updateAsync(T setEntity, NeoMap searchMap){
        return updateAsync(setEntity, searchMap, async.getExecutor());
    }

    public <T> CompletableFuture<T> updateAsync(T setEntity, T searchEntity){
        return updateAsync(setEntity, searchEntity, async.getExecutor());
    }

    public CompletableFuture<NeoMap> updateAsync(NeoMap dataMap, Columns columns){
        return updateAsync(dataMap, columns, async.getExecutor());
    }

    public <T> CompletableFuture<T> updateAsync(T entity, Columns columns, NamingChg namingChg){
        return updateAsync(entity, columns, namingChg, async.getExecutor());
    }

    public <T> CompletableFuture<T> updateAsync(T entity, Columns columns){
        return updateAsync(entity, columns, async.getExecutor());
    }

    public CompletableFuture<NeoMap> updateAsync(NeoMap dataMap){
        return updateAsync(dataMap, async.getExecutor());
    }

    public <T> CompletableFuture<T> updateAsync(T entity){
        return updateAsync(entity, async.getExecutor());
    }


    public CompletableFuture<NeoMap> oneAsync(Columns columns, NeoMap searchMap){
        return oneAsync(columns, searchMap, async.getExecutor());
    }

    public <T> CompletableFuture<T> oneAsync(Columns columns, T entity){
        return oneAsync(columns, entity, async.getExecutor());
    }

    public CompletableFuture<NeoMap> oneAsync(NeoMap searchMap){
        return oneAsync(searchMap, async.getExecutor());
    }

    public <T> CompletableFuture<T> oneAsync(T entity){
        return oneAsync(entity, async.getExecutor());
    }

    public CompletableFuture<NeoMap> oneAsync(Number id){
        return oneAsync(id, async.getExecutor());
    }


    public CompletableFuture<List<NeoMap>> listAsync(Columns columns, NeoMap searchMap){
        return listAsync(columns, searchMap, async.getExecutor());
    }

    public <T> CompletableFuture<List<T>> listAsync(Columns columns, T entity){
        return listAsync(columns, entity, async.getExecutor());
    }

    public CompletableFuture<List<NeoMap>> listAsync(NeoMap searchMap){
        return listAsync(searchMap, async.getExecutor());
    }

    public <T> CompletableFuture<List<T>> listAsync(T entity){
        return listAsync(entity, async.getExecutor());
    }


    public <T> CompletableFuture<List<T>> valuesAsync(Class<T> tClass, String field, NeoMap searchMap){
        return valuesAsync(tClass, field, searchMap, async.getExecutor());
    }

    public <T> CompletableFuture<List<T>> valuesAsync(Class<T> tClass, String field, Object entity){
        return valuesAsync(tClass, field, entity, async.getExecutor());
    }

    public CompletableFuture<List<String>> valuesAsync(String field, NeoMap searchMap){
        return valuesAsync(field, searchMap, async.getExecutor());
    }

    public CompletableFuture<List<String>> valuesAsync(String field, Object entity){
        return valuesAsync(field, entity, async.getExecutor());
    }

    public CompletableFuture<List<String>> valuesAsync(String field){
        return valuesAsync(field, async.getExecutor());
    }


    public <T> CompletableFuture<T> valueAsync(Class<T> tClass, String field, NeoMap searchMap){
        return valueAsync(tClass, field, searchMap, async.getExecutor());
    }

    public <T> CompletableFuture<T> valueAsync(Class<T> tClass, String field, Object entity){
        return valueAsync(tClass, field, entity, async.getExecutor());
    }

    public CompletableFuture<String> valueAsync(String field, NeoMap searchMap){
        return valueAsync(field, searchMap, async.getExecutor());
    }

    public CompletableFuture<String> valueAsync(String field, Object entity){
        return valueAsync(field, entity, async.getExecutor());
    }


    public CompletableFuture<List<NeoMap>> pageAsync(Columns columns, NeoMap searchMap, NeoPage page){
        return pageAsync(columns, searchMap, page, async.getExecutor());
    }

    public CompletableFuture<List<NeoMap>> pageAsync(Columns columns, NeoMap searchMap){
        return pageAsync(columns, searchMap, async.getExecutor());
    }

    public <T> CompletableFuture<List<T>> pageAsync(Columns columns, T entity, NeoPage page){
        return pageAsync(columns, entity, page, async.getExecutor());
    }

    public CompletableFuture<List<NeoMap>> pageAsync(NeoMap searchMap, NeoPage page){
        return pageAsync(searchMap, page, async.getExecutor());
    }

    public <T> CompletableFuture<List<T>> pageAsync(T entity, NeoPage page){
        return pageAsync(entity, page, async.getExecutor());
    }

    public CompletableFuture<List<NeoMap>> pageAsync(Columns columns, NeoPage page){
        return pageAsync(columns, page, async.getExecutor());
    }

    public CompletableFuture<List<NeoMap>> pageAsync(NeoPage page){
        return pageAsync(page, async.getExecutor());
    }

    public CompletableFuture<List<NeoMap>> pageAsync(NeoMap searchMap){
        return pageAsync(searchMap, async.getExecutor());
    }


    public CompletableFuture<Integer> countAsync(NeoMap searchMap){
        return countAsync(searchMap, async.getExecutor());
    }

    public CompletableFuture<Integer> countAsync(Object entity){
        return countAsync(entity, async.getExecutor());
    }

    public CompletableFuture<Integer> countAsync(){
        return countAsync(async.getExecutor());
    }
}
