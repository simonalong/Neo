package com.simon.neo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javafx.util.Pair;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhouzhenyong
 * @since 2019/3/3 下午2:53
 */
public class Neo {

    Logger log = LoggerFactory.getLogger(Neo.class);

    private Map<String, NeoDb> dbMap = new ConcurrentHashMap<>();
    /**
     * 默认的名称转换
     */
    private String defaultDbName = "default";

    private static final String SELECT = "select";
    private ConnectPool pool;

    private Neo(){}

    public static Neo connect(String url, String username, String password) throws SQLException {
        Neo neo = new Neo();
        Properties properties = new Properties();
        properties.setProperty("URL", url);
        properties.setProperty("user", username);
        properties.setProperty("password", password);
        neo.pool = new ConnectPool(properties);
        return neo;
    }

    public static Neo connect(String url, String username, String password, Properties properties) throws SQLException {
        Neo neo = new Neo();
        Properties baseProperties = new Properties();
        baseProperties.setProperty("URL", url);
        baseProperties.setProperty("user", username);
        baseProperties.setProperty("password", password);
        baseProperties.putAll(properties);
        neo.pool = new ConnectPool(baseProperties);
        return neo;
    }

    public static Neo connect(String propertiesClassPath) {
        Neo neo = new Neo();
        neo.pool = new ConnectPool(propertiesClassPath);
        return neo;
    }

    public static Neo connect(Properties properties) {
        Neo neo = new Neo();
        neo.pool = new ConnectPool(properties);
        return neo;
    }

    public static Neo connect(DataSource dataSource) {
        Neo neo = new Neo();
        neo.pool = new ConnectPool(dataSource);
        return neo;
    }

    public NeoTable getTable(){
        return new NeoTable();
    }

    /**
     * 数据插入
     * @param tableName 表名
     * @param neoMap 待插入的数据
     * @return 插入之后的返回值
     */
    public NeoMap insert(String tableName, NeoMap neoMap) {
        if (!NeoMap.isEmpty(neoMap)) {
            executeUpdate(neoMap, NeoMap.of(), generateInsertSql(tableName, neoMap));
        }
        return neoMap;
    }

    @SuppressWarnings("unchecked")
    public <T> T insert(String tableName, T entity) {
        return insert(tableName, NeoMap.from(entity)).as((Class<T>) entity.getClass());
    }

    /**
     * 数据删除
     * @param tableName 表名
     * @param searchMap where 后面的条件数据
     * @return 插入之后的返回值
     */
    public Integer delete(String tableName, NeoMap searchMap) {
        if (!NeoMap.isEmpty(searchMap)) {
            executeUpdate(NeoMap.of(), searchMap, generateDeleteSql(tableName, searchMap));
        }
        return 1;
    }

    public <T> Integer delete(String tableName, T entity) {
        return delete(tableName, NeoMap.from(entity));
    }

    /**
     * 数据更新
     * @param tableName 表名
     * @param dataMap set的更新的数据
     * @param searchMap where后面的语句条件数据
     * @return 更新之后的返回值
     */
    public NeoMap update(String tableName, NeoMap dataMap, NeoMap searchMap) {
        executeUpdate(dataMap, searchMap, generateUpdateSql(tableName, dataMap, searchMap));
        dataMap.putAll(searchMap);
        return one(tableName, dataMap);
    }

    @SuppressWarnings("unchecked")
    public <T> T update(String tableName, T setEntity, NeoMap searchMap) {
        return update(tableName, NeoMap.from(setEntity), searchMap).as((Class<T>) setEntity.getClass());
    }

    public <T> T update(String tableName, T setEntity, T searchEntity) {
        return update(tableName, setEntity, NeoMap.from(searchEntity));
    }

    public <T> NeoMap update(String tableName, NeoMap setMap, T searchEntity) {
        return update(tableName, setMap, NeoMap.from(searchEntity));
    }

