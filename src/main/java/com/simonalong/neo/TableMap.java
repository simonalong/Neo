package com.simonalong.neo;

import com.alibaba.fastjson.JSON;
import com.simonalong.neo.annotation.Column;
import com.simonalong.neo.annotation.Table;
import com.simonalong.neo.db.TimeDateConverter;
import com.simonalong.neo.exception.NeoException;
import com.simonalong.neo.exception.NumberOfValueException;
import com.simonalong.neo.exception.ParameterNullException;
import com.simonalong.neo.util.ObjectUtil;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.simonalong.neo.NeoConstant.DEFAULT_TABLE;

/**
 * 存储表中的结构
 *
 * @author shizi
 * @since 2020/3/21 下午8:10
 */
@SuppressWarnings("unused")
public class TableMap implements Map<String, Object>, Cloneable, Serializable {

    private static final Integer KV_NUM = 2;
    /**
     * key：为表名，value：为表中对应的数据：key为列，value为列值
     */
    private final Map<String, NeoMap> dataMap = new ConcurrentHashMap<>();

    public static TableMap of(Object... kvs) {
        return TableMap.of(DEFAULT_TABLE, kvs);
    }

    public static TableMap of(String tableName, Object... kvs) {
        if (kvs.length % KV_NUM != 0) {
            throw new NumberOfValueException("参数请使用：key,value,key,value...这种参数格式");
        }

        TableMap tableMap = new TableMap();
        NeoMap neoMap = new NeoMap();
        for (int i = 0; i < kvs.length; i += KV_NUM) {
            if (null == kvs[i]) {
                throw new ParameterNullException("NeoMap.of()中的参数不可为null");
            }
            neoMap.put((String) kvs[i], kvs[i + 1]);
        }
        if (!neoMap.isEmpty()) {
            tableMap.put(tableName, neoMap);
        }
        return tableMap;
    }

    /**
     * 从数据获取
     * <p>
     * 这个数据实体有注解@Table
     *
     * @param object 实体
     * @return tableMap
     */
    public static TableMap from(Object object) {
        if (object.getClass().isAnnotationPresent(Table.class)) {
            Table table = object.getClass().getDeclaredAnnotation(Table.class);
            String tableName = table.value();
            TableMap tableMap = TableMap.of(tableName);
            tableMap.put(tableName, NeoMap.from(object));
            return tableMap;
        }
        return TableMap.of();
    }

    public static TableMap from(String tableName, Object object) {
        TableMap tableMap = TableMap.of(tableName);
        tableMap.put(tableName, NeoMap.from(object));
        return tableMap;
    }

    public static TableMap from(String tableName, Object object, NeoMap.NamingChg namingChg) {
        TableMap tableMap = TableMap.of(tableName);
        tableMap.put(tableName, NeoMap.from(object, namingChg));
        return tableMap;
    }

    public static TableMap from(String tableName, Object object, String... columnsNames) {
        TableMap tableMap = TableMap.of(tableName);
        tableMap.put(tableName, NeoMap.from(object, columnsNames));
        return tableMap;
    }

    /**
     * 对象转换为TableMap
     *
     * @param object      待转换的对象
     * @param namingChg   转换规则
     * @param inFieldList 包括的对象的属性名列表
     * @param exFieldList 排除的对象的属性名列表
     * @return 转换之后的TableMap
     */
    private static TableMap from(String tableName, Object object, NeoMap.NamingChg namingChg, List<String> inFieldList, List<String> exFieldList) {
        TableMap tableMap = TableMap.of(tableName);
        if (null == object) {
            return tableMap;
        }

        NeoMap neoMap = NeoMap.from(object, namingChg, inFieldList, exFieldList, false);
        tableMap.put(tableName, neoMap);
        return tableMap;
    }

    /**
     * 指定包括的属性进行对象转换为TableMap
     *
     * @param tableName 表名
     * @param object 待转换对象
     * @param fields 对象的属性名列表
     * @return 转换之后的TableMap
     */
    public static TableMap fromInclude(String tableName, Object object, String... fields) {
        TableMap tableMap = TableMap.of(tableName);
        tableMap.put(tableName, NeoMap.from(object, fields));
        return tableMap;
    }

