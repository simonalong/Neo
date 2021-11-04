package com.simonalong.neo.column;

import com.simonalong.neo.Columns;
import com.simonalong.neo.entity.TestEntity;
import com.simonalong.neo.NeoBaseTest;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author zhouzhenyong
 * @since 2019/3/21 下午10:26
 */
public class ColumnsTest extends NeoBaseTest {

    public ColumnsTest() {}

    @Test
    public void testOf0() {
        Assert.assertEquals(Columns.of("name"), Columns.of("name"));
        Assert.assertEquals(Columns.of("`name`"), Columns.of("name"));
    }

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
        Assert.assertEquals(Columns.of("`name`"), Columns.of("name", "name"));
    }

    /**
     * 输出
     */
    @Test
    public void selectStringTest1(){
        String expect = "`c1`, `c2`, `c3`";

        Assert.assertEquals(expect, Columns.of("c1", "`c2`", "`c3`").toSelectString());
    }

//    /**
//     * 输出
//     */
//    @Test
//    public void selectStringTest2() {
//        String expect = "neo_table1.`id` as neo_table1_dom_id, neo_table1.`name` as neo_table1_dom_name, neo_table1.`sl` as neo_table1_dom_sl, neo_table1.`desc` as neo_table1_dom_desc, neo_table1.`group` as neo_table1_dom_group, neo_table1.`desc1` as neo_table1_dom_desc1, neo_table1.`age` as neo_table1_dom_age, neo_table1.`user_name` as neo_table1_dom_user_name";
//        Assert.assertEquals(expect, Columns.of().setNeo(neo).table(TABLE_NAME).toSelectString());
//    }

    /**
     * 输出
     */
    @Test
    public void distinctTest1(){
        String expect = "distinct `c1`, `c2`, `c3`";

        Assert.assertEquals(expect, Columns.of("c1", "`c2`", "`c3`").distinct().toSelectString());
    }

//    /**
//     * 输出
//     */
//    @Test
//    public void distinctTest2() {
//        String expect = "distinct neo_table1.`id` as neo_table1_dom_id, neo_table1.`name` as neo_table1_dom_name, neo_table1.`sl` as neo_table1_dom_sl, neo_table1.`desc` as neo_table1_dom_desc, neo_table1.`group` as neo_table1_dom_group, neo_table1.`desc1` as neo_table1_dom_desc1, neo_table1.`age` as neo_table1_dom_age, neo_table1.`user_name` as neo_table1_dom_user_name";
//        Assert.assertEquals(expect, Columns.of().setNeo(neo).table(TABLE_NAME).distinct().toSelectString());
//    }

    @Test
    public void testFrom1() {
        // `data_base_name`, `group`, `user_name`, `name`, `id`
        Assert.assertEquals(Columns.of("`data_base_name`", "`group`", "`user_name`", "`name`", "`id`"), Columns.from(TestEntity.class));
    }

    /**
     * 测试剔除某几个属性的功能
     */
    @Test
    public void testFrom4() {
        // `data_base_name`, `group`, `neo_user_name`, `name`, `id`
        Columns expect = Columns.of("`neo_user_name`", "`group`", "`name`", "`id`", "`data_base_name`");
        Assert.assertEquals(expect, Columns.from(ColumnEntity.class));

        Columns expect1 = Columns.of("`neo_user_name`", "`name`", "`id`", "`data_base_name`");
        Assert.assertEquals(expect1, Columns.from(ColumnEntity.class, "group"));

        Columns expect2 = Columns.of("`neo_user_name`", "`name`", "`data_base_name`");
        Assert.assertEquals(expect2, Columns.from(ColumnEntity.class, "group", "id"));
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
    public void appendTest3() {
        Columns aColumns = Columns.of("`c1`");
        Columns bColumns = Columns.of("`c2`");
        Columns cColumns = Columns.of("`c1`", "`c2`");
        // `c1`, `c2`
        Assert.assertEquals(aColumns.append(bColumns), cColumns);
    }

    @Test
    public void appendTest4() {
        Columns aColumns = Columns.of("`c1`");
        Columns bColumns = Columns.of("`c2`", "`c1`");
        Columns cColumns = Columns.of("`c1`", "`c2`");
        // `c1`, `c2`
        Assert.assertEquals(aColumns.append(bColumns), cColumns);
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
//    @Test
//    public void allColumnTest1() {
//        // neo_table1.`group`, neo_table1.`user_name`, neo_table1.`age`, neo_table1.`id`, neo_table1.`name`
//        Columns columns = Columns.of().setNeo(neo).table("neo_table1", "*");
//        Columns expect = Columns.of().setNeo(neo).table("neo_table1", "id", "name", "sl", "desc", "group", "desc1", "age", "user_name");
//        Assert.assertEquals(expect.toSelectString(), columns.toSelectString());
//    }

    /**
     * 获取所有的列名 "*"，有多余的列，则会覆盖
     */
//    @Test
//    public void allColumnTest2() {
//        // neo_table1.`group`, neo_table1.`user_name`, neo_table1.`age`, neo_table1.`id`, neo_table1.`name`
//        Columns columns = Columns.of().setNeo(neo).table("neo_table1", "*", "group");
//        Columns expect = Columns.of().setNeo(neo).table("neo_table1","id", "name", "sl", "desc", "group", "desc1", "age", "user_name");
//        Assert.assertEquals(expect, columns);
//    }

    /**
     * 拼接
     */
    @Test
    public void allColumnTest4() {
        Columns aColumn = Columns.of().setNeo(neo).table("neo_table1", "*");
        Columns bColumn = Columns.of().setNeo(neo).table("neo_table2", "*");
        Columns totalColumn = Columns.of().setNeo(neo).table("neo_table1", "*").table("neo_table2", "*");
        Assert.assertEquals(aColumn.append(bColumn), totalColumn);
    }

    @Test
    public void containTest() {
        Columns columns = Columns.of("a", "b");
        Assert.assertTrue(columns.contains("a"));
    }

    @Test
    public void isEmptyTest1() {
        Columns columns = Columns.of("a", "b");
        Assert.assertFalse(Columns.isEmpty(columns));
    }

    @Test
    public void isEmptyTest2() {
        List<Columns> columnList = Arrays.asList(Columns.of("a", "b"), Columns.of("c"));
        Assert.assertFalse(Columns.isEmpty(columnList));
    }

    @Test
    public void getFieldSetsTest() {
        Columns columns = Columns.of("a", "b");
        show(columns.getFieldSets());
    }
}
