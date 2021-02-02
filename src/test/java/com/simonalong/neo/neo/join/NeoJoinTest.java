package com.simonalong.neo.neo.join;

import com.simonalong.neo.*;
import com.simonalong.neo.db.NeoPage;
import com.simonalong.neo.db.TableJoinOn;
import com.simonalong.neo.sql.builder.JoinSqlBuilder;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

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

    /**
     * 基本的join处理
     */
    @Test
    public void testJoin1() {
        NeoJoiner neoJoiner = neo.joiner();

        // 配置的列
        Columns columns = Columns.of().setNeo(neo);
        columns.table("neo_table1", "age");
        columns.table("neo_table2", "name");
        columns.table("neo_table2", "group");

        // 配置多表join
        TableJoinOn tableJoinOn = new TableJoinOn("neo_table1");
        tableJoinOn.leftJoin("neo_table1", "neo_table2").on("id", "n_id");
        tableJoinOn.leftJoin("neo_table2", "neo_table3").on("n_id", "n_id");

        // 配置查询条件
        TableMap searchMap = TableMap.of();
        searchMap.put("neo_table1", "name", "nihao");
        searchMap.put("neo_table2", "group", "ok");

        // select
        // neo_table1.`age` as neo_table1_dom_age,
        // neo_table2.`group` as neo_table2_dom_group,
        // neo_table2.`name` as neo_table2_dom_name
        //
        // from
        // neo_table1 left join neo_table2 on neo_table1.`id`=neo_table2.`n_id`
        // left join neo_table3 on neo_table2.`n_id`=neo_table3.`n_id`
        //
        // where neo_table2.`group` =  ? and neo_table1.`name` =  ?

        // [ok, nihao]
        show(neoJoiner.one(columns, tableJoinOn, searchMap));
    }

    /**
     * order by 排序搜索
     */
    @Test
    public void testJoin2() {
        NeoJoiner neoJoiner = neo.joiner();

        // 配置的列
        Columns columns = Columns.of().setNeo(neo);
        columns.table("neo_table1", "age");
        columns.table("neo_table2", "name");
        columns.table("neo_table2", "group");

        // 配置多表join
        TableJoinOn tableJoinOn = new TableJoinOn("neo_table1");
        tableJoinOn.leftJoin("neo_table1", "neo_table2").on("id", "n_id");
        tableJoinOn.leftJoin("neo_table2", "neo_table3").on("n_id", "n_id");

        // 配置查询条件
        TableMap searchMap = TableMap.of();
        searchMap.put("neo_table1", "name", "nihao");
        searchMap.put("neo_table2", "group", "ok");
        searchMap.put("neo_table2", "order by", "age desc");

        // select
        //
        // neo_table1.`age` as neo_table1_dom_age,
        // neo_table2.`group` as neo_table2_dom_group,
        // neo_table2.`name` as neo_table2_dom_name
        //
        // from
        // neo_table1 left join neo_table2 on neo_table1.`id`=neo_table2.`n_id`
        // left join neo_table3 on neo_table2.`n_id`=neo_table3.`n_id`
        //
        // where neo_table2.`group` =  ? and neo_table1.`name` =  ?
        //
        // order by neo_table2.`age` desc

        // [ok, nihao]
        show(neoJoiner.one(columns, tableJoinOn, searchMap));
        show(JoinSqlBuilder.build(columns, tableJoinOn, searchMap));
        // [ok, nihao]
        show(JoinSqlBuilder.buildValueList(searchMap));
    }

    /**
     * in 集合搜索
     */
    @Test
    public void testJoin3() {
        NeoJoiner neoJoiner = neo.joiner();

        // 配置的列
        Columns columns = Columns.of().setNeo(neo);
        columns.table("neo_table1", "age");
        columns.table("neo_table2", "name");
        columns.table("neo_table2", "group");

        // 配置多表join
        TableJoinOn tableJoinOn = new TableJoinOn("neo_table1");
        tableJoinOn.leftJoin("neo_table1", "neo_table2").on("id", "n_id");
        tableJoinOn.leftJoin("neo_table2", "neo_table3").on("n_id", "n_id");

        // 配置查询条件
        TableMap searchMap = TableMap.of();
        searchMap.put("neo_table1", "name", "nihao");
        searchMap.put("neo_table2", "group", "ok");

        // select
        //
        // neo_table1.`age` as neo_table1_dom_age,
        // neo_table2.`group` as neo_table2_dom_group,
        // neo_table2.`name` as neo_table2_dom_name
        //
        // from
        // neo_table1 left join neo_table2 on neo_table1.`id`=neo_table2.`n_id`
        // left join neo_table3 on neo_table2.`n_id`=neo_table3.`n_id`
        //
        // where
        // neo_table2.`group` =  ? and
        // neo_table2.`n_id` in ('12','34','232') and
        // neo_table1.`name` =  ?

        // [ok, nihao]
        show(neoJoiner.one(columns, tableJoinOn, searchMap));
    }

    /**
     * 查询多列
     */
    @Test
    public void testJoin4() {
        NeoJoiner neoJoiner = neo.joiner();

        // 配置的列
        Columns columns = Columns.of().setNeo(neo);
        columns.table("neo_table1", "age");
        columns.table("neo_table2", "name", "group", "user_name");

        // 配置多表join
        TableJoinOn tableJoinOn = new TableJoinOn("neo_table1");
        tableJoinOn.leftJoin("neo_table1", "neo_table2").on("id", "n_id");
        tableJoinOn.leftJoin("neo_table2", "neo_table3").on("n_id", "n_id");

        // 配置查询条件
        TableMap searchMap = TableMap.of();
        searchMap.put("neo_table1", "name", "nihao");
        searchMap.put("neo_table2", "group", "ok");

        // select
        //
        // neo_table2.`user_name` as neo_table2_dom_user_name,
        // neo_table1.`age` as neo_table1_dom_age,
        // neo_table2.`group` as neo_table2_dom_group,
        // neo_table2.`name` as neo_table2_dom_name
        //
        // from  neo_table1 left join neo_table2 on neo_table1.`id`=neo_table2.`n_id`
        // left join neo_table3 on neo_table2.`n_id`=neo_table3.`n_id`
        //
        // where
        // neo_table2.`group` =  ? and neo_table1.`name` =  ?

        // [ok, nihao]
        show(neoJoiner.one(columns, tableJoinOn, searchMap));
    }

    /**
     * 查询某个表全部列
     */
    @Test
    public void testJoin5() {
        NeoJoiner neoJoiner = neo.joiner();

        // 配置的列
        Columns columns = Columns.of().setNeo(neo);
        columns.table("neo_table1", "*");
        columns.table("neo_table2", "name");

        // 配置多表join
        TableJoinOn tableJoinOn = new TableJoinOn("neo_table1");
        tableJoinOn.leftJoin("neo_table1", "neo_table2").on("id", "n_id");
        tableJoinOn.leftJoin("neo_table2", "neo_table3").on("n_id", "n_id");

        // 配置查询条件
        TableMap searchMap = TableMap.of();
        searchMap.put("neo_table1", "name", "nihao");
        searchMap.put("neo_table2", "group", "ok");

        // select
        //
        // neo_table1.`id` as neo_table1_dom_id,
        // neo_table1.`name` as neo_table1_dom_name,
        // neo_table1.`sl` as neo_table1_dom_sl,
        // neo_table1.`group` as neo_table1_dom_group,
        // neo_table1.`age` as neo_table1_dom_age,
        // neo_table1.`user_name` as neo_table1_dom_user_name,
        // neo_table2.`name` as neo_table2_dom_name
        //
        // from
        // neo_table1 left join neo_table2 on neo_table1.`id`=neo_table2.`n_id`
        // left join neo_table3 on neo_table2.`n_id`=neo_table3.`n_id`
        //
        // where neo_table2.`group` =  ? and neo_table1.`name` =  ?

        // [ok, nihao]
        show(neoJoiner.one(columns, tableJoinOn, searchMap));
    }

    /**
     * 返回实体类型
     */
    @Test
    public void testJoin6() {
        NeoJoiner neoJoiner = neo.joiner();

        // 配置的列
        Columns columns = Columns.of().setNeo(neo);
        columns.table("neo_table1", "group");
        columns.table("neo_table2", "name");

        // 配置多表join
        TableJoinOn tableJoinOn = new TableJoinOn("neo_table1");
        tableJoinOn.leftJoin("neo_table1", "neo_table2").on("id", "n_id");
        tableJoinOn.leftJoin("neo_table2", "neo_table3").on("n_id", "n_id");

        // 配置查询条件
        TableMap searchMap = TableMap.of();
        searchMap.put("neo_table1", "name", "nihao");
        searchMap.put("neo_table2", "group", "ok");

        // select
        //
        // neo_table1.`id` as neo_table1_dom_id,
        // neo_table1.`name` as neo_table1_dom_name,
        // neo_table1.`sl` as neo_table1_dom_sl,
        // neo_table1.`group` as neo_table1_dom_group,
        // neo_table1.`age` as neo_table1_dom_age,
        // neo_table1.`user_name` as neo_table1_dom_user_name,
        // neo_table2.`name` as neo_table2_dom_name
        //
        // from
        // neo_table1 left join neo_table2 on neo_table1.`id`=neo_table2.`n_id`
        // left join neo_table3 on neo_table2.`n_id`=neo_table3.`n_id`
        //
        // where neo_table2.`group` =  ? and neo_table1.`name` =  ?

        // [ok, nihao]
        show(neoJoiner.one(JoinEntity2.class, columns, tableJoinOn, searchMap));
    }

    /**
     * like模糊查询
     */
    @Test
    public void testJoin7() {
        NeoJoiner neoJoiner = neo.joiner();

        // 配置的列
        Columns columns = Columns.of().setNeo(neo);
        columns.table("neo_table1", "group");
        columns.table("neo_table2", "name");

        // 配置多表join
        TableJoinOn tableJoinOn = new TableJoinOn("neo_table1");
        tableJoinOn.leftJoin("neo_table1", "neo_table2").on("id", "n_id");
        tableJoinOn.leftJoin("neo_table2", "neo_table3").on("n_id", "n_id");

        // 配置查询条件
        TableMap searchMap = TableMap.of();
        searchMap.put("neo_table1", "name", "nihao");
        searchMap.put("neo_table2", "group", "like #x#");

        // select
        // neo_table1.`group` as neo_table1_dom_group,
        // neo_table2.`name` as neo_table2_dom_name
        //
        // from
        // neo_table1 left join neo_table2 on neo_table1.`id`=neo_table2.`n_id`
        // left join neo_table3 on neo_table2.`n_id`=neo_table3.`n_id`
        //
        // where
        // neo_table2.`group` like '%x%' and
        // neo_table1.`name` =  ?
        show(neoJoiner.one(columns, tableJoinOn, searchMap));

        show(JoinSqlBuilder.build(columns, tableJoinOn, searchMap));
    }

    /**
     * 不同查询函数
     * <p>
     *  one, list, value, values, page, count
     */
    @Test
    public void testJoin8() {
        NeoJoiner neoJoiner = neo.joiner();

        // 配置的列
        Columns columns = Columns.of().setNeo(neo);
        columns.table("neo_table1", "group");
        columns.table("neo_table2", "name");

        // 配置多表join
        TableJoinOn tableJoinOn = new TableJoinOn("neo_table1");
        tableJoinOn.leftJoin("neo_table1", "neo_table2").on("id", "n_id");
        tableJoinOn.leftJoin("neo_table2", "neo_table3").on("n_id", "n_id");

        // 配置查询条件
        TableMap searchMap = TableMap.of();
        searchMap.put("neo_table1", "name", "nihao");
        searchMap.put("neo_table2", "group", "ok");

        // select
        //
        // neo_table1.`group` as neo_table1_dom_group,
        // neo_table2.`name` as neo_table2_dom_name
        //
        // from
        // neo_table1 left join neo_table2 on neo_table1.`id`=neo_table2.`n_id`
        // left join neo_table3 on neo_table2.`n_id`=neo_table3.`n_id`
        //
        // where neo_table2.`group` =  ? and neo_table1.`name` =  ?
        show(neoJoiner.one(columns, tableJoinOn, searchMap));
        show(neoJoiner.list(columns, tableJoinOn, searchMap));
        show(neoJoiner.value(columns, tableJoinOn, searchMap));
        show(neoJoiner.values(columns, tableJoinOn, searchMap));
        show(neoJoiner.page(columns, tableJoinOn, searchMap, NeoPage.of(1, 20)));
        show(neoJoiner.count(tableJoinOn, searchMap));

        show(JoinSqlBuilder.build(columns, tableJoinOn, searchMap));
    }
}
