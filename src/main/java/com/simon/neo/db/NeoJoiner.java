package com.simon.neo.db;

import static com.simon.neo.sql.SqlBuilder.*;
import static com.simon.neo.db.AliasParser.*;
import com.simon.neo.Columns;
import com.simon.neo.Neo;
import com.simon.neo.NeoMap;
import com.simon.neo.sql.JoinType;
import java.util.List;

/**
 * @author zhouzhenyong
 * @since 2019/4/20 下午9:46
 */
public final class NeoJoiner {

    private Neo neo;
    /**
     * 左表表名，原始表名
     */
    private String leftTableNameOrigin;
    /**
     * 左表表名，如果有别名，则为别名
     */
    private String leftTableName;
    /**
     * join时候左表表名，原始表名
     */
    private String joinLeftTableNameOrigin;
    /**
     * join时候左表表名，如果有别名，则为别名
     */
    private String joinLeftTableName;
    /**
     * 右表表名，原始表名
     */
    private String rightTableNameOrigin;
    /**
     * 右表表名，如果有别名，则为别名
     */
    private String rightTableName;
    /**
     * join的类型
     */
    private JoinType joinType;
    /**
     * join对应的sql，from a xxJoin b on a.xxx=b.xxx
     */
    private String joinSql = " from ";
    /**
     * 在一些join中，会有一些额外的条件， 比如leftOuterJoin，这种就是leftJoin和other表的key为空情况，sqlCondition为：tableb.key is null
     */
    private String sqlCondition;

    public NeoJoiner(Neo neo, String leftTableName, String rightTableName) {
        this.neo = neo;
        this.leftTableName = getAlias(leftTableName);
        this.joinLeftTableName = getAlias(leftTableName);
        this.rightTableName = getAlias(rightTableName);

        // 原始表名设置
        this.leftTableNameOrigin = leftTableName;
        this.joinLeftTableNameOrigin = leftTableName;
        this.rightTableNameOrigin = rightTableName;
    }

    public NeoJoiner setJoin(JoinType joinType){
        this.joinType = joinType;
        this.sqlCondition = buildJoinCondition(neo, leftTableName, rightTableName, joinType);
        return this;
    }

    /**
     * 默认的join采用的是innerJoin
     *
     * @param leftTableName 左表表名
     * @param rightTableName 右表表名
     * @return 做关联的关联器
     */
    public NeoJoiner join(String leftTableName, String rightTableName){
        this.joinLeftTableName = null;
        this.leftTableName = getAlias(leftTableName);
        this.rightTableName = getAlias(rightTableName);
        this.joinType = JoinType.INNER_JOIN;

        // 原始表名设置
        this.leftTableNameOrigin = leftTableName;
        this.rightTableNameOrigin = rightTableName;
        return this;
    }

    /**
     * 左关联，只保留左表的信息
     *
     * @param leftTableName 左表表名
     * @param rightTableName 右表表名
     * @return 做关联的关联器
     */
    public NeoJoiner leftJoin(String leftTableName, String rightTableName){
        this.joinLeftTableName = null;
        this.leftTableName = getAlias(leftTableName);
        this.rightTableName = getAlias(rightTableName);
        this.joinType = JoinType.LEFT_JOIN;

        // 原始表名设置
        this.leftTableNameOrigin = leftTableName;
        this.rightTableNameOrigin = rightTableName;
        return this;
    }

    /**
     * 左关联，只保留左表的信息
     *
     * @param leftTableName 左表表名
     * @param rightTableName 右表表名
     * @return 做关联的关联器
     */
    public NeoJoiner rightJoin(String leftTableName, String rightTableName){
        this.joinLeftTableName = null;
        this.leftTableName = getAlias(leftTableName);
        this.rightTableName = getAlias(rightTableName);
        this.joinType = JoinType.RIGHT_JOIN;

        // 原始表名设置
        this.leftTableNameOrigin = leftTableName;
        this.rightTableNameOrigin = rightTableName;
        return this;
    }

    /**
     * 左关联，只保留左表的信息
     *
     * @param leftTableName 左表表名
     * @param rightTableName 右表表名
     * @return 做关联的关联器
     */
    public NeoJoiner innerJoin(String leftTableName, String rightTableName){
        this.joinLeftTableName = null;
        this.leftTableName = getAlias(leftTableName);
        this.rightTableName = getAlias(rightTableName);
        this.joinType = JoinType.INNER_JOIN;

        // 原始表名设置
        this.leftTableNameOrigin = leftTableName;
        this.rightTableNameOrigin = rightTableName;
        return this;
    }

