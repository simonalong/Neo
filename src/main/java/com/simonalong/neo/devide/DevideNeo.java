package com.simonalong.neo.devide;

import com.simonalong.neo.Columns;
import com.simonalong.neo.Neo;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.core.AbstractClassExtenderDb;
import com.simonalong.neo.db.NeoPage;
import com.simonalong.neo.xa.NeoXa;
import com.simonalong.neo.devide.strategy.DevideStrategy;
import com.simonalong.neo.devide.strategy.DevideStrategyFactory;
import com.simonalong.neo.devide.strategy.DevideTypeEnum;
import com.simonalong.neo.exception.NeoException;
import com.simonalong.neo.exception.NeoNotSupport;
import com.simonalong.neo.exception.NotFindDevideDbException;
import com.simonalong.neo.exception.NotFindDevideTableException;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 分库分表管理对象
 *
 * @author zhouzhenyong
 * @since 2019/6/21 下午3:48
 */
@NoArgsConstructor
public final class DevideNeo extends AbstractClassExtenderDb {

    /**
     * 分库的对应的列名
     */
    private String devideDbColumnName;
    /**
     * 分库用的逻辑表名
     */
    private String devideDbLoginTableName;
    /**
     * 待分库的库集合
     */
    private List<Neo> dbList = new ArrayList<>();
    /**
     * 没有命中走的默认db
     */
    @Setter
    private Neo defaultDb;
    /**
     * 多资源的XA事务管理
     */
    private NeoXa neoXa;
    /**
     * 分库分表策略
     */
    @Setter
    private DevideStrategy devideStrategy;
    /**
     * 分库分表策略枚举，设置这个会自动匹配系统内部的分库策略
     */
    @Setter
    private DevideTypeEnum devideTypeEnum = DevideTypeEnum.HASH;
    /**
     * 表的哈希处理映射, key：表名，value表的哈希信息
     */
    private Map<String, TableDevideConfig> devideTableInfoMap = new ConcurrentHashMap<>();
    /**
     * 没有命中分表，则走默认的表（需要使用方自行设置）。key为逻辑表，value为实际表
     */
    @Setter
    private Map<String, String> defaultTableNameMap = new ConcurrentHashMap<>();

    public void setDbList(List<Neo> dbList) {
        if (null != dbList) {
            this.dbList = dbList;
            this.neoXa = NeoXa.ofNeoList(dbList);
        }
    }

    /**
     * 设置分库
     *
     * @param devideDbLoginTableName 分库采用的表的逻辑表名，比如实际表名为neo_table12，但是目前表也分表了，则为neo_table，若只分库了，表都一样，则用实际的表即可
     * @param columnName             分库的表的列名
     */
    public void setDevideDb(String devideDbLoginTableName, String columnName) {
        this.devideDbColumnName = columnName;
        this.devideDbLoginTableName = devideDbLoginTableName;
    }

    /**
     * 设置分表
     * <p>
     * 最后得到的库名，举例比如：db0, db1, ... db12
     *
     * @param devideTables 分表的表达式，{0, 12}作为后缀。比如：table_name{0, 12}
     * @param columnName   分表用到的列名
     */
    public void setDevideTable(String devideTables, String columnName) {
        if (null == devideTables || "".equals(devideTables)) {
            throw new NeoException("数据配置为空");
        }
        if (null == columnName || "".equals(columnName)) {
            throw new NeoException("数据配置为空");
        }

        String regex = "^(.*)\\{(.*),(\\s)*(.*)}$";
        Matcher matcher = Pattern.compile(regex).matcher(devideTables);
        if (matcher.find()) {
            TableDevideConfig tableDevideConfig = new TableDevideConfig();
            String tableName = matcher.group(1);

            // 分库分表字段校验
            if (null != devideDbLoginTableName && devideDbLoginTableName.equals(tableName)) {
                if (null != devideDbColumnName && devideDbColumnName.equals(columnName)) {
                    throw new NeoException("分库的列和分表的列不可为同一个，否则分表失效");
                }
            }

            Integer min = Integer.valueOf(matcher.group(2));
            Integer max = Integer.valueOf(matcher.group(4));
            if (min >= max) {
                throw new NeoException("数据配置错误: 最大值不能小于最小值");
            }
            tableDevideConfig.setTableName(tableName);
            tableDevideConfig.setColumnName(columnName);
            tableDevideConfig.setActTableNameList(getActTableNameList(tableName, min, max));
            tableDevideConfig.setMin(min);

            Integer size = max - min;
            tableDevideConfig.setSize(size);

            // 个数为2的次方，则设置mark数据
            if ((size & (size - 1)) == 0) {
                tableDevideConfig.setMarkNum(size);
            }

            devideTableInfoMap.putIfAbsent(tableName, tableDevideConfig);
        } else {
            throw new NeoException("分表表名: " + devideTables + "，不符合解析方式");
        }
    }