    /**
     * 指定包括的属性进行对象转换为TableMap
     *
     * @param tableName 表名
     * @param object    待转换对象
     * @param namingChg 命名转换风格
     * @param fields    对象的属性名列表
     * @return 转换之后的TableMap
     */
    public static TableMap fromInclude(String tableName, Object object, NeoMap.NamingChg namingChg, String... fields) {
        TableMap tableMap = TableMap.of(tableName);
        tableMap.put(tableName, NeoMap.fromInclude(object, namingChg, fields));
        return tableMap;
    }

    /**
     * 指定排除的属性进行对象转换为TableMap
     *
     * @param tableName 表名
     * @param object 待转换对象
     * @param fields 排除的对象的属性名列表
     * @return 转换之后的TableMap
     */
    public static TableMap fromExclude(String tableName, Object object, String... fields) {
        TableMap tableMap = TableMap.of(tableName);
        tableMap.put(tableName, NeoMap.fromExclude(object, fields));
        return tableMap;
    }

    /**
     * 指定排除的属性进行对象转换为TableMap
     *
     * @param tableName 表名
     * @param object    待转换对象
     * @param namingChg 转换规则
     * @param fields    排除的对象的属性名列表
     * @return 转换之后的TableMap
     */
    public static TableMap fromExclude(String tableName, Object object, NeoMap.NamingChg namingChg, String... fields) {
        TableMap tableMap = TableMap.of(tableName);
        tableMap.put(tableName, NeoMap.fromExclude(object, namingChg, fields));
        return tableMap;
    }

    public static <T> List<TableMap> fromArray(String tableName, List<T> dataList) {
        if (null == dataList || dataList.isEmpty()) {
            return Collections.emptyList();
        }
        return NeoMap.fromArray(dataList).stream().map(map -> TableMap.of().append(tableName, map)).collect(Collectors.toList());
    }

    /**
     * 从实体集合转换为TableMap
     *
     * @param tableName 表名
     * @param dataList 待转换的数据集合
     * @param columns  只转换的数据列名
     * @param <T>      实体类型
     * @return TableMap
     */
    public static <T> List<TableMap> fromArray(String tableName, List<T> dataList, Columns columns) {
        if (null == dataList || dataList.isEmpty()) {
            return Collections.emptyList();
        }
        return NeoMap.fromArray(dataList, columns).stream().map(map -> TableMap.of().append(tableName, map)).collect(Collectors.toList());
    }

    public static <T> List<TableMap> fromArray(String tableName, List<T> dataList, NeoMap.NamingChg namingChg, Columns columns) {
        if (null == dataList || dataList.isEmpty()) {
            return Collections.emptyList();
        }
        return NeoMap.fromArray(dataList, namingChg, columns).stream().map(map -> TableMap.of().append(tableName, map)).collect(Collectors.toList());
    }

    public static <T> List<TableMap> fromArray(String tableName, List<T> dataList, NeoMap.NamingChg namingChg) {
        if (null == dataList || dataList.isEmpty()) {
            return Collections.emptyList();
        }
        return NeoMap.fromArray(dataList, namingChg).stream().map(map -> TableMap.of().append(tableName, map)).collect(Collectors.toList());
    }

    public static boolean isEmpty(TableMap neoMap) {
        return neoMap == null || neoMap.isEmpty();
    }

    public static boolean isUnEmpty(TableMap tableMap) {
        return !(isEmpty(tableMap));
    }

    @Override
    public int size() {
        return dataMap.size();
    }

    public int size(String tableName) {
        if (dataMap.containsKey(tableName)) {
            return dataMap.get(tableName).size();
        } else if (dataMap.containsKey(DEFAULT_TABLE)) {
            return dataMap.get(DEFAULT_TABLE).size();
        }
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return dataMap.isEmpty();
    }

    public boolean isEmpty(String tableName) {
        if (dataMap.containsKey(tableName)) {
            return dataMap.get(tableName).isEmpty();
        } else if(dataMap.containsKey(DEFAULT_TABLE)){
            return dataMap.get(DEFAULT_TABLE).isEmpty();
        }
        return true;
    }

    @Override
    public boolean containsKey(Object key) {
        return dataMap.containsKey(key);
    }

