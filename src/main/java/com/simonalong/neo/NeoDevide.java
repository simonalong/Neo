package com.simonalong.neo;

import com.simonalong.neo.core.AbstractBaseDb;
import com.simonalong.neo.db.NeoPage;
import com.simonalong.neo.exception.NeoException;
import com.simonalong.neo.util.ObjectUtil;

import java.util.ArrayList;
import java.util.Collection;
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
public final class NeoDevide extends AbstractBaseDb {

    /**
     * 待分库的db的名字
     */
    private String dbName;
    /**
     * 库名以及对应的db的名字，比如：key：xxx_1, value：哈希值：1， Neo
     */
    private Map<String, Pair<Integer, Neo>> devideNeoMap = new ConcurrentHashMap<>(8);
    /**
     * 分库字段对应的表名
     */
    private String devideTableName;
    /**
     * 分库字段
     */
    private String devideColumnName;
    /**
     * 分库的最小值
     */
    private Integer min = 0;
    /**
     * 分库的最大值
     */
    private Integer max = 0;
    private Integer devideSize = 0;

    /**
     * 添加分库
     *
     * @param devideDbNames 分库的db分库表达式，{0, 12}作为后缀。比如：xxx{0, 12}
     * @param neoList       db的列表
     * @param tableName     待分库的表名
     * @param columnName    列名
     */
    public NeoDevide(String devideDbNames, List<Neo> neoList, String tableName, String columnName) {
        String regex = "^(.*)\\{(\\d),.*(\\d)}$";
        Matcher matcher = Pattern.compile(regex).matcher(devideDbNames);
        if (matcher.find()) {
            dbName = matcher.group(1);
            min = Integer.valueOf(matcher.group(2));
            max = Integer.valueOf(matcher.group(3));
            if (min >= max) {
                throw new NeoException("数据配置错误: 最大值不能小于最小值");
            }
            devideSize = max - min;
            for (Integer index = min, indexJ = 0; index <= max; index++, indexJ++) {
                devideNeoMap.putIfAbsent(dbName + index, new Pair<>(indexJ, neoList.get(indexJ)));
            }
            this.devideTableName = tableName;
            this.devideColumnName = columnName;
        } else {
            throw new NeoException("没有发现要分库的数据");
        }
    }

    /**
     * 根据对应字段的值获取分库对应的DB
     *
     * @param value 分库字段对应的值
     * @return 分库db
     */
    private Neo getDevideDb(Object value) {
        Number index = ObjectUtil.cast(Number.class, value);
        if (null == index) {
            throw new NeoException("数据转换失败");
        }

        String tableDevideStr = dbName + ((index.intValue() % devideSize) + min);
        if (devideNeoMap.containsKey(tableDevideStr)) {
            return devideNeoMap.get(tableDevideStr).getValue();
        } else {
            throw new NeoException("没有找到对应的分库");
        }
    }

    private Object getDevideColumnValue(String tableName, NeoMap dataMap) {
        if (devideTableName.equals(tableName)) {
            return dataMap.get(devideColumnName);
        }
        return null;
    }