    /**
     * 左关联，只保留左表的信息
     *
     * @param leftTableName 左表表名
     * @param rightTableName 右表表名
     * @return 做关联的关联器
     */
    public NeoJoiner outerJoin(String leftTableName, String rightTableName){
        this.joinLeftTableName = null;
        this.leftTableName = getAlias(leftTableName);
        this.rightTableName = getAlias(rightTableName);
        this.joinType = JoinType.OUTER_JOIN;

        // 原始表名设置
        this.leftTableNameOrigin = leftTableName;
        this.rightTableNameOrigin = rightTableName;
        return this;
    }

    /**
     * 左关联，只保留左表的信息
     *
     * @param leftTableName 左表表名
     * @param rightTableName 右表表名
     * @return 做关联的关联器
     */
    public NeoJoiner leftJoinExceptInner(String leftTableName, String rightTableName){
        this.joinLeftTableName = null;
        this.leftTableName = getAlias(leftTableName);
        this.rightTableName = getAlias(rightTableName);
        this.joinType = JoinType.LEFT_JOIN_EXCEPT_INNER;

        // 原始表名设置
        this.leftTableNameOrigin = leftTableName;
        this.rightTableNameOrigin = rightTableName;
        return this;
    }

    /**
     * 左关联，只保留左表的信息
     *
     * @param leftTableName 左表表名
     * @param rightTableName 右表表名
     * @return 做关联的关联器
     */
    public NeoJoiner rightJoinExceptInner(String leftTableName, String rightTableName){
        this.joinLeftTableName = null;
        this.leftTableName = getAlias(leftTableName);
        this.rightTableName = getAlias(rightTableName);
        this.joinType = JoinType.RIGHT_JOIN_EXCEPT_INNER;

        // 原始表名设置
        this.leftTableNameOrigin = leftTableName;
        this.rightTableNameOrigin = rightTableName;
        return this;
    }

    /**
     * 左关联，只保留左表的信息
     *
     * @param leftTableName 左表表名
     * @param rightTableName 右表表名
     * @return 做关联的关联器
     */
    public NeoJoiner outerJoinExceptInner(String leftTableName, String rightTableName){
        this.joinLeftTableName = null;
        this.leftTableName = getAlias(leftTableName);
        this.rightTableName = getAlias(rightTableName);
        this.joinType = JoinType.OUTER_JOIN_EXCEPT_INNER;

        // 原始表名设置
        this.leftTableNameOrigin = leftTableName;
        this.rightTableNameOrigin = rightTableName;
        return this;
    }

    /**
     * join 的关联字段
     *
     * @param leftColumnName 左表的列名
     * @param rightColumnName 右表的列名
     * @return 返回关联之后的Joiner
     */
    public NeoJoiner on(String leftColumnName, String rightColumnName){
        this.joinSql += buildJoin(leftColumnName, rightColumnName);
        return this;
    }

    private String buildJoin(String leftColumnName, String rightColumnName) {
        if(null != joinLeftTableNameOrigin){
            return joinLeftTableNameOrigin + " " + joinType.getSql() + " " + rightTableNameOrigin
                + " on " + leftTableName + "." + toDbField(leftColumnName) + "=" + rightTableName + "." + toDbField(rightColumnName);
        }else{
            return " " + joinType.getSql() + " " + rightTableNameOrigin
                + " on " + leftTableName + "." + toDbField(leftColumnName) + "=" + rightTableName + "." + toDbField(rightColumnName);
        }
    }

    /**
     * join核查中的查询一个数据
     *
     * {@code select xxx from a inner join b on a.xxxxxx=b.yyy where x=? and y=? and a.m=? and b.n=? order by xx }
     *
     * @param columns 所有表的列名
     * @param searchMap 搜索条件
     * @return join执行后的结果
     */
    public NeoMap one(Columns columns, NeoMap searchMap){
        return neo.exeOne(generateSql(columns, searchMap), buildValueList(searchMap).toArray());
    }

    public NeoMap one(Columns columns){
        return one(columns, NeoMap.of());
    }

