package com.simonalong.neo;

import com.simonalong.neo.NeoMap.NamingChg;
import com.simonalong.neo.entity.TestEntity;
import com.simonalong.neo.neo.NeoBaseTest;
import java.sql.SQLException;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author zhouzhenyong
 * @since 2019/3/21 下午10:26
 */
public class ColumnsTest extends NeoBaseTest {

    public ColumnsTest() throws SQLException {}

    @Test
    public void testOf1() {
        // `name`, `name1`
        Assert.assertEquals(Columns.of("`name`", "`name1`"), Columns.of("name", "name1"));
    }

    @Test
    public void testOf2() {
        // `name`
        Assert.assertEquals(Columns.of("`name`"), Columns.of("name", "name"));
    }

    @Test
    public void testOf3() {
        // `name`
        show(Columns.of("neo_table1.`group`", "neo_table1.`user_name`", "neo_table1.`age`", "neo_table1.`id`", "neo_table1.`name`"));
        Assert.assertEquals(Columns.of("`name`"), Columns.of("name", "name"));
    }

    @Test
    public void aliasTest1(){
        // `c1` s c11, `c2`, `c3`
        show(Columns.of("`c1` as c11", "`c2`", "`c3`"));
    }

    @Test
    public void aliasTest2(){
        // `c1` c11, `c2`, `c3`
        show(Columns.of("c1 c11", "`c2`", "`c3`"));
    }

    @Test
    public void aliasTest3(){
        // `c1`  c11, `c3`
        show(Columns.of("c1  c11", "`c1`", "`c3`").buildFields());
    }

    @Test
    public void aliasTest4(){
        // table.`c1`  c11, `c3`
        show(Columns.of("table.c1  c11", "`c1`", "`c3`").buildFields());
    }

    @Test
    public void testFrom1() {
        // `userName`, `group`, `name`, `id`, `dataBaseName`
        show(Columns.from(TestEntity.class));
        Assert.assertEquals(Columns.of("`userName`", "`group`", "`name`", "`id`", "`dataBaseName`"), Columns.from(TestEntity.class));
    }

    @Test
    public void testFrom2() {
        // neo_table1.`group`, neo_table1.`user_name`, neo_table1.`age`, neo_table1.`id`, neo_table1.`name`
        Columns expect = Columns.of("neo_table1.`group`", "neo_table1.`user_name`", "neo_table1.`age`", "neo_table1.`id`", "neo_table1.`name`");
        Assert.assertEquals(expect, Columns.from(neo, "neo_table1"));
    }

    @Test
    public void testFrom3() {
        // `data_base_name`, `group`, `user_name`, `name`, `id`
        show(Columns.from(TestEntity.class, NamingChg.UNDERLINE));
        Columns expect = Columns.of("`user_name`", "`group`", "`name`", "`id`", "`data_base_name`");
        Assert.assertEquals(expect, Columns.from(TestEntity.class, NamingChg.UNDERLINE));
    }

    @Test
    public void appendTest1() {
        // `c`, `a`, `b`
        Assert.assertEquals(Columns.of("`a`", "`b`", "`c`"), Columns.of("c").append(Columns.of("a", "b")));
    }

    @Test
    public void appendTest2() {
        // `c1`, `c2`, `c3`, `a1`, `a2`
        Assert.assertEquals(Columns.of("`c1`", "`c2`", "`c3`", "`a1`", "`a2`"), Columns.of("c1", "c2", "c3").append("a1", "a2"));
    }

    @Test
    public void asTest1() {
        // `c1` as c11, `c2`, `c3`
        Assert.assertEquals(Columns.of("`c1` as c11", "`c2`", "`c3`"), Columns.of("c1 as c11", "c2", "c3"));
    }

    /**
     * as的列的转换
     */
    @Test
    public void asTest2() {
        // `c1` as c11, `c3`
        Assert.assertEquals(Columns.of("`c1` as c11", "`c3`"), Columns.of("c1 as c11", "c1", "c3"));
    }

    /**
     * as的列的转换
     */
    @Test
    public void asTest3() {
        // table1.`c1` s c11, `c3`
        Assert.assertEquals(Columns.of("table1.`c1` as c11", "c3"), Columns.of("table1.c1 as c11", "c3"));
    }

    /**
     * 别名的列的转换
     */
    @Test
    public void asTest4() {
        // table1.`c1` s c11, `c3`
        Assert.assertEquals(Columns.of("table1.`c1` as c11", "c3"), Columns.of("table1.`c1` as c11", "c3"));
    }

    /**
     * 别名列的转换
     */
    @Test
    public void asTest5() {
        // table1.`c1` s c11, `c3`
        Assert.assertEquals(Columns.of("table1.`c1` c11", "c3"), Columns.of("table1.c1 c11", "c3"));
    }

    @Test
    public void tableCsTest1() {
        // tableName.`c2`, tableName.`c3`, tableName.`c1`
        Assert.assertEquals(Columns.of("tableName.`c2`", "tableName.`c3`", "tableName.`c1`"),
            Columns.table("tableName").cs("c1", "c2", "c3"));
    }

