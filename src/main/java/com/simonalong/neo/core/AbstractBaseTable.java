package com.simonalong.neo.core;

import com.simonalong.neo.Columns;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.NeoMap.NamingChg;
import com.simonalong.neo.exception.DbNotSetException;
import com.simonalong.neo.table.NeoPage;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import lombok.extern.slf4j.Slf4j;

/**
 * 该抽象类做三件事情
 * <ul>
 * <li>1.提供默认的线程池</li>
 * <li>2.异步接口实现，通过调用同步函数完成</li>
 * <li>3.同步函数调用通过db的同步函数完成</li>
 * <li>4.提供db外部实现接口</li>
 * <li>5.提供table外部获取接口</li>
 *</ul>
 * @author zhouzhenyong
 * @since 2019-08-17 17:18
 */
@Slf4j
public abstract class AbstractBaseTable extends AbstractTableAsync implements TableSync {

    @Override
    public Executor getExecutor() {
        return DefaultExecutor.getInstance().getExecutor();
    }

    /**
     * 获取当前处理该表的DB数据库
     * @return db同步库对象
     */
    abstract public DbSync getDb();

    /**
     * 获取当前处理的表名
     * @return 返回表名
     */
    abstract public String getTableName();

    @Override
    public NeoMap insert(NeoMap dataMap) {
        return getDbInner().insert(getTableName(), dataMap);
    }

    @Override
    public <T> T insert(T object) {
        return getDbInner().insert(getTableName(), object);
    }

    @Override
    public <T> T insert(T object, NamingChg naming) {
        return getDbInner().insert(getTableName(), object, naming);
    }

    @Override
    public Integer delete(NeoMap searchMap){
        return getDbInner().delete(getTableName(), searchMap);
    }

    @Override
    public <T> Integer delete(T object){
        return getDbInner().delete(getTableName(), object);
    }

    @Override
    public <T> Integer delete(T object, NamingChg naming){
        return getDbInner().delete(getTableName(), object, naming);
    }

    @Override
    public Integer delete(Number id){
        return getDbInner().delete(getTableName(), id);
    }

    @Override
    public NeoMap update(NeoMap dataMap, NeoMap searchMap){
        return getDbInner().update(getTableName(), dataMap, searchMap);
    }

    @Override
    public <T> T update(T setEntity, NeoMap searchMap, NamingChg namingChg){
        return getDbInner().update(getTableName(), setEntity, searchMap, namingChg);
    }

    @Override
    public <T> T update(T setEntity, NeoMap searchMap){
        return getDbInner().update(getTableName(), setEntity, searchMap);
    }

    @Override
    public <T> T update(T setEntity, T searchEntity){
        return getDbInner().update(getTableName(), setEntity, searchEntity);
    }

    @Override
    public NeoMap update(NeoMap dataMap, Columns columns){
        return getDbInner().update(getTableName(), dataMap, columns);
    }

    @Override
    public <T> T update(T entity, Columns columns, NamingChg namingChg){
        return getDbInner().update(getTableName(), entity, columns, namingChg);
    }

    @Override
    public <T> NeoMap update(NeoMap setMap, T searchEntity){
        return getDbInner().update(getTableName(), setMap, searchEntity);
    }

    @Override
    public <T> T update(T entity, Columns columns){
        return getDbInner().update(getTableName(), entity, columns);
    }

    @Override
    public NeoMap update(NeoMap dataMap){
        return getDbInner().update(getTableName(), dataMap);
    }

    @Override
    public <T> T update(T entity){
        return getDbInner().update(getTableName(), entity);
    }

    @Override
    public NeoMap one(Columns columns, NeoMap searchMap){
        return getDbInner().one(getTableName(), columns, searchMap);
    }

    @Override
    public <T> T one(Columns columns, T entity){
        return getDbInner().one(getTableName(), columns, entity);
    }

    @Override
    public NeoMap one(Columns columns, Number key){
        return getDbInner().one(getTableName(), columns, key);
    }

    @Override
    public NeoMap one(NeoMap searchMap){
        return getDbInner().one(getTableName(), searchMap);
    }