    @SuppressWarnings("all")
    public boolean containsKey(String tableName, Object key) {
        if (dataMap.containsKey(tableName)) {
            return dataMap.get(tableName).containsKey(key);
        } else if(dataMap.containsKey(DEFAULT_TABLE)){
            return dataMap.get(DEFAULT_TABLE).containsValue(key);
        }
        return false;
    }

    public boolean haveTable(String tableName){
        return dataMap.containsKey(tableName);
    }

    @Override
    @SuppressWarnings("all")
    public boolean containsValue(Object value) {
        return dataMap.containsKey(value);
    }

    public boolean containsValue(String tableName, Object value) {
        if (dataMap.containsKey(tableName)) {
            return dataMap.get(tableName).containsValue(value);
        } else if(dataMap.containsKey(DEFAULT_TABLE)){
            return dataMap.get(DEFAULT_TABLE).containsValue(value);
        }
        return false;
    }

    @Override
    @SuppressWarnings("all")
    public Object get(Object key) {
        if (dataMap.containsKey(key)) {
            return dataMap.get(key);
        } else if (dataMap.containsKey(DEFAULT_TABLE)) {
            return dataMap.get(DEFAULT_TABLE);
        }
        return null;
    }

    public Object get(String tableName, String key) {
        if (dataMap.containsKey(tableName)) {
            return dataMap.get(tableName).get(key);
        } else if (dataMap.containsKey(DEFAULT_TABLE)) {
            return dataMap.get(DEFAULT_TABLE).get(key);
        } else if (DEFAULT_TABLE.equals(tableName)) {
            return getFirst().get(key);
        }
        return null;
    }

    /**
     * 获取表对应的列和值
     *
     * @param tableName 表名
     * @return 一个表对应的列名和值
     */
    public NeoMap getNeoMap(String tableName) {
        return (NeoMap) get(tableName);
    }

    /**
     * 获取第一个找到的数据
     * <p>
     * 该功能用于有数据就获取，但是不知道里面的key
     *
     * @return 获取的某个表数据
     */
    public NeoMap getFirst() {
        Entry<String, NeoMap> dataEntry = dataMap.entrySet().stream().findFirst().orElse(null);
        if (null != dataEntry) {
            return dataEntry.getValue();
        }
        return NeoMap.of();
    }

    /**
     * 获取对应的表的列表的实体类型
     *
     * @param tableName 表名
     * @param key       key
     * @return 实体对应的NeoMap
     */
    public NeoMap getValueNeoMap(String tableName, String key) {
        if (dataMap.containsKey(tableName)) {
            return getNeoMap(tableName).get(NeoMap.class, key);
        } else if (dataMap.containsKey(DEFAULT_TABLE)) {
            return dataMap.get(DEFAULT_TABLE).get(NeoMap.class, key);
        }
        return NeoMap.of();
    }

    public <T> T get(Class<T> tClass, String tableName, String key) {
        if (dataMap.containsKey(tableName)) {
            return getNeoMap(tableName).get(tClass, key);
        } else if (dataMap.containsKey(DEFAULT_TABLE)) {
            return getNeoMap(DEFAULT_TABLE).get(tClass, key);
        }
        return null;
    }

    /**
     * 添加数据
     * <p>
     * 如果没有指定表名，则我们会将其放到默认表中
     *
     * @param key   key
     * @param value value
     * @return key的旧值
     */
    @Override
    public Object put(String key, Object value) {
        if (value instanceof NeoMap) {
            return dataMap.put(key, (NeoMap) value);
        } else {
            return dataMap.put(key, NeoMap.from(value));
        }
    }

    public Object put(String tableName, String key, Object value) {
        return put(tableName, key, value, true);
    }

    /**
     * 是否进行时间类型到Long类型的转换来放置数据
     *
     * @param tableName      表名
     * @param key            key
     * @param value          value
     * @param timeTypeToLong 是否时间类型转Long：true：转换，false: 不转换
     * @return value的值
     */
    public Object put(String tableName, String key, Object value, Boolean timeTypeToLong) {
        return dataMap.compute(tableName, (k, v) -> {
            if (v == null) {
                NeoMap data = NeoMap.of();
                data.put(key, value, timeTypeToLong);
                return data;
            } else {
                v.put(key, value, timeTypeToLong);
                return v;
            }
        });
    }

