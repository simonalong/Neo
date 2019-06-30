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

    private Map<Class<?>, Class<?>> wrapperMap = new HashMap<>(9);

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
    }

    /**
     * 将基本类型的类转化为对应的包装类
     *
     * @param sourceClass 原类型
     * @return 如果是基本类型，则是包装后的类型
     */
    public Class<?> primaryTypeToWrapper(Class<?> sourceClass){
        if(null != sourceClass && sourceClass.isPrimitive()){
            return wrapperMap.get(sourceClass);
        }
        return sourceClass;
    }
}
