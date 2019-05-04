package com.simon.neo.util;

import java.lang.reflect.InvocationTargetException;
import lombok.experimental.UtilityClass;

/**
 * @author zhouzhenyong
 * @since 2018/12/26 下午11:56
 */
@UtilityClass
public class Objects {

    private static final String NULL_STR = "null";

    /**
     * 将对象的数据，按照cls类型进行转换
     * @param cls 待转换的Class类型
     * @param data 数据
     * @return Class类型对应的对象
     */
    public <T> T cast(Class<T> cls, String data){
        if(cls.equals(String.class)) {
            // 针对data为null的情况进行转换
            if(NULL_STR.equals(data)){
                return null;
            }
            return cls.cast(data);
        } else if (Character.class.isAssignableFrom(cls)) {
            return cls.cast(data.toCharArray());
        } else {
            try {
                if(NULL_STR.equals(data)){
                    return null;
                }
                return cls.cast(cls.getMethod("valueOf", String.class).invoke(null, data));
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
