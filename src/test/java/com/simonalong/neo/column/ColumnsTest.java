package com.simonalong.neo.column;

import com.simonalong.neo.Columns;
import com.simonalong.neo.entity.TestEntity;
import com.simonalong.neo.NeoBaseTest;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
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
        // db.`c1`  c11, `c3`
        show(Columns.of("db.c1  c11", "`c1`", "`c3`").buildFields());
    }

    @Test
    public void testFrom1() {
        // `data_base_name`, `group`, `user_name`, `name`, `id`
        show(Columns.from(TestEntity.class));
        Assert.assertEquals(Columns.of("`data_base_name`", "`group`", "`user_name`", "`name`", "`id`"), Columns.from(TestEntity.class));
    }

    @Test
    public void testFrom2() {
        // neo_table1.`group`, neo_table1.`user_name`, neo_table1.`age`, neo_table1.`id`, neo_table1.`name`
        Columns expect = Columns.of("neo_table1.`group`", "neo_table1.`user_name`", "neo_table1.`age`", "neo_table1.`sl`", "neo_table1.`id`", "neo_table1.`name`");
        Assert.assertEquals(expect, Columns.of(neo).table("neo_table1"));
    }

    @Test
    public void testFrom3() {
        // `data_base_name`, `group`, `neo_user_name`, `name`, `id`
        show(Columns.from(ColumnEntity.class));
        Columns expect = Columns.of("`neo_user_name`", "`group`", "`name`", "`id`", "`data_base_name`");
        Assert.assertEquals(expect, Columns.from(ColumnEntity.class));
    }

    /**
     * 测试剔除某几个属性的功能
     */
    @Test
    public void testFrom4() {
        // `data_base_name`, `group`, `neo_user_name`, `name`, `id`
        show(Columns.from(ColumnEntity.class, null));
        Columns expect = Columns.of("`neo_user_name`", "`group`", "`name`", "`id`", "`data_base_name`");
        Assert.assertEquals(expect, Columns.from(ColumnEntity.class));

        show(Columns.from(ColumnEntity.class, "group"));
        Columns expect1 = Columns.of("`neo_user_name`", "`name`", "`id`", "`data_base_name`");
        Assert.assertEquals(expect1, Columns.from(ColumnEntity.class, "group"));

        show(Columns.from(ColumnEntity.class, "group", "id"));
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
        show(aColumns);
        show(bColumns);
        show(cColumns);
        // `c1`, `c2`
        Assert.assertEquals(aColumns.append(bColumns), cColumns);
    }

    @Test
    public void appendTest4() {
        Columns aColumns = Columns.of("`c1`");
        Columns bColumns = Columns.of("`c2`", "`c1`");
        Columns cColumns = Columns.of("`c1`", "`c2`");
        show(aColumns);
        show(bColumns);
        show(cColumns);
        // `c1`, `c2`
        Assert.assertEquals(aColumns.append(bColumns), cColumns);
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
        Columns columns = Columns.of(neo).table("neo_table1", "*");

        Assert.assertEquals(Columns.of("neo_table1.`group`", "neo_table1.`user_name`", "neo_table1.`age`", "neo_table1.`sl`", "neo_table1.`id`", "neo_table1.`name`"),
            columns);
    }

    /**
     * 获取所有的列名 "*"，有多余的列，则会覆盖
     */
    @Test
    public void allColumnTest2() {
        // neo_table1.`group`, neo_table1.`user_name`, neo_table1.`age`, neo_table1.`id`, neo_table1.`name`
        Columns columns = Columns.of(neo).table("neo_table1", "*", "group");
        Assert.assertEquals(Columns.of("neo_table1.`group`", "neo_table1.`user_name`", "neo_table1.`age`", "neo_table1.`sl`", "neo_table1.`id`", "neo_table1.`name`"), columns);
    }

    /**
     * 获取所有的列名 "*"，如果有别名，则以别名为主
     */
    @Test
    public void allColumnTest3() {
        // neo_table1.`user_name`, neo_table1.`age`, neo_table1.`group` as g, neo_table1.`id`, neo_table1.`name`
        Columns columns = Columns.of(neo).table("neo_table1", "*", "group as g");
        Assert.assertEquals(Columns.of("neo_table1.`group` as g", "neo_table1.`user_name`", "neo_table1.`age`", "neo_table1.`sl`", "neo_table1.`id`", "neo_table1.`name`"), columns);
    }

    /**
     * 拼接
     */
    @Test
    public void allColumnTest4() {
        Columns aColumn = Columns.of(neo).table("neo_table1", "*");
        show(aColumn);
        Columns bColumn = Columns.of(neo).table("neo_table2", "*");
        show(bColumn);
        Columns totalColumn = Columns.of(neo).table("neo_table1", "*").table("neo_table2", "*");
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
