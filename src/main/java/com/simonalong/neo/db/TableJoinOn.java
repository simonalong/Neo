package com.simonalong.neo.db;

import static com.simonalong.neo.db.AliasParser.*;
import com.simonalong.neo.sql.JoinType;
import com.simonalong.neo.sql.builder.SqlBuilder;
import lombok.Getter;

/**
 * 该类新版本修改仅为内部使用，不再对外使用
 *
 * @author zhouzhenyong
 * @since 2019/4/20 下午9:46
 */
public final class TableJoinOn {

    /**
     * 中间表的joiner
     */
    private TableJoiner innerTableJoiner;
    /**
     * join对应的：from 之后的数据：table1 left join table2 on table1.`a_id` = table2.`id` inner join table3 on table1.`b_id` = table3.`id`
     */
    @Getter
    private String joinSql = " ";

    public TableJoinOn(String tableName) {
        this.joinSql += tableName;
    }

    /**
     * 默认的join采用的是innerJoin
     *
     * @param leftTableName 左表表名
     * @param rightTableName 右表表名
     * @return 做关联的关联器
     */
    public TableJoinOn join(String leftTableName, String rightTableName){
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
    public TableJoinOn leftJoin(String leftTableName, String rightTableName){
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
    public TableJoinOn rightJoin(String leftTableName, String rightTableName){
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
    public TableJoinOn innerJoin(String leftTableName, String rightTableName){
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
    public TableJoinOn outerJoin(String leftTableName, String rightTableName){
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
    public TableJoinOn leftJoinExceptInner(String leftTableName, String rightTableName){
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
    public TableJoinOn rightJoinExceptInner(String leftTableName, String rightTableName){
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
    public TableJoinOn outerJoinExceptInner(String leftTableName, String rightTableName){
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
    public TableJoinOn on(String leftColumnName, String rightColumnName) {
        this.joinSql += buildJoin(leftColumnName, rightColumnName);
        return this;
    }

    private String buildJoin(String leftColumnName, String rightColumnName) {
        return " " + innerTableJoiner.getJoinType()
            .getSql() + " " + innerTableJoiner.getRightTableName() + " on " + innerTableJoiner.getLeftTableNameAlias() + "." + SqlBuilder.toDbField(
            leftColumnName) + "=" + innerTableJoiner.getRightTableNameAlias() + "." + SqlBuilder.toDbField(rightColumnName);
    }


    @Getter
    static class TableJoiner{

        /**
         * 左表名字
         */
        private final String leftTableName;
        /**
         * 左表别名
         */
        private final String leftTableNameAlias;
        /**
         * 右表名字
         */
        private final String rightTableName;
        /**
         * 右表别名
         */
        private final String rightTableNameAlias;
        /**
         * join的类型
         */
        private final JoinType joinType;

        TableJoiner(String leftTableName, String rightTableName, JoinType joinType){
            this.leftTableName = leftTableName;
            this.leftTableNameAlias = getAlias(leftTableName);
            this.rightTableName = rightTableName;
            this.rightTableNameAlias = getAlias(rightTableName);
            this.joinType = joinType;
        }
    }
}
