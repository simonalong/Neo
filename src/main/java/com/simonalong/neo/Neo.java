package com.simonalong.neo;

import static com.simonalong.neo.NeoConstant.ALIAS_DOM;
import static com.simonalong.neo.NeoConstant.DEFAULT_TABLE;
import static com.simonalong.neo.NeoConstant.LIMIT;
import static com.simonalong.neo.NeoConstant.ORDER_BY;
import static com.simonalong.neo.NeoConstant.LOG_PRE;
import static com.simonalong.neo.NeoConstant.SELECT;

import com.simonalong.neo.NeoMap.NamingChg;
import com.simonalong.neo.core.AbstractBaseDb;
import com.simonalong.neo.db.ConnectPool;
import com.simonalong.neo.db.NeoColumn;
import com.simonalong.neo.db.NeoDb;
import com.simonalong.neo.db.NeoJoiner;
import com.simonalong.neo.db.NeoPage;
import com.simonalong.neo.db.NeoTable;
import com.simonalong.neo.db.TimeDateConverter;
import com.simonalong.neo.exception.UidGeneratorNotInitException;
import com.simonalong.neo.sql.SqlBuilder;
import com.simonalong.neo.sql.SqlStandard.LogType;
import com.simonalong.neo.uid.UidGenerator;
import com.simonalong.neo.db.TableIndex.Index;
import com.simonalong.neo.sql.JoinType;
import com.simonalong.neo.sql.SqlExplain;
import com.simonalong.neo.sql.SqlMonitor;
import com.simonalong.neo.sql.SqlStandard;
import com.simonalong.neo.sql.TxIsolationEnum;
import com.simonalong.neo.util.ObjectUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
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
public class Neo extends AbstractBaseDb {

    @Getter
    private NeoDb db;
    @Getter
    private ConnectPool pool;
    private SqlStandard standard = SqlStandard.getInstance();
    private SqlMonitor monitor = SqlMonitor.getInstance();
    private SqlExplain explain = SqlExplain.getInstance();
    private UidGenerator uidGenerator;
    /**
     * sql解析开关
     */
    @Setter
    @Getter
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

    /**
     * 事务
     */
    @Getter
    private ThreadLocal<Boolean> txFlag;

    public Neo() {
    }

    public static void clone(Neo source, Neo target) {
        target.setExplainFlag(source.getExplainFlag());
        target.setStandardFlag(source.getStandardFlag());
        target.setMonitorFlag(source.getMonitorFlag());
        target.init(source.getPool().getDataSource());
    }

    /**
     * 针对有些数据库不需要用户，比如SQLite，这里给这种数据库提供
     */
    public static Neo connect(String url) {
        return connect(url, null, null);
    }

    public static Neo connect(String url, String username, String password) {
        Neo neo = new Neo();
        neo.init(url, username, password);
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
        if (null != txFlag) {
            txFlag.remove();
        }
    }

    /**
     * 默认采用Hikaricp 作为连接池
     * @param url 数据库连接url
     * @param username 用户名
     * @param password 密码
     */
    public void init(String url, String username, String password){
        Properties baseProper = new Properties();
        if (null != url) {
            baseProper.setProperty("jdbcUrl", url);
        }

        if (null != username) {
            baseProper.setProperty("dataSource.user", username);
        }

        if (null != password) {
            baseProper.setProperty("dataSource.password", password);
        }

        // 针对mysql的特殊设置，下面这个用于设置获取remarks信息
        baseProper.setProperty("dataSource.remarks", "true");
        baseProper.setProperty("dataSource.useInformationSchema", "true");
        this.pool = new ConnectPool(this);
        this.pool.initFromHikariCP(baseProper);
        this.txFlag = ThreadLocal.withInitial(() -> false);
    }

    public void init(DataSource dataSource){
        this.pool = new ConnectPool(this, dataSource);
        this.txFlag = ThreadLocal.withInitial(() -> false);
    }

    public void initFromDruid(Properties properties) {
        this.pool = new ConnectPool(this);
        this.pool.initFromDruid(properties);
        this.txFlag = ThreadLocal.withInitial(() -> false);
    }

    public void initFromHikariCP(Properties properties) {
        // 针对mysql的特殊设置，下面这个用于设置获取remarks信息
        properties.setProperty("dataSource.remarks", "true");
        properties.setProperty("dataSource.useInformationSchema", "true");

        this.pool = new ConnectPool(this);
        this.pool.initFromHikariCP(properties);
        this.txFlag = ThreadLocal.withInitial(() -> false);
    }

