package com.simonalong.neo;

import com.simonalong.neo.core.AbstractBaseDb;
import com.simonalong.neo.db.NeoPage;
import com.simonalong.neo.exception.NeoException;
import com.simonalong.neo.exception.NotFindDevideDbException;
import com.simonalong.neo.util.ObjectUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 分库管理对象
 *
 * @author zhouzhenyong
 * @since 2019/6/21 下午3:48
 */
@NoArgsConstructor
public final class NeoDevide extends AbstractBaseDb {

    /**
     * 分库的对应的列名
     */
    private String dbColumnName;
    /**
     * 带分库的数据集合
     */
    private List<Neo> dbList = new ArrayList<>();
    /**
     * 表的哈希处理映射, key：表名，value表的哈希信息
     */
    private Map<String, TableHashInfo> tableHashInfoMap = new ConcurrentHashMap<>();

    public void setDevideDb(List<Neo> neoList, String columnName) {
        this.dbColumnName = columnName;
        this.dbList = neoList;
    }

    public void setDevideTable(String devideTables, String columnName) {
        if (null == devideTables || "".equals(devideTables)) {
            throw new NeoException("数据配置为空");
        }
        if (null == columnName || "".equals(columnName)) {
            throw new NeoException("数据配置为空");
        }

        String regex = "^(.*)\\{(\\d),.*(\\d)}$";
        Matcher matcher = Pattern.compile(regex).matcher(devideTables);
        if (matcher.find()) {
            TableHashInfo tableHashInfo = new TableHashInfo();
            String tableName = matcher.group(1);
            Integer min = Integer.valueOf(matcher.group(2));
            Integer max = Integer.valueOf(matcher.group(3));
            if (min >= max) {
                throw new NeoException("数据配置错误: 最大值不能小于最小值");
            }
            tableHashInfo.setTableName(tableName);
            tableHashInfo.setColumnName(columnName);
            tableHashInfo.setMin(min);
            tableHashInfo.setSize(max - min);

            tableHashInfoMap.putIfAbsent(tableName, tableHashInfo);
        } else {
            throw new NeoException("没有发现要分库的数据");
        }
    }

    @Data
    private static class TableHashInfo {

        /**
         * 表名
         */
        private String tableName;
        /**
         * 分表的列名
         */
        private String columnName;
        private Integer min;
        private Integer size;
    }

//    /**
//     * 库名以及对应的db的名字，比如：key：xxx_1, value：哈希值：1， Neo
//     */
//    private Map<String, Pair<Integer, Neo>> devideDbMap = new ConcurrentHashMap<>(8);
//    /**
//     * 表名以及对应的分表的名字，比如：key：xxx1，value：哈希值：1， xxx
//     */
//    private Map<String, TableHashInfo> devideTableMap = new ConcurrentHashMap<>(8);
//    /**
//     * 分库的字段表名和字段
//     */
//    private Map<String, String> devideDbParameterMap = new ConcurrentHashMap<>(8);
//    /**
//     * 分表的字段表名和字段
//     */
//    private Map<String, String> devideTableParameterMap = new ConcurrentHashMap<>(8);
//    /**
//     * 分库的最小值
//     */
//    private Integer min = 0;
//    private Integer devideSize = 0;

    /**
     * 设置分库
     * <p>
     * <p>
     * 最后得到的库名，举例比如：db0, db1, ... db11
     *
     * @param devideDbNames 分库的db分库表达式，{0, 12}作为后缀。比如：xxx{0, 12}
     * @param neoList       db的列表
     */
    public void setDevideDb(String devideDbNames, List<Neo> neoList) {
        String regex = "^(.*)\\{(\\d),.*(\\d)}$";
        Matcher matcher = Pattern.compile(regex).matcher(devideDbNames);
        if (matcher.find()) {
            dbName = matcher.group(1);
            Integer min = Integer.valueOf(matcher.group(2));
            Integer max = Integer.valueOf(matcher.group(3));
            if (min >= max) {
                throw new NeoException("数据配置错误: 最大值不能小于最小值");
            }
            this.min = min;
            this.devideSize = max - min;
            for (Integer index = min, indexJ = 0; index < max; index++, indexJ++) {
                devideDbMap.putIfAbsent(dbName + index, new Pair<>(indexJ, neoList.get(indexJ)));
            }
        } else {
            throw new NeoException("没有发现要分库的数据");
        }
    }

