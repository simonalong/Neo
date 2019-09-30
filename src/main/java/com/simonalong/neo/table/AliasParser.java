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
     * 获取别名
     * {@code group as g --> g} {@code group g --> g}
     *
     * @param dataStr 表名或者字段列
     * @return 表的别名或者列的别名
     */
    public String getAlias(String dataStr){
        String asStr = " as ";
        String spaceStr = " ";
        dataStr = dataStr.trim();
        if(dataStr.contains(asStr)){
            return dataStr.substring(dataStr.indexOf(asStr) + asStr.length()).trim();
        }

        if (dataStr.contains(spaceStr)){
            return dataStr.substring(dataStr.indexOf(spaceStr) + spaceStr.length()).trim();
        }
        return dataStr;
    }

    /**
     * 获取名字的原名
     * {@code table1 as t1 --> table1} {@code table1 t1--> table1}
     * @return 表名或者列名的原名字
     */
    public String getOrigin(String tableNameStr){
        String asStr = " as ";
        String spaceStr = " ";
        tableNameStr = tableNameStr.trim();
        if(tableNameStr.contains(asStr)){
            return tableNameStr.substring(0, tableNameStr.indexOf(asStr)).trim();
        }

        if (tableNameStr.contains(spaceStr)){
            return tableNameStr.substring(0, tableNameStr.indexOf(spaceStr)).trim();
        }
        return tableNameStr;
    }
}
