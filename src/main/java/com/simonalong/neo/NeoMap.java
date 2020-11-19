package com.simonalong.neo;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.simonalong.neo.annotation.Column;
import com.simonalong.neo.db.TimeDateConverter;
import com.simonalong.neo.exception.NeoException;
import com.simonalong.neo.exception.NeoMapChgException;
import com.simonalong.neo.exception.NumberOfValueException;
import com.simonalong.neo.exception.ParameterNullException;
import com.simonalong.neo.express.BaseOperate;
import com.simonalong.neo.express.Operate;
import com.simonalong.neo.util.ClassUtil;
import com.simonalong.neo.util.ObjectUtil;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import static com.simonalong.neo.util.LogicOperateUtil.*;

/**
 * 实现map中的所有功能
 *
 * <p>
 *     此外新增更多功能
 *     <ul>
 *         <li>1.各种静态读取方法</li>
 *         <li>2.跟实体的转换</li>
 *         <li>3.不同类型的值获取</li>
 *         <li>4.value可以为null</li>
 *         <li>5.条件过滤</li>
 *     </ul>
 * @author zhouzhenyong
 * @since 2019/3/12 下午12:46
 */
@Slf4j
@NoArgsConstructor
public class NeoMap extends BaseOperate implements Map<String, Object>, Cloneable, Serializable {

    private static final Integer KV_NUM = 2;
    /**
     * key为对应的表，value：key为数据对应的key，value为属性对应的值
     */
    private Map<String, Object> dataMap = new ConcurrentHashMap<>();
    @Getter
    private Set<String> nullValueKeySet = new HashSet<>();
    /**
     * 只是支持value为空
     */
    @Setter
    @Getter
    @Accessors(chain = true)
    private Boolean supportValueNull = false;
    /**
     * 全局的命名转换，请注意，该转换会对所有NeoMap生效，默认不转换
     */
    @Getter
    @Setter
    private static NamingChg globalNaming = NamingChg.UNDERLINE;
    /**
     * 本次的默认转换规则
     */
    @Setter
    @Getter
    @Accessors(chain = true)
    private NamingChg namingChg = null;

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

    @SuppressWarnings("unchecked")
    public static NeoMap fromMap(Map<String, ?> dataMap) {
        NeoMap map = NeoMap.of();
        if (null == dataMap) {
            return map;
        }

        map.putAll(dataMap);
        return map;
    }

    /**
     * 对象转换为NeoMap
     *
     * <p>转换规则为默认不转换，放在默认表中
     *
     * @param object 待转换对象
     * @return 转换之后的NeoMap
     */
    public static NeoMap from(Object object) {
        return from(object, NamingChg.UNDERLINE, new ArrayList<>(), new ArrayList<>(), false);
    }

    public static NeoMap from(Object object, Boolean supportValueNull) {
        return from(object, NamingChg.UNDERLINE, new ArrayList<>(), new ArrayList<>(), supportValueNull);
    }

    /**
     * 对象转换为NeoMap
     *
     * @param object 待转换对象
     * @param namingChg 转换规则
     * @return 转换之后的NeoMap
     */
    public static NeoMap from(Object object, NamingChg namingChg) {
        return from(object, namingChg, new ArrayList<>(), new ArrayList<>(), false);
    }

    public static NeoMap from(Object object, Boolean supportValueNull, NamingChg namingChg) {
        return from(object, namingChg, new ArrayList<>(), new ArrayList<>(), supportValueNull);
    }

    /**
     * 根据指定的一些列转换为NeoMap对象，该函数同函数 fromInclude，只是为了方便命名的统一
     *
     * @param object 待转换对象
     * @param columnsNames 对象的属性名列表
     * @return 转换之后的NeoMap
     */
    public static NeoMap from(Object object, String... columnsNames) {
        return from(object, NamingChg.UNDERLINE, Arrays.asList(columnsNames), new ArrayList<>(), false);
    }

    public static NeoMap from(Object object, Boolean supportValueNull, String... columnsNames) {
        return from(object, NamingChg.UNDERLINE, Arrays.asList(columnsNames), new ArrayList<>(), supportValueNull);
    }

    private static NeoMap from(Object object, Columns columns) {
        if (Columns.isEmpty(columns)) {
            return from(object);
        }
        return from(object, columns.getMetaFieldSets().toArray(new String[]{}));
    }

    private static NeoMap from(Object object, Columns columns, Boolean supportValueNull) {
        if (Columns.isEmpty(columns)) {
            return from(object);
        }
        return from(object, supportValueNull, columns.getMetaFieldSets().toArray(new String[]{}));
    }

