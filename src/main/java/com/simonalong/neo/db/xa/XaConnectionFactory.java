package com.simonalong.neo.db.xa;

//import com.mysql.cj.jdbc.JdbcConnection;
//import com.mysql.cj.jdbc.MysqlXAConnection;
//import com.mysql.jdbc.jdbc2.optional.MysqlXAConnection;
//import com.mysql.jdbc.jdbc2.optional.MysqlXAConnection;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import javax.sql.DataSource;
import javax.sql.XAConnection;

/**
 * @author zhouzhenyong
 * @since 2019/11/20 下午11:49
 */
@Slf4j
public class XaConnectionFactory {

    public static XAConnection getXaConnect(DataSource dataSource) {

//        try {
//            return new MysqlXAConnection(dataSource.getConnection().unwrap(JdbcConnection.class), true);
//        } catch (SQLException e) {
//            throw new UnsupportedOperationException("Not supported by DruidDataSource");
//        }
        return null;
    }
}
