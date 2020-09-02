package com.simonalong.neo.replication;

import com.mysql.cj.jdbc.exceptions.CommunicationsException;
import com.simonalong.neo.*;
import com.simonalong.neo.core.AbstractExecutorDb;
import com.simonalong.neo.db.NeoPage;
import com.simonalong.neo.exception.NeoException;
import com.simonalong.neo.express.Express;
import com.simonalong.neo.util.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLTransientConnectionException;
import java.util.List;
import java.util.function.Function;

import static com.simonalong.neo.NeoConstant.LOG_PRE;

/**
 * @author shizi
 * @since 2020/5/31 5:55 PM
 */
@Slf4j
public abstract class AbstractMasterSlaveDb extends AbstractExecutorDb implements MasterSlaveSelector {

    static final String MS_LOG_PRE = LOG_PRE + "[master-slave]";

    @Override
    public NeoMap insert(String tableName, NeoMap dataMap) {
        return doMasterCall(db -> db.insert(tableName, dataMap));
    }

    @Override
    public <T> T insert(String tableName, T object) {
        return doMasterCall(db -> db.insert(tableName, object));
    }

    @Override
    public NeoMap save(String tableName, NeoMap dataMap, String... searchColumnKey) {
        return doMasterCall(db -> db.save(tableName, dataMap, searchColumnKey));
    }

    @Override
    public <T> T save(String tableName, T object, String... searchColumnKey) {
        return doMasterCall(db -> db.save(tableName, object, searchColumnKey));
    }

    @Override
    public Integer delete(String tableName, NeoMap searchMap) {
        return doMasterCall(db -> db.delete(tableName, searchMap));
    }

    @Override
    public Integer delete(String tableName, Express searchExpress) {
        return doMasterCall(db -> db.delete(tableName, searchExpress));
    }

    @Override
    public <T> Integer delete(String tableName, T object) {
        return doMasterCall(db -> db.delete(tableName, object));
    }

    @Override
    public Integer delete(String tableName, Number id) {
        return doMasterCall(db -> db.delete(tableName, id));
    }

    @Override
    public NeoMap update(String tableName, NeoMap dataMap, NeoMap searchMap) {
        return doMasterCall(db -> db.update(tableName, dataMap, searchMap));
    }

    @Override
    public <T> T update(String tableName, T setEntity, NeoMap searchMap) {
        return doMasterCall(db -> db.update(tableName, setEntity, searchMap));
    }

    @Override
    public NeoMap update(String tableName, NeoMap dataMap, Express searchExpress){
        return doMasterCall(db -> db.update(tableName, dataMap, searchExpress));
    }

    @Override
    public <T> T update(String tableName, T setEntity, Express searchExpress) {
        return doMasterCall(db -> db.update(tableName, setEntity, searchExpress));
    }

    @Override
    public <T> T update(String tableName, T setEntity, T searchEntity) {
        return doMasterCall(db -> db.update(tableName, setEntity, searchEntity));
    }

    @Override
    public <T> NeoMap update(String tableName, NeoMap setMap, T searchEntity) {
        return doMasterCall(db -> db.update(tableName, setMap, searchEntity));
    }

    @Override
    public NeoMap update(String tableName, NeoMap dataMap, Columns columns) {
        return doMasterCall(db -> db.update(tableName, dataMap, columns));
    }

    @Override
    public <T> T update(String tableName, T entity, Columns columns) {
        return doMasterCall(db -> db.update(tableName, entity, columns));
    }

    @Override
    public NeoMap update(String tableName, NeoMap dataMap) {
        return doMasterCall(db -> db.update(tableName, dataMap));
    }

    @Override
    public <T> T update(String tableName, T entity) {
        return doMasterCall(db -> db.update(tableName, entity));
    }

    @Override
    public NeoMap one(String tableName, Columns columns, NeoMap searchMap) {
        return doSlaveCall(db -> db.one(tableName, columns, searchMap));
    }

