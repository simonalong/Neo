package com.simonalong.neo.util;

import lombok.experimental.UtilityClass;

/**
 * @author shizi
 * @since 2020/3/22 下午5:39
 */
@UtilityClass
public class ClassUtil {

    /**
     * 判断一个类型是否我们需要判决的底层对象
     * 1.是基本类型或者基本类型的包装类型 Boolean Byte Character Short Integer Long Double Float
     * 2.String 类型
     * 注意:
     * 其中void.class.isPrimitive() 返回true，我们这里不核查这种
     */
    @SuppressWarnings("all")
    public boolean isBaseField(Class<?> cls) {
        if ((cls.isPrimitive() && !cls.equals(void.class)) || cls.equals(String.class)) {
            return true;
        } else {
            try{
                if(!cls.equals(Void.class)) {
                    return ((Class) cls.getField("TYPE").get(null)).isPrimitive();
                }
            } catch (IllegalAccessException | NoSuchFieldException e) {
                return false;
            }
            return false;
        }
    }
}