    /**
     * 初始化分库分表策略
     */
    public void init() {
        // 默认采用哈希方式分库分表
        if (null == devideStrategy) {
            if (null != dbList) {
                this.devideStrategy = DevideStrategyFactory.getStrategy(devideTypeEnum, dbList.size(), devideTableInfoMap);
            }
        }

        validateTable();
    }

    /**
     * 获取多库多表的处理
     *
     * @return 多库多表实体
     */
    public DevideMultiNeo asDevideMultiNeo() {
        return new DevideMultiNeo(dbList, defaultDb, devideTableInfoMap);
    }

    private List<String> getActTableNameList(String tableName, Integer min, Integer max) {
        List<String> resultList = new ArrayList<>();
        for (int index = min; index < max; index++) {
            resultList.add(tableName + index);
        }
        return resultList;
    }

    /**
     * 如果配置了分表，则要查看是否配置的分表都是存在的
     */
    private void validateTable() {
        if (!dbList.isEmpty()) {
            for (Neo db: dbList) {
                List<String> actTableNameList = devideTableInfoMap.values().stream().flatMap(e->e.getActTableNameList().stream()).collect(Collectors.toList());
                for (String tableName : actTableNameList) {
                    db.test(tableName);
                }
            }
        } else if (null != defaultDb) {
            List<String> actTableNameList = devideTableInfoMap.values().stream().flatMap(e->e.getActTableNameList().stream()).collect(Collectors.toList());
            for (String tableName : actTableNameList) {
                defaultDb.test(tableName);
            }
        }
    }

    /**
     * 分库路由获取实际的表名
     *
     * @param tableName 逻辑表明
     * @param dataMap   查询实体
     * @return 实际库名。没有找到，则报异常
     */
    private Neo getDevideDb(String tableName, NeoMap dataMap) {
        validate(tableName);
        Neo dbFinal = devideStrategy.getDb(dbList, getDevideDbColumnValue(tableName, dataMap));
        if (null != dbFinal) {
            return dbFinal;
        } else if (null != defaultDb) {
            return defaultDb;
        }
        throw new NotFindDevideDbException("table: " + tableName);
    }

    /**
     * 分库路由获取实际的表名
     *
     * @param tableName 逻辑表明
     * @param object    查询实体
     * @return 实际库名。没有找到，则报异常
     */
    private Neo getDevideDb(String tableName, Object object) {
        validate(tableName);
        Neo dbFinal = devideStrategy.getDb(dbList, getDevideDbColumnValue(tableName, object));
        if (null != dbFinal) {
            return dbFinal;
        } else if (null != defaultDb) {
            return defaultDb;
        }
        throw new NotFindDevideDbException("table: " + tableName);
    }

    /**
     * 分表路由获取实际的表名
     *
     * @param tableName 逻辑表明
     * @param dataMap   查询实体
     * @return 实际表名。没有找到，则报异常
     */
    private String getDevideTable(String tableName, NeoMap dataMap) {
        validate(tableName);
        String actTableName = devideStrategy.getTable(tableName, getDevideTableColumnValue(tableName, dataMap));
        if (null != actTableName) {
            return actTableName;
        } else if (defaultTableNameMap.containsKey(tableName)) {
            return defaultTableNameMap.get(tableName);
        }
        throw new NotFindDevideTableException(tableName);
    }

    /**
     * 分表路由获取实际的表名
     *
     * @param tableName 逻辑表明
     * @param object    查询实体
     * @return 实际表名。没有找到，则报异常
     */
    private String getDevideTable(String tableName, Object object) {
        validate(tableName);
        String tableNameFinal = devideStrategy.getTable(tableName, getDevideTableColumnValue(tableName, object));
        if (null != tableNameFinal) {
            return tableNameFinal;
        } else if (defaultTableNameMap.containsKey(tableName)) {
            return defaultTableNameMap.get(tableName);
        }
        throw new NotFindDevideTableException(tableName);
    }

