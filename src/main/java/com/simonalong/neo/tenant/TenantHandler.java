package com.simonalong.neo.tenant;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 多租户处理器
 *
 * @author shizi
 * @since 2021-05-06 10:49:37
 */
public class TenantHandler {

    private Boolean includeAll = false;
    private Boolean excludeAll = false;
    private final List<String> includeTables = new ArrayList<>();
    private final List<String> excludeTables = new ArrayList<>();
    @Getter
    private String columnName;

    /**
     * 设置支持多租户的表
     *
     * @param tables 多个表名
     */
    public void setIncludeTables(String... tables) {
        if (null == tables) {
            return;
        }
        if (tables.length == 1) {
            if ("*".equals(tables[0])) {
                includeAll = true;
                return;
            }
        }
        includeTables.addAll(Arrays.asList(tables));
    }

    /**
     * 设置不支持多租户的表
     *
     * @param tables 多个表名
     */
    public void setExcludeTables(String... tables) {
        if (null == tables) {
            return;
        }
        if (tables.length == 1) {
            if ("*".equals(tables[0])) {
                excludeAll = true;
                return;
            }
        }
        excludeTables.addAll(Arrays.asList(tables));
    }

    /**
     * 设置租户字段列名
     *
     * @param columnName 列名
     */
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Boolean containTable(String tableName) {
        if (excludeAll) {
            return false;
        }

        if (excludeTables.contains(tableName)) {
            return false;
        }

        if (includeAll) {
            return true;
        }

        return includeTables.contains(tableName);
    }
}
