package com.simonalong.neo.neo;

import com.simonalong.neo.Columns;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.table.NeoPage;
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
     * select neo_table1.`id`
     * from neo_table1 inner join neo_table2 on neo_table1.`id`=neo_table2.`id`  order by `sort` desc limit 1
     */
    @Test
    public void joinOneTest1() {
        String table1 = "neo_table1";
        String table2 = "neo_table2";
        show(neo.join(table1, table2).on("id", "id")
            .one(Columns.of().table(table1, "id"), NeoMap.of("order by", "sort desc")));
    }

    /**
     * join 采用的是innerJoin
     *
     * 搜索字段为一个表的所有
     * select neo_table1.`group`, neo_table1.`user_name`, neo_table1.`age`, neo_table1.`id`, neo_table1.`name`
     * from neo_table1 inner join neo_table2 on neo_table1.`id`=neo_table2.`id`  order by `sort` desc limit 1
     */
    @Test
    public void joinOneTest2() {
        String table1 = "neo_table1";
        String table2 = "neo_table2";
        show(neo.join(table1, table2).on("id", "id")
            .one(Columns.of(neo).table(table1, "*").table(table2, "*"), NeoMap.of("order by", "neo_table2.`sort` desc")));
    }

    /**
     * join 采用的是innerJoin
     *
     * 搜索字段为多个表中的字段
     * select neo_table1.`id`, neo_table2.`group`  from neo_table1 inner join neo_table2 on neo_table1.`id`=neo_table2.`n_id`
     * order by `sort` desc limit 1
     */
    @Test
    public void joinOneTest3() {
        String table1 = "neo_table1";
        String table2 = "neo_table2";
        show(neo.join(table1, table2).on("id", "id")
            .one(Columns.of().table(table1, "id").table(table2, "group"), NeoMap.of("order by", "sort desc")));
    }

    /**
     * join 采用的是innerJoin
     *
     * 搜索字段为多个表中的所有
     * select neo_table1.`group`, neo_table2.`age`, neo_table1.`user_name`, neo_table2.`user_name`, neo_table2.`sort`,
     * neo_table1.`age`, neo_table1.`id`, neo_table2.`group`, neo_table1.`name`, neo_table2.`name`, neo_table2.`id`
     * from neo_table1 inner join neo_table2 on neo_table1.`id`=neo_table2.`n_id`  order by `sort` desc limit 1
     */
    @Test
    public void joinOneTest4() {
        String table1 = "neo_table1";
        String table2 = "neo_table2";
        show(neo.join(table1, table2).on("id", "n_id")
            .one(Columns.of(neo).table(table1, "*").table(table2, "*"), NeoMap.of("order by", "sort desc")));
    }

    /**
     * join 采用的是innerJoin
     *
     * 针对列的别名方式使用
     *
     * select neo_table2.`group` as group2, neo_table1.`group` as group1  from neo_table1 inner join neo_table2 on neo_table1.`id`=neo_table2.`n_id`
     * order by `sort` desc limit 1
     */
    @Test
    public void joinOneTest5() {
        String table1 = "neo_table1";
        String table2 = "neo_table2";
        // {group1=group3, group2=group4}
        show(neo.join(table1, table2).on("id", "n_id")
            .one(Columns.of().table(table1, "group as group1").table(table2, "group as group2"), NeoMap.of("order by", "sort desc")));
    }

    /**
     * join 采用的是innerJoin
     *
     * 针对列的别名方式使用：在已有其他列情况下，别名会覆盖原列
     *
     * select neo_table1.`group` as group1  from neo_table1 inner join neo_table2 on neo_table1.`id`=neo_table2.`n_id`
     * order by `sort` desc limit 1
     */
    @Test
    public void joinOneTest6() {
        String table1 = "neo_table1";
        String table2 = "neo_table2";
        // {group1=group3}
        show(neo.join(table1, table2).on("id", "n_id")
            .one(Columns.of().table(table1, "group", "group as group1"), NeoMap.of("order by", "sort desc")));
    }

    /**
     * join 采用的是innerJoin
     *
     * 针对列的别名方式使用：在已有其他列情况下，别名的覆盖，对于*的覆盖
     *
     * select neo_table1.`user_name`, neo_table1.`age`, neo_table1.`id`, neo_table1.`group` as group1, neo_table1.`name`
     * from neo_table1 inner join neo_table2 on neo_table1.`id`=neo_table2.`n_id`  order by `sort` desc limit 1
     */
    @Test
    public void joinOneTest7() {
        String table1 = "neo_table1";
        String table2 = "neo_table2";
        // {group1=group3, id=13, name=name1, user_name=user_name1}
        show(neo.join(table1, table2).on("id", "n_id")
            .one(Columns.of(neo).table(table1, "*", "group as group1"), NeoMap.of("order by", "sort desc")));
    }

    /**
     * 针对有搜索条件情况下的使用
     *
     * select neo_table1.`group`, neo_table1.`user_name`, neo_table1.`age`, neo_table1.`id`, neo_table1.`name`
     * from neo_table1 inner join neo_table2 on neo_table1.`id`=neo_table2.`n_id`
     * where neo_table1.`group` =  ? and neo_table1.`id` =  ? null limit 1
     */
    @Test
    public void joinOneTest8() {
        String table1 = "neo_table1";
        String table2 = "neo_table2";
        // {group1=group3, id=13, name=name1, user_name=user_name1}
        show(neo.join(table1, table2).on("id", "n_id")
            .one(Columns.of(neo).table(table1, "*"), NeoMap.of().table(table1, "group", "group1", "id", 11)));
    }

    /**
     * 测试多条件
     *
     * select neo_table1.`group`, neo_table1.`user_name`, neo_table1.`age`, neo_table1.`id`, neo_table1.`name`
     * from neo_table1 inner join neo_table2 on neo_table1.`id`=neo_table2.`n_id`
     * where neo_table1.`group` =  ? and neo_table1.`id` =  ? and neo_table2.`group` =  ? order by `sort` desc limit 1
     */
    @Test
    public void joinOneTest9() {
        String table1 = "neo_table1";
        String table2 = "neo_table2";
        // {group1=group3, id=13, name=name1, user_name=user_name1}
        show(neo.join(table1, table2).on("id", "n_id")
            .one(Columns.of(neo).table(table1, "*"), NeoMap.of().table(table1, "group", "group1", "id", 11)
                .table(table2, "group", "group1").append("order by", "sort desc")));
    }

    /**
     * join 采用的是innerJoin
     *
     * select neo_table1.`group`  from neo_table1 inner join neo_table2 on neo_table1.`id`=neo_table2.`n_id`
     */
    @Test
    public void joinListTest1() {
        String table1 = "neo_table1";
        String table2 = "neo_table2";
        // [{group=group1}, {group=group1}, {group=group2}, {group=group3}]
        show(neo.join(table1, table2).on("id", "n_id")
            .list(Columns.of().table(table1, "group"), NeoMap.of()));
    }

    /**
     * join 采用的是innerJoin
     *
     * select neo_table1.`user_name`, neo_table2.`group` as group2, neo_table1.`age`, neo_table1.`id`, neo_table1.`group` as group1, neo_table1.`name`
     * from neo_table1 inner join neo_table2 on neo_table1.`id`=neo_table2.`id`
     */
    @Test
    public void joinListTest2() {
        String table1 = "neo_table1";
        String table2 = "neo_table2";
        // [{group1=group1, group2=group1, id=11, name=}, {group1=group1, group2=group2, id=11, name=}, {group1=group2, group2=group3, id=12, name=haode}, {group1=group3, group2=group4, id=13, name=name1, user_name=user_name1}]
        show(neo.join(table1, table2).on("id", "id")
            .list(Columns.of(neo).table(table1, "*", "group as group1").table(table2, "group as group2")));
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
        // [{id=13}, {id=11}, {id=11}, {id=12}]
        show(neo.leftJoin(table1, table2).on("id", "id")
            .list(Columns.of().table(table1, "id"),  NeoMap.of("order by", "sort desc")));
    }

    /**
     * select neo_table1.`group`
     * from neo_table1 right join neo_table2 on neo_table1.`id`=neo_table2.`n_id`  order by `sort` desc limit 1
     */
    @Test
    public void leftJoinListTest2() {
        String table1 = "neo_table1";
        String table2 = "neo_table2";
        show(neo.rightJoin(table1, table2).on("id", "id")
            .value(table1, "group", NeoMap.of("order by", "sort desc")));
    }

    /**
     * order by的放置，可以放到table里面，这样，其中order by后面的属性就是当前表的
     *
     * select neo_table1.`group`
     * from neo_table1 left join neo_table2 on neo_table1.`id`=neo_table2.`id`
     * where neo_table2.`group` =  ? order by neo_table2.`sort` desc
     */
    @Test
    public void leftJoinOrderByTest1() {
        String table1 = "neo_table1";
        String table2 = "neo_table2";
        // [group3, group1, group2]
        show(neo.leftJoin(table1, table2).on("id", "id")
            .values(table1, "group", NeoMap.of().table(table2, "group", "jj", "order by", "sort desc")));
    }

    /**
     * order by的放置，里面可以放置多个参数
     *
     * select neo_table1.`group`  from neo_table1 left join neo_table2 on neo_table1.`id`=neo_table2.`id`
     * order by neo_table1.`name` desc, neo_table1.`group` asc
     */
    @Test
    public void leftJoinOrderByTest2() {
        String table1 = "neo_table1";
        String table2 = "neo_table2";
        // [group3, group1, group2]
        show(neo.leftJoin(table1, table2).on("id", "id")
            .values(table1, "group", NeoMap.of().table(table1, "order by", "name desc, group asc")));
    }

    /**
     * order by的放置，如果放到里面，也可以指定对应的表名
     *
     * select neo_table1.`group`
     * from neo_table1 left join neo_table2 on neo_table1.`id`=neo_table2.`id`
     * where neo_table1.`group` =  ? order by neo_table2.`sort` desc
     */
    @Test
    public void leftJoinOrderByTest3() {
        String table1 = "neo_table1";
        String table2 = "neo_table2";
        // [group3, group1, group2]
        show(neo.leftJoin(table1, table2).on("id", "id")
            .values(table1, "group", NeoMap.of().table(table1, "group", "jj", "order by", "neo_table2.sort desc")));
    }

    /**
     * select neo_table1.`group`
     * from neo_table1 left join neo_table2 on neo_table1.`id`=neo_table2.`id`
     * where neo_table2.`group` =  ? order by neo_table1.`group` desc
     */
    @Test
    public void leftJoinOrderByTest4() {
        String table1 = "neo_table1";
        String table2 = "neo_table2";
        // [group3, group1, group2]
        show(neo.leftJoin(table1, table2).on("id", "id")
            .values(table1, "group",
                NeoMap.of().table(table2, "group", "jj").append("order by", "neo_table1.group desc")));
    }

    /**
     * order by可以多个表之间的排序进行关联
     *
     * select neo_table1.`group`
     * from neo_table1 left join neo_table2 on neo_table1.`id`=neo_table2.`id`
     * where neo_table1.`name` =  ? and neo_table2.`group` =  ?
     * order by neo_table2.`sort` desc, neo_table1.`group` asc
     */
    @Test
    public void leftJoinOrderByTest5() {
        String table1 = "neo_table1";
        String table2 = "neo_table2";
        // [group3, group1, group2]
        show(neo.leftJoin(table1, table2).on("id", "id")
            .values(table1, "group", NeoMap.of().table(table2, "group", "jj", "order by", "sort desc")
                    .table(table1, "name", "22", "order by", "group asc")));
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
//        // [group3, group1, group2]
//        show(neo.outerJoin(table1, table2).on("id", "n_id")
//            .values(table1, "group", tailSql, NeoMap.of("order by", "sort desc")));
//    }

    /**
     * 多表join
     *
     * select neo_table1.`group`, neo_table1.`id`, neo_table2.`name`
     * from neo_table1 right join neo_table2 on neo_table1.`id`=neo_table2.`id`
     * right join neo_table3 on neo_table2.`name`=neo_table3.`name`  limit 1
     */
    // todo 多表join的时候sql有点问题
    @Test
    public void multiJoinTest() {
        String table1 = "neo_table1";
        String table2 = "neo_table2";
        String table3 = "neo_table3";
        show(neo.rightJoin(table1, table2).on("id", "id")
            .rightJoin(table2, table3).on("name", "name")
            .one(Columns.of().table(table1, "id", "group").table(table2, "name")));
    }

    /**
     * select neo_table1.`group`, neo_table1.`id`, neo_table2.`group`, neo_table1.`name`, neo_table2.`name`, neo_table2.`id`
     * from neo_table1 inner join neo_table2 on neo_table1.`name`=neo_table2.`name`
     * where neo_table1.`group` =  ? and neo_table1.`name` =  ? and neo_table2.`name` =  ? limit 1
     */
    @Test
    public void oneTest(){
        // select neo_table1.`group`, neo_table1.`id`, neo_table2.`group`, neo_table1.`name`, neo_table2.`name`, neo_table2.`id`
        // from neo_table1 inner join neo_table2 on neo_table1.`name`=neo_table2.`name`  where `neo_table1.group` = 'ok' and `neo_table1.name` = 'haode'
        String table1 = "neo_table1";
        String table2 = "neo_table2";
        show(neo.join(table1, table2).on("name", "name")
            .one(Columns.of().table(table1, "id", "name", "group").table(table2, "id", "name", "group"),
                NeoMap.of().table(table1, "group", "ok", "name", "haode").table(table2, "name", "ceshi")));
    }

    /**
     * left join except inner
     *
     * 该处理的时候会自动增加一个条件，就是设置右表的主键为null，其实就是取的一个左表减去和右表公共的部分
     *
     * select neo_table1.`group`, neo_table1.`id`, neo_table2.`group`, neo_table1.`name`, neo_table2.`name`, neo_table2.`id`
     * from neo_table1 left join neo_table2 on neo_table1.`name`=neo_table2.`name`
     * where (neo_table2.id is null) limit 1
     */
    @Test
    public void leftJoinExceptInnerTest(){
        String table1 = "neo_table1";
        String table2 = "neo_table2";
        show(neo.leftJoinExceptInner(table1, table2).on("name", "name")
            .one(Columns.of().table(table1, "id", "name", "group").table(table2, "id", "name", "group"))
        );
    }

    /**
     * join的分页查询
     *
     * select neo_table1.`group`, neo_table1.`user_name`, neo_table1.`age`, neo_table1.`id`, neo_table1.`name`
     * from neo_table1 inner join neo_table2 on neo_table1.`id`=neo_table2.`id`
     * order by neo_table1.`group` desc
     * limit 0, 12
     */
    @Test
    public void pageTest(){
        String table1 = "neo_table1";
        String table2 = "neo_table2";
        show(neo.join(table1, table2).on("id", "id")
            .page(Columns.of(neo).table(table1, "*"), NeoMap.of().table(table1, "order by", "group desc"),
                NeoPage.of(1, 12)));
    }

    /**
     * 表的别名
     *
     * 这里要通过在表的前头直接填写即可
     *
     * select t1.`name`, t1.`id`, t1.`user_name`, t1.`group`, t1.`age`
     * from neo_table1 as t1 inner join neo_table2 as t2 on t1.`id`=t2.`id`
     * order by t2.`id` desc limit 0, 12
     */
    @Test
    public void tableAsTest1(){
        String table1 = "neo_table1 as t1";
        String table2 = "neo_table2 as t2";
        show(neo.join(table1, table2).on("id", "id")
            .page(Columns.of(neo).table(table1, "*"), NeoMap.of().table(table2, "order by", "id desc"),
                NeoPage.of(1, 12)));
    }

    /**
     * 自己和自己
     *
     * select t2.`n_id`, t2.`age`, t2.`sort`, t2.`user_name`, t2.`name`, t2.`group`, t2.`enum1`, t2.`id`
     * from neo_table3 as t1 left join neo_table3 as t2 on t1.`id`=t2.`n_id`
     *
     * 需要利用到别名系统才行
     */
    @Test
    public void joinSelfTest1(){
        String table1 = "neo_table3 as t1";
        String table2 = "neo_table3 as t2";
        neo.leftJoin(table1, table2).on("id", "n_id").list(Columns.of(neo).table(table2, "*"));
    }
}
