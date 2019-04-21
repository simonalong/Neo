package com.simon.neo.sql;

import static com.simon.neo.sql.JoinType.LEFT_JOIN_EXCEPT_INNER;
import static com.simon.neo.sql.JoinType.OUTER_JOIN_EXCEPT_INNER;
import static com.simon.neo.sql.JoinType.RIGHT_JOIN_EXCEPT_INNER;

import com.simon.neo.Columns;
import com.simon.neo.Neo;
import com.simon.neo.NeoMap;
import com.simon.neo.db.NeoTable;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.util.Pair;
import lombok.experimental.UtilityClass;

/**
 * @author zhouzhenyong
 * @since 2019/3/29 下午11:13
 */
@UtilityClass
public class SqlBuilder {

    /**
     * 字段搜索的like前缀
     */
    private static final String LIKE_PRE = "like ";
    /**
     * 比较操作符的前缀
     */
    private static final List<String> THAN_PRE = Arrays.asList(">", "<", ">=", "<=");

    private static ThreadLocal<Boolean> withValueFlag = ThreadLocal.withInitial(() -> false);

    public <T> String in(List<T> values) {
        if (null == values || values.isEmpty()) {
            return "";
        }
        List<String> strList = values.stream().map(c -> (null == c) ? null : String.valueOf(c)).collect(Collectors.toList());
        return "('" + String.join("','", strList) + "')";
    }

    public String buildWhere(NeoMap searchMap) {
        if(!NeoMap.isEmpty(searchMap)) {
            return " where " + buildCondition(searchMap);
        }
        return "";
    }

    public String buildWhereWithValue(NeoMap searchMap) {
        withValueFlag.set(true);
        return buildWhere(searchMap);
    }

    /**
     * 值为占位符的条件拼接
     * @param searchMap 搜索条件map
     * @return 比如：`group` = ? and `name` = ?
     */
    public String buildCondition(NeoMap searchMap){
        StringBuilder stringBuilder = new StringBuilder();
        if (!NeoMap.isEmpty(searchMap)) {
            stringBuilder.append("`");
            return stringBuilder
                .append(String.join(" and `", buildConditionMeta(searchMap)))
                .toString();
        }
        return stringBuilder.toString();
    }

    /**
     * 带值的搜索拼接
     * @param searchMap 搜索条件map
     * @return 比如：`group` = 'group1' and `name` = 'name1'
     */
    public String buildConditionWithValue(NeoMap searchMap){
        withValueFlag.set(true);
        return buildCondition(searchMap);
    }

    /**
     * 返回拼接的sql
     * @param tableName 表名
     * @param searchMap 查询参数
     * @return 返回sql，比如：select * from xxx where a=? and b=? order by `xxx` desc limit 1
     */
    public String buildOne(String tableName, Columns columns, NeoMap searchMap, String tailSql) {
        StringBuilder sqlAppender = new StringBuilder("select ");
        if (!Columns.isEmpty(columns)) {
            sqlAppender.append(columns.buildFields());
        } else {
            sqlAppender.append("*");
        }
        sqlAppender.append(" from ").append(tableName).append(buildWhere(searchMap)).append(" ");
        if (null != tailSql) {
            sqlAppender.append(" ").append(tailSql);
        }
        sqlAppender.append(limitOne());
        return sqlAppender.toString();
    }

    /**
     * 返回拼接的sql
     * @param tableName 表名
     * @param searchMap 查询参数
     * @return 返回sql，比如：select * from xxx where a=? and b=? order by `xxx` desc
     */
    public String buildList(String tableName, Columns columns, NeoMap searchMap, String tailSql){
        StringBuilder sqlAppender = new StringBuilder("select ");
        if (!Columns.isEmpty(columns)){
            sqlAppender.append(columns.buildFields());
        }else{
            sqlAppender.append("*");
        }
        sqlAppender.append(" from ").append(tableName).append(buildWhere(searchMap)).append(" ");
        if(null != tailSql){
            sqlAppender.append(" ").append(tailSql);
        }
        return sqlAppender.toString();
    }

    /**
     * 返回拼接的sql
     * @param tableName 表名
     * @param searchMap 查询参数
     * @return 返回sql，比如：select * from xxx where a=? and b=? order by `xxx` desc limit pageIndex, pageSize
     */
    public String buildPageList(String tableName, Columns columns, NeoMap searchMap, String tailSql, Integer pageIndex, Integer pageSize){
        StringBuilder sqlAppender = new StringBuilder("select ");
        if (!Columns.isEmpty(columns)){
            sqlAppender.append(columns.buildFields());
        }else{
            sqlAppender.append("*");
        }
        sqlAppender.append(" from ").append(tableName).append(SqlBuilder.buildWhere(searchMap)).append(" ");
        if(null != tailSql){
            sqlAppender.append(" ").append(tailSql);
        }
        sqlAppender.append(" limit ").append(pageIndex).append(", ").append(pageSize);
        return sqlAppender.toString();
    }

