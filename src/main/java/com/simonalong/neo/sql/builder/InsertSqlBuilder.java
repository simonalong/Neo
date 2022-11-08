package com.simonalong.neo.sql.builder;

import com.simonalong.neo.NeoMap;
import com.simonalong.neo.tenant.TenantHandler;
import lombok.experimental.UtilityClass;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author shizi
 * @since 2020/3/22 下午7:49
 */
@UtilityClass
public class InsertSqlBuilder extends BaseSqlBuilder {

    /**
     * 返回insert的拼接sql
     *
     * @param tenantHandler 租户管理器
     * @param tableName     表名
     * @param valueMap      数据实体
     * @return 拼接的sql，比如：insert into table1 (`age`, `name`) values (?, ?)
     */
    public String build(TenantHandler tenantHandler, String tableName, NeoMap valueMap) {
        stuffTenantId(tenantHandler, tableName, valueMap);
        return "insert into " + tableName + " (" + buildInsertTable(valueMap.keySet()) + ") values (" + buildInsertValues(valueMap) + ")";
    }

    /**
     * 返回数据不存在时候的 insert 的拼接sql
     *
     * @param tenantHandler 租户管理器
     * @param tableName     表名
     * @param valueMap      数据实体
     * @param searchMap     搜索条件
     * @return 拼接的sql，比如：insert into demo1(`name`, `group`) select ?, ? where not exists (select * from demo1 where `name` = ?);
     */
    public String buildInsertOfUnExist(TenantHandler tenantHandler, String tableName, NeoMap valueMap, NeoMap searchMap) {
        if (NeoMap.isEmpty(valueMap)) {
            return null;
        }
        stuffTenantId(tenantHandler, tableName, valueMap);
        return "insert into " + tableName + " (" + buildInsertTable(valueMap.keySet()) + ") select " + buildInsertValues(valueMap) + " where not exists (select * from " + tableName + SqlBuilder.buildWhere(searchMap) + ")";
    }

    /**
     * `name`, `group`
     *
     * @param keys keys
     * @return tableName后面括号中的sql
     */
    public String buildInsertTable(Set<String> keys) {
        return keys.stream().map(SqlBuilder::toDbField).collect(Collectors.joining(", "));
    }

    /**
     * 返回占位符
     *
     * @param valueMap 条件
     * @return 占位符拼接：?, ?, ?, ?
     */
    private String buildInsertValues(NeoMap valueMap) {
        return valueMap.keySet().stream().map(f -> "?").collect(Collectors.joining(", "));
    }
}