    @Override
    public <T> T one(T entity){
        return getDbInner().one(getTableName(), entity);
    }

    @Override
    public NeoMap one(Number id){
        return getDbInner().one(getTableName(), id);
    }

    @Override
    public List<NeoMap> list(Columns columns, NeoMap searchMap){
        return getDbInner().list(getTableName(), columns, searchMap);
    }

    @Override
    public <T> List<T> list(Columns columns, T entity){
        return getDbInner().list(getTableName(), columns, entity);
    }

    @Override
    public List<NeoMap> list(NeoMap searchMap){
        return getDbInner().list(getTableName(), searchMap);
    }

    @Override
    public <T> List<T> list(T entity){
        return getDbInner().list(getTableName(), entity);
    }

    @Override
    public List<NeoMap> list(Columns columns){
        return getDbInner().list(getTableName(), columns);
    }

    @Override
    public <T> T value(Class<T> tClass, String field, NeoMap searchMap){
        return getDbInner().value(getTableName(), tClass, field, searchMap);
    }

    @Override
    public <T> T value(Class<T> tClass, String field, Object entity){
        return getDbInner().value(getTableName(), tClass, field, entity);
    }

    @Override
    public String value(String field, NeoMap searchMap){
        return getDbInner().value(getTableName(), field, searchMap);
    }

    @Override
    public String value(String field, Object entity){
        return getDbInner().value(getTableName(), field, entity);
    }

    @Override
    public String value(String field, Number entity){
        return getDbInner().value(getTableName(), field, entity);
    }

    @Override
    public <T> List<T> values(Class<T> tClass, String field, NeoMap searchMap){
        return getDbInner().values(getTableName(), tClass, field, searchMap);
    }

    @Override
    public <T> List<T> values(Class<T> tClass, String field, Object entity){
        return getDbInner().values(getTableName(), tClass, field, entity);
    }

    @Override
    public List<String> values(String field, NeoMap searchMap){
        return getDbInner().values(getTableName(), field, searchMap);
    }

    @Override
    public List<String> values(String field, Object entity){
        return getDbInner().values(getTableName(), field, entity);
    }

    @Override
    public List<String> values(String field){
        return getDbInner().values(getTableName(), field);
    }

    @Override
    public List<NeoMap> page(Columns columns, NeoMap searchMap, NeoPage page){
        return getDbInner().page(getTableName(), columns, searchMap, page);
    }

    @Override
    public List<NeoMap> page(Columns columns, NeoMap searchMap){
        return getDbInner().page(getTableName(), columns, searchMap);
    }

    @Override
    public <T> List<T> page(Columns columns, T entity, NeoPage page){
        return getDbInner().page(getTableName(), columns, entity, page);
    }

    @Override
    public List<NeoMap> page(NeoMap searchMap, NeoPage page){
        return getDbInner().page(getTableName(), searchMap, page);
    }

    @Override
    public <T> List<T> page(T entity, NeoPage page){
        return getDbInner().page(getTableName(), entity, page);
    }

    @Override
    public List<NeoMap> page(Columns columns, NeoPage page){
        return getDbInner().page(getTableName(), columns, page);
    }

    @Override
    public List<NeoMap> page(NeoPage page){
        return getDbInner().page(getTableName(), page);
    }

    @Override
    public List<NeoMap> page(NeoMap searchMap){
        return getDbInner().page(getTableName(), searchMap);
    }

    @Override
    public Integer count(NeoMap searchMap){
        return getDbInner().count(getTableName(), searchMap);
    }

    @Override
    public Integer count(Object entity){
        return getDbInner().count(getTableName(), entity);
    }

    @Override
    public Integer count(){
        return getDbInner().count(getTableName());
    }

    @Override
    public Integer batchInsert(List<NeoMap> dataMapList){
        return getDbInner().batchInsert(getTableName(), dataMapList);
    }

    @Override
    public <T> Integer batchInsertEntity(List<T> dataList, NamingChg namingChg) {
        return getDbInner().batchInsertEntity(getTableName(), dataList, namingChg);
    }

    @Override
    public <T> Integer batchInsertEntity(List<T> dataList) {
        return getDbInner().batchInsertEntity(getTableName(), dataList);
    }

