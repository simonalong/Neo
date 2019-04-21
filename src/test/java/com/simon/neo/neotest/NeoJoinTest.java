package com.simon.neo.neotest;

import com.simon.neo.Columns;
import com.simon.neo.NeoMap;
import com.simon.neo.db.NeoColumn.Column;
import java.sql.SQLException;
import org.junit.Test;

/**
 * CREATE TABLE `neo_table1` (
 *   `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
 *   `group` char(64) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '数据来源组，外键关联lk_config_group',
 *   `name` varchar(64) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '任务name',
 *   `user_name` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '修改人名字',
 *   `age` int(11) DEFAULT NULL,
 *   PRIMARY KEY (`id`),
 *   KEY `group_index` (`group`)
 * ) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
 *
 * CREATE TABLE `neo_table2` (
 *   `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
 *   `group` char(64) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '数据来源组，外键关联lk_config_group',
 *   `name` varchar(64) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '任务name',
 *   `user_name` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '修改人名字',
 *   `age` int(11) DEFAULT NULL,
 *   `n_id` int(11) unsigned NOT NULL,
 *   `sort` int(11) NOT NULL,
 *   PRIMARY KEY (`id`),
 *   KEY `group_index` (`group`)
 * ) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
 */

/**
 * @author zhouzhenyong
 * @since 2019/4/20 下午7:54
 */
public class NeoJoinTest extends NeoBaseTest {

    public NeoJoinTest() throws SQLException {}

    /**
     * join 采用的是innerJoin
     *
     * select neo_table2.`id` from neo_table2 inner join neo_table1 on neo_table2.`n_id`=neo_table1.`id`   limit 1
     */
    @Test
    public void joinOneTest1() {
        String tableName = "neo_table2";
        String otherTableName = "neo_table1";
        String tailSql = "order by sort desc";
        show(neo.join(tableName, otherTableName).on("n_id", "id").one(Columns.of("id"), tailSql));
    }

    /**
     * join 采用的是innerJoin
     *
     * select neo_table2.`id`, neo_table1.`group` from neo_table2 inner join neo_table1 on neo_table2.`n_id`=neo_table1.`id` order by sort desc limit 1
     */
    @Test
    public void joinOneTest2() {
        String tableName = "neo_table2";
        String otherTableName = "neo_table1";
        String tailSql = "order by sort desc";
        show(neo.join(tableName, otherTableName).on("n_id", "id")
            .one(Columns.of("id"), Columns.of("group"), NeoMap.of(), tailSql));
    }

    /**
     * join 采用的是innerJoin
     *
     * 针对多个列，这里支持别名的方式
     *
     * select neo_table2.`group` as group1, neo_table1.`group` as group2 from neo_table2 inner join neo_table1 on neo_table2.`n_id`=neo_table1.`id` order by sort desc limit 1
     */
    @Test
    public void joinOneTest() {
        String tableName = "neo_table2";
        String otherTableName = "neo_table1";
        String tailSql = "order by sort desc";
        show(neo.join(tableName, otherTableName).on("n_id", "id")
            .one(Columns.of("group as group1"), Columns.of("group as group2"), NeoMap.of(), tailSql));
    }

    /**
     * join 采用的是innerJoin
     *
     * select neo_table2.`group` from neo_table2 inner join neo_table1 on neo_table2.`n_id`=neo_table1.`id`
     */
    @Test
    public void joinListValueTest() {
        String tableName = "neo_table2";
        String otherTableName = "neo_table1";
        show(neo.join(tableName, otherTableName).on("n_id", "id")
            .list(Columns.of("group")));
    }

    /**
     * join 采用的是innerJoin
     *
     * select neo_table2.`group` as group1, neo_table1.`group` as group2 from neo_table2 inner join neo_table1 on neo_table2.`n_id`=neo_table1.`id`
     */
    @Test
    public void joinListValuesTest() {
        String tableName = "neo_table2";
        String otherTableName = "neo_table1";
        show(neo.join(tableName, otherTableName).on("n_id", "id")
            .list(Columns.of("group as group1"), Columns.of("group as group2")));
    }

