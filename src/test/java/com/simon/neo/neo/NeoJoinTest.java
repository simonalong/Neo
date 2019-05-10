package com.simon.neo.neo;

import com.simon.neo.Columns;
import com.simon.neo.NeoMap;
import com.simon.neo.db.NeoPage;
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
     * select neo_table1.`id` from neo_table1 inner join neo_table2 on neo_table1.`id`=neo_table2.`n_id`  order by sort desc limit 1
     */
    @Test
    public void joinOneTest1() {
        String table1 = "neo_table1";
        String table2 = "neo_table2";
        String tailSql = "order by sort desc";
        show(neo.join(table1, table2).on("id", "n_id")
            .one(Columns.table(table1).cs("id"), tailSql));
    }

    /**
     * join 采用的是innerJoin
     *
     * 搜索字段为一个表的所有
     * select neo_table1.`group`, neo_table1.`user_name`, neo_table1.`age`, neo_table1.`id`, neo_table1.`name`
     * from neo_table1 inner join neo_table2 on neo_table1.`id`=neo_table2.`n_id`  order by sort desc limit 1
     */
    @Test
    public void joinOneTest2() {
        String table1 = "neo_table1";
        String table2 = "neo_table2";
        String tailSql = "order by sort desc";
        show(neo.join(table1, table2).on("id", "n_id")
            .one(Columns.table(table1, neo).cs("*"), tailSql));
    }

    /**
     * join 采用的是innerJoin
     *
     * 搜索字段为多个表中的字段
     * select neo_table2.`id`, neo_table1.`group` from neo_table2 inner join neo_table1 on neo_table2.`n_id`=neo_table1.`id` order by sort desc limit 1
     */
    @Test
    public void joinOneTest3() {
        String table1 = "neo_table1";
        String table2 = "neo_table2";
        String tailSql = "order by sort desc";
        show(neo.join(table1, table2).on("id", "n_id")
            .one(Columns.table(table1).cs("id").and(table2).cs("group"), tailSql));
    }

    /**
     * join 采用的是innerJoin
     *
     * 搜索字段为多个表中的所有
     * select neo_table1.`group`, neo_table2.`age`, neo_table1.`user_name`, neo_table2.`user_name`, neo_table2.`n_id`,
     * neo_table2.`sort`, neo_table1.`age`, neo_table1.`id`, neo_table2.`group`, neo_table1.`name`, neo_table2.`name`,
     * neo_table2.`id`
     * from neo_table1 inner join neo_table2 on neo_table1.`id`=neo_table2.`n_id`  order by sort desc limit 1
     */
    @Test
    public void joinOneTest4() {
        String table1 = "neo_table1";
        String table2 = "neo_table2";
        String tailSql = "order by sort desc";
        show(neo.join(table1, table2).on("id", "n_id")
            .one(Columns.table(table1, neo).cs("*").and(table2).cs("*"), tailSql));
    }

    /**
     * join 采用的是innerJoin
     *
     * 针对列的别名方式使用
     *
     * select neo_table2.`group` as group2, neo_table1.`group` as group1
     * from neo_table1
     * inner join neo_table2 on neo_table1.`id`=neo_table2.`n_id`  order by sort desc limit 1
     */
    @Test
    public void joinOneTest5() {
        String table1 = "neo_table1";
        String table2 = "neo_table2";
        String tailSql = "order by sort desc";
        // {group1=group3, group2=group4}
        show(neo.join(table1, table2).on("id", "n_id")
            .one(Columns.table(table1).cs("group as group1").and(table2).cs("group as group2"), tailSql));
    }

    /**
     * join 采用的是innerJoin
     *
     * 针对列的别名方式使用：在已有其他列情况下，别名会覆盖原列
     *
     * select neo_table1.`group` as group1
     * from neo_table1
     * inner join neo_table2 on neo_table1.`id`=neo_table2.`n_id`  order by sort desc limit 1
     */
    @Test
    public void joinOneTest6() {
        String table1 = "neo_table1";
        String table2 = "neo_table2";
        String tailSql = "order by sort desc";
        // {group1=group3}
        show(neo.join(table1, table2).on("id", "n_id")
            .one(Columns.table(table1).cs("group", "group as group1"), tailSql));
    }

    /**
     * join 采用的是innerJoin
     *
     * 针对列的别名方式使用：在已有其他列情况下，别名的覆盖，对于*的覆盖
     *
     * select neo_table1.`user_name`, neo_table1.`age`, neo_table1.`id`, neo_table1.`group` as group1, neo_table1.`name`
     * from neo_table1
     * inner join neo_table2 on neo_table1.`id`=neo_table2.`n_id`  order by sort desc limit 1
     */
    @Test
    public void joinOneTest7() {
        String table1 = "neo_table1";
        String table2 = "neo_table2";
        String tailSql = "order by sort desc";
        // {group1=group3, id=13, name=name1, user_name=user_name1}
        show(neo.join(table1, table2).on("id", "n_id")
            .one(Columns.table(table1, neo).cs("*", "group as group1"), tailSql));
    }

    /**
     * 针对有搜索条件情况下的使用
     *
     * select neo_table1.`group`
     * from neo_table1 inner join neo_table2 on neo_table1.`id`=neo_table2.`n_id`
     * where neo_table1.`group` = 'group1' order by sort desc limit 1
     */
    @Test
    public void joinOneTest8() {
        String table1 = "neo_table1";
        String table2 = "neo_table2";
        String tailSql = "order by sort desc";
        // {group1=group3, id=13, name=name1, user_name=user_name1}
        show(neo.join(table1, table2).on("id", "n_id")
            .one(Columns.table(table1, neo).cs("*"), tailSql, NeoMap.table(table1).cs("group", "group1", "id", 11)));
    }

    /**
     * 测试多条件
     *
     * select neo_table1.`group`, neo_table1.`user_name`, neo_table1.`age`, neo_table1.`id`, neo_table1.`name`
     * from neo_table1 inner join neo_table2 on neo_table1.`id`=neo_table2.`n_id`
     * where neo_table1.`group` = 'group1' and neo_table1.`id` = 11 and neo_table2.`group` = 'group1' order by sort desc limit 1
     */
    @Test
    public void joinOneTest9() {
        String table1 = "neo_table1";
        String table2 = "neo_table2";
        String tailSql = "order by sort desc";
        // {group1=group3, id=13, name=name1, user_name=user_name1}
        show(neo.join(table1, table2).on("id", "n_id")
            .one(Columns.table(table1, neo).cs("*"), tailSql, NeoMap.table(table1).cs("group", "group1", "id", 11).and(table2).cs("group", "group1")));
    }

    /**
     * join 采用的是innerJoin
     *
     * select neo_table1.`group`
     * from neo_table1
     * inner join neo_table2 on neo_table1.`id`=neo_table2.`n_id`
     */
    @Test
    public void joinListTest1() {
        String table1 = "neo_table1";
        String table2 = "neo_table2";
        // [{group=group1}, {group=group1}, {group=group2}, {group=group3}]
        show(neo.join(table1, table2).on("id", "n_id")
            .list(Columns.table(table1).cs("group"), NeoMap.of()));
    }

    /**
     * join 采用的是innerJoin
     *
     * select neo_table1.`user_name`, neo_table2.`group` as group2, neo_table1.`age`, neo_table1.`id`,
     * neo_table1.`group` as group1, neo_table1.`name`
     * from neo_table1
     * inner join neo_table2 on neo_table1.`id`=neo_table2.`n_id`
     */
    @Test
    public void joinListTest2() {
        String table1 = "neo_table1";
        String table2 = "neo_table2";
        // [{group1=group1, group2=group1, id=11, name=}, {group1=group1, group2=group2, id=11, name=}, {group1=group2, group2=group3, id=12, name=haode}, {group1=group3, group2=group4, id=13, name=name1, user_name=user_name1}]
        show(neo.join(table1, table2).on("id", "n_id")
            .list(Columns.table(table1, neo).cs("*", "group as group1").and(table2).cs("group as group2"), NeoMap.of()));
    }

    /**
     * select neo_table1.`id`
     * from neo_table1
     * left join neo_table2 on neo_table1.`id`=neo_table2.`n_id`  order by sort desc
     */
    @Test
    public void leftJoinListTest1() {
        String table1 = "neo_table1";
        String table2 = "neo_table2";
        String tailSql = "order by sort desc";
        // [{id=13}, {id=11}, {id=11}, {id=12}]
        show(neo.leftJoin(table1, table2).on("id", "n_id").list(Columns.table(table1).cs("id"), tailSql, NeoMap.of()));
    }

    /**
     * select neo_table2.`group`
     * from neo_table2 right
     * join neo_table1 on neo_table2.`n_id`=neo_table1.`id`  order by sort desc limit 1
     */
    @Test
    public void leftJoinListTest2() {
        String table1 = "neo_table1";
        String table2 = "neo_table2";
        String tailSql = "order by sort desc";
        show(neo.rightJoin(table1, table2).on("id", "n_id")
            .value(table1, "group", tailSql, NeoMap.of()));
    }

    /**
     * select neo_table1.`group` from neo_table1 right join neo_table2 on neo_table1.`id`=neo_table2.`n_id`  order by sort desc
     */
    @Test
    public void leftJoinListTest3() {
        String table1 = "neo_table1";
        String table2 = "neo_table2";
        String tailSql = "order by sort desc";
        // [group3, group1, group2]
        show(neo.leftJoin(table1, table2).on("id", "n_id")
            .values(table1, "group", tailSql, NeoMap.of("group", "jj")));
    }

