package com.simonalong.neo;

import static com.simonalong.neo.NeoConstant.DEFAULT_TABLE;

import com.alibaba.fastjson.JSON;
import com.simonalong.neo.annotation.Column;
import com.simonalong.neo.db.AliasParser;
import com.simonalong.neo.db.TimeDateConverter;
import com.simonalong.neo.exception.NeoMapChgException;
import com.simonalong.neo.exception.NumberOfValueException;
import com.simonalong.neo.exception.ParameterNullException;
import com.simonalong.neo.util.ObjectUtil;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhouzhenyong
 * @since 2019/3/12 下午12:46
 */
@Slf4j
public class NeoMap implements Map<String, Object>, Cloneable, Serializable {

    private static final Integer KV_NUM = 2;
    private ConcurrentSkipListMap<String, EntryValue> dataMap;
    /**
     * 全局的命名转换，默认不转换
     */
    private static NamingChg globalNaming = NamingChg.DEFAULT;
    /**
     * 本次的默认转换规则
     */
    @Setter
    @Accessors(chain = true)
    private NamingChg namingChg = NamingChg.DEFAULT;

    public NeoMap() {
        dataMap = new ConcurrentSkipListMap<>();
    }

    /**
     * 设置全局名称转换字符，请注意，该转换会对所有NeoMap生效
     *
     * @param namingChg 转换类型
     */
    public static void setDefaultNamingChg(NamingChg namingChg) {
        globalNaming = namingChg;
    }

    /**
     * 通过key-value-key-value生成
     *
     * @param kvs 参数是通过key-value-key-value等等这种
     * @return 生成的map数据
     */
    public static NeoMap of(Object... kvs) {
        if (kvs.length % KV_NUM != 0) {
            throw new NumberOfValueException("参数请使用：key,value,key,value...这种参数格式");
        }

        NeoMap neoMap = new NeoMap();
        for (int i = 0; i < kvs.length; i += KV_NUM) {
            if (null == kvs[i]) {
                throw new ParameterNullException("NeoMap.of()中的参数不可为null");
            }
            neoMap.put((String) kvs[i], kvs[i + 1]);
        }
        return neoMap;
    }

    /**
     * 将多个NeoMap中的value集合起来
     *
     * @param maps 多个NeoMap
     * @return 多个NeoMap的Values集合
     */
    public static List<Object> values(NeoMap... maps) {
        List<Object> valueList = new ArrayList<>();
        if (null != maps && maps.length > 0) {
            Stream.of(maps).forEach(m -> valueList.addAll(m.values()));
        }
        return valueList;
    }

    public static NeoMap fromMap(Map<String, ?> dataMap) {
        NeoMap data = NeoMap.of();
        if (null == dataMap) {
            return data;
        }
        data.putAll(dataMap);
        return data;
    }

    /**
     * 对象转换为NeoMap
     *
     * <p>转换规则为默认不转换
     *
     * @param object 待转换对象
     * @return 转换之后的NeoMap
     */
    public static NeoMap from(Object object) {
        return from(object, NamingChg.DEFAULT, new ArrayList<>(), new ArrayList<>());
    }

    /**
     * 对象转换为NeoMap
     *
     * @param object 待转换对象
     * @param namingChg 转换规则
     * @return 转换之后的NeoMap
     */
    public static NeoMap from(Object object, NamingChg namingChg) {
        return from(object, namingChg, new ArrayList<>(), new ArrayList<>());
    }

    /**
     * 根据指定的一些列转换为NeoMap对象，该函数同函数 fromInclude，只是为了方便命名的统一
     *
     * @param object 待转换对象
     * @param columnsNames 对象的属性名列表
     * @return 转换之后的NeoMap
     */
    public static NeoMap from(Object object, String... columnsNames) {
        return from(object, NamingChg.DEFAULT, Arrays.asList(columnsNames), new ArrayList<>());
    }

    public static NeoMap from(Object object, Columns columns) {
        if (Columns.isEmpty(columns)) {
            return from(object);
        }
        return from(object, columns.getFieldSets().toArray(new String[]{}));
    }

    public static NeoMap from(Object object, Columns columns, NamingChg namingChg) {
        if (Columns.isEmpty(columns)) {
            return from(object, namingChg);
        }
        return from(object, namingChg, new ArrayList<>(columns.getFieldSets()), new ArrayList<>());
    }

