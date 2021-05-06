package com.simonalong.neo.sql.builder;

import com.simonalong.neo.NeoMap;
import com.simonalong.neo.express.SearchQuery;
import com.simonalong.neo.tenant.TenantContextHolder;
import com.simonalong.neo.tenant.TenantHandler;

import java.util.List;

/**
 * @author shizi
 * @since 2021-05-06 15:40:12
 */
public class BaseSqlBuilder {

    /**
     * 给表对应的搜索值添加搜索字段
     *
     * @param tableName 表名
     * @param valueMap 新增的值
     */
    static protected void stuffTenantId(TenantHandler tenantHandler, String tableName, NeoMap valueMap) {
        if (null == tenantHandler) {
            return;
        }

        if (tenantHandler.containTable(tableName)) {
            if (!valueMap.containsKey(tenantHandler.getColumnName())) {
                valueMap.put(tenantHandler.getColumnName(), TenantContextHolder.getTenantId());
            }
        }
    }

    static protected void stuffTenantId(TenantHandler tenantHandler, String tableName, SearchQuery searchQuery) {
        if (null == tenantHandler) {
            return;
        }

        if (tenantHandler.containTable(tableName)) {
            if (!searchQuery.containKey(tenantHandler.getColumnName())) {
                searchQuery.andEm(tenantHandler.getColumnName(), TenantContextHolder.getTenantId());
            }
        }
    }

    static protected void stuffTenantId(TenantHandler tenantHandler, String tableName, List<NeoMap> valueMapList) {
        if (null == tenantHandler) {
            return;
        }

        if (tenantHandler.containTable(tableName)) {
            for (NeoMap valueMap : valueMapList) {
                if (!valueMap.containsKey(tenantHandler.getColumnName())) {
                    valueMap.put(tenantHandler.getColumnName(), TenantContextHolder.getTenantId());
                }
            }
        }
    }
}
