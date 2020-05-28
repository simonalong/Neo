package com.simonalong.neo.sql;

import com.simonalong.neo.sql.builder.SqlBuilder;

import java.util.List;

/**
 * @author shizi
 * @since 2020/4/19 6:08 PM
 */
public class InList {

    private List dataList;

    public InList(List collection) {
        this.dataList = collection;
    }

    @SuppressWarnings("unchecked")
    public String buildSql(String key) {
        if (null == dataList || dataList.isEmpty()) {
            return null;
        }
        return key + " in " + SqlBuilder.buildIn(dataList);
    }
}
