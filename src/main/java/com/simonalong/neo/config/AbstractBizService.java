package com.simonalong.neo.config;

import com.simonalong.neo.Columns;
import com.simonalong.neo.Neo;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.NeoMap.NamingChg;
import com.simonalong.neo.table.NeoPage;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author zhouzhenyong
 * @since 2019-08-17 17:18
 */
// todo 下面的待填写
public abstract class AbstractBizService extends AbstractTableNeo{

    public abstract Neo getNeo();

    @Override
    public String getTableName() {
        return null;
    }

    @Override
    public AbstractBaseNeo getAbsNeo() {
        return getNeo();
    }

    @Override
    public CompletableFuture<NeoMap> insertAsync(NeoMap dataMap, Executor executor) {
        return null;
    }

    @Override
    public <T> CompletableFuture<T> insertAsync(T object, Executor executor) {
        return null;
    }

    @Override
    public <T> CompletableFuture<T> insertAsync(T object, NamingChg naming, Executor executor) {
        return null;
    }

    @Override
    public CompletableFuture<Integer> deleteAsync(NeoMap dataMap, Executor executor) {
        return null;
    }

    @Override
    public <T> CompletableFuture<Integer> deleteAsync(T object, Executor executor) {
        return null;
    }

    @Override
    public <T> CompletableFuture<Integer> deleteAsync(T entity, NamingChg naming, Executor executor) {
        return null;
    }

    @Override
    public CompletableFuture<Integer> deleteAsync(Number id, Executor executor) {
        return null;
    }

    @Override
    public CompletableFuture<NeoMap> updateAsync(NeoMap dataMap, NeoMap searchMap, Executor executor) {
        return null;
    }

    @Override
    public <T> CompletableFuture<T> updateAsync(T setEntity, NeoMap searchMap, NamingChg namingChg, Executor executor) {
        return null;
    }

    @Override
    public <T> CompletableFuture<T> updateAsync(T setEntity, NeoMap searchMap, Executor executor) {
        return null;
    }

    @Override
    public <T> CompletableFuture<T> updateAsync(T setEntity, T searchEntity, Executor executor) {
        return null;
    }

    @Override
    public CompletableFuture<NeoMap> updateAsync(NeoMap dataMap, Columns columns, Executor executor) {
        return null;
    }

    @Override
    public <T> CompletableFuture<T> updateAsync(T entity, Columns columns, NamingChg namingChg, Executor executor) {
        return null;
    }

    @Override
    public <T> CompletableFuture<T> updateAsync(T entity, Columns columns, Executor executor) {
        return null;
    }

    @Override
    public CompletableFuture<NeoMap> updateAsync(NeoMap dataMap, Executor executor) {
        return null;
    }

    @Override
    public <T> CompletableFuture<T> updateAsync(T entity, Executor executor) {
        return null;
    }

    @Override
    public CompletableFuture<NeoMap> oneAsync(Columns columns, NeoMap searchMap, Executor executor) {
        return null;
    }

    @Override
    public <T> CompletableFuture<T> oneAsync(Columns columns, T entity, Executor executor) {
        return null;
    }

    @Override
    public CompletableFuture<NeoMap> oneAsync(NeoMap searchMap, Executor executor) {
        return null;
    }

    @Override
    public <T> CompletableFuture<T> oneAsync(T entity, Executor executor) {
        return null;
    }

    @Override
    public CompletableFuture<NeoMap> oneAsync(Number id, Executor executor) {
        return null;
    }

    @Override
    public CompletableFuture<List<NeoMap>> listAsync(Columns columns, NeoMap searchMap, Executor executor) {
        return null;
    }

    @Override
    public <T> CompletableFuture<List<T>> listAsync(Columns columns, T entity, Executor executor) {
        return null;
    }

    @Override
    public CompletableFuture<List<NeoMap>> listAsync(NeoMap searchMap, Executor executor) {
        return null;
    }

    @Override
    public <T> CompletableFuture<List<T>> listAsync(T entity, Executor executor) {
        return null;
    }

    @Override
    public <T> CompletableFuture<List<T>> valuesAsync(Class<T> tClass, String field, NeoMap searchMap,
        Executor executor) {
        return null;
    }

    @Override
    public <T> CompletableFuture<List<T>> valuesAsync(Class<T> tClass, String field, Object entity, Executor executor) {
        return null;
    }

    @Override
    public CompletableFuture<List<String>> valuesAsync(String field, NeoMap searchMap, Executor executor) {
        return null;
    }

    @Override
    public CompletableFuture<List<String>> valuesAsync(String field, Object entity, Executor executor) {
        return null;
    }

    @Override
    public CompletableFuture<List<String>> valuesAsync(String field, Executor executor) {
        return null;
    }

    @Override
    public <T> CompletableFuture<T> valueAsync(Class<T> tClass, String field, NeoMap searchMap, Executor executor) {
        return null;
    }

    @Override
    public <T> CompletableFuture<T> valueAsync(Class<T> tClass, String field, Object entity, Executor executor) {
        return null;
    }

    @Override
    public CompletableFuture<String> valueAsync(String field, NeoMap searchMap, Executor executor) {
        return null;
    }

    @Override
    public CompletableFuture<String> valueAsync(String field, Object entity, Executor executor) {
        return null;
    }

    @Override
    public CompletableFuture<List<NeoMap>> pageAsync(Columns columns, NeoMap searchMap, NeoPage page,
        Executor executor) {
        return null;
    }