    /**
     * 获取拆分后对应的db和该db对应的数据
     *
     * @param tableName 表名
     * @param dataList  数据集合
     * @param <T>       类型
     * @return 返回值：key为db对象，value为db对应的数据集
     */
    private <T> List<DevideDbBatch> getDevideDbAndData(String tableName, List<T> dataList) {
        List<DevideDbBatch> devideDbBatchList = new ArrayList<>();
        List<NeoMap> dataMapList = dataList.stream().map(d -> NeoMap.from(d, NeoMap.NamingChg.UNDERLINE)).collect(Collectors.toList());
        Map<Neo, Map<String, List<NeoMap>>> devideBatchDataMap = new ConcurrentHashMap<>(dataMapList.size());

        // 将数据归类
        for (NeoMap dataMap : dataMapList) {
            Neo db = getDevideDb(tableName, dataMap);
            String tableNameActual = getDevideTable(tableName, dataMap);
            devideBatchDataMap.compute(db, (k, valueMap) -> {
                if (null == valueMap) {
                    Map<String, List<NeoMap>> tableActDataMap = new ConcurrentHashMap<>(32);
                    doAddDataToTable(tableActDataMap, tableNameActual, dataMap);
                    return tableActDataMap;
                } else {
                    doAddDataToTable(valueMap, tableNameActual, dataMap);
                    return valueMap;
                }
            });
        }

        // 将数据转换为List结构
        devideBatchDataMap.forEach((key, value) -> {
            DevideDbBatch devideDbBatch = new DevideDbBatch();
            devideDbBatch.setDb(key);
            List<DevideTableBatch> batchList = value.entrySet().stream().map(e -> {
                DevideTableBatch devideTableBatch = new DevideTableBatch();
                devideTableBatch.setTableActName(e.getKey());
                devideTableBatch.setDataMapList(e.getValue());
                return devideTableBatch;
            }).collect(Collectors.toList());
            devideDbBatch.setDevideTableBatchList(batchList);
            devideDbBatchList.add(devideDbBatch);
        });

        return devideDbBatchList;
    }

    private void doAddDataToTable(Map<String, List<NeoMap>> tableActDataMap, String tableNameActual, NeoMap dataMap) {
        tableActDataMap.compute(tableNameActual, (t, datas) -> {
            if (null == datas) {
                List<NeoMap> resultDataMapList = new ArrayList<>();
                resultDataMapList.add(dataMap);
                return resultDataMapList;
            } else {
                datas.add(dataMap);
                return datas;
            }
        });
    }

    private Object getDevideTableColumnValue(String tableName, NeoMap dataMap) {
        if (devideTableInfoMap.containsKey(tableName)) {
            TableDevideConfig tableDevideConfig = devideTableInfoMap.get(tableName);
            if (null != tableDevideConfig) {
                return dataMap.get(tableDevideConfig.getColumnName());
            }
        }
        return null;
    }

    /**
     * 获取分表所需要的列的值
     *
     * @param tableName 逻辑表名
     * @param object    搜索对应的数据，number（主键值）或者普通数据搜索
     * @return 列值
     */
    private Object getDevideTableColumnValue(String tableName, Object object) {
        String tableNameFinal = tableName;
        // 分表
        if (devideTableInfoMap.containsKey(tableName)) {
            TableDevideConfig tableDevideConfig = devideTableInfoMap.get(tableName);
            String devideColumn = tableDevideConfig.getColumnName();

            // 主键
            if (object instanceof Number) {
                tableNameFinal = tableNameFinal + devideTableInfoMap.get(tableName).getMin();
                String primaryColumnName = dbList.get(0).getTable(tableNameFinal).getPrimary();

                // 判断主键是否与当前分表对应的列相同
                if (null != primaryColumnName && primaryColumnName.equals(devideColumn)) {
                    return object;
                }
            } else if (null != object) {
                return NeoMap.from(object).get(tableDevideConfig.getColumnName());
            }
        }
        return null;
    }

    private Object getDevideDbColumnValue(String tableName, NeoMap dataMap) {
        if (null != devideDbLoginTableName && devideDbLoginTableName.equals(tableName)) {
            return dataMap.get(devideDbColumnName);
        }
        return null;
    }

    /**
     * 获取分库所需要的列的值
     *
     * @param tableName 逻辑表名
     * @param object    搜索对应的数据，number（主键值）或者普通数据搜索
     * @return 列值
     */
    private Object getDevideDbColumnValue(String tableName, Object object) {
        if (null != devideDbLoginTableName && devideDbLoginTableName.equals(tableName)) {
            // 主键
            if (object instanceof Number) {
                String tableNameFinal = tableName;
                // 分表的话，则获取其中最小的表的结构
                if (devideTableInfoMap.containsKey(tableName)) {
                    tableNameFinal = tableNameFinal + devideTableInfoMap.get(tableName).getMin();
                }

                // 判断主键是否与当前分库对应的列相同
                String primaryColumnName = dbList.get(0).getTable(tableNameFinal).getPrimary();
                if (null != primaryColumnName && primaryColumnName.equals(devideDbColumnName)) {
                    return object;
                }
            } else if (null != object) {
                return NeoMap.from(object).get(devideDbColumnName);
            }
        }
        return null;
    }

    private void validate(String tableName) {
        if (null == tableName || "".equals(tableName)) {
            throw new NeoException("表名不可为空");
        }

        if (null == devideStrategy) {
            throw new NeoException("请先调用start函数设置分库分表策略");
        }
    }

    private List<Neo> getNeoList() {
        return dbList;
    }

    @Override
    public NeoMap insert(String tableName, NeoMap dataMap) {
        Neo neo = getDevideDb(tableName, dataMap);
        return neo.insert(getDevideTable(tableName, dataMap), dataMap);
    }

