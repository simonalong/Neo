package com.simon.neo.db;

import static com.simon.neo.sql.SqlBuilder.*;
import com.simon.neo.Columns;
import com.simon.neo.Neo;
import com.simon.neo.NeoMap;
import com.simon.neo.sql.JoinType;
import java.util.List;

/**
 * @author zhouzhenyong
 * @since 2019/4/20 下午9:46
 */
public class NeoJoiner {

    private Neo neo;
    /**
     * 左表表名
     */
    private String leftTableName;
    /**
     * 右表表名
     */
    private String rightTableName;
    /**
     * join的类型
     */
    private JoinType joinType;
    /**
     * join对应的sql，from a xxJoin b on a.xxx=b.xxx
     */
    private String joinSql;
    /**
     * 在一些join中，会有一些额外的条件
     */
    private String sqlCondition;


    public NeoJoiner(Neo neo, String tableLeft, String tableRight) {
        this.neo = neo;
        this.leftTableName = tableLeft;
        this.rightTableName = tableRight;
    }

    public NeoJoiner setJoin(JoinType joinType){
        this.joinType = joinType;
        this.sqlCondition = buildJoinCondition(neo, leftTableName, rightTableName, joinType);
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
        this.joinSql = buildJoin(leftTableName, rightTableName, leftColumnName, rightColumnName, joinType);
        return this;
    }

    /**
     * join核查中的查询一个数据
     *
     * {@code select xxx from a inner join b on a.xxxxxx=b.yyy where x=? and y=? and a.m=? and b.n=? order by xx }
     *
     * @param leftColumns 左表的列名
     * @param rightColumns 右表的列名
     * @param searchMap 搜索条件
     * @param tailSql sql的尾部语句
     * @return join执行后的结果
     */
    public NeoMap one(Columns leftColumns, Columns rightColumns, NeoMap searchMap, String tailSql){
        String joinHeadSql = buildJoinHead(leftTableName, leftColumns, rightTableName, rightColumns);
        String joinTailSql = buildJoinTail(sqlCondition, searchMap, tailSql);
        return neo.exeOne(joinHeadSql + " " + joinSql + " " + joinTailSql);
    }

    public NeoMap one(Columns leftColumns, Columns rightColumns, NeoMap searchMap){
        return one(leftColumns, rightColumns, searchMap, "");
    }

    public NeoMap one(Columns leftColumns, NeoMap searchMap, String tailSql){
        return one(leftColumns, Columns.of(), searchMap, tailSql);
    }

    public NeoMap one(Columns leftColumns, NeoMap searchMap){
        return one(leftColumns, Columns.of(), searchMap);
    }

    @SuppressWarnings("unchecked")
    public <T> T one(Columns leftColumns, Columns rightColumns, T entity, String tailSql){
        return one(leftColumns, rightColumns, NeoMap.of(entity), tailSql).as((Class<T>) entity.getClass());
    }

    @SuppressWarnings("unchecked")
    public <T> T one(Columns leftColumns, Columns rightColumns, T entity){
        return one(leftColumns, rightColumns, NeoMap.of(entity), "").as((Class<T>) entity.getClass());
    }

    @SuppressWarnings("unchecked")
    public <T> T one(Columns leftColumns, T entity, String tailSql){
        return one(leftColumns, Columns.of(), NeoMap.of(entity), tailSql).as((Class<T>) entity.getClass());
    }

    @SuppressWarnings("unchecked")
    public <T> T one(Columns leftColumns, T entity){
        return one(leftColumns, Columns.of(), NeoMap.of(entity)).as((Class<T>) entity.getClass());
    }

    public NeoMap one(Columns leftColumns, Columns rightColumns, String tailSql){
        return one(leftColumns, rightColumns, NeoMap.of(), tailSql);
    }

    public NeoMap one(Columns leftColumns, Columns rightColumns){
        return one(leftColumns, rightColumns, "");
    }

    public NeoMap one(Columns leftColumns, String tailSql){
        return one(leftColumns, Columns.of(), tailSql);
    }

    public NeoMap one(Columns leftColumns){
        return one(leftColumns, Columns.of());
    }

    /**
     * join核查中的查询一个数据
     *
     * @param leftColumns 左表的列名
     * @param rightColumns 右表的列名
     * @param searchMap 搜索条件
     * @param tailSql sql的尾部语句
     * @return join执行后的结果
     */
    public List<NeoMap> list(Columns leftColumns, Columns rightColumns, NeoMap searchMap, String tailSql){
        String joinHeadSql = buildJoinHead(leftTableName, leftColumns, rightTableName, rightColumns);
        String joinTailSql = buildJoinTail(sqlCondition, searchMap, tailSql);
        return neo.exeList(joinHeadSql + " " + joinSql + " " + joinTailSql);
    }

    public List<NeoMap> list(Columns leftColumns, Columns rightColumns, NeoMap searchMap){
        return list(leftColumns, rightColumns, searchMap, "");
    }

    public List<NeoMap> list(Columns leftColumns, NeoMap searchMap, String tailSql){
        return list(leftColumns, Columns.of(), searchMap, tailSql);
    }

