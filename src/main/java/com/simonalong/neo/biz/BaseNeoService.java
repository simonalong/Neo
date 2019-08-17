package com.simonalong.neo.biz;

import com.simonalong.neo.Columns;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.db.NeoPage;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

/**
 * @author zhouzhenyong
 * @since 2019-08-17 12:32
 */
interface BaseNeoService {

    NeoMap insert(NeoMap dataMap);

    <T> T insert(T object);

    CompletableFuture<NeoMap> insertAsync(NeoMap dataMap);

    <T> CompletableFuture<T> insertAsync(T object);

    Integer delete(NeoMap searchMap);

    <T> Integer delete(T object);

    CompletableFuture<Integer> deleteAsync(NeoMap dataMap);

    <T> CompletableFuture<Integer> deleteAsync(T object);

    NeoMap update(NeoMap dataMap, NeoMap searchMap);

    NeoMap update(NeoMap dataMap, Columns columns);

    NeoMap update(NeoMap dataMap);

    <T> T update(T object);

    CompletableFuture<NeoMap> updateAsync(NeoMap dataMap, NeoMap searchMap);

    CompletableFuture<NeoMap> updateAsync(NeoMap dataMap, Columns columns);

    CompletableFuture<NeoMap> updateAsync(NeoMap dataMap);

    <T> CompletableFuture<T> updateAsync(T object);

    NeoMap one(Long id);

    NeoMap one(Columns columns, NeoMap searchMap);

    CompletableFuture<NeoMap> oneAsync(Long id);

    CompletableFuture<NeoMap> oneAsync(Columns columns, NeoMap searchMap);

    List<NeoMap> list(Columns columns, NeoMap searchMap);

    List<NeoMap> list(NeoMap dataMap);

    CompletableFuture<List<NeoMap>> listAsync(Columns columns, NeoMap searchMap);

    CompletableFuture<List<NeoMap>> listAsync(NeoMap dataMap);

    List<NeoMap> page(Columns columns, NeoMap searchMap, NeoPage page);

    List<NeoMap> page(Columns columns, NeoMap searchMap);

    List<NeoMap> page(Columns columns, NeoMap searchMap, Integer pageNo, Integer pageSize);

    List getPage(NeoMap dataMap, NeoPage page);

    List getPage(NeoMap searchMap, Integer pageNo, Integer pageSize);

    List getPage(NeoMap dataMap);

    Integer count(NeoMap searchMap);

    CompletableFuture<Integer> countAsync(NeoMap searchMap);

    <T> T value(Class<T> tClass, String field, NeoMap searchMap);

    String value(String field, NeoMap searchMap);

    String value(String field, Long id);

    <T> List<T> values(Class<T> tClass, String field, NeoMap searchMap);

    <T> List<T> values(Class<T> tClass, String field, Long id);

    List<String> values(String field, NeoMap searchMap);

    List<String> values(String field, Long id);

    <T> CompletableFuture<T> valueAsync(Class<T> tClass, String field, NeoMap searchMap);

    CompletableFuture<String> valueAsync(String field, NeoMap searchMap);

    CompletableFuture<String> valueAsync(String field, Long id);

    <T> CompletableFuture<List<T>> valuesAsync(Class<T> tClass, String field, NeoMap searchMap);

    <T> CompletableFuture<List<T>> valuesAsync(Class<T> tClass, String field, Long id);

    CompletableFuture<List<String>> valuesAsync(String field, NeoMap searchMap);

    CompletableFuture<List<String>> valuesAsync(String field, Long id);

    ExecutorService getExecutor();
}