    /**
     * 查询一行的数据
     * @param sql 只接收select 方式
     * @param parameters 参数
     * @return 一个结果Map
     */
    public NeoMap exeOne(String sql, Object... parameters){
        if (startWithSelect(sql)){
            return executeQuery(generateOneSqlPair(sql, Arrays.asList(parameters)));
        }
        return NeoMap.of();
    }

    public <T> T exeOne(Class<T> tClass, String sql, Object... parameters){
        return exeOne(sql, parameters).as(tClass);
    }

    /**
     * 查询一行实体数据
     * @param tableName 表名
     * @param columns 列名
     * @param searchMap 搜索条件
     * @param tailSql sql尾部后缀
     * @return 返回一个实体的Map影射
     */
    public NeoMap one(String tableName, NeoColumn columns, NeoMap searchMap, String tailSql) {
        return executeQuery(generateOneSqlPair(tableName, columns, searchMap, tailSql));
    }

    @SuppressWarnings("unchecked")
    public <T> T one(String tableName, NeoColumn columns, T entity, String tailSql){
        return one(tableName, columns, NeoMap.from(entity), tailSql).as((Class<T>) entity.getClass());
    }

    public NeoMap one(String tableName, NeoMap searchMap, String tailSql){
        return one(tableName, null, searchMap, tailSql);
    }

    public <T> T one(String tableName, T entity, String tailSql){
        return one(tableName, null, entity, tailSql);
    }

    public NeoMap one(String tableName, NeoColumn columns, NeoMap searchMap){
        return one(tableName, columns, searchMap, null);
    }

    public <T> T one(String tableName, NeoColumn columns, T entity){
        return one(tableName, columns, entity, null);
    }

    public NeoMap one(String tableName, NeoMap searchMap){
        return one(tableName, null, searchMap);
    }

    public <T> T one(String tableName, T entity){
        return one(tableName, null, entity);
    }

    /**
     * 查询一行的数据
     * @param sql 只接收select 方式
     * @param parameters 参数
     * @return 一个结果Map
     */
    public List<NeoMap> exeList(String sql, Object... parameters){
        if (startWithSelect(sql)){
            return executeQueryList(generateListSqlPair(sql, Arrays.asList(parameters)));
        }
        return new ArrayList<>();
    }

    public <T> List<T> exeList(Class<T> tClass, String sql, Object... parameters){
        return NeoMap.asArray(exeList(sql, parameters), tClass);
    }

