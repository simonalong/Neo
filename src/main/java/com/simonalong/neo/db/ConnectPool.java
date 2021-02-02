package com.simonalong.neo.db;

import com.alibaba.druid.pool.DruidDataSource;
import com.simonalong.neo.Neo;
import com.simonalong.neo.exception.NeoException;
import com.simonalong.neo.sql.TxIsolationEnum;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLTransientConnectionException;
import java.util.Properties;
import javax.sql.DataSource;
import lombok.Getter;

/**
 * @author zhouzhenyong
 * @since 2019/3/16 上午9:34
 */
public final class ConnectPool {

    private final Neo neo;
    @Getter
    private DataSource dataSource;
    private ThreadLocal<ReusableConnection> connectLocal = new ThreadLocal<>();

    public ConnectPool(Neo neo, DataSource dataSource) {
        this.neo = neo;
        this.dataSource = dataSource;
    }

    public ConnectPool(Neo neo) {
        this.neo = neo;
    }

    public void initFromDruid(Properties properties){
        DruidDataSource druidDataSource= new DruidDataSource();
        druidDataSource.configFromPropety(properties);
        this.dataSource = druidDataSource;
    }

    public void initFromHikariCP(Properties properties){
        this.dataSource = new HikariDataSource(new HikariConfig(properties));;
    }

    /**
     * 获取链接，非事务获取普通的链接对象，事务获取可重用的链接
     *
     * @return 返回连接
     * @throws SQLException 获取连接失败
     */
    public Connection getConnect() throws SQLException {
        Connection con = connectLocal.get();
        if (null != con) {
            return con;
        }

        try {
            con = dataSource.getConnection();
            // XA事务获取可重用连接
            if (neo.isXaTransaction()) {
                ReusableConnection reConnection = ReusableConnection.of(con);
                connectLocal.set(reConnection);
                return reConnection;
            }

            // 本机事务获取可重用连接，而且设置非自动提交
            if (neo.isTransaction()) {
                ReusableConnection reConnection = ReusableConnection.of(con);
                reConnection.setAutoCommit(false);
                connectLocal.set(reConnection);
                return reConnection;
            }
            return con;
        } catch (SQLTransientConnectionException e) {
            throw new NeoException(e);
        }
    }

    /**
     * 设置事务的一些配置
     *
     * @param isolationEnum 事务隔离级别
     * @param readOnly 事务的可读性
     * @throws SQLException SQL异常
     */
    @SuppressWarnings("all")
    public void setTxConfig(TxIsolationEnum isolationEnum, Boolean readOnly) throws SQLException {
        Connection con = connectLocal.get();
        if (null != con) {
            if (null != readOnly) {
                con.setReadOnly(readOnly);
            }

            if (null != isolationEnum) {
                con.setTransactionIsolation(isolationEnum.getLevel());
            }
        }
    }

    public void submit() throws SQLException {
        ReusableConnection con = connectLocal.get();
        // 针对事务嵌套这里采用最外层事务提交
        if (neo.txIsRoot()) {
            if (null != con) {
                try {
                    con.commit();
                    con.setAutoCommit(true);
                    con.handleClose();
                } finally {
                    connectLocal.remove();
                }
            }
        }
    }
    public void rollback() throws SQLException {
        ReusableConnection con = connectLocal.get();
        if (null != con) {
            try {
                con.rollback();
                con.setAutoCommit(true);
                con.handleClose();
            } finally {
                connectLocal.remove();
            }
        }
    }
}
