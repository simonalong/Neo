package com.simonalong.neo.db;

import static com.simonalong.neo.db.AliasParser.*;
import com.simonalong.neo.Columns;
import com.simonalong.neo.Neo;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.TableMap;
import com.simonalong.neo.sql.JoinType;
import com.simonalong.neo.sql.builder.JoinSqlBuilder;
import com.simonalong.neo.sql.builder.SqlBuilder;
import java.util.List;
import lombok.Getter;

/**
 * @author zhouzhenyong
 * @since 2019/4/20 下午9:46
 */
public final class NeoJoiner {

    private Neo neo;
    /**
     * 起始表的joiner
     */
    private TableJoiner tableJoiner;
    /**
     * 中间表的joiner
     */
    private TableJoiner innerTableJoiner;
    /**
     * join对应的：from 之后的数据：table1 left join table2 on table1.`a_id` = table2.`id` inner join table3 on table1.`b_id` = table3.`id`
     */
    @Getter
    private String joinSql = " ";
    /**
     * 在一些join中，会有一些额外的条件， 比如leftOuterJoin，这种就是leftJoin和other表的key为空情况，sqlCondition为：tableb.key is null
     */
    private String sqlCondition;


    public NeoJoiner(Neo neo, String tableName) {
        this.neo = neo;
        this.joinSql += tableName;
    }

    /**
     * 默认的join采用的是innerJoin
     *
     * @param leftTableName 左表表名
     * @param rightTableName 右表表名
     * @return 做关联的关联器
     */
    public NeoJoiner join(String leftTableName, String rightTableName){
        this.innerTableJoiner = new TableJoiner(leftTableName, rightTableName, JoinType.INNER_JOIN);
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
        this.innerTableJoiner = new TableJoiner(leftTableName, rightTableName, JoinType.LEFT_JOIN);
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
        this.innerTableJoiner = new TableJoiner(leftTableName, rightTableName, JoinType.RIGHT_JOIN);
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
        this.innerTableJoiner = new TableJoiner(leftTableName, rightTableName, JoinType.INNER_JOIN);
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
        this.innerTableJoiner = new TableJoiner(leftTableName, rightTableName, JoinType.OUTER_JOIN);
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
        this.innerTableJoiner = new TableJoiner(leftTableName, rightTableName, JoinType.LEFT_JOIN_EXCEPT_INNER);
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
        this.innerTableJoiner = new TableJoiner(leftTableName, rightTableName, JoinType.RIGHT_JOIN_EXCEPT_INNER);
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
        this.innerTableJoiner = new TableJoiner(leftTableName, rightTableName, JoinType.OUTER_JOIN_EXCEPT_INNER);
        return this;
    }

    /**
     * join 的关联字段
     *
     * @param leftColumnName 左表的列名
     * @param rightColumnName 右表的列名
     * @return 返回关联之后的Joiner
     */
    public NeoJoiner on(String leftColumnName, String rightColumnName) {
        this.joinSql += buildJoin(leftColumnName, rightColumnName);
        return this;
    }

    private String buildJoin(String leftColumnName, String rightColumnName) {
        if(null == innerTableJoiner){
            return tableJoiner.getLeftTableName() + " " + tableJoiner.getJoinType().getSql() + " " + tableJoiner.getRightTableName()
                + " on " + tableJoiner.getLeftTableNameAlias() + "." + SqlBuilder.toDbField(leftColumnName)
                + "=" + tableJoiner.getRightTableNameAlias() + "." + SqlBuilder.toDbField(rightColumnName);
        }else{
            return " " + innerTableJoiner.getJoinType().getSql() + " " + innerTableJoiner.getRightTableName()
                + " on " + innerTableJoiner.getLeftTableNameAlias() + "." + SqlBuilder.toDbField(leftColumnName)
                + "=" + innerTableJoiner.getRightTableNameAlias() + "." + SqlBuilder.toDbField(rightColumnName);
        }
    }

    /**
     * 根据列和搜索条件，生成sql
     *
     * @param columns 列名
     * @param searchMap 表的搜索条件
     * @return 拼接后的sql
     */
    private String generateSql(Columns columns, NeoMap searchMap){
        String joinHeadSql = JoinSqlBuilder.buildJoinHead(neo, columns);
        String joinTailSql = JoinSqlBuilder.buildJoinTail(sqlCondition, searchMap);
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
        return "select " + tableName + "." + SqlBuilder.toDbField(columnName) + " " + joinSql + " " + joinTailSql;
    }

    @Getter
    class TableJoiner{

        /**
         * 左表名字
         */
        private String leftTableName;
        /**
         * 左表别名
         */
        private String leftTableNameAlias;
        /**
         * 右表名字
         */
        private String rightTableName;
        /**
         * 右表别名
         */
        private String rightTableNameAlias;
        /**
         * join的类型
         */
        private JoinType joinType;

        TableJoiner(String leftTableName, String rightTableName, JoinType joinType){
            this.leftTableName = leftTableName;
            this.leftTableNameAlias = getAlias(leftTableName);
            this.rightTableName = rightTableName;
            this.rightTableNameAlias = getAlias(rightTableName);
            this.joinType = joinType;
        }
    }
}
