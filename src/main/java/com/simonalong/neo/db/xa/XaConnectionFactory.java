package com.simonalong.neo.db.xa;

import com.mysql.cj.jdbc.JdbcConnection;
import com.mysql.cj.jdbc.MysqlXAConnection;
import com.simonalong.neo.db.DbType;
import java.sql.Connection;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import javax.sql.XAConnection;

/**
 * @author zhouzhenyong
 * @since 2019/11/20 下午11:49
 */
@Slf4j
public class XaConnectionFactory {

    /**
     * 获取XA的connect
     * <p> 通过将connect封装为XA的connect来进行使用
     * @param connection connect
     * @param dbType 数据库类型
     * @return XA的connect
     */
    public static XAConnection getXaConnect(Connection connection, DbType dbType) {
        try {
            switch (dbType) {
                case MYSQL:
                    return new MysqlXAConnection(connection.unwrap(JdbcConnection.class), true);
                case PGSQL:
                    // todo 这里要采用postgresql的链接类型
                    return new MysqlXAConnection(connection.unwrap(JdbcConnection.class), true);
                default:
                    throw new UnsupportedOperationException("Not supported by connect");
            }
        } catch (SQLException e) {
            throw new UnsupportedOperationException("Not supported by connect");
        }
    }
}