    @Override
    public NeoMap one(String tableName, Columns columns, Express searchExpress){
        return doSlaveCall(db -> db.one(tableName, columns, searchExpress));
    }

    @Override
    public <T> T one(String tableName, Columns columns, T entity) {
        return doSlaveCall(db -> db.one(tableName, columns, entity));
    }

    @Override
    public NeoMap one(String tableName, Columns columns, Number key) {
        return doSlaveCall(db -> db.one(tableName, columns, key));
    }

    @Override
    public NeoMap one(String tableName, NeoMap searchMap) {
        return doSlaveCall(db -> db.one(tableName, searchMap));
    }

    @Override
    public <T> T one(String tableName, T entity) {
        return doSlaveCall(db -> db.one(tableName, entity));
    }

    @Override
    public NeoMap one(String tableName, Number id) {
        return doSlaveCall(db -> db.one(tableName, id));
    }

    @Override
    public NeoMap one(String tableName, Express searchExpress) {
        return doSlaveCall(db -> db.one(tableName, searchExpress));
    }

    @Override
    public <T> T one(Class<T> tClass, String tableName, Columns columns, NeoMap searchMap) {
        return doSlaveCall(db -> db.one(tClass, tableName, columns, searchMap));
    }

    @Override
    public <T> T one(Class<T> tClass, String tableName, Columns columns, Number key) {
        return doSlaveCall(db -> db.one(tClass, tableName, columns, key));
    }

    @Override
    public <T> T one(Class<T> tClass, String tableName, NeoMap searchMap) {
        return doSlaveCall(db -> db.one(tClass, tableName, searchMap));
    }

    @Override
    public <T> T one(Class<T> tClass, String tableName, Number id) {
        return doSlaveCall(db -> db.one(tClass, tableName, id));
    }

    @Override
    public List<NeoMap> list(String tableName, Columns columns, NeoMap searchMap) {
        return doSlaveCall(db -> db.list(tableName, columns, searchMap));
    }

    @Override
    public List<NeoMap> list(String tableName, Columns columns, Express searchExpress) {
        return doSlaveCall(db -> db.list(tableName, columns, searchExpress));
    }

    @Override
    public <T> List<T> list(String tableName, Columns columns, T entity) {
        return doSlaveCall(db -> db.list(tableName, columns, entity));
    }

    @Override
    public List<NeoMap> list(String tableName, NeoMap searchMap) {
        return doSlaveCall(db -> db.list(tableName, searchMap));
    }

    @Override
    public <T> List<T> list(String tableName, T entity) {
        return doSlaveCall(db -> db.list(tableName, entity));
    }

    @Override
    public List<NeoMap> list(String tableName, Columns columns) {
        return doSlaveCall(db -> db.list(tableName, columns));
    }

    @Override
    public List<NeoMap> list(String tableName, Express searchExpress){
        return doSlaveCall(db -> db.list(tableName, searchExpress));
    }

    @Override
    public <T> List<T> list(Class<T> tClass, String tableName, Columns columns, NeoMap searchMap) {
        return doSlaveCall(db -> db.list(tClass, tableName, columns, searchMap));
    }

    @Override
    public <T> List<T> list(Class<T> tClass, String tableName, Columns columns, Express express) {
        return doSlaveCall(db -> db.list(tClass, tableName, columns, express));
    }

    @Override
    public <T> List<T> list(Class<T> tClass, String tableName, NeoMap searchMap) {
        return doSlaveCall(db -> db.list(tClass, tableName, searchMap));
    }

    @Override
    public <T> List<T> list(Class<T> tClass, String tableName, Columns columns) {
        return doSlaveCall(db -> db.list(tClass, tableName, columns));
    }

    @Override
    public <T> List<T> list(Class<T> tClass, String tableName, Express searchExpress) {
        return doSlaveCall(db -> db.list(tClass, tableName, searchExpress));
    }

    @Override
    @Deprecated
    public <T> T value(String tableName, Class<T> tClass, String field, NeoMap searchMap) {
        return doSlaveCall(db -> db.value(tableName, tClass, field, searchMap));
    }