    /**
     * 返回拼接的sql
     * @param tableName 表名
     * @param searchMap 查询参数
     * @return select `xxx` from yyy where a=? and b=? limit 1
     */
    public String buildCount(String tableName, NeoMap searchMap){
        return "select count(1) from " + tableName + SqlBuilder.buildWhere(searchMap) + limitOne();
    }

    /**
     * 返回拼接的sql
     * @param tableName 表名
     * @param field 要查询的字段
     * @param searchMap 查询参数
     * @return select `xxx` from yyy where a=? and b=? order by `xx` limit 1
     */
    public String buildValue(String tableName, String field, NeoMap searchMap, String tailSql){
        StringBuilder sqlAppender = new StringBuilder("select `").append(field).append("` from ").append(tableName)
            .append(SqlBuilder.buildWhere(searchMap));
        if(null != tailSql){
            sqlAppender.append(" ").append(tailSql);
        }
        sqlAppender.append(limitOne());
        return sqlAppender.toString();
    }

    /**
     * 返回拼接的sql
     * @param tableName 表名
     * @param field 要查询的字段
     * @param searchMap 查询参数
     * @return select `xxx` from yyy where a=? and b=? order by `xx`
     */
    public String buildValues(String tableName, String field, NeoMap searchMap, String tailSql){
        StringBuilder sqlAppender = new StringBuilder("select `").append(field).append("` from ").append(tableName)
            .append(SqlBuilder.buildWhere(searchMap));
        if(null != tailSql){
            sqlAppender.append(" ").append(tailSql);
        }
        return sqlAppender.toString();
    }

    public String buildValues(Set<String> fieldList){
        return String.join(", ", fieldList.stream().map(f->"?").collect(Collectors.toList()));
    }

    public String buildInsert(String tableName, NeoMap neoMap){
        return "insert into "
            + tableName
            + "(`"
            + String.join("`, `", neoMap.keySet())
            + "`) values ("
            + buildValues(neoMap.keySet())
            + ")";
    }

    public String buildDelete(String tableName, NeoMap neoMap){
        return "delete from " + tableName + SqlBuilder.buildWhere(neoMap);
    }

    public String buildUpdate(String tableName, NeoMap dataMap, NeoMap searchMap){
        return "update " + tableName + buildSetValues(dataMap.keySet()) + SqlBuilder.buildWhere(searchMap);
    }

    public String buildSetValues(Set<String> fieldList){
        return " set `" + String.join(", `", fieldList.stream().map(f -> f + "`=?").collect(Collectors.toList()));
    }

