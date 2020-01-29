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
    String LOG_PRE = "[Neo] ";
    /**
     * SQL 的order by
     */
    String ORDER_BY = "order by";
    /**
     * SQL 的limit
     */
    String LIMIT = "limit";
    /**
     * SQL 的desc
     */
    String DESC = "desc";
    /**
     * SQL 的asc
     */
    String ASC = "asc";
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
     * 多表时候中间的分割
     */
    String ALIAS_DOM = "_NDom_";
    /**
     * SQL 的as
     */
    String AS = " as ";
    /**
     * SQL 的数据库列的分隔标识符
     */
    String COLUMN_PRE = "`";

    /**
     * 函数：execute
     */
    String FUN_EXECUTE = "execute";
    /**
     * 函数：executeBatch
     */
    String FUN_EXECUTE_BATCH = "executeBatch";
}