    /**
     * 设置分表
     * <p>
     * <p>
     * 最后得到的表名，举例比如：xxx0, xxx1, ... xxx11
     *
     * @param devideTableNames 分表的表达式，{0, 12}作为后缀。比如：xxx{0, 12}
     * @param columnName       分表的列名
     */
    public void addDevideTable(String devideTableNames, String columnName) {
        String regex = "^(.*)\\{(\\d),.*(\\d)}$";
        Matcher matcher = Pattern.compile(regex).matcher(devideTableNames);
        if (matcher.find()) {
            String tableName = matcher.group(1);
            Integer min = Integer.valueOf(matcher.group(2));
            Integer max = Integer.valueOf(matcher.group(3));
            if (min >= max) {
                throw new NeoException("数据配置错误: 最大值不能小于最小值");
            }

            Integer size = max - min;
            for (Integer index = min, indexJ = 0; index < max; index++, indexJ++) {
                Integer finalIndexJ = indexJ;
                Integer finalIndex = index;
                devideTableMap.compute(tableName, (k, v) -> {
                    if (null == v) {
                        TableHashInfo tableHashInfo = new TableHashInfo();

                        List<TableHashMeta> innerHashTableList = new ArrayList<>();
                        innerHashTableList.add(new TableHashMeta(finalIndexJ, tableName + finalIndex));
                        tableHashInfo.setMin(min);
                        tableHashInfo.setSize(size);
                        tableHashInfo.setInnerHashTableList(innerHashTableList);
                        return tableHashInfo;
                    } else {
                        List<TableHashMeta> innerHashTableList = v.getInnerHashTableList();
                        innerHashTableList.add(new TableHashMeta(finalIndexJ, tableName + finalIndex));
                        v.setInnerHashTableList(innerHashTableList);
                        return v;
                    }
                });
            }

            devideTableParameterMap.putIfAbsent(tableName, columnName);
        } else {
            throw new NeoException("没有发现要分表的数据");
        }
    }

    /**
     * 添加分库的参数
     *
     * @param tableName  表名
     * @param columnName 列名
     */
    public void addDbDevideParameter(String tableName, String columnName) {
        devideDbParameterMap.putIfAbsent(tableName, columnName);
    }

    private Neo getDevideDb(String tableName, NeoMap dataMap) {
        return doGetDevideDb(getDevideDbColumnValue(tableName, dataMap));
    }

    private Neo getDevideDb(String tableName, Object object) {
        return doGetDevideDb(getDevideDbColumnValue(tableName, object));
    }

    private String getDevideTable(String tableName, NeoMap dataMap) {
        return doGetDevideTable(tableName, getDevideTableColumnValue(tableName, dataMap));
    }

    private String getDevideTable(String tableName, Object object) {
        return doGetDevideTable(tableName, getDevideTableColumnValue(tableName, object));
    }

    /**
     * 根据对应字段的值获取分库对应的DB
     *
     * @param value 分库字段对应的值
     * @return 分库db
     */
    private Neo doGetDevideDb(Object value) {
        Number index = ObjectUtil.cast(Number.class, value);
        if (null == index) {
            return null;
        }

        String tableDevideStr = dbName + ((index.intValue() % devideSize) + min);
        if (devideDbMap.containsKey(tableDevideStr)) {
            return devideDbMap.get(tableDevideStr).getValue();
        } else {
            throw new NeoException("没有找到对应的分库");
        }
    }

    private String doGetDevideTable(String tableName, Object value) {
        Number index = ObjectUtil.cast(Number.class, value);
        if (null == index) {
            return tableName;
        }

        TableHashInfo tableHashInfo = devideTableMap.get(tableName);
        List<TableHashMeta> innerHashTableList = tableHashInfo.getInnerHashTableList();
        for (TableHashMeta hashMeta : innerHashTableList) {
            if (index.equals(hashMeta.getIndex())) {
                return hashMeta.getTableNameWithIndex();
            }
        }
        throw new NeoException("没有找到对应的分表");
    }

