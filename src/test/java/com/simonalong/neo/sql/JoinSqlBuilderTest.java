package com.simonalong.neo.sql;

import com.simonalong.neo.Columns;
import com.simonalong.neo.NeoBaseTest;
import com.simonalong.neo.TableMap;
import com.simonalong.neo.db.TableJoinOn;
import com.simonalong.neo.sql.builder.JoinSqlBuilder;
import org.junit.Test;

import java.sql.SQLException;

/**
 * @author shizi
 * @since 2020/3/22 下午9:16
 */
public class JoinSqlBuilderTest extends NeoBaseTest {

    public JoinSqlBuilderTest()  {
    }

    @Test
    public void buildColumnsTest1() {
        // select `group`, `name`
        show(JoinSqlBuilder.buildColumns(Columns.of("name", "group")));
    }

    /**
     * 测试buildColumns：注意，在有多表的情况下，请设置Neo（即数据库对象）
     */
    @Test
    public void buildColumnsTest2() {
        // select `group`, `name`
        Columns columns = Columns.of().setNeo(neo);
        columns.table("table1", "name", "group");
        columns.table("table2", "id", "user_name");
        // table2.`id` as table2_NDom_id, table1.`name` as table1_NDom_name, table2.`user_name` as table2_NDom_user_name, table1.`group` as table1_NDom_group
        show(JoinSqlBuilder.buildColumns(columns));
    }

    @Test
    public void testJoin() {
        String table1 = "table1";
        String table2 = "table2";
        String table3 = "table3";
        String table4 = "table4";
        TableJoinOn joiner = new TableJoinOn(table1);
        joiner.leftJoin(table1, table2).on("a_id", "id");
        joiner.leftJoin(table1, table3).on("c_id", "id");
        joiner.rightJoin(table2, table4).on("d_id", "id");

        //  from table1 left join table2 on table1.`a_id`=table2.`id`
        //              left join table3 on table1.`c_id`=table3.`id`
        //              right join table4 on table2.`d_id`=table4.`id`
        show(joiner.getJoinSql());
    }

    @Test
    public void buildConditionMetaTest1() {
        TableMap tableMap = new TableMap();
        tableMap.put("table1", "ke1", "valeu");
        tableMap.put("table2", "name", 12);
        show(JoinSqlBuilder.buildConditionMeta(tableMap));
    }

    @Test
    public void buildConditionMetaTest2() {
        TableMap tableMap = new TableMap();
        tableMap.put("table1", "ke1", "like value#");
        tableMap.put("table2", "name", "> 12");
        // [table1.`ke1` like 'value%', table2.`name` > ?]
        show(JoinSqlBuilder.buildConditionMeta(tableMap));
    }

    @Test
    public void buildTest(){
        String table1 = "neo_table1";
        String table2 = "neo_table2";
        String table3 = "neo_table3";
        String table4 = "neo_table4";

        // 生成多个表的展示字段
        Columns columns = Columns.of().setNeo(neo);
        columns.table(table1, "id", "name");
        columns.table(table2, "id");

        // 多表的join关系
        TableJoinOn joinner = new TableJoinOn(table1);
        joinner.leftJoin(table1, table2).on("id", "id");
        joinner.leftJoin(table2, table3).on("id", "id");
        joinner.rightJoin(table2, table4).on("id", "id");

        // 多表的搜索条件
        TableMap searchMap = TableMap.of();
        searchMap.put(table1, "name", "group");
        searchMap.put(table2, "age", 12);
        searchMap.put(table2, "order by", "name desc");
        searchMap.put(table1, "order by", "group asc");

        // select neo_table1.`name` as neo_table1_NDom_name, neo_table1.`id` as neo_table1_NDom_id, neo_table2.`id` as neo_table2_NDom_id
        // from neo_table1  left join neo_table2 on neo_table1.`id`=neo_table2.`id`
        //                  left join neo_table3 on neo_table2.`id`=neo_table3.`id`
        //                  right join neo_table4 on neo_table2.`id`=neo_table4.`id`
        // where  where neo_table1.`name` =  ? and neo_table2.`age` =  ?
        // order by neo_table1.`group` asc, neo_table2.`name` desc
        show(JoinSqlBuilder.build(columns, joinner, searchMap));
    }
}