    /**
     * 查询具体的数据列表
     * @param tableName 表名
     * @param columns 列数据
     * @param searchMap 搜索条件
     * @param tailSql 尾部sql
     * @return 返回一列数据
     */
    public List<NeoMap> list(String tableName, NeoColumn columns, NeoMap searchMap, String tailSql) {
        return executeQueryList(generateListSqlPair(tableName, columns, searchMap, tailSql));
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> list(String tableName, NeoColumn columns, T entity, String tailSql){
        return NeoMap.asArray(list(tableName, columns, NeoMap.from(entity), tailSql), (Class<T>) entity.getClass());
    }

    public List<NeoMap> list(String tableName, NeoMap searchMap, String tailSql){
        return list(tableName, null, searchMap, tailSql);
    }

    public <T> List<T> list(String tableName, T entity, String tailSql){
        return list(tableName, null, entity, tailSql);
    }

    public List<NeoMap> list(String tableName, NeoColumn columns, NeoMap searchMap){
        return list(tableName, columns, searchMap, null);
    }

    public <T> List<T> list(String tableName, NeoColumn columns, T entity){
        return list(tableName, columns, entity, null);
    }

    public List<NeoMap> list(String tableName, NeoMap searchMap){
        return list(tableName, searchMap, null);
    }

    public <T> List<T> list(String tableName, T entity){
        return list(tableName, entity, null);
    }

    /**
     * 查询一行的数据
     * @param sql 只接收select 方式
     * @param parameters 参数
     * @return 一个结果Map
     */
    public <T> T exeValue(Class<T> tClass, String sql, Object... parameters) {
        Iterator<Object> iterator = executeQuery(generateValueSqlPair(sql, Arrays.asList(parameters))).values().iterator();
        return iterator.hasNext() ? asObject(tClass, iterator.next()) : null;
    }

    public String exeValue(String sql, Object... parameters){
        return exeValue(String.class, sql, parameters);
    }

    /**
     * 查询某行某列的值
     * @param tableName 表名
     * @param tClass 返回值的类型
     * @param field 某个属性的名字
     * @param searchMap 搜索条件
     * @param tailSql 尾部sql，比如：order by `xxx`
     * @return 指定的数据值
     */
    public <T> T value(String tableName, Class<T> tClass, String field, NeoMap searchMap, String tailSql){
        if (null != tClass && !NeoMap.isEmpty(searchMap)) {
            Iterator<Object> iterator = executeQuery(generateValueSqlPair(tableName, field, searchMap, tailSql)).values().iterator();
            return iterator.hasNext() ? asObject(tClass, iterator.next()) : null;
        }
        return null;
    }

    public <T> T value(String tableName, Class<T> tClass, String field, Object entity, String tailSql) {
        return value(tableName, tClass, field, NeoMap.from(entity), tailSql);
    }

    public <T> T value(String tableName, Class<T> tClass, String field, NeoMap searchMap) {
        return value(tableName, tClass, field, searchMap, null);
    }

    public <T> T value(String tableName, Class<T> tClass, String field, Object entity) {
        return value(tableName, tClass, field, entity, null);
    }

    public String value(String tableName, String field, NeoMap searchMap, String tailSql){
        return value(tableName, String.class, field, searchMap, tailSql);
    }

    public String value(String tableName, String field, Object entity, String tailSql) {
        return value(tableName, String.class, field, entity, tailSql);
    }

    public String value(String tableName, String field, NeoMap searchMap) {
        return value(tableName, field, searchMap, null);
    }

    public String value(String tableName, String field, Object entity) {
        return value(tableName, field, entity, null);
    }

    /**
     * 查询一行的数据
     * @param tClass 数据实体的类
     * @param sql 查询一行的sql
     * @param parameters 查询的搜索参数
     * @param <T> 数据实体的类型
     * @return 查询到的数据实体，如果没有找到则返回null
     */
    public <T> List<T> exeValues(Class<T> tClass, String sql, Object... parameters) {
        List<NeoMap> resultList = executeQueryList(generateValuesSqlPair(sql, Arrays.asList(parameters)));
        if (!resultList.isEmpty()) {
            return resultList.stream().map(r -> {
                Iterator<Object> it = r.values().iterator();
                return it.hasNext() ? asObject(tClass, it.next()) : null;
            }).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public List<String> exeValues(String sql, Object... parameters){
        return exeValues(String.class, sql, parameters);
    }

    /**
     * 查询一列的值
     * @param tableName 表名
     * @param tClass 实体类的类
     * @param field 列名
     * @param searchMap 搜索条件
     * @param tailSql sql尾部，比如order by `xxx`
     * @return 一列值
     */
    public <T> List<T> values(String tableName, Class<T> tClass, String field, NeoMap searchMap, String tailSql){
        if (null != tClass && !NeoMap.isEmpty(searchMap)) {
            List<NeoMap> resultList = executeQueryList(generateValuesSqlPair(tableName, field, searchMap, tailSql));
            if(!resultList.isEmpty()){
                return resultList.stream()
                    .map(r -> asObject(tClass, r.get(field)))
                    .filter(Objects::nonNull).distinct()
                    .collect(Collectors.toList());
            }
        }
        return null;
    }

    public <T> List<T> values(String tableName, Class<T> tClass, String field, Object entity, String tailSql) {
        return values(tableName, tClass, field, NeoMap.from(entity), tailSql);
    }

    public <T> List<T> values(String tableName, Class<T> tClass, String field, NeoMap searchMap) {
        return values(tableName, tClass, field, searchMap, null);
    }

    public <T> List<T> values(String tableName, Class<T> tClass, String field, Object entity) {
        return values(tableName, tClass, field, entity, null);
    }

    public List<String> values(String tableName, String field, NeoMap searchMap, String tailSql) {
        return values(tableName, String.class, field, searchMap, tailSql);
    }

    public List<String> values(String tableName, String field, Object entity, String tailSql) {
        return values(tableName, String.class, field, entity, tailSql);
    }

    public List<String> values(String tableName, String field, NeoMap searchMap) {
        return values(tableName, String.class, field, searchMap);
    }

    public List<String> values(String tableName, String field, Object entity) {
        return values(tableName, String.class, field, entity, null);
    }

    /**
     * 分组数据
     * @param tableName 表名
     * @param columns   列的属性
     * @param searchMap 搜索条件
     * @param tailSql   sql后缀，比如：order by `xxx`
     * @param startIndex 分页起始
     * @param pageSize  分页大小
     * @return 分页对应的数据
     */
    public List<NeoMap> page(String tableName, NeoColumn columns, NeoMap searchMap, String tailSql, Integer startIndex,
        Integer pageSize) {
        return executeQueryList(generatePageSqlPair(tableName, columns, searchMap, tailSql, startIndex, pageSize));
    }

    public List<NeoMap> page(String tableName, NeoMap searchMap, String tailSql, Integer startIndex, Integer pageSize){
        return page(tableName, null, searchMap, tailSql, startIndex, pageSize);
    }

    public List<NeoMap> page(String tableName, NeoColumn columns, NeoMap searchMap, Integer startIndex, Integer pageSize){
        return page(tableName, columns, searchMap, null, startIndex, pageSize);
    }

    public List<NeoMap> page(String tableName, NeoMap searchMap, Integer startIndex, Integer pageSize){
        return page(tableName, null, searchMap, startIndex, pageSize);
    }

    public List<NeoMap> page(String tableName, NeoColumn columns, NeoMap searchMap, String tailSql, NeoPage page){
        return page(tableName, columns, searchMap, tailSql, page.startIndex(), page.pageSize());
    }

    public List<NeoMap> page(String tableName, NeoMap searchMap, String tailSql, NeoPage page){
        return page(tableName, null, searchMap, tailSql, page.startIndex(), page.pageSize());
    }

    public List<NeoMap> page(String tableName, NeoColumn columns, NeoMap searchMap, NeoPage page){
        return page(tableName, columns, searchMap, null, page.startIndex(), page.pageSize());
    }

    public List<NeoMap> page(String tableName, NeoMap searchMap, NeoPage page){
        return page(tableName, null, searchMap, page.startIndex(), page.pageSize());
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> page(String tableName, NeoColumn columns, T entity, String tailSql, Integer startIndex,
        Integer pageSize) {
        return NeoMap.asArray(page(tableName, columns, NeoMap.from(entity), tailSql, startIndex, pageSize),
            (Class<T>) entity.getClass());
    }

    public <T> List<T> page(String tableName, T entity, String tailSql, Integer startIndex, Integer pageSize){
        return page(tableName, null, entity, tailSql, startIndex, pageSize);
    }

    public <T> List<T> page(String tableName, NeoColumn columns, T entity, Integer startIndex, Integer pageSize){
        return page(tableName, columns, entity, null, startIndex, pageSize);
    }

    public <T> List<T> page(String tableName, T entity, Integer startIndex, Integer pageSize){
        return page(tableName, null, entity, startIndex, pageSize);
    }

    public <T> List<T> page(String tableName, NeoColumn columns, T entity, String tailSql, NeoPage page){
        return page(tableName, columns, entity, tailSql, page.startIndex(), page.pageSize());
    }

    public <T> List<T> page(String tableName, T entity, String tailSql, NeoPage page){
        return page(tableName, null, entity, tailSql, page.startIndex(), page.pageSize());
    }

    public <T> List<T> page(String tableName, NeoColumn columns, T entity, NeoPage page){
        return page(tableName, columns, entity, null, page.startIndex(), page.pageSize());
    }

    public <T> List<T> page(String tableName, T entity, NeoPage page){
        return page(tableName, null, entity, page.startIndex(), page.pageSize());
    }

    /**
     * 查询一行的数据
     * @param sql 只接收select 方式
     * @param parameters 参数
     * @return 一个结果Map
     */
    public Integer exeCount(String sql, Object... parameters) {
        Iterator<Object> iterator = executeQuery(generateValueSqlPair(sql, Arrays.asList(parameters))).values()
            .iterator();
        return iterator.hasNext() ? Integer.valueOf(asString(iterator.next())) : null;
    }

    public Integer count(String tableName) {
        return count(tableName, NeoMap.of());
    }

    public Integer count(String tableName, Object entity) {
        return count(tableName, NeoMap.from(entity));
    }

    public Integer count(String tableName, NeoMap searchMap) {
        Iterator<Object> it = executeQuery(generateCountSqlPair(tableName, searchMap)).values().iterator();
        return it.hasNext() ? Integer.valueOf(asString(it.next())) : null;
    }

    /**
     * 执行更新的语句
     * @param dataMap 实体数据
     * @param searchMap where 后面搜索的数据
     * @param sql 拼接后的sql
     */
    private void executeUpdate(NeoMap dataMap, NeoMap searchMap, String sql){
        try(Connection con = pool.getConnect()){
            try (PreparedStatement statement = con.prepareStatement(sql)) {

                List<String> fieldList = new ArrayList<>(dataMap.keySet());
                int i, dataSize = fieldList.size();
                for (i = 0; i < dataSize; i++) {
                    statement.setObject(i + 1, dataMap.get(fieldList.get(i)));
                }

                List<String> whereFieldList = new ArrayList<>(searchMap.keySet());
                for (i = 0; i < whereFieldList.size(); i++) {
                    statement.setObject(i + 1 + dataSize, searchMap.get(whereFieldList.get(i)));
                }
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行查询
     * @param sqlPair 拼接后的sql和sql中对应的查询参数列表组成的一组
     * @return 执行查询后查询的数据
     */
    private NeoMap executeQuery(Pair<String, List<Object>> sqlPair) {
        if(null == sqlPair){
            return NeoMap.of();
        }
        String sql = sqlPair.getKey();
        List<Object> parameters = sqlPair.getValue();
        NeoMap result = NeoMap.of();
        try(Connection con = pool.getConnect()){
            try (PreparedStatement statement = con.prepareStatement(sql)) {

                for (int i = 0; i < parameters.size(); i++) {
                    statement.setObject(i + 1, parameters.get(i));
                }

                ResultSet rs = statement.executeQuery();
                ResultSetMetaData metaData = rs.getMetaData();
                int col = metaData.getColumnCount();

                if (rs.next()) {
                    for (int j = 1; j <= col; j++) {
                        result.put(metaData.getColumnName(j), rs.getObject(j));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 执行查询
     * @param sqlPair 拼接后的sql和sql中对应的查询参数列表组成的一组
     * @return 执行查询后查询的数据
     */
    private List<NeoMap> executeQueryList(Pair<String, List<Object>> sqlPair) {
        if(null == sqlPair){
            return new ArrayList<>();
        }
        String sql = sqlPair.getKey();
        List<Object> parameters = sqlPair.getValue();
        List<NeoMap> result = new ArrayList<>();
        try (Connection con = pool.getConnect()) {
            try (PreparedStatement statement = con.prepareStatement(sql)) {

                for (int i = 0; i < parameters.size(); i++) {
                    statement.setObject(i + 1, parameters.get(i));
                }

                ResultSet rs = statement.executeQuery();
                ResultSetMetaData metaData = rs.getMetaData();
                int col = metaData.getColumnCount();

                while (rs.next()) {
                    NeoMap row = NeoMap.of();
                    for (int j = 1; j <= col; j++) {
                        row.put(metaData.getColumnName(j), rs.getObject(j));
                    }
                    result.add(row);
                }
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private String generateInsertSql(String tableName, NeoMap neoMap){
        String sql = "insert into "
            + tableName
            + "(`"
            + String.join("`, `", neoMap.keySet())
            + "`) values ("
            + buildValues(neoMap.keySet())
            + ")";
        log.debug(sql);
        return sql;
    }

    /**
     * sql 是select 语句
     */
    private Boolean startWithSelect(String sql){
        return null != sql && sql.startsWith(SELECT);
    }

    private String generateDeleteSql(String tableName, NeoMap neoMap){
        String sql = "delete from " + tableName + buildWhere(neoMap.keySet());
        log.debug(sql);
        return sql;
    }

    private String generateUpdateSql(String tableName, NeoMap dataMap, NeoMap searchMap){
        String sql = "update " + tableName + buildSetValues(dataMap.keySet()) + buildWhere(searchMap.keySet());
        log.debug(sql);
        return sql;
    }

    /**
     * 替换一层转换符：比如%s，生成查询一行数据的sql
     * key: select * from %s where a=? -> select * from xxx where a=?
     * value: 去除跟转换符匹配的数据，剩下的数据列表用于sql的?替换
     */
    private Pair<String, List<Object>> generateOneSqlPair(String sqlOrigin, List<Object> parameters){
        String sql = String.format(sqlOrigin, parameters.toArray()) + selectOneTail();
        log.debug(sql);
        return new Pair<>(sql, cutParameters(sqlOrigin, parameters));
    }

    /**
     * 通过表名和查询参数生成查询一行数据的sql
     */
    private Pair<String, List<Object>> generateOneSqlPair(String tableName, NeoColumn columns, NeoMap searchMap, String tailSql){
        return new Pair<>(generateOneSql(tableName, columns, searchMap, tailSql), generateValueList(searchMap));
    }

    /**
     * 替换一层转换符：比如%s，生成查询一行数据的sql
     * key: select * from %s where a=? -> select * from xxx where a=?
     * value: 去除跟转换符匹配的数据，剩下的数据列表用于sql的?替换
     */
    private Pair<String, List<Object>> generateListSqlPair(String sqlOrigin, List<Object> parameters){
        String sql = String.format(sqlOrigin, parameters.toArray());
        log.debug(sql);
        return new Pair<>(sql, cutParameters(sqlOrigin, parameters));
    }

    /**
     * 通过表名和查询参数生成查询一行数据的sql
     */
    private Pair<String, List<Object>> generateListSqlPair(String tableName, NeoColumn columns, NeoMap searchMap, String tailSql){
        return new Pair<>(generateListSql(tableName, columns, searchMap, tailSql), generateValueList(searchMap));
    }

    /**
     * 通过表名和查询参数生成查询一行数据的sql
     */
    private Pair<String, List<Object>> generatePageSqlPair(String tableName, NeoColumn columns, NeoMap searchMap,
        String tailSql, Integer pageIndex, Integer pageSize) {
        return new Pair<>(generatePageSql(tableName, columns, searchMap, tailSql, pageIndex, pageSize), generateValueList(searchMap));
    }

    /**
     * 通过表名和查询参数生成查询一行数据的sql
     */
    private Pair<String, List<Object>> generateValueSqlPair(String sqlOrigin, List<Object> parameters){
        String sql = String.format(sqlOrigin, parameters.toArray()) + selectOneTail();
        log.debug(sql);
        return new Pair<>(sql, cutParameters(sqlOrigin, parameters));
    }

    /**
     * 通过表名和查询参数生成查询一行数据的sql
     */
    private Pair<String, List<Object>> generateCountSqlPair(String tableName, NeoMap searchMap){
        return new Pair<>(generateCountSql(tableName, searchMap), generateValueList(searchMap));
    }

    /**
     * 通过表名和查询参数生成查询一行数据的sql
     */
    private Pair<String, List<Object>> generateValueSqlPair(String tableName, String field, NeoMap searchMap, String tailSql){
        return new Pair<>(generateValueSql(tableName, field, searchMap, tailSql), generateValueList(searchMap));
    }

    /**
     * 替换一层转换符：比如%s，生成查询一行数据的sql
     * key: select * from %s where a=? -> select * from xxx where a=?
     * value: 去除跟转换符匹配的数据，剩下的数据列表用于sql的?替换
     */
    private Pair<String, List<Object>> generateValuesSqlPair(String sqlOrigin, List<Object> parameters){
        String sql = String.format(sqlOrigin, parameters.toArray());
        log.debug(sql);
        return new Pair<>(sql, cutParameters(sqlOrigin, parameters));
    }

    /**
     * 通过表名和查询参数生成查询一行数据的sql
     */
    private Pair<String, List<Object>> generateValuesSqlPair(String tableName, String field, NeoMap searchMap, String tailSql){
        return new Pair<>(generateValuesSql(tableName, field, searchMap, tailSql), generateValueList(searchMap));
    }

    /**
     * 返回拼接的sql
     * @param tableName 表名
     * @param searchMap 查询参数
     * @return 返回sql，比如：select * from xxx where a=? and b=? order by `xxx` desc limit 1
     */
    private String generateOneSql(String tableName, NeoColumn columns, NeoMap searchMap, String tailSql){
        StringBuilder sqlAppender = new StringBuilder("select ");
        if (!NeoColumn.isEmpty(columns)){
            sqlAppender.append(columns.buildFields());
        }else{
            sqlAppender.append("*");
        }
        sqlAppender.append(" from ").append(tableName).append(buildWhere(searchMap.keySet())).append(" ");
        if(null != tailSql){
            sqlAppender.append(" ").append(tailSql);
        }
        sqlAppender.append(selectOneTail());
        String sql = sqlAppender.toString();
        log.debug(sql);
        return sql;
    }

    /**
     * 返回拼接的sql
     * @param tableName 表名
     * @param searchMap 查询参数
     * @return 返回sql，比如：select * from xxx where a=? and b=? order by `xxx` desc
     */
    private String generateListSql(String tableName, NeoColumn columns, NeoMap searchMap, String tailSql){
        StringBuilder sqlAppender = new StringBuilder("select ");
        if (!NeoColumn.isEmpty(columns)){
            sqlAppender.append(columns.buildFields());
        }else{
            sqlAppender.append("*");
        }
        sqlAppender.append(" from ").append(tableName).append(buildWhere(searchMap.keySet())).append(" ");
        if(null != tailSql){
            sqlAppender.append(" ").append(tailSql);
        }
        String sql = sqlAppender.toString();
        log.debug(sql);
        return sql;
    }

    /**
     * 返回拼接的sql
     * @param tableName 表名
     * @param searchMap 查询参数
     * @return 返回sql，比如：select * from xxx where a=? and b=? order by `xxx` desc
     */
    private String generatePageSql(String tableName, NeoColumn columns, NeoMap searchMap, String tailSql, Integer pageIndex, Integer pageSize){
        StringBuilder sqlAppender = new StringBuilder("select ");
        if (!NeoColumn.isEmpty(columns)){
            sqlAppender.append(columns.buildFields());
        }else{
            sqlAppender.append("*");
        }
        sqlAppender.append(" from ").append(tableName).append(buildWhere(searchMap.keySet())).append(" ");
        if(null != tailSql){
            sqlAppender.append(" ").append(tailSql);
        }
        sqlAppender.append(" limit ").append(pageIndex).append(", ").append(pageSize);
        String sql = sqlAppender.toString();
        log.debug(sql);
        return sql;
    }

    /**
     * 返回拼接的sql
     * @param tableName 表名
     * @param searchMap 查询参数
     * @return select `xxx` from yyy where a=? and b=? limit 1
     */
    private String generateCountSql(String tableName, NeoMap searchMap){
        String sql = "select count(1) from " + tableName + buildWhere(searchMap.keySet()) + selectOneTail();
        log.debug(sql);
        return sql;
    }

    /**
     * 返回拼接的sql
     * @param tableName 表名
     * @param field 要查询的字段
     * @param searchMap 查询参数
     * @return select `xxx` from yyy where a=? and b=? order by `xx` limit 1
     */
    private String generateValueSql(String tableName, String field, NeoMap searchMap, String tailSql){
        StringBuilder sqlAppender = new StringBuilder("select `").append(field).append("` from ").append(tableName)
            .append(buildWhere(searchMap.keySet()));
        if(null != tailSql){
            sqlAppender.append(" ").append(tailSql);
        }
        sqlAppender.append(selectOneTail());
        String sql = sqlAppender.toString();
        log.debug(sql);
        return sql;
    }

    /**
     * 返回拼接的sql
     * @param tableName 表名
     * @param field 要查询的字段
     * @param searchMap 查询参数
     * @return select `xxx` from yyy where a=? and b=? order by `xx` limit 1
     */
    private String generateValuesSql(String tableName, String field, NeoMap searchMap, String tailSql){
        StringBuilder sqlAppender = new StringBuilder("select `").append(field).append("` from ").append(tableName)
            .append(buildWhere(searchMap.keySet()));
        if(null != tailSql){
            sqlAppender.append(" ").append(tailSql);
        }
        String sql = sqlAppender.toString();
        log.debug(sql);
        return sql;
    }

    private String selectOneTail(){
        return " limit 1";
    }

    private List<Object> generateValueList(NeoMap searchMap){
        if (NeoMap.isEmpty(searchMap)) {
            return Collections.emptyList();
        }
        return new ArrayList<>(searchMap.values());
    }

    /**
     * 截取转换符之后的数据
     * @param sqlOrigin 原始的sql
     * @param parameters 输入的参数
     * @return 截取之后的参数数组，用于jdbc中的?填写的数据
     */
    private List<Object> cutParameters(String sqlOrigin, List<Object> parameters){
        String regex = "(%s|%c|%b|%d|%x|%o|%f|%a|%e|%g|%h|%%|%n|%tx)";
        Matcher m = Pattern.compile(regex).matcher(sqlOrigin);
        int count = 0;
        while (m.find()) {
            count++;
        }
        return parameters.subList(count, parameters.size());
    }

    private String buildValues(Set<String> fieldList){
        return String.join(", ", fieldList.stream().map(f->"?").collect(Collectors.toList()));
    }

    private String buildSetValues(Set<String> fieldList){
        return " set `" + String.join(", `", fieldList.stream().map(f -> f + "`=?").collect(Collectors.toList()));
    }

    private String buildWhere(Set<String> fieldList){
        StringBuilder stringBuilder = new StringBuilder();
        if (null != fieldList && !fieldList.isEmpty()){
            stringBuilder.append(" where `");
        }
        return stringBuilder
            .append(String.join(" and `", fieldList.stream().map(f -> f + "`=?").collect(Collectors.toList())))
            .toString();
    }

    private <T> T asObject(Class<T> tClass, Object target) {
        if (String.class.isAssignableFrom(tClass)) {
            return tClass.cast((null == target ? null : String.valueOf(target)));
        } else {
            return tClass.cast(target);
        }
    }

    private String asString(Object object){
        return (null == object?null:String.valueOf(object));
    }
}