    private Object getDevideColumnValue(String tableName, Object object) {
        if (devideTableName.equals(tableName)) {
            return NeoMap.from(object).get(devideColumnName);
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
    private <T> List<Pair<Neo, List<NeoMap>>> getDevideDbAndData(String tableName, List<T> dataList) {
        List<Pair<Neo, List<NeoMap>>> mapList = new ArrayList<>();
        List<NeoMap> dataMapList = dataList.stream().map(d -> NeoMap.from(d, NeoMap.NamingChg.UNDERLINE)).collect(Collectors.toList());

        // 非分库的表，则返回所有的数据
        if (!tableName.equals(devideTableName)) {
            return devideNeoMap.values().stream().map(m -> new Pair<>(m.getValue(), dataMapList)).collect(Collectors.toList());
        } else {
            Collection<Pair<Integer, Neo>> indexAndNeoCollection = devideNeoMap.values();
            for (Pair<Integer, Neo> indexNeoPair : indexAndNeoCollection) {
                mapList.add(new Pair<>(indexNeoPair.getValue(), getDevideMapList(indexNeoPair.getKey(), dataMapList)));
            }
        }

        return mapList;
    }

    /**
     * 根据哈希值获取该db负责的数据
     *
     * @param index    哈希索引
     * @param dataList 数据
     * @return 该哈希对应的数据
     */
    private List<NeoMap> getDevideMapList(Integer index, List<NeoMap> dataList) {
        List<NeoMap> resultList = new ArrayList<>();
        for (NeoMap neoMap : dataList) {
            if (neoMap.containsKey(devideColumnName)) {
                Integer value = ObjectUtil.cast(Integer.class, neoMap.get(devideColumnName));
                if (null != value && value.equals(index)) {
                    resultList.add(neoMap);
                }
            } else {
                resultList.add(neoMap);
            }
        }
        return resultList;
    }

    private List<Neo> getNeoList() {
        return devideNeoMap.values().stream().map(Pair::getValue).collect(Collectors.toList());
    }

    @Override
    public NeoMap insert(String tableName, NeoMap dataMap) {
        Neo neo = getDevideDb(getDevideColumnValue(tableName, dataMap));
        if (null != neo) {
            return neo.insert(tableName, dataMap);
        } else {
            getNeoList().forEach(n -> n.insert(tableName, dataMap));
            return NeoMap.of();
        }
    }

    @Override
    public <T> T insert(String tableName, T object) {
        Neo neo = getDevideDb(getDevideColumnValue(tableName, object));
        if (null != neo) {
            return neo.insert(tableName, object);
        } else {
            getNeoList().forEach(n -> n.insert(tableName, object));
            return object;
        }
    }

    @Override
    public Integer delete(String tableName, NeoMap searchMap) {
        Neo neo = getDevideDb(getDevideColumnValue(tableName, searchMap));
        if (null != neo) {
            return neo.delete(tableName, searchMap);
        } else {
            getNeoList().forEach(n -> n.delete(tableName, searchMap));
            return getNeoList().size();
        }
    }

    @Override
    public <T> Integer delete(String tableName, T object) {
        Neo neo = getDevideDb(getDevideColumnValue(tableName, object));
        if (null != neo) {
            return neo.delete(tableName, object);
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
        Neo neo = getDevideDb(getDevideColumnValue(tableName, searchMap));
        if (null != neo) {
            return neo.update(tableName, dataMap, searchMap);
        } else {
            getNeoList().forEach(n -> n.update(tableName, dataMap, searchMap));
            return NeoMap.of();
        }
    }

    @Override
    public <T> T update(String tableName, T setEntity, NeoMap searchMap) {
        Neo neo = getDevideDb(getDevideColumnValue(tableName, searchMap));
        if (null != neo) {
            return neo.update(tableName, setEntity, searchMap);
        } else {
            getNeoList().forEach(n -> n.update(tableName, setEntity, searchMap));
            return null;
        }
    }

    @Override
    public <T> T update(String tableName, T setEntity, T searchEntity) {
        Neo neo = getDevideDb(getDevideColumnValue(tableName, searchEntity));
        if (null != neo) {
            return neo.update(tableName, setEntity, searchEntity);
        } else {
            getNeoList().forEach(n -> n.update(tableName, setEntity, searchEntity));
            return null;
        }
    }

    @Override
    public <T> NeoMap update(String tableName, NeoMap setMap, T searchEntity) {
        Neo neo = getDevideDb(getDevideColumnValue(tableName, searchEntity));
        if (null != neo) {
            return neo.update(tableName, setMap, searchEntity);
        } else {
            getNeoList().forEach(n -> n.update(tableName, setMap, searchEntity));
            return NeoMap.of();
        }
    }

    @Override
    public NeoMap update(String tableName, NeoMap dataMap, Columns columns) {
        Neo neo = getDevideDb(getDevideColumnValue(tableName, dataMap.assign(columns)));
        if (null != neo) {
            return neo.update(tableName, dataMap, columns);
        } else {
            getNeoList().forEach(n -> n.update(tableName, dataMap, columns));
            return NeoMap.of();
        }
    }

    @Override
    public <T> T update(String tableName, T entity, Columns columns) {
        Neo neo = getDevideDb(getDevideColumnValue(tableName, NeoMap.from(entity, columns, NeoMap.NamingChg.UNDERLINE)));
        if (null != neo) {
            return neo.update(tableName, entity, columns);
        } else {
            getNeoList().forEach(n -> n.update(tableName, entity, columns));
            return null;
        }
    }

    @Override
    public NeoMap update(String tableName, NeoMap dataMap) {
        Neo neo = getDevideDb(getDevideColumnValue(tableName, dataMap));
        if (null != neo) {
            return neo.update(tableName, dataMap);
        } else {
            getNeoList().forEach(n -> n.update(tableName, dataMap));
            return NeoMap.of();
        }
    }

    @Override
    public <T> T update(String tableName, T entity) {
        Neo neo = getDevideDb(getDevideColumnValue(tableName, entity));
        if (null != neo) {
            return neo.update(tableName, entity);
        } else {
            getNeoList().forEach(n -> n.update(tableName, entity));
            return null;
        }
    }

    @Override
    public NeoMap one(String tableName, Columns columns, NeoMap searchMap) {
        Neo neo = getDevideDb(getDevideColumnValue(tableName, searchMap));
        if (null != neo) {
            return neo.one(tableName, columns, searchMap);
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
        Neo neo = getDevideDb(getDevideColumnValue(tableName, entity));
        if (null != neo) {
            return neo.one(tableName, columns, entity);
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
        Neo neo = getDevideDb(getDevideColumnValue(tableName, searchMap));
        if (null != neo) {
            return neo.one(tableName, searchMap);
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
        Neo neo = getDevideDb(getDevideColumnValue(tableName, entity));
        if (null != neo) {
            return neo.one(tableName, entity);
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
        Neo neo = getDevideDb(getDevideColumnValue(tableName, searchMap));
        if (null != neo) {
            return neo.list(tableName, columns, searchMap);
        } else {
            return getNeoList().stream().flatMap(db -> db.list(tableName, columns, searchMap).stream()).collect(Collectors.toList());
        }
    }

    @Override
    public <T> List<T> list(String tableName, Columns columns, T entity) {
        Neo neo = getDevideDb(getDevideColumnValue(tableName, entity));
        if (null != neo) {
            return neo.list(tableName, columns, entity);
        } else {
            return getNeoList().stream().flatMap(db -> db.list(tableName, columns, entity).stream()).collect(Collectors.toList());
        }
    }

    @Override
    public List<NeoMap> list(String tableName, NeoMap searchMap) {
        Neo neo = getDevideDb(getDevideColumnValue(tableName, searchMap));
        if (null != neo) {
            return neo.list(tableName, searchMap);
        } else {
            return getNeoList().stream().flatMap(db -> db.list(tableName, searchMap).stream()).collect(Collectors.toList());
        }
    }

    @Override
    public <T> List<T> list(String tableName, T entity) {
        Neo neo = getDevideDb(getDevideColumnValue(tableName, entity));
        if (null != neo) {
            return neo.list(tableName, entity);
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
        Neo neo = getDevideDb(getDevideColumnValue(tableName, searchMap));
        if (null != neo) {
            return neo.value(tableName, tClass, field, searchMap);
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
        Neo neo = getDevideDb(getDevideColumnValue(tableName, entity));
        if (null != neo) {
            return neo.value(tableName, tClass, field, entity);
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
        Neo neo = getDevideDb(getDevideColumnValue(tableName, searchMap));
        if (null != neo) {
            return neo.value(tableName, field, searchMap);
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
        Neo neo = getDevideDb(getDevideColumnValue(tableName, entity));
        if (null != neo) {
            return neo.value(tableName, field, entity);
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
        Neo neo = getDevideDb(getDevideColumnValue(tableName, searchMap));
        if (null != neo) {
            return neo.values(tableName, tClass, field, searchMap);
        } else {
            return getNeoList().stream().flatMap(db -> db.values(tableName, tClass, field, searchMap).stream()).collect(Collectors.toList());
        }
    }

    @Override
    public <T> List<T> values(String tableName, Class<T> tClass, String field, Object entity) {
        Neo neo = getDevideDb(getDevideColumnValue(tableName, entity));
        if (null != neo) {
            return neo.values(tableName, tClass, field, entity);
        } else {
            return getNeoList().stream().flatMap(db -> db.values(tableName, tClass, field, entity).stream()).collect(Collectors.toList());
        }
    }

    @Override
    public List<String> values(String tableName, String field, NeoMap searchMap) {
        Neo neo = getDevideDb(getDevideColumnValue(tableName, searchMap));
        if (null != neo) {
            return neo.values(tableName, field, searchMap);
        } else {
            return getNeoList().stream().flatMap(db -> db.values(tableName, field, searchMap).stream()).collect(Collectors.toList());
        }
    }

    @Override
    public List<String> values(String tableName, String field, Object entity) {
        Neo neo = getDevideDb(getDevideColumnValue(tableName, entity));
        if (null != neo) {
            return neo.values(tableName, field, entity);
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
        Neo neo = getDevideDb(getDevideColumnValue(tableName, searchMap));
        if (null != neo) {
            return neo.page(tableName, columns, searchMap, page);
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
        Neo neo = getDevideDb(getDevideColumnValue(tableName, entity));
        if (null != neo) {
            return neo.page(tableName, columns, entity, page);
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
        Neo neo = getDevideDb(getDevideColumnValue(tableName, searchMap));
        if (null != neo) {
            return neo.page(tableName, searchMap, page);
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
        Neo neo = getDevideDb(getDevideColumnValue(tableName, entity));
        if (null != neo) {
            return neo.page(tableName, entity, page);
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
        Neo neo = getDevideDb(getDevideColumnValue(tableName, searchMap));
        if (null != neo) {
            return neo.count(tableName, searchMap);
        } else {
            return getNeoList().stream().map(db -> db.count(tableName, searchMap)).reduce((a, b) -> a + b).orElse(0);
        }
    }

    @Override
    public Integer count(String tableName, Object entity) {
        Neo neo = getDevideDb(getDevideColumnValue(tableName, entity));
        if (null != neo) {
            return neo.count(tableName, entity);
        } else {
            return getNeoList().stream().map(db -> db.count(tableName, entity)).reduce((a, b) -> a + b).orElse(0);
        }
    }

    @Override
    public Integer count(String tableName) {
        return getNeoList().stream().map(db -> db.count(tableName)).reduce((a, b) -> a + b).orElse(0);
    }

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
}
