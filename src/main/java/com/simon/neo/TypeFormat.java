package com.simon.neo;

import com.simon.neo.util.Objects;
import java.lang.reflect.Array;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.experimental.UtilityClass;

/**
 * 将Object转换为指定的类型
 *
 * @author zhouzhenyong
 * @since 2019/5/4 下午12:30
 */
@UtilityClass
public class TypeFormat {

    public Boolean toBoolean(Object value) {
        if(null == value){
            return null;
        }

        if (value instanceof Boolean) {
            return Boolean.class.cast(value);
        }
        return Boolean.valueOf(String.valueOf(value));
    }

    public Character toChar(Object value){
        if(null == value){
            return null;
        }

        if(value instanceof Character){
            return Character.class.cast(value);
        }
        String valueStr = String.valueOf(value);
        if (valueStr.length() == 0){
            return null;
        }
        return valueStr.charAt(0);
    }

    public String toStr(Object value){
        if(null == value){
            return null;
        }

        if(value instanceof String){
            return String.class.cast(value);
        }
        return String.valueOf(value);
    }

    public Byte toByte(Object value){
        if(null == value){
            return null;
        }

        try {
            if (value instanceof Number) {
                return Number.class.cast(value).byteValue();
            }
            return Byte.valueOf(String.valueOf(value));
        }catch (NumberFormatException | ClassCastException e){
            e.printStackTrace();
            return null;
        }
    }

    public Short toShort(Object value){
        if(null == value){
            return null;
        }

        try {
            if (value instanceof Number) {
                return Number.class.cast(value).shortValue();
            }
            return Short.valueOf(String.valueOf(value));
        }catch (NumberFormatException | ClassCastException e){
            e.printStackTrace();
            return null;
        }
    }

    public Integer toInt(Object value){
        if(null == value){
            return null;
        }

        try {
            if (value instanceof Number) {
                return Number.class.cast(value).intValue();
            }
            return Integer.valueOf(String.valueOf(value));
        }catch (NumberFormatException | ClassCastException e){
            e.printStackTrace();
            return null;
        }
    }

    public Long toLong(Object value){
        if(null == value){
            return null;
        }

        try {
            if (value instanceof Number) {
                return Number.class.cast(value).longValue();
            }
            return Long.valueOf(String.valueOf(value));
        }catch (NumberFormatException | ClassCastException e){
            e.printStackTrace();
            return null;
        }
    }

    public Double toDouble(Object value){
        if(null == value){
            return null;
        }

        try {
            if (value instanceof Number) {
                return Number.class.cast(value).doubleValue();
            }
            return Double.valueOf(String.valueOf(value));
        }catch (NumberFormatException | ClassCastException e){
            e.printStackTrace();
            return null;
        }
    }

    public Float toFloat(Object value){
        if(null == value){
            return null;
        }

        try {
            if (value instanceof Number) {
                return Number.class.cast(value).floatValue();
            }
            return Float.valueOf(String.valueOf(value));
        }catch (NumberFormatException | ClassCastException e){
            e.printStackTrace();
            return null;
        }
    }

    public <T> T toEnum(Class<T> tClass, Object value) {
        if (null == tClass || null == value) {
            return null;
        }

        if (tClass.isEnum() && value instanceof String) {
            return Stream.of(tClass.getEnumConstants())
                .filter(t -> t.toString().equalsIgnoreCase(String.class.cast(value))).findFirst()
                .orElse(null);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public <T> T[] toArray(Object value){
        if(null == value){
            return null;
        }

        if(value.getClass().isArray()){
            return (T[]) value;
        }

        if(value instanceof Collection){
            return (T[]) Collection.class.cast(value).toArray();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> toList(Object value){
        if(null == value){
            return null;
        }

        if (List.class.isAssignableFrom(value.getClass())){
            return List.class.cast(value);
        }

        if(Array.class.isAssignableFrom(value.getClass())){
            return Arrays.asList(toArray(value));
        }

        if(Collection.class.isAssignableFrom(value.getClass())){
            return new ArrayList(Collection.class.cast(value));
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public <T> Set<T> toSet(Object value){
        if(null == value){
            return null;
        }

        if (Set.class.isAssignableFrom(value.getClass())){
            return Set.class.cast(value);
        }

        if(Array.class.isAssignableFrom(value.getClass())){
            return new HashSet(Arrays.asList(toArray(value)));
        }

        if (Collection.class.isAssignableFrom(value.getClass())) {
            return new HashSet<>(Collection.class.cast(value));
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public <T> Queue<T> toQueue(Object value){
        if(null == value){
            return null;
        }

        if (Queue.class.isAssignableFrom(value.getClass())){
            return Queue.class.cast(value);
        }

        if(Array.class.isAssignableFrom(value.getClass())){
            return new ArrayDeque(Arrays.asList(toArray(value)));
        }

        if (Collection.class.isAssignableFrom(value.getClass())) {
            return new ArrayDeque<>(Collection.class.cast(value));
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public <T> Collection<T> toCollection(Object value){
        if(null == value){
            return null;
        }

        if(Collection.class.isAssignableFrom(value.getClass())){
            return Collection.class.cast(value);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public <K, V> Map<K, V> toMap(Object value){
        if(null == value){
            return null;
        }

        if(value instanceof Map){
            return (Map<K, V>)Map.class.cast(value);
        }
        return null;
    }

    public NeoMap toNeoMap(Object value){
        if(null == value){
            return null;
        }

        if(value instanceof NeoMap){
            return NeoMap.class.cast(value);
        }

        return NeoMap.from(value);
    }

    @SuppressWarnings("unchecked")
    public <T> T cast(Class<? extends T> tClass, Object value) {
        if (null == tClass || null == value) {
            return null;
        }

        // 对于是对应的实例，则直接转换，对于不是的，则进行额外特殊处理
        if(tClass.isInstance(value)){
            return (T) value;
        }

        if(tClass == char.class || tClass == Character.class){
            return (T) toChar(value);
        }
        if(tClass == String.class){
            return (T) toStr(value);
        }

        if(tClass == byte.class || tClass == Byte.class){
            return (T) toByte(value);
        }

        if(tClass == short.class || tClass == Short.class){
            return (T) toShort(value);
        }

        if(tClass == int.class || tClass == Integer.class){
            return (T) toInt(value);
        }

        if(tClass == long.class || tClass == Long.class){
            return (T) toLong(value);
        }

        if(tClass == float.class || tClass == Float.class){
            return (T) toFloat(value);
        }

        if(tClass == double.class || tClass == Double.class){
            return (T) toDouble(value);
        }

        if(tClass.isEnum()){
            return toEnum(tClass, value);
        }

        if(tClass.isArray() || Array.class.isAssignableFrom(tClass)){
            return (T) toArray(value);
        }

        if(List.class.isAssignableFrom(tClass)){
            return (T) toList(value);
        }

        if(Set.class.isAssignableFrom(tClass)){
            return (T) toSet(value);
        }

        if(Queue.class.isAssignableFrom(tClass)){
            return (T) toQueue(value);
        }

        if (Collection.class.isAssignableFrom(tClass)){
            return (T) toCollection(value);
        }
        return Objects.cast(tClass, String.valueOf(value));
    }
}
