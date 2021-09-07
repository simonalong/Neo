package com.simonalong.neo;

import com.alibaba.fastjson.JSON;
import com.simonalong.neo.NeoMap.NamingChg;
import com.simonalong.neo.core.AbstractExecutorDb;
import com.simonalong.neo.db.*;
import com.simonalong.neo.db.TableIndex.Index;
import com.simonalong.neo.exception.NeoException;
import com.simonalong.neo.exception.NeoTxException;
import com.simonalong.neo.exception.TableNotFindException;
import com.simonalong.neo.express.SearchQuery;
import com.simonalong.neo.sql.*;
import com.simonalong.neo.sql.SqlStandard.LogType;
import com.simonalong.neo.sql.builder.*;
import com.simonalong.neo.tenant.TenantHandler;
import com.simonalong.neo.util.ExceptionUtil;
import com.simonalong.neo.util.ObjectUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.simonalong.neo.NeoConstant.*;

/**
 * @author zhouzhenyong
 * @since 2019/3/3 下午2:53
 */
@Slf4j
@SuppressWarnings("unused")
@EqualsAndHashCode(of = {"name"}, callSuper = false)
public class Neo extends AbstractExecutorDb {

    @Getter
    private NeoDb db;
    @Getter
    private String name;
    @Getter
    private DbType dbType = DbType.MYSQL;
    @Getter
    private ConnectFactory connectFactory;
    private final SqlStandard standard = SqlStandard.getInstance();
    private final SqlMonitor monitor = SqlMonitor.getInstance();
    private final SqlExplain explain = SqlExplain.getInstance();
    /**
     * sql解析开关
     */
    @Setter
    @Getter
    private Boolean explainFlag = true;
    /**
     * sql监控开关
     */
    @Setter
    @Getter
    private Boolean monitorFlag = true;
    /**
     * 日志打印开关
     */
    @Setter
    @Getter
    private Boolean logPrint = false;
    /**
     * 规范校验开关
     */
    @Setter
    @Getter
    private Boolean standardFlag = true;
    /**
     * 租户管理器
     */
    @Getter
    private TenantHandler tenantHandler = null;
    /**
     * 事务开启开启个数
     */
    private final ThreadLocal<AtomicInteger> txNum = ThreadLocal.withInitial(AtomicInteger::new);
    /**
     * XA分布式事务开启关闭状态
     */
    private Boolean xaStatus = false;

    public Neo() {
    }

    public static void clone(Neo source, Neo target) {
        target.setExplainFlag(source.getExplainFlag());
        target.setStandardFlag(source.getStandardFlag());
        target.setMonitorFlag(source.getMonitorFlag());
        target.init(source.getConnectFactory().getDataSource());
    }

    /**
     * 针对有些数据库不需要用户，比如SQLite，这里给这种数据库提供
     *
     * @param url url
     * @return neo 实体
     */
    public static Neo connect(String url) {
        return connect(url, null, null);
    }

    public static Neo connect(String url, String username, String password) {
        Neo neo = new Neo();
        neo.init(url, username, password);
        return neo;
    }

    public static Neo connect(String url, String username, String password, String driverClassName) {
        Neo neo = new Neo();
        neo.init(url, username, password, driverClassName);
        return neo;
    }

    public static Neo connectFromHikariCP(Properties properties) {
        Neo neo = new Neo();
        neo.initFromHikariCP(properties);
        return neo;
    }

    public static Neo connectFromDruid(Properties properties) {
        Neo neo = new Neo();
        neo.initFromDruid(properties);
        return neo;
    }

    public static Neo connect(DataSource dataSource) {
        Neo neo = new Neo();
        neo.init(dataSource);
        return neo;
    }

    @Override
    public void finalize() throws Throwable {
        super.finalize();
        txNum.get().set(0);
    }

    /**
     * 默认采用Hikaricp 作为连接池
     *
     * @param url      数据库连接url
     * @param username 用户名
     * @param password 密码
     */
    public void init(String url, String username, String password) {
        init(url, username, password, null);
    }

    public void init(String url, String username, String password, String driverClassName) {
        Properties baseProper = new Properties();
        if (null != url) {
            baseProper.setProperty("jdbcUrl", url);
        }

        if (null != driverClassName) {
            baseProper.setProperty("driverClassName", driverClassName);
        }

        if (null != username) {
            baseProper.setProperty("dataSource.user", username);
        }

        if (null != password) {
            baseProper.setProperty("dataSource.password", password);
        }

        // 默认采用hikaricp
        initFromHikariCP(baseProper);
    }

    public void init(DataSource dataSource) {
        this.connectFactory = new ConnectFactory(this, dataSource);
        Connection connection;
        try {
            connection = dataSource.getConnection();
            this.name = connection.getMetaData().getURL();
            this.dbType = DbType.parse(this.name);
        } catch (SQLException e) {
            throw new NeoException(e);
        }
    }

    public void initFromDruid(Properties properties) {
        this.connectFactory = new ConnectFactory(this);
        this.connectFactory.initFromDruid(properties);
        this.name = properties.getProperty("druid.url");
        this.dbType = DbType.parse(this.name);
    }

    public void initFromHikariCP(Properties properties) {
        // 针对mysql的特殊设置，下面这个用于设置获取remarks信息
        properties.setProperty("dataSource.remarks", "true");
        properties.setProperty("dataSource.useInformationSchema", "true");

        this.connectFactory = new ConnectFactory(this);
        this.connectFactory.initFromHikariCP(properties);
        // 配置dbType
        if (properties.containsKey("jdbc-url")) {
            this.name = properties.getProperty("jdbc-url");
            this.dbType = DbType.parse(this.name);
        } else if (properties.containsKey("datasource.jdbc-url")) {
            this.name = properties.getProperty("datasource.jdbc-url");
            this.dbType = DbType.parse(this.name);
        } else if (properties.containsKey("url")) {
            this.name = properties.getProperty("url");
            this.dbType = DbType.parse(this.name);
        } else if (properties.containsKey("jdbcUrl")) {
            this.name = properties.getProperty("jdbcUrl");
            this.dbType = DbType.parse(this.name);
        } else if (properties.containsKey("datasource.jdbcUrl")) {
            this.name = properties.getProperty("datasource.jdbcUrl");
            this.dbType = DbType.parse(this.name);
        } else {
            throw new NeoException("hikaricp 配置没有找到url");
        }
    }

    public Connection getConnection() {
        try {
            return getConnectFactory().getConnect();
        } catch (SQLException e) {
            log.error(LOG_PRE_NEO + "get connect fail", e);
            return null;
        }
    }

    /**
     * 初始化表信息
     *
     * @param tablePres 表的前缀
     * @return 初始化db之后的表信息
     */
    public Neo initDb(String... tablePres) {
        try (Connection con = connectFactory.getConnect()) {
            this.db = NeoDb.of(this, con.getCatalog(), con.getSchema(), tablePres);
        } catch (SQLException e) {
            if (e instanceof SQLFeatureNotSupportedException) {
                this.db = NeoDb.of(this, tablePres);
            } else {
                throw new NeoException(e);
            }
        }
        return this;
    }

    /**
     * 添加自定义规范
     *
     * @param regex   正则表达式
     * @param desc    命中之后
     * @param logType 日志级别
     */
    public void addStandard(String regex, String desc, LogType logType) {
        standard.addStandard(regex, desc, logType);
    }

    /**
     * 获取table的信息
     *
     * @param tableName 表名
     * @return 表对应的实例
     */
    public NeoTable asTable(String tableName) {
        checkDb(tableName);
        return db.getTable(tableName);
    }

    /**
     * 测试链接
     */
    public void test() {
        execute("select 1");
    }

    public void test(String tableName) {
        execute("select 1 from " + tableName);
    }

    public void setTenantHandler(TenantHandler tenantHandler) {
        this.tenantHandler = tenantHandler;
    }

    public NeoJoiner joiner() {
        return new NeoJoiner(this);
    }