    /**
     * 指定包括的属性进行对象转换为NeoMap
     *
     * @param object 待转换对象
     * @param fields 对象的属性名列表
     * @return 转换之后的NeoMap
     */
    public static NeoMap fromInclude(Object object, String... fields) {
        return from(object, NamingChg.DEFAULT, Arrays.asList(fields), new ArrayList<>());
    }

    /**
     * 指定包括的属性进行对象转换为NeoMap
     *
     * @param object 待转换对象
     * @param fields 对象的属性名列表
     * @return 转换之后的NeoMap
     */
    public static NeoMap fromInclude(Object object, NamingChg namingChg, String... fields) {
        return from(object, NamingChg.DEFAULT, Arrays.asList(fields), new ArrayList<>());
    }

    /**
     * 指定排除的属性进行对象转换为NeoMap
     *
     * @param object 待转换对象
     * @param fields 排除的对象的属性名列表
     * @return 转换之后的NeoMap
     */
    public static NeoMap fromExclude(Object object, String... fields) {
        return from(object, NamingChg.DEFAULT, new ArrayList<>(), Arrays.asList(fields));
    }

    /**
     * 指定排除的属性进行对象转换为NeoMap
     *
     * @param object 待转换对象
     * @param namingChg 转换规则
     * @param fields 排除的对象的属性名列表
     * @return 转换之后的NeoMap
     */
    public static NeoMap fromExclude(Object object, NamingChg namingChg, String... fields) {
        return from(object, namingChg, new ArrayList<>(), Arrays.asList(fields));
    }

    /**
     * 对象转换为NeoMap
     *
     * @param object 待转换的对象
     * @param namingChg 转换规则
     * @param inFieldList 包括的对象的属性名列表
     * @param exFieldList 排除的对象的属性名列表
     * @return 转换之后的NeoMap
     */
    @SuppressWarnings("unchecked")
    private static NeoMap from(Object object, NamingChg namingChg, List<String> inFieldList, List<String> exFieldList) {
        NeoMap neoMap = NeoMap.of().setNamingChg(namingChg);
        if (null == object) {
            return neoMap;
        }

        String valueResult = valueCheck(object);
        if (null != valueResult) {
            throw new NeoMapChgException("object 不是普通对象，转换失败：" + valueResult);
        }

        if (Map.class.isAssignableFrom(object.getClass())) {
            return neoMap.append(Map.class.cast(object));
        }
        return innerFrom(neoMap, object, inFieldList, exFieldList);
    }

    /**
     * 将其他的map的key进行转换
     * @param sourceMap 待转换的数据
     * @param namingChg 转换方式
     * @return 转换之后的数据，比如key：dataName 到 data_name
     */
    public static NeoMap fromMap(NeoMap sourceMap, NamingChg namingChg) {
        NeoMap targetMap = NeoMap.of();
        sourceMap.stream().forEach(c -> targetMap.putIfAbsent(namingChg.smallCamelToOther(c.getKey()), c.getValue()));
        return targetMap;
    }

    /**
     * 判断对象是否是普通对象，若是枚举、数组和集合则返回具体的文案，否则返回null
     *
     * @param value 待检查的值
     * @return null：普通对象；文本：非普通对象
     */
    @SuppressWarnings("unchecked")
    private static String valueCheck(Object value){
        Class vClass = value.getClass();
        if(vClass.isEnum()){
            return "对象是枚举类型";
        }

        if(vClass.isArray()){
            return "对象是数组类型";
        }

        if (Collection.class.isAssignableFrom(vClass)) {
            return "对象是集合类型";
        }

        return null;
    }

