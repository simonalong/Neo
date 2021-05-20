package com.simonalong.neo.dialet;

import com.alibaba.druid.filter.AutoLoad;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 对值处理的函数枚举
 *
 * @author shizi
 * @since 2021-05-19 15:40:22
 */
@AllArgsConstructor
public enum FunctionEnum {

    INET4_ATON("ipv3地址转换为整数"),
    INET4_NTOA("整数转换为ipv3");

    @Getter
    private final String desc;

    private static final Map<Integer, FunctionEnum> indexEnumMap;
    private static final Map<String, FunctionEnum> nameEnumMap;

    static {
        indexEnumMap = Arrays.stream(FunctionEnum.values()).collect(Collectors.toMap(FunctionEnum::ordinal, e -> e));
        nameEnumMap = Arrays.stream(FunctionEnum.values()).collect(Collectors.toMap(FunctionEnum::name, e -> e));
    }

    public static FunctionEnum parse(Integer index) {
        if (!indexEnumMap.containsKey(index)) {
            throw new RuntimeException("不支持下标: " + index);
        }
        return indexEnumMap.get(index);
    }

    public static FunctionEnum parse(String name) {
        if (!nameEnumMap.containsKey(name)) {
            throw new RuntimeException("不支持name: " + name);
        }
        return nameEnumMap.get(name);
    }
}
