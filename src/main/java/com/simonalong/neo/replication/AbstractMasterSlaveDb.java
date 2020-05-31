package com.simonalong.neo.replication;

import com.simonalong.neo.Columns;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.TableMap;
import com.simonalong.neo.core.AbstractBaseDb;
import com.simonalong.neo.db.NeoPage;

import java.util.List;

/**
 * @author shizi
 * @since 2020/5/31 5:55 PM
 */
public abstract class AbstractMasterSlaveDb extends AbstractBaseDb implements MasterSlaveSelector {

    @Override
    public NeoMap insert(String tableName, NeoMap dataMap) {
        return getMasterDb().insert(tableName, dataMap);
    }

    @Override
    public <T> T insert(String tableName, T object) {
        return getMasterDb().insert(tableName, object);
    }

    @Override
    public Integer delete(String tableName, NeoMap searchMap) {
        return getMasterDb().delete(tableName, searchMap);
    }

    @Override
    public <T> Integer delete(String tableName, T object) {
        return getMasterDb().delete(tableName, object);
    }

    @Override
    public Integer delete(String tableName, Number id) {
        return getMasterDb().delete(tableName, id);
    }

    @Override
    public NeoMap update(String tableName, NeoMap dataMap, NeoMap searchMap) {
        return getMasterDb().update(tableName, dataMap, searchMap);
    }

    @Override
    public <T> T update(String tableName, T setEntity, NeoMap searchMap) {
        return getMasterDb().update(tableName, setEntity, searchMap);
    }

    @Override
    public <T> T update(String tableName, T setEntity, T searchEntity) {
        return getMasterDb().update(tableName, setEntity, searchEntity);
    }

    @Override
    public <T> NeoMap update(String tableName, NeoMap setMap, T searchEntity) {
        return getMasterDb().update(tableName, setMap, searchEntity);
    }

    @Override
    public NeoMap update(String tableName, NeoMap dataMap, Columns columns) {
        return getMasterDb().update(tableName, dataMap, columns);
    }

    @Override
    public <T> T update(String tableName, T entity, Columns columns) {
        return getMasterDb().update(tableName, entity, columns);
    }

    @Override
    public NeoMap update(String tableName, NeoMap dataMap) {
        return getMasterDb().update(tableName, dataMap);
    }

    @Override
    public <T> T update(String tableName, T entity) {
        return getMasterDb().update(tableName, entity);
    }

    @Override
    public NeoMap one(String tableName, Columns columns, NeoMap searchMap) {
        return getSlaveDb().one(tableName, columns, searchMap);
    }

    @Override
    public <T> T one(String tableName, Columns columns, T entity) {
        return getSlaveDb().one(tableName, columns, entity);
    }

    @Override
    public NeoMap one(String tableName, Columns columns, Number key) {
        return getSlaveDb().one(tableName, columns, key);
    }

    @Override
    public NeoMap one(String tableName, NeoMap searchMap) {
        return getSlaveDb().one(tableName, searchMap);
    }

    @Override
    public <T> T one(String tableName, T entity) {
        return getSlaveDb().one(tableName, entity);
    }

    @Override
    public NeoMap one(String tableName, Number id) {
        return getSlaveDb().one(tableName, id);
    }

    @Override
    public <T> T one(Class<T> tClass, String tableName, Columns columns, NeoMap searchMap) {
        return getSlaveDb().one(tClass, tableName, columns, searchMap);
    }

    @Override
    public <T> T one(Class<T> tClass, String tableName, Columns columns, Number key) {
        return getSlaveDb().one(tClass, tableName, columns, key);
    }

    @Override
    public <T> T one(Class<T> tClass, String tableName, NeoMap searchMap) {
        return getSlaveDb().one(tClass, tableName, searchMap);
    }

    @Override
    public <T> T one(Class<T> tClass, String tableName, Number id) {
        return getSlaveDb().one(tClass, tableName, id);
    }

    @Override
    public List<NeoMap> list(String tableName, Columns columns, NeoMap searchMap) {
        return getSlaveDb().list(tableName, columns, searchMap);
    }

