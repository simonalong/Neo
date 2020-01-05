package com.simonalong.neo.db.xa;

import static com.simonalong.neo.NeoConstant.FUN_EXECUTE;
import static com.simonalong.neo.NeoConstant.FUN_EXECUTE_BATCH;

import com.mysql.cj.jdbc.MysqlXid;
import com.simonalong.neo.Neo;
import com.simonalong.neo.exception.ParameterUnValidException;
import java.lang.reflect.Method;
import java.sql.SQLException;
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
public class NeoProxy implements MethodInterceptor {

    @Getter
    private XAResource rm;
    @Getter
    private Xid xid;
    private Neo neo;
    /**
     * 目标代理对象
     */
    @Getter
    private Neo proxy;
    /**
     * 是否执行有效
     */
    @Getter
    private Boolean aliveStatus = false;

    public NeoProxy (Object object) {
        Neo db;
        if (object instanceof Neo) {
            db = (Neo) object;
        } else if (object instanceof DataSource) {
            db = Neo.connect((DataSource) object);
        } else {
            throw new ParameterUnValidException("value=" + object.getClass().toString() + "不是Neo也不是Datasource类型");
        }

        this.neo = db;

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(Neo.class);
        enhancer.setCallback(this);

        this.proxy = (Neo) enhancer.create();
        // 初始化proxy中的数据
        Neo.clone(db, this.proxy);
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        if(targetMethod(method, objects)) {
            try {
                beforeExecute();
                methodProxy.invokeSuper(o, objects);
                afterExecute();
            } catch (SQLException | XAException e) {
                log.error("执行异常", e);
            }
        } else {
            methodProxy.invokeSuper(o, objects);
        }
        return o;
    }

    /**
     * 是否目标方法
     * @param method 方法名
     * @param objects 方法参数
     * @return true：是要拦截的方法，false：不是要拦截的方法
     */
    private Boolean targetMethod(Method method, Object[] objects) {
        return matchExecute(method, objects) || matchExecuteBatch(method, objects);
    }

    /**
     * 匹配函数{@code protected <T> T execute(Boolean multiLine, Supplier<Pair<String, List<Object>>> sqlSupplier,
     *             Function<PreparedStatement, T> stateFun)}
     * @param method 函数名
     * @param objects 参数类型
     * @return true：匹配成功，false：匹配失败
     */
    @SuppressWarnings("all")
    private Boolean matchExecute(Method method, Object[] objects){
        if (!method.getName().equals(FUN_EXECUTE) || objects.length != 3) {
            return false;
        }

        if(!(objects[0] instanceof Boolean)){
            return false;
        }

        if(!(objects[1] instanceof Supplier)){
            return false;
        }

        return objects[2] instanceof Function;
    }

    /**
     * {@code protected Integer executeBatch(Pair<String, List<List<Object>>> sqlPair)}
     * @return true：匹配成功，false：匹配失败
     */
    private Boolean matchExecuteBatch(Method method, Object[] objects) {
        if (!method.getName().equals(FUN_EXECUTE_BATCH) || objects.length != 1){
            return false;
        }

        return objects[0] instanceof Pair;
    }

    private void beforeExecute() throws SQLException, XAException {
        // 开启事务，这样其中的connect就可以重用了
        neo.getTxStatusLocal().set(true);
        rm = XaConnectionFactory.getXaConnect(neo.getConnection()).getXAResource();
        Xid xid = new MysqlXid("g12345".getBytes(), String.valueOf(System.currentTimeMillis()).getBytes(), 1);
        aliveStatus = true;
        rm.start(xid, XAResource.TMNOFLAGS);
    }

    private void afterExecute() throws XAException {
        rm.end(xid, XAResource.TMSUCCESS);
    }
}
