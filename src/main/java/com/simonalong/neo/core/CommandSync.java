package com.simonalong.neo.core;

import com.simonalong.neo.Columns;
import com.simonalong.neo.NeoMap;

import java.util.List;

/**
 * @author shizi
 * @since 2020/6/11 2:58 PM
 */
public interface CommandSync extends Sync {

    NeoMap insert(String tableName, NeoMap dataMap);

    <T> T insert(String tableName, T object);


    Integer delete(String tableName, NeoMap searchMap);

    <T> Integer delete(String tableName, T object);

    Integer delete(String tableName, Number id);


    NeoMap update(String tableName, NeoMap dataMap, NeoMap searchMap);

    <T> T update(String tableName, T setEntity, NeoMap searchMap);

    <T> T update(String tableName, T setEntity, T searchEntity);

    <T> NeoMap update(String tableName, NeoMap setMap, T searchEntity);

    NeoMap update(String tableName, NeoMap dataMap, Columns columns);

    <T> T update(String tableName, T entity, Columns columns);

    NeoMap update(String tableName, NeoMap dataMap);

    <T> T update(String tableName, T entity);


    Integer batchInsert(String tableName, List<NeoMap> dataMapList);

    <T> Integer batchInsertEntity(String tableName, List<T> dataList);


    Integer batchUpdate(String tableName, List<NeoMap> dataList);

    Integer batchUpdate(String tableName, List<NeoMap> dataList, Columns columns);

    <T> Integer batchUpdateEntity(String tableName, List<T> dataList);

    <T> Integer batchUpdateEntity(String tableName, List<T> dataList, Columns columns);
}
