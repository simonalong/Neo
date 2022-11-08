package com.simonalong.neo.sql.builder;


import com.simonalong.neo.NeoMap;
import com.simonalong.neo.TableMap;
import com.simonalong.neo.express.BaseOperate;
import com.simonalong.neo.express.SearchQuery;
import com.simonalong.neo.tenant.TenantContextHolder;
import com.simonalong.neo.tenant.TenantHandler;

import java.util.List;
import java.util.Set;

/**
 * @author shizi
 * @since 2021-05-06 15:40:12
 */
public class BaseSqlBuilder {

    /**
     * 给表对应的搜索值添加搜索字段
     *
     * @param tenantHandler           多租户处理器
     * @param tableName               表名
     * @param valueMap                新增的值
     */
    static protected void stuffTenantId(TenantHandler tenantHandler, String tableName, NeoMap valueMap) {
        if (null == tenantHandler) {
            return;
        }

        if (tenantHandler.containTable(tableName)) {
            if (!valueMap.containsKey(tenantHandler.getColumnName())) {
                String tenantId = TenantContextHolder.getTenantId();
                if (null == tenantId || "".equals(tenantId)) {
                    return;
                }

                valueMap.put(tenantHandler.getColumnName(), tenantId);
            }
        }
    }

    static public void stuffTenantId(TenantHandler tenantHandler, String tableName, SearchQuery searchQuery) {
        if (null == tenantHandler) {
            return;
        }

        if (tenantHandler.containTable(tableName)) {
            if (!searchQuery.containKey(tenantHandler.getColumnName())) {
                String tenantId = TenantContextHolder.getTenantId();
                if (null == tenantId || "".equals(tenantId)) {
                    return;
                }
                searchQuery.and(BaseOperate.AndEmTable(tableName, tenantHandler.getColumnName(), tenantId));
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
                    String tenantId = TenantContextHolder.getTenantId();
                    if (null == tenantId || "".equals(tenantId)) {
                        return;
                    }
                    valueMap.put(tenantHandler.getColumnName(), tenantId);
                }
            }
        }
    }

    static protected void stuffTenantId(TenantHandler tenantHandler, TableMap searchMap) {
        if (null == tenantHandler) {
            return;
        }

        String tenantId = TenantContextHolder.getTenantId();
        if (null == tenantId || "".equals(tenantId)) {
            return;
        }

        searchMap.getDataMap().forEach((k, v) -> {
            if (!v.containsKey(tenantHandler.getColumnName())) {
                v.put(tenantHandler.getColumnName(), tenantId);
            }
        });
    }

    static protected void stuffTenantId(TenantHandler tenantHandler, SearchQuery searchQuery) {
        if (null == tenantHandler) {
            return;
        }

        String tenantId = TenantContextHolder.getTenantId();
        if (null == tenantId || "".equals(tenantId)) {
            return;
        }

        Set<String> tableSet = searchQuery.getTablaNameSet();
        for (String tableName : tableSet) {
            if (!searchQuery.containKey(tableName, tenantHandler.getColumnName())) {
                searchQuery.and(BaseOperate.AndEmTable(tableName, tenantHandler.getColumnName(), tenantId));
            }
        }
    }
}