    @Override
    public <T> List<T> list(String tableName, Columns columns, T entity) {
        return getSlaveDb().list(tableName, columns, entity);
    }

    @Override
    public List<NeoMap> list(String tableName, NeoMap searchMap) {
        return getSlaveDb().list(tableName, searchMap);
    }

    @Override
    public <T> List<T> list(String tableName, T entity) {
        return getSlaveDb().list(tableName, entity);
    }

    @Override
    public List<NeoMap> list(String tableName, Columns columns) {
        return getSlaveDb().list(tableName, columns);
    }

    @Override
    public <T> List<T> list(Class<T> tClass, String tableName, Columns columns, NeoMap searchMap) {
        return getSlaveDb().list(tClass, tableName, columns, searchMap);
    }

    @Override
    public <T> List<T> list(Class<T> tClass, String tableName, NeoMap searchMap) {
        return getSlaveDb().list(tClass, tableName, searchMap);
    }

    @Override
    public <T> List<T> list(Class<T> tClass, String tableName, Columns columns) {
        return getSlaveDb().list(tClass, tableName, columns);
    }

    @Override
    public <T> T value(String tableName, Class<T> tClass, String field, NeoMap searchMap) {
        return getSlaveDb().value(tableName, tClass, field, searchMap);
    }

    @Override
    public <T> T value(String tableName, Class<T> tClass, String field, Object entity) {
        return getSlaveDb().value(tableName, tClass, field, entity);
    }

    @Override
    public String value(String tableName, String field, NeoMap searchMap) {
        return getSlaveDb().value(tableName, field, searchMap);
    }

    @Override
    public String value(String tableName, String field, Object entity) {
        return getSlaveDb().value(tableName, field, entity);
    }

    @Override
    public String value(String tableName, String field, Number id) {
        return getSlaveDb().value(tableName, field, id);
    }

    @Override
    public <T> List<T> values(String tableName, Class<T> tClass, String field, NeoMap searchMap) {
        return getSlaveDb().values(tableName, tClass, field, searchMap);
    }

    @Override
    public <T> List<T> values(String tableName, Class<T> tClass, String field, Object entity) {
        return getSlaveDb().values(tableName, tClass, field, entity);
    }

    @Override
    public List<String> values(String tableName, String field, NeoMap searchMap) {
        return getSlaveDb().values(tableName, field, searchMap);
    }

    @Override
    public List<String> values(String tableName, String field, Object entity) {
        return getSlaveDb().values(tableName, field, entity);
    }

    @Override
    public List<String> values(String tableName, String field) {
        return getSlaveDb().values(tableName, field);
    }

    @Override
    public List<NeoMap> page(String tableName, Columns columns, NeoMap searchMap, NeoPage page) {
        return getSlaveDb().page(tableName, columns, searchMap, page);
    }

    @Override
    public <T> List<T> page(String tableName, Columns columns, T entity, NeoPage page) {
        return getSlaveDb().page(tableName, columns, entity, page);
    }

    @Override
    public List<NeoMap> page(String tableName, NeoMap searchMap, NeoPage page) {
        return getSlaveDb().page(tableName, searchMap, page);
    }

    @Override
    public <T> List<T> page(String tableName, T entity, NeoPage page) {
        return getSlaveDb().page(tableName, entity, page);
    }

    @Override
    public List<NeoMap> page(String tableName, Columns columns, NeoPage page) {
        return getSlaveDb().page(tableName, columns, page);
    }

    @Override
    public List<NeoMap> page(String tableName, NeoPage page) {
        return getSlaveDb().page(tableName, page);
    }

    @Override
    public <T> List<T> page(Class<T> tClass, String tableName, Columns columns, NeoMap searchMap, NeoPage page) {
        return getSlaveDb().page(tClass, tableName, columns, searchMap, page);
    }

    @Override
    public <T> List<T> page(Class<T> tClass, String tableName, NeoMap searchMap, NeoPage page) {
        return getSlaveDb().page(tClass, tableName, searchMap, page);
    }

    @Override
    public <T> List<T> page(Class<T> tClass, String tableName, Columns columns, NeoPage page) {
        return getSlaveDb().page(tClass, tableName, columns, page);
    }

