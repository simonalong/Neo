package com.simonalong.neo.util;

import lombok.experimental.UtilityClass;

/**
 * @author shizi
 * @since 2020/8/30 2:32 上午
 */
@UtilityClass
public class LogicOperateUtil {

    /**
     * 过滤逻辑操作符的头部
     *
     * @param operateStr 逻辑处理字符
     * @return 过滤逻辑处理操作符后的字符串
     */
    public String filterLogicHead(String operateStr) {
        if (null == operateStr) {
            return "";
        }
        String result = operateStr.trim();
        if (result.startsWith("and ") || result.startsWith("or ")) {
            if (result.startsWith("and ")) {
                result = result.substring("and ".length()).trim();
            }
            if (result.startsWith("or ")) {
                result = result.substring("or ".length()).trim();
            }
            return filterLogicHead(result);
        }
        return result;
    }
}