    public static NeoMap from(Object object, Columns columns, NamingChg namingChg) {
        if (Columns.isEmpty(columns)) {
            return from(object, namingChg);
        }
        return from(object, namingChg, new ArrayList<>(columns.getMetaFieldSets()), new ArrayList<>(), false);
    }

    public static NeoMap from(Object object, Columns columns, NamingChg namingChg, Boolean supportValueNull) {
        if (Columns.isEmpty(columns)) {
            return from(object, namingChg);
        }
        return from(object, namingChg, new ArrayList<>(columns.getMetaFieldSets()), new ArrayList<>(), supportValueNull);
    }

    /**
     * 指定包括的属性进行对象转换为NeoMap
     *
     * @param object 待转换对象
     * @param fields 对象的属性名列表
     * @return 转换之后的NeoMap
     */
    public static NeoMap fromInclude(Object object, String... fields) {
        return from(object, NamingChg.UNDERLINE, Arrays.asList(fields), new ArrayList<>(), false);
    }

    /**
     * 指定包括的属性进行对象转换为NeoMap
     *
     * @param object 待转换对象
     * @param namingChg 命名转换风格
     * @param fields 对象的属性名列表
     * @return 转换之后的NeoMap
     */
    public static NeoMap fromInclude(Object object, NamingChg namingChg, String... fields) {
        return from(object, namingChg, Arrays.asList(fields), new ArrayList<>(), false);
    }

    /**
     * 指定排除的属性进行对象转换为NeoMap
     *
     * @param object 待转换对象
     * @param fields 排除的对象的属性名列表
     * @return 转换之后的NeoMap
     */
    public static NeoMap fromExclude(Object object, String... fields) {
        return from(object, NamingChg.UNDERLINE, new ArrayList<>(), Arrays.asList(fields), false);
    }

    /**
     * 指定排除的属性进行对象转换为NeoMap
     *
     * @param object 待转换对象
     * @param namingChg 转换规则
     * @param fields 排除的对象的属性名列表
     * @return 转换之后的NeoMap
     */
    static NeoMap fromExclude(Object object, NamingChg namingChg, String... fields) {
        return from(object, namingChg, new ArrayList<>(), Arrays.asList(fields), false);
    }