    @Override
    public <T> T insert(String tableName, T object) {
        Neo neo = getDevideDb(tableName, object);
        return neo.insert(getDevideTable(tableName, object), object);
    }

    @Override
    public Integer delete(String tableName, NeoMap searchMap) {
        Neo neo = getDevideDb(tableName, searchMap);
        if (null != neo) {
            return neo.delete(getDevideTable(tableName, searchMap), searchMap);
        } else {
            neoXa.run(() -> getNeoList().forEach(n -> n.delete(getDevideTable(tableName, searchMap), searchMap)));
            return getNeoList().size();
        }
    }

    @Override
    public <T> Integer delete(String tableName, T object) {
        Neo neo = getDevideDb(tableName, object);
        if (null != neo) {
            return neo.delete(getDevideTable(tableName, object), object);
        } else {
            neoXa.run(() -> getNeoList().forEach(n -> n.delete(getDevideTable(tableName, object), object)));
            return getNeoList().size();
        }
    }

    @Override
    public Integer delete(String tableName, Number id) {
        Neo neo = getDevideDb(tableName, id);
        if (null != neo) {
            return neo.delete(getDevideTable(tableName, id), id);
        } else {
            neoXa.run(() -> getNeoList().forEach(n -> n.delete(getDevideTable(tableName, id), id)));
            return getNeoList().size();
        }
    }

    @Override
    public NeoMap update(String tableName, NeoMap dataMap, NeoMap searchMap) {
        Neo neo = getDevideDb(tableName, searchMap);
        if (null != neo) {
            return neo.update(getDevideTable(tableName, searchMap), dataMap, searchMap);
        } else {
            neoXa.run(() -> getNeoList().forEach(n -> n.update(tableName, dataMap, searchMap)));
            return NeoMap.of();
        }
    }

    @Override
    public <T> T update(String tableName, T setEntity, NeoMap searchMap) {
        Neo neo = getDevideDb(tableName, searchMap);
        if (null != neo) {
            return neo.update(getDevideTable(tableName, searchMap), setEntity, searchMap);
        } else {
            neoXa.run(() -> getNeoList().forEach(n -> n.update(tableName, setEntity, searchMap)));
            return null;
        }
    }

    @Override
    public <T> T update(String tableName, T setEntity, T searchEntity) {
        Neo neo = getDevideDb(tableName, searchEntity);
        if (null != neo) {
            return neo.update(getDevideTable(tableName, searchEntity), setEntity, searchEntity);
        } else {
            neoXa.run(() -> getNeoList().forEach(n -> n.update(tableName, setEntity, searchEntity)));
            return null;
        }
    }

    @Override
    public <T> NeoMap update(String tableName, NeoMap setMap, T searchEntity) {
        Neo neo = getDevideDb(tableName, searchEntity);
        if (null != neo) {
            return neo.update(getDevideTable(tableName, searchEntity), setMap, searchEntity);
        } else {
            neoXa.run(() -> getNeoList().forEach(n -> n.update(tableName, setMap, searchEntity)));
            return NeoMap.of();
        }
    }

    @Override
    public NeoMap update(String tableName, NeoMap dataMap, Columns columns) {
        Neo neo = getDevideDb(tableName, dataMap.assign(columns));
        if (null != neo) {
            return neo.update(getDevideTable(tableName, dataMap.assign(columns)), dataMap, columns);
        } else {
            neoXa.run(() -> getNeoList().forEach(n -> n.update(tableName, dataMap, columns)));
            return NeoMap.of();
        }
    }

    @Override
    public <T> T update(String tableName, T entity, Columns columns) {
        Neo neo = getDevideDb(tableName, NeoMap.from(entity, columns, NeoMap.NamingChg.UNDERLINE));
        if (null != neo) {
            return neo.update(getDevideTable(tableName, NeoMap.from(entity, columns, NeoMap.NamingChg.UNDERLINE)), entity, columns);
        } else {
            neoXa.run(() -> getNeoList().forEach(n -> n.update(tableName, entity, columns)));
            return null;
        }
    }

    @Override
    public NeoMap update(String tableName, NeoMap dataMap) {
        Neo neo = getDevideDb(tableName, dataMap);
        if (null != neo) {
            return neo.update(getDevideTable(tableName, dataMap), dataMap);
        } else {
            neoXa.run(() -> getNeoList().forEach(n -> n.update(tableName, dataMap)));
            return NeoMap.of();
        }
    }

    @Override
    public <T> T update(String tableName, T entity) {
        Neo neo = getDevideDb(tableName, entity);
        if (null != neo) {
            return neo.update(getDevideTable(tableName, entity), entity);
        } else {
            neoXa.run(() -> getNeoList().forEach(n -> n.update(tableName, entity)));
            return null;
        }
    }