    @Test
    public void tableCsTest2() {
        Columns columns = Columns.table("tableName").cs("c1", "c2", "c3").append("a1", "a2");
        // tableName.`c1`, tableName.`c2`, tableName.`c3`, `a1`, `a2`
        Assert.assertEquals(Columns.of("tableName.`c1`", "tableName.`c2`", "tableName.`c3`", "`a1`", "`a2`"), columns);
    }

    /**
     * 多表的数据字段
     */
    @Test
    public void tableCsTest3() {
        String table1 = "table1";
        String table2 = "table2";
        String table3 = "table3";
        Columns columns = Columns.table(table1).cs("a1", "b1")
            .and(table2).cs("a2", "b2")
            .and(table3).cs("a3", "b3");

        // table3.`b3`, table1.`b1`, table1.`c1`, table1.`a1`, table2.`c2`, table2.`b2`, table2.`a2`, table3.`a3`
        show(columns.buildFields());
        Assert.assertEquals(Columns.of("table1.`a1`", "table1.`b1`", "table2.`a2`", "table2.`b2`", "table3.`a3`", "table3.`b3`"), columns);
    }

    /**
     * 针对as xxx的情况，进行合并
     */
    @Test
    public void tableCsTest4() {
        // neo_table1.`group` s g
        Assert.assertEquals(Columns.of("neo_table1.`group` as g"), Columns.table("neo_table1").cs("group as g", "group"));
    }

    /**
     * 针对as xxx的情况，进行合并
     */
    @Test
    public void tableCsTest5() {
        // neo_table1.`group` s g, neo_table1.`name`
        Columns columns = Columns.table("neo_table1").cs("group", "name").cs("group as g");
        Assert.assertEquals(Columns.of("neo_table1.`group` as g", "neo_table1.`name`"), columns);
    }

    /**
     * 针对表的所有列进行展开，包含as xx情况的合并
     */
    @Test
    public void tableCsTest6() {
        // neo_table1.`user_name`, neo_table1.`age`, neo_table1.`group` as g, neo_table1.`id`, neo_table1.`name`
        Columns columns = Columns.table("neo_table1", neo).cs("group as g", "group", "*");
        show(columns);
        Assert.assertEquals(Columns.of("neo_table1.`user_name`", "neo_table1.`age`", "neo_table1.`group` as g",
            "neo_table1.`id`", "neo_table1.`name`"), columns);
    }

    @Test
    public void tableCsTest7() {
        String table1 = "neo_table1";
        String table2 = "neo_table2";
        show(Columns.table(table1).cs("group as group1").and(table2).cs("group as group2"));
    }

    @Test
    public void tableCsTest8() {
        String table1 = "neo_table1";
        String table2 = "neo_table2";
        show(Columns.table(table2, neo).cs("*"));
    }


    @Test
    public void removeTest() {
        Columns columns = Columns.of("c1", "c2");
        columns.remove("c2");
        // `c1`
        Assert.assertEquals(Columns.of("`c1`"), columns);
    }

    /**
     * 获取所有的列名 "*"
     */
    @Test
    public void allColumnTest1() {
        // neo_table1.`group`, neo_table1.`user_name`, neo_table1.`age`, neo_table1.`id`, neo_table1.`name`
        Columns columns = Columns.table("neo_table1", neo).cs("*");

        Assert.assertEquals(Columns.of("neo_table1.`group`", "neo_table1.`user_name`", "neo_table1.`age`", "neo_table1.`id`", "neo_table1.`name`"),
            columns);
    }

    /**
     * 获取所有的列名 "*"，有多余的列，则会覆盖
     */
    @Test
    public void allColumnTest2() {
        // neo_table1.`group`, neo_table1.`user_name`, neo_table1.`age`, neo_table1.`id`, neo_table1.`name`
        Columns columns = Columns.setNeo(neo).setTableName().table("neo_table1", neo).cs("*", "group");
        Assert.assertEquals(Columns.of("neo_table1.`group`", "neo_table1.`user_name`", "neo_table1.`age`", "neo_table1.`id`", "neo_table1.`name`"), columns);
    }

    /**
     * 获取所有的列名 "*"，如果有别名，则以别名为主
     */
    @Test
    public void allColumnTest3() {
        // neo_table1.`user_name`, neo_table1.`age`, neo_table1.`group` as g, neo_table1.`id`, neo_table1.`name`
        Columns columns = Columns.table("neo_table1", neo).cs("*", "group as g");
        Assert.assertEquals(Columns.of("neo_table1.`group` as g", "neo_table1.`user_name`", "neo_table1.`age`", "neo_table1.`id`", "neo_table1.`name`"), columns);
    }

    @Test
    public void containTest() {
        Columns columns = Columns.of("a", "b");
        Assert.assertTrue(columns.contains("a"));
    }
}
