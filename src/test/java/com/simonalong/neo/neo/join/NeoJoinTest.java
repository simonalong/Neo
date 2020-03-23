package com.simonalong.neo.neo.join;

import com.simonalong.neo.Columns;
import com.simonalong.neo.NeoBaseTest;
import com.simonalong.neo.TableMap;
import com.simonalong.neo.db.NeoJoiner;
import com.simonalong.neo.sql.builder.JoinSqlBuilder;
import org.junit.Test;

import java.sql.SQLException;

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
 * 针对sql的join部分，这里支持如下这么几种
 * <p>
 *     <ul>
 *         <li>1.手动写sql</li>
 *         <li>2.手动写sql，并通过JoinSqlBuilder进行辅助</li>
 *         <li>3.调用JoinSqlBuilder 进行构造执行</li>
 *     </ul>
 * @author zhouzhenyong
 * @since 2019/4/20 下午7:54
 */
public class NeoJoinTest extends NeoBaseTest {

    public NeoJoinTest() throws SQLException {}

    /**
     * 手动拼接sql
     *
     * 支持复杂的例子 join
     */
    @Test
    public void joinTest1(){
        String table1 = "neo_table1";
        String table2 = "neo_table2";
        String table3 = "neo_table3";

        // 生成多个表的展示字段
        Columns columns = Columns.of(neo);
        columns.table(table1, "*");
        columns.table(table2, "id");

        // 多表的join关系
        NeoJoiner joinner = new NeoJoiner(neo, table1);
        joinner.leftJoin(table1, table2).on("id", "n_id");
        joinner.leftJoin(table2, table3).on("n_id", "n_id");

        // 多表的搜索条件
        TableMap searchMap = TableMap.of();
        searchMap.put(table1, "name", "group");
        searchMap.put(table2, "name", "12");
        searchMap.put(table2, "order by", "name desc");
        searchMap.put(table1, "order by", "group asc");

        // 注意，后面的参数跟%s是一一对应的，只有最后的那个参数是要跟 buildConditionWithWhere 一起设置，否则不用设置
        show(neo.exeList(
            "select %s from %s %s %s",
            JoinSqlBuilder.buildColumns(columns),
            JoinSqlBuilder.buildJoinOn(joinner),
            JoinSqlBuilder.buildConditionWithWhere(searchMap),
            JoinSqlBuilder.buildOrderBy(searchMap),

            // 该参数在 设置了 buildConditionWithWhere 的话，一定要设置，否则有异常问题
            JoinSqlBuilder.buildValueList(searchMap)
        ));
    }

    /**
     * 测试join，获取某一列
     */
    @Test
    public void joinTestOne1() {
        String table1 = "neo_table1";
        String table2 = "neo_table2";
        String table3 = "neo_table3";

        // 生成多个表的展示字段
        Columns columns = Columns.of(neo);
        columns.table(table1, "name");
        columns.table(table2, "id");

        // 多表的join关系
        NeoJoiner joinner = new NeoJoiner(neo, table1);
        joinner.leftJoin(table1, table2).on("id", "n_id");
        joinner.leftJoin(table2, table3).on("n_id", "n_id");

        // 多表的搜索条件
        TableMap searchMap = TableMap.of();
        searchMap.put(table1, "name", "group");
        searchMap.put(table2, "name", "12");
        searchMap.put(table2, "order by", "name desc");
        searchMap.put(table1, "order by", "group asc");

        // select neo_table1.`name` as neo_table1_NDom_name, neo_table2.`id` as neo_table2_NDom_id
        // from  neo_table1 left join neo_table2 on neo_table1.`id`=neo_table2.`n_id`
        //                  left join neo_table3 on neo_table2.`n_id`=neo_table3.`n_id`
        // where neo_table1.`name` =  ? and neo_table2.`name` =  ?
        // order by neo_table1.`group` asc, neo_table2.`name` desc
        show(neo.exeOne(JoinSqlBuilder.build(columns, joinner, searchMap), JoinSqlBuilder.buildValueList(searchMap).toArray()));
    }

    /**
     * 测试join，获取某表的所有列
     */
    @Test
    public void joinTestOne2() {
        String table1 = "neo_table1";
        String table2 = "neo_table2";
        String table3 = "neo_table3";

        // 生成多个表的展示字段
        Columns columns = Columns.of(neo);
        columns.table(table1, "*");
        columns.table(table2, "id");

        // 多表的join关系
        NeoJoiner joinner = new NeoJoiner(neo, table1);
        joinner.leftJoin(table1, table2).on("id", "n_id");
        joinner.leftJoin(table2, table3).on("n_id", "n_id");

        // 多表的搜索条件
        TableMap searchMap = TableMap.of();
        searchMap.put(table1, "name", "group");
        searchMap.put(table2, "name", "12");
        searchMap.put(table2, "order by", "name desc");
        searchMap.put(table1, "order by", "group asc");

        // select
        //      neo_table1.`group` as neo_table1_NDom_group,
        //      neo_table1.`name` as neo_table1_NDom_name,
        //      neo_table1.`user_name` as neo_table1_NDom_user_name,
        //      neo_table1.`sl` as neo_table1_NDom_sl,
        //      neo_table1.`id` as neo_table1_NDom_id,
        //      neo_table2.`id` as neo_table2_NDom_id,
        //      neo_table1.`age` as neo_table1_NDom_age
        // from  neo_table1 left join neo_table2 on neo_table1.`id`=neo_table2.`n_id`
        //                  left join neo_table3 on neo_table2.`n_id`=neo_table3.`n_id`
        // where neo_table1.`name` =  ? and neo_table2.`name` =  ?
        // order by neo_table1.`group` asc, neo_table2.`name` desc
        show(neo.exeList(JoinSqlBuilder.build(columns, joinner, searchMap), JoinSqlBuilder.buildValueList(searchMap).toArray()));
    }

    /**
     * 获取并转换为实体
     */
    @Test
    public void joinTestOne3() {
        String table1 = "neo_table1";
        String table2 = "neo_table2";
        String table3 = "neo_table3";

        // 生成多个表的展示字段
        Columns columns = Columns.of(neo);
        columns.table(table1, "name");
        columns.table(table2, "id");

        // 多表的join关系
        NeoJoiner joinner = new NeoJoiner(neo, table1);
        joinner.leftJoin(table1, table2).on("id", "n_id");
        joinner.leftJoin(table2, table3).on("n_id", "n_id");

        // 多表的搜索条件
        TableMap searchMap = TableMap.of();
        searchMap.put(table1, "name", "group");
        searchMap.put(table2, "name", "12");
        searchMap.put(table2, "order by", "name desc");
        searchMap.put(table1, "order by", "group asc");

        // select neo_table1.`name` as neo_table1_NDom_name, neo_table2.`id` as neo_table2_NDom_id
        // from  neo_table1 left join neo_table2 on neo_table1.`id`=neo_table2.`n_id`
        //                  left join neo_table3 on neo_table2.`n_id`=neo_table3.`n_id`
        // where neo_table1.`name` =  ? and neo_table2.`name` =  ?
        // order by neo_table1.`group` asc, neo_table2.`name` desc
        TableMap tableMap = neo.exeOne(JoinSqlBuilder.build(columns, joinner, searchMap), JoinSqlBuilder.buildValueList(searchMap).toArray());
        JoinEntity2 entity2 = tableMap.as(JoinEntity2.class);
        // JoinEntity2(name=group, id=29)
        show(entity2);
    }
}
