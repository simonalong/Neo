package com.simon.neo;

import com.simon.neo.NeoColumn.Column;
import com.simon.neo.NeoMap.NamingChg;
import com.simon.neo.NeoTable.Table;
import com.simon.neo.TableIndex.Index;
import com.simon.neo.sql.SqlExplain;
import com.simon.neo.sql.SqlMonitor;
import com.simon.neo.sql.SqlStandard;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javafx.util.Pair;
import javax.sql.DataSource;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhouzhenyong
 * @since 2019/3/3 下午2:53
 */
@Slf4j
public class Neo {

    private static final String PRE_LOG = "[Neo] ";

    @Getter
    private NeoDb db;
    private ConnectPool pool;
    private static final String SELECT = "select";
    /**
     * 字段搜索的like前缀
     */
    private static final String LIKE_PRE = "like ";
    /**
     * 比较操作符的前缀
     */
    private static final List<String> THAN_PRE = Arrays.asList(">", "<", ">=", "<=");
    private SqlStandard standard = SqlStandard.getInstance();
    private SqlMonitor monitor = SqlMonitor.getInstance();
    private SqlExplain explain = SqlExplain.getInstance();
    /**
     * sql解析开关
     */
    @Setter
    private Boolean explainFlag = true;
    /**
     * 规范校验开关
     */
    @Setter
    @Getter
    private Boolean standardFlag = true;
    /**
     * sql监控开关
     */
    @Setter
    @Getter
    private Boolean monitorFlag = true;

    private Neo(){}

    public static Neo connect(String url, String username, String password, Properties properties) {
        Neo neo = new Neo();
        Properties baseProper = new Properties();
        baseProper.setProperty("jdbcUrl", url);
        baseProper.setProperty("dataSource.user", username);
        baseProper.setProperty("dataSource.password", password);

        // 针对mysql的特殊设置，下面这个用于设置获取remarks信息
        baseProper.setProperty("dataSource.remarks", "true");
        baseProper.setProperty("dataSource.useInformationSchema", "true");
        if(null != properties && !properties.isEmpty()) {
            baseProper.putAll(properties);
        }
        neo.pool = new ConnectPool(baseProper);
        neo.initDb();
        return neo;
    }

    public static Neo connect(String url, String username, String password) {
        return connect(url, username, password, null);
    }

    /**
     * 通过路径加载生成
     * @param propertiesPath 可以为绝对文件路径，也可以为classpath路径，classpath路径记得以/开头
     */
    public static Neo connect(String propertiesPath) {
        Neo neo = new Neo();
        neo.pool = new ConnectPool(propertiesPath);
        neo.initDb();
        return neo;
    }

    public static Neo connect(Properties properties) {
        Neo neo = new Neo();

        // 针对mysql的特殊设置，下面这个用于设置获取remarks信息
        properties.setProperty("dataSource.remarks", "true");
        properties.setProperty("dataSource.useInformationSchema", "true");

        neo.pool = new ConnectPool(properties);
        neo.initDb();
        return neo;
    }

    public static Neo connect(DataSource dataSource) {
        Neo neo = new Neo();
        neo.pool = new ConnectPool(dataSource);
        neo.initDb();
        return neo;
    }

    private void initDb(){
        try(Connection con = pool.getConnect()) {
            this.db = NeoDb.of(this, con.getCatalog(), con.getSchema());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 初始化所有表信息
        getAllTables().forEach(this::initTable);
    }

    /**
     * 获取table的信息
     * @param tableName 表名
     * @return 表对应的实例
     */
    public NeoTable asTable(String tableName){
        return db.getTable(tableName);
    }

    /**
     * 数据插入
     * @param tableName 表名
     * @param valueMap 待插入的数据
     * @return 插入之后的返回值
     */
    public NeoMap insert(String tableName, NeoMap valueMap) {
        Long id = execute((a,b)->{}, () -> generateInsertSqlPair(tableName, valueMap), this::executeInsert);
        String incrementKey = db.getPrimaryAndAutoIncName(tableName);
        if (null != incrementKey) {
            valueMap.put(incrementKey, id);
            return one(tableName, valueMap);
        }
        return valueMap;
    }

    @SuppressWarnings("unchecked")
    public <T> T insert(String tableName, T entity, NamingChg naming) {
        return insert(tableName, NeoMap.from(entity, naming)).as((Class<T>) entity.getClass());
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
            return execute((a,b)->{}, () -> generateDeleteSqlPair(tableName, searchMap), this::executeUpdate);
        }
        return 0;
    }

    public Integer delete(String tableName, Object entity, NamingChg naming) {
        return delete(tableName, NeoMap.from(entity, naming));
    }

    public Integer delete(String tableName, Object entity) {
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
        execute((a,b)->{}, () -> generateUpdateSqlPair(tableName, dataMap, searchMap), this::executeUpdate);
        closeStandard();
        NeoMap result = one(tableName, NeoMap.of().append(searchMap).append(dataMap));
        openStandard();
        return result;
    }

    @SuppressWarnings("unchecked")
    public <T> T update(String tableName, T setEntity, NeoMap searchMap) {
        return update(tableName, NeoMap.from(setEntity), searchMap).as((Class<T>) setEntity.getClass());
    }

    public <T> T update(String tableName, T setEntity, T searchEntity) {
        return update(tableName, setEntity, NeoMap.from(searchEntity));
    }

    public NeoMap update(String tableName, NeoMap dataMap, Columns columns) {
        return update(tableName, dataMap, dataMap.assign(columns));
    }

