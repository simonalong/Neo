package com.simonalong.neo.db;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.simonalong.neo.Neo;
import com.simonalong.neo.sql.TxIsolationEnum;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import javax.sql.DataSource;

import static com.simonalong.neo.NeoConstant.LOG_PRE;

/**
 * @author zhouzhenyong
 * @since 2019/3/16 上午9:34
 */
@Slf4j
public final class ConnectPool {

    private final Neo neo;
    @Getter
    private DataSource dataSource;
    private ThreadLocal<ReusableConnection> connectLocal = new ThreadLocal<>();

    public ConnectPool(Neo neo, DataSource dataSource) {
        this.neo = neo;
        this.dataSource = dataSource;
    }

    public ConnectPool(Neo neo, Properties properties) {
        this.neo = neo;
        try {
            this.dataSource = DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            log.error(LOG_PRE + "");
        }
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

        con = dataSource.getConnection();
        if (neo.isTransaction()) {
            ReusableConnection reConnection = ReusableConnection.of(con);
            reConnection.setAutoCommit(false);
            connectLocal.set(reConnection);
            return reConnection;
        }
        return con;
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