    public Connection getConnection(){
        try {
            return getPool().getConnect();
        } catch (SQLException e) {
            log.error("get connect fail", e);
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
        try (Connection con = pool.getConnect()) {
            this.db = NeoDb.of(this, con.getCatalog(), con.getSchema(), tablePres);
        } catch (SQLException e) {
            if (e instanceof SQLFeatureNotSupportedException) {
                this.db = NeoDb.of(this, tablePres);
            } else {
                e.printStackTrace();
                return this;
            }
        }
        return this;
    }

    /**
     * 添加自定义规范
     *
     * @param regex 正则表达式
     * @param desc 命中之后
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
     * 数据插入
     *
     * @param tableName 表名
     * @param valueMap 待插入的数据
     * @return 插入之后返回的插入后的值
     */
    @Override
    public NeoMap insert(String tableName, NeoMap valueMap) {
        checkDb(tableName);
        NeoMap valueMapTem = valueMap.clone();
        return tx(() -> {
            Number id = execute(false, () -> generateInsertSqlPair(tableName, valueMapTem), this::executeInsert);
            String incrementKey = db.getPrimaryAndAutoIncName(tableName);
            if (null != incrementKey) {
                return oneWithXMode(tableName, NeoMap.of(incrementKey, id));
            }
            return valueMap;
        });
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
     * 数据删除
     *
     * @param tableName 表名
     * @param searchMap where 后面的条件数据
     * @return 插入之后的返回值
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
     * @param dataMap set的更新的数据
     * @param searchMap where后面的语句条件数据
     * @return 更新之后的返回值
     */
    @Override
    public NeoMap update(String tableName, NeoMap dataMap, NeoMap searchMap) {
        NeoMap dataMapTem = dataMap.clone();
        NeoMap searchMapTem = searchMap.clone();
        return tx(() -> {
            execute(false, () -> generateUpdateSqlPair(tableName, dataMapTem, searchMapTem), this::executeUpdate);
            closeStandard();
            NeoMap result = oneWithXMode(tableName, NeoMap.of().append(searchMap).append(dataMap));
            openStandard();
            return result;
        });
    }

    /**
     * 添加排他锁执行查询
     *
     * @param tableName 表名
     * @param params 参数
     * @return 返回值
     */
    private NeoMap oneWithXMode(String tableName, NeoMap params) {
        String sql = SqlBuilder.buildOne(this, tableName, null, params) + " for update";
        List<Object> parameters = new ArrayList<>(params.values());
        return execute(false, () -> generateExeSqlPair(sql, parameters), this::executeOne);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T update(String tableName, T setEntity, NeoMap searchMap) {
        if (setEntity.getClass().isPrimitive()) {
            log.error(LOG_PRE + "数据{}是基本类型", setEntity);
            return setEntity;
        }
        NeoMap neoMap = update(tableName, NeoMap.from(setEntity, NamingChg.UNDERLINE), searchMap);
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
     * @param dataMap 待更新的数据
     * @param columns 搜索条件，其中该列为 dataMap 中对应的key的名字
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

    /**
     * 直接实体对应数据传入更新，则需要包含主键对应的key才行
     *
     * @param tableName 表明
     * @param dataMap 待更新的实体数据对应的map
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
     * @param entity 待更新的实体数据对应的map
     * @return 更新之后的实体数据对应的map
     */
    @Override
    public <T> T update(String tableName, T entity) {
        checkDb(tableName);
        if (entity.getClass().isPrimitive()) {
            log.error(LOG_PRE + "参数{}是基本类型", entity);
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
     * @param sql 只接收select 方式
     * @param parameters 参数
     * @return 一个结果Map
     */
    @Override
    public NeoMap exeOne(String sql, Object... parameters) {
        return execute(false, () -> generateExeSqlPair(sql, Arrays.asList(parameters)), this::executeOne);
    }

    @Override
    public <T> T exeOne(Class<T> tClass, String sql, Object... parameters) {
        return exeOne(sql, parameters).setNamingChg(NamingChg.UNDERLINE).as(tClass);
    }

    /**
     * 查询一行实体数据
     *
     * @param tableName 表名
     * @param columns 列名
     * @param searchMap 搜索条件
     * @return 返回一个实体的Map影射
     */
    @Override
    public NeoMap one(String tableName, Columns columns, NeoMap searchMap) {
        NeoMap searchMapTem = searchMap.clone();
        return execute(false, () -> generateOneSqlPair(tableName, columns, searchMapTem), this::executeOne);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T one(String tableName, Columns columns, T entity) {
        if (entity instanceof Number) {
            return one(tableName, columns, (Number) entity).setNamingChg(NamingChg.UNDERLINE)
                .as((Class<T>) entity.getClass());
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
     * @param entity 搜索的实体类型数据
     * @param <T> 插入的对象类型
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
     * @param id 主键id数据
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
     * 查询一行的数据
     *
     * @param sql 只接收select 方式
     * @param parameters 参数
     * @return 一个结果Map列表
     */
    @Override
    public List<NeoMap> exeList(String sql, Object... parameters) {
        if (startWithSelect(sql)) {
            return execute(true, () -> generateExeSqlPair(sql, Arrays.asList(parameters)), this::executeList);
        }
        return new ArrayList<>();
    }

    @Override
    public <T> List<T> exeList(Class<T> tClass, String sql, Object... parameters) {
        return NeoMap.asArray(exeList(sql, parameters), NamingChg.UNDERLINE, tClass);
    }

    /**
     * 查询具体的数据列表
     *
     * @param tableName 表名
     * @param columns 列数据
     * @param searchMap 搜索条件
     * @return 返回一列数据
     */
    @Override
    public List<NeoMap> list(String tableName, Columns columns, NeoMap searchMap) {
        NeoMap searchMapTem = searchMap.clone();
        return execute(true, () -> generateListSqlPair(tableName, columns, searchMapTem), this::executeList);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> list(String tableName, Columns columns, T entity) {
        if (null != entity) {
            if (entity.getClass().isPrimitive()) {
                log.error(LOG_PRE + "参数{}是基本类型", entity);
                return Collections.emptyList();
            }
            return NeoMap
                .asArray(list(tableName, columns, NeoMap.from(entity, NamingChg.UNDERLINE)), NamingChg.UNDERLINE,
                    (Class<T>) entity.getClass());
        }
        log.warn(LOG_PRE + "entity is null");
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

    /**
     * 查询返回单个值
     *
     * @param tClass 目标类的class
     * @param sql 只接收select 方式
     * @param parameters 参数
     * @param <T> 返回的目标类型
     * @return 目标类的对象
     */
    @Override
    public <T> T exeValue(Class<T> tClass, String sql, Object... parameters) {
        NeoMap result = execute(false, () -> generateExeSqlPair(sql, Arrays.asList(parameters)), this::executeOne);
        if (null != result) {
            Iterator<Object> it = result.values().iterator();
            return it.hasNext() ? ObjectUtil.cast(tClass, it.next()) : null;
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
     * @param tClass 返回值的类型
     * @param field 某个属性的名字
     * @param searchMap 搜索条件
     * @param <T> 目标类型
     * @return 指定的数据值
     */
    @Override
    public <T> T value(String tableName, Class<T> tClass, String field, NeoMap searchMap) {
        if (null != tClass && !NeoMap.isEmpty(searchMap)) {
            NeoMap searchMapTem = searchMap.clone();
            NeoMap result = execute(false, () -> generateValueSqlPair(tableName, field, searchMapTem),
                this::executeOne);
            if (null != result) {
                Iterator<Object> it = result.values().iterator();
                return it.hasNext() ? ObjectUtil.cast(tClass, it.next()) : null;
            }
        }
        return null;
    }

    @Override
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

    @Override
    public String value(String tableName, String field, NeoMap searchMap) {
        return value(tableName, String.class, field, searchMap);
    }

    /**
     * 根据实体查询属性的值，若entity为数字类型，则认为是主键
     *
     * @param tableName 表名
     * @param field 属性
     * @param entity 实体
     * @return 表某个属性的值
     */
    @Override
    public String value(String tableName, String field, Object entity) {
        checkDb(tableName);
        // 若entity为数字类型，则认为是主键
        if (entity instanceof Number) {
            String primaryKey = db.getPrimaryName(tableName);
            if (null != primaryKey && !"".equals(primaryKey)) {
                return value(tableName, String.class, field, NeoMap.of(primaryKey, entity));
            }
        }
        return value(tableName, String.class, field, NeoMap.from(entity));
    }

    @Override
    public String value(String tableName, String field, Number entity) {
        checkDb(tableName);
        String primaryKey = db.getPrimaryName(tableName);
        if (null != primaryKey && !"".equals(primaryKey)) {
            return value(tableName, String.class, field, NeoMap.of(primaryKey, entity));
        }
        log.warn(LOG_PRE + "db {}'s primary key is null, please set", tableName);
        return null;
    }

    /**
     * 查询一行的数据
     *
     * @param tClass 数据实体的类
     * @param sql 查询一行的sql
     * @param parameters 查询的搜索参数
     * @param <T> 数据实体的类型
     * @return 查询到的数据实体，如果没有找到则返回null
     */
    @Override
    public <T> List<T> exeValues(Class<T> tClass, String sql, Object... parameters) {
        List<NeoMap> resultList = execute(true, () -> generateExeSqlPair(sql, Arrays.asList(parameters)),
            this::executeList);
        if (null != resultList && !resultList.isEmpty()) {
            return resultList.stream().map(r -> {
                Iterator<Object> it = r.values().iterator();
                return it.hasNext() ? ObjectUtil.cast(tClass, it.next()) : null;
            }).filter(Objects::nonNull).distinct().collect(Collectors.toList());
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
     * @param tClass 实体类的类
     * @param field 列名
     * @param searchMap 搜索条件
     * @param <T> 目标类型
     * @return 一列值
     */
    @Override
    public <T> List<T> values(String tableName, Class<T> tClass, String field, NeoMap searchMap) {
        NeoMap searchMapTem = searchMap.clone();
        List<NeoMap> resultList = execute(false, () -> generateValuesSqlPair(tableName, field, searchMapTem),
            this::executeList);

        if (null != resultList && !resultList.isEmpty()) {
            return resultList.stream()
                .map(r -> r.get(tClass, field))
                .filter(Objects::nonNull).distinct()
                .collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public <T> List<T> values(String tableName, Class<T> tClass, String field, Object entity) {
        checkDb(tableName);
        // 若entity为数字类型，则认为是主键
        if (entity instanceof Number) {
            String primaryKey = db.getPrimaryName(tableName);
            if (null != primaryKey && !"".equals(primaryKey)) {
                return values(tableName, tClass, field, NeoMap.of(primaryKey, entity));
            }
        }
        return values(tableName, tClass, field, NeoMap.from(entity));
    }

    @Override
    public List<String> values(String tableName, String field, NeoMap searchMap) {
        return values(tableName, String.class, field, searchMap);
    }

    /**
     * 通过实体查询一列的列表
     *
     * @param tableName 表名
     * @param field 列名
     * @param entity 实体数据
     * @return 列对应的列表
     */
    @Override
    public List<String> values(String tableName, String field, Object entity) {
        checkDb(tableName);
        // 若entity为数字类型，则认为是主键
        if (entity instanceof Number) {
            String primaryKey = db.getPrimaryName(tableName);
            if (null != primaryKey && !"".equals(primaryKey)) {
                return values(tableName, String.class, field, NeoMap.of(primaryKey, entity));
            }
        }
        return values(tableName, String.class, field, NeoMap.from(entity));
    }

    @Override
    public List<String> values(String tableName, String field) {
        return values(tableName, String.class, field, NeoMap.of());
    }

    /**
     * 执行分页数据查询
     *
     * @param sql 对应的sql，里面可以包含limit也可以不包含，都兼容，如果不包含，则会追加，如果包含，则会根据是否需要填充，进行填充参数或者直接执行
     * @param startIndex 分页起始
     * @param pageSize 分页大小
     * @param parameters 参数
     * @return 分页对应的数据
     */
    @Override
    public List<NeoMap> exePage(String sql, Integer startIndex, Integer pageSize, Object... parameters) {
        if (startWithSelect(sql)) {
            return execute(true, () -> generateExePageSqlPair(sql, Arrays.asList(parameters), startIndex, pageSize),
                this::executeList);
        }
        return new ArrayList<>();
    }

    @Override
    public List<NeoMap> exePage(String sql, NeoPage neoPage, Object... parameters) {
        return exePage(sql, neoPage.getStartIndex(), neoPage.getPageSize(), parameters);
    }

    /**
     * 分组数据
     *
     * @param tableName 表名
     * @param columns 列的属性
     * @param searchMap 搜索条件
     * @param page 分页
     * @return 分页对应的数据
     */
    @Override
    public List<NeoMap> page(String tableName, Columns columns, NeoMap searchMap, NeoPage page) {
        NeoMap searchMapTem = searchMap.clone();
        return execute(true,
            () -> generatePageSqlPair(tableName, columns, searchMapTem, page.getStartIndex(), page.getPageSize()),
            this::executeList);
    }

    @Override
    public List<NeoMap> page(String tableName, Columns columns, NeoMap searchMap) {
        return page(tableName, columns, searchMap.delete("pager"), NeoPage.from(searchMap));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> page(String tableName, Columns columns, T entity, NeoPage page) {
        if (entity.getClass().isPrimitive()) {
            log.error(LOG_PRE + "参数{}是基本类型");
            return Collections.emptyList();
        }
        return NeoMap.asArray(page(tableName, columns, NeoMap.from(entity, NamingChg.UNDERLINE), page),
            NamingChg.UNDERLINE, (Class<T>) entity.getClass());
    }

    @Override
    public List<NeoMap> page(String tableName, NeoMap searchMap, NeoPage page) {
        return page(tableName, Columns.of().setNeo(this).table(tableName), searchMap, page);
    }

    @Override
    public <T> List<T> page(String tableName, T entity, NeoPage page) {
        return page(tableName, Columns.of().setNeo(this).table(tableName), entity, page);
    }

    @Override
    public List<NeoMap> page(String tableName, Columns columns, NeoPage page) {
        return page(tableName, columns, NeoMap.of(), page);
    }

    @Override
    public List<NeoMap> page(String tableName, NeoPage page) {
        return page(tableName, Columns.of().setNeo(this).table(tableName), NeoMap.of(), page);
    }

    /**
     * 分页搜索
     *
     * @param tableName 表名
     * @param searchMap 搜索条件，其中默认searchMap中包含key为：'pager'的数据，里面是pageNo和pageSize
     * @return 分页数据
     */
    @Override
    public List<NeoMap> page(String tableName, NeoMap searchMap) {
        return page(tableName, searchMap.assignExcept("pager"), NeoPage.from(searchMap));
    }

    /**
     * 执行个数数据的查询
     *
     * @param sql 只接收select 方式
     * @param parameters 参数
     * @return 一个结果Map
     */
    @Override
    public Integer exeCount(String sql, Object... parameters) {
        NeoMap result = execute(false, () -> generateExeSqlPair(sql, Arrays.asList(parameters)), this::executeOne);
        if (null != result) {
            Iterator<Object> it = result.values().iterator();
            return it.hasNext() ? ObjectUtil.cast(Integer.class, it.next()) : null;
        }
        return null;
    }

    @Override
    public Integer count(String tableName, NeoMap searchMap) {
        NeoMap searchMapTem = searchMap.clone();
        NeoMap result = execute(false, () -> generateCountSqlPair(tableName, searchMap), this::executeOne);
        if (null != result) {
            Iterator<Object> it = result.values().iterator();
            return it.hasNext() ? ObjectUtil.cast(Integer.class, it.next()) : null;
        }
        return null;
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
     * @param sql 待执行的sql
     * @param parameters 占位符和转换符的数据
     * @return 外层是多结果集，内层是对应的单结果集中的数据，为list形式的数据封装
     */
    @Override
    public List<List<NeoMap>> execute(String sql, Object... parameters) {
        return execute(false, () -> generateExeSqlPair(sql, Arrays.asList(parameters)), this::execute);
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
     * 默认的join采用的是innerJoin
     *
     * @param leftTableName 左表表名
     * @param rightTableName 右表表名
     * @return 做关联的关联器
     */
    public NeoJoiner join(String leftTableName, String rightTableName) {
        return innerJoin(leftTableName, rightTableName);
    }

    /**
     * 左关联，只保留左表的信息
     *
     * @param leftTableName 左表表名
     * @param rightTableName 右表表名
     * @return 做关联的关联器
     */
    public NeoJoiner leftJoin(String leftTableName, String rightTableName) {
        return new NeoJoiner(this, leftTableName, rightTableName, JoinType.LEFT_JOIN);
    }

    /**
     * 左关联，只保留左表的信息
     *
     * @param leftTableName 左表表名
     * @param rightTableName 右表表名
     * @return 做关联的关联器
     */
    public NeoJoiner rightJoin(String leftTableName, String rightTableName) {
        return new NeoJoiner(this, leftTableName, rightTableName, JoinType.RIGHT_JOIN);
    }

    /**
     * 左关联，只保留左表的信息
     *
     * @param leftTableName 左表表名
     * @param rightTableName 右表表名
     * @return 做关联的关联器
     */
    public NeoJoiner innerJoin(String leftTableName, String rightTableName) {
        return new NeoJoiner(this, leftTableName, rightTableName, JoinType.INNER_JOIN);
    }

    /**
     * 左关联，只保留左表的信息
     *
     * @param leftTableName 左表表名
     * @param rightTableName 右表表名
     * @return 做关联的关联器
     */
    public NeoJoiner outerJoin(String leftTableName, String rightTableName) {
        return new NeoJoiner(this, leftTableName, rightTableName, JoinType.OUTER_JOIN);
    }

    /**
     * 左关联，只保留左表的信息
     *
     * @param leftTableName 左表表名
     * @param rightTableName 右表表名
     * @return 做关联的关联器
     */
    public NeoJoiner leftJoinExceptInner(String leftTableName, String rightTableName) {
        return new NeoJoiner(this, leftTableName, rightTableName, JoinType.LEFT_JOIN_EXCEPT_INNER);
    }

    /**
     * 左关联，只保留左表的信息
     *
     * @param leftTableName 左表表名
     * @param rightTableName 右表表名
     * @return 做关联的关联器
     */
    public NeoJoiner rightJoinExceptInner(String leftTableName, String rightTableName) {
        return new NeoJoiner(this, leftTableName, rightTableName, JoinType.RIGHT_JOIN_EXCEPT_INNER);
    }

    /**
     * 左关联，只保留左表的信息
     *
     * @param leftTableName 左表表名
     * @param rightTableName 右表表名
     * @return 做关联的关联器
     */
    public NeoJoiner outerJoinExceptInner(String leftTableName, String rightTableName) {
        return new NeoJoiner(this, leftTableName, rightTableName, JoinType.OUTER_JOIN_EXCEPT_INNER);
    }

    /**
     * 批量插入NeoMap列表数据
     *
     * @param tableName 表名
     * @param dataMapList 设置数据和对应的搜索map的映射集合
     * @return 插入的数据个数：0或者all
     */
    @Override
    public Integer batchInsert(String tableName, List<NeoMap> dataMapList) {
        if (null == dataMapList || dataMapList.isEmpty()) {
            return 0;
        }
        List<NeoMap> dataMapListTem = clone(dataMapList);
        return executeBatch(generateBatchInsertPair(tableName, dataMapListTem));
    }

    /**
     * 批量插入实体列表
     *
     * @param tableName 表名
     * @param dataList 数据列表
     * @param <T> 目标类型
     * @return 插入的数据个数：0或者all
     */
    @Override
    public <T> Integer batchInsertEntity(String tableName, List<T> dataList) {
        return batchInsert(tableName, NeoMap.fromArray(dataList));
    }

    /**
     * 批量更新
     *
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
     *
     * @param tableName 表名
     * @param dataList 待更新的数据
     * @return 批量更新的个数：0或者all
     */
    @Override
    public Integer batchUpdate(String tableName, List<NeoMap> dataList) {
        checkDb(tableName);
        Columns columns = Columns.of(db.getPrimaryName(tableName));
        List<NeoMap> dataListTem = clone(dataList);
        return innerBatchUpdate(tableName, buildBatchValueAndWhereList(tableName, dataListTem, columns));
    }

    /**
     * 批量更新，指定搜索的哪些列
     *
     * @param tableName 表名
     * @param dataList 待更新的数据
     * @param columns where搜索条件用到的前面待更新的数据的列
     * @return 批量更新的个数：0或者all
     */
    @Override
    public Integer batchUpdate(String tableName, List<NeoMap> dataList, Columns columns) {
        List<NeoMap> dataListTem = clone(dataList);
        return innerBatchUpdate(tableName, buildBatchValueAndWhereList(tableName, dataListTem, columns));
    }

    /**
     * 批量更新，默认根据主键进行更新
     *
     * @param tableName 表名
     * @param dataList 待更新的数据
     * @param <T> 目标类型
     * @return 批量更新的个数：0或者all
     */
    @Override
    public <T> Integer batchUpdateEntity(String tableName, List<T> dataList) {
        checkDb(tableName);
        Columns columns = Columns.of(NeoMap.dbToJavaStr(db.getPrimaryName(tableName)));
        return innerBatchUpdate(tableName, buildBatchValueAndWhereListFromEntity(dataList, columns));
    }

    /**
     * 批量执行更新，指定搜索的哪些列
     *
     * @param tableName 表名
     * @param dataList 数据列表
     * @param columns 注意：这里的列为对象的属性名字，这里不是对象转换到NeoMap之后的列
     * @param <T> 目标类型
     * @return 批量更新的个数：0或者all
     */
    @Override
    public <T> Integer batchUpdateEntity(String tableName, List<T> dataList, Columns columns) {
        return innerBatchUpdate(tableName, buildBatchValueAndWhereListFromEntity(dataList, columns));
    }

    /**
     * 事务的执行 注意： 1.这里的事务传播机制采用，如果已经有事务在运行，则挂接在高层事务里面，这里进行最外层统一提交 2.隔离级别采用数据库默认 3.读写的事务
     *
     * @param runnable 待执行的任务
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
     * @param <T> 目标类型
     * @return 事务执行完成返回的数据
     */
    public <T> T tx(Supplier<T> supplier) {
        return tx(null, null, supplier);
    }

    /**
     * 事务的执行 注意：这里的事务传播机制采用，如果已经有事务在运行，则挂接在高层事务里面，这里进行最外层统一提交
     *
     * @param readOnly 事务的只读属性，默认为false
     * @param runnable 待执行的任务
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
     * @param <T> 待返回值的类型
     * @return 事务执行之后的返回值
     */
    public <T> T tx(Boolean readOnly, Supplier<T> supplier) {
        return tx(null, readOnly, supplier);
    }

    /**
     * 事务的执行 注意：这里的事务传播机制采用，如果已经有事务在运行，则挂接在高层事务里面，这里进行最外层统一提交
     *
     * @param isolationEnum 事务的隔离级别，如果为null，则采用数据库的默认隔离级别
     * @param runnable 待执行的任务
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
     * @param supplier 待执行的任务
     * @param <T> 待返回值的类型
     * @return 事务执行之后的返回值
     */
    public <T> T tx(TxIsolationEnum isolationEnum, Supplier<T> supplier) {
        return tx(isolationEnum, false, supplier);
    }

    /**
     * 事务的执行 注意：这里的事务传播机制采用，如果已经有事务在运行，则挂接在高层事务里面，这里进行最外层统一提交
     *
     * @param isolationEnum 事务的隔离级别，如果为null，则采用数据库的默认隔离级别
     * @param readOnly 事务的只读属性，默认为false
     * @param runnable 待执行的任务
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
     * @param readOnly 事务的只读属性，默认为false
     * @param supplier 待执行的任务
     * @param <T> 目标类型
     * @return 事务执行完成返回的数据
     */
    public <T> T tx(TxIsolationEnum isolationEnum, Boolean readOnly, Supplier<T> supplier) {
        // 针对事务嵌套这里采用最外层事务提交
        Boolean originalTxFlag = txFlag.get();
        txFlag.set(true);
        try {
            pool.setTxConfig(isolationEnum, readOnly);
            if (openTxMonitor()) {
                // 统计sql
                monitor.startTx();
            }
            T result = supplier.get();
            pool.submit();

            if (openTxMonitor()) {
                // 统计sql信息
                monitor.calculate();
            }
            return result;
        } catch (Exception e) {
            log.error(LOG_PRE + "[提交失败，事务回滚]");
            e.printStackTrace();
            try {
                pool.rollback();
            } catch (SQLException e1) {
                log.error(LOG_PRE + "[回滚失败]");
                e1.printStackTrace();
            }
        } finally {
            txFlag.set(originalTxFlag);
        }
        return null;
    }

    /**
     * 获取创建sql的语句 {@code create db xxx{ id xxxx; } comment ='xxxx'; }
     *
     * @param tableName 表名
     * @return 表的创建语句
     */
    public String getTableCreate(String tableName) {
        return (String) (execute("show create table `" + tableName + "`").get(0).get(0).get("Create Table"));
    }

    /**
     * 获取全局id
     *
     * @return 当前分布式系统内的唯一id
     */
    public Long getUuid() {
        if (null == uidGenerator) {
            throw new UidGeneratorNotInitException();
        }
        return uidGenerator.getUuid();
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

    /**
     * 开启全局id生成器
     */
    public void openUidGenerator() {
        uidGenerator = UidGenerator.getInstance(this);
    }

    public Boolean isTransaction() {
        return txFlag.get();
    }

    /**
     * explain 命令解析sql
     *
     * @param multiLine 是否多行数据
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
     * @param multiLine 是否多行执行，对于多行执行，这里会进行explain 对应的sql核查
     * @param sqlSupplier sql和对应的参数的拼接生成器
     * @param stateFun sql Statement执行回调函数
     * @param <T> 返回值的类型
     * @return 返回对应的要求的返回值
     */
    protected <T> T execute(Boolean multiLine, Supplier<Pair<String, List<Object>>> sqlSupplier,
        Function<PreparedStatement, T> stateFun) {
        Pair<String, List<Object>> sqlPair = sqlSupplier.get();
        String sql = sqlPair.getKey();
        List<Object> parameters = sqlPair.getValue();

        // sql 多行查询的explain衡量
        explain(multiLine, sqlPair);
        try (Connection con = pool.getConnect()) {
            try (PreparedStatement state = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                if (standardFlag) {
                    // sql规范化校验
                    standard.valid(sql);
                }
                if (openMonitor()) {
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
                    monitor.calculate();
                }
                return result;
            } catch (SQLException e) {
                e.printStackTrace();
                log.error(LOG_PRE + "sql=> " + sql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(LOG_PRE + "sql=> " + sql);
        } finally {
            if (openMonitor()) {
                monitor.close();
            }
        }
        return null;
    }

    protected Integer executeBatch(Pair<String, List<List<Object>>> sqlPair) {
        String sql = sqlPair.getKey();
        List<List<Object>> parameterList = sqlPair.getValue();

        try (Connection con = pool.getConnect()) {
            try (PreparedStatement state = con.prepareStatement(sql)) {
                con.setAutoCommit(false);
                if (standardFlag) {
                    // sql规范化校验
                    standard.valid(sql);
                }
                if (openMonitor()) {
                    // 添加对sql的监控
                    monitor.start(sql, Collections.singletonList(parameterList));
                }

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

                state.executeBatch();
                con.commit();
                con.setAutoCommit(true);

                if (openMonitor()) {
                    // 统计sql信息
                    monitor.calculate();
                }
                return batchCount;
            } catch (SQLException e) {
                log.error(LOG_PRE + "[执行异常] [sql=> " + sql + " ]");
                e.printStackTrace();
                try {
                    // 出现异常，进行回滚
                    if (!con.isClosed()) {
                        con.rollback();
                        con.setAutoCommit(true);
                    }
                } catch (SQLException e1) {
                    log.error(LOG_PRE + "[回滚异常]");
                    e1.printStackTrace();
                }
            }
        } catch (SQLException e) {
            log.error(LOG_PRE + "[执行异常] [sql=> " + sql + " ]");
            e.printStackTrace();
        } finally {
            if (openMonitor()) {
                monitor.close();
            }
        }
        return 0;
    }

    /**
     * 是否开启sql监控：针对一次执行的情况，只有在非事务且监控开启情况下才对单独执行监控
     */
    private Boolean openMonitor() {
        return !txFlag.get() && monitorFlag;
    }

    /**
     * 是否开启sql监控：针对一次执行的情况，只有在非事务且监控开启情况下才对单独执行监控
     */
    private Boolean openTxMonitor() {
        return txFlag.get();
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
        valueMap = filterNonDbColumn(tableName, valueMap);
        return new Pair<>(SqlBuilder.buildInsert(tableName, valueMap), new ArrayList<>(valueMap.values()));
    }

    /**
     * 生成删除的sql和参数 key: delete xxx value: 对应的参数
     */
    private Pair<String, List<Object>> generateDeleteSqlPair(String tableName, NeoMap searchMap) {
        searchMap = filterNonDbColumn(tableName, searchMap);
        return new Pair<>(SqlBuilder.buildDelete(tableName, searchMap), new ArrayList<>(searchMap.values()));
    }

    /**
     * 生成插入的sql和参数 key: update xxx value: 对应的参数
     */
    private Pair<String, List<Object>> generateUpdateSqlPair(String tableName, NeoMap dataMap, NeoMap searchMap) {
        searchMap = filterNonDbColumn(tableName, searchMap);
        dataMap = filterNonDbColumn(tableName, dataMap);
        return new Pair<>(SqlBuilder.buildUpdate(tableName, dataMap, searchMap), NeoMap.values(dataMap, searchMap));
    }

    /**
     * 生成查询一条数据的sql和参数 key: select xxx value: 对应的参数
     */
    private Pair<String, List<Object>> generateOneSqlPair(String tableName, Columns columns, NeoMap searchMap) {
        searchMap = filterNonDbColumn(tableName, searchMap);
        return new Pair<>(SqlBuilder.buildOne(this, tableName, columns, searchMap), generateValueList(searchMap));
    }

    /**
     * 生成查询列表的sql和参数 key: select xxx value: 对应的参数
     */
    private Pair<String, List<Object>> generateListSqlPair(String tableName, Columns columns, NeoMap searchMap) {
        searchMap = filterNonDbColumn(tableName, searchMap);
        return new Pair<>(SqlBuilder.buildList(this, tableName, columns, searchMap), generateValueList(searchMap));
    }

    /**
     * 生成查询分页数据的sql和参数 key: select xxx value: 对应的参数
     */
    private Pair<String, List<Object>> generatePageSqlPair(String tableName, Columns columns, NeoMap searchMap,
        Integer startIndex, Integer pageSize) {
        searchMap = filterNonDbColumn(tableName, searchMap);
        return new Pair<>(SqlBuilder.buildPageList(this, tableName, columns, searchMap, startIndex, pageSize),
            generateValueList(searchMap));
    }

    /**
     * 生成查询总数的sql和参数 key: select xxx value: 对应的参数
     */
    private Pair<String, List<Object>> generateCountSqlPair(String tableName, NeoMap searchMap) {
        searchMap = filterNonDbColumn(tableName, searchMap);
        return new Pair<>(SqlBuilder.buildCount(tableName, searchMap), generateValueList(searchMap));
    }

    /**
     * 生成查询总数的sql和参数 key: select xxx value: 对应的参数
     */
    private Pair<String, List<Object>> generateValueSqlPair(String tableName, String field, NeoMap searchMap) {
        searchMap = filterNonDbColumn(tableName, searchMap);
        return new Pair<>(SqlBuilder.buildValue(tableName, field, searchMap), generateValueList(searchMap));
    }

    /**
     * 生成查询值列表的sql和参数 key: select xxx value: 对应的参数
     */
    private Pair<String, List<Object>> generateValuesSqlPair(String tableName, String field, NeoMap searchMap) {
        searchMap = filterNonDbColumn(tableName, searchMap);
        return new Pair<>(SqlBuilder.buildValues(tableName, field, searchMap), generateValueList(searchMap));
    }

    /**
     * 通过表名和查询参数生成查询一行数据的sql
     */
    private Pair<String, List<Object>> generateExeSqlPair(String sqlOrigin, List<Object> parameters) {
        Pair<List<Object>, List<Object>> pair = replaceHolderParameters(sqlOrigin, parameters);
        List<Object> keys = pair.getKey();
        String sql = sqlOrigin;

        if (null != keys && !keys.isEmpty()) {
            sql = String.format(sqlOrigin, keys.toArray());
        }

        return new Pair<>(sql, pair.getValue());
    }

    /**
     * 通过原始sql和分页，进行拼接执行
     */
    private Pair<String, List<Object>> generateExePageSqlPair(String sqlOrigin, List<Object> parameters,
        Integer startIndex, Integer pageSize) {
        if (!sqlOrigin.contains(LIMIT)) {
            sqlOrigin += " limit " + startIndex + ", " + pageSize;
        }
        return generateExeSqlPair(sqlOrigin, parameters);
    }

    /**
     * 通过表名和查询参数生成查询一行数据的sql
     */
    private Pair<String, List<List<Object>>> generateBatchInsertPair(String tableName, List<NeoMap> parameters) {
        return new Pair<>(SqlBuilder.buildInsert(tableName, parameters.get(0)),
            parameters.stream().map(r -> filterNonDbColumn(tableName, r)).map(this::generateValueList)
                .collect(Collectors.toList()));
    }

    /**
     * 通过表名和查询参数生成查询一行数据的sql
     */
    private Pair<String, List<List<Object>>> generateBatchUpdatePair(String tableName,
        List<Pair<NeoMap, NeoMap>> pairList) {
        return new Pair<>(SqlBuilder.buildUpdate(tableName, pairList.get(0).getKey(), pairList.get(0).getValue()),
            pairList.stream().map(p -> NeoMap.values(p.getKey(), p.getValue())).collect(Collectors.toList()));
    }

    /**
     * 构建批次的value和where语句对的list
     *
     * @param tableName 表名
     * @param dataList 全部的数据列表
     * @param columns 指定哪些列的值作为查询条件，该为NeoMap中的key
     * @return 其中每个数据的key都是update中的set中用的值，value都是where中的查询条件
     */
    private List<Pair<NeoMap, NeoMap>> buildBatchValueAndWhereList(String tableName, List<NeoMap> dataList,
        Columns columns) {
        if (null == dataList || dataList.isEmpty()) {
            return new ArrayList<>();
        }
        return dataList.stream().map(r -> filterNonDbColumn(tableName, r)).map(m -> new Pair<>(m, m.assign(columns)))
            .collect(Collectors.toList());
    }

    /**
     * 构建批次的value和where语句对的list
     *
     * @param dataList 全部的数据列表
     * @param columns 指定哪些列的值作为查询条件，该为NeoMap中的key
     * @return 其中每个数据的key都是update中的set中用的值，value都是where中的查询条件
     */
    private <T> List<Pair<NeoMap, NeoMap>> buildBatchValueAndWhereListFromEntity(List<T> dataList, Columns columns) {
        if (null == dataList || dataList.isEmpty()) {
            return new ArrayList<>();
        }
        return dataList.stream()
            .map(m -> new Pair<>(NeoMap.from(m, NamingChg.UNDERLINE), NeoMap.from(m, columns, NamingChg.UNDERLINE)))
            .collect(Collectors.toList());
    }

    private List<Object> generateValueList(NeoMap searchMap) {
        return SqlBuilder.buildValueList(searchMap);
    }

    /**
     * 过滤不是列名的key，并且对其中NeoMap中为Long类型的时间类型进行转换
     *
     * 注意： 由于mysql中时间类型year不支持{@link java.util.Date}这个类型直接传入（其他四个时间类型支持），因此需要单独处理
     *
     * @param dataMap 待处理的数据
     * @return 处理后的数据
     */
    private NeoMap filterNonDbColumn(String tableName, NeoMap dataMap) {
        // key为列名，value为：key为列的数据库类型名字，value为列对应的java中的类型
        Map<String, Pair<String, Class<?>>> columnMap = getColumnList(tableName).stream()
            .collect(
                Collectors.toMap(NeoColumn::getColumnName, r -> new Pair<>(r.getColumnTypeName(), r.getJavaClass())));
        NeoMap result = NeoMap.of();
        dataMap.stream().filter(e -> columnMap.containsKey(e.getKey()) || e.getKey().equals(ORDER_BY))
            .forEach(r -> {
                String key = r.getKey();
                Object value = r.getValue();
                if (!key.equals(ORDER_BY)) {
                    Pair<String, Class<?>> typeAndClass = columnMap.get(key);
                    result
                        .put(key, TimeDateConverter.longToDbTime(typeAndClass.getValue(), typeAndClass.getKey(), value),
                            false);
                } else {
                    result.put(key, value, false);
                }
            });
        dataMap.clear();
        return result;
    }

    /**
     * 将转换符和占位符拆分开
     *
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
            } else {
                replaceOperatorList.add(parameters.get(count));
            }
            count++;
        }
        return new Pair<>(replaceOperatorList, placeHolderList);
    }

    private Number executeInsert(PreparedStatement statement) {
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

    private Integer executeUpdate(PreparedStatement statement) {
        try {
            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private List<List<NeoMap>> execute(PreparedStatement statement) {
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
                            generateResult(data, meta, rs, i);
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

    private NeoMap executeOne(PreparedStatement statement) {
        NeoMap result = NeoMap.of();
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
            e.printStackTrace();
        }
        return result;
    }

    private List<NeoMap> executeList(PreparedStatement statement) {
        List<NeoMap> result = new ArrayList<>();
        try {
            ResultSet rs = statement.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            int col = metaData.getColumnCount();

            while (rs.next()) {
                NeoMap row = NeoMap.of();
                for (int j = 1; j <= col; j++) {
                    generateResult(row, metaData, rs, j);
                }
                result.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private Pair<String, String> getTableAliasAndColumn(String columnLabel) {
        if (columnLabel.contains(ALIAS_DOM)) {
            int index = columnLabel.indexOf(ALIAS_DOM);
            return new Pair<>(columnLabel.substring(0, index),
                columnLabel.substring(index + ALIAS_DOM.length(), columnLabel.length()));
        }
        return new Pair<>(DEFAULT_TABLE, columnLabel);
    }

    private void generateResult(NeoMap row, ResultSetMetaData metaData, ResultSet rs, Integer index)
        throws SQLException {
        Pair<String, String> pair = getTableAliasAndColumn(metaData.getColumnLabel(index));
        String tableAlis = pair.getKey();
        String columnLabel = pair.getValue();
        row.put(tableAlis, columnLabel, TimeDateConverter.dbTimeToLong(rs.getObject(index)));
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
    private void checkDb(String tableName) {
        if (null == db) {
            initDb(tableName);
        } else if (!db.containTable(tableName)) {
            db.addTable(tableName);
        }
    }
}