    @Override
    public NeoMap one(String tableName, Columns columns, NeoMap searchMap) {
        Neo neo = getDevideDb(tableName, searchMap);
        if (null != neo) {
            return neo.one(getDevideTable(tableName, searchMap), columns, searchMap);
        } else {
            List<Neo> neoList = getNeoList();
            for (Neo db : neoList) {
                NeoMap data = db.one(getDevideTable(tableName, searchMap), columns, searchMap);
                if (NeoMap.isUnEmpty(data)) {
                    return data;
                }
            }
            return NeoMap.of();
        }
    }

    @Override
    public <T> T one(String tableName, Columns columns, T entity) {
        Neo neo = getDevideDb(tableName, entity);
        if (null != neo) {
            return neo.one(getDevideTable(tableName, entity), columns, entity);
        } else {
            List<Neo> neoList = getNeoList();
            for (Neo db : neoList) {
                T data = db.one(getDevideTable(tableName, entity), columns, entity);
                if (null != data) {
                    return data;
                }
            }
            return null;
        }
    }

    @Override
    public NeoMap one(String tableName, Columns columns, Number key) {
        Neo neo = getDevideDb(tableName, key);
        if (null != neo) {
            return neo.one(getDevideTable(tableName, key), key);
        } else {
            List<Neo> neoList = getNeoList();
            for (Neo neoInner : neoList) {
                NeoMap data = neoInner.one(getDevideTable(tableName, key), columns, key);
                if (NeoMap.isUnEmpty(data)) {
                    return data;
                }
            }
        }
        return null;
    }

    @Override
    public NeoMap one(String tableName, NeoMap searchMap) {
        Neo neo = getDevideDb(tableName, searchMap);
        if (null != neo) {
            return neo.one(getDevideTable(tableName, searchMap), searchMap);
        } else {
            List<Neo> neoList = getNeoList();
            for (Neo db : neoList) {
                NeoMap data = db.one(getDevideTable(tableName, searchMap), searchMap);
                if (NeoMap.isUnEmpty(data)) {
                    return data;
                }
            }
            return null;
        }
    }

    @Override
    public <T> T one(String tableName, T entity) {
        Neo neo = getDevideDb(tableName, entity);
        if (null != neo) {
            return neo.one(getDevideTable(tableName, entity), entity);
        } else {
            List<Neo> neoList = getNeoList();
            for (Neo db : neoList) {
                T data = db.one(getDevideTable(tableName, entity), entity);
                if (null != data) {
                    return data;
                }
            }
            return null;
        }
    }

    @Override
    public NeoMap one(String tableName, Number key) {
        Neo neo = getDevideDb(tableName, key);
        if (null != neo) {
            return neo.one(getDevideTable(tableName, key), key);
        } else {
            List<Neo> neoList = getNeoList();
            for (Neo db : neoList) {
                NeoMap data = db.one(getDevideTable(tableName, key), key);
                if (NeoMap.isUnEmpty(data)) {
                    return data;
                }
            }
        }
        return null;
    }

    @Override
    public List<NeoMap> list(String tableName, Columns columns, NeoMap searchMap) {
        Neo neo = getDevideDb(tableName, searchMap);
        if (null != neo) {
            return neo.list(getDevideTable(tableName, searchMap), columns, searchMap);
        } else {
            return neoXa.call(() -> getNeoList().stream().flatMap(db -> db.list(getDevideTable(tableName, searchMap), columns, searchMap).stream()).collect(Collectors.toList()));
        }
    }

    @Override
    public <T> List<T> list(String tableName, Columns columns, T entity) {
        Neo neo = getDevideDb(tableName, entity);
        if (null != neo) {
            return neo.list(getDevideTable(tableName, entity), columns, entity);
        } else {
            return neoXa.call(() -> getNeoList().stream().flatMap(db -> db.list(getDevideTable(tableName, entity), columns, entity).stream()).collect(Collectors.toList()));
        }
    }

    @Override
    public List<NeoMap> list(String tableName, NeoMap searchMap) {
        Neo neo = getDevideDb(tableName, searchMap);
        if (null != neo) {
            return neo.list(getDevideTable(tableName, searchMap), searchMap);
        } else {
            return neoXa.call(() -> getNeoList().stream().flatMap(db -> db.list(getDevideTable(tableName, searchMap), searchMap).stream()).collect(Collectors.toList()));
        }
    }

    @Override
    public <T> List<T> list(String tableName, T entity) {
        Neo neo = getDevideDb(tableName, entity);
        if (null != neo) {
            return neo.list(getDevideTable(tableName, entity), entity);
        } else {
            return neoXa.call(() -> getNeoList().stream().flatMap(db -> db.list(getDevideTable(tableName, entity), entity).stream()).collect(Collectors.toList()));
        }
    }

    @Override
    public List<NeoMap> list(String tableName, Columns columns) {
        throw new NeoNotSupport("数据量太大，该api分库分表场景不支持");
    }