    @Override
    public Integer batchUpdate(List<NeoMap> dataList) {
        return getDbInner().batchUpdate(getTableName(), dataList);
    }

    @Override
    public Integer batchUpdate(List<NeoMap> dataList, Columns columns) {
        return getDbInner().batchUpdate(getTableName(), dataList, columns);
    }

    @Override
    public <T> Integer batchUpdateEntity(List<T> dataList) {
        return getDbInner().batchUpdateEntity(getTableName(), dataList);
    }

    @Override
    public <T> Integer batchUpdateEntity(List<T> dataList, Columns columns, NamingChg namingChg) {
        return getDbInner().batchUpdateEntity(getTableName(), dataList, columns, namingChg);
    }

    @Override
    public <T> Integer batchUpdateEntity(List<T> dataList, Columns columns) {
        return getDbInner().batchUpdateEntity(getTableName(), dataList, columns);
    }


    @Override
    public CompletableFuture<NeoMap> insertAsync(NeoMap dataMap, Executor executor) {
        return CompletableFuture.supplyAsync(() -> insert(dataMap), executor);
    }

    @Override
    public <T> CompletableFuture<T> insertAsync(T object, Executor executor) {
        return CompletableFuture.supplyAsync(() -> insert(object), executor);
    }

    @Override
    public <T> CompletableFuture<T> insertAsync(T object, NamingChg naming, Executor executor) {
        return CompletableFuture.supplyAsync(() -> insert(object, naming), executor);
    }

    @Override
    public CompletableFuture<Integer> deleteAsync(NeoMap dataMap, Executor executor) {
        return CompletableFuture.supplyAsync(() -> delete(dataMap), executor);
    }

    @Override
    public <T> CompletableFuture<Integer> deleteAsync(T object, Executor executor) {
        return CompletableFuture.supplyAsync(() -> delete(object), executor);
    }

    @Override
    public <T> CompletableFuture<Integer> deleteAsync(T entity, NamingChg naming, Executor executor) {
        return CompletableFuture.supplyAsync(() -> delete(entity, naming), executor);
    }

    @Override
    public CompletableFuture<Integer> deleteAsync(Number id, Executor executor) {
        return CompletableFuture.supplyAsync(() -> delete(id), executor);
    }

    @Override
    public CompletableFuture<NeoMap> updateAsync(NeoMap dataMap, NeoMap searchMap, Executor executor) {
        return CompletableFuture.supplyAsync(() -> update(dataMap, searchMap), executor);
    }

    @Override
    public <T> CompletableFuture<T> updateAsync(T setEntity, NeoMap searchMap, NamingChg namingChg, Executor executor) {
        return CompletableFuture.supplyAsync(() -> update(setEntity, searchMap, namingChg), executor);
    }

    @Override
    public <T> CompletableFuture<T> updateAsync(T setEntity, NeoMap searchMap, Executor executor) {
        return CompletableFuture.supplyAsync(() -> update(setEntity, searchMap), executor);
    }

    @Override
    public <T> CompletableFuture<T> updateAsync(T setEntity, T searchEntity, Executor executor) {
        return CompletableFuture.supplyAsync(() -> update(setEntity, searchEntity), executor);
    }

    @Override
    public CompletableFuture<NeoMap> updateAsync(NeoMap dataMap, Columns columns, Executor executor) {
        return CompletableFuture.supplyAsync(() -> update(dataMap, columns), executor);
    }

    @Override
    public <T> CompletableFuture<T> updateAsync(T entity, Columns columns, NamingChg namingChg, Executor executor) {
        return CompletableFuture.supplyAsync(() -> update(entity, columns, namingChg), executor);
    }

    @Override
    public <T> CompletableFuture<T> updateAsync(T entity, Columns columns, Executor executor) {
        return CompletableFuture.supplyAsync(() -> update(entity, columns), executor);
    }

    @Override
    public CompletableFuture<NeoMap> updateAsync(NeoMap dataMap, Executor executor) {
        return CompletableFuture.supplyAsync(() -> update(dataMap), executor);
    }

