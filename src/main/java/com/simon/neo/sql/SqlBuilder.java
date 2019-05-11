package com.simon.neo.sql;

import static com.simon.neo.sql.JoinType.LEFT_JOIN_EXCEPT_INNER;
import static com.simon.neo.sql.JoinType.OUTER_JOIN_EXCEPT_INNER;
import static com.simon.neo.sql.JoinType.RIGHT_JOIN_EXCEPT_INNER;

import com.simon.neo.Columns;
import com.simon.neo.Neo;
import com.simon.neo.NeoMap;
import com.simon.neo.db.NeoTable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
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

    /**
     * 提取order by数据
     *
     * @param searchMap 搜索条件
     * @return 返回 order by `xxx` 或者 order by `xxx` desc
     */
    public String buildOrderBy(NeoMap searchMap) {
        String orderByStr = "order by";
        if (!NeoMap.isEmpty(searchMap)) {
            AtomicReference<String> orderByValue = new AtomicReference<>();
            searchMap.stream().filter(r -> r.getKey().trim().equals(orderByStr)).forEach(r -> {
                orderByValue.set(orderByStr + " " + orderByStr(String.valueOf(r.getValue())));
            });
            return " " + orderByValue.get() + " ";
        }
        return "";
    }

    /**
     * order by 后面的字符转换为数据库字样，比如 {@code group desc --> `group` desc} {@code group desc, name asc --> `group` desc, `name` asc}
     * @param orderByValueStr order by后面的字符
     * @return 转换后的字符
     */
    private String orderByStr(String orderByValueStr){
        if (null == orderByValueStr) {
            return "";
        }
        String descStr = "desc";
        String ascStr = "asc";
        List<String> values = Arrays.asList(orderByValueStr.split(","));
        List<String> valueList = new ArrayList<>();
        values.forEach(v -> {
            v = v.trim();
            if (v.contains(descStr) || v.contains(ascStr)) {
                Integer index = v.indexOf(" ");
                valueList.add(toDbField(v.substring(0, index)) + " " + v.substring(index + 1));
            } else {
                valueList.add(toDbField(v));
            }
        });
        return String.join(", ", valueList);
    }

    /**
     * 值为占位符的条件拼接
     * @param searchMap 搜索条件map
     * @return 比如：`group` = ? and `name` = ?
     */
    public String buildCondition(NeoMap searchMap){
        StringBuilder stringBuilder = new StringBuilder();
        if (!NeoMap.isEmpty(searchMap)) {
            return stringBuilder
                .append(String.join(" and ", buildConditionMeta(searchMap)))
                .toString();
        }
        return stringBuilder.toString();
    }

    /**
     * 返回拼接的sql
     * @param neo 库对象
     * @param tableName 表名
     * @param columns 列名
     * @param searchMap 查询条件
     * @param tailSql 尾部sql
     * @return 返回sql，比如：select * from xxx where a=? and b=? order by `xxx` desc limit 1
     */
    public String buildOne(Neo neo, String tableName, Columns columns, NeoMap searchMap, String tailSql) {
//        StringBuilder sqlAppender = new StringBuilder("select ");
//        if (!Columns.isEmpty(columns)) {
//            sqlAppender.append(columns.buildFields());
//        } else {
//            sqlAppender.append(Columns.from(neo, tableName).buildFields());
//        }
//        sqlAppender.append(" from ").append(tableName).append(buildWhere(searchMap)).append(buildOrderBy(searchMap));
//        sqlAppender.append(limitOne());

        return buildList(neo, tableName, columns, searchMap, tailSql) + limitOne();
    }

    /**
     * 返回拼接的sql
     * @param tableName 表名
     * @param columns 列信息
     * @param searchMap 搜索条件
     * @param tailSql 尾部sql
     * @return 返回拼接的sql，比如：select * from xxx where a=? and b=? order by `xxx` desc
     */
    public String buildList(Neo neo, String tableName, Columns columns, NeoMap searchMap, String tailSql){
        StringBuilder sqlAppender = new StringBuilder("select ");
        if (!Columns.isEmpty(columns)){
            sqlAppender.append(columns.buildFields());
        }else{
            sqlAppender.append(Columns.from(neo, tableName).buildFields());
        }
        sqlAppender.append(" from ").append(tableName).append(buildWhere(searchMap)).append(buildOrderBy(searchMap));
        return sqlAppender.toString();
    }

    /**
     * 返回拼接的包含分页的sql
     * @param tableName 表名
     * @param columns 列信息
     * @param searchMap 搜索条件
     * @param tailSql 尾部sql
     * @param startIndex 分页
     * @param pageSize 分页大小
     * @return 返回sql，比如：select * from xxx where a=? and b=? order by `xxx` desc limit pageIndex, getPageSize
     */
    public String buildPageList(Neo neo, String tableName, Columns columns, NeoMap searchMap, String tailSql, Integer startIndex, Integer pageSize){
//        StringBuilder sqlAppender = new StringBuilder("select ");
//        if (!Columns.isEmpty(columns)){
//            sqlAppender.append(columns.buildFields());
//        }else{
//            sqlAppender.append(Columns.from(neo, tableName).buildFields());
//        }
//        sqlAppender.append(" from ").append(tableName).append(SqlBuilder.buildWhere(searchMap)).append(" ");
//        if(null != tailSql){
//            sqlAppender.append(" ").append(tailSql);
//        }
//        sqlAppender.append(" limit ").append(startIndex).append(", ").append(pageSize);
        return buildList(neo, tableName, columns, searchMap, tailSql) + " limit " + startIndex + ", " + pageSize;
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
     * @param tailSql 尾部sql
     * @return 比如：select `xxx` from yyy where a=? and b=? order by `xx` limit 1
     */
    public String buildValue(String tableName, String field, NeoMap searchMap, String tailSql){
        return buildValues(tableName, field, searchMap, tailSql) + limitOne();
    }

    /**
     * 返回拼接的sql
     * @param tableName 表名
     * @param field 要查询的字段
     * @param searchMap 查询参数
     * @param tailSql 尾部sql
     * @return select `xxx` from yyy where a=? and b=? order by `xx`
     */
    public String buildValues(String tableName, String field, NeoMap searchMap, String tailSql){
        return "select " + toDbField(field) + " from " + tableName + SqlBuilder.buildWhere(searchMap) + buildOrderBy(searchMap);
    }

    public String buildValues(Set<String> fieldList){
        return String.join(", ", fieldList.stream().map(f->"?").collect(Collectors.toList()));
    }

    public String buildInsert(String tableName, NeoMap neoMap){
        neoMap = toDbField(neoMap);
        return "insert into " + tableName + " (" + String.join(", ", neoMap.keySet()) + ") values (" + buildValues(
            neoMap.keySet()) + ")";
    }

    public String buildDelete(String tableName, NeoMap neoMap){
        return "delete from " + tableName + SqlBuilder.buildWhere(neoMap);
    }

    public String buildUpdate(String tableName, NeoMap dataMap, NeoMap searchMap){
        return "update " + tableName + buildSetValues(dataMap.keySet()) + SqlBuilder.buildWhere(searchMap);
    }

    public String buildSetValues(Set<String> fieldList){
        return " set " + String.join(", ", fieldList.stream().map(f -> toDbField(f) + "=?").collect(Collectors.toList()));
    }

    /**
     * join的head 部分对应的sql，主要是选择的列
     *
     * @param neo 库对象
     * @param columns 多个表的列信息
     * @return join对应的head，比如：select xxx,xxx
     */
    public String buildJoinHead(Neo neo, Columns columns) {
        return "select " + columns.setNeo(neo).buildFields();
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
    public String buildJoin(String joinLeftTableName, String leftTableName, String leftColumnName,
        String rightTableName, String rightColumnName, JoinType joinType) {
        if(null != joinLeftTableName){
            return joinLeftTableName + " " + joinType.getSql() + " " + rightTableName + " on " + leftTableName + "."
                + toDbField(leftColumnName) + "=" + rightTableName + "." + toDbField(rightColumnName) + " ";
        }else{
            return " " + joinType.getSql() + " " + rightTableName + " on " + leftTableName + "."
                + toDbField(leftColumnName) + "=" + rightTableName + "." + toDbField(rightColumnName) + " ";
        }
    }

    /**
     * 创建在排除公共部分的join中对应的where条件
     *
     * 比如：对于left_join_except_inner，则是排除右表的key
     * @param neo 库对象
     * @param leftTableName 左表的表名
     * @param rightTableName 右表的表名
     * @param joinType join的类型
     * @return rightTableName.key is null 或者 leftTableName.key is null 或者 (leftTableName.key is null or rightTableName.key is null)
     */
    public String buildJoinCondition(Neo neo, String leftTableName, String rightTableName, JoinType joinType) {
        if (joinType.equals(LEFT_JOIN_EXCEPT_INNER)) {
            NeoTable table = neo.getTable(rightTableName);
            if (null != table) {
                return rightTableName + "." + table.getPrimary() + " is null";
            }
        } else if (joinType.equals(RIGHT_JOIN_EXCEPT_INNER)) {
            NeoTable table = neo.getTable(leftTableName);
            if (null != table) {
                return leftTableName + "." + table.getPrimary() + " is null";
            }
        } else if (joinType.equals(OUTER_JOIN_EXCEPT_INNER)) {
            NeoTable leftTable = neo.getTable(leftTableName);
            NeoTable rightTable = neo.getTable(rightTableName);
            StringBuilder result = new StringBuilder();
            if (null != leftTable) {
                result.append(leftTableName).append(".").append(leftTable.getPrimary()).append(" is null");
            }
            if (null != rightTable) {
                result.append(leftTableName).append(".").append(rightTable.getPrimary()).append(" is null");
            }
            return result.toString();
        }
        return "";
    }

    /**
     * 生成拼接的join的条件{@code where x=? and y=? and a.m=? and b.n=?}
     *
     * @param sqlCondition 对于join_except类型需要用的一些排除条件
     * @param searchMap 搜索条件map
     * @param tail 尾部sql
     * @return 返回拼接的sql
     */
    public String buildJoinTail(String sqlCondition, NeoMap searchMap, String tail) {
        StringBuilder sb = new StringBuilder();
        Boolean sqlConditionNotEmpty = null != sqlCondition && !"".equals(sqlCondition);
        Boolean searchMapsNotEmpty = !NeoMap.isEmpty(searchMap);
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
            sb.append(buildCondition(searchMap));
        }

        sb.append(" ").append(tail);
        return sb.toString();
    }

    public String limitOne(){
        return " limit 1";
    }

    private List<String> buildConditionMeta(NeoMap searchMap) {
        String orderByStr = "order by";
        return searchMap.stream().filter(r -> !r.getKey().trim().equals(orderByStr)).map(e->valueFix(searchMap, e)).collect(Collectors.toList());
    }

    /**
     * 对于NeoMap的key含有表名的进行转换，比如{@code table1.name} 到 {@code table1.`name`} 或者{@code name}到{@code `name`}
     *
     * @param neoMap 原map
     * @return 转换之后的map
     */
    public NeoMap toDbField(NeoMap neoMap) {
        NeoMap resultMap = NeoMap.of();
        String point = ".";
        neoMap.stream().forEach(e -> {
            String key = e.getKey();
            Object value = e.getValue();
            if (key.contains(point)) {
                Integer index = key.indexOf(".");
                resultMap.put(key.substring(0, index + 1) + toDbField(key.substring(index + 1)), value);
            } else {
                resultMap.put(toDbField(key), value);
            }
        });
        return resultMap;
    }

    /**
     * 生成值列表，对于特殊的值：含有like和比较符进行处理
     *
     * @param searchMap 搜索的值map
     * @return 值结果
     */
    public List<Object> buildValueList(NeoMap searchMap) {
        if (NeoMap.isEmpty(searchMap)) {
            return Collections.emptyList();
        }

        String orderByStr = "order by";
        return searchMap.stream().filter(r -> !r.getKey().trim().equals(orderByStr)).map(o->{
            Object v = o.getValue();
            if (v instanceof String) {
                String valueStr = String.class.cast(v);

                // 处理模糊搜索，like
                if (valueStr.startsWith(LIKE_PRE)) {
                    return null;
                }

                // 大小比较设置，针对 ">", "<", ">=", "<=" 这么几个进行比较
                if (haveThanPre(valueStr)) {
                    return getSymbolAndValue(valueStr).getValue();
                }
            }
            return v;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private String valueFix(NeoMap searchMap, Entry<String, Object> entry){
        Object value = entry.getValue();
        String key = toDbField(entry.getKey());
        if (value instanceof String) {
            String valueStr = String.class.cast(value);

            // 处理模糊搜索，like
            if (valueStr.startsWith(LIKE_PRE)) {
                return key + " like " + (withValueFlag.get() ? "'" + valueStr + "'" : "'" + getLikeValue(valueStr) + "'");
            }

            // 大小比较设置，针对 ">", "<", ">=", "<=" 这么几个进行比较
            if (haveThanPre(valueStr)) {
                Pair<String, String> symbolAndValue = getSymbolAndValue(valueStr);
                searchMap.put(entry.getKey(), symbolAndValue.getValue().trim());
                return key + " " + symbolAndValue.getKey() + ((withValueFlag.get() ? "'" + valueStr + "'" : " ?"));
            }
            return key + " = " + ((withValueFlag.get() ? "'" + valueStr + "'" : " ?"));
        }
        return key + " = " + ((withValueFlag.get() ? value : " ?"));
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

    /**
     * 获取值的比较符和数据值
     *
     * @param valueStr 带有比较符的参数
     * @return key为比较符，value为字符数据
     */
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

    /**
     * 将普通的列名转换为sql语句中的列，其实就是在列的前后添加`
     * @param column 原列名
     * @return 转换后的列名，比如name 到 `name`
     */
    private String toDbField(String column){
        if (column.contains("`")) {
            return column;
        }
        return "`" + column + "`";
    }
}