    @Override
    public <T> T value(Class<T> tClass, String tableName, String field, NeoMap searchMap) {
        return doSlaveCall(db -> db.value(tClass, tableName, field, searchMap));
    }

    @Override
    public <T> T value(Class<T> tClass, String tableName, String field, Express searchExpress) {
        return doSlaveCall(db -> db.value(tClass, tableName, field, searchExpress));
    }

    @Override
    @Deprecated
    public <T> T value(String tableName, Class<T> tClass, String field, Object entity) {
        return doSlaveCall(db -> db.value(tableName, tClass, field, entity));
    }

    @Override
    public <T> T value(Class<T> tClass, String tableName, String field, Object entity) {
        return doSlaveCall(db -> db.value(tClass, tableName, field, entity));
    }

    @Override
    public String value(String tableName, String field, NeoMap searchMap) {
        return doSlaveCall(db -> db.value(tableName, field, searchMap));
    }

    @Override
    public String value(String tableName, String field, Express searchExpress) {
        return doSlaveCall(db -> db.value(tableName, field, searchExpress));
    }

    @Override
    public String value(String tableName, String field, Object entity) {
        return doSlaveCall(db -> db.value(tableName, field, entity));
    }

    @Override
    public String value(String tableName, String field, Number id) {
        return doSlaveCall(db -> db.value(tableName, field, id));
    }

    @Override
    @Deprecated
    public <T> List<T> values(String tableName, Class<T> tClass, String field, NeoMap searchMap) {
        return doSlaveCall(db -> db.values(tClass, tableName, field, searchMap));
    }

    @Override
    @Deprecated
    public <T> List<T> values(String tableName, Class<T> tClass, String field, Object entity) {
        return doSlaveCall(db -> db.values(tClass, tableName, field, entity));
    }

    @Override
    public <T> List<T> values(Class<T> tClass, String tableName, String field, NeoMap searchMap) {
        return doSlaveCall(db -> db.values(tClass, tableName, field, searchMap));
    }

    @Override
    public <T> List<T> values(Class<T> tClass, String tableName, String field, Express searchExpress) {
        return doSlaveCall(db -> db.values(tClass, tableName, field, searchExpress));
    }

    @Override
    public <T> List<T> values(Class<T> tClass, String tableName, String field, Object entity) {
        return doSlaveCall(db -> db.values(tClass, tableName, field, entity));
    }

    @Override
    public List<String> values(String tableName, String field, NeoMap searchMap) {
        return doSlaveCall(db -> db.values(tableName, field, searchMap));
    }

    @Override
    public List<String> values(String tableName, String field, Express searchExpress) {
        return doSlaveCall(db -> db.values(tableName, field, searchExpress));
    }

    @Override
    public List<String> values(String tableName, String field, Object entity) {
        return doSlaveCall(db -> db.values(tableName, field, entity));
    }

    @Override
    public List<String> values(String tableName, String field) {
        return doSlaveCall(db -> db.values(tableName, field));
    }

    @Override
    public List<NeoMap> page(String tableName, Columns columns, NeoMap searchMap, NeoPage page) {
        return doSlaveCall(db -> db.page(tableName, columns, searchMap, page));
    }

    @Override
    public List<NeoMap> page(String tableName, Columns columns, Express searchExpress, NeoPage page){
        return doSlaveCall(db -> db.page(tableName, columns, searchExpress, page));
    }

    @Override
    public <T> List<T> page(String tableName, Columns columns, T entity, NeoPage page) {
        return doSlaveCall(db -> db.page(tableName, columns, entity, page));
    }

    @Override
    public List<NeoMap> page(String tableName, NeoMap searchMap, NeoPage page) {
        return doSlaveCall(db -> db.page(tableName, searchMap, page));
    }

    @Override
    public List<NeoMap> page(String tableName, Express searchExpress, NeoPage page) {
        return doSlaveCall(db -> db.page(tableName, searchExpress, page));
    }

