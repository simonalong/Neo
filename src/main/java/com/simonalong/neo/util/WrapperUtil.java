package com.simonalong.neo.util;

import java.util.HashMap;
import java.util.Map;

import lombok.experimental.UtilityClass;

/**
 * 基本类型向包装类型转换
 *
 * @author zhouzhenyong
 * @since 2019/5/16 下午11:11
 */
@UtilityClass
public class WrapperUtil {

    private final Map<Class<?>, Class<?>> wrapperMap = new HashMap<>(9);
    private final Map<Class<?>, Class<?>> unWrapperMap = new HashMap<>(9);

    static {
        wrapperMap.put(Integer.TYPE, Integer.class);
        wrapperMap.put(Long.TYPE, Long.class);
        wrapperMap.put(Double.TYPE, Double.class);
        wrapperMap.put(Byte.TYPE, Byte.class);
        wrapperMap.put(Short.TYPE, Short.class);
        wrapperMap.put(Character.TYPE, Character.class);
        wrapperMap.put(Boolean.TYPE, Boolean.class);
        wrapperMap.put(Float.TYPE, Float.class);
        wrapperMap.put(Void.TYPE, Void.class);

        unWrapperMap.put(Integer.class, Integer.TYPE);
        unWrapperMap.put(Long.class, Long.TYPE);
        unWrapperMap.put(Double.class, Double.TYPE);
        unWrapperMap.put(Byte.class, Byte.TYPE);
        unWrapperMap.put(Short.class, Short.TYPE);
        unWrapperMap.put(Character.class, Character.TYPE);
        unWrapperMap.put(Boolean.class, Boolean.TYPE);
        unWrapperMap.put(Float.class, Float.TYPE);
        unWrapperMap.put(Void.class, Void.TYPE);
    }

    /**
     * 将基本类型的类转化为对应的包装类
     *
     * @param sourceClass 原类型
     * @return 如果是基本类型，则是包装后的类型
     */
    public Class<?> primaryTypeToWrapper(Class<?> sourceClass) {
        if (null != sourceClass && sourceClass.isPrimitive()) {
            return wrapperMap.get(sourceClass);
        }
        return sourceClass;
    }

    /**
     * 包赚类型转换为基本类型
     *
     * @param sourceClass 包装类型
     * @return 基本类型或者原生类型
     */
    public Class<?> wrapperToPrimary(Class<?> sourceClass) {
        if (null != sourceClass) {
            if(unWrapperMap.containsKey(sourceClass)) {
                return unWrapperMap.get(sourceClass);
            }
            return sourceClass;
        }
        return null;
    }
}
