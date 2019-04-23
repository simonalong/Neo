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
     * 在一些join中，会有一些额外的条件， 比如leftOuterJoin，这种就是leftJoin和other表的key为空情况，sqlCondition为：tableb.key is null
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
     * 默认的join采用的是innerJoin
     *
     * @param leftTableName 左表表名
     * @param rightTableName 右表表名
     * @return 做关联的关联器
     */
    public NeoJoiner join(String leftTableName, String rightTableName){
        // todo
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
        // todo
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
        // todo
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
        // todo
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
        // todo
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
        // todo
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
        // todo
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
        // todo
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
        this.joinSql = buildJoin(leftTableName, leftColumnName, rightTableName, rightColumnName, joinType);
        return this;
    }

//    /**
//     * join核查中的查询一个数据
//     *
//     * {@code select xxx from a inner join b on a.xxxxxx=b.yyy where x=? and y=? and a.m=? and b.n=? order by xx }
//     *
//     * @param leftColumns 左表的列名
//     * @param rightColumns 右表的列名
//     * @param searchMap 搜索条件
//     * @param tailSql sql的尾部语句
//     * @return join执行后的结果
//     */
//    // todo 库对象中的表和列可以有多个
//    public NeoMap one(Columns... columns, NeoMap... searchMaps, String tailSql){
//        String joinHeadSql = buildJoinHead(neo, columns);
//        String joinTailSql = buildJoinTail(sqlCondition, searchMap, tailSql);
//        return neo.exeOne(joinHeadSql + " " + joinSql + " " + joinTailSql);
//    }

    public String oneStr(List<Columns> columns, List<NeoMap> searchMapList, String tailSql) {
        String joinHeadSql = buildJoinHead(neo, columns);
        String joinTailSql = buildJoinTail(sqlCondition, searchMapList, tailSql);
        return joinHeadSql + " " + joinSql + " " + joinTailSql;
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
        String joinHeadSql = buildJoinHead(neo, leftTableName, leftColumns, rightTableName, rightColumns);
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

    public NeoMap one(){
        return one(Columns.all(neo, leftTableName), Columns.all(neo, rightTableName));
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
        String joinHeadSql = buildJoinHead(neo, leftTableName, leftColumns, rightTableName, rightColumns);
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
     * @param tableName 表名
     * @param columnName 列名
     * @param tClass 目标类型
     * @param searchMap 搜索条件
     * @param tailSql 尾部sql，比如order by xxx
     * @param <T> 返回值类型
     * @return 返回的指定类型的值
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
     * 对于没有表名的，则这里默认为左表的列
     * @param columnName 列名
     * @param tClass 返回值的类型
     * @param searchMap 搜索map
     * @param tailSql 尾部sql
     * @param <T> 返回值的类型
     * @return 返回值对应的数据
     */
    public <T> T value(String columnName, Class<T> tClass, NeoMap searchMap, String tailSql){
        return value(leftTableName, columnName, tClass, searchMap, tailSql);
    }

    @SuppressWarnings("unchecked")
    public <T> T value(String columnName, Class<T> tClass, NeoMap searchMap){
        return value(leftTableName, columnName, tClass, searchMap);
    }

    public String value(String columnName, NeoMap searchMap, String tailSql){
        return value(leftTableName, columnName, searchMap, tailSql);
    }

    public String value(String columnName, NeoMap searchMap){
        return value(leftTableName, columnName, searchMap);
    }

    @SuppressWarnings("unchecked")
    public <T> T value(String columnName, Class<T> tClass, Object entity, String tailSql){
        return value(leftTableName, columnName, tClass, entity, tailSql);
    }

    @SuppressWarnings("unchecked")
    public <T> T value(String columnName, Class<T> tClass, Object entity){
        return value(leftTableName, columnName, tClass, entity);
    }

    public String value(String columnName, Object entity, String tailSql){
        return value(leftTableName, columnName, entity, tailSql);
    }

    public String value(String columnName, Object entity){
        return value(leftTableName, columnName, entity);
    }

    @SuppressWarnings("unchecked")
    public <T> T value(String columnName, Class<T> tClass, String tailSql){
        return value(leftTableName, columnName, tClass, tailSql);
    }

    @SuppressWarnings("unchecked")
    public <T> T value(String columnName, Class<T> tClass){
        return value(leftTableName, columnName, tClass);
    }

    public String value(String columnName){
        return value(leftTableName, columnName);
    }

    /**
     * join核查中的查询一个数据
     * @param tableName 表名
     * @param columnName 列名
     * @param tClass 目标class
     * @param searchMap 搜索条件
     * @param tailSql sql的尾部语句
     * @param <T> 返回值的目标类型
     * @return 返回值list
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

    @SuppressWarnings("unchecked")
    public <T> List<T> values(String columnName, Class<T> tClass, NeoMap searchMap, String tailSql){
        return values(leftTableName, columnName, tClass, searchMap, tailSql);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> values(String columnName, Class<T> tClass, NeoMap searchMap){
        return values(leftTableName, columnName, tClass, searchMap);
    }

    public List<String> values(String columnName, NeoMap searchMap, String tailSql){
        return values(leftTableName, columnName, searchMap, tailSql);
    }

    public List<String> values(String columnName, NeoMap searchMap){
        return values(leftTableName, columnName, searchMap);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> values(String columnName, Class<T> tClass, Object entity, String tailSql){
        return values(leftTableName, columnName, tClass, entity, tailSql);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> values(String columnName, Class<T> tClass, Object entity){
        return values(leftTableName, columnName, tClass, entity);
    }

    public List<String> values(String columnName, Object entity, String tailSql){
        return values(leftTableName, columnName, entity, tailSql);
    }

    public List<String> values(String columnName, Object entity){
        return values(leftTableName, columnName, entity);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> values(String columnName, Class<T> tClass, String tailSql){
        return values(leftTableName, columnName, tClass, tailSql);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> values(String columnName, Class<T> tClass){
        return values(leftTableName, columnName, tClass);
    }

    public List<String> values(String columnName){
        return values(leftTableName, columnName);
    }
}