//    /**
//     * 请注意，mysql 不支持 full join
//     *
//     * select neo_table2.`id`, neo_table1.`group` from neo_table2 outer join neo_table1 on neo_table2.`n_id`=neo_table1.`id` order by sort desc
//     */
//    @Test
//    public void outerJoinTest() {
//        String table1 = "neo_table1";
//        String table2 = "neo_table2";
//        String tailSql = "order by sort desc";
//        // [group3, group1, group2]
//        show(neo.outerJoin(table1, table2).on("id", "n_id")
//            .values(table1, "group", tailSql, NeoMap.of()));
//    }

    /**
     * 多表join
     *
     * select neo_table1.`group`, neo_table1.`id`, neo_table2.`name`
     * from neo_table1 right join neo_table2 on neo_table1.`id`=neo_table2.`n_id`
     * right join neo_table3 on neo_table2.`name`=neo_table3.`name`    limit 1
     */
    @Test
    public void multiJoinTest() {
        String table1 = "neo_table1";
        String table2 = "neo_table2";
        String table3 = "neo_table3";
        show(neo.rightJoin(table1, table2).on("id", "n_id")
            .rightJoin(table2, table3).on("name", "name")
            .one(Columns.table(table1).cs("id", "group").and(table2).cs("name")));
    }

    /**
     * select neo_table1.`group`, neo_table1.`id`, neo_table2.`group`, neo_table1.`name`, neo_table2.`name`, neo_table2.`id`
     * from neo_table1 inner join neo_table2 on neo_table1.`name`=neo_table2.`name`
     * where neo_table1.`group` = 'ok' and neo_table1.`name` = 'haode' and neo_table2.`name` = 'ceshi'  limit 1
     */
    @Test
    public void oneTest(){
        // select neo_table1.`group`, neo_table1.`id`, neo_table2.`group`, neo_table1.`name`, neo_table2.`name`, neo_table2.`id`
        // from neo_table1 inner join neo_table2 on neo_table1.`name`=neo_table2.`name`  where `neo_table1.group` = 'ok' and `neo_table1.name` = 'haode'
        String table1 = "neo_table1";
        String table2 = "neo_table2";
        show(neo.join(table1, table2).on("name", "name")
            .one(Columns.table(table1).cs("id", "name", "group").and(table2).cs("id", "name", "group"),
                NeoMap.table(table1).cs("group", "ok", "name", "haode").and(table2).cs("name", "ceshi")));
    }

    /**
     * left join except inner
     *
     * 该处理的时候会自动增加一个条件，就是设置右表的主键为null，其实就是取的一个左表减去和右表公共的部分
     *
     * select neo_table1.`group`, neo_table1.`id`, neo_table2.`group`, neo_table1.`name`, neo_table2.`name`, neo_table2.`id`
     * from neo_table1 left join neo_table2 on neo_table1.`name`=neo_table2.`name`
     * where (neo_table2.id is null)  limit 1
     */
    @Test
    public void leftJoinExceptInnerTest(){
        String table1 = "neo_table1";
        String table2 = "neo_table2";
        show(neo.leftJoinExceptInner(table1, table2).on("name", "name")
            .one(Columns.table(table1).cs("id", "name", "group").and(table2).cs("id", "name", "group"))
        );
    }

    /**
     * join的分页查询
     *
     * select neo_table1.`group`, neo_table1.`user_name`, neo_table1.`age`, neo_table1.`id`, neo_table1.`name`
     * from neo_table1 inner join neo_table2 on neo_table1.`id`=neo_table2.`n_id`
     * order by sort desc
     * limit 0, 12
     */
    @Test
    public void pageTest(){
        String table1 = "neo_table1";
        String table2 = "neo_table2";
        String tailSql = "order by sort desc";
        show(neo.join(table1, table2).on("id", "n_id")
            .page(Columns.table(table1, neo).cs("*"), tailSql, NeoPage.of(1, 12)));
    }
}