    @Override
    public <T> CompletableFuture<T> updateAsync(T entity, Executor executor) {
        return CompletableFuture.supplyAsync(() -> update(entity), executor);
    }

    @Override
    public CompletableFuture<NeoMap> oneAsync(Columns columns, NeoMap searchMap, Executor executor) {
        return CompletableFuture.supplyAsync(() -> one(columns, searchMap), executor);
    }

    @Override
    public <T> CompletableFuture<T> oneAsync(Columns columns, T entity, Executor executor) {
        return CompletableFuture.supplyAsync(() -> one(columns, entity), executor);
    }

    @Override
    public CompletableFuture<NeoMap> oneAsync(NeoMap searchMap, Executor executor) {
        return CompletableFuture.supplyAsync(() -> one(searchMap), executor);
    }

    @Override
    public <T> CompletableFuture<T> oneAsync(T entity, Executor executor) {
        return CompletableFuture.supplyAsync(() -> one(entity), executor);
    }

    @Override
    public CompletableFuture<NeoMap> oneAsync(Number id, Executor executor) {
        return CompletableFuture.supplyAsync(() -> one(id), executor);
    }

    @Override
    public CompletableFuture<List<NeoMap>> listAsync(Columns columns, NeoMap searchMap, Executor executor) {
        return CompletableFuture.supplyAsync(() -> list(columns, searchMap), executor);
    }

    @Override
    public <T> CompletableFuture<List<T>> listAsync(Columns columns, T entity, Executor executor) {
        return CompletableFuture.supplyAsync(() -> list(columns, entity), executor);
    }

    @Override
    public CompletableFuture<List<NeoMap>> listAsync(NeoMap searchMap, Executor executor) {
        return CompletableFuture.supplyAsync(() -> list(searchMap), executor);
    }

    @Override
    public <T> CompletableFuture<List<T>> listAsync(T entity, Executor executor) {
        return CompletableFuture.supplyAsync(() -> list(entity), executor);
    }

    @Override
    public <T> CompletableFuture<List<T>> valuesAsync(Class<T> tClass, String field, NeoMap searchMap,
        Executor executor) {
        return CompletableFuture.supplyAsync(() -> values(tClass, field, searchMap), executor);
    }

    @Override
    public <T> CompletableFuture<List<T>> valuesAsync(Class<T> tClass, String field, Object entity, Executor executor) {
        return CompletableFuture.supplyAsync(() -> values(tClass, field, entity), executor);
    }

    @Override
    public CompletableFuture<List<String>> valuesAsync(String field, NeoMap searchMap, Executor executor) {
        return CompletableFuture.supplyAsync(() -> values(field, searchMap), executor);
    }

    @Override
    public CompletableFuture<List<String>> valuesAsync(String field, Object entity, Executor executor) {
        return CompletableFuture.supplyAsync(() -> values(field, entity), executor);
    }

    @Override
    public CompletableFuture<List<String>> valuesAsync(String field, Executor executor) {
        return CompletableFuture.supplyAsync(() -> values(field), executor);
    }

    @Override
    public <T> CompletableFuture<T> valueAsync(Class<T> tClass, String field, NeoMap searchMap, Executor executor) {
        return CompletableFuture.supplyAsync(() -> value(tClass, field, searchMap), executor);
    }

    @Override
    public <T> CompletableFuture<T> valueAsync(Class<T> tClass, String field, Object entity, Executor executor) {
        return CompletableFuture.supplyAsync(() -> value(tClass, field, entity), executor);
    }

    @Override
    public CompletableFuture<String> valueAsync(String field, NeoMap searchMap, Executor executor) {
        return CompletableFuture.supplyAsync(() -> value(field, searchMap), executor);
    }

    @Override
    public CompletableFuture<String> valueAsync(String field, Object entity, Executor executor) {
        return CompletableFuture.supplyAsync(() -> value(field, entity), executor);
    }

    @Override
    public CompletableFuture<List<NeoMap>> pageAsync(Columns columns, NeoMap searchMap, NeoPage page,
        Executor executor) {
        return CompletableFuture.supplyAsync(() -> page(columns, searchMap, page), executor);
    }