    @SuppressWarnings("all")
    @Override
    @Deprecated
    public <T> T value(String tableName, Class<T> tClass, String field, NeoMap searchMap) {
        Neo neo = getDevideDb(tableName, searchMap);
        if (null != neo) {
            return neo.value(getDevideTable(tableName, searchMap), tClass, field, searchMap);
        } else {
            List<Neo> neoList = getNeoList();
            for (Neo db : neoList) {
                T data = db.value(getDevideTable(tableName, searchMap), tClass, field, searchMap);
                if (null != data) {
                    return data;
                }
            }
            return null;
        }
    }

    @SuppressWarnings("all")
    @Override
    @Deprecated
    public <T> T value(String tableName, Class<T> tClass, String field, Object entity) {
        Neo neo = getDevideDb(tableName, entity);
        if (null != neo) {
            return neo.value(getDevideTable(tableName, entity), tClass, field, entity);
        } else {
            List<Neo> neoList = getNeoList();
            for (Neo db : neoList) {
                T data = db.value(getDevideTable(tableName, entity), tClass, field, entity);
                if (null != data) {
                    return data;
                }
            }
            return null;
        }
    }

    @SuppressWarnings("all")
    @Override
    public <T> T value(Class<T> tClass, String tableName, String field, NeoMap searchMap) {
        Neo neo = getDevideDb(tableName, searchMap);
        if (null != neo) {
            return neo.value(tClass, getDevideTable(tableName, searchMap), field, searchMap);
        } else {
            List<Neo> neoList = getNeoList();
            for (Neo db : neoList) {
                T data = db.value(tClass, getDevideTable(tableName, searchMap), field, searchMap);
                if (null != data) {
                    return data;
                }
            }
            return null;
        }
    }

    @SuppressWarnings("all")
    @Override
    public <T> T value(Class<T> tClass, String tableName, String field, Object entity) {
        Neo neo = getDevideDb(tableName, entity);
        if (null != neo) {
            return neo.value(tClass, getDevideTable(tableName, entity), field, entity);
        } else {
            List<Neo> neoList = getNeoList();
            for (Neo db : neoList) {
                T data = db.value(tClass, getDevideTable(tableName, entity), field, entity);
                if (null != data) {
                    return data;
                }
            }
            return null;
        }
    }

    @SuppressWarnings("all")
    @Override
    public String value(String tableName, String field, NeoMap searchMap) {
        Neo neo = getDevideDb(tableName, searchMap);
        if (null != neo) {
            return neo.value(getDevideTable(tableName, searchMap), field, searchMap);
        } else {
            List<Neo> neoList = getNeoList();
            for (Neo db : neoList) {
                String data = db.value(getDevideTable(tableName, searchMap), field, searchMap);
                if (null != data) {
                    return data;
                }
            }
            return null;
        }
    }

    @SuppressWarnings("all")
    @Override
    public String value(String tableName, String field, Object entity) {
        Neo neo = getDevideDb(tableName, entity);
        if (null != neo) {
            return neo.value(getDevideTable(tableName, entity), field, entity);
        } else {
            List<Neo> neoList = getNeoList();
            for (Neo db : neoList) {
                String data = db.value(getDevideTable(tableName, entity), field, entity);
                if (null != data) {
                    return data;
                }
            }
            return null;
        }
    }

    @Override
    public String value(String tableName, String field, Number id) {
        Neo neo = getDevideDb(tableName, id);
        if (null != neo) {
            return neo.value(getDevideTable(tableName, id), field, id);
        } else {
            List<Neo> neoList = getNeoList();
            for (Neo db : neoList) {
                String data = db.value(getDevideTable(tableName, id), field, id);
                if (null != data) {
                    return data;
                }
            }
        }
        return null;
    }

    @Override
    @Deprecated
    public <T> List<T> values(String tableName, Class<T> tClass, String field, NeoMap searchMap) {
        Neo neo = getDevideDb(tableName, searchMap);
        if (null != neo) {
            return neo.values(getDevideTable(tableName, searchMap), tClass, field, searchMap);
        } else {
            return neoXa.call(
                () -> getNeoList().stream().flatMap(db -> db.values(getDevideTable(tableName, searchMap), tClass, field, searchMap).stream()).collect(Collectors.toList()));
        }
    }

    @Override
    @Deprecated
    public <T> List<T> values(String tableName, Class<T> tClass, String field, Object entity) {
        Neo neo = getDevideDb(tableName, entity);
        if (null != neo) {
            return neo.values(getDevideTable(tableName, entity), tClass, field, entity);
        } else {
            return neoXa.call(() -> getNeoList().stream().flatMap(db -> db.values(getDevideTable(tableName, entity), tClass, field, entity).stream()).collect(Collectors.toList()));
        }
    }

