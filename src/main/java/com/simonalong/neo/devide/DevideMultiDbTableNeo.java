package com.simonalong.neo.devide;

import com.simonalong.neo.Columns;
import com.simonalong.neo.Neo;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.db.NeoPage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 分库分表中的多库多表查询处理
 *
 * @author shizi
 * @since 2020/6/11 2:41 PM
 */
public class DevideMultiDbTableNeo extends AbstractBaseQuery {

    /**
     * 待分库的库集合
     */
    private List<Neo> dbList = new ArrayList<>();
    /**
     * 表的哈希处理映射, key：表名，value表的哈希信息
     */
    private Map<String, TableDevideConfig> devideTableInfoMap = new ConcurrentHashMap<>();

    @Override
    public NeoMap one(String tableName, Columns columns, NeoMap searchMap) {
        return null;
    }

    @Override
    public <T> T one(String tableName, Columns columns, T entity) {
        return null;
    }

    @Override
    public NeoMap one(String tableName, Columns columns, Number key) {
        return null;
    }

    @Override
    public NeoMap one(String tableName, NeoMap searchMap) {
        return null;
    }

    @Override
    public <T> T one(String tableName, T entity) {
        return null;
    }

    @Override
    public NeoMap one(String tableName, Number id) {
        return null;
    }

    @Override
    public <T> T one(Class<T> tClass, String tableName, Columns columns, NeoMap searchMap) {
        return null;
    }

    @Override
    public <T> T one(Class<T> tClass, String tableName, Columns columns, Number key) {
        return null;
    }

    @Override
    public <T> T one(Class<T> tClass, String tableName, NeoMap searchMap) {
        return null;
    }

    @Override
    public <T> T one(Class<T> tClass, String tableName, Number id) {
        return null;
    }

    @Override
    public List<NeoMap> list(String tableName, Columns columns, NeoMap searchMap) {
        return null;
    }

    @Override
    public <T> List<T> list(String tableName, Columns columns, T entity) {
        return null;
    }

    @Override
    public List<NeoMap> list(String tableName, NeoMap searchMap) {
        return null;
    }

    @Override
    public <T> List<T> list(String tableName, T entity) {
        return null;
    }

    @Override
    public List<NeoMap> list(String tableName, Columns columns) {
        return null;
    }

    @Override
    public <T> List<T> list(Class<T> tClass, String tableName, Columns columns, NeoMap searchMap) {
        return null;
    }

    @Override
    public <T> List<T> list(Class<T> tClass, String tableName, NeoMap searchMap) {
        return null;
    }

    @Override
    public <T> List<T> list(Class<T> tClass, String tableName, Columns columns) {
        return null;
    }

    @Override
    public <T> T value(String tableName, Class<T> tClass, String field, NeoMap searchMap) {
        return null;
    }

    @Override
    public <T> T value(String tableName, Class<T> tClass, String field, Object entity) {
        return null;
    }

    @Override
    public <T> T value(Class<T> tClass, String tableName, String field, NeoMap searchMap) {
        return null;
    }

    @Override
    public <T> T value(Class<T> tClass, String tableName, String field, Object entity) {
        return null;
    }

    @Override
    public String value(String tableName, String field, NeoMap searchMap) {
        return null;
    }

    @Override
    public String value(String tableName, String field, Object entity) {
        return null;
    }

    @Override
    public String value(String tableName, String field, Number id) {
        return null;
    }

    @Override
    public <T> List<T> values(String tableName, Class<T> tClass, String field, NeoMap searchMap) {
        return null;
    }

    @Override
    public <T> List<T> values(String tableName, Class<T> tClass, String field, Object entity) {
        return null;
    }

    @Override
    public <T> List<T> values(Class<T> tClass, String tableName, String field, NeoMap searchMap) {
        return null;
    }

    @Override
    public <T> List<T> values(Class<T> tClass, String tableName, String field, Object entity) {
        return null;
    }

    @Override
    public List<String> values(String tableName, String field, NeoMap searchMap) {
        return null;
    }

    @Override
    public List<String> values(String tableName, String field, Object entity) {
        return null;
    }

    @Override
    public List<String> values(String tableName, String field) {
        return null;
    }

    @Override
    public List<NeoMap> page(String tableName, Columns columns, NeoMap searchMap, NeoPage page) {
        return null;
    }

    @Override
    public <T> List<T> page(String tableName, Columns columns, T entity, NeoPage page) {
        return null;
    }

    @Override
    public List<NeoMap> page(String tableName, NeoMap searchMap, NeoPage page) {
        return null;
    }

    @Override
    public <T> List<T> page(String tableName, T entity, NeoPage page) {
        return null;
    }

    @Override
    public List<NeoMap> page(String tableName, Columns columns, NeoPage page) {
        return null;
    }

    @Override
    public List<NeoMap> page(String tableName, NeoPage page) {
        return null;
    }

    @Override
    public <T> List<T> page(Class<T> tClass, String tableName, Columns columns, NeoMap searchMap, NeoPage page) {
        return null;
    }

    @Override
    public <T> List<T> page(Class<T> tClass, String tableName, NeoMap searchMap, NeoPage page) {
        return null;
    }

    @Override
    public <T> List<T> page(Class<T> tClass, String tableName, Columns columns, NeoPage page) {
        return null;
    }

    @Override
    public <T> List<T> page(Class<T> tClass, String tableName, NeoPage page) {
        return null;
    }

    @Override
    public Integer count(String tableName, NeoMap searchMap) {
        return null;
    }

    @Override
    public Integer count(String tableName, Object entity) {
        return null;
    }

    @Override
    public Integer count(String tableName) {
        return null;
    }

    @Override
    public Boolean exist(String tableName, NeoMap searchMap) {
        return null;
    }

    @Override
    public Boolean exist(String tableName, Object entity) {
        return null;
    }
}
