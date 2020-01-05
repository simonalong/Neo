package com.simonalong.neo.db.xa;

import com.mysql.cj.jdbc.JdbcConnection;
import com.mysql.cj.jdbc.MysqlXAConnection;
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

    public static XAConnection getXaConnect(Connection connection) {
        try {
            return new MysqlXAConnection(connection.unwrap(JdbcConnection.class), true);
        } catch (SQLException e) {
            throw new UnsupportedOperationException("Not supported by connect");
        }
    }
}
