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
     * 列名
     * @return 列名
     */
    String value() default "";

    /**
     * 表的别名
     *
     * <p>
     *    如果{@link Table}中也设置了，则会覆盖其中的名字
     * @return 对应表的别名
     */
    String table() default "";
}
