package com.simon.neo;

import com.simon.neo.exception.NumberOfValueException;
import com.simon.neo.exception.ParameterNullException;
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
import lombok.Setter;

/**
 * @author zhouzhenyong
 * @since 2019/3/12 下午12:46
 */
public class NeoMap implements Map<String, Object> {

    private Map<String, Object> dataMap;
    /**
     * 全局的命名转换，默认不转换
     */
    private static NamingChg globalNaming = NamingChg.DEFAULT;
    /**
     * 用于自定义的转换器，key为变量名，value为dataMap的key映射，结构为：Map<String, String>
     */
    @Setter
    private NeoMap userDefineNaming;
    /**
     * 单体数据的命名转换
     */
    @Setter
    private NamingChg localNaming;

    private NeoMap() {
        dataMap = new ConcurrentSkipListMap<>();
    }

    public static List<Object> values(NeoMap... maps) {
        List<Object> valueList = new ArrayList<>();
        if (null != maps && maps.length > 0) {
            Stream.of(maps).forEach(m -> valueList.addAll(m.values()));
        }
        return valueList;
    }

    /**
     * 设置全局名称转换字符，请注意，该转换会对所有NeoMap生效
     */
    public static void setDefaultNamingChg(NamingChg namingChg) {
        globalNaming = namingChg;
    }

    public static NeoMap of(Object... kvs) {
        if (kvs.length % 2 != 0) {
            throw new NumberOfValueException("参数请使用：key,value,key,value...这种参数格式");
        }

        NeoMap neoMap = new NeoMap();
        for (int i = 0; i < kvs.length; i += 2) {
            if (null == kvs[i]) {
                throw new ParameterNullException("NeoMap.of()中的参数不可为null");
            }
            neoMap.put((String) kvs[i], kvs[i + 1]);
        }
        return neoMap;
    }

    /**
     * 对象转换为NeoMap
     * @param object 待转换对象
     */
    public static NeoMap from(Object object) {
        return from(object, NamingChg.DEFAULT, new ArrayList<>(), new ArrayList<>());
    }

    /**
     * 对象转换为NeoMap
     * @param object 待转换对象
     */
    public static NeoMap from(Object object, NamingChg namingChg) {
        return from(object, namingChg, new ArrayList<>(), new ArrayList<>());
    }

    /**
     * 对象转换为NeoMap
     * @param object 待转换对象
     * @param userDefineNaming 用户自定义的转换，结构为<code>Map<String, String></code>
     */
    public static NeoMap from(Object object, NeoMap userDefineNaming) {
        return from(object, userDefineNaming, new ArrayList<>(), new ArrayList<>());
    }

    /**
     * 指定包括的属性进行对象转换为NeoMap
     * @param object 待转换对象
     * @param fields 需要的属性
     */
    public static NeoMap fromInclude(Object object, String... fields) {
        return from(object, NamingChg.DEFAULT, Arrays.asList(fields), new ArrayList<>());
    }

    /**
     * 指定排除的属性进行对象转换为NeoMap
     * @param object 待转换对象
     * @param fields 排除的属性
     */
    public static NeoMap fromExclude(Object object, String... fields) {
        return from(object, NamingChg.DEFAULT, new ArrayList<>(), Arrays.asList(fields));
    }

    /**
     * 对象转换为NeoMap
     * @param object 待转换的对象
     * @param userNaming 用户自定义命名转换方式
     * @param inFieldList 包括的属性
     * @param exFieldList 排除的属性
     */
    public static NeoMap from(Object object, NeoMap userNaming, List<String> inFieldList, List<String> exFieldList) {
        NeoMap neoMap = NeoMap.of();
        if (null == object) {
            return neoMap;
        }
        neoMap.setUserDefineNaming(userNaming);
        return innerFrom(neoMap, object, inFieldList, exFieldList);
    }

