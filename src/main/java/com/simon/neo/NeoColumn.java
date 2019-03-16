package com.simon.neo;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author zhouzhenyong
 * @since 2019/3/12 下午8:30
 */
public class NeoColumn {

    /**
     * 数据库中的列名
     */
    private String columnName;
    /**
     * 列的别名
     */
    private String columnLabel;
    /**
     * 尺寸大小
     */
    private Integer size;
    /**
     * 列的转换的java的类型
     */
    private Class<?> javaClass;
    /**
     * 列的JDBC类型
     * @see java.sql.JDBCType
     */
    private String columnJDBCType;
    /**
     * 列的数据库中定义的类型
     */
    private String columnTypeName;
    /**
     * 列是否是可以自增的
     */
    private Boolean isAutoIncrement;
    /**
     * 列是否是主键
     */
    private Boolean isPrimaryKey;
    /**
     * 列是否是外键
     */
    private Boolean isForeignKey;
}