    @Override
    public <T> List<T> page(Class<T> tClass, String tableName, NeoPage page) {
        return getSlaveDb().page(tClass, tableName, page);
    }

    @Override
    public Integer count(String tableName, NeoMap searchMap) {
        return getSlaveDb().count(tableName, searchMap);
    }

    @Override
    public Integer count(String tableName, Object entity) {
        return getSlaveDb().count(tableName, entity);
    }

    @Override
    public Integer count(String tableName) {
        return getSlaveDb().count(tableName);
    }

    @Override
    public Boolean exist(String tableName, NeoMap searchMap) {
        return getSlaveDb().exist(tableName, searchMap);
    }

    @Override
    public Boolean exist(String tableName, Object entity) {
        return getSlaveDb().exist(tableName, entity);
    }

    @Override
    public Integer batchInsert(String tableName, List<NeoMap> dataMapList) {
        return getMasterDb().batchInsert(tableName, dataMapList);
    }

    @Override
    public <T> Integer batchInsertEntity(String tableName, List<T> dataList) {
        return getMasterDb().batchInsertEntity(tableName, dataList);
    }

    @Override
    public Integer batchUpdate(String tableName, List<NeoMap> dataList) {
        return getMasterDb().batchUpdate(tableName, dataList);
    }

    @Override
    public Integer batchUpdate(String tableName, List<NeoMap> dataList, Columns columns) {
        return getMasterDb().batchUpdate(tableName, dataList, columns);
    }

    @Override
    public <T> Integer batchUpdateEntity(String tableName, List<T> dataList) {
        return getMasterDb().batchUpdateEntity(tableName, dataList);
    }

    @Override
    public <T> Integer batchUpdateEntity(String tableName, List<T> dataList, Columns columns) {
        return getMasterDb().batchUpdateEntity(tableName, dataList, columns);
    }

    @Override
    public TableMap exeOne(String sql, Object... parameters) {
        return getMasterDb().exeOne(sql, parameters);
    }

    @Override
    public <T> T exeOne(Class<T> tClass, String sql, Object... parameters) {
        return getMasterDb().exeOne(tClass, sql, parameters);
    }

    @Override
    public List<TableMap> exeList(String sql, Object... parameters) {
        return getMasterDb().exeList(sql, parameters);
    }

    @Override
    public <T> List<T> exeList(Class<T> tClass, String sql, Object... parameters) {
        return getMasterDb().exeList(tClass, sql, parameters);
    }

    @Override
    public <T> T exeValue(Class<T> tClass, String sql, Object... parameters) {
        return getMasterDb().exeValue(tClass, sql, parameters);
    }

    @Override
    public String exeValue(String sql, Object... parameters) {
        return getMasterDb().exeValue(sql, parameters);
    }

    @Override
    public <T> List<T> exeValues(Class<T> tClass, String sql, Object... parameters) {
        return getMasterDb().exeValues(tClass, sql, parameters);
    }

    @Override
    public List<String> exeValues(String sql, Object... parameters) {
        return getMasterDb().exeValues(sql, parameters);
    }

    @Override
    public List<TableMap> exePage(String sql, Integer startIndex, Integer pageSize, Object... parameters) {
        return getMasterDb().exePage(sql, startIndex, pageSize, parameters);
    }

    @Override
    public <T> List<T> exePage(Class<T> tClass, String sql, Integer startIndex, Integer pageSize, Object... parameters) {
        return getMasterDb().exePage(tClass, sql, startIndex, pageSize, parameters);
    }

    @Override
    public List<TableMap> exePage(String sql, NeoPage neoPage, Object... parameters) {
        return getMasterDb().exePage(sql, neoPage, parameters);
    }

    @Override
    public <T> List<T> exePage(Class<T> tClass, String sql, NeoPage neoPage, Object... parameters) {
        return getMasterDb().exePage(tClass, sql, neoPage, parameters);
    }

    @Override
    public Integer exeCount(String sql, Object... parameters) {
        return getMasterDb().exeCount(sql, parameters);
    }

    @Override
    public List<List<TableMap>> execute(String sql, Object... parameters) {
        return getMasterDb().execute(sql, parameters);
    }
}