    @Override
    public <T> List<T> values(Class<T> tClass, String tableName, String field, NeoMap searchMap) {
        Neo neo = getDevideDb(tableName, searchMap);
        if (null != neo) {
            return neo.values(tClass, getDevideTable(tableName, searchMap), field, searchMap);
        } else {
            return neoXa.call(
                () -> getNeoList().stream().flatMap(db -> db.values(tClass, getDevideTable(tableName, searchMap), field, searchMap).stream()).collect(Collectors.toList()));
        }
    }

    @Override
    public <T> List<T> values(Class<T> tClass, String tableName, String field, Object entity) {
        Neo neo = getDevideDb(tableName, entity);
        if (null != neo) {
            return neo.values(tClass, getDevideTable(tableName, entity), field, entity);
        } else {
            return neoXa.call(() -> getNeoList().stream().flatMap(db -> db.values(tClass, getDevideTable(tableName, entity), field, entity).stream()).collect(Collectors.toList()));
        }
    }

    @Override
    public List<String> values(String tableName, String field, NeoMap searchMap) {
        Neo neo = getDevideDb(tableName, searchMap);
        if (null != neo) {
            return neo.values(getDevideTable(tableName, searchMap), field, searchMap);
        } else {
            return neoXa.call(() -> getNeoList().stream().flatMap(db -> db.values(getDevideTable(tableName, searchMap), field, searchMap).stream()).collect(Collectors.toList()));
        }
    }

    @Override
    public List<String> values(String tableName, String field, Object entity) {
        Neo neo = getDevideDb(tableName, entity);
        if (null != neo) {
            return neo.values(getDevideTable(tableName, entity), field, entity);
        } else {
            return neoXa.call(() -> getNeoList().stream().flatMap(db -> db.values(getDevideTable(tableName, entity), field, entity).stream()).collect(Collectors.toList()));
        }
    }

    @Override
    public List<String> values(String tableName, String field) {
        throw new NeoNotSupport("数据量太大，该api分库分表场景不支持");
    }

    @Override
    public List<NeoMap> page(String tableName, Columns columns, NeoMap searchMap, NeoPage page) {
        Neo neo = getDevideDb(tableName, searchMap);
        if (null != neo) {
            return neo.page(getDevideTable(tableName, searchMap), columns, searchMap, page);
        } else {
            NeoPage aggregate = NeoPage.of(0, page.getStartIndex() + page.getPageSize());
            List<NeoMap> allDataList = getNeoList().stream()
                .flatMap(db -> db.page(getDevideTable(tableName, searchMap), columns, searchMap, aggregate).stream())
                .collect(Collectors.toList());
            Integer startIndex = page.getStartIndex();
            Integer pageSize = page.getPageSize();
            return allDataList.subList(startIndex, startIndex + pageSize);
        }
    }

    @Override
    public <T> List<T> page(String tableName, Columns columns, T entity, NeoPage page) {
        Neo neo = getDevideDb(tableName, entity);
        if (null != neo) {
            return neo.page(getDevideTable(tableName, entity), columns, entity, page);
        } else {
            NeoPage aggregate = NeoPage.of(0, page.getStartIndex() + page.getPageSize());
            List<T> allDataList = getNeoList().stream().flatMap(db -> db.page(getDevideTable(tableName, entity), columns, entity, aggregate).stream()).collect(Collectors.toList());
            Integer startIndex = page.getStartIndex();
            Integer pageSize = page.getPageSize();
            return allDataList.subList(startIndex, startIndex + pageSize);
        }
    }

    @SuppressWarnings("all")
    @Override
    public List<NeoMap> page(String tableName, NeoMap searchMap, NeoPage page) {
        Neo neo = getDevideDb(tableName, searchMap);
        if (null != neo) {
            return neo.page(getDevideTable(tableName, searchMap), searchMap, page);
        } else {
            NeoPage aggregate = NeoPage.of(0, page.getStartIndex() + page.getPageSize());
            List<NeoMap> allDataList = getNeoList().stream()
                .flatMap(db -> db.page(getDevideTable(tableName, searchMap), searchMap, aggregate).stream())
                .collect(Collectors.toList());
            Integer startIndex = page.getStartIndex();
            Integer pageSize = page.getPageSize();
            return allDataList.subList(startIndex, startIndex + pageSize);
        }
    }

    @Override
    public <T> List<T> page(String tableName, T entity, NeoPage page) {
        Neo neo = getDevideDb(tableName, entity);
        if (null != neo) {
            return neo.page(getDevideTable(tableName, entity), entity, page);
        } else {
            NeoPage aggregate = NeoPage.of(0, page.getStartIndex() + page.getPageSize());
            List<T> allDataList = getNeoList().stream().flatMap(db -> db.page(getDevideTable(tableName, entity), entity, aggregate).stream()).collect(Collectors.toList());
            Integer startIndex = page.getStartIndex();
            Integer pageSize = page.getPageSize();
            return allDataList.subList(startIndex, startIndex + pageSize);
        }
    }

