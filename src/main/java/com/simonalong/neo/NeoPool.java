package com.simonalong.neo;

import com.simonalong.neo.core.AbstractBaseDb;
import com.simonalong.neo.db.NeoPage;
import com.simonalong.neo.exception.NeoException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 多db管理对象
 *
 * @author zhouzhenyong
 * @since 2019/6/21 下午3:48
 */
public final class NeoPool extends AbstractBaseDb {

    private static Map<String, Neo> neoMap = new ConcurrentHashMap<>(8);
    /**
     * key：表名，value：列名
     */
    private static Map<String, String> tableColumnMap = new ConcurrentHashMap<>(8);
    private Integer min=0;
    private Integer max=0;
    private static final NeoPool INSTANCE = new NeoPool();

    public static NeoPool getInstance() {
        return INSTANCE;
    }

    /**
     * 添加分库
     *
     * @param devideDbNames 分库的db分库表达式，{0, 12}作为后缀。比如：xxx{0, 12}
     * @param neoList       db的列表
     * @param columnName    列名
     * @return pool对象
     */
    public NeoPool addDevideDbList(String devideDbNames, List<Neo> neoList, String columnName) {
        String regex = "^(.*)\\{(\\d),.*(\\d)}$";
        Matcher matcher = Pattern.compile(regex).matcher(devideDbNames);

        while (matcher.find()) {
            String dbName = matcher.group(1);
            min = Integer.valueOf(matcher.group(2));
            max = Integer.valueOf(matcher.group(3));
            if(min >= max){
                throw new NeoException("");
            }
            for (Integer index = min, indexJ = 0; index <= max; index++, indexJ++) {
                neoMap.putIfAbsent(dbName + index, neoList.get(indexJ));
            }
            tableColumnMap.put(dbName, columnName);
        }
        return this;
    }

    @Override
    public NeoMap insert(String tableName, NeoMap dataMap) {
        return null;
    }

    @Override
    public <T> T insert(String tableName, T object) {
        return null;
    }

    @Override
    public Integer delete(String tableName, NeoMap searchMap) {
        return null;
    }

    @Override
    public <T> Integer delete(String tableName, T object) {
        return null;
    }

    @Override
    public Integer delete(String tableName, Number id) {
        return null;
    }

    @Override
    public NeoMap update(String tableName, NeoMap dataMap, NeoMap searchMap) {
        return null;
    }

    @Override
    public <T> T update(String tableName, T setEntity, NeoMap searchMap) {
        return null;
    }

    @Override
    public <T> T update(String tableName, T setEntity, T searchEntity) {
        return null;
    }

    @Override
    public <T> NeoMap update(String tableName, NeoMap setMap, T searchEntity) {
        return null;
    }

    @Override
    public NeoMap update(String tableName, NeoMap dataMap, Columns columns) {
        return null;
    }

    @Override
    public <T> T update(String tableName, T entity, Columns columns) {
        return null;
    }

    @Override
    public NeoMap update(String tableName, NeoMap dataMap) {
        return null;
    }

    @Override
    public <T> T update(String tableName, T entity) {
        return null;
    }

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
    public <T> T value(String tableName, Class<T> tClass, String field, NeoMap searchMap) {
        return null;
    }

    @Override
    public <T> T value(String tableName, Class<T> tClass, String field, Object entity) {
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
    public String value(String tableName, String field, Number entity) {
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
    public List<NeoMap> page(String tableName, Columns columns, NeoMap searchMap) {
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
    public List<NeoMap> page(String tableName, NeoMap searchMap) {
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
    public Integer batchInsert(String tableName, List<NeoMap> dataMapList) {
        return null;
    }

    @Override
    public <T> Integer batchInsertEntity(String tableName, List<T> dataList) {
        return null;
    }

    @Override
    public Integer batchUpdate(String tableName, List<NeoMap> dataList) {
        return null;
    }

    @Override
    public Integer batchUpdate(String tableName, List<NeoMap> dataList, Columns columns) {
        return null;
    }

    @Override
    public <T> Integer batchUpdateEntity(String tableName, List<T> dataList) {
        return null;
    }

    @Override
    public <T> Integer batchUpdateEntity(String tableName, List<T> dataList, Columns columns) {
        return null;
    }

    @Override
    public TableMap exeOne(String sql, Object... parameters) {
        return null;
    }

    @Override
    public <T> T exeOne(Class<T> tClass, String sql, Object... parameters) {
        return null;
    }

    @Override
    public List<TableMap> exeList(String sql, Object... parameters) {
        return null;
    }

    @Override
    public <T> List<T> exeList(Class<T> tClass, String sql, Object... parameters) {
        return null;
    }

    @Override
    public <T> T exeValue(Class<T> tClass, String sql, Object... parameters) {
        return null;
    }

    @Override
    public String exeValue(String sql, Object... parameters) {
        return null;
    }

    @Override
    public <T> List<T> exeValues(Class<T> tClass, String sql, Object... parameters) {
        return null;
    }

    @Override
    public List<String> exeValues(String sql, Object... parameters) {
        return null;
    }

    @Override
    public List<TableMap> exePage(String sql, Integer startIndex, Integer pageSize, Object... parameters) {
        return null;
    }

    @Override
    public List<TableMap> exePage(String sql, NeoPage neoPage, Object... parameters) {
        return null;
    }

    @Override
    public Integer exeCount(String sql, Object... parameters) {
        return null;
    }

    @Override
    public List<List<TableMap>> execute(String sql, Object... parameters) {
        return null;
    }

    private Neo getDevideDb(Object value) {

    }
}