    /**
     * select neo_table2.`id` from neo_table2 left join neo_table1 on neo_table2.`n_id`=neo_table1.`id` order by sort desc
     */
    @Test
    public void leftJoinListTest1() {
        String tableName = "neo_table2";
        String otherTableName = "neo_table1";
        String tailSql = "order by sort desc";
        show(neo.leftJoin(tableName, otherTableName).on("n_id", "id").list(Columns.of("id"), tailSql));
    }

    /**
     * select neo_table2.group from neo_table2 right join neo_table1 on neo_table2.`n_id`=neo_table1.`id` order by sort desc limit 1
     */
    @Test
    public void leftJoinListTest2() {
        String tableName = "neo_table2";
        String otherTableName = "neo_table1";
        String tailSql = "order by sort desc";
        show(neo.rightJoin(tableName, otherTableName).on("n_id", "id")
            .value(tableName, "group", tailSql));
    }

    /**
     * select neo_table2.group from neo_table2 inner join neo_table1 on neo_table2.`n_id`=neo_table1.`id` order by sort desc
     */
    @Test
    public void leftJoinListTest3() {
        String tableName = "neo_table2";
        String otherTableName = "neo_table1";
        String tailSql = "order by sort desc";
        show(neo.innerJoin(tableName, otherTableName).on("n_id", "id")
            .values(tableName, "group", tailSql));
    }

    /**
     * 请注意，mysql 不支持 full join
     *
     * select neo_table2.`id`, neo_table1.`group` from neo_table2 outer join neo_table1 on neo_table2.`n_id`=neo_table1.`id` order by sort desc
     */
    @Test
    public void outerJoinTest() {
        String tableName = "neo_table2";
        String otherTableName = "neo_table1";
        String tailSql = "order by sort desc";
        show(neo.outerJoin(tableName, otherTableName).on("group", "group")
            .list(Columns.of("id"), Columns.of("group"), NeoMap.of(), tailSql));
    }

    /**
     * select neo_table2.`group`, neo_table2.`id` from neo_table2 left join neo_table1 on neo_table2.`group`=neo_table1.`group`  where (neo_table1.id is null) order by sort desc
     */
    @Test
    public void leftJoinExceptInnerTest() {
        String tableName = "neo_table2";
        String otherTableName = "neo_table1";
        String tailSql = "order by sort desc";
        show(neo.leftJoinExceptInner(tableName, otherTableName).on("group", "group")
            .list(Columns.of("id", "group"), NeoMap.of(), tailSql));
    }

    /**
     * select neo_table1.`group` from neo_table2 right join neo_table1 on neo_table2.`group`=neo_table1.`group`  where (neo_table2.id is null) order by sort desc
     */
    @Test
    public void rightJoinExceptInnerTest() {
        String tableName = "neo_table2";
        String otherTableName = "neo_table1";
        String tailSql = "order by sort desc";
        show(neo.rightJoinExceptInner(tableName, otherTableName).on("group", "group")
            .list(Columns.of(), Columns.of("group"), tailSql));
    }

    /**
     * 请注意，mysql 不支持 full join
     *
     * select neo_table2.`id`, neo_table1.`group` from neo_table2 outer join neo_table1 on neo_table2.`group`=neo_table1.`group`  where (neo_table2.id is nullneo_table2.id is null) order by sort desc
     */
    @Test
    public void outerJoinExceptInnerTest() {
        String tableName = "neo_table2";
        String otherTableName = "neo_table1";
        String tailSql = "order by sort desc";
        show(neo.outerJoinExceptInner(tableName, otherTableName).on("group", "group")
            .list(Columns.of("id"), Columns.of("group"), NeoMap.of(), tailSql));
    }
}