    @Override
    public List<NeoMap> page(String tableName, Columns columns, NeoPage page) {
        throw new NeoNotSupport("数据量太大，该api分库分表场景不支持");
    }

    @Override
    public List<NeoMap> page(String tableName, NeoPage page) {
        throw new NeoNotSupport("数据量太大，该api分库分表场景不支持");
    }

    @Override
    public Integer count(String tableName, NeoMap searchMap) {
        Neo neo = getDevideDb(tableName, searchMap);
        if (null != neo) {
            return neo.count(getDevideTable(tableName, searchMap), searchMap);
        } else {
            return neoXa.call(() -> getNeoList().stream().map(db -> db.count(tableName, searchMap)).reduce((a, b) -> a + b).orElse(0));
        }
    }

    @Override
    public Integer count(String tableName, Object entity) {
        Neo neo = getDevideDb(tableName, entity);
        if (null != neo) {
            return neo.count(getDevideTable(tableName, entity), entity);
        } else {
            return neoXa.call(() -> getNeoList().stream().map(db -> db.count(getDevideTable(tableName, entity), entity)).reduce((a, b) -> a + b).orElse(0));
        }
    }

    @Override
    public Integer count(String tableName) {
        return neoXa.call(() -> getNeoList().stream().map(db -> db.count(tableName)).reduce((a, b) -> a + b).orElse(0));
    }


    @Override
    public Integer batchInsert(String tableName, List<NeoMap> dataMapList) {
        List<DevideDbBatch> dbAndDataList = getDevideDbAndData(tableName, dataMapList);
        Integer result = 0;
        for (DevideDbBatch devideData : dbAndDataList) {
            List<DevideTableBatch> tableBatchList = devideData.getDevideTableBatchList();
            if (!tableBatchList.isEmpty()) {
                result += neoXa.call(() -> {
                    Integer count = 0;
                    for (DevideTableBatch tableBatch : tableBatchList) {
                        count += devideData.getDb().batchInsert(tableBatch.getTableActName(), tableBatch.getDataMapList());
                    }
                    return count;
                });
            }
        }
        return result;
    }

    @Override
    public <T> Integer batchInsertEntity(String tableName, List<T> dataList) {
        List<NeoMap> dataMapList = dataList.stream().map(d -> NeoMap.from(d, NeoMap.NamingChg.UNDERLINE)).collect(Collectors.toList());
        return batchInsert(tableName, dataMapList);
    }

    @Override
    public Integer batchUpdate(String tableName, List<NeoMap> dataList) {
        List<DevideDbBatch> dbAndDataList = getDevideDbAndData(tableName, dataList);
        Integer result = 0;
        for (DevideDbBatch devideData : dbAndDataList) {
            List<DevideTableBatch> tableBatchList = devideData.getDevideTableBatchList();
            if (!tableBatchList.isEmpty()) {
                result += neoXa.call(() -> {
                    Integer count = 0;
                    for (DevideTableBatch tableBatch : tableBatchList) {
                        count += devideData.getDb().batchUpdate(tableBatch.getTableActName(), tableBatch.getDataMapList());
                    }
                    return count;
                });
            }
        }
        return result;
    }

    @Override
    public Integer batchUpdate(String tableName, List<NeoMap> dataList, Columns columns) {
        List<DevideDbBatch> dbAndDataList = getDevideDbAndData(tableName, dataList);
        Integer result = 0;
        for (DevideDbBatch devideData : dbAndDataList) {
            List<DevideTableBatch> tableBatchList = devideData.getDevideTableBatchList();
            if (!tableBatchList.isEmpty()) {
                result += neoXa.call(() -> {
                    Integer count = 0;
                    for (DevideTableBatch tableBatch : tableBatchList) {
                        count += devideData.getDb().batchUpdate(tableBatch.getTableActName(), tableBatch.getDataMapList(), columns);
                    }
                    return count;
                });
            }
        }
        return result;
    }

    @Override
    public <T> Integer batchUpdateEntity(String tableName, List<T> dataList) {
        List<NeoMap> dataMapList = dataList.stream().map(d -> NeoMap.from(d, NeoMap.NamingChg.UNDERLINE)).collect(Collectors.toList());
        return batchUpdate(tableName, dataMapList);
    }

    @Override
    public <T> Integer batchUpdateEntity(String tableName, List<T> dataList, Columns columns) {
        List<NeoMap> dataMapList = dataList.stream().map(d -> NeoMap.from(d, NeoMap.NamingChg.UNDERLINE)).collect(Collectors.toList());
        return batchUpdate(tableName, dataMapList, columns);
    }

    @Data
    private static class DevideDbBatch {

        private Neo db;
        private List<DevideTableBatch> devideTableBatchList;
    }

    @Data
    private static class DevideTableBatch {

        /**
         * 分表后的实际名字，若分表则为分表后的名字，否则为表原名
         */
        private String tableActName;
        private List<NeoMap> dataMapList;
    }
}
