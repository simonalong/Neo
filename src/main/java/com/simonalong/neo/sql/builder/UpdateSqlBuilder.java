package com.simonalong.neo.sql.builder;

import com.simonalong.neo.Columns;
import com.simonalong.neo.NeoMap;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author shizi
 * @since 2020/3/22 下午8:04
 */
@UtilityClass
public class UpdateSqlBuilder {

    public String build(String tableName, NeoMap dataMap, NeoMap searchMap) {
        return "update " + tableName + buildSetValues(dataMap) + SqlBuilder.buildWhere(searchMap);
    }

    public String buildSetValues(NeoMap searchMap) {
        return " set " + String.join(", ", searchMap.keySet().stream().map(f -> SqlBuilder.toDbField(f) + "=?").collect(Collectors.toList()));
    }

    /**
     * 构造批量更新语句，采用新的方式进行处理，用来在数量更多时候提高性能
     *
     * <p>比如：{@code
     * update table_name a join
     * (
     * select 1 as `id`, 'holy' as `name`
     * union
     * select 2 as `id`, 'shit' as `name`
     * ) b using(`id`)
     * set a.`name`=b.`name`;
     * }
     * @return update 批量更新语句
     * @since 0.5.2
     */
    public String buildBatch(String tableName, List<NeoMap> updateDataColumnList, Columns conditionColumns) {
        return "update " + tableName + " a join(" + getUnionSql(updateDataColumnList) + ") b using(" + getSearchColumns(conditionColumns) + ") set " + getUpdateKey(updateDataColumnList, conditionColumns);
    }

    /**
     * 返回{@code
     * select ? as `id`, ? as `name`
     * union
     * select ? as `id`, ? as `name`
     * union
     * select ? as `id`, ? as `name`
     * }
     * @param updateDataColumnList 数据
     * @return sql
     */
    @SuppressWarnings("all")
    private String getUnionSql(List<NeoMap> updateDataColumnList) {
        if (null == updateDataColumnList || updateDataColumnList.isEmpty()) {
            return "";
        }

        return updateDataColumnList.stream().map(e->{
            return " select " + e.entrySet().stream().map(m-> "? as " + SqlBuilder.toDbField(m.getKey())).collect(Collectors.joining(", "));
        }).collect(Collectors.joining(" union "));
    }

    /**
     * 返回{@code `id`, `name`, `group`}
     * @param columns 列
     * @return sql
     */
    private String getSearchColumns(Columns columns) {
        return columns.toSelectString();
    }

    /**
     * 设置值的列
     * 返回{@code a.`name`=b.`name`, a.`group`=b.`group`}
     *
     * @param updateDataColumnList 更新的列的集合
     * @param columns 列的集合
     * @return sql
     */
    private String getUpdateKey(List<NeoMap> updateDataColumnList, Columns columns) {
        if (null == updateDataColumnList || updateDataColumnList.isEmpty() || null == columns) {
            return "";
        }

        return updateDataColumnList.stream()
            // 排除掉搜索的列
            .map(e->e.assignExcept(columns))
            // 获取所有的列
            .flatMap(NeoMap::keyStream)
            // 去重
            .distinct()
            .map(e-> "a." + SqlBuilder.toDbField(e) + "=" + "b." + SqlBuilder.toDbField(e))
            .collect(Collectors.joining(", "));
    }
}
