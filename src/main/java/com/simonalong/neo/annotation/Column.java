package com.simonalong.neo.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zhouzhenyong
 * @since 2019/9/3 上午12:08
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {

    /**
     * 表中的列名
     * @return 表的列名
     */
    String value() default "";

    /**
     * 表名
     * @return 对应表的名字
     */
    String table() default "";
}
