package com.simonalong.neo.db.xa;

import com.simonalong.neo.Neo;
import com.simonalong.neo.exception.NumberOfValueException;
import com.simonalong.neo.exception.ParameterNullException;
import com.simonalong.neo.exception.ParameterUnValidException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javafx.util.Pair;
import javax.sql.DataSource;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import lombok.Getter;

/**
 * @author zhouzhenyong
 * @since 2019/11/20 下午11:17
 */
public class NeoXa {

    private static Map<String, Neo> neoMap = new ConcurrentHashMap<>(8);
    private static final Integer KV_NUM = 2;

    /**
     * 通过key-value-key-value生成
     *
     * @param kvs 参数是通过key-value-key-value等等这种
     * @return 生成的map数据
     */
    public static NeoXa of(Object... kvs) {
        if (kvs.length % KV_NUM != 0) {
            throw new NumberOfValueException("参数请使用：key,value,key,value...这种参数格式");
        }

        NeoXa xa = new NeoXa();
        for (int i = 0; i < kvs.length; i += KV_NUM) {
            if (null == kvs[i]) {
                throw new ParameterNullException("NeoMap.of()中的参数不可为null");
            }

            if (kvs[i + 1] instanceof InnerNeo) {
                neoMap.put((String) kvs[i], (InnerNeo) kvs[i + 1]);
            } else if (kvs[i + 1] instanceof DataSource) {
                neoMap.put((String) kvs[i], InnerNeo.connect((DataSource) kvs[i + 1]));
            } else {
                throw new ParameterUnValidException(
                    "value = " + kvs[i + 1].getClass().toString() + "不是Neo也不是Datasource类型");
            }
        }
        return xa;
    }

    /**
     * 添加db数据
     *
     * @param alias 数据库别名
     * @param dataSource 数据库链接对象
     * @return pool对象
     */
    public NeoXa add(String alias, DataSource dataSource) {
        neoMap.putIfAbsent(alias, Neo.connect(dataSource));
        return this;
    }

    /**
     * 添加db数据
     *
     * @param alias 数据库别名
     * @param neo 数据库对象
     * @return pool对象
     */
    public NeoXa add(String alias, Neo neo) {
        neoMap.putIfAbsent(alias, neo);
        return this;
    }

    /**
     * 获取对应的db数据
     *
     * @param alias db别名
     * @return neo对象
     */
    public InnerNeo get(String alias) {
        return (InnerNeo) neoMap.get(alias);
    }

    public void run(Runnable runnable) {
        runnable.run();
        afterProcess();
    }

    private void afterProcess() {
        // 全部事务成功标示
        boolean allSuccess = true;
        List<InnerNeo> innerNeoList = neoMap.values().stream().map(n -> (InnerNeo) n).filter(InnerNeo::getAliveFlag)
            .collect(Collectors.toList());
        for (InnerNeo innerNeo : innerNeoList) {
            try {
                // 任何一个事务失败，则全局事务标示为失败
                if (innerNeo.getRm().prepare(innerNeo.getXid()) != XAResource.XA_OK) {
                    allSuccess = false;
                }
            } catch (XAException e) {
                e.printStackTrace();
            }
        }

        if (allSuccess) {
            innerNeoList.forEach(n -> {
                try {
                    n.getRm().commit(n.getXid(), false);
                } catch (XAException e) {
                    e.printStackTrace();
                }
            });
        } else {
            innerNeoList.forEach(n -> {
                try {
                    n.getRm().rollback(n.getXid());
                } catch (XAException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    static class InnerNeo extends Neo {

        @Getter
        private XAResource rm;
        @Getter
        private Xid xid;
        /**
         * 是否执行有效
         */
        @Getter
        private Boolean aliveFlag = false;

        private InnerNeo() {
        }

        @Override
        protected <T> T execute(Boolean multiLine, Supplier<Pair<String, List<Object>>> sqlSupplier,
            Function<PreparedStatement, T> stateFun) {
            try {
                beforeExecute();
                T t = super.execute(multiLine, sqlSupplier, stateFun);
                afterExecute();
                return t;
            } catch (SQLException | XAException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected Integer executeBatch(Pair<String, List<List<Object>>> sqlPair) {
            return super.executeBatch(sqlPair);
        }

        private void beforeExecute() throws SQLException, XAException {
            rm = XaConnectionFactory.getXaConnect(this.getPool().getConnect()).getXAResource();
            // todo 下面的txid和txBranchId需要添补
            xid = XidFactory.getXid("", "", 1);
            aliveFlag = true;
            rm.start(xid, XAResource.TMNOFLAGS);
        }

        private void afterExecute() throws XAException {
            rm.end(xid, XAResource.TMSUCCESS);
        }
    }
}
