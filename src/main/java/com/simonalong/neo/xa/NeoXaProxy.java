package com.simonalong.neo.xa;

import com.simonalong.neo.Neo;
import com.simonalong.neo.exception.ParameterUnValidException;
import java.sql.SQLException;
import javax.sql.DataSource;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Neo 的XA代理对象
 *
 * @author zhouzhenyong
 * @since 2020/1/1 下午2:52
 */
@Slf4j
@Getter
public class NeoXaProxy {

    private XAResource rm;
    private Xid xid;
    /**
     * 目标代理对象
     */
    private final Neo target;

    public NeoXaProxy(Object object) {
        Neo db;
        if (object instanceof Neo) {
            db = (Neo) object;
        } else if (object instanceof DataSource) {
            db = Neo.connect((DataSource) object);
        } else {
            throw new ParameterUnValidException("value=" + object.getClass().toString() + "不是Neo也不是Datasource类型");
        }

        this.target = db;
    }

    /**
     * 开启分布式事务XA
     *
     * @throws SQLException 获取资源的sql异常
     * @throws XAException 启动xa异常
     */
    public void openXa() throws SQLException, XAException {
        target.openXA();
        rm = XaConnectionFactory.getXaConnect(target.getDbType(), target.getConnection()).getXAResource();
        xid = new NeoXid();
        rm.start(xid, XAResource.TMNOFLAGS);
    }

    public void endXid() throws XAException {
        rm.end(xid, XAResource.TMSUCCESS);
    }
}