    /**
     * 对象向NeoMap转换，排除一些属性，包含一些数据
     *
     * @param neoMap 待接收数据的NeoMap
     * @param object 待转换的对象
     * @param inFieldList 需要转换的属性列表
     * @param exFieldList 不需要转换的属性列表
     * @return 转换后的NeoMap
     */
    private static NeoMap innerFrom(NeoMap neoMap, Object object, List<String> inFieldList, List<String> exFieldList) {
        Field[] fields = object.getClass().getDeclaredFields();
        if (fields.length != 0) {
            Stream.of(fields).filter(f -> {
                if (!inFieldList.isEmpty()) {
                    return inFieldList.contains(f.getName());
                }
                if (!exFieldList.isEmpty()) {
                    return !exFieldList.contains(f.getName());
                }
                return true;
            }).forEach(f -> {
                f.setAccessible(true);
                try {
                    Object value = TimeDateConverter.entityTimeToLong(f.get(object));
                    if (null != value) {
                        neoMap.putIfAbsent(neoMap.namingChg(f), value);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            });
        }
        return neoMap;
    }

    public static <T> List<T> asArray(List<NeoMap> neoMaps, Class<T> tClass) {
        if (null == neoMaps || neoMaps.isEmpty()) {
            return new ArrayList<>();
        }

        return neoMaps.stream().map(m -> m.as(tClass)).collect(Collectors.toList());
    }

    public static <T> List<T> asArray(List<NeoMap> neoMaps, NamingChg namingChg, Class<T> tClass) {
        if (null == neoMaps || neoMaps.isEmpty()) {
            return new ArrayList<>();
        }

        return neoMaps.stream().map(m -> m.setNamingChg(namingChg).as(tClass)).collect(Collectors.toList());
    }

    public static <T> List<NeoMap> fromArray(List<T> dataList, Columns columns) {
        if (null == dataList || dataList.isEmpty()) {
            return new ArrayList<>();
        }
        return dataList.stream().map(m -> NeoMap.from(m, columns)).collect(Collectors.toList());
    }

    public static <T> List<NeoMap> fromArray(List<T> dataList) {
        return fromArray(dataList, NamingChg.UNDERLINE, null);
    }

    public static <T> List<NeoMap> fromArray(List<T> dataList, NamingChg namingChg, Columns columns) {
        if (null == dataList || dataList.isEmpty()) {
            return new ArrayList<>();
        }
        return dataList.stream().map(m -> NeoMap.from(m, columns, namingChg)).collect(Collectors.toList());
    }

    public static <T> List<NeoMap> fromArray(List<T> dataList, NamingChg namingChg) {
        return fromArray(dataList, namingChg, null);
    }

    /**
     * 将数据库字符转换为对应的属性变量规范字符
     *
     * @param source 源字符串
     * @param namingChg 转换规则
     * @return 转换之后的字符
     */
    public static String dbToJavaStr(String source, NamingChg namingChg) {
        return namingChg.otherToSmallCamel(source);
    }

    /**
     * 默认全局转换，小驼峰转换为其他类型
     *
     * {@code data_base_user <------> dataBaseUser }
     * {@code _data_base_user <------> dataBaseUser }
     * {@code data-db-user <------> dataBaseUser }
     *
     * @param source 源字符
     * @return 转换后的字符
     */
    public static String dbToJavaStr(String source) {
        return globalNaming.otherToSmallCamel(source);
    }

    public static boolean isEmpty(NeoMap neoMap) {
        return neoMap == null || neoMap.isEmpty();
    }

    public static boolean isEmpty(Collection<NeoMap> neoMaps) {
        return neoMaps == null || neoMaps.isEmpty() || neoMaps.stream().allMatch(Map::isEmpty);
    }

    /**
     * 设置对应的表的一些列
     * @param tableNameStr 表名
     * @param kvs 表中对应的列和值的对应
     * @return 拼接后的数据，比如：table1.`group`=ok, table1.`name`=kk, table2.`age`=123
     */
    public NeoMap table(String tableNameStr, Object... kvs){
        String tableName = AliasParser.getAlias(tableNameStr);
        NeoMap currentMap = NeoMap.of();
        if (kvs.length % KV_NUM != 0) {
            throw new NumberOfValueException("参数请使用：key,value,key,value...这种参数格式");
        }

        String orderByStr = "order by";
        for (int i = 0; i < kvs.length; i += KV_NUM) {
            if (null == kvs[i]) {
                throw new ParameterNullException("NeoMap.of()中的参数不可为null");
            }
            String key = (String) kvs[i];
            Object valueObj = kvs[i + 1];
            if (key.trim().equals(orderByStr) && valueObj instanceof String) {
                String filterValue = toOrderByValueStr(tableName, String.class.cast(valueObj));
                // 不包含order by对应的value，则设置转换的
                if (!containsKey(key)) {
                    currentMap.put(key, filterValue);
                } else {
                    // 已经包含过，则合并新的
                    String orderByValue = String.class.cast(get(key));
                    currentMap.put(key, orderByValue + ", " + filterValue);
                }
            } else {
                currentMap.put(toColumnStr(tableName, (String) kvs[i]), kvs[i + 1]);
            }
        }

        return append(currentMap);
    }

    /**
     * 将字段转变为sql中的字段名，即添加前后的引号``
     * @param key 表的列名
     * @return sql中对应的表的字段名
     */
    private String toColumnStr(String tableName, String key){
        if (null != tableName && !"".equals(tableName)) {
            return tableName + "." + key + "";
        }
        return "`" + key + "`";
    }

    /**
     * 对order by后面的字段添加表前缀，比如：{@code name desc --> table1.name desc}
     * {@code name desc, group asc --> db.name desc, db.group asc}
     *
     * @param value order by 后面的字段
     * @return 添加表前缀
     */
    private String toOrderByValueStr(String tableName, String value){
        String comma = ",";
        String dom = ".";
        if(value.contains(comma)){
            return Stream.of(value.split(comma)).map(v->{
                v = v.trim();
                if (!v.startsWith(tableName) && !v.contains(dom)) {
                    return tableName + "." + v;
                }
                return v;
            }).collect(Collectors.joining(comma + " "));
        }
        if (!value.startsWith(tableName) && !value.contains(dom)) {
            return tableName + "." + value;
        }
        return value;
    }

    /**
     * key的命名风格从其他转到小驼峰
     *
     * @param namingChg 转换类型
     * @return 转换后的类型
     */
    public NeoMap keyChgToSmallCamelFrom(NamingChg namingChg){
        NeoMap neoMap = NeoMap.of();
        stream().forEach(e -> neoMap.put(namingChg.otherToSmallCamel(e.getKey()), e.getValue()));
        return neoMap;
    }

    /**
     * key的命名风格从小驼峰转到其他
     *
     * @param namingChg 转换类型
     * @return 转换后的类型
     */
    public NeoMap keyChgFromSmallCamelTo(NamingChg namingChg){
        NeoMap neoMap = NeoMap.of();
        stream().forEach(e -> neoMap.put(namingChg.smallCamelToOther(e.getKey()), e.getValue()));
        return neoMap;
    }

    /**
     * 给所有的key设置前缀
     *
     * @param preFix 前缀
     * @return 所有的key替换之后的NeoMap
     */
    public NeoMap setKeyPre(String preFix) {
        NeoMap neoMap = NeoMap.of();
        stream().forEach(e -> neoMap.put(preFix + e.getKey(), e.getValue()));
        return neoMap;
    }

    public <T> T as(Class<T> tClass, NamingChg namingChg) {
        this.setNamingChg(namingChg);
        return as(tClass);
    }

    /**
     * NeoMap 转化为实体数据，其中key就是对应的属性
     *
     * @param tClass 目标类的Class
     * @param <T> 目标类的类型
     * @return 目标类的实体对象
     */
    @SuppressWarnings("unchecked")
    public <T> T as(Class<T> tClass) {
        if (dataMap.isEmpty()) {
            return null;
        }

        if (NeoMap.class.isAssignableFrom(tClass)) {
            return (T) this;
        }
        T t = null;
        try {
            t = tClass.newInstance();
            Field[] fields = tClass.getDeclaredFields();
            if (fields.length != 0) {
                T finalT = t;
                Stream.of(fields).forEach(f -> {
                    f.setAccessible(true);
                    try {
                        Object value = getValue(f);
                        if (null != value) {
                            f.set(finalT, value);
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return t;
    }

    private Object getValue(Field field) {
        String key = namingChg(field);
        Object value = toEntityValue(field, key);

        Class<?> fieldClass = field.getType();
        return ObjectUtil.cast(fieldClass, TimeDateConverter.valueToEntityTime(fieldClass, value));
    }

    /**
     * NeoMap中的value向实体类型转换，先经过时间转换器，对于是时间类型的则转换为事件类型，对于非时间类型的，则保持，然后再经过兼容类型
     *
     * @param f 属性
     * @param key 类型转换后的名字
     * @return 经过转换之后的实体中的值
     */
    private Object toEntityValue(Field f, String key) {
        Class<?> fieldClass = f.getType();
        Object value = get(key);
        if (value instanceof EntryValue) {
            Column column = f.getDeclaredAnnotation(Column.class);
            if (null != column) {
                String tableName = column.table();
                return EntryValue.class.cast(value).getNodeList().stream()
                    .filter(n -> n.getKey().equals(tableName))
                    .map(Node::getValue)
                    .findFirst().orElse(null);
            }
        }
        return ObjectUtil.cast(fieldClass, TimeDateConverter.valueToEntityTime(fieldClass, value));
    }

    /**
     * 只要固定的几个列值
     *
     * @param columns 具体的列
     * @return 新的map结构
     */
    public NeoMap assign(Columns columns) {
        if (Columns.isEmpty(columns)) {
            return this;
        }
        NeoMap neoMap = NeoMap.of();
        columns.stream().forEach(f -> {
            if (containsKey(f)) {
                neoMap.put(f, get(f));
            }
        });
        return neoMap;
    }

    /**
     * 获取指定的一些列，得到新的数据
     * {@code {"a":12, "b":"ok"} ---->  {"a":12}}
     * @param keys 要获取的key，比如上面的a
     * @return 返回包含指定key对应的map
     */
    public NeoMap assign(String... keys){
        return assign(Columns.of(keys));
    }

    /**
     * 排除指定的列
     * @param columns 其中的列值为map中的key
     * @return 排除指定列后的map
     */
    public NeoMap assignExcept(Columns columns) {
        if (Columns.isEmpty(columns)) {
            return this;
        }
        NeoMap result = NeoMap.of();
        this.stream().forEach(f -> {
            if (!columns.contains(f.getKey())) {
                result.put(f.getKey(), f.getValue());
            }
        });
        return result;
    }

    /**
     * 排除固定的列
     *
     * @param keys map中的指定的key
     * @return 新的map数据
     */
    public NeoMap assignExcept(String... keys){
        return assignExcept(Columns.of(keys));
    }

    /**
     * 对NeoMap中的key进行转换
     *
     * @param keys 新旧key的映射，key-value-key-value...的形式，比如：a, a1, b, b1
     * @return key转换之后的NeoMap，比如：{@code a=ok, b=name} 到 {@code a1=ok, b1=name}
     */
    @SuppressWarnings("confusing")
    public NeoMap keyConvert(String... keys) {
        NeoMap keyChgMap = NeoMap.of(Arrays.asList(keys).toArray());
        NeoMap neoMap = NeoMap.fromMap(this, NamingChg.DEFAULT);
        keyChgMap.stream().forEach(e -> {
            String oldKey = e.getKey();
            String newKey = (String) e.getValue();
            if (neoMap.containsKey(oldKey)) {
                neoMap.put(newKey, neoMap.get(oldKey));
                neoMap.remove(oldKey);
            }
        });
        return neoMap;
    }

    public NeoMap append(Map<String, ?> neoMap) {
        this.putAll(neoMap);
        return this;
    }

    public NeoMap append(NeoMap neoMap) {
        this.putAll(neoMap.getDataMap());
        return this;
    }

    public NeoMap append(String key, Object value) {
        this.put(key, value);
        return this;
    }

    public NeoMap append(String tableName, String key, Object value) {
        this.put(tableName, key, value);
        return this;
    }

    public NeoMap delete(String key){
        this.remove(key);
        return this;
    }

    public Stream<Entry<String, Object>> stream() {
        return dataMap.entrySet().stream()
            .flatMap(d -> d.getValue().getNodeList()
                .stream()
                .map(n-> new Node<>(d.getKey(), n.getValue()))
            );
    }

    public Stream<String> keyStream() {
        return dataMap.keySet().stream();
    }

    public Stream<Object> valueStream() {
        return dataMap.values().stream().flatMap(d-> d.getNodeList().stream().map(Node::getValue));
    }

    /**
     * 根据传入的类进行对应类型的获取
     *
     * @param tClass 目标值的class
     * @param key map的key
     * @param <T> 目标值的类型
     * @return 目标值
     */
    public <T> T get(Class<T> tClass, String key){
        return ObjectUtil.cast(tClass, get(key));
    }

    /**
     * 根据传入的类进行对应类型的获取，若没有获取到，则返回默认值
     *
     * @param tClass 目标的class
     * @param key map的key
     * @param defaultValue 默认值
     * @param <T> 目标的类型
     * @return 目标值
     */
    public <T> T get(Class<T> tClass, String key, T defaultValue){
        T result = get(tClass, key);
        return (null == result) ? defaultValue : result;
    }

    /**
     * 获取字符
     *
     * @param key map的key
     * @return 字符，如果是字符串，则返回字符串的第一个字符
     */
    public Character getChar(String key){
        return ObjectUtil.toChar(get(key));
    }

    public Character getChar(String key, Character defaultValue) {
        Character result = getChar(key);
        return (null == result) ? defaultValue : result;
    }

    /**
     * 返回值为String类型的值
     *
     * @param key map的key
     * @return String类型的值
     */
    public String getString(String key){
        return ObjectUtil.toStr(get(key));
    }

    public String getString(String key, String defaultValue){
        String result = getString(key);
        return (null == result) ? defaultValue : result;
    }

    /**
     * 获取值类型为Boolean的值
     * @param key map中对应的key
     * @return 若值为true或者TRUE，则返回true，否则其他任何值都返回false，包括false和null
     */
    public Boolean getBoolean(String key) {
        return ObjectUtil.toBoolean(get(key));
    }

    public Boolean getBoolean(String key, Boolean defaultValue) {
        Boolean result = getBoolean(key);
        return (null == result) ? defaultValue : result;
    }

    public Byte getByte(String key){
        return ObjectUtil.toByte(get(key));
    }

    public Byte getByte(String key, Byte defaultValue) {
        Byte result = getByte(key);
        return (null == result) ? defaultValue : result;
    }

    public Short getShort(String key){
        return ObjectUtil.toShort(get(key));
    }

    public Short getShort(String key, Short defaultValue) {
        Short result = getShort(key);
        return (null == result) ? defaultValue : result;
    }

    public Integer getInteger(String key){
        return ObjectUtil.toInt(get(key));
    }

    public Integer getInteger(String key, Integer defaultValue) {
        Integer result = getInteger(key);
        return (null == result) ? defaultValue : result;
    }

    public Long getLong(String key){
        return ObjectUtil.toLong(get(key));
    }

    public Long getLong(String key, Long defaultValue) {
        Long result = getLong(key);
        return (null == result) ? defaultValue : result;
    }

    public Double getDouble(String key){
        return ObjectUtil.toDouble(get(key));
    }

    public Double getDouble(String key, Double defaultValue) {
        Double result = getDouble(key);
        return (null == result) ? defaultValue : result;
    }

    public Float getFloat(String key){
        return ObjectUtil.toFloat(get(key));
    }

    public Float getFloat(String key, Float defaultValue) {
        Float result = getFloat(key);
        return (null == result) ? defaultValue : result;
    }

    /**
     * 获取value并转换为指定的类型的list
     *
     * @param tClass 目标对象的类，对于有些类型不是这个类的，只要能转换到这个类也是可以的，比如原先存的是{@code List<String>}，取的时候只要String可以转为Integer，那么这个tClass可以为Integer.class
     * @param key 目标值对应NeoMap中的key
     * @param <T> 目标对象类型
     * @return 集合类型
     */
    public <T> List<T> getList(Class<T> tClass, String key){
        return ObjectUtil.toList(get(key)).stream().map(r -> ObjectUtil.cast(tClass, r)).collect(Collectors.toList());
    }

    public <T> Set<T> getSet(Class<T> tClass, String key){
        return ObjectUtil.toSet(get(key)).stream().map(r -> ObjectUtil.cast(tClass, r)).collect(Collectors.toSet());
    }

    /**
     * 将值获取为NeoMap，对于value为普通对象的也可以用该方法
     *
     * 注意：只有value为普通对象的时候才会获取成功，对于枚举，集合，数组这些都会转换失败
     * @param key key值
     * @return 返回转换的NeoMap
     */
    public NeoMap getNeoMap(String key){
        return ObjectUtil.toNeoMap(get(key));
    }

    /**
     * 这里根据命名订阅，如果用户自定义表中存在映射，则使用该映射，否则用设置的命名映射规则
     * 属性名的字符转换
     * <p>有如下几种转换规则
     * <ul>
     *     <li>1.当前的类的属性有指定注解{@link Column}的，则按照该注解中的属性对应，否则走下面</li>
     *     <li>2.如果指定了本次转换规则，则按照本次转换，否则按照全局小驼峰下划线转换</li>
     * </ul>
     *
     */
    private String namingChg(Field field) {
        Column column = field.getDeclaredAnnotation(Column.class);
        if (null != column) {
            String columnName = column.value();
            String tableName = column.table();

            if (containsKey(columnName)){
                Node node = dataMap.get(columnName).getNodeList().stream().filter(n->n.getKey().equals(tableName)).findFirst().orElse(null);
                if(null != node){
                    return columnName;
                }
            }
        }

        if (!namingChg.equals(NamingChg.DEFAULT)) {
            return namingChg.smallCamelToOther(field.getName());
        }

        return globalNaming.smallCamelToOther(field.getName());
    }

    public Map<String, EntryValue> getDataMap() {
        return dataMap;
    }

    /**
     * 指定value值类型的情况下获取dataMap
     * @param tClass value的目标类
     * @param <T> 类型
     * @return 转换后的类型
     */
    public <T> Map<String, T> getDataMapAssignValueType(Class<T> tClass) {
        return stream().collect(Collectors.toMap(Entry::getKey, e -> ObjectUtil.cast(tClass, e.getValue())));
    }

    @Override
    public int size() {
        return dataMap.size();
    }

    @Override
    public int hashCode(){
        return dataMap.hashCode();
    }

    @Override
    public boolean isEmpty() {
        return dataMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        if (null == key) {
            return false;
        }
        return dataMap.containsKey(key);
    }

    /**
     * 包含所有的key
     * @param keys keys的列表
     * @return true： 包含所有的值，false，没有包含全部的key
     */
    public boolean containsKeys(String... keys){
        return Stream.of(keys).allMatch(this::containsKey);
    }

    @Override
    public boolean containsValue(Object value) {
        return dataMap.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        Object result = dataMap.get(key);
        if (null != result) {
            List<Node<String, Object>> dataList = EntryValue.class.cast(result).getNodeList();
            if (dataList.size() == 1) {
                return dataList.get(0).getValue();
            }else{
                return result;
            }
        }
        return null;
    }

    /**
     * 对所有的放到NeoMap中的值，如果是时间类型，则就要进行转换成Long类型
     * @param key key
     * @param value value
     * @return 原value
     */
    @Override
    public Object put(String key, Object value) {
        return put(DEFAULT_TABLE, key, value);
    }

    public Object put(String key, Object value, Boolean timeTypeToLong) {
        return put(DEFAULT_TABLE, key, value, timeTypeToLong);
    }

    /**
     * 添加含有指定表的列的数据
     * @param key key
     * @param tableName 表名
     * @param value 值
     * @return value数据
     */
    public Object put(String tableName, String key, Object value) {
        return put(tableName, key, value, true);
    }

    /**
     * 是否进行时间类型到Long类型的转换来放置数据
     * @param key key
     * @param value value
     * @param timeTypeToLong 是否时间类型转Long：true：转换，false: 不转换
     * @return value的值
     */
    public Object put(String tableName, String key, Object value, Boolean timeTypeToLong) {
        if (null != value) {
            if (timeTypeToLong) {
                doPut(tableName, key, TimeDateConverter.entityTimeToLong(value));
            } else {
                doPut(tableName, key, value);
            }
        }
        return value;
    }

    private void doPut(String tableName, String key, Object value) {
        if (value instanceof EntryValue) {
            dataMap.compute(key, (k, v) -> {
                if (null == v) {
                    return EntryValue.class.cast(value);
                } else {
                    Node<String, Object> node = v.getNodeList().stream().filter(n -> n.getKey().equals(tableName)).findFirst().orElse(null);
                    if (null == node) {
                        v.addTable(tableName, value);
                    } else {
                        node.setValue(value);
                    }
                    return v;
                }
            });
        } else {
            dataMap.compute(key, (k, v) -> {
                if (null == v) {
                    return new EntryValue(tableName, value);
                } else {
                    Node<String, Object> node = v.getNodeList().stream().filter(n -> n.getKey().equals(tableName)).findFirst().orElse(null);
                    if (null == node) {
                        v.addTable(tableName, value);
                    } else {
                        node.setValue(value);
                    }
                    return v;
                }
            });
        }
    }

    @Override
    public Object remove(Object key) {
        return dataMap.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
        if (null == m || m.isEmpty()) {
            return;
        }

        if(m instanceof NeoMap){
            getDataMap().putAll(NeoMap.class.cast(m).getDataMap());
        }else{
            m.forEach((key, value) -> put(key, TimeDateConverter.entityTimeToLong(value)));
        }
    }

    @Override
    public void clear() {
        dataMap.clear();
    }

    @Override
    public Set<String> keySet() {
        return dataMap.keySet();
    }

    @Override
    public Collection<Object> values() {
        return dataMap.entrySet().stream()
            .flatMap(t -> t.getValue().getNodeList().stream().map(Node::getValue))
            .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return dataMap.entrySet().stream()
            .flatMap(t -> t.getValue().getNodeList().stream().map(v->new Node<>(t.getKey(), v.getValue())))
            .collect(Collectors.toSet());
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof NeoMap) {
            Map<String, EntryValue> d1 = this.clone().getDataMap();
            Map<String, EntryValue> d2 = NeoMap.class.cast(object).getDataMap();
            return d1.equals(d2);
        }
        return false;
    }

    @Override
    public String toString() {
        Map<String, ?> map = dataMap.entrySet().stream().flatMap(e -> {
            List<Node<String, Object>> dataList = e.getValue().nodeList;
            if (1 == dataList.size()) {
                return Stream.of(new Node<>(e.getKey(), dataList.get(0).getValue()));
            } else {
                return dataList.stream().map(d -> new Node<>(e.getKey(), d.getValue()));
            }
        }).collect(Collectors.toMap(Node::getKey, Node::getValue));
        return JSON.toJSONString(map);
    }

    /**
     * 这里采用深拷贝，浅拷贝存在集合并发修改问题
     */
    @Override
    public NeoMap clone() {
        NeoMap neoMap = NeoMap.of();
        dataMap.forEach((key, value) -> neoMap.put(key, value.clone()));
        return neoMap;
    }

    public enum NamingChg {
        /**
         * 不转换
         */
        DEFAULT(t -> t, t -> t),
        /**
         * 小驼峰到大驼峰 {@code dataBaseUser <------> DateBaseUser }
         */
        BIGCAMEL(StringConverter::bigCamel, StringConverter::bigCamelToSmallCamel),
        /**
         * 小驼峰到下划线 {@code dataBaseUser <------> data_base_user }
         */
        UNDERLINE(StringConverter::underLine, StringConverter::underLineToSmallCamel),
        /**
         * 小驼峰到前下划线 {@code dataBaseUser <------> _data_base_user }
         */
        PREUNDER(StringConverter::preUnder, StringConverter::underLineToSmallCamel),
        /**
         * 小驼峰到后下划线 {@code dataBaseUser <------> data_base_user_ }
         */
        POSTUNDER(StringConverter::postUnder, StringConverter::underLineToSmallCamel),
        /**
         * 小驼峰到前后下划线 {@code dataBaseUser <------> _data_base_user_ }
         */
        PREPOSTUNDER(StringConverter::prePostUnder, StringConverter::underLineToSmallCamel),
        /**
         * 小驼峰到中划线 {@code dataBaseUser <------> data-db-user }
         */
        MIDDLELINE(StringConverter::middleLine, StringConverter::middleLineToSmallCamel),
        /**
         * 小驼峰到大写下划线 {@code dataBaseUser <------> DATA_BASE_USER }
         */
        UPPERUNER(StringConverter::upperUnder, StringConverter::upperUnderToSmallCamel),
        /**
         * 小驼峰到大写中划线 {@code dataBaseUser <------> DATA-BASE-USER }
         */
        UPPERMIDDLE(StringConverter::upperUnderMiddle, StringConverter::upperUnderMiddleToSmallCamel);

        /**
         * 用于名字的转换
         */
        private Function<String, String> smallCamelToOther;
        private Function<String, String> otherToSmallCamel;

        NamingChg(Function<String, String> smallCamelToOther, Function<String, String> otherToSmallCamel) {
            this.smallCamelToOther = smallCamelToOther;
            this.otherToSmallCamel = otherToSmallCamel;
        }

        /**
         * 小驼峰类型到其他类型的转换
         *
         * @param data 源数据
         * @return 转换后的数据
         */
        public String smallCamelToOther(String data) {
            return smallCamelToOther.apply(data);
        }

        /**
         * 其他类型到小驼峰转换
         *
         * @param data 源数据
         * @return 转换后的数据
         */
        public String otherToSmallCamel(String data) {
            return otherToSmallCamel.apply(data);
        }
    }

    @EqualsAndHashCode
    static class Node<K,V> implements Map.Entry<K,V> {
        final K key;
        V value;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            this.value = value;
            return value;
        }
    }

    @NoArgsConstructor
    @EqualsAndHashCode(of = "nodeList")
    public static class EntryValue{
        @Getter
        private List<Node<String, Object>> nodeList = new ArrayList<>();

        public EntryValue(Object object){
            nodeList.add(new Node<>(DEFAULT_TABLE, object));
        }

        public EntryValue(String tableName, Object object){
            nodeList.add(new Node<>(tableName, object));
        }

        public EntryValue addTable(String tableName, Object object){
            nodeList.add(new Node<>(tableName, object));
            return this;
        }

        @Override
        public Object clone() {
            EntryValue clone = new EntryValue();
            nodeList.forEach(n-> clone.addTable(n.getKey(), n.getValue()));
            return clone;
        }
    }
}