    @Override
    public <T> List<T> page(String tableName, T entity, NeoPage page) {
        return doSlaveCall(db -> db.page(tableName, entity, page));
    }

    @Override
    public List<NeoMap> page(String tableName, Columns columns, NeoPage page) {
        return doSlaveCall(db -> db.page(tableName, columns, page));
    }

    @Override
    public List<NeoMap> page(String tableName, NeoPage page) {
        return doSlaveCall(db -> db.page(tableName, page));
    }

    @Override
    public <T> List<T> page(Class<T> tClass, String tableName, Columns columns, NeoMap searchMap, NeoPage page) {
        return doSlaveCall(db -> db.page(tClass, tableName, columns, searchMap, page));
    }

    @Override
    public <T> List<T> page(Class<T> tClass, String tableName, Columns columns, Express searchExpress, NeoPage page) {
        return doSlaveCall(db -> db.page(tClass, tableName, columns, searchExpress, page));
    }

    @Override
    public <T> List<T> page(Class<T> tClass, String tableName, Express searchExpress, NeoPage page) {
        return doSlaveCall(db -> db.page(tClass, tableName, searchExpress, page));
    }

    @Override
    public <T> List<T> page(Class<T> tClass, String tableName, NeoMap searchMap, NeoPage page) {
        return doSlaveCall(db -> db.page(tClass, tableName, searchMap, page));
    }

    @Override
    public <T> List<T> page(Class<T> tClass, String tableName, Columns columns, NeoPage page) {
        return doSlaveCall(db -> db.page(tClass, tableName, columns, page));
    }

    @Override
    public <T> List<T> page(Class<T> tClass, String tableName, NeoPage page) {
        return doSlaveCall(db -> db.page(tClass, tableName, page));
    }

    @Override
    public Integer count(String tableName, NeoMap searchMap) {
        return doSlaveCall(db -> db.count(tableName, searchMap));
    }

    @Override
    public Integer count(String tableName, Express searchExpress) {
        return doSlaveCall(db -> db.count(tableName, searchExpress));
    }

    @Override
    public Integer count(String tableName, Object entity) {
        return doSlaveCall(db -> db.count(tableName, entity));
    }

    @Override
    public Integer count(String tableName) {
        return doSlaveCall(db -> db.count(tableName));
    }

    @Override
    public Boolean exist(String tableName, NeoMap searchMap) {
        return doSlaveCall(db -> db.exist(tableName, searchMap));
    }

    @Override
    public Boolean exist(String tableName, Express searchExpress) {
        return doSlaveCall(db -> db.exist(tableName, searchExpress));
    }

    @Override
    public Boolean exist(String tableName, Object entity) {
        return doSlaveCall(db -> db.exist(tableName, entity));
    }

    @Override
    public Integer batchInsert(String tableName, List<NeoMap> dataMapList) {
        return doMasterCall(db -> db.batchInsert(tableName, dataMapList));
    }

    @Override
    public <T> Integer batchInsertEntity(String tableName, List<T> dataList) {
        return doMasterCall(db -> db.batchInsertEntity(tableName, dataList));
    }

    @Override
    public Integer batchUpdate(String tableName, List<NeoMap> dataList) {
        return doMasterCall(db -> db.batchUpdate(tableName, dataList));
    }

    @Override
    public Integer batchUpdate(String tableName, List<NeoMap> dataList, Columns columns) {
        return doMasterCall(db -> db.batchUpdate(tableName, dataList, columns));
    }

    @Override
    public <T> Integer batchUpdateEntity(String tableName, List<T> dataList) {
        return doMasterCall(db -> db.batchUpdateEntity(tableName, dataList));
    }

    @Override
    public <T> Integer batchUpdateEntity(String tableName, List<T> dataList, Columns columns) {
        return doMasterCall(db -> db.batchUpdateEntity(tableName, dataList, columns));
    }

