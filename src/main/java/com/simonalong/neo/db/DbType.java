package com.simonalong.neo.db;

import com.simonalong.neo.exception.DbNotSupport;
import com.simonalong.neo.exception.NeoException;
import lombok.AllArgsConstructor;

/**
 * @author shizi
 * @since 2020/1/9 11:12 下午
 */
@AllArgsConstructor
public enum DbType {

    /**
     * mysql 默认
     */
    MYSQL("jdbc:mysql"),
    /**
     * sqlite
     */
    SQLITE("jdbc:sqlite"),
    /**
     * H2
     */
    H2("jdbc:h2"),
    /**
     * postgresql
     */
    PGSQL("jdbc:postgresql"),
    /**
     * oracle
     */
    ORACLE("jdbc:oracle"),
    /**
     * mariadb
     */
    MARIADB("jdbc:mariadb");

    private String name;

    public static DbType parse(String url) {
        if (null == url || "".equals(url)) {
            throw new NeoException("url 不可为空");
        }
        DbType[] dbList = DbType.values();
        for (DbType dbType : dbList) {
            if (url.startsWith(dbType.name)) {
                return dbType;
            }
        }
        throw new DbNotSupport("url 不支持");
    }
}