    private Object getDevideTableColumnValue(String tableName, NeoMap dataMap) {
        if (devideTableParameterMap.containsKey(tableName)) {
            return dataMap.get(devideTableParameterMap.get(tableName));
        }
        return null;
    }

    private Object getDevideTableColumnValue(String tableName, Object object) {
        if (devideTableParameterMap.containsKey(tableName)) {
            if (null != object) {
                return NeoMap.from(object).get(devideTableParameterMap.get(tableName));
            }
        }
        return null;
    }

    private Object getDevideDbColumnValue(String tableName, NeoMap dataMap) {
        if (devideDbParameterMap.containsKey(tableName)) {
            return dataMap.get(devideDbParameterMap.get(tableName));
        }
        return null;
    }

    private Object getDevideDbColumnValue(String tableName, Object object) {
        if (devideDbParameterMap.containsKey(tableName)) {
            if (null != object) {
                return NeoMap.from(object).get(devideDbParameterMap.get(tableName));
            }
        }
        return null;
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

//        // 非分库的表，则返回所有的数据
//        if (!devideDbParameterMap.containsKey(tableName)) {
            return devideDbMap.values().stream().map(m -> new Pair<>(m.getValue(), dataMapList)).collect(Collectors.toList());
//        } else {
//            Collection<Pair<Integer, Neo>> indexAndNeoCollection = devideDbMap.values();
//            for (Pair<Integer, Neo> indexNeoPair : indexAndNeoCollection) {
//                mapList.add(new Pair<>(indexNeoPair.getValue(), getDevideMapList(tableName, indexNeoPair.getKey(), dataMapList)));
//            }
//        }

        if (devideDbParameterMap.containsKey(tableName)) {
            DevideDbBatch devideDbBatch = new DevideDbBatch();
            devideDbBatch.setDb();
            devideDbBatch.setDevideTableBatchList();
            for (NeoMap dataMap : dataMapList) {

            }
        } else {
            throw new NeoException("没有找到对应的分库");
        }

        return devideDbBatchList;
    }

    /**
     * 根据哈希值获取该db负责的数据
     *
     * @param index    哈希索引
     * @param dataList 数据
     * @return 该哈希对应的数据
     */
    private List<NeoMap> getDevideMapList(String tableName, Integer index, List<NeoMap> dataList) {
        List<NeoMap> resultList = new ArrayList<>();
        for (NeoMap neoMap : dataList) {
            if (devideDbParameterMap.containsKey(tableName)) {
                String columnName = devideDbParameterMap.get(tableName);
                if (neoMap.containsKey(columnName)) {
                    Integer value = ObjectUtil.cast(Integer.class, neoMap.get(columnName));
                    if (null != value && value.equals(index)) {
                        resultList.add(neoMap);
                    }
                }
            } else {
                resultList.add(neoMap);
            }
        }
        return resultList;
    }

    private List<Neo> getNeoList() {
        return devideDbMap.values().stream().map(Pair::getValue).collect(Collectors.toList());
    }

    @Override
    public NeoMap insert(String tableName, NeoMap dataMap) {
        Neo neo = getDevideDb(tableName, dataMap);
        if (null != neo) {
            return neo.insert(getDevideTable(tableName, dataMap), dataMap);
        } else {
            throw new NotFindDevideDbException("table: " + tableName + ", columns: " + dataMap);
        }
    }

    @Override
    public <T> T insert(String tableName, T object) {
        Neo neo = getDevideDb(tableName, object);
        if (null != neo) {
            return neo.insert(getDevideTable(tableName, object), object);
        } else {
            throw new NotFindDevideDbException("table: " + tableName + ", columns: " + object);
        }
    }