    @Override
    public CompletableFuture<List<NeoMap>> pageAsync(Columns columns, NeoMap searchMap, Executor executor) {
        return null;
    }

    @Override
    public <T> CompletableFuture<List<T>> pageAsync(Columns columns, T entity, NeoPage page, Executor executor) {
        return null;
    }

    @Override
    public CompletableFuture<List<NeoMap>> pageAsync(NeoMap searchMap, NeoPage page, Executor executor) {
        return null;
    }

    @Override
    public <T> CompletableFuture<List<T>> pageAsync(T entity, NeoPage page, Executor executor) {
        return null;
    }

    @Override
    public CompletableFuture<List<NeoMap>> pageAsync(Columns columns, NeoPage page, Executor executor) {
        return null;
    }

    @Override
    public CompletableFuture<List<NeoMap>> pageAsync(NeoPage page, Executor executor) {
        return null;
    }

    @Override
    public CompletableFuture<List<NeoMap>> pageAsync(NeoMap searchMap, Executor executor) {
        return null;
    }

    @Override
    public CompletableFuture<Integer> countAsync(NeoMap searchMap, Executor executor) {
        return null;
    }

    @Override
    public CompletableFuture<Integer> countAsync(Object entity, Executor executor) {
        return null;
    }

    @Override
    public CompletableFuture<Integer> countAsync(Executor executor) {
        return null;
    }

    @Override
    public NeoMap insert(NeoMap dataMap) {
        return null;
    }

    @Override
    public <T> T insert(T object) {
        return null;
    }

    @Override
    public <T> T insert(T object, NamingChg naming) {
        return null;
    }

    @Override
    public Integer delete(NeoMap searchMap) {
        return null;
    }

    @Override
    public <T> Integer delete(T object) {
        return null;
    }

    @Override
    public <T> Integer delete(T object, NamingChg naming) {
        return null;
    }

    @Override
    public Integer delete(Number id) {
        return null;
    }

    @Override
    public NeoMap update(NeoMap dataMap, NeoMap searchMap) {
        return null;
    }

    @Override
    public <T> T update(T setEntity, NeoMap searchMap, NamingChg namingChg) {
        return null;
    }

    @Override
    public <T> T update(T setEntity, NeoMap searchMap) {
        return null;
    }

    @Override
    public <T> T update(T setEntity, T searchEntity) {
        return null;
    }

    @Override
    public NeoMap update(NeoMap dataMap, Columns columns) {
        return null;
    }

    @Override
    public <T> T update(T entity, Columns columns, NamingChg namingChg) {
        return null;
    }

    @Override
    public <T> T update(T entity, Columns columns) {
        return null;
    }

    @Override
    public NeoMap update(NeoMap dataMap) {
        return null;
    }

    @Override
    public <T> T update(T entity) {
        return null;
    }

    @Override
    public NeoMap one(Columns columns, NeoMap searchMap) {
        return null;
    }

    @Override
    public <T> T one(Columns columns, T entity) {
        return null;
    }

    @Override
    public NeoMap one(Columns columns, Number key) {
        return null;
    }

    @Override
    public NeoMap one(NeoMap searchMap) {
        return null;
    }

    @Override
    public <T> T one(T entity) {
        return null;
    }

    @Override
    public NeoMap one(Number id) {
        return null;
    }

    @Override
    public List<NeoMap> list(Columns columns, NeoMap searchMap) {
        return null;
    }

    @Override
    public <T> List<T> list(Columns columns, T entity) {
        return null;
    }

    @Override
    public List<NeoMap> list(NeoMap searchMap) {
        return null;
    }

    @Override
    public <T> List<T> list(T entity) {
        return null;
    }

    @Override
    public <T> T value(Class<T> tClass, String field, NeoMap searchMap) {
        return null;
    }

    @Override
    public <T> T value(Class<T> tClass, String field, Object entity) {
        return null;
    }

    @Override
    public String value(String field, NeoMap searchMap) {
        return null;
    }

    @Override
    public String value(String field, Object entity) {
        return null;
    }

    @Override
    public String value(String field, Number entity) {
        return null;
    }

    @Override
    public <T> List<T> values(Class<T> tClass, String field, NeoMap searchMap) {
        return null;
    }

    @Override
    public <T> List<T> values(Class<T> tClass, String field, Object entity) {
        return null;
    }

    @Override
    public List<String> values(String field, NeoMap searchMap) {
        return null;
    }

    @Override
    public List<String> values(String field, Object entity) {
        return null;
    }

    @Override
    public List<String> values(String field) {
        return null;
    }

    @Override
    public List<NeoMap> page(Columns columns, NeoMap searchMap, NeoPage page) {
        return null;
    }

    @Override
    public List<NeoMap> page(Columns columns, NeoMap searchMap) {
        return null;
    }

    @Override
    public <T> List<T> page(Columns columns, T entity, NeoPage page) {
        return null;
    }

    @Override
    public List<NeoMap> page(NeoMap searchMap, NeoPage page) {
        return null;
    }

    @Override
    public <T> List<T> page(T entity, NeoPage page) {
        return null;
    }

    @Override
    public List<NeoMap> page(Columns columns, NeoPage page) {
        return null;
    }

    @Override
    public List<NeoMap> page(NeoPage page) {
        return null;
    }

    @Override
    public List<NeoMap> page(NeoMap searchMap) {
        return null;
    }

    @Override
    public Integer count(NeoMap searchMap) {
        return null;
    }

    @Override
    public Integer count(Object entity) {
        return null;
    }

    @Override
    public Integer count(String tableName) {
        return null;
    }
}
