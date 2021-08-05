package com.simonalong.neo;

/**
 * 对于常见的常量关键字列举在这里
 *
 * @author zhouzhenyong
 * @since 2019/5/14 下午6:45
 */
public interface NeoConstant {

    /**
     * 日志前缀
     */
    String LOG_PRE_NEO = "[Neo] ";
    /**
     * SQL 的order by
     */
    String ORDER_BY = "order by";
    /**
     * SQL 的limit
     */
    String LIMIT = "limit";

    /**
     * 所有列名
     */
    String ALL_COLUMN_NAME = "*";
    /**
     * 所有的属性名
     */
    String ALL_FIELD = "*";
    /**
     * SQL 的select
     */
    String SELECT = "select";
    /**
     * 默认表
     */
    String DEFAULT_TABLE = "_default_";

    /**
     * bit的个数
     */
    int BIT_NUM = 64;

    String AND = "and";
    String OR = "or";
    String EMPTY = "";
    /**
     * SQL 的desc
     */
    String DESC = "desc";
    /**
     * SQL 的asc
     */
    String ASC = "asc";
    String GROUP_BY = "group by";
    String IS_NOT_NULL = "is not null";
    String IS_NULL = "is null";
    String ONT_IN = "not in";
    String IN = "in";
    String NOT_LIKE = "not like";
    String LIKE = "like";

    String EQUAL = "=";
    String SPACE = " ";
    String LESS_EQUAL = "<=";
    String GREATER_EQUAL = ">=";
    String LESS_THAN = "<";
    String GREATER_THAN = ">";
    String NOT_EQUAL = "!=";
}