    /**
     * 对象转换为NeoMap
     *
     * @param object 待转换的对象
     * @param namingChg 转换规则
     * @param inFieldList 包括的对象的属性名列表
     * @param exFieldList 排除的对象的属性名列表
     * @param supportValueNull 是否支持value为空
     * @return 转换之后的NeoMap
     */
    @SuppressWarnings("unchecked")
    public static NeoMap from(Object object, NamingChg namingChg, List<String> inFieldList, List<String> exFieldList, Boolean supportValueNull) {
        NeoMap neoMap = NeoMap.of().setNamingChg(namingChg).setSupportValueNull(supportValueNull);
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
     * 反解析数据
     * @param fastJsonString {@link com.alibaba.fastjson}的序列化字符串
     * @return 解析后的数据
     */
    public static NeoMap fromFastJsonStr(String fastJsonString) {
        NeoMap resultMap = NeoMap.of();
        if (null == fastJsonString || "".equals(fastJsonString)) {
            return resultMap;
        }
        resultMap.putAll(JSON.parseObject(fastJsonString));
        return resultMap;
    }

    /**
     * 反解析数据
     * @param gsonString {@link com.google.gson}的序列化字符串
     * @return 解析后的数据
     */
    @SuppressWarnings("unchecked")
    public static NeoMap fromGsonStr(String gsonString) {
        NeoMap resultMap = NeoMap.of();
        if (null == gsonString || "".equals(gsonString)) {
            return resultMap;
        }
        resultMap.putAll(new Gson().fromJson(gsonString, Map.class));
        return resultMap;
    }

    /**
     * 判断对象是否是普通对象，若是枚举、数组和集合则返回具体的文案，否则返回null
     *
     * @param value 待检查的值
     * @return null：普通对象；文本：非普通对象
     */
    @SuppressWarnings("unchecked")
    public static String valueCheck(Object value){
        Class vClass = value.getClass();

        if (vClass.isEnum()) {
            return "对象是枚举类型";
        }

        if (vClass.isArray()) {
            return "对象是数组类型";
        }

        if (ClassUtil.isBaseField(vClass)) {
            return "对象是基本类型";
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
                    neoMap.put(neoMap.namingChg(f, false), value);
                } catch (IllegalAccessException e) {
                    throw new NeoException(e);
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

    /**
     * 从实体集合转换为NeoMap集合
     * @param dataList 待转换的数据集合
     * @param columns 只要的列名
     * @param <T> 实体类型
     * @return NeoMap集合
     */
    public static <T> List<NeoMap> fromArray(List<T> dataList, Columns columns) {
        if (null == dataList || dataList.isEmpty()) {
            return new ArrayList<>();
        }
        return dataList.stream().map(m -> NeoMap.from(m, columns)).collect(Collectors.toList());
    }

    public static <T> List<NeoMap> fromArray(List<T> dataList) {
        return fromArray(dataList, NamingChg.UNDERLINE, null, false);
    }

    public static <T> List<NeoMap> fromArray(List<T> dataList, NamingChg namingChg, Columns columns) {
        if (null == dataList || dataList.isEmpty()) {
            return new ArrayList<>();
        }
        return dataList.stream().map(m -> NeoMap.from(m, columns, namingChg)).collect(Collectors.toList());
    }

    public static <T> List<NeoMap> fromArray(List<T> dataList, NamingChg namingChg) {
        return fromArray(dataList, namingChg, null, false);
    }

    public static <T> List<NeoMap> fromArray(List<T> dataList, Columns columns, Boolean supportValueNull) {
        if (null == dataList || dataList.isEmpty()) {
            return new ArrayList<>();
        }
        return dataList.stream().map(m -> NeoMap.from(m, columns, supportValueNull)).collect(Collectors.toList());
    }

    public static <T> List<NeoMap> fromArray(List<T> dataList, Boolean supportValueNull) {
        return fromArray(dataList, NamingChg.UNDERLINE, null, supportValueNull);
    }

    public static <T> List<NeoMap> fromArray(List<T> dataList, NamingChg namingChg, Columns columns, Boolean supportValueNull) {
        if (null == dataList || dataList.isEmpty()) {
            return new ArrayList<>();
        }
        return dataList.stream().map(m -> NeoMap.from(m, columns, namingChg, supportValueNull)).collect(Collectors.toList());
    }

    public static <T> List<NeoMap> fromArray(List<T> dataList, NamingChg namingChg, Boolean supportValueNull) {
        return fromArray(dataList, namingChg, null, supportValueNull);
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
        return neoMaps == null || neoMaps.isEmpty() || neoMaps.stream().allMatch(e->{
            if (null == e) {
                return true;
            }

            return e.isEmpty();
        });
    }

    public static boolean isUnEmpty(NeoMap neoMap) {
        return !isEmpty(neoMap);
    }

    public static boolean isUnEmpty(Collection<NeoMap> neoMaps) {
        return !isEmpty(neoMaps);
    }

    /**
     * 将NeoMap底层的ConcurrentHashMap类型结构转换为key有序的ConcurrentSkipListMap（有序的跳表结构）
     * @return 设定key顺序后的map
     */
    public NeoMap openSorted() {
        dataMap = new ConcurrentSkipListMap<>(dataMap);
        return this;
    }

    /**
     * 将NeoMap底层的ConcurrentSkipListMap（key有序的跳表结构）类型结构转换为有序的ConcurrentHashMap
     * @return 设定key无顺序后的map
     */
    public NeoMap closeSorted() {
        dataMap = new ConcurrentHashMap<>();
        return this;
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
        T t;
        try {
            Constructor<T> constructor = tClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            t = constructor.newInstance();
            Field[] fields = tClass.getDeclaredFields();
            if (fields.length != 0) {
                Stream.of(fields).forEach(f -> {
                    f.setAccessible(true);
                    try {
                        Object value = getValue(f);
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

    private Object getValue(Field field) {
        String key = namingChg(field, true);
        Class<?> fieldClass = field.getType();
        return ObjectUtil.cast(fieldClass, TimeDateConverter.valueToEntityTime(fieldClass, get(key)));
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
        columns.streamMeta().forEach(f -> {
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
        if (!containsKeys(keys)) {
            throw new NeoException("不包含key: " + Arrays.asList(keys));
        }
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
        NeoMap result = this.clone();
        this.stream().forEach(f -> {
            if (columns.contains(f.getKey())) {
                result.delete(f.getKey());
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

    public NeoMap append(Map<String, ?> neoMap) {
        this.putAll(neoMap);
        return this;
    }

    public NeoMap append(NeoMap neoMap) {
        this.getDataMap().putAll(neoMap.getDataMap());
        return this;
    }

    public NeoMap append(String key, Object value) {
        this.put(key, value);
        return this;
    }

    public NeoMap delete(String key){
        this.remove(key);
        return this;
    }

    public Stream<Entry<String, Object>> stream() {
        return entrySet().stream();
    }

    public Stream<String> keyStream() {
        return keySet().stream();
    }

    public Stream<Object> valueStream() {
        return values().stream();
    }

    /**
     * 获取第一个value
     *
     * <p>
     * 在不知道key情况下获取一个值
     * @return value
     */
    public Object getFirst() {
        Entry<String, Object> entry = dataMap.entrySet().stream().findFirst().orElse(null);
        if (null != entry) {
            return entry.getValue();
        }
        return null;
    }

    public <T> T getFirst(Class<T> tClass) {
        Object object = getFirst();
        if (null != object) {
            return ObjectUtil.cast(tClass, object);
        }
        return null;
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
     * 根据field返回属性对应的转换后的字符：比如用户自定义的，否则按照内定义的默认规则
     * 属性名的字符转换
     * <p>有如下几种转换规则
     * <ul>
     *     <li>1.当前的类的属性有指定注解{@link Column}的，则按照该注解中的属性对应，否则走下面</li>
     *     <li>2.如果指定了本次转换规则，则按照本次转换，否则按照全局小驼峰下划线转换</li>
     * </ul>
     * @param field 对应属性的key
     * @param checkContain 是否关心当前的key在map中存在
     * @return 转换后的属性名
     */
    private String namingChg(Field field, Boolean checkContain) {
        Column column = field.getDeclaredAnnotation(Column.class);
        if (null != column) {
            String columnName = column.value();
            if (checkContain) {
                if (containsKey(columnName)) {
                    return columnName;
                }
            } else {
                return columnName;
            }
        }

        if (null != namingChg) {
            return namingChg.smallCamelToOther(field.getName());
        }

        if (null != globalNaming) {
            return globalNaming.smallCamelToOther(field.getName());
        }

        return NamingChg.DEFAULT.smallCamelToOther(field.getName());
    }

    public Map<String, Object> getDataMap() {
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
        if (supportValueNull) {
            return dataMap.size() + nullValueKeySet.size();
        }
        return dataMap.size();
    }

    @Override
    public int hashCode() {
        if (supportValueNull) {
            return (dataMap.hashCode() + nullValueKeySet.hashCode()) / 2;
        }
        return dataMap.hashCode();
    }

    @Override
    public boolean isEmpty() {
        if (supportValueNull) {
            return dataMap.isEmpty() && nullValueKeySet.isEmpty();
        }
        return dataMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        if (null == key) {
            return false;
        }
        if (supportValueNull) {
            return dataMap.containsKey(key) || nullValueKeySet.contains(key);
        }
        return dataMap.containsKey(key);
    }

    /**
     * 包含所有的key
     * @param keys keys的列表
     * @return true： 包含所有的值，false：有某些key不包含
     */
    public boolean containsKeys(String... keys){
        return Stream.of(keys).allMatch(this::containsKey);
    }

    @Override
    public boolean containsValue(Object value) {
        if (supportValueNull && null == value) {
            return true;
        }
        return dataMap.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        return dataMap.get(key);
    }

    /**
     * 对所有的放到NeoMap中的值，如果是时间类型，则就要进行转换成Long类型
     * @param key key
     * @param value value
     * @return 原value
     */
    @Override
    public Object put(String key, Object value) {
        return put(key, value, true);
    }

    /**
     * 是否进行时间类型到Long类型的转换来放置数据
     * @param key key
     * @param value value
     * @param timeTypeToLong 是否时间类型转Long：true：转换，false: 不转换
     * @return value的值
     */
    public Object put(String key, Object value, Boolean timeTypeToLong) {
        if (null != value) {
            if (timeTypeToLong) {
                dataMap.put(key, TimeDateConverter.entityTimeToLong(value));
            } else {
                dataMap.put(key, value);
            }
        } else {
            if (supportValueNull) {
                nullValueKeySet.add(key);
            }
        }
        return value;
    }

    @Override
    public Object remove(Object key) {
        if (supportValueNull) {
            nullValueKeySet.remove(key);
        }
        return dataMap.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
        if (null == m || m.isEmpty()) {
            return;
        }

        if (m instanceof NeoMap) {
            NeoMap innerMap = (NeoMap) m;
            getDataMap().putAll(innerMap.getDataMap());
            getNullValueKeySet().addAll(innerMap.getNullValueKeySet());
        } else {
            m.forEach((key, value) -> {
                if (null == value) {
                    getNullValueKeySet().add(key);
                } else {
                    put(key, TimeDateConverter.entityTimeToLong(value));
                }
            });
        }
    }

    @Override
    public void clear() {
        dataMap.clear();
        nullValueKeySet.clear();
    }

    @SuppressWarnings("all")
    @Override
    public Set<String> keySet() {
        return entrySet().stream().map(Entry::getKey).collect(Collectors.toSet());
    }

    @SuppressWarnings("all")
    @Override
    public Collection<Object> values() {
        return entrySet().stream().map(Entry::getValue).collect(Collectors.toList());
    }

    /**
     * 这里需要将value为null的也添加进来
     *
     * @return entry集合
     */
    @Override
    public Set<Map.Entry<String, Object>> entrySet() {
        Set<Map.Entry<String, Object>> entrySet = new HashSet<>();
        entrySet.addAll(dataMap.entrySet().stream().map(e -> new NeoMap.Node(e.getKey(), e.getValue())).collect(Collectors.toSet()));
        entrySet.addAll(nullValueKeySet.stream().map(e -> new NeoMap.Node(e, null)).collect(Collectors.toSet()));
        return entrySet;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof NeoMap) {
            NeoMap innerObject = (NeoMap) object;
            if (dataMap.equals(innerObject.getDataMap())) {
                if (supportValueNull || innerObject.getSupportValueNull()) {
                    return nullValueKeySet.equals(innerObject.getNullValueKeySet());
                }
                return true;
            }
        }
        return false;
    }

    /**
     * NeoMap 打印按照json格式输出string
     * <p>
     *     NeoMap 中有时候也会放表和表中对应的列和对应的值，但是也会放普通的数据，这两种打印为了方便看，这里进行区分开
     * @return 数据对应的json格式字符串
     */
    @Override
    public String toString() {
        return dataMap.toString();
    }

    /**
     * fastJson格式的序列化字符
     * @return 字符串
     */
    public String toFastJsonString() {
        return JSON.toJSONString(dataMap);
    }

    /**
     * gson格式的序列化字符
     * @return 字符串
     */
    public String toGsonString() {
        return new Gson().toJson(dataMap);
    }

    /**
     * 这里采用深拷贝，浅拷贝存在集合并发修改问题
     */
    @SuppressWarnings("all")
    @Override
    public NeoMap clone() {
        NeoMap neoMap = NeoMap.of();
        neoMap.putAll(dataMap);
        neoMap.setNamingChg(namingChg);
        neoMap.setSupportValueNull(supportValueNull);
        neoMap.getNullValueKeySet().addAll(nullValueKeySet);
        return neoMap;
    }

    @Override
    public String generateOperate() {
        NeoQueue<Operate> operateQueue = NeoQueue.of();
        if (dataMap instanceof ConcurrentSkipListMap) {
            dataMap.forEach((key, value) -> operateQueue.push(AndEm(key, value)));
        } else {
            dataMap.forEach((key, value) -> operateQueue.offer(AndEm(key, value)));
        }

        Operate operate;
        StringBuilder stringBuilder = new StringBuilder();
        while ((operate = operateQueue.poll()) != null) {
            String operateStr = operate.generateOperate();
            stringBuilder.append(" and ").append(filterLogicHead(operateStr));
        }

        return stringBuilder.toString();
    }

    /**
     * 多个值中，只要有一个合法，则就合法，
     * @return true：合法，false：不合法
     */
    @Override
    public Boolean valueLegal() {
        Set<Map.Entry<String, Object>> entrySet = dataMap.entrySet();
        for (Entry<String, Object> stringObjectEntry : entrySet) {
            if(!ObjectUtil.isEmpty(stringObjectEntry.getValue())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public NeoQueue<Object> getValueQueue() {
        if (!valueLegal()) {
            return NeoQueue.of();
        }
        NeoQueue<Object> operateQueue = NeoQueue.of();
        if (dataMap instanceof ConcurrentSkipListMap) {
            dataMap.forEach((key, value) -> operateQueue.push(value));
        } else {
            dataMap.forEach((key, value) -> operateQueue.offer(value));
        }
        return operateQueue;
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
        private final Function<String, String> smallCamelToOther;
        private final Function<String, String> otherToSmallCamel;

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

    static class Node implements Map.Entry<String, Object>, Comparable<Object> {
        final String key;
        Object value;

        Node(String key, Object value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public Object getValue() {
            return value;
        }

        @Override
        public Object setValue(Object value) {
            this.value = value;
            return value;
        }

        @Override
        public int compareTo(Object o) {
            if (this.equals(o)) {
                return 0;
            }

            Node innerNode = (Node) o;
            return key.compareTo(innerNode.getKey());
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Node)) {
                return false;
            }
            Node node = (Node) o;
            if (null == getValue()) {
                return getKey().equals(node.getKey());
            } else {
                return getKey().equals(node.getKey()) && getValue().equals(node.getValue());
            }
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(getKey());
        }
    }
}
