package com.simon.neo;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import javax.sql.DataSource;

/**
 * @author zhouzhenyong
 * @since 2019/3/16 上午9:34
 */
public class ConnectPool {

    private final DataSource dataSource;

    public ConnectPool(DataSource dataSource){
        this.dataSource = dataSource;
    }

    public ConnectPool(String propertiesPath){
        this.dataSource = new HikariDataSource(new HikariConfig(propertiesPath));
    }

    public ConnectPool(Properties properties){
        this.dataSource = new HikariDataSource(new HikariConfig(properties));
    }

    public Connection getConnect() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
