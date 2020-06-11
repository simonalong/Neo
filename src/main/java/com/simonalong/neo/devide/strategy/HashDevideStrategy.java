package com.simonalong.neo.devide.strategy;

import com.simonalong.neo.Neo;
import com.simonalong.neo.devide.TableDevideConfig;
import com.simonalong.neo.exception.NeoException;

import java.util.List;
import java.util.Map;

/**
 * 哈希分库策略
 *
 * @author shizi
 * @since 2020/6/10 5:15 PM
 */
public class HashDevideStrategy implements DevideStrategy {

    private Integer dbSize;
    private Integer markNum;
    /**
     * key：表名，value表的分表配置
     */
    private Map<String, TableDevideConfig> tableDevideConfigMap;

    public HashDevideStrategy(Integer dbSize, Map<String, TableDevideConfig> tableDevideConfigMap) {
        this.dbSize = dbSize;
        if ((dbSize & (dbSize - 1)) == 0) {
            this.markNum = dbSize - 1;
        }
        this.tableDevideConfigMap = tableDevideConfigMap;
    }

    @Override
    public Neo getDb(List<Neo> neoList, Object value) {
        if (null != neoList && neoList.size() == 1) {
            return neoList.get(0);
        }

        if (null == value) {
            return null;
        }

        if (value instanceof Number) {
            Integer num = Number.class.cast(value).intValue();
            if (null != neoList) {
                if (null != markNum) {
                    return neoList.get(num & markNum);
                } else {
                    return neoList.get(num % dbSize);
                }
            }
        }
        return null;
    }

    @Override
    public String getTable(String logicTableName, Object value) {
        if (value instanceof Number) {
            Integer num = Number.class.cast(value).intValue();
            if (null != tableDevideConfigMap && !tableDevideConfigMap.isEmpty()) {
                if (tableDevideConfigMap.containsKey(logicTableName)) {
                    TableDevideConfig tableDevideConfig = tableDevideConfigMap.get(logicTableName);
                    return logicTableName + (tableDevideConfig.getMin() + (num % tableDevideConfig.getSize()));
                }
            }
        }
        return logicTableName;
    }
}
