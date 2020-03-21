package com.simonalong.neo.uid.snowflake;

import com.simonalong.neo.Neo;
import com.simonalong.neo.uid.snowflake.handler.WorkerIdHandler;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;


/**
 * 对应命名空间下所有节点信息进行统一管理
 *
 * @author shizi
 * @since 2020/2/6 10:50 上午
 */
@Slf4j
public class NamespaceNodeManager {

    private static volatile NamespaceNodeManager INSTANCE = null;
    /**
     * worker节点操作map
     */
    private Map<String, WorkerIdHandler> workerIdHandlerMap = new HashMap<>(12);
    @Setter
    private Neo db;

    private NamespaceNodeManager() {
    }

    public static NamespaceNodeManager getInstance() {
        if (null == INSTANCE) {
            synchronized (NamespaceNodeManager.class) {
                if (null == INSTANCE) {
                    INSTANCE = new NamespaceNodeManager();
                }
            }
        }
        return INSTANCE;
    }

//    public void add(String namespace) {
//        // 包含则不再添加
//        if (workerIdHandlerMap.containsKey(namespace)) {
//            return;
//        }
//
//        workerIdHandlerMap.putIfAbsent(namespace, new DefaultWorkerIdHandler(namespace, zkClient, configNodeHandler));
//    }


}