    /**
     * 数据插入
     *
     * @param tableName 表名
     * @param valueMap  待插入的数据
     * @return 插入之后返回的插入后的值
     */
    @Override
    public NeoMap insert(String tableName, NeoMap valueMap) {
        checkDb(tableName);
        Object id = execute(false, () -> generateInsertSqlPair(tableName, valueMap.clone()), this::executeInsert);
        Pair<String, ? extends Class<?>> keyAndType = db.getPrimaryKeyAutoIncNameAndType(tableName);

        String incrementKey = keyAndType.getKey();
        Class<?> incrementKeyType = keyAndType.getValue();

        NeoMap result = valueMap.clone();
        if (null != incrementKey && null != incrementKeyType) {
            result.put(incrementKey, ObjectUtil.cast(incrementKeyType, id));
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T insert(String tableName, T entity) {
        NeoMap neoMap = insert(tableName, NeoMap.from(entity, NamingChg.UNDERLINE));
        if (!NeoMap.isEmpty(neoMap)) {
            return neoMap.setNamingChg(NamingChg.UNDERLINE).as((Class<T>) entity.getClass());
        }
        return null;
    }

    /**
     * 不存在时候插入，存在则返回
     *
     * @param tableName       表名
     * @param dataMap         新增的实体
     * @param searchColumnKey 作为搜索条件的搜索的key
     * @return 插入成功则返回插入后数据，否则返回原数据
     */
    @Override
    public NeoMap insertOfUnExist(String tableName, NeoMap dataMap, String... searchColumnKey) {
        return tx(() -> {
            SearchQuery searchQuery = new SearchQuery();
            if (searchColumnKey.length != 0) {
                searchQuery.and(dataMap.assign(searchColumnKey));
            } else {
                searchQuery.and(dataMap);
            }

            searchQuery.append(" for update");
            Integer count = count(tableName, searchQuery);

            if (0 == count) {
                return insert(tableName, dataMap);
            } else {
                return dataMap;
            }
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T insertOfUnExist(String tableName, T object, String... searchColumnKey) {
        return insertOfUnExist(tableName, NeoMap.from(object), searchColumnKey).as((Class<T>) object.getClass());
    }

    /**
     * 不存在则插入，存在则更新
     *
     * @param tableName       表名
     * @param dataMap         待更新或者插入的数据
     * @param searchColumnKey 作为搜索条件的搜索的key
     * @return 更新或者插入后的数据
     */
    @Override
    public NeoMap save(String tableName, NeoMap dataMap, String... searchColumnKey) {
        if (searchColumnKey.length != 0) {
            if (!dataMap.containsKeys(searchColumnKey)) {
                throw new NeoException("不包含key: " + Arrays.asList(searchColumnKey));
            }
        }
        return tx(() -> {
            SearchQuery searchQuery = new SearchQuery();
            if (searchColumnKey.length != 0) {
                searchQuery.and(dataMap.assign(searchColumnKey));
            } else {
                searchQuery.and(dataMap);
            }

            searchQuery.append(" for update");
            Integer count = count(tableName, searchQuery);

            if (0 == count) {
                return insert(tableName, dataMap);
            } else {
                return update(tableName, dataMap, dataMap.assign(searchColumnKey));
            }
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T save(String tableName, T object, String... searchColumnKey) {
        return save(tableName, NeoMap.from(object), searchColumnKey).as((Class<T>) object.getClass());
    }

    /**
     * 数据删除
     *
     * @param tableName 表名
     * @param searchMap where 后面的条件数据
     * @return 影响的行数
     */
    @Override
    public Integer delete(String tableName, NeoMap searchMap) {
        if (!NeoMap.isEmpty(searchMap)) {
            NeoMap searchMapTem = searchMap.clone();
            return execute(false, () -> generateDeleteSqlPair(tableName, searchMapTem), this::executeUpdate);
        }
        return 0;
    }

    @Override
    public Integer delete(String tableName, SearchQuery searchQuery) {
        return execute(false, () -> generateDeleteSqlPair(tableName, searchQuery), this::executeUpdate);
    }

    @Override
    public <T> Integer delete(String tableName, T entity) {
        if (entity instanceof Number) {
            return delete(tableName, (Number) entity);
        }
        return delete(tableName, NeoMap.from(entity, NamingChg.UNDERLINE));
    }

    @Override
    public Integer delete(String tableName, Number id) {
        checkDb(tableName);
        String primaryKey = db.getPrimaryName(tableName);
        if (null != primaryKey) {
            return delete(tableName, NeoMap.of(primaryKey, id));
        }
        return 0;
    }

    /**
     * 数据更新
     *
     * @param tableName 表名
     * @param dataMap   set的更新的数据
     * @param searchMap where后面的语句条件数据
     * @return 更新之后的返回值
     */
    @Override
    public NeoMap update(String tableName, NeoMap dataMap, NeoMap searchMap) {
        if (NeoMap.isEmpty(dataMap)) {
            return null;
        }
        checkDb(tableName);
        NeoMap dataMapTem = dataMap.clone();
        NeoMap searchMapTem = searchMap.clone();
        return tx(() -> {
            execute(false, () -> generateUpdateSqlPair(tableName, dataMapTem, searchMapTem), this::executeUpdate);
            Boolean oldStandard = getStandardFlag();
            closeStandard();

            NeoMap valueSearchMap = NeoMap.of();
            if (searchMap.containsKey(db.getPrimaryName(tableName))) {
                valueSearchMap = searchMap.assign(db.getPrimaryName(tableName));
            } else {
                valueSearchMap.append(searchMapTem).append(dataMapTem);
            }
            NeoMap result = one(tableName, valueSearchMap);
            setStandardFlag(oldStandard);
            return result;
        });
    }

    /**
     * 添加排他锁执行查询
     *
     * @param tableName 表名
     * @param params    参数
     * @return 返回值
     */
    private NeoMap oneWithXMode(String tableName, NeoMap params) {
        String sql = SelectSqlBuilder.buildOne(this, tableName, null, params) + " for update";
        List<Object> parameters = new ArrayList<>(params.values());
        return execute(false, () -> generateExeSqlPair(sql, parameters), this::executeOne).getNeoMap(tableName);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T update(String tableName, T setEntity, NeoMap searchMap) {
        if (setEntity.getClass().isPrimitive()) {
            log.error(LOG_PRE_NEO + "数据{}是基本类型", setEntity);
            return setEntity;
        }
        NeoMap neoMap = update(tableName, NeoMap.from(setEntity, NamingChg.UNDERLINE), searchMap);
        if (!NeoMap.isEmpty(neoMap)) {
            return neoMap.as((Class<T>) setEntity.getClass());
        }
        return null;
    }

    @Override
    public NeoMap update(String tableName, NeoMap dataMap, SearchQuery searchQuery) {
        NeoMap dataMapTem = dataMap.clone();
        return tx(() -> {
            execute(false, () -> generateUpdateSqlPair(tableName, dataMapTem, searchQuery), this::executeUpdate);
            Boolean oldStandard = getStandardFlag();
            closeStandard();
            NeoMap result = oneWithXMode(tableName, dataMapTem);
            setStandardFlag(oldStandard);
            return result;
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T update(String tableName, T setEntity, SearchQuery searchQuery) {
        if (setEntity.getClass().isPrimitive()) {
            log.error(LOG_PRE_NEO + "数据{}是基本类型", setEntity);
            return setEntity;
        }
        NeoMap neoMap = update(tableName, NeoMap.from(setEntity, NamingChg.UNDERLINE), searchQuery);
        if (!NeoMap.isEmpty(neoMap)) {
            return neoMap.as((Class<T>) setEntity.getClass());
        }
        return null;
    }

    @Override
    public <T> T update(String tableName, T setEntity, T searchEntity) {
        checkDb(tableName);
        if (searchEntity instanceof Number) {
            String primaryKey = db.getPrimaryName(tableName);
            if (null != primaryKey) {
                return update(tableName, setEntity, NeoMap.of(primaryKey, searchEntity));
            }
        }
        return update(tableName, setEntity, NeoMap.from(searchEntity, NamingChg.UNDERLINE));
    }

    @Override
    public <T> NeoMap update(String tableName, NeoMap setMap, T searchEntity) {
        return update(tableName, setMap, NeoMap.from(searchEntity, NamingChg.UNDERLINE));
    }

    /**
     * 更新
     *
     * @param tableName 表名
     * @param dataMap   待更新的数据
     * @param columns   搜索条件，其中该列为 dataMap 中对应的key的名字
     * @return map对象
     */
    @Override
    public NeoMap update(String tableName, NeoMap dataMap, Columns columns) {
        return update(tableName, dataMap, dataMap.assign(columns));
    }

    @Override
    public <T> T update(String tableName, T entity, Columns columns) {
        return update(tableName, entity, NeoMap.from(entity, columns, NamingChg.UNDERLINE));
    }

    @Override
    public <T> T update(String tableName, T setEntity, Number id) {
        return update(tableName, setEntity, NeoMap.of(db.getPrimaryName(tableName), id));
    }

    @Override
    public NeoMap update(String tableName, NeoMap setMap, Number id) {
        return update(tableName, setMap, NeoMap.of(db.getPrimaryName(tableName), id));
    }

    /**
     * 直接实体对应数据传入更新，则需要包含主键对应的key才行
     *
     * @param tableName 表明
     * @param dataMap   待更新的实体数据对应的map
     * @return 更新之后的实体数据对应的map
     */
    @Override
    public NeoMap update(String tableName, NeoMap dataMap) {
        checkDb(tableName);
        Columns columns = Columns.of(db.getPrimaryName(tableName));
        NeoMap searchMap = dataMap.assign(columns);
        // 若没有指定主键，则不进行DB更新
        if (dataMap.equals(searchMap)) {
            return dataMap;
        }
        return update(tableName, dataMap, dataMap.assign(columns));
    }

    /**
     * 直接实体传入更新，则需要包含主键对应的key才行
     *
     * @param tableName 表明
     * @param entity    待更新的实体数据对应的map
     * @return 更新之后的实体数据对应的map
     */
    @Override
    public <T> T update(String tableName, T entity) {
        checkDb(tableName);
        if (entity.getClass().isPrimitive()) {
            log.error(LOG_PRE_NEO + "参数{}是基本类型", entity);
            return entity;
        }
        String keyStr = NeoMap.dbToJavaStr(db.getPrimaryName(tableName));
        NeoMap searchMap = NeoMap.fromInclude(entity, NamingChg.UNDERLINE, keyStr);
        // 若没有指定主键，则不进行DB更新
        if (NeoMap.from(entity, NamingChg.UNDERLINE).equals(searchMap)) {
            return entity;
        }
        return update(tableName, entity, searchMap);
    }

    /**
     * 查询一行的数据
     *
     * @param sql        只接收select 方式
     * @param parameters 参数
     * @return 一个结果Map
     */
    @Override
    public TableMap exeOne(String sql, Object... parameters) {
        return execute(false, () -> generateExeSqlPair(sql, Arrays.asList(parameters)), this::executeOne);
    }

    @Override
    public <T> T exeOne(Class<T> tClass, String sql, Object... parameters) {
        return exeOne(sql, parameters).as(tClass);
    }

    /**
     * 查询一行实体数据
     *
     * @param tableName 表名
     * @param columns   列名
     * @param searchMap 搜索条件
     * @return 返回一个实体的Map影射
     */
    @Override
    public NeoMap one(String tableName, Columns columns, NeoMap searchMap) {
        checkDb(tableName);
        NeoMap searchMapTem = searchMap.clone();
        TableMap result = execute(false, () -> generateOneSqlPair(tableName, columns, searchMapTem), this::executeOne);
        return result.getNeoMap(tableName);
    }

    @Override
    public NeoMap one(String tableName, Columns columns, SearchQuery searchQuery) {
        TableMap result = execute(false, () -> generateOneSqlPair(tableName, columns, searchQuery), this::executeOne);
        return result.getNeoMap(tableName);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T one(String tableName, Columns columns, T entity) {
        if (entity instanceof Number) {
            return one(tableName, columns, (Number) entity).setNamingChg(NamingChg.UNDERLINE).as((Class<T>) entity.getClass());
        }
        NeoMap neoMap = one(tableName, columns, NeoMap.from(entity));
        if (!NeoMap.isEmpty(neoMap)) {
            return neoMap.setNamingChg(NamingChg.UNDERLINE).as((Class<T>) entity.getClass());
        }
        return null;
    }

    @Override
    public NeoMap one(String tableName, Columns columns, Number key) {
        checkDb(tableName);
        String primaryKey = db.getPrimaryName(tableName);
        NeoMap neoMap = NeoMap.of();
        if (null != primaryKey) {
            return one(tableName, columns, NeoMap.of(primaryKey, key));
        }
        return neoMap;
    }

    /**
     * 该函数查询是select * xxx，请尽量不要用，用具体的列即可
     *
     * @param tableName 表名
     * @param searchMap 搜索的数据
     * @return NeoMap对象
     */
    @Override
    public NeoMap one(String tableName, NeoMap searchMap) {
        return one(tableName, Columns.of().setNeo(this).table(tableName), searchMap);
    }

    /**
     * 该函数查询是select * xxx，请尽量不要用，用具体的列即可
     *
     * @param tableName 表名
     * @param entity    搜索的实体类型数据
     * @param <T>       插入的对象类型
     * @return 插入的对象类型
     */
    @Override
    public <T> T one(String tableName, T entity) {
        return one(tableName, Columns.of().setNeo(this).table(tableName), entity);
    }

    /**
     * 通过id获取数据，则默认会将该数据认为是主键
     *
     * @param tableName 表名
     * @param id        主键id数据
     * @return 查询到的数据
     */
    @Override
    public NeoMap one(String tableName, Number id) {
        checkDb(tableName);
        String primaryKey = db.getPrimaryName(tableName);
        NeoMap neoMap = NeoMap.of();
        if (null != primaryKey) {
            return one(tableName, NeoMap.of(primaryKey, id));
        }
        return neoMap;
    }

    /**
     * 查询一行（一个实体）
     *
     * @param tableName     表名
     * @param searchQuery 复杂结构表达式
     * @return 一个实体对应的类型
     */
    @Override
    public NeoMap one(String tableName, SearchQuery searchQuery) {
        checkDb(tableName);
        return execute(false, () -> generateOneSqlPair(tableName, searchQuery), this::executeOne).getNeoMap(tableName);
    }

    /**
     * 查询一行的数据
     *
     * @param sql        只接收select 方式
     * @param parameters 参数
     * @return 一个结果Map列表
     */
    @Override
    public List<TableMap> exeList(String sql, Object... parameters) {
        if (startWithSelect(sql)) {
            return execute(true, () -> generateExeSqlPair(sql, Arrays.asList(parameters)), this::executeList);
        }
        return new ArrayList<>();
    }

    @Override
    public <T> List<T> exeList(Class<T> tClass, String sql, Object... parameters) {
        return exeList(sql, parameters).stream().map(table -> table.as(tClass)).collect(Collectors.toList());
    }

    /**
     * 查询具体的数据列表
     *
     * @param tableName 表名
     * @param columns   列数据
     * @param searchMap 搜索条件
     * @return 返回一列数据
     */
    @Override
    public List<NeoMap> list(String tableName, Columns columns, NeoMap searchMap) {
        NeoMap searchMapTem = searchMap.clone();
        return execute(true, () -> generateListSqlPair(tableName, columns, searchMapTem), this::executeList).stream()
            .map(table -> table.getNeoMap(tableName))
            .collect(Collectors.toList());
    }

    @Override
    public List<NeoMap> list(String tableName, Columns columns, SearchQuery searchQuery) {
        return execute(true, () -> generateListSqlPair(tableName, columns, searchQuery), this::executeList).stream()
            .map(table -> table.getNeoMap(tableName))
            .collect(Collectors.toList());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> list(String tableName, Columns columns, T entity) {
        if (null != entity) {
            if (entity.getClass().isPrimitive()) {
                log.error(LOG_PRE_NEO + "参数{}是基本类型", entity);
                return Collections.emptyList();
            }
            return NeoMap.asArray(list(tableName, columns, NeoMap.from(entity, NamingChg.UNDERLINE)), NamingChg.UNDERLINE, (Class<T>) entity.getClass());
        }
        log.warn(LOG_PRE_NEO + "entity is null");
        return Collections.emptyList();
    }

    @Override
    public List<NeoMap> list(String tableName, NeoMap searchMap) {
        return list(tableName, null, searchMap);
    }

    @Override
    public <T> List<T> list(String tableName, T entity) {
        return list(tableName, null, entity);
    }

    @Override
    public List<NeoMap> list(String tableName, Columns columns) {
        return list(tableName, columns, NeoMap.of());
    }

    @Override
    public List<NeoMap> list(String tableName, SearchQuery searchQuery) {
        checkDb(tableName);
        return execute(false, () -> generateListSqlPair(tableName, searchQuery), this::executeList).stream()
            .map(table -> table.getNeoMap(tableName))
            .collect(Collectors.toList());
    }

    /**
     * 查询返回单个值
     *
     * @param tClass     目标类的class
     * @param sql        只接收select 方式
     * @param parameters 参数
     * @param <T>        返回的目标类型
     * @return 目标类的对象
     */
    @Override
    public <T> T exeValue(Class<T> tClass, String sql, Object... parameters) {
        TableMap result = execute(false, () -> generateExeSqlPair(sql, Arrays.asList(parameters)), this::executeOne);
        if (null != result) {
            return result.getFirst().getFirst(tClass);
        }
        return null;
    }

    @Override
    public String exeValue(String sql, Object... parameters) {
        return exeValue(String.class, sql, parameters);
    }

    /**
     * 查询某行某列的值
     *
     * @param tableName 表名
     * @param tClass    返回值的类型
     * @param field     某个属性的名字
     * @param searchMap 搜索条件
     * @param <T>       目标类型
     * @return 指定的数据值
     */
    @Override
    @Deprecated
    public <T> T value(String tableName, Class<T> tClass, String field, NeoMap searchMap) {
        if (NeoMap.isEmpty(searchMap)) {
//            log.warn("搜索条件为空，[{}.java:{}]", Thread.currentThread().getStackTrace()[3].getClassName(), Thread.currentThread().getStackTrace()[3].getLineNumber());
        }
        if (null != tClass) {
            NeoMap searchMapTem = searchMap.clone();
            TableMap result = execute(false, () -> generateValueSqlPair(tableName, field, searchMapTem), this::executeOne);
            if (null != result) {
                return result.get(tClass, tableName, field);
            }
        }
        return null;
    }

    @Override
    @Deprecated
    public <T> T value(String tableName, Class<T> tClass, String field, Object entity) {
        checkDb(tableName);
        // 若entity为数字类型，则认为是主键
        if (entity instanceof Number) {
            String primaryKey = db.getPrimaryName(tableName);
            if (null != primaryKey && !"".equals(primaryKey)) {
                return value(tableName, tClass, field, NeoMap.of(primaryKey, entity));
            }
        }
        return value(tableName, tClass, field, NeoMap.from(entity));
    }

    /**
     * 查询某行某列的值
     *
     * @param tableName 表名
     * @param tClass    返回值的类型
     * @param field     某个属性的名字
     * @param searchMap 搜索条件
     * @param <T>       目标类型
     * @return 指定的数据值
     */
    @Override
    public <T> T value(Class<T> tClass, String tableName, String field, NeoMap searchMap) {
        if (NeoMap.isEmpty(searchMap)) {
//            log.warn("搜索条件为空，[{}.java:{}]", Thread.currentThread().getStackTrace()[3].getClassName(), Thread.currentThread().getStackTrace()[3].getLineNumber());
        }
        if (null != tClass) {
            NeoMap searchMapTem = searchMap.clone();
            TableMap result = execute(false, () -> generateValueSqlPair(tableName, field, searchMapTem), this::executeOne);
            if (null != result) {
                return result.get(tClass, tableName, field);
            }
        }
        return null;
    }

    @Override
    public <T> T value(Class<T> tClass, String tableName, String field, SearchQuery searchQuery) {
        if (null != tClass) {
            TableMap result = execute(false, () -> generateValueSqlPair(tableName, field, searchQuery), this::executeOne);
            if (null != result) {
                return result.get(tClass, tableName, field);
            }
        }
        return null;
    }

    @Override
    public <T> T value(Class<T> tClass, String tableName, String field, Object entity) {
        checkDb(tableName);
        // 若entity为数字类型，则认为是主键
        if (entity instanceof Number) {
            String primaryKey = db.getPrimaryName(tableName);
            if (null != primaryKey && !"".equals(primaryKey)) {
                return value(tClass, tableName, field, NeoMap.of(primaryKey, entity));
            }
        }
        return value(tClass, tableName, field, NeoMap.from(entity));
    }

    @Override
    public String value(String tableName, String field, NeoMap searchMap) {
        return value(String.class, tableName, field, searchMap);
    }

    @Override
    public String value(String tableName, String field, SearchQuery searchQuery) {
        TableMap result = execute(false, () -> generateValueSqlPair(tableName, field, searchQuery), this::executeOne);
        if (null != result) {
            return result.get(String.class, tableName, field);
        }
        return null;
    }

    /**
     * 根据实体查询属性的值，若entity为数字类型，则认为是主键
     *
     * @param tableName 表名
     * @param field     属性
     * @param entity    实体
     * @return 表某个属性的值
     */
    @Override
    public String value(String tableName, String field, Object entity) {
        checkDb(tableName);
        // 若entity为数字类型，则认为是主键
        if (entity instanceof Number) {
            String primaryKey = db.getPrimaryName(tableName);
            if (null != primaryKey && !"".equals(primaryKey)) {
                return value(String.class, tableName, field, NeoMap.of(primaryKey, entity));
            }
        }
        return value(String.class, tableName, field, NeoMap.from(entity));
    }

    @Override
    public String value(String tableName, String field, Number id) {
        checkDb(tableName);
        String primaryKey = db.getPrimaryName(tableName);
        if (null != primaryKey && !"".equals(primaryKey)) {
            return value(String.class, tableName, field, NeoMap.of(primaryKey, id));
        }
        log.warn(LOG_PRE_NEO + "db {}'s primary key is null, please set", tableName);
        return null;
    }

    /**
     * 查询一行的数据
     *
     * @param tClass     数据实体的类
     * @param sql        查询一行的sql
     * @param parameters 查询的搜索参数
     * @param <T>        数据实体的类型
     * @return 查询到的数据实体，如果没有找到则返回null
     */
    @Override
    public <T> List<T> exeValues(Class<T> tClass, String sql, Object... parameters) {
        List<TableMap> resultList = execute(true, () -> generateExeSqlPair(sql, Arrays.asList(parameters)), this::executeList);

        if (null != resultList && !resultList.isEmpty()) {
            return resultList.stream().map(r -> {
                Iterator<Object> it = r.values().iterator();
                return it.hasNext() ? ObjectUtil.cast(tClass, ((NeoMap) it.next()).getFirst()) : null;
            }).filter(Objects::nonNull).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public List<String> exeValues(String sql, Object... parameters) {
        return exeValues(String.class, sql, parameters);
    }

    /**
     * 查询一列的值
     *
     * @param tableName 表名
     * @param tClass    实体类的类
     * @param field     列名
     * @param searchMap 搜索条件
     * @param <T>       目标类型
     * @return 一列值
     */
    @Override
    @Deprecated
    public <T> List<T> values(String tableName, Class<T> tClass, String field, NeoMap searchMap) {
        NeoMap searchMapTem = searchMap.clone();
        List<NeoMap> resultList = execute(false, () -> generateValuesSqlPair(tableName, false, field, searchMapTem), this::executeList).stream().map(table -> {
            if (table.haveTable(DEFAULT_TABLE)) {
                return table.getNeoMap(DEFAULT_TABLE);
            } else {
                return table.getNeoMap(tableName);
            }
        }).collect(Collectors.toList());

        if (!NeoMap.isEmpty(resultList)) {
            return resultList.stream().map(r -> r.get(tClass, field)).filter(Objects::nonNull).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    @Deprecated
    public <T> List<T> values(String tableName, Class<T> tClass, String field, Object entity) {
        checkDb(tableName);
        // 若entity为数字类型，则认为是主键
        if (entity instanceof Number) {
            String primaryKey = db.getPrimaryName(tableName);
            if (null != primaryKey && !"".equals(primaryKey)) {
                return values(tClass, tableName, field, NeoMap.of(primaryKey, entity));
            }
        }
        return values(tClass, tableName, field, NeoMap.from(entity));
    }

    /**
     * 查询一列的值
     *
     * @param tableName 表名
     * @param tClass    实体类的类
     * @param field     列名
     * @param searchMap 搜索条件
     * @param <T>       目标类型
     * @return 一列值
     */
    @Override
    public <T> List<T> values(Class<T> tClass, String tableName, String field, NeoMap searchMap) {
        if (NeoMap.isEmpty(searchMap)) {
//            log.warn("搜索条件为空，[{}.java:{}]", Thread.currentThread().getStackTrace()[3].getClassName(), Thread.currentThread().getStackTrace()[3].getLineNumber());
        }
        if (null != tClass) {
            NeoMap searchMapTem = searchMap.clone();
            List<NeoMap> resultList = execute(false, () -> generateValuesSqlPair(tableName, false, field, searchMapTem), this::executeList).stream().map(table -> {
                if (table.haveTable(DEFAULT_TABLE)) {
                    return table.getNeoMap(DEFAULT_TABLE);
                } else {
                    return table.getNeoMap(tableName);
                }
            }).collect(Collectors.toList());

            if (!NeoMap.isEmpty(resultList)) {
                return resultList.stream().map(r -> r.get(tClass, field)).filter(Objects::nonNull).collect(Collectors.toList());
            }
        }
        return new ArrayList<>();
    }

    @Override
    public <T> List<T> values(Class<T> tClass, String tableName, String field, SearchQuery searchQuery) {
        List<NeoMap> resultList = execute(false, () -> generateValuesSqlPair(tableName, false, field, searchQuery), this::executeList).stream().map(table -> {
            if (table.haveTable(DEFAULT_TABLE)) {
                return table.getNeoMap(DEFAULT_TABLE);
            } else {
                return table.getNeoMap(tableName);
            }
        }).collect(Collectors.toList());

        if (!NeoMap.isEmpty(resultList)) {
            return resultList.stream().map(r -> r.get(tClass, field)).filter(Objects::nonNull).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public <T> List<T> values(Class<T> tClass, String tableName, String field, Object entity) {
        checkDb(tableName);
        // 若entity为数字类型，则认为是主键
        if (entity instanceof Number) {
            String primaryKey = db.getPrimaryName(tableName);
            if (null != primaryKey && !"".equals(primaryKey)) {
                return values(tClass, tableName, field, NeoMap.of(primaryKey, entity));
            }
        }
        return values(tClass, tableName, field, NeoMap.from(entity));
    }

    @Override
    public List<String> values(String tableName, String field, NeoMap searchMap) {
        return values(String.class, tableName, field, searchMap);
    }

    @Override
    public List<String> values(String tableName, String field, SearchQuery searchQuery) {
        return values(String.class, tableName, field, searchQuery);
    }

    /**
     * 通过实体查询一列的列表
     *
     * @param tableName 表名
     * @param field     列名
     * @param entity    实体数据
     * @return 列对应的列表
     */
    @Override
    public List<String> values(String tableName, String field, Object entity) {
        checkDb(tableName);
        // 若entity为数字类型，则认为是主键
        if (entity instanceof Number) {
            String primaryKey = db.getPrimaryName(tableName);
            if (null != primaryKey && !"".equals(primaryKey)) {
                return values(String.class, tableName, field, NeoMap.of(primaryKey, entity));
            }
        }
        return values(String.class, tableName, field, NeoMap.from(entity));
    }

    @Override
    public List<String> values(String tableName, String field) {
        return values(String.class, tableName, field, NeoMap.of());
    }

    @Override
    public <T> List<T> valuesOfDistinct(Class<T> tClass, String tableName, String field, NeoMap searchMap) {
        if (NeoMap.isEmpty(searchMap)) {
//            log.warn("搜索条件为空，[{}.java:{}]", Thread.currentThread().getStackTrace()[3].getClassName(), Thread.currentThread().getStackTrace()[3].getLineNumber());
        }
        if (null != tClass) {
            NeoMap searchMapTem = searchMap.clone();
            List<NeoMap> resultList = execute(false, () -> generateValuesSqlPair(tableName, true, field, searchMapTem), this::executeList).stream().map(table -> {
                if (table.haveTable(DEFAULT_TABLE)) {
                    return table.getNeoMap(DEFAULT_TABLE);
                } else {
                    return table.getNeoMap(tableName);
                }
            }).collect(Collectors.toList());

            if (!NeoMap.isEmpty(resultList)) {
                return resultList.stream().map(r -> r.get(tClass, field)).filter(Objects::nonNull).collect(Collectors.toList());
            }
        }
        return new ArrayList<>();
    }

    @Override
    public <T> List<T> valuesOfDistinct(Class<T> tClass, String tableName, String field, SearchQuery searchQuery) {
        List<NeoMap> resultList = execute(false, () -> generateValuesSqlPair(tableName, true, field, searchQuery), this::executeList).stream().map(table -> {
            if (table.haveTable(DEFAULT_TABLE)) {
                return table.getNeoMap(DEFAULT_TABLE);
            } else {
                return table.getNeoMap(tableName);
            }
        }).collect(Collectors.toList());

        if (!NeoMap.isEmpty(resultList)) {
            return resultList.stream().map(r -> r.get(tClass, field)).filter(Objects::nonNull).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public <T> List<T> valuesOfDistinct(Class<T> tClass, String tableName, String field, Object entity) {
        checkDb(tableName);
        // 若entity为数字类型，则认为是主键
        if (entity instanceof Number) {
            String primaryKey = db.getPrimaryName(tableName);
            if (null != primaryKey && !"".equals(primaryKey)) {
                return valuesOfDistinct(tClass, tableName, field, NeoMap.of(primaryKey, entity));
            }
        }
        return valuesOfDistinct(tClass, tableName, field, NeoMap.from(entity));
    }

    @Override
    public List<String> valuesOfDistinct(String tableName, String field, NeoMap searchMap) {
        return valuesOfDistinct(String.class, tableName, field, searchMap);
    }

    @Override
    public List<String> valuesOfDistinct(String tableName, String field, SearchQuery searchQuery) {
        return valuesOfDistinct(String.class, tableName, field, searchQuery);
    }

    @Override
    public List<String> valuesOfDistinct(String tableName, String field, Object entity) {
        checkDb(tableName);
        // 若entity为数字类型，则认为是主键
        if (entity instanceof Number) {
            String primaryKey = db.getPrimaryName(tableName);
            if (null != primaryKey && !"".equals(primaryKey)) {
                return valuesOfDistinct(String.class, tableName, field, NeoMap.of(primaryKey, entity));
            }
        }
        return valuesOfDistinct(String.class, tableName, field, NeoMap.from(entity));
    }

    @Override
    public List<String> valuesOfDistinct(String tableName, String field) {
        return valuesOfDistinct(String.class, tableName, field, NeoMap.of());
    }


    /**
     * 执行分页数据查询
     *
     * @param sql        对应的sql，里面可以包含limit也可以不包含，都兼容，如果不包含，则会追加，如果包含，则会根据是否需要填充，进行填充参数或者直接执行
     * @param startIndex 分页起始
     * @param pageSize   分页大小
     * @param parameters 参数
     * @return 分页对应的数据
     */
    @Override
    public List<TableMap> exePage(String sql, Integer startIndex, Integer pageSize, Object... parameters) {
        if (startWithSelect(sql)) {
            return execute(true, () -> generateExePageSqlPair(sql, Arrays.asList(parameters), startIndex, pageSize), this::executeList);
        }
        return new ArrayList<>();
    }

    @Override
    public <T> List<T> exePage(Class<T> tClass, String sql, Integer startIndex, Integer pageSize, Object... parameters) {
        return exePage(sql, startIndex, pageSize, parameters).stream().map(table -> table.as(tClass)).collect(Collectors.toList());
    }

    @Override
    @Deprecated
    public List<TableMap> exePage(String sql, NeoPage neoPage, Object... parameters) {
        return exePage(sql, neoPage.getStartIndex(), neoPage.getSize(), parameters);
    }

    @Override
    @Deprecated
    public <T> List<T> exePage(Class<T> tClass, String sql, NeoPage neoPage, Object... parameters) {
        return exePage(sql, neoPage, parameters).stream().map(table -> table.as(tClass)).collect(Collectors.toList());
    }

    @Override
    public List<TableMap> exePage(String sql, PageReq<?> pageReq, Object... parameters) {
        return exePage(sql, pageReq.getStartIndex(), pageReq.getSize(), parameters);
    }

    @Override
    public <T> List<T> exePage(Class<T> tClass, String sql, PageReq<?> pageReq, Object... parameters) {
        return exePage(sql, pageReq, parameters).stream().map(table -> table.as(tClass)).collect(Collectors.toList());
    }


    /**
     * 分组数据
     *
     * @param tableName 表名
     * @param columns   列的属性
     * @param searchMap 搜索条件
     * @param page      分页
     * @return 分页对应的数据
     */
    @Override
    @Deprecated
    public List<NeoMap> page(String tableName, Columns columns, NeoMap searchMap, NeoPage page) {
        checkDb(tableName);
        NeoMap searchMapTem = searchMap.clone();
        List<TableMap> result = execute(true, () -> generatePageSqlPair(tableName, columns, searchMapTem, page.getStartIndex(), page.getSize()), this::executeList);

        return result.stream().map(table -> table.getNeoMap(tableName)).collect(Collectors.toList());
    }

    @Override
    @Deprecated
    public List<NeoMap> page(String tableName, Columns columns, SearchQuery searchQuery, NeoPage page) {
        checkDb(tableName);
        List<TableMap> result = execute(true, () -> generatePageSqlPair(tableName, columns, searchQuery, page.getStartIndex(), page.getSize()), this::executeList);

        return result.stream().map(table -> table.getNeoMap(tableName)).collect(Collectors.toList());
    }

    @Override
    @Deprecated
    @SuppressWarnings("unchecked")
    public <T> List<T> page(String tableName, Columns columns, T entity, NeoPage page) {
        if (entity.getClass().isPrimitive()) {
            log.error(LOG_PRE_NEO + "参数{}是基本类型", entity.getClass());
            return Collections.emptyList();
        }
        return NeoMap.asArray(page(tableName, columns, NeoMap.from(entity, NamingChg.UNDERLINE), page), NamingChg.UNDERLINE, (Class<T>) entity.getClass());
    }

    @Override
    @Deprecated
    public List<NeoMap> page(String tableName, NeoMap searchMap, NeoPage page) {
        return page(tableName, Columns.of().setNeo(this).table(tableName), searchMap, page);
    }

    @Override
    @Deprecated
    public List<NeoMap> page(String tableName, SearchQuery searchQuery, NeoPage page) {
        return page(tableName, Columns.of().setNeo(this).table(tableName), searchQuery, page);
    }

    @Override
    @Deprecated
    public <T> List<T> page(String tableName, T entity, NeoPage page) {
        return page(tableName, Columns.of().setNeo(this).table(tableName), entity, page);
    }

    @Override
    @Deprecated
    public List<NeoMap> page(String tableName, Columns columns, NeoPage page) {
        return page(tableName, columns, NeoMap.of(), page);
    }

    @Override
    @Deprecated
    public List<NeoMap> page(String tableName, NeoPage page) {
        return page(tableName, Columns.of().setNeo(this).table(tableName), NeoMap.of(), page);
    }


    /**
     * 分组数据
     *
     * @param tableName 表名
     * @param columns   列的属性
     * @param searchMap 搜索条件
     * @param pageReq   分页
     * @return 分页对应的数据
     */
    @Override
    public List<NeoMap> page(String tableName, Columns columns, NeoMap searchMap, PageReq<?> pageReq) {
        checkDb(tableName);
        NeoMap searchMapTem = searchMap.clone();
        List<TableMap> result = execute(true, () -> generatePageSqlPair(tableName, columns, searchMapTem, pageReq.getStartIndex(), pageReq.getSize()), this::executeList);

        return result.stream().map(table -> table.getNeoMap(tableName)).collect(Collectors.toList());
    }

    @Override
    public List<NeoMap> page(String tableName, Columns columns, SearchQuery searchQuery, PageReq<?> pageReq) {
        checkDb(tableName);
        List<TableMap> result = execute(true, () -> generatePageSqlPair(tableName, columns, searchQuery, pageReq.getStartIndex(), pageReq.getSize()), this::executeList);

        return result.stream().map(table -> table.getNeoMap(tableName)).collect(Collectors.toList());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> page(String tableName, Columns columns, T entity, PageReq<?> pageReq) {
        if (entity.getClass().isPrimitive()) {
            log.error(LOG_PRE_NEO + "参数{}是基本类型", entity.getClass());
            return Collections.emptyList();
        }
        return NeoMap.asArray(page(tableName, columns, NeoMap.from(entity, NamingChg.UNDERLINE), pageReq), NamingChg.UNDERLINE, (Class<T>) entity.getClass());
    }

    @Override
    public List<NeoMap> page(String tableName, NeoMap searchMap, PageReq<?> pageReq) {
        return page(tableName, Columns.of().setNeo(this).table(tableName), searchMap, pageReq);
    }

    @Override
    public List<NeoMap> page(String tableName, SearchQuery searchQuery, PageReq<?> pageReq) {
        return page(tableName, Columns.of().setNeo(this).table(tableName), searchQuery, pageReq);
    }

    @Override
    public <T> List<T> page(String tableName, T entity, PageReq<?> pageReq) {
        return page(tableName, Columns.of().setNeo(this).table(tableName), entity, pageReq);
    }

    @Override
    public List<NeoMap> page(String tableName, Columns columns, PageReq<?> pageReq) {
        return page(tableName, columns, NeoMap.of(), pageReq);
    }

    @Override
    public List<NeoMap> page(String tableName, PageReq<?> pageReq) {
        return page(tableName, Columns.of().setNeo(this).table(tableName), NeoMap.of(), pageReq);
    }


    /**
     * 执行个数数据的查询
     *
     * @param sql        只接收select 方式
     * @param parameters 参数
     * @return 一个结果Map
     */
    @Override
    public Integer exeCount(String sql, Object... parameters) {
        TableMap result = execute(false, () -> generateExeSqlPair(sql, Arrays.asList(parameters)), this::executeOne);
        return doCount(result);
    }

    @Override
    public Integer count(String tableName, NeoMap searchMap) {
        NeoMap searchMapTem = searchMap.clone();
        TableMap result = execute(false, () -> generateCountSqlPair(tableName, searchMapTem), this::executeOne);
        return doCount(result);
    }

    @Override
    public Integer count(String tableName, SearchQuery searchQuery) {
        TableMap result = execute(false, () -> generateCountSqlPair(tableName, searchQuery), this::executeOne);
        return doCount(result);
    }

    private Integer doCount(TableMap result) {
        if (null != result) {
            Integer data = ObjectUtil.toInt(result.getFirst().getFirst());
            return (null == data) ? 0 : data;
        }
        return 0;
    }

    @Override
    public Integer count(String tableName, Object entity) {
        return count(tableName, NeoMap.from(entity));
    }

    @Override
    public Integer count(String tableName) {
        return count(tableName, NeoMap.of());
    }

    /**
     * 该函数用于执行sql，该函数支持多结果集
     *
     * @param sql        待执行的sql
     * @param parameters 占位符和转换符的数据
     * @return 外层是多结果集，内层是对应的单结果集中的数据，为list形式的数据封装
     */
    @Override
    public List<List<TableMap>> execute(String sql, Object... parameters) {
        return execute(false, () -> generateExeSqlPair(sql, Arrays.asList(parameters)), this::execute);
    }

    /**
     * 清理表数据
     * <p>
     * 该api会执行 truncate tableName 会将该表对应的数据清理完
     *
     * @param tableName 表名
     */
    public void truncateTable(String tableName) {
        execute("truncate " + tableName);
    }

    public Set<String> getColumnNameList(String tableName) {
        return getColumnList(tableName).stream().map(NeoColumn::getColumnName).collect(Collectors.toSet());
    }

    public List<NeoColumn> getColumnList(String tableName) {
        checkDb(tableName);
        return db.getColumnList(tableName);
    }

    public List<Index> getIndexList(String tableName) {
        checkDb(tableName);
        return db.getIndexList(tableName);
    }

    public List<String> getIndexNameList(String tableName) {
        checkDb(tableName);
        return db.getIndexNameList(tableName);
    }

    public List<String> getAllTableNameList(String schema) {
        return db.getTableNameList(schema);
    }

    public List<String> getAllTableNameList() {
        return db.getTableNameList();
    }

    public NeoTable getTable(String schema, String tableName) {
        checkDb(tableName);
        return db.getTable(schema, tableName);
    }

    public NeoTable getTable(String tableName) {
        return getTable(null, tableName);
    }

    /**
     * 批量插入NeoMap列表数据
     *
     * @param tableName   表名
     * @param dataMapList 设置数据和对应的搜索map的映射集合
     * @return 插入的数据个数：0或者all
     */
    @Override
    public Integer batchInsert(String tableName, List<NeoMap> dataMapList) {
        if (null == dataMapList || dataMapList.isEmpty()) {
            return 0;
        }
        List<NeoMap> dataMapListTem = clone(dataMapList);
        NeoMap insertColumns = NeoMap.of();
        dataMapList.parallelStream().forEach(insertColumns::putAll);
        return executeBatch(generateBatchInsertPair(tableName, insertColumns, dataMapListTem));
    }

    /**
     * 批量插入实体列表
     *
     * @param tableName 表名
     * @param dataList  数据列表
     * @param <T>       目标类型
     * @return 插入的数据个数：0或者all
     */
    @Override
    public <T> Integer batchInsertEntity(String tableName, List<T> dataList) {
        return batchInsert(tableName, NeoMap.fromArray(dataList, true));
    }

    /**
     * 批量更新，默认根据主键进行更新
     *
     * @param tableName 表名
     * @param dataList  待更新的数据
     * @return 批量更新的个数：0或者all
     */
    @Override
    public Integer batchUpdate(String tableName, List<NeoMap> dataList) {
        if (null == dataList || dataList.isEmpty()) {
            return 0;
        }
        checkDb(tableName);
        List<NeoMap> dataListTem = clone(dataList);
        Columns columns = Columns.of(db.getPrimaryName(tableName));
        checkBatchUpdateParams(dataList, columns);
        return execute(false, () -> generateBatchUpdateSqlPair(tableName, dataListTem, columns), this::executeUpdate);
    }

    /**
     * 批量更新，指定搜索的哪些列
     *
     * @param tableName 表名
     * @param dataList  待更新的数据
     * @param columns   where搜索条件用到的前面待更新的数据的列
     * @return 批量更新的个数：0或者all
     */
    @Override
    public Integer batchUpdate(String tableName, List<NeoMap> dataList, Columns columns) {
        if (null == dataList || dataList.isEmpty()) {
            return 0;
        }
        checkDb(tableName);
        List<NeoMap> dataListTem = clone(dataList);
        checkBatchUpdateParams(dataList, columns);
        return execute(false, () -> generateBatchUpdateSqlPair(tableName, dataListTem, columns), this::executeUpdate);
    }

    /**
     * 批量更新，默认根据主键进行更新
     *
     * @param tableName 表名
     * @param dataList  待更新的数据
     * @param <T>       目标类型
     * @return 批量更新的个数：0或者all
     */
    @Override
    public <T> Integer batchUpdateEntity(String tableName, List<T> dataList) {
        if (null == dataList || dataList.isEmpty()) {
            return 0;
        }

        List<NeoMap> dataMapList = NeoMap.fromArray(dataList, NamingChg.UNDERLINE, true);
        return batchUpdate(tableName, dataMapList);
    }

    /**
     * 批量执行更新，指定搜索的哪些列
     *
     * @param tableName        表名
     * @param dataList         数据列表
     * @param conditionColumns 注意：这里的列为对象的属性名字，这里是对象转换到NeoMap之后的列
     * @param <T>              目标类型
     * @return 批量更新的个数：0或者all
     */
    @Override
    public <T> Integer batchUpdateEntity(String tableName, List<T> dataList, Columns conditionColumns) {
        if (null == dataList || dataList.isEmpty()) {
            return 0;
        }
        List<NeoMap> dataMapList = NeoMap.fromArray(dataList, NamingChg.UNDERLINE, true);
        return batchUpdate(tableName, dataMapList, conditionColumns);
    }

    /**
     * 事务的执行 注意： 1.这里的事务传播机制采用，如果已经有事务在运行，则挂接在高层事务里面，这里进行最外层统一提交 2.隔离级别采用数据库默认 3.读写的事务
     *
     * @param runnable 待执行的任务
     * @throws NeoTxException 事务出现异常会抛出
     */
    public void tx(Runnable runnable) {
        tx(null, null, () -> {
            runnable.run();
            return null;
        });
    }

    /**
     * 事务的执行 注意： 1.这里的事务传播机制采用，如果已经有事务在运行，则挂接在高层事务里面，这里进行最外层统一提交 2.隔离级别采用数据库默认 3.读写的事务
     *
     * @param supplier 待执行的任务
     * @param <T>      目标类型
     * @return 事务执行完成返回的数据
     * @throws NeoTxException 事务出现异常会抛出
     */
    public <T> T tx(Supplier<T> supplier) {
        return tx(null, null, supplier);
    }

    /**
     * 事务的执行 注意：这里的事务传播机制采用，如果已经有事务在运行，则挂接在高层事务里面，这里进行最外层统一提交
     *
     * @param readOnly 事务的只读属性，默认为false
     * @param runnable 待执行的任务
     * @throws NeoTxException 事务出现异常会抛出
     */
    public void tx(Boolean readOnly, Runnable runnable) {
        tx(null, readOnly, () -> {
            runnable.run();
            return null;
        });
    }

    /**
     * 事务的执行 注意：这里的事务传播机制采用，如果已经有事务在运行，则挂接在高层事务里面，这里进行最外层统一提交
     *
     * @param readOnly 事务的只读属性，默认为false
     * @param supplier 待执行的任务
     * @param <T>      待返回值的类型
     * @return 事务执行之后的返回值
     * @throws NeoTxException 事务出现异常会抛出
     */
    public <T> T tx(Boolean readOnly, Supplier<T> supplier) {
        return tx(null, readOnly, supplier);
    }

    /**
     * 事务的执行 注意：这里的事务传播机制采用，如果已经有事务在运行，则挂接在高层事务里面，这里进行最外层统一提交
     *
     * @param isolationEnum 事务的隔离级别，如果为null，则采用数据库的默认隔离级别
     * @param runnable      待执行的任务
     * @throws NeoTxException 事务出现异常会抛出
     */
    public void tx(TxIsolationEnum isolationEnum, Runnable runnable) {
        tx(isolationEnum, false, () -> {
            runnable.run();
            return null;
        });
    }

    /**
     * 事务的执行 注意：这里的事务传播机制采用，如果已经有事务在运行，则挂接在高层事务里面，这里进行最外层统一提交
     *
     * @param isolationEnum 事务的隔离级别，如果为null，则采用数据库的默认隔离级别
     * @param supplier      待执行的任务
     * @param <T>           待返回值的类型
     * @return 事务执行之后的返回值
     * @throws NeoTxException 事务出现异常会抛出
     */
    public <T> T tx(TxIsolationEnum isolationEnum, Supplier<T> supplier) {
        return tx(isolationEnum, false, supplier);
    }

    /**
     * 事务的执行 注意：这里的事务传播机制采用，如果已经有事务在运行，则挂接在高层事务里面，这里进行最外层统一提交
     *
     * @param isolationEnum 事务的隔离级别，如果为null，则采用数据库的默认隔离级别
     * @param readOnly      事务的只读属性，默认为false
     * @param runnable      待执行的任务
     * @throws NeoTxException 事务出现异常会抛出
     */
    public void tx(TxIsolationEnum isolationEnum, Boolean readOnly, Runnable runnable) {
        tx(isolationEnum, readOnly, () -> {
            runnable.run();
            return null;
        });
    }

    /**
     * 带返回值的事务执行，可以进行嵌套事务，针对嵌套事务，这里进行最外层统一提交 注意：这里的事务传播机制采用，如果已经有事务在运行，则挂接在高层事务里面，这里进行最外层统一提交
     *
     * @param isolationEnum 事务的隔离级别，如果为null，则采用数据库的默认隔离级别
     * @param readOnly      事务的只读属性，默认为false
     * @param supplier      待执行的任务
     * @param <T>           目标类型
     * @return 事务执行完成返回的数据
     * @throws NeoTxException 事务出现异常会抛出
     */
    public <T> T tx(TxIsolationEnum isolationEnum, Boolean readOnly, Supplier<T> supplier) {
        // 由于XA事务和本机事务不兼容，如果开启XA，则优先运行XA
        if (isXaTransaction()) {
            return supplier.get();
        }
        txNumIncrement();
        try {
            connectFactory.setTxConfig(isolationEnum, readOnly);
            if (openTxMonitor()) {
                monitor.startTx();
            }
            T result = supplier.get();
            connectFactory.submit();

            if (openTxMonitor()) {
                monitor.calculate(result);
            }
            return result;
        } catch (Throwable e) {
            log.error(LOG_PRE_NEO + "[提交失败，事务回滚], {}", e.getMessage());
            try {
                connectFactory.rollback();
                throw new NeoTxException(ExceptionUtil.unwrapException(e));
            } catch (SQLException e1) {
                log.error(LOG_PRE_NEO + "[回滚失败]");
                throw new NeoTxException(ExceptionUtil.unwrapException(e));
            }
        } finally {
            txNumDecrement();
        }
    }

    private void txNumIncrement() {
        txNum.get().incrementAndGet();
    }

    private void txNumDecrement() {
        txNum.get().decrementAndGet();
    }

    public boolean txIsRoot() {
        return txNum.get().get() == 1;
    }

    /**
     * 获取创建sql的语句 {@code create db xxx{ id xxxx; } comment ='xxxx'; }
     *
     * @param tableName 表名
     * @return 表的创建语句
     */
    public String getTableCreate(String tableName) {
        List<List<TableMap>> table = execute("show create table `" + tableName + "`");
        return table.get(0).get(0).getFirst().getString("Create Table");
    }

    /**
     * 判断对应的表名是否存在
     *
     * @param tableName 表名
     * @return true：表存在，false：表不存在
     */
    public Boolean tableExist(String tableName) {
        return null != getTable(tableName);
    }

    /**
     * 关闭sql规范
     */
    public void closeStandard() {
        standardFlag = false;
    }

    /**
     * 开启sql规范
     */
    public void openStandard() {
        standardFlag = true;
    }

    public void openXA() {
        xaStatus = true;
    }

    public Boolean isTransaction() {
        return txNum.get().get() > 0;
    }

    public Boolean isXaTransaction() {
        return xaStatus;
    }

    /**
     * explain 命令解析sql
     *
     * @param multiLine        是否多行数据
     * @param sqlAndParamsList sql和对应的参数数据
     */
    private void explain(Boolean multiLine, Pair<String, List<Object>> sqlAndParamsList) {
        if (multiLine) {
            if (explainFlag) {
                explain.explain(this, sqlAndParamsList.getKey(), sqlAndParamsList.getValue());
            }
        }
    }

    /**
     * sql执行器
     *
     * @param multiLine   是否多行执行，对于多行执行，这里会进行explain 对应的sql核查
     * @param sqlSupplier sql和对应的参数的拼接生成器
     * @param stateFun    sql Statement执行回调函数
     * @param <T>         返回值的类型
     * @return 返回对应的要求的返回值
     */
    protected <T> T execute(Boolean multiLine, Supplier<Pair<String, List<Object>>> sqlSupplier, Function<PreparedStatement, T> stateFun) {
        try {
            NeoContext.load(this);
            Pair<String, List<Object>> sqlPair = sqlSupplier.get();
            String sql = sqlPair.getKey();
            List<Object> parameters = sqlPair.getValue();

            if (null == sql) {
                return null;
            }

            // sql 多行查询的explain衡量
            explain(multiLine, sqlPair);
            try (Connection con = connectFactory.getConnect()) {
                try (PreparedStatement state = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    if (standardFlag) {
                        // sql规范化校验
                        standard.valid(sql);
                    }
                    if (openMonitor() || openLogPrint()) {
                        // 添加对sql的监控
                        monitor.start(sql, parameters);
                    }

                    int i, dataSize = parameters.size();
                    for (i = 0; i < dataSize; i++) {
                        state.setObject(i + 1, parameters.get(i));
                    }

                    T result = stateFun.apply(state);
                    if (openMonitor()) {
                        // 统计sql信息
                        monitor.calculate(result);
                    }

                    if (openLogPrint()) {
                        monitor.printLog(result);
                    }
                    return result;
                } catch (Throwable e) {
                    log.error(LOG_PRE_NEO + "sql=> {}, parameters => {}", sql, parameters, e);
                    throw new NeoException(e);
                }
            } catch (SQLException e) {
                log.error(LOG_PRE_NEO + "sql=> {}, parameters => {}", sql, parameters, e);
                throw new NeoException(e);
            } finally {
                if (openMonitor()) {
                    monitor.close();
                }
            }
        } finally {
            NeoContext.load(this);
        }
    }

    protected Integer executeBatch(Pair<String, List<NeoMap>> sqlPair) {
        try {
            NeoContext.load(this);
            String sql = sqlPair.getKey();
            List<NeoMap> parameterList = sqlPair.getValue();

            return tx(() -> {
                try (Connection con = connectFactory.getConnect()) {
                    try (PreparedStatement state = con.prepareStatement(sql)) {
                        if (standardFlag) {
                            // sql规范化校验
                            standard.valid(sql);
                        }
                        if (openMonitor()) {
                            // 添加对sql的监控
                            monitor.start(sql, new ArrayList<>(parameterList));
                        }

                        int i, j, batchCount = parameterList.size(), fieldCount;
                        for (i = 0; i < batchCount; i++) {
                            NeoMap indexValueMap = parameterList.get(i);
                            fieldCount = indexValueMap.getInteger("size");
                            for (j = 0; j < fieldCount; j++) {
                                state.setObject(j + 1, indexValueMap.get(String.valueOf(j)));
                            }
                            state.addBatch();
                        }

                        int[] results = state.executeBatch();

                        if (openMonitor()) {
                            // 统计sql信息
                            monitor.calculate(results);
                        }
                        return batchCount;
                    } catch (Throwable e) {
                        log.error(LOG_PRE_NEO + "[执行异常] [sql=> {}][parameters={}]", sql, JSON.toJSONString(parameterList), e);
                    }
                } catch (SQLException e) {
                    log.error(LOG_PRE_NEO + "[执行异常] [sql=> {}][parameters={}]", sql, JSON.toJSONString(parameterList), e);
                    throw new NeoException(e);
                } finally {
                    if (openMonitor()) {
                        monitor.close();
                    }
                }
                return 0;
            });
        } finally {
            NeoContext.load(this);
        }
    }

    /**
     * 是否开启sql监控：针对一次执行的情况，只有在非事务且监控开启情况下才对单独执行监控
     */
    private Boolean openMonitor() {
        return !isTransaction() && monitorFlag;
    }

    private Boolean openLogPrint() {
        return logPrint;
    }

    /**
     * 是否开启sql监控：针对一次执行的情况，只有在非事务且监控开启情况下才对单独执行监控
     */
    private Boolean openTxMonitor() {
        return isTransaction();
    }

    /**
     * sql 是select 语句
     */
    private Boolean startWithSelect(String sql) {
        return null != sql && sql.startsWith(SELECT);
    }

    /**
     * 生成插入的sql和参数 key: insert xxx value: 对应的参数
     */
    private Pair<String, List<Object>> generateInsertSqlPair(String tableName, NeoMap valueMap) {
        valueMap = filterColumn(tableName, valueMap);
        return new Pair<>(InsertSqlBuilder.build(tenantHandler, tableName, valueMap), new ArrayList<>(valueMap.values()));
    }

    /**
     * 生成删除的sql和参数 key: delete xxx value: 对应的参数
     */
    private Pair<String, List<Object>> generateDeleteSqlPair(String tableName, NeoMap searchMap) {
        searchMap = filterColumn(tableName, searchMap);
        return new Pair<>(DeleteSqlBuilder.build(tenantHandler, tableName, searchMap), SqlBuilder.buildValueList(searchMap));
    }

    private Pair<String, List<Object>> generateDeleteSqlPair(String tableName, SearchQuery searchQuery) {
        return new Pair<>(DeleteSqlBuilder.build(tenantHandler, tableName, searchQuery), new ArrayList<>(searchQuery.toValue()));
    }

    /**
     * 生成插入的sql和参数 key: update xxx value: 对应的参数
     */
    private Pair<String, List<Object>> generateUpdateSqlPair(String tableName, NeoMap dataMap, NeoMap searchMap) {
        NeoMap searchMapTem = filterColumn(tableName, searchMap);
        NeoMap updateMap = filterColumn(tableName, dataMap);

        // 该行数据要在valueList初始化之前执行
        String sql = UpdateSqlBuilder.build(tenantHandler, tableName, updateMap, searchMapTem);
        if (null == sql) {
            log.warn(LOG_PRE_NEO + "sql为空");
            return new Pair<>(null, new ArrayList<>());
        }

        List<Object> valueList = new ArrayList<>();
        valueList.addAll(generateValueList(updateMap));
        valueList.addAll(generateValueList(searchMapTem));
        return new Pair<>(sql, valueList);
    }

    private Pair<String, List<Object>> generateUpdateSqlPair(String tableName, NeoMap dataMap, SearchQuery searchQuery) {
        NeoMap updateMap = filterColumn(tableName, dataMap);

        // 该行数据要在valueList初始化之前执行
        String sql = UpdateSqlBuilder.build(tenantHandler, tableName, updateMap, searchQuery);

        List<Object> valueList = new ArrayList<>();
        valueList.addAll(generateValueList(updateMap));
        valueList.addAll(searchQuery.toValue());
        return new Pair<>(sql, valueList);
    }

    /**
     * 生成查询一条数据的sql和参数 key: select xxx value: 对应的参数
     */
    private Pair<String, List<Object>> generateOneSqlPair(String tableName, Columns columns, NeoMap searchMap) {
        searchMap = filterColumn(tableName, searchMap);
        return new Pair<>(SelectSqlBuilder.buildOne(this, tableName, columns, searchMap), generateValueList(searchMap));
    }

    private Pair<String, List<Object>> generateOneSqlPair(String tableName, Columns columns, SearchQuery searchQuery) {
        return new Pair<>(SelectSqlBuilder.buildOne(this, tableName, columns, searchQuery), searchQuery.toValue());
    }

    /**
     * 生成查询一条数据的sql和参数 key: select xxx value: 对应的参数
     */
    private Pair<String, List<Object>> generateOneSqlPair(String tableName, SearchQuery searchQuery) {
        return new Pair<>(SelectSqlBuilder.buildOne(this, tableName, searchQuery), searchQuery.toValue());
    }

    /**
     * 生成查询列表的sql和参数 key: select xxx value: 对应的参数
     */
    private Pair<String, List<Object>> generateListSqlPair(String tableName, Columns columns, NeoMap searchMap) {
        searchMap = filterColumn(tableName, searchMap);
        return new Pair<>(SelectSqlBuilder.buildList(this, tableName, columns, searchMap), generateValueList(searchMap));
    }

    private Pair<String, List<Object>> generateListSqlPair(String tableName, Columns columns, SearchQuery searchQuery) {
        return new Pair<>(SelectSqlBuilder.buildList(this, tableName, columns, searchQuery), searchQuery.toValue());
    }

    /**
     * 生成查询列表的sql和参数 key: select xxx value: 对应的参数
     */
    private Pair<String, List<Object>> generateListSqlPair(String tableName, SearchQuery searchQuery) {
        return new Pair<>(SelectSqlBuilder.buildList(this, tableName, searchQuery), searchQuery.toValue());
    }

    /**
     * 生成查询分页数据的sql和参数 key: select xxx value: 对应的参数
     */
    private Pair<String, List<Object>> generatePageSqlPair(String tableName, Columns columns, NeoMap searchMap, Integer startIndex, Integer pageSize) {
        searchMap = filterColumn(tableName, searchMap);
        return new Pair<>(SelectSqlBuilder.buildPage(this, tableName, columns, searchMap, startIndex, pageSize), generateValueList(searchMap));
    }

    private Pair<String, List<Object>> generatePageSqlPair(String tableName, Columns columns, SearchQuery searchQuery, Integer startIndex, Integer pageSize) {
        return new Pair<>(SelectSqlBuilder.buildPage(this, tableName, columns, searchQuery, startIndex, pageSize), searchQuery.toValue());
    }

    /**
     * 生成查询总数的sql和参数 key: select xxx value: 对应的参数
     */
    private Pair<String, List<Object>> generateCountSqlPair(String tableName, NeoMap searchMap) {
        searchMap = filterColumn(tableName, searchMap);
        return new Pair<>(SelectSqlBuilder.buildCount(tenantHandler, tableName, searchMap), generateValueList(searchMap));
    }

    private Pair<String, List<Object>> generateCountSqlPair(String tableName, SearchQuery searchQuery) {
        return new Pair<>(SelectSqlBuilder.buildCount(tenantHandler, tableName, searchQuery), searchQuery.toValue());
    }

    /**
     * 生成查询总数的sql和参数 key: select xxx value: 对应的参数
     */
    private Pair<String, List<Object>> generateValueSqlPair(String tableName, String field, NeoMap searchMap) {
        searchMap = filterColumn(tableName, searchMap);
        return new Pair<>(SelectSqlBuilder.buildValue(tenantHandler, tableName, field, searchMap), generateValueList(searchMap));
    }

    /**
     * 生成查询总数的sql和参数 key: select xxx value: 对应的参数
     */
    private Pair<String, List<Object>> generateValueSqlPair(String tableName, String field, SearchQuery searchQuery) {
        return new Pair<>(SelectSqlBuilder.buildValue(tenantHandler, tableName, field, searchQuery), searchQuery.toValue());
    }

    /**
     * 生成查询值列表的sql和参数 key: select xxx value: 对应的参数
     */
    private Pair<String, List<Object>> generateValuesSqlPair(String tableName, Boolean distinct, String field, NeoMap searchMap) {
        searchMap = filterColumn(tableName, searchMap);
        return new Pair<>(SelectSqlBuilder.buildValues(tenantHandler, tableName, distinct, field, searchMap), generateValueList(searchMap));
    }

    /**
     * 生成查询值列表的sql和参数 key: select xxx value: 对应的参数
     */
    private Pair<String, List<Object>> generateValuesSqlPair(String tableName, Boolean distinct, String field, SearchQuery searchQuery) {
        return new Pair<>(SelectSqlBuilder.buildValues(tenantHandler, tableName, distinct, field, searchQuery), searchQuery.toValue());
    }


    /**
     * 通过表名和查询参数生成查询一行数据的sql
     */
    private Pair<String, List<Object>> generateExeSqlPair(String sqlOrigin, List<Object> parameters) {
        Pair<List<Object>, List<Object>> pair = replaceHolderParameters(sqlOrigin, parameters);
        List<Object> keys = pair.getKey();
        String sql = sqlOrigin;

        if (null != keys && !keys.isEmpty()) {
            for (Object key : keys) {
                int index = sql.indexOf("%s");
                if (index > 1) {
                    if (sql.charAt(index - 1) == '\'') {
                        sql = sql.substring(index + "%s".length());
                    } else {
                        sql = sql.replaceFirst("%s", key.toString());
                    }
                }
            }
        }

        return new Pair<>(sql, pair.getValue());
    }

    /**
     * 通过原始sql和分页，进行拼接执行
     */
    private Pair<String, List<Object>> generateExePageSqlPair(String sqlOrigin, List<Object> parameters, Integer startIndex, Integer pageSize) {
        if (!sqlOrigin.contains(LIMIT)) {
            sqlOrigin += " limit " + startIndex + ", " + pageSize;
        }
        return generateExeSqlPair(sqlOrigin, parameters);
    }

    /**
     * 通过表名和查询参数生成查询一行数据的sql
     */
    private Pair<String, List<NeoMap>> generateBatchInsertPair(String tableName, NeoMap insertColumns, List<NeoMap> parameters) {
        String sql = InsertSqlBuilder.build(tenantHandler, tableName, insertColumns);
        List<NeoMap> indexAndValueMap = new ArrayList<>();
        int index;
        List<String> keys = new ArrayList<>(insertColumns.keySet());

        for (NeoMap data : parameters) {
            NeoMap valueMap = NeoMap.of();
            for (index = 0; index < keys.size(); index++) {
                valueMap.put(Integer.toString(index), data.get(keys.get(index)));
            }
            valueMap.put("size", keys.size());
            indexAndValueMap.add(valueMap);
        }
        return new Pair<>(sql, indexAndValueMap);
    }

    /**
     * 生成更新批处理的sql以及对应的值
     *
     * @param tableName            待更新数据的表名
     * @param updateDataColumnList 待更新的数据
     * @param conditionColumns     条件列名
     * @return sql以及对应的占位符中的值
     */
    private Pair<String, List<Object>> generateBatchUpdateSqlPair(String tableName, List<NeoMap> updateDataColumnList, Columns conditionColumns) {
        return new Pair<>(UpdateSqlBuilder.buildBatch(tenantHandler, tableName, updateDataColumnList, conditionColumns), SqlBuilder.buildBatchValueList(updateDataColumnList));
    }

    private List<Object> generateValueList(NeoMap searchMap) {
        return SqlBuilder.buildValueList(searchMap);
    }

    /**
     * 过滤不是列名的key，并且对其中NeoMap中为Long类型的时间类型进行转换
     * <p>
     * 注意： 由于mysql中时间类型year不支持{@link Date}这个类型直接传入（其他四个时间类型支持），因此需要单独处理
     *
     * @param dataMap 待处理的数据
     * @return 处理后的数据
     */
    private NeoMap filterColumn(String tableName, NeoMap dataMap) {
        // key为列名，value为：key为列的数据库类型名字，value为列对应的java中的类型
        Map<String, Pair<String, Class<?>>> columnMap = getColumnList(tableName).stream()
            .collect(Collectors.toMap(NeoColumn::getColumnName, r -> new Pair<>(r.getColumnTypeName(), r.getJavaClass())));
        NeoMap result = NeoMap.of();
        result.setSupportValueNull(dataMap.getSupportValueNull());
        dataMap.stream().filter(e -> columnMap.containsKey(e.getKey())).forEach(r -> {
            String key = r.getKey();
            Object value = r.getValue();
            Pair<String, Class<?>> typeAndClass = columnMap.get(key);
            result.put(key, TimeDateConverter.longToDbTime(typeAndClass.getValue(), typeAndClass.getKey(), value), false);
        });
        result.setNamingChg(dataMap.getNamingChg());
        return result;
    }

    /**
     * 将转换符和占位符拆分开
     *
     * <p>
     * 这里借鉴mybatis的占位符和替换符的思想，用java的转换为作为替换符，目前替换支持%s
     *
     * @param sqlOrigin  原始的sql
     * @param parameters 输入的参数
     * @return 将转换符和占位符拆分开后的数组对：%s替换数据，?占位数据
     */
    @SuppressWarnings("rawtypes")
    private Pair<List<Object>, List<Object>> replaceHolderParameters(String sqlOrigin, List<Object> parameters) {
        // 匹配替换符：%s（不匹配'%s，后者可能是like中的数据）和占位符：?
        String regex = "((?<!')%s|\\?)";
        Matcher m = Pattern.compile(regex).matcher(sqlOrigin);
        int count = 0;
        List<Object> replaceOperatorList = new ArrayList<>();
        List<Object> placeHolderList = new ArrayList<>();
        while (m.find()) {
            if ("?".equals(m.group())) {
                // 承接 ? 的数据
                placeHolderList.add(parameters.get(count));
            } else {
                // 要替换%s 的数据
                replaceOperatorList.add(parameters.get(count));
            }
            count++;
        }

        if (count > 0 && parameters.size() > count) {
            int sizeNum = parameters.size();
            for (int index = count; index < sizeNum; index++) {
                placeHolderList.addAll((Collection) parameters.get(index));
            }
        }

        // %s替换数据，?占位数据
        return new Pair<>(replaceOperatorList, placeHolderList);
    }

    private Object executeInsert(PreparedStatement statement) {
        try {
            statement.executeUpdate();
            // 返回主键
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                return rs.getObject(1);
            }
        } catch (SQLException e) {
            throw new NeoException(e);
        }
        return null;
    }

    private Integer executeUpdate(PreparedStatement statement) {
        try {
            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new NeoException(e);
        }
    }

    private List<List<TableMap>> execute(PreparedStatement statement) {
        List<List<TableMap>> resultList = new ArrayList<>();
        try {
            statement.execute();
            do {
                try (ResultSet rs = statement.getResultSet()) {
                    if (rs == null) {
                        continue;
                    }
                    ResultSetMetaData meta = rs.getMetaData();
                    List<TableMap> dataList = new ArrayList<>();
                    while (rs.next()) {
                        TableMap data = TableMap.of();
                        for (int i = 1; i <= meta.getColumnCount(); i++) {
                            generateResult(data, meta, rs, i);
                        }
                        dataList.add(data);
                    }
                    resultList.add(dataList);
                }
            } while (statement.getMoreResults());
        } catch (SQLException e) {
            throw new NeoException(e);
        }
        return resultList;
    }

    private TableMap executeOne(PreparedStatement statement) {
        TableMap result = TableMap.of();
        try {
            ResultSet rs = statement.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            int col = metaData.getColumnCount();

            if (rs.next()) {
                for (int j = 1; j <= col; j++) {
                    generateResult(result, metaData, rs, j);
                }
            }
        } catch (SQLException e) {
            throw new NeoException(e);
        }
        return result;
    }

    private List<TableMap> executeList(PreparedStatement statement) {
        List<TableMap> result = new ArrayList<>();
        try {
            ResultSet rs = statement.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            int col = metaData.getColumnCount();

            while (rs.next()) {
                TableMap row = TableMap.of();
                for (int j = 1; j <= col; j++) {
                    generateResult(row, metaData, rs, j);
                }
                result.add(row);
            }
        } catch (SQLException e) {
            throw new NeoException(e);
        }
        return result;
    }

    private void generateResult(TableMap row, ResultSetMetaData metaData, ResultSet rs, Integer index) throws SQLException {
        String tableName = metaData.getTableName(index);
        String columnLabel = metaData.getColumnLabel(index);
        Object result = rs.getObject(index);
        if (null != result) {
            if (null == tableName || "".equals(tableName)) {
                row.put(DEFAULT_TABLE, columnLabel, result);
            } else {
                row.put(tableName, columnLabel, result);
            }
        }
    }

    /**
     * 克隆模式，做一个备份
     *
     * @param dataMapList 原搜索条件列表
     * @return 克隆之后的条件列表
     */
    private List<NeoMap> clone(List<NeoMap> dataMapList) {
        List<NeoMap> dataMapTem = new ArrayList<>();
        dataMapList.forEach(d -> dataMapTem.add(d.clone()));
        return dataMapTem;
    }

    /**
     * 核查数据库是否存储，或者表是否包含
     *
     * @param tableName 表名
     */
    public void checkDb(String tableName) {
        if (null == db) {
            initDb(tableName);
        } else if (!db.containTable(tableName)) {
            db.addTable(tableName);
        }

        if (!db.containTable(tableName)) {
            throw new TableNotFindException(tableName);
        }
    }

    /**
     * 核查批量更新
     * <p>
     * 其中dataList中必须包含条件中对应的列字段
     *
     * @param dataList         待处理的数据
     * @param conditionColumns 作为条件的列字段
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private void checkBatchUpdateParams(List dataList, Columns conditionColumns) {
        if (null == dataList || dataList.isEmpty() || Columns.isEmpty(conditionColumns)) {
            return;
        }

        List<NeoMap> dataMapList = NeoMap.fromArray(dataList, NamingChg.UNDERLINE);
        Set<String> keys = dataMapList.stream().flatMap(NeoMap::keyStream).collect(Collectors.toSet());

        Set<String> fields = conditionColumns.getMetaFieldSets();
        for (String field : fields) {
            if (!keys.contains(field)) {
                throw new NeoException("列" + field + "不包含搜索数据在内");
            }
        }
    }
}