    /**
     * join核查中的查询一个数据
     *
     * @param columns 左表的列名
     * @param searchMap 搜索条件
     * @return join执行后的结果
     */
    public List<NeoMap> list(Columns columns, NeoMap searchMap){
        return neo.exeList(generateSql(columns, searchMap), buildValueList(searchMap).toArray());
    }

    public List<NeoMap> list(Columns columns){
        return list(columns, NeoMap.of());
    }

    public List<NeoMap> page(Columns columns, NeoMap searchMap, NeoPage neoPage) {
        Integer startIndex = neoPage.getStartIndex();
        Integer pageSize = neoPage.getPageSize();
        String sql = generatePageSql(columns, searchMap,  startIndex, pageSize);
        return neo.exePage(sql, startIndex, pageSize, buildValueList(searchMap).toArray());
    }

    public List<NeoMap> page(Columns columns, NeoPage page){
        return page(columns, NeoMap.of(), page);
    }

    /**
     * join核查中的查询一个数据
     *
     * @param columns 左表的列名
     * @param searchMap sql的尾部语句
     * @return join执行后的结果
     */
    public Integer count(Columns columns, NeoMap searchMap){
        return neo.exeCount(generateSql(columns, searchMap), buildValueList(searchMap).toArray());
    }

    /**
     * join核查中的查询一个数据
     * @param tClass 目标类型
     * @param columnName 列名
     * @param searchMap 搜索条件
     * @param <T> 返回值类型
     * @return 返回的指定类型的值
     */
    public <T> T value(Class<T> tClass, String tableName, String columnName, NeoMap searchMap){
        String joinTailSql = buildJoinTail(sqlCondition, searchMap);
        String originSql = generateValueSql(tableName, columnName, joinTailSql);
        return neo.exeValue(tClass, originSql, buildValueList(searchMap).toArray());
    }

    public <T> T value(Class<T> tClass, String tableName, String columnName){
        return value(tClass, tableName, columnName, NeoMap.of());
    }

    public String value(String tableName, String columnName, NeoMap searchMap){
        return value(String.class, tableName, columnName, searchMap);
    }

    public String value(String tableName, String columnName){
        return value(tableName, columnName, NeoMap.of());
    }

    /**
     * join核查中的查询一个数据
     * @param tClass 目标类型
     * @param columnName 列名
     * @param searchMap 搜索条件
     * @param <T> 返回值类型
     * @return 返回的指定类型的值
     */
    public <T> List<T> values(Class<T> tClass, String tableName, String columnName, NeoMap searchMap){
        String joinTailSql = buildJoinTail(sqlCondition, searchMap);
        String originSql = generateValueSql(tableName, columnName, joinTailSql);
        return neo.exeValues(tClass, originSql, buildValueList(searchMap).toArray());
    }

    public <T> List<T> values(Class<T> tClass, String tableName, String columnName){
        return values(tClass, tableName, columnName, NeoMap.of());
    }

    public List<String> values(String tableName, String columnName, NeoMap searchMap){
        return values(String.class, tableName, columnName, searchMap);
    }

    public List<String> values(String tableName, String columnName){
        return values(tableName, columnName, NeoMap.of());
    }

    private String generateSql(Columns columns, NeoMap searchMap){
        String joinHeadSql = buildJoinHead(neo, columns);
        String joinTailSql = buildJoinTail(sqlCondition, searchMap);
        if(null != joinSql && !"".equals(joinSql)){
            return joinHeadSql + " " + joinSql + " " + joinTailSql;
        }else {
            return joinHeadSql + " " + joinTailSql;
        }
    }

    /**
     * 构造join的执行的sql和对应的参数
     *
     * @param columns 要展示的列
     * @param searchMap 搜索条件
     * @return Pair对象：key为拼接的含有占位符的原始sql， value为对应的填充参数
     */
    private String generatePageSql(Columns columns, NeoMap searchMap, Integer startIndex, Integer pageSize){
        String sqlOrigin = generateSql(columns, searchMap);
        if (!sqlOrigin.contains("limit")){
            sqlOrigin += " limit " + startIndex + ", " + pageSize;
        }
        return sqlOrigin;
    }

    private String generateValueSql(String tableName, String columnName, String joinTailSql){
        return "select " + tableName + "." + toDbField(columnName) + " " + joinSql + " " + joinTailSql;
    }
}
