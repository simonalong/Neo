package com.simonalong.neo.sql.builder;

import com.simonalong.neo.Columns;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.TableMap;
import com.simonalong.neo.db.TableJoinOn;
import com.simonalong.neo.express.SearchQuery;
import lombok.experimental.UtilityClass;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author shizi
 * @since 2020/3/22 下午8:50
 */
@UtilityClass
public class JoinSqlBuilder {


    public String build(Columns columns, TableJoinOn joiner, TableMap searchMap) {
        return "select " + buildColumns(columns) + " from " + buildJoinOn(joiner) + buildConditionWithWhere(searchMap);
    }

    public String build(Columns columns, TableJoinOn joiner, SearchQuery searchQuery) {
        return "select " + buildColumns(columns) + " from " + buildJoinOn(joiner) + buildConditionWithWhere(searchQuery);
    }

    public String buildDistinct(Columns columns, TableJoinOn joiner, TableMap searchMap) {
        return "select distinct " + buildColumns(columns) + " from " + buildJoinOn(joiner) + buildConditionWithWhere(searchMap);
    }

    public String buildDistinct(Columns columns, TableJoinOn joiner, SearchQuery searchQuery) {
        return "select distinct " + buildColumns(columns) + " from " + buildJoinOn(joiner) + buildConditionWithWhere(searchQuery);
    }

    public String buildCount(TableJoinOn joiner, TableMap searchMap) {
        return "select count(1) from " + buildJoinOn(joiner) + buildConditionWithWhere(searchMap);
    }

    public String buildCount(TableJoinOn joiner, SearchQuery searchQuery) {
        return "select count(1) from " + buildJoinOn(joiner) + buildConditionWithWhere(searchQuery);
    }

    /**
     * 拼接join中的 select 后面的选项
     *
     * <p>
     *     多个表的多个列，都可以展示
     *
     * @param columns   列名
     * @return 拼接字段：table2.`id` as table2_NDom_id, table1.`name` as table1_NDom_name, table2.`user_name` as table2_NDom_user_name, table1.`group` as table1_NDom_group
     */
    public String buildColumns(Columns columns) {
        return columns.toSelectString();
    }

    /**
     * 获取多表join的sql部分
     *
     * @param tableJoinOn joiner
     * @return table1 left join table2 on table1.`id` = table2.`a_id` inner join table3.`id` on table2.`c_id` = table3.`id`
     */
    public String buildJoinOn(TableJoinOn tableJoinOn) {
        return tableJoinOn.getJoinSql();
    }

    public String buildConditionWithWhere(TableMap tableMap) {
        if (!TableMap.isEmpty(tableMap)) {
            return " where " + buildWhereCondition(tableMap);
        }
        return "";
    }

    public String buildConditionWithWhere(SearchQuery searchQuery) {
        return searchQuery.toSql();
    }

    /**
     * 返回where后面的带有占位符的条件sql
     *
     * @param searchMap 搜索条件map
     * @return 比如：`group` = ? and `name` = ?
     */
    public String buildWhereCondition(TableMap searchMap) {
        StringBuilder stringBuilder = new StringBuilder();
        if (TableMap.isUnEmpty(searchMap)) {
            return stringBuilder.append(String.join(" and ", buildConditionMeta(searchMap))).toString();
        }
        return stringBuilder.toString();
    }

    public List<String> buildConditionMeta(TableMap searchMap) {
        return searchMap.clone().entrySet().stream().flatMap(e->{
            String tableName = e.getKey();
            NeoMap valueMap = (NeoMap) e.getValue();
            return SqlBuilder.buildConditionMeta(valueMap).stream().map(v->tableName + "." + v);
        }).collect(Collectors.toList());
    }

    public List<Object> buildValueList(TableMap searchMap) {
        if (TableMap.isEmpty(searchMap)) {
            return Collections.emptyList();
        }

        return searchMap.clone().entrySetOfSort().stream().flatMap(e-> ((NeoMap) e.getValue()).valueQueue().stream()).collect(Collectors.toList());
    }

    public List<Object> buildValueList(SearchQuery searchQuery) {
        return searchQuery.toValue();
    }
}