    @Override
    public TableMap exeOne(String sql, Object... parameters) {
        return doMasterCall(db -> db.exeOne(sql, parameters));
    }

    @Override
    public <T> T exeOne(Class<T> tClass, String sql, Object... parameters) {
        return doMasterCall(db -> db.exeOne(tClass, sql, parameters));
    }

    @Override
    public List<TableMap> exeList(String sql, Object... parameters) {
        return doMasterCall(db -> db.exeList(sql, parameters));
    }

    @Override
    public <T> List<T> exeList(Class<T> tClass, String sql, Object... parameters) {
        return doMasterCall(db -> db.exeList(tClass, sql, parameters));
    }

    @Override
    public <T> T exeValue(Class<T> tClass, String sql, Object... parameters) {
        return doMasterCall(db -> db.exeValue(tClass, sql, parameters));
    }

    @Override
    public String exeValue(String sql, Object... parameters) {
        return doMasterCall(db -> db.exeValue(sql, parameters));
    }

    @Override
    public <T> List<T> exeValues(Class<T> tClass, String sql, Object... parameters) {
        return doMasterCall(db -> db.exeValues(tClass, sql, parameters));
    }

    @Override
    public List<String> exeValues(String sql, Object... parameters) {
        return doMasterCall(db -> db.exeValues(sql, parameters));
    }

    @Override
    public List<TableMap> exePage(String sql, Integer startIndex, Integer pageSize, Object... parameters) {
        return doMasterCall(db -> db.exePage(sql, startIndex, pageSize, parameters));
    }

    @Override
    public <T> List<T> exePage(Class<T> tClass, String sql, Integer startIndex, Integer pageSize, Object... parameters) {
        return doMasterCall(db -> db.exePage(tClass, sql, startIndex, pageSize, parameters));
    }

    @Override
    public List<TableMap> exePage(String sql, NeoPage neoPage, Object... parameters) {
        return doMasterCall(db -> db.exePage(sql, neoPage, parameters));
    }

    @Override
    public <T> List<T> exePage(Class<T> tClass, String sql, NeoPage neoPage, Object... parameters) {
        return doMasterCall(db -> db.exePage(tClass, sql, neoPage, parameters));
    }

    @Override
    public Integer exeCount(String sql, Object... parameters) {
        return doMasterCall(db -> db.exeCount(sql, parameters));
    }

    @Override
    public List<List<TableMap>> execute(String sql, Object... parameters) {
        return doMasterCall(db -> db.execute(sql, parameters));
    }

    /**
     * 内部主库调用
     *
     * @param function 回调处理
     * @param <T>      返回类型
     * @return 返回值
     */
    private <T> T doMasterCall(Function<AbstractExecutorDb, T> function) {
        MasterSlaveNeo.InnerActiveDb db = selectMasterDb();
        try {
            return function.apply(db.getDb());
        } catch (NeoException e) {
            deActiveMaster(db.getName());
            if (null != ExceptionUtil.getCause(e, SQLTransientConnectionException.class) || null != ExceptionUtil.getCause(e, CommunicationsException.class)) {
                log.warn(MS_LOG_PRE + "主库({}) 异常, 切库", db.getName(), e);
                return doMasterCall(function);
            }
            throw e;
        }
    }

    /**
     * 内部从库调用
     *
     * @param function 回调处理
     * @param <T>      返回类型
     * @return 返回值
     */
    private <T> T doSlaveCall(Function<AbstractExecutorDb, T> function) {
        MasterSlaveNeo.InnerActiveDb db = selectSlaveDb();
        try {
            return function.apply(db.getDb());
        } catch (NeoException e) {
            deActiveSlave(db.getName());
            if (null != ExceptionUtil.getCause(e, SQLTransientConnectionException.class) || null != ExceptionUtil.getCause(e, CommunicationsException.class)) {
                log.warn(MS_LOG_PRE + "从库({}) 异常, 切库", db.getName(), e);
                return doSlaveCall(function);
            }
            throw e;
        }
    }
}
