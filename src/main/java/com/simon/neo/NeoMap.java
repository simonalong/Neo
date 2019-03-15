package com.simon.neo;

import com.simon.neo.exception.NumberOfValueException;
import com.simon.neo.exception.ParameterNullException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author zhouzhenyong
 * @since 2019/3/12 下午12:46
 */
// todo
public class NeoMap implements Map<String, Object> {

    private Map<String, Object> dataMap;
    private static NamingChg defaultNaming = NamingChg.DEFAULT;

    private NeoMap() {
        dataMap = new ConcurrentSkipListMap<>();
    }

    public static NeoMap parseJson(String json) {
        return new NeoMap();
    }

    /**
     * 设置全局名称转换字符
     */
    public static void setDefaultNamingChg(NamingChg namingChg) {
        defaultNaming = namingChg;
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
     * 对象转换为NeoMap，key为属性名字
     */
    public static NeoMap from(Object object) {
        NeoMap neoMap = NeoMap.of();
        if (null == object) {
            return neoMap;
        }

        Field[] fields = object.getClass().getDeclaredFields();
        if (fields.length != 0) {
            Stream.of(fields).forEach(f -> {
                f.setAccessible(true);
                try {
                    Object value = f.getType().cast(f.get(object));
                    if (null != value) {
                        neoMap.putIfAbsent(f.getName(), value);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            });
        }
        return neoMap;
    }

    public static <T> List<T> asArray(List<NeoMap> neoMaps, Class<T> tClass){
        if(null == neoMaps || neoMaps.isEmpty()){
            return new ArrayList<>();
        }

        return neoMaps.stream().map(m->m.as(tClass)).collect(Collectors.toList());
    }

    /**
     * NeoMap 转化为实体数据，其中key就是对应的属性
     *
     * @param tClass 目标类的Class
     * @param <T> 目标类的类型
     * @return 目标类的实体对象
     */
    public <T> T as(Class<T> tClass) {
        if (dataMap.isEmpty()) {
            return null;
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
                        f.set(finalT, dataMap.get(f.getName()));
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

    public static <T> List<T> toList(List<NeoMap> dataMapList){
        // todo
        return new ArrayList<>();
    }

    public static boolean isEmpty(NeoMap neoMap){
        return neoMap == null || neoMap.isEmpty();
    }

    enum NamingChg {
        DEFAULT();
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

    public String toJson() {
        // todo
        return dataMap.toString();
    }

    @Override
    public String toString() {
        return "NeoMap=" + dataMap.toString();
    }
}
