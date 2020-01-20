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
     * mysql
     */
    MYSQL("jdbc:mysql"),
    /**
     * sqlite
     */
    SQLITE("jdbc:sqlite"),
    /**
     * postgresql
     */
    PGSQL("jdbc:postgresql"),
    /**
     * oracle
     */
    ORACLE("jdbc:oracle");

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
