package com.simonalong.neo.annotation;

import java.lang.annotation.*;

import static com.simonalong.neo.NeoConstant.DEFAULT_TABLE;

/**
 * @author shizi
 * @since 2020/3/19 下午1:04
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {

    /**
     * 表中的列名
     *
     * <p>
     *     如果{@link Column}中设置了tableName，则会覆盖该名字
     * @return 表的列名
     */
    String value() default DEFAULT_TABLE;
}