    @Override
    public CompletableFuture<List<NeoMap>> pageAsync(Columns columns, NeoMap searchMap, Executor executor) {
        return CompletableFuture.supplyAsync(() -> page(columns, searchMap), executor);
    }

    @Override
    public <T> CompletableFuture<List<T>> pageAsync(Columns columns, T entity, NeoPage page, Executor executor) {
        return CompletableFuture.supplyAsync(() -> page(columns, entity, page), executor);
    }

    @Override
    public CompletableFuture<List<NeoMap>> pageAsync(NeoMap searchMap, NeoPage page, Executor executor) {
        return CompletableFuture.supplyAsync(() -> page(searchMap, page), executor);
    }

    @Override
    public <T> CompletableFuture<List<T>> pageAsync(T entity, NeoPage page, Executor executor) {
        return CompletableFuture.supplyAsync(() -> page(entity, page), executor);
    }

    @Override
    public CompletableFuture<List<NeoMap>> pageAsync(Columns columns, NeoPage page, Executor executor) {
        return CompletableFuture.supplyAsync(() -> page(columns, page), executor);
    }

    @Override
    public CompletableFuture<List<NeoMap>> pageAsync(NeoPage page, Executor executor) {
        return CompletableFuture.supplyAsync(() -> page(page), executor);
    }

    @Override
    public CompletableFuture<List<NeoMap>> pageAsync(NeoMap searchMap, Executor executor) {
        return CompletableFuture.supplyAsync(() -> page(searchMap), executor);
    }

    @Override
    public CompletableFuture<Integer> countAsync(NeoMap searchMap, Executor executor) {
        return CompletableFuture.supplyAsync(() -> count(searchMap), executor);
    }

    @Override
    public CompletableFuture<Integer> countAsync(Object entity, Executor executor) {
        return CompletableFuture.supplyAsync(() -> count(entity), executor);
    }

    @Override
    public CompletableFuture<Integer> countAsync(Executor executor) {
        return CompletableFuture.supplyAsync(this::count, executor);
    }


    @Override
    public CompletableFuture<Integer> batchInsertAsync(List<NeoMap> dataMapList, Executor executor) {
        return CompletableFuture.supplyAsync(() -> batchInsert(dataMapList), executor);
    }

    @Override
    public <T> CompletableFuture<Integer> batchInsertEntityAsync(List<T> dataList, NamingChg namingChg, Executor executor){
        return CompletableFuture.supplyAsync(() -> batchInsertEntity(dataList, namingChg), executor);
    }

    @Override
    public <T> CompletableFuture<Integer> batchInsertEntityAsync(List<T> dataList, Executor executor){
        return CompletableFuture.supplyAsync(() -> batchInsertEntity(dataList), executor);
    }


    @Override
    public CompletableFuture<Integer> batchUpdateAsync(List<NeoMap> dataList, Executor executor){
        return CompletableFuture.supplyAsync(() -> batchUpdate(dataList), executor);
    }

    @Override
    public CompletableFuture<Integer> batchUpdateAsync(List<NeoMap> dataList, Columns columns, Executor executor){
        return CompletableFuture.supplyAsync(() -> batchUpdate(dataList, columns), executor);
    }

    @Override
    public <T> CompletableFuture<Integer> batchUpdateEntityAsync(List<T> dataList, Executor executor){
        return CompletableFuture.supplyAsync(() -> batchUpdateEntity(dataList), executor);
    }

    @Override
    public <T> CompletableFuture<Integer> batchUpdateEntityAsync(List<T> dataList, Columns columns, NamingChg namingChg, Executor executor){
        return CompletableFuture.supplyAsync(() -> batchUpdateEntity(dataList, columns, namingChg), executor);
    }

    @Override
    public <T> CompletableFuture<Integer> batchUpdateEntityAsync(List<T> dataList, Columns columns, Executor executor){
        return CompletableFuture.supplyAsync(() -> batchUpdateEntity(dataList, columns), executor);
    }

    private DbSync getDbInner() {
        DbSync db = getDb();
        if (null == db) {
            log.error("DB not set");
            throw new DbNotSetException();
        }
        return db;
    }

}
