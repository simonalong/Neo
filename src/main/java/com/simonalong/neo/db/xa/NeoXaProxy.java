package com.simonalong.neo.db.xa;

import static com.simonalong.neo.NeoConstant.FUN_EXECUTE;
import static com.simonalong.neo.NeoConstant.FUN_EXECUTE_BATCH;

import com.mysql.cj.jdbc.MysqlXid;
import com.simonalong.neo.Neo;
import com.simonalong.neo.exception.ParameterUnValidException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;
import javafx.util.Pair;
import javax.sql.DataSource;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * Neo 的动态代理对象
 *
 * @author zhouzhenyong
 * @since 2020/1/1 下午2:52
 */
@Slf4j
public class NeoXaProxy implements MethodInterceptor {

    @Getter
    private XAResource rm;
    @Getter
    private Xid xid;
    /**
     * 目标代理对象
     */
    @Getter
    private Neo target;

    NeoXaProxy(Object object) {
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

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        return methodProxy.invokeSuper(o, objects);
    }

    /**
     * 开启分布式事务XA
     */
    public void openXa() throws SQLException, XAException {
        target.openXA();
        rm = XaConnectionFactory.getXaConnect(target.getConnection()).getXAResource();
        xid = new MysqlXid(UUID.randomUUID().toString().getBytes(), String.valueOf(System.currentTimeMillis()).getBytes(), 1);
        rm.start(xid, XAResource.TMNOFLAGS);
    }

    public void endXid() throws XAException {
        rm.end(xid, XAResource.TMSUCCESS);
    }
}