    @Override
    public Integer delete(String tableName, NeoMap searchMap) {
        Neo neo = getDevideDb(tableName, searchMap);
        if (null != neo) {
            return neo.delete(getDevideTable(tableName, searchMap), searchMap);
        } else {
            getNeoList().forEach(n -> n.delete(tableName, searchMap));
            return getNeoList().size();
        }
    }

    @Override
    public <T> Integer delete(String tableName, T object) {
        Neo neo = getDevideDb(tableName, object);
        if (null != neo) {
            return neo.delete(getDevideTable(tableName, object), object);
        } else {
            getNeoList().forEach(n -> n.delete(tableName, object));
            return getNeoList().size();
        }
    }

    @Override
    public Integer delete(String tableName, Number id) {
        getNeoList().forEach(n -> n.delete(tableName, id));
        return getNeoList().size();
    }

    @Override
    public NeoMap update(String tableName, NeoMap dataMap, NeoMap searchMap) {
        Neo neo = getDevideDb(tableName, searchMap);
        if (null != neo) {
            return neo.update(getDevideTable(tableName, searchMap), dataMap, searchMap);
        } else {
            getNeoList().forEach(n -> n.update(tableName, dataMap, searchMap));
            return NeoMap.of();
        }
    }

    @Override
    public <T> T update(String tableName, T setEntity, NeoMap searchMap) {
        Neo neo = getDevideDb(tableName, searchMap);
        if (null != neo) {
            return neo.update(getDevideTable(tableName, searchMap), setEntity, searchMap);
        } else {
            getNeoList().forEach(n -> n.update(tableName, setEntity, searchMap));
            return null;
        }
    }

    @Override
    public <T> T update(String tableName, T setEntity, T searchEntity) {
        Neo neo = getDevideDb(tableName, searchEntity);
        if (null != neo) {
            return neo.update(getDevideTable(tableName, searchEntity), setEntity, searchEntity);
        } else {
            getNeoList().forEach(n -> n.update(tableName, setEntity, searchEntity));
            return null;
        }
    }

    @Override
    public <T> NeoMap update(String tableName, NeoMap setMap, T searchEntity) {
        Neo neo = getDevideDb(tableName, searchEntity);
        if (null != neo) {
            return neo.update(getDevideTable(tableName, searchEntity), setMap, searchEntity);
        } else {
            getNeoList().forEach(n -> n.update(tableName, setMap, searchEntity));
            return NeoMap.of();
        }
    }

    @Override
    public NeoMap update(String tableName, NeoMap dataMap, Columns columns) {
        Neo neo = getDevideDb(tableName, dataMap.assign(columns));
        if (null != neo) {
            return neo.update(getDevideTable(tableName, dataMap.assign(columns)), dataMap, columns);
        } else {
            getNeoList().forEach(n -> n.update(tableName, dataMap, columns));
            return NeoMap.of();
        }
    }

    @Override
    public <T> T update(String tableName, T entity, Columns columns) {
        Neo neo = getDevideDb(tableName, NeoMap.from(entity, columns, NeoMap.NamingChg.UNDERLINE));
        if (null != neo) {
            return neo.update(getDevideTable(tableName, NeoMap.from(entity, columns, NeoMap.NamingChg.UNDERLINE)), entity, columns);
        } else {
            getNeoList().forEach(n -> n.update(tableName, entity, columns));
            return null;
        }
    }

    @Override
    public NeoMap update(String tableName, NeoMap dataMap) {
        Neo neo = getDevideDb(tableName, dataMap);
        if (null != neo) {
            return neo.update(getDevideTable(tableName, dataMap), dataMap);
        } else {
            getNeoList().forEach(n -> n.update(tableName, dataMap));
            return NeoMap.of();
        }
    }

