package com.simonalong.neo.db.xa;

import com.alibaba.fastjson.JSON;
import com.simonalong.neo.Neo;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.exception.NumberOfValueException;
import com.simonalong.neo.exception.ParameterNullException;
import com.simonalong.neo.exception.xa.XaCommitException;
import com.simonalong.neo.exception.xa.XaEndException;
import com.simonalong.neo.exception.xa.XaPrepareException;
import com.simonalong.neo.exception.xa.XaStartException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhouzhenyong
 * @since 2019/11/20 下午11:17
 */
@Slf4j
public class NeoXa {

    /**
     * key为当前db的名字，value为Neo的动态代理对象
     */
    private static Map<String, NeoXaProxy> dbMap = new ConcurrentHashMap<>(8);
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
            Object key = kvs[i];
            if (null == key) {
                throw new ParameterNullException("NeoMap.of()中的参数不可为null");
            }

            if (!(key instanceof String)) {
                throw new RuntimeException("key 类型必须为String类型");
            }

            dbMap.put((String) key, new NeoXaProxy(kvs[i + 1]));
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
        dbMap.putIfAbsent(alias, new NeoXaProxy(dataSource));
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
        dbMap.putIfAbsent(alias, new NeoXaProxy(neo));
        return this;
    }

    /**
     * 获取对应的db数据
     *
     * @param alias db别名
     * @return neo对象
     */
    public Neo get(String alias) {
        if (!dbMap.containsKey(alias)) {
            log.warn("没有找到名字为{} 的db", alias);
            return null;
        }
        return dbMap.get(alias).getTarget();
    }

    public void run(Runnable runnable) {
        try {
            startXid();
            runnable.run();
            endXid();
            prepareXid();
            commitXid();
        } catch (Throwable e) {
            if (e instanceof XaStartException) {
                log.error("start xid fail");
            } else if (e instanceof XaEndException) {
                log.error("end xid fail");
            } else if (e instanceof XaPrepareException) {
                log.error("prepare xid fail");
                rollbackXid();
            } else if (e instanceof XaCommitException) {
                log.error("commit xid fail");
                rollbackXid();
            }
            log.error("xa run fail, xid={}", getXidStr(), e);
        }
    }

    private List<String> getXidStr() {
        return dbMap.values().stream().map(proxy -> {
            String gid = new String(proxy.getXid().getGlobalTransactionId());
            String branchId = new String(proxy.getXid().getBranchQualifier());
            String formatId = String.valueOf(proxy.getXid().getFormatId());
            return NeoMap.of("gid", gid, "branchId", branchId, "formatId", formatId).toString();
        }).collect(Collectors.toList());
    }

    /**
     * 执行 xa start xid
     */
    private void startXid() throws XaStartException {
        try {
            for (NeoXaProxy proxy : dbMap.values()) {
                proxy.openXa();
            }
        } catch (SQLException | XAException e) {
            throw new XaStartException(e);
        }
    }

    /**
     * 执行 xa end xid
     */
    private void endXid() throws XaEndException {
        try {
            for (NeoXaProxy proxy : dbMap.values()) {
                proxy.endXid();
            }
        } catch (XAException e) {
            throw new XaEndException(e);
        }
    }

    /**
     * 执行 xa prepare xid
     */
    private void prepareXid() throws XaPrepareException {
        try {
            for (NeoXaProxy proxy : dbMap.values()) {
                if (proxy.getRm().prepare(proxy.getXid()) != XAResource.XA_OK) {
                    throw new XaPrepareException();
                }
            }
        } catch (XAException e) {
            throw new XaPrepareException(e);
        }
    }

    /**
     * 执行 xa commit xid
     */
    private void commitXid() throws XaCommitException {
        try {
            for (NeoXaProxy proxy : dbMap.values()) {
                proxy.getRm().commit(proxy.getXid(), false);
            }
        } catch (XAException e) {
            throw new XaCommitException(e);
        }
    }

    /**
     * 执行 xa rollback xid
     */
    private void rollbackXid() {
        try {
            for (NeoXaProxy proxy : dbMap.values()) {
                proxy.getRm().rollback(proxy.getXid());
            }
        } catch (XAException e) {
            log.error("rollback error", e);
        }
    }
}
