package com.simonalong.neo.table;

import lombok.experimental.UtilityClass;

/**
 * 别名解析工具
 *
 * @author zhouzhenyong
 * @since 2019/5/12 上午12:45
 */
@UtilityClass
public class AliasParser {

    /**
     * 判断列是否有别名
     * @param fieldStr 列名
     * @return true 有别名，false：没有别名
     */
    public Boolean haveAlias(String fieldStr){
        String asStr = " as ";
        String spaceStr = " ";
        fieldStr = fieldStr.trim();
        return fieldStr.contains(asStr) || fieldStr.contains(spaceStr);
    }

    /**
     * 去除别名字段之后的字段：{@code table.name as n --> table.name} {@code table.name n}
     * @param fieldStr 字段列
     * @return 去除别名后的名字
     */
    public String metaData(String fieldStr){
        String asStr = "as";
        String spaceStr = " ";
        if(fieldStr.contains(asStr)){
            return fieldStr.substring(0, fieldStr.indexOf(asStr)).trim();
        }

        if (fieldStr.contains(spaceStr)){
            return fieldStr.substring(0, fieldStr.indexOf(spaceStr)).trim();
        }
        return fieldStr;
    }

    /**
     * 获取列的别名
     * {@code group as g --> g} {@code group g --> g}
     *
     * @param fieldStr 字段列
     * @return 列的别名
     */
    public String getAlias(String fieldStr){
        String asStr = " as ";
        String spaceStr = " ";
        fieldStr = fieldStr.trim();
        if(fieldStr.contains(asStr)){
            return fieldStr.substring(fieldStr.indexOf(asStr) + asStr.length()).trim();
        }

        if (fieldStr.contains(spaceStr)){
            return fieldStr.substring(fieldStr.indexOf(spaceStr) + spaceStr.length()).trim();
        }
        return fieldStr;
    }
}
