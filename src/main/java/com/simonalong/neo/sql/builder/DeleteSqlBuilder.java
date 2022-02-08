package com.simonalong.neo.sql.builder;

import com.simonalong.neo.NeoMap;
import com.simonalong.neo.express.SearchQuery;
import com.simonalong.neo.tenant.TenantHandler;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shizi
 * @since 2020/3/22 下午7:51
 */
@Slf4j
@UtilityClass
public class DeleteSqlBuilder extends BaseSqlBuilder {

    public String build(TenantHandler tenantHandler, String tableName, NeoMap valueMap) {
        stuffTenantId(tenantHandler, tableName, valueMap);
        String searchSql = SqlBuilder.buildWhere(valueMap);
        if ("".equals(searchSql)) {
            log.warn("delete函数搜索条件为空，请使用手写sql执行");
            return null;
        }
        return "delete from " + tableName + searchSql;
    }

    public String build(TenantHandler tenantHandler, String tableName, SearchQuery searchQuery) {
        stuffTenantId(tenantHandler, tableName, searchQuery);
        String searchSql = searchQuery.toSql();
        if ("".equals(searchSql)) {
            log.warn("delete函数搜索条件为空，请使用手写sql执行");
            return null;
        }
        return "delete from " + tableName + searchSql;
    }
}
