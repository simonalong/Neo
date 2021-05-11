package com.simonalong.neo.sql.builder;

import com.simonalong.neo.NeoMap;
import com.simonalong.neo.express.SearchQuery;
import com.simonalong.neo.tenant.TenantHandler;
import lombok.experimental.UtilityClass;

/**
 * @author shizi
 * @since 2020/3/22 下午7:51
 */
@UtilityClass
public class DeleteSqlBuilder extends BaseSqlBuilder {

    public String build(TenantHandler tenantHandler, String tableName, NeoMap valueMap) {
        stuffTenantId(tenantHandler, tableName, valueMap);
        return "delete from " + tableName + SqlBuilder.buildWhere(valueMap);
    }

    public String build(TenantHandler tenantHandler, String tableName, SearchQuery searchQuery) {
        stuffTenantId(tenantHandler, tableName, searchQuery);
        return "delete from " + tableName + searchQuery.toSql();
    }
}
