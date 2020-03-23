package com.simonalong.neo.sql.builder;

import com.simonalong.neo.Columns;
import com.simonalong.neo.Neo;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.TableMap;
import com.simonalong.neo.db.NeoJoiner;
import lombok.experimental.UtilityClass;

import java.util.*;
import java.util.stream.Collectors;

import static com.simonalong.neo.NeoConstant.ASC;
import static com.simonalong.neo.NeoConstant.DESC;
import static com.simonalong.neo.NeoConstant.ORDER_BY;

/**
 * @author shizi
 * @since 2020/3/22 下午8:50
 */
@UtilityClass
public class JoinSqlBuilder {


    public String build(Columns columns, NeoJoiner joiner, TableMap searchMap) {
        return "select " + buildColumns(columns) + " from " + buildJoinOn(joiner) + buildConditionWithWhere(searchMap) + buildOrderBy(searchMap);
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
     * @param neoJoiner joiner
     * @return table1 left join table2 on table1.`id` = table2.`a_id` inner join table3.`id` on table2.`c_id` = table3.`id`
     */
    public String buildJoinOn(NeoJoiner neoJoiner) {
        return neoJoiner.getJoinSql();
    }

    public String buildConditionWithWhere(TableMap tableMap) {
        tableMap = tableMap.assignExceptKeys(ORDER_BY);
        if (!TableMap.isEmpty(tableMap)) {
            return " where " + buildWhereCondition(tableMap);
        }
        return "";
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

    /**
     * join的head 部分对应的sql，主要是选择的列
     *
     * @param tableName 表名
     * @param columnName 表选择的列
     * @return join对应的head，比如：select xxx,xxx
     */
    public String buildJoinHead(String tableName, String columnName){
        return "select " + tableName + "." + columnName;
    }

    /**
     * join的head 部分对应的sql，主要是选择的列
     *
     * @param neo 库对象
     * @param columns 多个表的列信息
     * @return join对应的head，比如：select xxx,xxx
     */
    public String buildJoinHead(Neo neo, Columns columns) {
        return "select " + columns.setNeo(neo).toString();
    }


    /**
     * 生成拼接的join的条件{@code where x=? and y=? and a.m=? and b.n=?}
     *
     * @param sqlCondition 对于join_except类型需要用的一些排除条件
     * @param searchMap 搜索条件map
     * @return 返回拼接的sql
     */
    public String buildJoinTail(String sqlCondition, NeoMap searchMap) {
        StringBuilder sb = new StringBuilder();
        Boolean sqlConditionNotEmpty = null != sqlCondition && !"".equals(sqlCondition);
        Boolean searchMapsNotEmpty = !isEmptyExceptOrderBy(searchMap);
        if (sqlConditionNotEmpty || searchMapsNotEmpty) {
            sb.append(" where ");
        }

        if (sqlConditionNotEmpty) {
            sb.append("(").append(sqlCondition).append(")");
        }

        if (sqlConditionNotEmpty && searchMapsNotEmpty) {
            sb.append(" and ");
        }

        if (searchMapsNotEmpty) {
            sb.append(SqlBuilder.buildWhereCondition(searchMap)).append(SqlBuilder.buildOrderBy(searchMap));
        }else{
            sb.append(SqlBuilder.buildOrderBy(searchMap));
        }
        return sb.toString();
    }

//    /**
//     * join 的join 部分对应的sql
//     *
//     * @param leftTableName 左表名
//     * @param leftColumnName 左表的on列名
//     * @param rightTableName 右表名
//     * @param rightColumnName 右表的on列名
//     * @param joinType join的类型
//     * @return join对应的部分sql，比如：from leftTableName xxJoin rightTableName on leftTableName.`leftColumnName` = rightTableName.`rightColumnName`
//     */
//    private String buildJoin(String joinLeftTableName, String leftTableName, String leftColumnName,
//        String rightTableName, String rightColumnName, JoinType joinType) {
//        if(null != joinLeftTableName){
//            return joinLeftTableName + " " + joinType.getSql() + " " + rightTableName
//                + " on " + leftTableName + "." + SqlBuilder.toDbField(leftColumnName) + "=" + rightTableName + "." + SqlBuilder.toDbField(rightColumnName);
//        }else{
//            return " " + joinType.getSql() + " " + rightTableName
//                + " on " + leftTableName + "." + SqlBuilder.toDbField(leftColumnName) + "=" + rightTableName + "." + SqlBuilder.toDbField(rightColumnName);
//        }
//    }

//    /**
//     * 创建在排除公共部分的join中对应的where条件
//     *
//     * 比如：对于left_join_except_inner，则是排除右表的key
//     * @param neo 库对象
//     * @param leftTableName 左表的表名
//     * @param rightTableName 右表的表名
//     * @param joinType join的类型
//     * @return rightTableName.key is null 或者 leftTableName.key is null 或者 (leftTableName.key is null or rightTableName.key is null)
//     */
//    public String buildConditionWithWhere(Neo neo, String leftTableName, String rightTableName, JoinType joinType) {
//        if (joinType.equals(LEFT_JOIN_EXCEPT_INNER)) {
//            NeoTable table = neo.getTable(rightTableName);
//            if (null != table) {
//                return rightTableName + "." + table.getPrimary() + " is null";
//            }
//        } else if (joinType.equals(RIGHT_JOIN_EXCEPT_INNER)) {
//            NeoTable table = neo.getTable(leftTableName);
//            if (null != table) {
//                return leftTableName + "." + table.getPrimary() + " is null";
//            }
//        } else if (joinType.equals(OUTER_JOIN_EXCEPT_INNER)) {
//            NeoTable leftTable = neo.getTable(leftTableName);
//            NeoTable rightTable = neo.getTable(rightTableName);
//            StringBuilder result = new StringBuilder();
//            if (null != leftTable) {
//                result.append(leftTableName).append(".").append(leftTable.getPrimary()).append(" is null");
//            }
//            if (null != rightTable) {
//                result.append(leftTableName).append(".").append(rightTable.getPrimary()).append(" is null");
//            }
//            return result.toString();
//        }
//        return "";
//    }

    /**
     * 不算order by来判断搜索条件是否为空
     *
     * @param searchMap 搜索条件
     * @return true：空，false：不空
     */
    private Boolean isEmptyExceptOrderBy(NeoMap searchMap){
        String orderByStr = "order by";
        return null == searchMap.stream().filter(r -> !r.getKey().trim().equals(orderByStr)).findAny().orElse(null);
    }

    public List<Object> buildValueList(TableMap searchMap) {
        if (TableMap.isEmpty(searchMap)) {
            return Collections.emptyList();
        }

        return searchMap.clone().entrySet().stream().flatMap(e-> SqlBuilder.buildValueList((NeoMap) e.getValue()).stream()).collect(Collectors.toList());
    }

    /**
     * 构造order by 语句
     * <p>
     *     注意：该函数只处理order by属性其他的不处理
     *
     * @param searchMap 多表的包含order by的语句
     * @return 比如：order by neo_table1.`group` asc, neo_table2.`name` desc
     */
    public String buildOrderBy(TableMap searchMap) {
        if (!TableMap.isEmpty(searchMap)) {
            List<String> orderByStrList = searchMap.clone().entrySet().stream().flatMap(e-> {
                String tableName = e.getKey();
                NeoMap valueMap = (NeoMap) e.getValue();
                if (valueMap.containsKey(ORDER_BY)) {
                    return getOrderByStrList(tableName, valueMap.getString(ORDER_BY)).stream();
                }
                return null;
            }).filter(Objects::nonNull).collect(Collectors.toList());
            return " " + ORDER_BY + " " + String.join(", ", orderByStrList);
        }
        return "";
    }

    /**
     * 获取order by 后面的字符转换集合
     * <p>
     *     {@code group desc --> `group` desc} {@code group desc, name asc --> `group` desc, `name` asc}
     *
     * @param orderByValueStr order by后面的字符
     * @return 转换后的字符，比如：[neo_table1.`name` desc]
     */
    public List<String> getOrderByStrList(String tableName, String orderByValueStr) {
        if (null == orderByValueStr) {
            return Collections.emptyList();
        }
        List<String> values = Arrays.asList(orderByValueStr.split(","));
        List<String> valueList = new ArrayList<>();
        values.forEach(v -> {
            v = v.trim();
            if (v.contains(DESC) || v.contains(ASC)) {
                Integer index = v.indexOf(" ");
                valueList.add(tableName + "." + SqlBuilder.toDbField(v.substring(0, index)) + " " + v.substring(index + 1));
            } else {
                valueList.add(tableName + "." + SqlBuilder.toDbField(v));
            }
        });
        return valueList;
    }
}