    @Override
    public <T> T update(String tableName, T entity) {
        Neo neo = getDevideDb(tableName, entity);
        if (null != neo) {
            return neo.update(getDevideTable(tableName, entity), entity);
        } else {
            getNeoList().forEach(n -> n.update(tableName, entity));
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
                NeoMap data = db.one(tableName, columns, searchMap);
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
                T data = db.one(tableName, columns, entity);
                if (null != data) {
                    return data;
                }
            }
            return null;
        }
    }

    @Override
    public NeoMap one(String tableName, Columns columns, Number key) {
        List<Neo> neoList = getNeoList();
        for (Neo neo : neoList) {
            NeoMap data = neo.one(tableName, columns, key);
            if (NeoMap.isUnEmpty(data)) {
                return data;
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
                NeoMap data = db.one(tableName, searchMap);
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
                T data = db.one(tableName, entity);
                if (null != data) {
                    return data;
                }
            }
            return null;
        }
    }

    @Override
    public NeoMap one(String tableName, Number id) {
        List<Neo> neoList = getNeoList();
        for (Neo neo : neoList) {
            NeoMap data = neo.one(tableName, id);
            if (NeoMap.isUnEmpty(data)) {
                return data;
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
            return getNeoList().stream().flatMap(db -> db.list(tableName, columns, searchMap).stream()).collect(Collectors.toList());
        }
    }

    @Override
    public <T> List<T> list(String tableName, Columns columns, T entity) {
        Neo neo = getDevideDb(tableName, entity);
        if (null != neo) {
            return neo.list(getDevideTable(tableName, entity), columns, entity);
        } else {
            return getNeoList().stream().flatMap(db -> db.list(tableName, columns, entity).stream()).collect(Collectors.toList());
        }
    }

    @Override
    public List<NeoMap> list(String tableName, NeoMap searchMap) {
        Neo neo = getDevideDb(tableName, searchMap);
        if (null != neo) {
            return neo.list(getDevideTable(tableName, searchMap), searchMap);
        } else {
            return getNeoList().stream().flatMap(db -> db.list(tableName, searchMap).stream()).collect(Collectors.toList());
        }
    }

    @Override
    public <T> List<T> list(String tableName, T entity) {
        Neo neo = getDevideDb(tableName, entity);
        if (null != neo) {
            return neo.list(getDevideTable(tableName, entity), entity);
        } else {
            return getNeoList().stream().flatMap(db -> db.list(tableName, entity).stream()).collect(Collectors.toList());
        }
    }

    @Override
    public List<NeoMap> list(String tableName, Columns columns) {
        return getNeoList().stream().flatMap(db -> db.list(tableName, columns).stream()).collect(Collectors.toList());
    }

    @SuppressWarnings("all")
    @Override
    public <T> T value(String tableName, Class<T> tClass, String field, NeoMap searchMap) {
        Neo neo = getDevideDb(tableName, searchMap);
        if (null != neo) {
            return neo.value(getDevideTable(tableName, searchMap), tClass, field, searchMap);
        } else {
            List<Neo> neoList = getNeoList();
            for (Neo db : neoList) {
                T data = db.value(tableName, tClass, field, searchMap);
                if (null != data) {
                    return data;
                }
            }
            return null;
        }
    }

    @SuppressWarnings("all")
    @Override
    public <T> T value(String tableName, Class<T> tClass, String field, Object entity) {
        Neo neo = getDevideDb(tableName, entity);
        if (null != neo) {
            return neo.value(getDevideTable(tableName, entity), tClass, field, entity);
        } else {
            List<Neo> neoList = getNeoList();
            for (Neo db : neoList) {
                T data = db.value(tableName, tClass, field, entity);
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
                String data = db.value(tableName, field, searchMap);
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
                String data = db.value(tableName, field, entity);
                if (null != data) {
                    return data;
                }
            }
            return null;
        }
    }

    @Override
    public String value(String tableName, String field, Number id) {
        List<Neo> neoList = getNeoList();
        for (Neo neo : neoList) {
            String data = neo.value(tableName, field, id);
            if (null != data) {
                return data;
            }
        }
        return null;
    }

    @Override
    public <T> List<T> values(String tableName, Class<T> tClass, String field, NeoMap searchMap) {
        Neo neo = getDevideDb(tableName, searchMap);
        if (null != neo) {
            return neo.values(getDevideTable(tableName, searchMap), tClass, field, searchMap);
        } else {
            return getNeoList().stream().flatMap(db -> db.values(tableName, tClass, field, searchMap).stream()).collect(Collectors.toList());
        }
    }

    @Override
    public <T> List<T> values(String tableName, Class<T> tClass, String field, Object entity) {
        Neo neo = getDevideDb(tableName, entity);
        if (null != neo) {
            return neo.values(getDevideTable(tableName, entity), tClass, field, entity);
        } else {
            return getNeoList().stream().flatMap(db -> db.values(tableName, tClass, field, entity).stream()).collect(Collectors.toList());
        }
    }

    @Override
    public List<String> values(String tableName, String field, NeoMap searchMap) {
        Neo neo = getDevideDb(tableName, searchMap);
        if (null != neo) {
            return neo.values(getDevideTable(tableName, searchMap), field, searchMap);
        } else {
            return getNeoList().stream().flatMap(db -> db.values(tableName, field, searchMap).stream()).collect(Collectors.toList());
        }
    }

    @Override
    public List<String> values(String tableName, String field, Object entity) {
        Neo neo = getDevideDb(tableName, entity);
        if (null != neo) {
            return neo.values(getDevideTable(tableName, entity), field, entity);
        } else {
            return getNeoList().stream().flatMap(db -> db.values(tableName, field, entity).stream()).collect(Collectors.toList());
        }
    }

    @Override
    public List<String> values(String tableName, String field) {
        return getNeoList().stream().flatMap(db -> db.list(tableName, field).stream()).collect(Collectors.toList());
    }

    @Override
    public List<NeoMap> page(String tableName, Columns columns, NeoMap searchMap, NeoPage page) {
        Neo neo = getDevideDb(tableName, searchMap);
        if (null != neo) {
            return neo.page(getDevideTable(tableName, searchMap), columns, searchMap, page);
        } else {
            NeoPage aggregate = NeoPage.of(0, page.getStartIndex() + page.getPageSize());
            List<NeoMap> allDataList = getNeoList().stream().flatMap(db -> db.page(tableName, columns, searchMap, aggregate).stream()).collect(Collectors.toList());
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
            List<T> allDataList = getNeoList().stream().flatMap(db -> db.page(tableName, columns, entity, aggregate).stream()).collect(Collectors.toList());
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
            List<NeoMap> allDataList = getNeoList().stream().flatMap(db -> db.page(tableName, searchMap, aggregate).stream()).collect(Collectors.toList());
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
            List<T> allDataList = getNeoList().stream().flatMap(db -> db.page(tableName, entity, aggregate).stream()).collect(Collectors.toList());
            Integer startIndex = page.getStartIndex();
            Integer pageSize = page.getPageSize();
            return allDataList.subList(startIndex, startIndex + pageSize);
        }
    }

    @SuppressWarnings("all")
    @Override
    public List<NeoMap> page(String tableName, Columns columns, NeoPage page) {
        NeoPage aggregate = NeoPage.of(0, page.getStartIndex() + page.getPageSize());
        List<NeoMap> allDataList = getNeoList().stream().flatMap(db -> db.page(tableName, columns, aggregate).stream()).collect(Collectors.toList());
        Integer startIndex = page.getStartIndex();
        Integer pageSize = page.getPageSize();
        return allDataList.subList(startIndex, startIndex + pageSize);
    }

    @Override
    public List<NeoMap> page(String tableName, NeoPage page) {
        NeoPage aggregate = NeoPage.of(0, page.getStartIndex() + page.getPageSize());
        List<NeoMap> allDataList = getNeoList().stream().flatMap(db -> db.page(tableName, aggregate).stream()).collect(Collectors.toList());
        Integer startIndex = page.getStartIndex();
        Integer pageSize = page.getPageSize();
        return allDataList.subList(startIndex, startIndex + pageSize);
    }

    @Override
    public Integer count(String tableName, NeoMap searchMap) {
        Neo neo = getDevideDb(tableName, searchMap);
        if (null != neo) {
            return neo.count(getDevideTable(tableName, searchMap), searchMap);
        } else {
            return getNeoList().stream().map(db -> db.count(tableName, searchMap)).reduce((a, b) -> a + b).orElse(0);
        }
    }

    @Override
    public Integer count(String tableName, Object entity) {
        Neo neo = getDevideDb(tableName, entity);
        if (null != neo) {
            return neo.count(getDevideTable(tableName, entity), entity);
        } else {
            return getNeoList().stream().map(db -> db.count(tableName, entity)).reduce((a, b) -> a + b).orElse(0);
        }
    }

    @Override
    public Integer count(String tableName) {
        return getNeoList().stream().map(db -> db.count(tableName)).reduce((a, b) -> a + b).orElse(0);
    }

    // todo
    @Override
    public Integer batchInsert(String tableName, List<NeoMap> dataMapList) {
        List<Pair<Neo, List<NeoMap>>> dbAndDataList = getDevideDbAndData(tableName, dataMapList);
        Integer result = 0;
        for (Pair<Neo, List<NeoMap>> dataPair : dbAndDataList) {
            result += dataPair.getKey().batchInsert(tableName, dataPair.getValue());
        }
        return result;
    }

    @Override
    public <T> Integer batchInsertEntity(String tableName, List<T> dataList) {
        List<Pair<Neo, List<NeoMap>>> dbAndDataList = getDevideDbAndData(tableName, dataList);
        Integer result = 0;
        for (Pair<Neo, List<NeoMap>> dataPair : dbAndDataList) {
            result += dataPair.getKey().batchInsert(tableName, dataPair.getValue());
        }
        return result;
    }

    @Override
    public Integer batchUpdate(String tableName, List<NeoMap> dataList) {
        List<Pair<Neo, List<NeoMap>>> dbAndDataList = getDevideDbAndData(tableName, dataList);
        Integer result = 0;
        for (Pair<Neo, List<NeoMap>> dataPair : dbAndDataList) {
            result += dataPair.getKey().batchUpdate(tableName, dataPair.getValue());
        }
        return result;
    }

    @Override
    public Integer batchUpdate(String tableName, List<NeoMap> dataList, Columns columns) {
        List<Pair<Neo, List<NeoMap>>> dbAndDataList = getDevideDbAndData(tableName, dataList.stream().map(d -> d.assign(columns)).collect(Collectors.toList()));
        Integer result = 0;
        for (Pair<Neo, List<NeoMap>> dataPair : dbAndDataList) {
            result += dataPair.getKey().batchUpdate(tableName, dataPair.getValue(), columns);
        }
        return result;
    }

    @Override
    public <T> Integer batchUpdateEntity(String tableName, List<T> dataList) {
        List<Pair<Neo, List<NeoMap>>> dbAndDataList = getDevideDbAndData(tableName, dataList);
        Integer result = 0;
        for (Pair<Neo, List<NeoMap>> dataPair : dbAndDataList) {
            result += dataPair.getKey().batchUpdate(tableName, dataPair.getValue());
        }
        return result;
    }

    @Override
    public <T> Integer batchUpdateEntity(String tableName, List<T> dataList, Columns columns) {
        List<Pair<Neo, List<NeoMap>>> dbAndDataList = getDevideDbAndData(tableName,
            dataList.stream().map(d -> NeoMap.from(d, columns, NeoMap.NamingChg.UNDERLINE)).collect(Collectors.toList()));
        Integer result = 0;
        for (Pair<Neo, List<NeoMap>> dataPair : dbAndDataList) {
            result += dataPair.getKey().batchUpdate(tableName, dataPair.getValue(), columns);
        }
        return result;
    }

    @Data
    private static class DevideDbBatch {

        private Neo db;
        private List<DevideTableBatch> devideTableBatchList;
    }

    @Data
    private static class DevideTableBatch {

        private String tableName;
        private List<NeoMap> dataMapList;
    }


    @Getter
    @AllArgsConstructor
    private static class TableHashMeta {

        /**
         * 哈希的下标索引
         */
        private Integer index;
        /**
         * 带有后缀的表名
         */
        private String tableNameWithIndex;
    }
}
