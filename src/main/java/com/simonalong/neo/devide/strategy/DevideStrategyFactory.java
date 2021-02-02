package com.simonalong.neo.devide.strategy;

import com.simonalong.neo.devide.TableDevideConfig;

import java.util.Map;

/**
 * @author shizi
 * @since 2020/6/10 6:25 PM
 */
public class DevideStrategyFactory {

    public static DevideStrategy getStrategy(DevideTypeEnum devideTypeEnum, Integer dbSize, Map<String, TableDevideConfig> tableDevideConfigMap) {
        switch (devideTypeEnum) {
            case HASH:
                return new HashDevideStrategy(dbSize, tableDevideConfigMap);
            case UUID_HASH:
                return new UuidHashDevideStrategy(dbSize, tableDevideConfigMap);
            default:
                return new HashDevideStrategy(dbSize, tableDevideConfigMap);
        }
    }
}
