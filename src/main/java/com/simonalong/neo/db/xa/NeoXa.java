package com.simonalong.neo.db.xa;

import com.simonalong.neo.Neo;
import com.simonalong.neo.exception.NumberOfValueException;
import com.simonalong.neo.exception.ParameterNullException;
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

    private static Map<String, NeoProxy> dbMap = new ConcurrentHashMap<>(8);
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

            dbMap.put((String) key, new NeoProxy(kvs[i + 1]));
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
        dbMap.putIfAbsent(alias, new NeoProxy(dataSource));
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
        dbMap.putIfAbsent(alias, new NeoProxy(neo));
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
        return dbMap.get(alias).getProxy();
    }

    public void run(Runnable runnable) {
        runnable.run();
        afterProcess();
    }

    private void afterProcess() {
        // 全部事务成功标示
        Boolean allSuccess = true;
        List<NeoProxy> neoProxyList = dbMap.values().stream().filter(NeoProxy::getAliveFlag).collect(Collectors.toList());
        for (NeoProxy neoProxy : neoProxyList) {
            try {
                // 任何一个事务失败，则全局事务标示为失败
                if (neoProxy.getRm().prepare(neoProxy.getXid()) != XAResource.XA_OK) {
                    allSuccess = false;
                }
            } catch (XAException e) {
                e.printStackTrace();
            }
        }

        if (allSuccess) {
            neoProxyList.forEach(n -> {
                try {
                    n.getRm().commit(n.getXid(), false);
                } catch (XAException e) {
                    e.printStackTrace();
                }
            });
        } else {
            neoProxyList.forEach(n -> {
                try {
                    n.getRm().rollback(n.getXid());
                } catch (XAException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