    /**
     * 查询一行的数据
     * @param sql 只接收select 方式
     * @param parameters 参数
     * @return 一个结果Map
     */
    public NeoMap exeOne(String sql, Object... parameters) {
        return execute((a,b)->{}, () -> generateExeSqlPair(sql, Arrays.asList(parameters), true), this::executeOne);
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
    public NeoMap one(String tableName, Columns columns, NeoMap searchMap, String tailSql) {
        return execute((a,b)->{}, () -> generateOneSqlPair(tableName, columns, searchMap, tailSql), this::executeOne);
    }

    @SuppressWarnings("unchecked")
    public <T> T one(String tableName, Columns columns, T entity, String tailSql){
        return one(tableName, columns, NeoMap.from(entity), tailSql).as((Class<T>) entity.getClass());
    }

    /**
     * 该函数查询是select * xxx，请尽量不要用，用具体的列即可
     * @param tableName 表名
     * @param searchMap 搜索的数据
     * @param tailSql sql的尾部sql填充
     */
    public NeoMap one(String tableName, NeoMap searchMap, String tailSql){
        return one(tableName, null, searchMap, tailSql);
    }

    /**
     * 该函数查询是select * xxx，请尽量不要用，用具体的列即可
     * @param tableName 表名
     * @param entity 搜索的实体类型数据
     * @param tailSql sql的尾部sql填充
     */
    public <T> T one(String tableName, T entity, String tailSql){
        return one(tableName, null, entity, tailSql);
    }

    public NeoMap one(String tableName, Columns columns, NeoMap searchMap){
        return one(tableName, columns, searchMap, null);
    }

    public <T> T one(String tableName, Columns columns, T entity){
        return one(tableName, columns, entity, null);
    }

    /**
     * 该函数查询是select * xxx，请尽量不要用，用具体的列即可
     * @param tableName 表名
     * @param searchMap 搜索的映射
     */
    public NeoMap one(String tableName, NeoMap searchMap){
        return one(tableName, null, searchMap);
    }

    /**
     * 该函数查询是select * xxx，请尽量不要用，用具体的列即可
     * @param tableName 表名
     * @param entity 查询的实体数据
     * @param <T> 实体的类型映射
     */
    public <T> T one(String tableName, T entity){
        return one(tableName, null, entity);
    }

    /**
     * 查询一行的数据
     * @param sql 只接收select 方式
     * @param parameters 参数
     * @return 一个结果Map
     */
    public List<NeoMap> exeList(String sql, Object... parameters) {
        if (startWithSelect(sql)) {
            return execute((a, b) -> {
                    if (explainFlag) {
                        explain.explain(this, a, b);
                    }
                }, () -> generateExeSqlPair(sql, Arrays.asList(parameters), false), this::executeList);
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
    public List<NeoMap> list(String tableName, Columns columns, NeoMap searchMap, String tailSql) {
        return execute((a, b) -> {
                if (explainFlag) {
                    explain.explain(this, a, b);
                }
            }, () -> generateListSqlPair(tableName, columns, searchMap, tailSql), this::executeList);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> list(String tableName, Columns columns, T entity, String tailSql){
        return NeoMap.asArray(list(tableName, columns, NeoMap.from(entity), tailSql), (Class<T>) entity.getClass());
    }

    public List<NeoMap> list(String tableName, NeoMap searchMap, String tailSql){
        return list(tableName, null, searchMap, tailSql);
    }

    public <T> List<T> list(String tableName, T entity, String tailSql){
        return list(tableName, null, entity, tailSql);
    }

    public List<NeoMap> list(String tableName, Columns columns, NeoMap searchMap){
        return list(tableName, columns, searchMap, null);
    }

    public <T> List<T> list(String tableName, Columns columns, T entity){
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
        NeoMap result = execute((a,b)->{}, () -> generateExeSqlPair(sql, Arrays.asList(parameters), true), this::executeOne);
        if (null != result) {
            Iterator<Object> it = result.values().iterator();
            return it.hasNext() ? asObject(tClass, it.next()) : null;
        }
        return null;
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
    public <T> T value(String tableName, Class<T> tClass, String field, NeoMap searchMap, String tailSql) {
        if (null != tClass && !NeoMap.isEmpty(searchMap)) {
            NeoMap result = execute((a,b)->{}, () -> generateValueSqlPair(tableName, field, searchMap, tailSql), this::executeOne);
            if (null != result) {
                Iterator<Object> it = result.values().iterator();
                return it.hasNext() ? asObject(tClass, it.next()) : null;
            }
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
        List<NeoMap> resultList = execute((a,b)->{
            if (explainFlag) {
                explain.explain(this, a, b);
            }
        }, () -> generateExeSqlPair(sql, Arrays.asList(parameters), false), this::executeList);
        if (null != resultList && !resultList.isEmpty()) {
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
        List<NeoMap> resultList = execute((a,b)->{}, () -> generateValuesSqlPair(tableName, field, searchMap, tailSql), this::executeList);

        if(null != resultList && !resultList.isEmpty()){
            return resultList.stream()
                .map(r -> asObject(tClass, r.get(field)))
                .filter(Objects::nonNull).distinct()
                .collect(Collectors.toList());
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
    public List<NeoMap> page(String tableName, Columns columns, NeoMap searchMap, String tailSql, Integer startIndex,
        Integer pageSize) {
        return execute((a, b) -> {
                if (explainFlag) {
                    explain.explain(this, a, b);
                }
            }, () -> generatePageSqlPair(tableName, columns, searchMap, tailSql, startIndex, pageSize), this::executeList);
    }

    public List<NeoMap> page(String tableName, NeoMap searchMap, String tailSql, Integer startIndex, Integer pageSize){
        return page(tableName, null, searchMap, tailSql, startIndex, pageSize);
    }

    public List<NeoMap> page(String tableName, Columns columns, NeoMap searchMap, Integer startIndex, Integer pageSize){
        return page(tableName, columns, searchMap, null, startIndex, pageSize);
    }

    public List<NeoMap> page(String tableName, NeoMap searchMap, Integer startIndex, Integer pageSize){
        return page(tableName, null, searchMap, startIndex, pageSize);
    }

    public List<NeoMap> page(String tableName, Columns columns, NeoMap searchMap, String tailSql, NeoPage page){
        return page(tableName, columns, searchMap, tailSql, page.startIndex(), page.pageSize());
    }

    public List<NeoMap> page(String tableName, NeoMap searchMap, String tailSql, NeoPage page){
        return page(tableName, null, searchMap, tailSql, page.startIndex(), page.pageSize());
    }

    public List<NeoMap> page(String tableName, Columns columns, NeoMap searchMap, NeoPage page){
        return page(tableName, columns, searchMap, null, page.startIndex(), page.pageSize());
    }

    public List<NeoMap> page(String tableName, NeoMap searchMap, NeoPage page){
        return page(tableName, null, searchMap, page.startIndex(), page.pageSize());
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> page(String tableName, Columns columns, T entity, String tailSql, Integer startIndex,
        Integer pageSize) {
        return NeoMap.asArray(page(tableName, columns, NeoMap.from(entity), tailSql, startIndex, pageSize),
            (Class<T>) entity.getClass());
    }

    /**
     * 查询页面对应的数据，请尽量不要使用该函数，select * from xxxx，请使用指明具体的列名的函数
     * @param tableName 表名
     * @param entity 搜索的实体数据
     * @param startIndex 分页的起始位置
     * @param pageSize 分页的大小
     */
    public <T> List<T> page(String tableName, T entity, String tailSql, Integer startIndex, Integer pageSize){
        return page(tableName, null, entity, tailSql, startIndex, pageSize);
    }

    public <T> List<T> page(String tableName, Columns columns, T entity, Integer startIndex, Integer pageSize){
        return page(tableName, columns, entity, null, startIndex, pageSize);
    }

    /**
     * 查询页面对应的数据，请尽量不要使用该函数，select * from xxxx
     * @param tableName 表名
     * @param entity 搜索的实体数据
     * @param startIndex 分页的起始位置
     * @param pageSize 分页的大小
     */
    public <T> List<T> page(String tableName, T entity, Integer startIndex, Integer pageSize){
        return page(tableName, null, entity, startIndex, pageSize);
    }

    public <T> List<T> page(String tableName, Columns columns, T entity, String tailSql, NeoPage page){
        return page(tableName, columns, entity, tailSql, page.startIndex(), page.pageSize());
    }

    /**
     * 查询页面对应的数据，请尽量不要使用该函数，select * from xxxx，请使用指明具体的列名的函数
     * @param tableName 表名
     * @param entity 搜索的实体数据
     * @param tailSql sql的尾部sql
     * @param page 分页的实体
     */
    public <T> List<T> page(String tableName, T entity, String tailSql, NeoPage page){
        return page(tableName, null, entity, tailSql, page.startIndex(), page.pageSize());
    }

    public <T> List<T> page(String tableName, Columns columns, T entity, NeoPage page){
        return page(tableName, columns, entity, null, page.startIndex(), page.pageSize());
    }

    /**
     * 查询页面对应的数据，请尽量不要使用该函数，select * from xxxx，请使用指明具体的列名的函数
     * @param tableName 表名
     * @param entity 搜索的实体数据
     * @param page 分页的实体
     */
    public <T> List<T> page(String tableName, T entity, NeoPage page){
        return page(tableName, null, entity, page.startIndex(), page.pageSize());
    }

    /**
     * 执行个数数据的查询
     * @param sql 只接收select 方式
     * @param parameters 参数
     * @return 一个结果Map
     */
    public Integer exeCount(String sql, Object... parameters) {
        NeoMap result = execute((a,b)->{}, () -> generateExeSqlPair(sql, Arrays.asList(parameters), true), this::executeOne);
        if (null != result){
            Iterator<Object> it = result.values().iterator();
            return it.hasNext() ? Integer.valueOf(asString(it.next())) : null;
        }
        return null;
    }

    public Integer count(String tableName, NeoMap searchMap) {
        NeoMap result = execute((a,b)->{}, () -> generateCountSqlPair(tableName, searchMap), this::executeOne);
        if(null != result) {
            Iterator<Object> it = result.values().iterator();
            return it.hasNext() ? Integer.valueOf(asString(it.next())) : null;
        }
        return null;
    }

    public Integer count(String tableName, Object entity) {
        return count(tableName, NeoMap.from(entity));
    }

    public Integer count(String tableName) {
        return count(tableName, NeoMap.of());
    }

    /**
     * 该函数用于执行sql，该函数支持多结果集
     * @param sql 待执行的sql
     * @param parameters 占位符和转换符的数据
     * @return 外层是多结果集，内层是对应的单结果集中的数据，为list形式的数据封装
     */
    public List<List<NeoMap>> execute(String sql, Object... parameters) {
        return execute((a,b)->{}, () -> generateExeSqlPair(sql, Arrays.asList(parameters), false), this::execute);
    }

    public List<String> getColumnNameList(String tableName){
        return getColumnList(tableName).stream().map(NeoColumn::getColumnName).collect(Collectors.toList());
    }

    public List<NeoColumn> getColumnList(String tableName){
        return db.getColumnList(tableName);
    }

    public List<Index> getIndexList(String tableName){
        return db.getIndexList(tableName);
    }

    public List<String> getIndexNameList(String tableName){
        return db.getIndexNameList(tableName);
    }

    public List<String> getAllTableNameList(String schema){
        return db.getTableNameList(schema);
    }

    public List<String> getAllTableNameList(){
        return db.getTableNameList();
    }

    public NeoTable getTable(String schema, String tableName){
        return db.getTable(schema, tableName);
    }

    public NeoTable getTable(String tableName){
        return db.getTable(tableName);
    }

    /**
     * 批量插入NeoMap列表数据
     * @param tableName 表名
     * @param dataMapList 设置数据和对应的搜索map的映射集合
     * @return 插入的数据个数：0或者all
     */
    public Integer batchInsert(String tableName, List<NeoMap> dataMapList) {
        if (null == dataMapList || dataMapList.isEmpty()) {
            return 0;
        }
        return executeBatch(generateBatchInsertPair(tableName, dataMapList));
    }

    /**
     * 批量插入实体列表
     * @param tableName 表名
     * @param dataList 数据列表
     * @param namingChg 命名转换
     * @return 插入的数据个数：0或者all
     */
    public <T> Integer batchInsertEntity(String tableName, List<T> dataList, NamingChg namingChg){
        return batchInsert(tableName, NeoMap.fromArray(dataList, namingChg));
    }

    /**
     * 批量插入实体列表
     * @param tableName 表名
     * @param dataList 数据列表
     * @return 插入的数据个数：0或者all
     */
    public <T> Integer batchInsertEntity(String tableName, List<T> dataList){
        return batchInsert(tableName, NeoMap.fromArray(dataList));
    }

    /**
     * 批量更新
     * @param tableName 表名
     * @param pairList 设置待更新的数据和对应的搜索数据的映射集合
     */
    private Integer innerBatchUpdate(String tableName, List<Pair<NeoMap, NeoMap>> pairList) {
        if (null == pairList || pairList.isEmpty()) {
            return 0;
        }
        return executeBatch(generateBatchUpdatePair(tableName, pairList));
    }

    /**
     * 批量更新，默认根据主键进行更新
     * @param tableName 表名
     * @param dataList 待更新的数据
     * @return 批量更新的个数：0或者all
     */
    public Integer batchUpdate(String tableName, List<NeoMap> dataList){
        Columns columns = Columns.of(NeoMap.dbToJavaStr(db.getPrimaryAndAutoIncName(tableName)));
        return innerBatchUpdate(tableName, buildBatchValueAndWhereList(dataList, columns));
    }

    /**
     * 批量更新，指定搜索的哪些列
     * @param tableName 表名
     * @param dataList 待更新的数据
     * @param columns where搜索条件用到的前面待更新的数据的列
     * @return 批量更新的个数：0或者all
     */
    public Integer batchUpdate(String tableName, List<NeoMap> dataList, Columns columns){
        return innerBatchUpdate(tableName, buildBatchValueAndWhereList(dataList, columns));
    }

    /**
     * 批量更新，默认根据主键进行更新
     * @param tableName 表名
     * @param dataList 待更新的数据
     * @return 批量更新的个数：0或者all
     */
    public <T> Integer batchUpdateEntity(String tableName, List<T> dataList){
        Columns columns = Columns.of(db.getPrimaryAndAutoIncName(tableName));
        return innerBatchUpdate(tableName, buildBatchValueAndWhereListFromEntity(dataList, columns, null));
    }

    /**
     * 批量执行更新，指定搜索的哪些列和命名转换方式
     * @param tableName 表名
     * @param dataList 数据列表
     * @param columns 这里的列为对象的属性名字，记得这里不是对象转换到NeoMap之后的列
     * @param namingChg 对象的命名转换，如果为null，则执行全局的，默认的全局为不转换
     * @return 批量更新的个数：0或者all
     */
    public <T> Integer batchUpdateEntity(String tableName, List<T> dataList, Columns columns, NamingChg namingChg){
        return innerBatchUpdate(tableName, buildBatchValueAndWhereListFromEntity(dataList, columns, namingChg));
    }

    /**
     * 批量执行更新，指定搜索的哪些列
     * @param tableName 表名
     * @param dataList 数据列表
     * @param columns 注意：这里的列为对象的属性名字，这里不是对象转换到NeoMap之后的列
     * @return 批量更新的个数：0或者all
     */
    public <T> Integer batchUpdateEntity(String tableName, List<T> dataList, Columns columns){
        return batchUpdateEntity(tableName, dataList, columns, null);
    }

    /**
     * 获取创建sql的语句
     * create table xxx{
     *     id xxxx;
     * } comment ='xxxx';
     */
    public String getTableCreate(String tableName){
        return (String) (execute("show create table `" + tableName + "`").get(0).get(0).get("Create Table"));
    }

    /**
     * 关闭sql规范
     */
    public void closeStandard(){
        standardFlag = false;
    }

    /**
     * 开启sql规范
     */
    public void openStandard(){
        standardFlag = true;
    }

    /**
     * 获取table的信息
     */
    private Set<String> getAllTables() {
        Set<String> tableSet = new HashSet<>();
        try (Connection con = pool.getConnect()) {
            DatabaseMetaData dbMeta = con.getMetaData();
            ResultSet rs = dbMeta.getTables(con.getCatalog(), null, null, new String[]{"TABLE"});
            while (rs.next()) {
                Table table = Table.parse(rs);
                tableSet.add(table.getTableName());
                db.addTable(this, table);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tableSet;
    }

    private void initTable(String tableName){
        // 初始化表中的列信息
        initColumnMeta(tableName);
        // 初始化表的主键、外键和索引
        initPrimary(tableName);
        // 初始化索引
        initIndex(tableName);
    }

    /**
     * 主要是初始化表的一些信息：主键，外键，索引：这里先添加主键，其他的后面再说
     */
    private void initPrimary(String tableName) {
        try (Connection con = pool.getConnect()) {
            DatabaseMetaData dbMeta = con.getMetaData();
            ResultSet rs = dbMeta.getPrimaryKeys(con.getCatalog(), con.getSchema(), tableName);
            if (rs.next()) {
                db.setPrimaryKey(con.getSchema(), tableName, rs.getString("COLUMN_NAME"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 主要是初始化表的索引信息：索引
     */
    private void initIndex(String tableName){
        try(Connection con = pool.getConnect()){
            DatabaseMetaData dbMeta = con.getMetaData();
            // 最后一个参数表示是否要求结果的准确性，倒数第二个表示是否唯一索引
            ResultSet rs = dbMeta.getIndexInfo(con.getCatalog(), con.getSchema(), tableName, false, true);
            while (rs.next()) {
                NeoTable table = db.getTable(tableName);
                table.initIndex(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化表中的列信息
     * @param tableName 表名
     */
    @SuppressWarnings("all")
    private void initColumnMeta(String tableName){
        try (Connection con = pool.getConnect()) {
            Map<String, Column> columnMap = generateColumnMetaMap(con, tableName);
            String sql = "select * from " + tableName + " limit 1";

            try (PreparedStatement statement = con.prepareStatement(sql)) {
                ResultSet rs = statement.executeQuery();
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                Set<NeoColumn> columnList = new LinkedHashSet<>();
                for (int i = 1; i <= columnCount; i++) {
                    NeoColumn column = NeoColumn.parse(metaData, i);
                    column.setColumnMeta(columnMap.get(column.getColumnName()));
                    columnList.add(column);
                }
                db.addColumn(tableName, columnList);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成列的元数据map
     *
     * @return key为列名，value为Column
     */
    private Map<String, Column> generateColumnMetaMap(Connection conn, String tableName){
        Map<String, Column> columnMap = new HashMap<>(16);
        try {
            // 最后一个参数表示是否要求结果的准确性，倒数第二个表示是否唯一索引
            ResultSet rs = conn.getMetaData().getColumns(conn.getCatalog(), null, tableName, null);
            while (rs.next()) {
                Column column = Column.parse(rs);
                columnMap.put(column.getColumnName(), column);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return columnMap;
    }

    /**
     * sql执行器
     * @param before sql执行之前进行explain检查
     * @param sqlSupplier sql和对应的参数的拼接生成器
     * @param stateFun sql Statement执行回调函数
     * @param <T> 返回值的类型
     * @return 返回对应的要求的返回值
     */
    // todo before 这个可以进行优化，放到外面，不需要作为回调，因为这个在最外面
    private <T> T execute(BiConsumer<String, List<Object>> before, Supplier<Pair<String, List<Object>>> sqlSupplier, Function<PreparedStatement, T> stateFun) {
        Pair<String, List<Object>> sqlPair = sqlSupplier.get();
        String sql = sqlPair.getKey();
        List<Object> parameters = sqlPair.getValue();

        before.accept(sql, parameters);
        try(Connection con = pool.getConnect()){
            try (PreparedStatement state = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                if (standardFlag) {
                    // sql规范化校验
                    standard.valid(sql);
                }
                if (monitorFlag) {
                    // 添加对sql的监控
                    monitor.start(this, sql, parameters);
                }

                int i, dataSize = parameters.size();
                for (i = 0; i < dataSize; i++) {
                    state.setObject(i + 1, parameters.get(i));
                }

                T result = stateFun.apply(state);

                if (monitorFlag) {
                    // 统计sql信息
                    monitor.calculate();
                }
                return result;
            } catch (SQLException e) {
                e.printStackTrace();
                log.error(PRE_LOG + "sql=> " + sql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(PRE_LOG + "sql=> " + sql);
        } finally {
            monitor.close();
        }
        return null;
    }

    private Integer executeBatch(Pair<String, List<List<Object>>> sqlPair){
        String sql = sqlPair.getKey();
        List<List<Object>> parameterList = sqlPair.getValue();

        try (Connection con = pool.getConnect()) {
            try (PreparedStatement state = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                con.setAutoCommit(false);
                // 插入批次数据
                int i, j, batchCount = parameterList.size(), fieldCount;
                for (i = 0; i < batchCount; i++) {
                    List<Object> mList = parameterList.get(i);
                    fieldCount = parameterList.get(i).size();
                    for (j = 0; j < fieldCount; j++) {
                        state.setObject(j + 1, mList.get(j));
                    }
                    state.addBatch();
                }

                if (standardFlag) {
                    // sql规范化校验
                    standard.valid(sql);
                }
                if (monitorFlag) {
                    // 添加对sql的监控
                    monitor.start(this, sql, Collections.singletonList(parameterList));
                }
                state.executeBatch();
                con.commit();
                con.setAutoCommit(true);

                if (monitorFlag) {
                    // 统计sql信息
                    monitor.calculate();
                }
                return batchCount;
            } catch (SQLException e) {
                log.error(PRE_LOG + "[执行异常] [sql=> " + sql + " ]");
                e.printStackTrace();
                try {
                    // 出现异常，进行回滚
                    if(!con.isClosed()){
                        con.rollback();
                        con.setAutoCommit(true);
                    }
                } catch (SQLException e1) {
                    log.error(PRE_LOG + "[回滚异常]");
                    e1.printStackTrace();
                }
            }
        } catch (SQLException e) {
            log.error(PRE_LOG + "[执行异常] [sql=> " + sql + " ]");
            e.printStackTrace();
        } finally {
            monitor.close();
        }
        return 0;
    }


    private String generateInsertSql(String tableName, NeoMap neoMap){
        return "insert into "
            + tableName
            + "(`"
            + String.join("`, `", neoMap.keySet())
            + "`) values ("
            + buildValues(neoMap.keySet())
            + ")";
    }

    /**
     * sql 是select 语句
     */
    private Boolean startWithSelect(String sql){
        return null != sql && sql.startsWith(SELECT);
    }

    private String generateDeleteSql(String tableName, NeoMap neoMap){
        return "delete from " + tableName + buildWhere(neoMap);
    }

    private String generateUpdateSql(String tableName, NeoMap dataMap, NeoMap searchMap){
        return "update " + tableName + buildSetValues(dataMap.keySet()) + buildWhere(searchMap);
    }

    /**
     * 生成插入的sql和参数
     * key: insert xxx
     * value: 对应的参数
     */
    private Pair<String, List<Object>> generateInsertSqlPair(String tableName, NeoMap valueMap){
        return new Pair<>(generateInsertSql(tableName, valueMap), new ArrayList<>(valueMap.values()));
    }

    /**
     * 生成删除的sql和参数
     * key: delete xxx
     * value: 对应的参数
     */
    private Pair<String, List<Object>> generateDeleteSqlPair(String tableName, NeoMap searchMap){
        return new Pair<>(generateDeleteSql(tableName, searchMap), new ArrayList<>(searchMap.values()));
    }

    /**
     * 生成插入的sql和参数
     * key: update xxx
     * value: 对应的参数
     */
    private Pair<String, List<Object>> generateUpdateSqlPair(String tableName, NeoMap dataMap, NeoMap searchMap){
        return new Pair<>(generateUpdateSql(tableName, dataMap, searchMap), NeoMap.values(dataMap, searchMap));
    }

    /**
     * 生成查询一条数据的sql和参数
     * key: select xxx
     * value: 对应的参数
     */
    private Pair<String, List<Object>> generateOneSqlPair(String tableName, Columns columns, NeoMap searchMap, String tailSql){
        return new Pair<>(generateOneSql(tableName, columns, searchMap, tailSql), generateValueList(searchMap));
    }

    /**
     * 生成查询列表的sql和参数
     * key: select xxx
     * value: 对应的参数
     */
    private Pair<String, List<Object>> generateListSqlPair(String tableName, Columns columns, NeoMap searchMap, String tailSql){
        return new Pair<>(generateListSql(tableName, columns, searchMap, tailSql), generateValueList(searchMap));
    }

    /**
     * 生成查询分页数据的sql和参数
     * key: select xxx
     * value: 对应的参数
     */
    private Pair<String, List<Object>> generatePageSqlPair(String tableName, Columns columns, NeoMap searchMap,
        String tailSql, Integer pageIndex, Integer pageSize) {
        return new Pair<>(generatePageSql(tableName, columns, searchMap, tailSql, pageIndex, pageSize), generateValueList(searchMap));
    }

    /**
     * 生成查询总数的sql和参数
     * key: select xxx
     * value: 对应的参数
     */
    private Pair<String, List<Object>> generateCountSqlPair(String tableName, NeoMap searchMap){
        return new Pair<>(generateCountSql(tableName, searchMap), generateValueList(searchMap));
    }

    /**
     * 生成查询总数的sql和参数
     * key: select xxx
     * value: 对应的参数
     */
    private Pair<String, List<Object>> generateValueSqlPair(String tableName, String field, NeoMap searchMap, String tailSql){
        return new Pair<>(generateValueSql(tableName, field, searchMap, tailSql), generateValueList(searchMap));
    }

    /**
     * 生成查询值列表的sql和参数
     * key: select xxx
     * value: 对应的参数
     */
    private Pair<String, List<Object>> generateValuesSqlPair(String tableName, String field, NeoMap searchMap, String tailSql){
        return new Pair<>(generateValuesSql(tableName, field, searchMap, tailSql), generateValueList(searchMap));
    }

    /**
     * 通过表名和查询参数生成查询一行数据的sql
     */
    private Pair<String, List<Object>> generateExeSqlPair(String sqlOrigin, List<Object> parameters, Boolean single){
        Pair<List<Object>, List<Object>> pair = replaceHolderParameters(sqlOrigin, parameters);
        String sql = String.format(sqlOrigin, pair.getKey().toArray());
        if (null != single && single) {
            sql += limitOne();
        }
        return new Pair<>(sql, pair.getValue());
    }

    /**
     * 通过表名和查询参数生成查询一行数据的sql
     */
    private Pair<String, List<List<Object>>> generateBatchInsertPair(String tableName, List<NeoMap> parameters){
        String sql = generateInsertSql(tableName, parameters.get(0));
        return new Pair<>(sql, parameters.stream().map(this::generateValueList).collect(Collectors.toList()));
    }

    /**
     * 通过表名和查询参数生成查询一行数据的sql
     */
    private Pair<String, List<List<Object>>> generateBatchUpdatePair(String tableName, List<Pair<NeoMap, NeoMap>> pairList){
        String sql = generateUpdateSql(tableName, pairList.get(0).getKey(), pairList.get(0).getValue());
        return new Pair<>(sql, pairList.stream().map(p-> NeoMap.values(p.getKey(), p.getValue())).collect(Collectors.toList()));
    }

    /**
     * 构建批次的value和where语句对的list
     * @param dataList 全部的数据列表
     * @param columns 指定哪些列的值作为查询条件，该为NeoMap中的key
     * @return 其中每个数据的key都是update中的set中用的值，value都是where中的查询条件
     */
    private List<Pair<NeoMap, NeoMap>> buildBatchValueAndWhereList(List<NeoMap> dataList, Columns columns){
        if(null == dataList || dataList.isEmpty()){
            return new ArrayList<>();
        }
        return dataList.stream().map(m-> new Pair<>(m, m.assign(columns))).collect(Collectors.toList());
    }

    /**
     * 构建批次的value和where语句对的list
     * @param dataList 全部的数据列表
     * @param columns 指定哪些列的值作为查询条件，该为NeoMap中的key
     * @return 其中每个数据的key都是update中的set中用的值，value都是where中的查询条件
     */
    private <T> List<Pair<NeoMap, NeoMap>> buildBatchValueAndWhereListFromEntity(List<T> dataList, Columns columns,
        NamingChg namingChg) {
        if (null == dataList || dataList.isEmpty()) {
            return new ArrayList<>();
        }
        return dataList.stream()
            .map(m -> new Pair<>(NeoMap.from(m, namingChg), NeoMap.from(m, columns, namingChg)))
            .collect(Collectors.toList());
    }

    /**
     * 返回拼接的sql
     * @param tableName 表名
     * @param searchMap 查询参数
     * @return 返回sql，比如：select * from xxx where a=? and b=? order by `xxx` desc limit 1
     */
    private String generateOneSql(String tableName, Columns columns, NeoMap searchMap, String tailSql) {
        StringBuilder sqlAppender = new StringBuilder("select ");
        if (!Columns.isEmpty(columns)) {
            sqlAppender.append(columns.buildFields());
        } else {
            sqlAppender.append("*");
        }
        sqlAppender.append(" from ").append(tableName).append(buildWhere(searchMap)).append(" ");
        if (null != tailSql) {
            sqlAppender.append(" ").append(tailSql);
        }
        sqlAppender.append(limitOne());
        return sqlAppender.toString();
    }

    /**
     * 返回拼接的sql
     * @param tableName 表名
     * @param searchMap 查询参数
     * @return 返回sql，比如：select * from xxx where a=? and b=? order by `xxx` desc
     */
    private String generateListSql(String tableName, Columns columns, NeoMap searchMap, String tailSql){
        StringBuilder sqlAppender = new StringBuilder("select ");
        if (!Columns.isEmpty(columns)){
            sqlAppender.append(columns.buildFields());
        }else{
            sqlAppender.append("*");
        }
        sqlAppender.append(" from ").append(tableName).append(buildWhere(searchMap)).append(" ");
        if(null != tailSql){
            sqlAppender.append(" ").append(tailSql);
        }
        return sqlAppender.toString();
    }

    /**
     * 返回拼接的sql
     * @param tableName 表名
     * @param searchMap 查询参数
     * @return 返回sql，比如：select * from xxx where a=? and b=? order by `xxx` desc
     */
    private String generatePageSql(String tableName, Columns columns, NeoMap searchMap, String tailSql, Integer pageIndex, Integer pageSize){
        StringBuilder sqlAppender = new StringBuilder("select ");
        if (!Columns.isEmpty(columns)){
            sqlAppender.append(columns.buildFields());
        }else{
            sqlAppender.append("*");
        }
        sqlAppender.append(" from ").append(tableName).append(buildWhere(searchMap)).append(" ");
        if(null != tailSql){
            sqlAppender.append(" ").append(tailSql);
        }
        sqlAppender.append(" limit ").append(pageIndex).append(", ").append(pageSize);
        return sqlAppender.toString();
    }

    /**
     * 返回拼接的sql
     * @param tableName 表名
     * @param searchMap 查询参数
     * @return select `xxx` from yyy where a=? and b=? limit 1
     */
    private String generateCountSql(String tableName, NeoMap searchMap){
        return "select count(1) from " + tableName + buildWhere(searchMap) + limitOne();
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
            .append(buildWhere(searchMap));
        if(null != tailSql){
            sqlAppender.append(" ").append(tailSql);
        }
        sqlAppender.append(limitOne());
        return sqlAppender.toString();
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
            .append(buildWhere(searchMap));
        if(null != tailSql){
            sqlAppender.append(" ").append(tailSql);
        }
        return sqlAppender.toString();
    }

    private String limitOne(){
        return " limit 1";
    }

    private List<Object> generateValueList(NeoMap searchMap){
        if (NeoMap.isEmpty(searchMap)) {
            return Collections.emptyList();
        }
        return new ArrayList<>(searchMap.values());
    }

    /**
     * 将转换符和占位符拆分开
     * @param sqlOrigin 原始的sql
     * @param parameters 输入的参数
     * @return 将转换符和占位符拆分开后的数组对
     */
    private Pair<List<Object>, List<Object>> replaceHolderParameters(String sqlOrigin, List<Object> parameters) {
        // 转换符和占位符
        String regex = "(%s|%c|%b|%d|%x|%o|%f|%a|%e|%g|%h|%%|%n|%tx|\\?)";
        Matcher m = Pattern.compile(regex).matcher(sqlOrigin);
        int count = 0;
        List<Object> replaceOperatorList = new ArrayList<>();
        List<Object> placeHolderList = new ArrayList<>();
        while (m.find()) {
            if ("?".equals(m.group())) {
                placeHolderList.add(parameters.get(count));
            }else{
                replaceOperatorList.add(parameters.get(count));
            }
            count++;
        }
        return new Pair<>(replaceOperatorList, placeHolderList);
    }

    private String buildValues(Set<String> fieldList){
        return String.join(", ", fieldList.stream().map(f->"?").collect(Collectors.toList()));
    }

    private String buildSetValues(Set<String> fieldList){
        return " set `" + String.join(", `", fieldList.stream().map(f -> f + "`=?").collect(Collectors.toList()));
    }

    private String buildWhere(NeoMap searchMap) {
        StringBuilder stringBuilder = new StringBuilder();
        if (!NeoMap.isEmpty(searchMap)) {
            stringBuilder.append(" where `");
            return stringBuilder
                .append(String.join(" and `", buildCondition(searchMap)))
                .toString();
        }
        return stringBuilder.toString();
    }

    private List<String> buildCondition(NeoMap searchMap) {
        return searchMap.entrySet().stream().map(e->valueFix(searchMap, e)).collect(Collectors.toList());
    }

    private String valueFix(NeoMap searchMap, Entry<String, Object> entry){
        Object value = entry.getValue();
        if (value instanceof String) {
            String valueStr = String.class.cast(value);
            // 设置模糊搜索
            if (valueStr.startsWith(LIKE_PRE)) {
                searchMap.put(entry.getKey(), getLikeValue(valueStr));
                return entry.getKey() + "` like ?";
            }

            // 大小比较设置，针对 ">", "<", ">=", "<=" 这么几个进行比较
            if (haveThanPre(valueStr)) {
                Pair<String, String> symbolAndValue = getSymbolAndValue(valueStr);
                searchMap.put(entry.getKey(), symbolAndValue.getValue());
                return entry.getKey() + "` " + symbolAndValue.getKey() + " ?";
            }
        }
        return entry.getKey() + "` = ?";
    }

    /**
     * 将传入的包含有like前缀的字符串，提取出value，然后拼接，比如：like xxx -> 'xxx%'
     */
    private String getLikeValue(String likeValue) {
        return likeValue.substring(likeValue.indexOf(LIKE_PRE) + LIKE_PRE.length()) + "%";
    }

    @SuppressWarnings("all")
    private Pair<String, String> getSymbolAndValue(String valueStr) {
        valueStr = valueStr.trim();
        if (valueStr.startsWith(">")) {
            if (valueStr.startsWith(">=")) {
                return new Pair<>(">=", valueStr.substring(valueStr.indexOf(">=") + ">=".length()));
            } else {
                return new Pair<>(">", valueStr.substring(valueStr.indexOf(">") + ">".length()));
            }
        } else if (valueStr.startsWith("<")) {
            if (valueStr.startsWith("<=")) {
                return new Pair<>("<=", valueStr.substring(valueStr.indexOf("<=") + "<=".length()));
            } else {
                return new Pair<>("<", valueStr.substring(valueStr.indexOf("<") + "<".length()));
            }
        }
        return new Pair<>("=", valueStr);
    }

    /**
     * 搜索的数据是否有比较类型的前缀
     */
    private boolean haveThanPre(String value){
        if (null == value || "".equals(value)) {
            return false;
        }

        for(String pre : THAN_PRE){
            if(value.startsWith(pre)){
                return true;
            }
        }
        return false;
    }

    private Long executeInsert(PreparedStatement statement){
        try {
            statement.executeUpdate();
            // 返回主键
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Integer executeUpdate(PreparedStatement statement){
        try {
            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private List<List<NeoMap>> execute(PreparedStatement statement){
        List<List<NeoMap>> resultList = new ArrayList<>();
        try {
            statement.execute();
            do {
                try (ResultSet rs = statement.getResultSet()) {
                    if (rs == null) {
                        continue;
                    }
                    ResultSetMetaData meta = rs.getMetaData();
                    List<NeoMap> dataList = new ArrayList<>();
                    while (rs.next()) {
                        NeoMap data = NeoMap.of();
                        for (int i = 1; i <= meta.getColumnCount(); i++) {
                            data.put(meta.getColumnName(i), rs.getObject(i));
                        }
                        dataList.add(data);
                    }
                    resultList.add(dataList);
                }
            } while (statement.getMoreResults());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    private NeoMap executeOne(PreparedStatement statement){
        NeoMap result = NeoMap.of();
        try {
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
        return result;
    }

    private List<NeoMap> executeList(PreparedStatement statement){
        List<NeoMap> result = new ArrayList<>();
        try {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
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