    @Override
    public Object remove(Object key) {
        return dataMap.remove(key);
    }

    @SuppressWarnings("all")
    public Object remove(String tableName, Object key) {
        if (dataMap.containsKey(tableName)) {
            return dataMap.get(tableName).remove(key);
        } else if (dataMap.containsKey(DEFAULT_TABLE)) {
            return dataMap.get(DEFAULT_TABLE).remove(key);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void putAll(Map<? extends String, ?> m) {
        dataMap.putAll((Map<? extends String, ? extends NeoMap>) m);
    }

    public void putAll(String tableName, Map<? extends String, ?> m) {
        if (dataMap.containsKey(tableName)) {
            dataMap.get(tableName).putAll(m);
        }
    }

    @Override
    public void clear() {
        dataMap.clear();
    }

    public void clear(String tableName) {
        if (dataMap.containsKey(tableName)) {
            dataMap.get(tableName).clear();
        } else if (dataMap.containsKey(DEFAULT_TABLE)) {
            dataMap.get(DEFAULT_TABLE).clear();
        }
    }

    @Override
    public Set<String> keySet() {
        return dataMap.keySet();
    }

    public Set<String> keySet(String tableName) {
        if (dataMap.containsKey(tableName)) {
            return dataMap.get(tableName).keySet();
        } else if (dataMap.containsKey(DEFAULT_TABLE)) {
            return dataMap.get(DEFAULT_TABLE).keySet();
        }
        return null;
    }

    @Override
    public Collection<Object> values() {
        return new ArrayList<>(dataMap.values());
    }

    public Collection<Object> values(String tableName) {
        if (dataMap.containsKey(tableName)) {
            return dataMap.get(tableName).values();
        } else if (dataMap.containsKey(DEFAULT_TABLE)) {
            return dataMap.get(DEFAULT_TABLE).values();
        }
        return Collections.emptyList();
    }

    public List<NeoMap> getNeoMapList(String tableName) {
        return values(tableName).stream().map(o -> (NeoMap) o).collect(Collectors.toList());
    }

    public <T> List<T> getEntityList(Class<T> tClass, String tableName) {
        return getNeoMapList(tableName).stream().map(map -> map.as(tClass)).collect(Collectors.toList());
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return dataMap.entrySet().stream().map(v -> new NeoMap.Node(v.getKey(), v.getValue())).collect(Collectors.toSet());
    }

    public Set<Entry<String, Object>> entrySet(String tableName) {
        if (dataMap.containsKey(tableName)) {
            return dataMap.get(tableName).entrySet();
        } else if (dataMap.containsKey(DEFAULT_TABLE)) {
            return dataMap.get(DEFAULT_TABLE).entrySet();
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Map)) {
            return false;
        }
        return dataMap.equals(o);
    }

    public boolean equals(String tableName, Object o) {
        if (dataMap.containsKey(tableName)) {
            return dataMap.get(tableName).equals(o);
        } else if (dataMap.containsKey(DEFAULT_TABLE)) {
            return dataMap.get(DEFAULT_TABLE).equals(o);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return dataMap.hashCode();
    }

    public List<NeoMap> toNeoMapList() {
        return values().stream().map(v -> (NeoMap) v).collect(Collectors.toList());
    }

    /**
     * 多表情况下转换为一个实体
     *
     * @param tClass 目标实体，属性可能包含@Column，其中属性tableName有值
     * @param <T>    类型
     * @return 对应类型的实体
     */
    public <T> T as(Class<T> tClass) {
        return as(tClass, NeoMap.NamingChg.DEFAULT);
    }

    public <T> T as(Class<T> tClass, String tableName) {
        return as(tClass, tableName, NeoMap.NamingChg.DEFAULT);
    }

    public <T> T as(Class<T> tClass, NeoMap.NamingChg namingChg) {
        return as(tClass, DEFAULT_TABLE, namingChg);
    }

    /**
     * 多表情况下转换为一个实体
     * <p>
     * tableName的优先级：入参 大于 @Column中的tableName 大于 @Table
     *
     * @param tClass    目标实体，属性可能包含@Column，其中属性tableName有值
     * @param tableName 表名。如果入参不空，则采用实体的@Table注解或者@Column中有tableName，否则采用入参
     * @param namingChg 命名转换规则
     * @param <T>       类型
     * @return 对应类型的实体
     */
    public <T> T as(Class<T> tClass, String tableName, NeoMap.NamingChg namingChg) {
        if (dataMap.isEmpty()) {
            return null;
        }

        if (NeoMap.class.isAssignableFrom(tClass)) {
            return null;
        }
        T t;
        try {
            Constructor<T> constructor = tClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            t = constructor.newInstance();
            Field[] fields = tClass.getDeclaredFields();
            if (fields.length != 0) {
                Stream.of(fields).forEach(f -> {
                    // final字段不处理
                    if (Modifier.isFinal(f.getModifiers())) {
                        return;
                    }
                    f.setAccessible(true);
                    try {
                        Object value = getValue(tClass, tableName, f, namingChg);
                        if (null != value) {
                            f.set(t, value);
                        }
                    } catch (IllegalAccessException e) {
                        throw new NeoException(e);
                    }
                });
            }
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new NeoException(e);
        }
        return t;
    }

    private Object getValue(Class<?> tClass, String tableName, Field field, NeoMap.NamingChg namingChg) {
        Entry<String, String> tableAndFieldName = getTableAndFieldName(tClass, field, tableName, namingChg);

        String tableFinalName = tableAndFieldName.getKey();
        String columnName = tableAndFieldName.getValue();
        if (null == tableFinalName || "".equals(tableFinalName) || null == columnName || "".equals(columnName)) {
            return null;
        }

        Class<?> fieldClass = field.getType();
        return ObjectUtil.cast(fieldClass, TimeDateConverter.valueToEntityTime(fieldClass, get(tableFinalName, columnName)));
    }

    /**
     * NeoMap中的value向实体类型转换，先经过时间转换器，对于是时间类型的则转换为时间类型，对于非时间类型的，则保持，然后再经过兼容类型
     *
     * @param f   属性
     * @param key 类型转换后的名字
     * @return 经过转换之后的实体中的值
     */
    private Object toEntityValue(Field f, String key) {
        Class<?> fieldClass = f.getType();
        return ObjectUtil.cast(fieldClass, TimeDateConverter.valueToEntityTime(fieldClass, get(key)));
    }

    /**
     * 获取表名和列名
     * <p>
     * 表名优先级：TableName > @Column(table) > @Table
     * 列名优先级：@Column(value) > NamingChg
     *
     * @param tClass    待接收的实体类
     * @param field     属性
     * @param tableName 表名
     * @param namingChg 转换规则
     * @return 表名和属性名
     */
    private Entry<String, String> getTableAndFieldName(Class<?> tClass, Field field, String tableName, NeoMap.NamingChg namingChg) {
        String tableFinalName = tableName;
        String columnFinalName;

        if (!namingChg.equals(NeoMap.NamingChg.DEFAULT)) {
            columnFinalName = namingChg.smallCamelToOther(field.getName());
        } else {
            columnFinalName = NeoMap.getGlobalNaming().smallCamelToOther(field.getName());
        }

        Table table = tClass.getDeclaredAnnotation(Table.class);
        if (null != table) {
            if (!"".equals(table.value())) {
                tableFinalName = table.value();
            }
        }

        Column column = field.getDeclaredAnnotation(Column.class);
        if (null != column) {
            if (!"".equals(column.value())) {
                columnFinalName = column.value();
            }

            if (!"".equals(column.table())) {
                tableFinalName = column.table();
            }
        }

        if (null != tableName && !"".equals(tableName) && !DEFAULT_TABLE.equals(tableName)) {
            tableFinalName = tableName;
        }

        return new HashMap.SimpleImmutableEntry<>(tableFinalName, columnFinalName);
    }

    /**
     * 将TableMap集合转成实体集合
     * <p>
     * 注意，这里会从Class对应的注解@Table中获取表名，否则会数据不会设置
     *
     * @param tClass       实体类型
     * @param tableMapList 表map集合
     * @param <T>          类型
     * @return 实体集合
     */
    public static <T> List<T> asArray(Class<T> tClass, List<TableMap> tableMapList) {
        if (null == tableMapList || tableMapList.isEmpty()) {
            return Collections.emptyList();
        }

        if (!tClass.isAnnotationPresent(Table.class)) {
            return Collections.emptyList();
        }

        return tableMapList.stream().map(table -> table.as(tClass)).collect(Collectors.toList());
    }

    /**
     * 将TableMap集合转成实体集合
     * <p>
     * 注意，这里会从Class可以不用注解@Table和@Column，如果有也会被tableName给覆盖
     *
     * @param tClass       实体类型
     * @param tableName    表名
     * @param tableMapList 表map集合
     * @param <T>          类型
     * @return 实体集合
     */
    public static <T> List<T> asArray(Class<T> tClass, String tableName, List<TableMap> tableMapList) {
        if (null == tableMapList || tableMapList.isEmpty()) {
            return Collections.emptyList();
        }

        return tableMapList.stream().map(table -> table.as(tClass, tableName, NeoMap.NamingChg.DEFAULT)).collect(Collectors.toList());
    }

    public static <T> List<T> asArray(Class<T> tClass, NeoMap.NamingChg namingChg, List<TableMap> tableMapList) {
        if (null == tableMapList || tableMapList.isEmpty()) {
            return Collections.emptyList();
        }

        return tableMapList.stream().map(table -> table.as(tClass, namingChg)).collect(Collectors.toList());
    }

    public static <T> List<T> asArray(Class<T> tClass, String tableName, NeoMap.NamingChg namingChg, List<TableMap> tableMapList) {
        if (null == tableMapList || tableMapList.isEmpty()) {
            return Collections.emptyList();
        }

        return tableMapList.stream().map(table -> table.as(tClass, tableName, namingChg)).collect(Collectors.toList());
    }


    /**
     * 只要固定的几个列值
     *
     * @param tableName 待获取的表
     * @param columns   具体的列
     * @return 新的map结构
     */
    public NeoMap assign(String tableName, Columns columns) {
        return getNeoMap(tableName).assign(columns);
    }

    /**
     * 获取指定的一些列，得到新的数据
     * {@code {"a":12, "b":"ok"} ---->  {"a":12}}
     *
     * @param tableName 表名
     * @param keys 要获取的key，比如上面的a
     * @return 返回包含指定key对应的map
     */
    public NeoMap assign(String tableName, String... keys) {
        return getNeoMap(tableName).assign(keys);
    }

    /**
     * 排除指定的列
     *
     * @param tableName 表名
     * @param columns 其中的列值为map中的key
     * @return 排除指定列后的map
     */
    public NeoMap assignExcept(String tableName, Columns columns) {
        return getNeoMap(tableName).assignExcept(columns);
    }

    /**
     * 排除固定的列
     *
     * @param tableName 表名
     * @param keys      map中的指定的key
     * @return 新的map数据
     */
    public NeoMap assignExcept(String tableName, String... keys) {
        return getNeoMap(tableName).assignExcept(keys);
    }

    public TableMap assignExceptKeys(String... keys) {
        values().stream().map(o -> (NeoMap) o).forEach(m -> m.assignExcept(keys));
        return this;
    }

    public TableMap append(String tableName, Map<String, ?> neoMap) {
        if (containsKey(tableName)) {
            getNeoMap(tableName).append(neoMap);
        }
        return this;
    }

    public TableMap append(String tableName, String key, Object value) {
        if (containsKey(tableName)) {
            getNeoMap(tableName).append(key, value);
        }
        this.put(tableName, key, value);
        return this;
    }

    public TableMap append(String tableName, NeoMap neoMap) {
        if (containsKey(tableName)) {
            getNeoMap(tableName).append(neoMap);
        }
        this.put(tableName, neoMap);
        return this;
    }

    public TableMap append(String key, Object value) {
        return append(DEFAULT_TABLE, key, value);
    }

    /**
     * 删除
     *
     * @param tableName 表名
     * @return 表名对应的旧数据
     */
    public NeoMap delete(String tableName) {
        return (NeoMap) remove(tableName);
    }

    public Object delete(String tableName, String key) {
        getNeoMap(tableName).remove(key);
        return this;
    }

    public Stream<Entry<String, Object>> stream(String tableName) {
        if (containsKey(tableName)) {
            getNeoMap(tableName).stream();
        }
        return Stream.of();
    }

    public Stream<String> keyStream(String tableName) {
        if (containsKey(tableName)) {
            getNeoMap(tableName).keyStream();
        } else if (dataMap.containsKey(DEFAULT_TABLE)) {
            return dataMap.get(DEFAULT_TABLE).keyStream();
        }
        return Stream.of();
    }

    public Stream<Object> valueStream(String tableName) {
        if (containsKey(tableName)) {
            getNeoMap(tableName).stream();
        }
        return Stream.of();
    }

    /**
     * 获取字符
     *
     * @param tableName 表名
     * @param key map的key
     * @return 字符，如果是字符串，则返回字符串的第一个字符
     */
    public Character getChar(String tableName, String key) {
        return ObjectUtil.toChar(get(tableName, key));
    }

    public Character getChar(String tableName, String key, Character defaultValue) {
        Character result = getChar(tableName, key);
        return (null == result) ? defaultValue : result;
    }

    /**
     * 返回值为String类型的值
     *
     * @param tableName 表名
     * @param key map的key
     * @return String类型的值
     */
    public String getString(String tableName, String key) {
        return ObjectUtil.toStr(get(tableName, key));
    }

    public String getString(String tableName, String key, String defaultValue) {
        String result = getString(tableName, key);
        return (null == result) ? defaultValue : result;
    }

    /**
     * 获取值类型为Boolean的值
     *
     * @param tableName 表名
     * @param key map中对应的key
     * @return 若值为true或者TRUE，则返回true，否则其他任何值都返回false，包括false和null
     */
    public Boolean getBoolean(String tableName, String key) {
        return ObjectUtil.toBoolean(get(tableName, key));
    }

    public Boolean getBoolean(String tableName, String key, Boolean defaultValue) {
        Boolean result = getBoolean(tableName, key);
        return (null == result) ? defaultValue : result;
    }

    public Byte getByte(String tableName, String key) {
        return ObjectUtil.toByte(get(key));
    }

    public Byte getByte(String tableName, String key, Byte defaultValue) {
        Byte result = getByte(tableName, key);
        return (null == result) ? defaultValue : result;
    }

    public Short getShort(String tableName, String key) {
        return ObjectUtil.toShort(get(tableName, key));
    }

    public Short getShort(String tableName, String key, Short defaultValue) {
        Short result = getShort(tableName, key);
        return (null == result) ? defaultValue : result;
    }

    public Integer getInteger(String tableName, String key) {
        return ObjectUtil.toInt(get(tableName, key));
    }

    public Integer getInteger(String tableName, String key, Integer defaultValue) {
        Integer result = getInteger(tableName, key);
        return (null == result) ? defaultValue : result;
    }

    public Long getLong(String tableName, String key) {
        return ObjectUtil.toLong(get(tableName, key));
    }

    public Long getLong(String tableName, String key, Long defaultValue) {
        Long result = getLong(tableName, key);
        return (null == result) ? defaultValue : result;
    }

    public Double getDouble(String tableName, String key) {
        return ObjectUtil.toDouble(get(tableName, key));
    }

    public Double getDouble(String tableName, String key, Double defaultValue) {
        Double result = getDouble(tableName, key);
        return (null == result) ? defaultValue : result;
    }

    public Float getFloat(String tableName, String key) {
        return ObjectUtil.toFloat(get(tableName, key));
    }

    public Float getFloat(String tableName, String key, Float defaultValue) {
        Float result = getFloat(tableName, key);
        return (null == result) ? defaultValue : result;
    }

    public Map<String, NeoMap> getDataMap() {
        return dataMap;
    }

    /**
     * 包含所有的key
     *
     * @param tables 表名的列表
     * @return true： 包含所有的值，false：有某些key不包含
     */
    public boolean containsKeys(String... tables) {
        return Stream.of(tables).allMatch(this::containsKey);
    }

    /**
     * 这里采用深拷贝，浅拷贝存在集合并发修改问题
     */
    @SuppressWarnings("all")
    @Override
    public TableMap clone() {
        TableMap tableMap = TableMap.of();
        dataMap.entrySet().stream().forEach(e -> tableMap.put(e.getKey(), e.getValue().clone()));
        return tableMap;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(dataMap);
    }
}
