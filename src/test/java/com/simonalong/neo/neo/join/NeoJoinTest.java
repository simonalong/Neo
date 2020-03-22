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
 * @author zhouzhenyong
 * @since 2019/4/20 下午7:54
 */
public class NeoJoinTest extends NeoBaseTest {

    public NeoJoinTest() throws SQLException {}

    /**
     * 比较复杂的例子 join
     */
    @Test
    public void joinTest(){
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
//        searchMap.put(table1, "name", "group");
        searchMap.put(table2, "name", "12");
//        searchMap.put(table2, "order by", "name desc");
//        searchMap.put(table1, "order by", "group asc");

        // todo 这里有点问题，在有参数的情况下，? 这个是动态增加的，参数是不够的
        show(neo.exeList(
            "select %s from %s %s order by neo_table1.`id` desc",
            JoinSqlBuilder.buildColumns(columns),
            JoinSqlBuilder.buildJoinOn(joinner),
            JoinSqlBuilder.buildConditionWithWhere(searchMap)
        ));
    }
}