    /**
     * join的head 部分对应的sql，主要是选择的列
     *
     * @param leftTableName 左表表名
     * @param leftColumns 左表选择的列
     * @param rightTableName 右表表名
     * @param rightColumns 右表的列
     * @return join对应的head，比如：select xxx,xxx
     */
    public String buildJoinHead(String leftTableName, Columns leftColumns, String rightTableName, Columns rightColumns){
        StringBuilder sb = new StringBuilder("select ");
        if(!Columns.isEmpty(leftColumns)){
            sb.append(leftColumns.buildFields(leftTableName));
        }

        if(!Columns.isEmpty(leftColumns) && !Columns.isEmpty(rightColumns)){
            sb.append(", ");
        }

        if(!Columns.isEmpty(rightColumns)){
            sb.append(rightColumns.buildFields(rightTableName));
        }
        return sb.toString();
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
     * join 的join 部分对应的sql
     *
     * @param leftTableName 左表名
     * @param leftColumnName 左表的on列名
     * @param rightTableName 右表名
     * @param rightColumnName 右表的on列名
     * @param joinType join的类型
     * @return join对应的部分sql，比如：from leftTableName xxJoin rightTableName on leftTableName.`leftColumnName` = rightTableName.`rightColumnName`
     */
    public String buildJoin(String leftTableName, String leftColumnName, String rightTableName, String rightColumnName, JoinType joinType){
        return "from " + leftTableName + " " + joinType.getSql() + " " + rightTableName + " on " + leftTableName + ".`"
            + leftColumnName + "`=" + rightTableName + ".`" + rightColumnName + "`";
    }

    /**
     * 创建在排除公共部分的join中对应的where条件
     *
     * 比如：对于left_join_except_inner，则是排除右表的key
     * @param leftTableName 左表的表名
     * @param rightTableName 右表的表名
     * @param joinType join的类型
     * @return rightTableName.key is null 或者 leftTableName.key is null 或者 (leftTableName.key is null or rightTableName.key is null)
     */
    public String buildJoinCondition(Neo neo, String leftTableName, String rightTableName, JoinType joinType){
        if (joinType.equals(LEFT_JOIN_EXCEPT_INNER)){
            NeoTable table = neo.getTable(rightTableName);
            if(null != table){
                return rightTableName + "." + table.getPrimary() + " is null";
            }
        } else if(joinType.equals(RIGHT_JOIN_EXCEPT_INNER)){
            NeoTable table = neo.getTable(leftTableName);
            if(null != table){
                return leftTableName + "." + table.getPrimary() + " is null";
            }
        } else if(joinType.equals(OUTER_JOIN_EXCEPT_INNER)){
            NeoTable leftTable = neo.getTable(leftTableName);
            NeoTable rightTable = neo.getTable(rightTableName);
            StringBuilder result = new StringBuilder();
            if(null != leftTable){
                result.append(leftTableName).append(".").append(leftTable.getPrimary()).append(" is null");
            }
            if(null != rightTable){
                result.append(leftTableName).append(".").append(rightTable.getPrimary()).append(" is null");
            }
            return result.toString();
        }
        return "";
    }

    /**
     * 对于join的对应的sql的后缀部分
     *
     * @param sqlCondition sql的条件
     * @param searchMap 搜索条件
     * @param tail sql的尾部信息，比如order by xxx
     * @return where xxx and xxx
     */
    public String buildJoinTail(String sqlCondition, NeoMap searchMap, String tail) {
        StringBuilder sb = new StringBuilder();
        Boolean sqlConditionNotEmpty = null != sqlCondition && !"".equals(sqlCondition);
        Boolean searchMapEmpty = NeoMap.isEmpty(searchMap);
        if (sqlConditionNotEmpty || !searchMapEmpty) {
            sb.append(" where ");
        }

        if (sqlConditionNotEmpty) {
            sb.append("(").append(sqlCondition).append(")");
        }

        if (!searchMapEmpty) {
            sb.append(" and ").append(buildConditionWithValue(searchMap));
        }

        sb.append(" ").append(tail);
        return sb.toString();
    }

    public String limitOne(){
        return " limit 1";
    }

    private List<String> buildConditionMeta(NeoMap searchMap) {
        return searchMap.entrySet().stream().map(e->valueFix(searchMap, e)).collect(Collectors.toList());
    }

    private String valueFix(NeoMap searchMap, Entry<String, Object> entry){
        Object value = entry.getValue();
        if (value instanceof String) {
            String valueStr = String.class.cast(value);
            // 设置模糊搜索
            if (valueStr.startsWith(LIKE_PRE)) {
                searchMap.put(entry.getKey(), getLikeValue(valueStr));
                return entry.getKey() + "` like " + (withValueFlag.get() ? "'" + valueStr + "'" : "?");
            }

            // 大小比较设置，针对 ">", "<", ">=", "<=" 这么几个进行比较
            if (haveThanPre(valueStr)) {
                Pair<String, String> symbolAndValue = getSymbolAndValue(valueStr);
                searchMap.put(entry.getKey(), symbolAndValue.getValue());
                return entry.getKey() + "` " + symbolAndValue.getKey() + ((withValueFlag.get() ? "'" + valueStr + "'" : " ?"));
            }
            return entry.getKey() + "` = " + ((withValueFlag.get() ? "'" + valueStr + "'" : " ?"));
        }
        return entry.getKey() + "` = " + ((withValueFlag.get() ? value : " ?"));
    }

    /**
     * 搜索的数据是否有比较类型的前缀
     */
    private boolean haveThanPre(String value){
        if (null == value || "".equals(value)) {
            return false;
        }

        for(String pre : THAN_PRE){
            if(value.startsWith(pre)){
                return true;
            }
        }
        return false;
    }

    /**
     * 将传入的包含有like前缀的字符串，提取出value，然后拼接，比如：like xxx -> 'xxx%'
     */
    private String getLikeValue(String likeValue) {
        return likeValue.substring(likeValue.indexOf(LIKE_PRE) + LIKE_PRE.length()) + "%";
    }

    @SuppressWarnings("all")
    private Pair<String, String> getSymbolAndValue(String valueStr) {
        valueStr = valueStr.trim();
        if (valueStr.startsWith(">")) {
            if (valueStr.startsWith(">=")) {
                return new Pair<>(">=", valueStr.substring(valueStr.indexOf(">=") + ">=".length()));
            } else {
                return new Pair<>(">", valueStr.substring(valueStr.indexOf(">") + ">".length()));
            }
        } else if (valueStr.startsWith("<")) {
            if (valueStr.startsWith("<=")) {
                return new Pair<>("<=", valueStr.substring(valueStr.indexOf("<=") + "<=".length()));
            } else {
                return new Pair<>("<", valueStr.substring(valueStr.indexOf("<") + "<".length()));
            }
        }
        return new Pair<>("=", valueStr);
    }
}