    public List<NeoMap> list(Columns leftColumns, NeoMap searchMap){
        return list(leftColumns, Columns.of(), searchMap);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> list(Columns leftColumns, Columns rightColumns, T entity, String tailSql){
        return NeoMap.asArray(list(leftColumns, rightColumns, NeoMap.of(entity), tailSql), (Class<T>) entity.getClass());
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> list(Columns leftColumns, Columns rightColumns, T entity){
        return NeoMap.asArray(list(leftColumns, rightColumns, NeoMap.of(entity), ""), (Class<T>) entity.getClass());
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> list(Columns leftColumns, T entity, String tailSql){
        return NeoMap.asArray(list(leftColumns, Columns.of(), NeoMap.of(entity), tailSql), (Class<T>) entity.getClass());
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> list(Columns leftColumns, T entity){
        return NeoMap.asArray(list(leftColumns, Columns.of(), NeoMap.of(entity)), (Class<T>) entity.getClass());
    }

    public List<NeoMap> list(Columns leftColumns, Columns rightColumns, String tailSql){
        return list(leftColumns, rightColumns, NeoMap.of(), tailSql);
    }

    public List<NeoMap> list(Columns leftColumns, Columns rightColumns){
        return list(leftColumns, rightColumns, "");
    }

    public List<NeoMap> list(Columns leftColumns, String tailSql){
        return list(leftColumns, Columns.of(), tailSql);
    }

    public List<NeoMap> list(Columns leftColumns){
        return list(leftColumns, Columns.of());
    }

    /**
     * join核查中的查询一个数据
     *
     * @param searchMap 搜索条件
     * @param tailSql sql的尾部语句
     * @return join执行后的结果
     */
    @SuppressWarnings("unchecked")
    public <T> T value(String tableName, String columnName, Class<T> tClass, NeoMap searchMap, String tailSql){
        String joinHeadSql = buildJoinHead(tableName, columnName);
        String joinTailSql = buildJoinTail(sqlCondition, searchMap, tailSql);
        return neo.exeValue(tClass, joinHeadSql + " " + joinSql + " " + joinTailSql);
    }

    @SuppressWarnings("unchecked")
    public <T> T value(String tableName, String columnName, Class<T> tClass, NeoMap searchMap){
        return value(tableName, columnName, tClass, searchMap, "");
    }

    public String value(String tableName, String columnName, NeoMap searchMap, String tailSql){
        return value(tableName, columnName, String.class, searchMap, tailSql);
    }

    public String value(String tableName, String columnName, NeoMap searchMap){
        return value(tableName, columnName, searchMap, "");
    }

    @SuppressWarnings("unchecked")
    public <T> T value(String tableName, String columnName, Class<T> tClass, Object entity, String tailSql){
        return value(tableName, columnName, tClass, NeoMap.from(entity), tailSql);
    }

    @SuppressWarnings("unchecked")
    public <T> T value(String tableName, String columnName, Class<T> tClass, Object entity){
        return value(tableName, columnName, tClass, NeoMap.from(entity), "");
    }

    public String value(String tableName, String columnName, Object entity, String tailSql){
        return value(tableName, columnName, String.class, NeoMap.from(entity), tailSql);
    }

    public String value(String tableName, String columnName, Object entity){
        return value(tableName, columnName, NeoMap.from(entity), "");
    }

    @SuppressWarnings("unchecked")
    public <T> T value(String tableName, String columnName, Class<T> tClass, String tailSql){
        return value(tableName, columnName, tClass, NeoMap.of(), tailSql);
    }

    @SuppressWarnings("unchecked")
    public <T> T value(String tableName, String columnName, Class<T> tClass){
        return value(tableName, columnName, tClass, "");
    }

    public String value(String tableName, String columnName, String tailSql){
        return value(tableName, columnName, String.class, tailSql);
    }

    public String value(String tableName, String columnName){
        return value(tableName, columnName, "");
    }

    /**
     * join核查中的查询一个数据
     *
     * @param searchMap 搜索条件
     * @param tailSql sql的尾部语句
     * @return join执行后的结果
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> values(String tableName, String columnName, Class<T> tClass, NeoMap searchMap, String tailSql){
        String joinHeadSql = buildJoinHead(tableName, columnName);
        String joinTailSql = buildJoinTail(sqlCondition, searchMap, tailSql);
        return neo.exeValues(tClass, joinHeadSql + " " + joinSql + " " + joinTailSql);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> values(String tableName, String columnName, Class<T> tClass, NeoMap searchMap){
        return values(tableName, columnName, tClass, searchMap, "");
    }

    public List<String> values(String tableName, String columnName, NeoMap searchMap, String tailSql){
        return values(tableName, columnName, String.class, searchMap, tailSql);
    }

    public List<String> values(String tableName, String columnName, NeoMap searchMap){
        return values(tableName, columnName, searchMap, "");
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> values(String tableName, String columnName, Class<T> tClass, Object entity, String tailSql){
        return values(tableName, columnName, tClass, NeoMap.from(entity), tailSql);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> values(String tableName, String columnName, Class<T> tClass, Object entity){
        return values(tableName, columnName, tClass, NeoMap.from(entity), "");
    }

    public List<String> values(String tableName, String columnName, Object entity, String tailSql){
        return values(tableName, columnName, String.class, NeoMap.from(entity), tailSql);
    }

    public List<String> values(String tableName, String columnName, Object entity){
        return values(tableName, columnName, NeoMap.from(entity), "");
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> values(String tableName, String columnName, Class<T> tClass, String tailSql){
        return values(tableName, columnName, tClass, NeoMap.of(), tailSql);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> values(String tableName, String columnName, Class<T> tClass){
        return values(tableName, columnName, tClass, "");
    }

    public List<String> values(String tableName, String columnName, String tailSql){
        return values(tableName, columnName, String.class, tailSql);
    }

    public List<String> values(String tableName, String columnName){
        return values(tableName, columnName, "");
    }
}
