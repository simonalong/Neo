package com.simonalong.neo.db.xa;

import com.mysql.cj.jdbc.MysqlXid;
import com.simonalong.neo.Neo;
import com.simonalong.neo.exception.NeoXAException;
import com.simonalong.neo.exception.NumberOfValueException;
import com.simonalong.neo.exception.ParameterNullException;
import com.simonalong.neo.exception.ParameterUnValidException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.simonalong.neo.util.EncryptUtil;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;

import static com.simonalong.neo.NeoConstant.LOG_PRE;

/**
 * @author zhouzhenyong
 * @since 2019/11/20 下午11:17
 */
@Slf4j
public class NeoXa {

    private static Map<String, Neo> neoMap = new ConcurrentHashMap<>(8);
    /**
     * 资源管理器对应的发号器仓库
     */
    private Neo tm;
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

            Object value = kvs[i + 1];
            if (value instanceof Neo) {
                Neo db = (Neo) value;
                neoMap.put((String) kvs[i], db);
            } else if (value instanceof DataSource) {
                Neo db = Neo.connect((DataSource) value);
                neoMap.put((String) kvs[i], db);
            } else {
                throw new ParameterUnValidException("value = " + kvs[i + 1].getClass().toString() + "不是Neo也不是Datasource类型");
            }
        }
        return xa;
    }

    /**
     * 添加db数据
     *
     * @param alias      数据库别名
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
     * @param neo   数据库对象
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
     * @return neoXa对象
     */
    public Neo get(String alias) {
        return neoMap.get(alias);
    }

    public void run(Runnable runnable) {
        beforeProcess();
        runnable.run();
        afterProcess();
    }

    /**
     * 设置事务管理器
     * <p>
     * 这里事务管理器主要用于生成事务id
     *
     * @param tmDb 事务管理器中包含全局id表的DB
     * @return neoXa对象
     */
    public NeoXa setTmDb(Neo tmDb) {
        this.tm = tmDb;
        return this;
    }

    /**
     * 前置处理，用于初始化全局事务id
     */
    private void beforeProcess() {
        this.tm.openUidGenerator();
        String globalTrId = getGlobalTrId();
        neoMap.values().forEach(n -> {
            n.openXAFlag();
            n.setXid(new MysqlXid(globalTrId.getBytes(), getBranchQualifierId().getBytes(), 1));
        });
    }

    private void afterProcess() {
        boolean allTxSuccess = true;
        List<Neo> neoList = neoMap.values().stream().filter(Neo::getSuccessFlag).collect(Collectors.toList());
        for (Neo neo : neoList) {
            try {
                if (neo.getRm().prepare(neo.getXid()) != XAResource.XA_OK) {
                    allTxSuccess = false;
                    break;
                }
            } catch (XAException e) {
                throw new NeoXAException("prepare fail", e);
            }
        }

        if (allTxSuccess) {
            log.info(LOG_PRE + " tx all success, ready to commit");
            neoList.forEach(n -> {
                try {
                    n.getRm().commit(n.getXid(), false);
                } catch (XAException e) {
                    throw new NeoXAException("commit fail", e);
                }
            });
        } else {
            log.warn(LOG_PRE + " tx have fail, ready to rollback");
            neoList.forEach(n -> {
                try {
                    n.getRm().rollback(n.getXid());
                } catch (XAException e) {
                    throw new NeoXAException("rollback fail", e);
                }
            });
        }
    }

    private String getGlobalTrId() {
        return EncryptUtil.MD5(tm.getUuid().toString());
    }

    private String getBranchQualifierId() {
        return EncryptUtil.MD5(tm.getUuid().toString());
    }
}