    /**
     * 对象转换为NeoMap
     * @param object 待转换的对象
     * @param naming 转换方式
     * @param inFieldList 包括的属性
     * @param exFieldList 排除的属性
     */
    public static NeoMap from(Object object, NamingChg naming, List<String> inFieldList, List<String> exFieldList) {
        NeoMap neoMap = NeoMap.of();
        if (null == object) {
            return neoMap;
        }
        neoMap.setLocalNaming(naming);
        return innerFrom(neoMap, object, inFieldList, exFieldList);
    }

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
                    Object value = f.getType().cast(f.get(object));
                    if (null != value) {
                        neoMap.putIfAbsent(neoMap.namingChg(f.getName()), value);
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

    public static String stringChg(String source, NamingChg namingChg){
        return namingChg.namingChg(source);
    }

    public static boolean isEmpty(NeoMap neoMap) {
        return neoMap == null || neoMap.isEmpty();
    }

    /**
     * NeoMap 转化为实体数据，其中key就是对应的属性
     *
     * @param tClass 目标类的Class
     * @param <T> 目标类的类型
     * @return 目标类的实体对象
     */
    public <T> T as(Class<T> tClass) {
        return as(tClass, localNaming);
    }

    /**
     * NeoMap 转化为实体数据，其中key就是对应的属性
     *
     * @param tClass 目标类的Class
     * @param naming 属性名的转换
     * @param <T> 目标类的类型
     * @return 目标类的实体对象
     */
    public <T> T as(Class<T> tClass, NamingChg naming) {
        if (dataMap.isEmpty()) {
            return null;
        }
        T t = null;
        try {
            t = tClass.newInstance();
            Field[] fields = tClass.getDeclaredFields();
            if (fields.length != 0) {
                T finalT = t;
                setLocalNaming(naming);
                Stream.of(fields).forEach(f -> {
                    f.setAccessible(true);
                    try {
                        f.set(finalT, dataMap.get(namingChg(f.getName())));
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

    public NeoMap append(NeoMap neoMap) {
        this.putAll(neoMap.getDataMap());
        return this;
    }

    public NeoMap append(String key, String value) {
        this.put(key, value);
        return this;
    }

    /**
     * 这里根据命名订阅，如果用户自定义表中存在映射，则使用该映射，否则用设置的命名映射规则
     */
    private String namingChg(String name) {
        if (null != userDefineNaming) {
            String chgName = (String) userDefineNaming.get(name);
            if (null != chgName) {
                return chgName;
            }
        }
        return (null != localNaming ? localNaming : globalNaming).namingChg(name);
    }

    public enum NamingChg {
        /**
         * 不转换
         */
        DEFAULT(t -> t),
        /**
         * 小驼峰到大驼峰 dataBaseUser -> DateBaseUser
         */
        BIGCAMEL(StringNaming::bigCamel),
        /**
         * 小驼峰到下划线 dataBaseUser -> data_base_user
         */
        UNDERLINE(StringNaming::underLine),
        /**
         * 小驼峰到前下划线 dataBaseUser -> _data_base_user
         */
        PREUNDER(StringNaming::preUnder),
        /**
         * 小驼峰到前下划线 dataBaseUser -> data_base_user_
         */
        POSTUNDER(StringNaming::postUnder),
        /**
         * 小驼峰到前后下划线 dataBaseUser -> _data_base_user_
         */
        PREPOSTUNDER(StringNaming::prePostUnder),
        /**
         * 小驼峰到中划线 dataBaseUser -> data-base-user
         */
        MIDDLELINE(StringNaming::middleLine),
        /**
         * 小驼峰到大写下划线 dataBaseUser -> DATA_BASE_USER
         */
        UPPERUNER(StringNaming::upperUnder),
        /**
         * 小驼峰到大写中划线 dataBaseUser -> DATA-BASE-USER
         */
        UPPERMIDDLE(StringNaming::upperUnderMiddle);

        /**
         * 用于名字的转换
         */
        private Function<String, String> chg;

        NamingChg(Function<String, String> chgFun) {
            this.chg = chgFun;
        }

        /**
         * 将map中的key转换到object对象对应的属性名字
         */
        public String namingChg(String data) {
            return chg.apply(data);
        }
    }

    public Map<String, Object> getDataMap() {
        return dataMap;
    }

    @Override
    public int size() {
        return dataMap.size();
    }

    @Override
    public boolean isEmpty() {
        return dataMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return dataMap.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return dataMap.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        return dataMap.get(key);
    }

    @Override
    public Object put(String key, Object value) {
        if (null != value) {
            dataMap.put(key, value);
        }
        return value;
    }

    @Override
    public Object remove(Object key) {
        return dataMap.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
        dataMap.putAll(m);
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
        return dataMap.values();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return dataMap.entrySet();
    }

    @Override
    public String toString() {
        return dataMap.toString();
    }
}
