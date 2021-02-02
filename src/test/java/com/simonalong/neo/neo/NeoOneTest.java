package com.simonalong.neo.neo;

import com.simonalong.neo.Columns;
import com.simonalong.neo.NeoBaseTest;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.entity.DemoEntity;

import java.util.Date;

import com.simonalong.neo.neo.entity.OneEntity;
import lombok.SneakyThrows;
import org.junit.*;

/**
 * 测试Neo.one这个函数
 *
 * @author zhouzhenyong
 * @since 2019/3/12 下午12:47
 */
public class NeoOneTest extends NeoBaseTest {

    public NeoOneTest() {}

    @BeforeClass
    public static void beforeClass() {
        neo.truncateTable(TABLE_NAME);
    }

    @Before
    public void beforeTest() {
        neo.truncateTable(TABLE_NAME);
    }

    @AfterClass
    public static void afterClass() {
        neo.truncateTable(TABLE_NAME);
    }

    /**
     * 查询一行数据
     * 采用直接执行sql方式
     */
    @Test
    @SneakyThrows
    public void testExeOne1() {
        NeoMap dataMap = NeoMap.of("group", "group_one", "name", "name_one");
        neo.insert(TABLE_NAME, dataMap);

        NeoMap resultMap = neo.exeOne("select `group`, `name` from " + TABLE_NAME + " where `group`=?", "group_one").getNeoMap(TABLE_NAME);
        Assert.assertEquals(dataMap, resultMap);
    }

    /**
     * 查询一行数据
     * 采用直接执行sql方式，设定返回实体类型
     */
    @Test
    @SneakyThrows
    public void testExeOne2() {
        DemoEntity data = new DemoEntity();
        data.setGroup("group_one");
        data.setName("name_one");
        neo.insert(TABLE_NAME, data);

        DemoEntity result = neo.exeOne(DemoEntity.class, "select * from %s where `group`=?", "neo_table1", "group_one");
        result.setId(null);

        Assert.assertEquals(data, result);
    }

    /**
     * 查询一行数据
     * 条件通过NeoMap设置
     * 相当于：select neo_table1.`group`, neo_table1.`user_name`, neo_table1.`age`, neo_table1.`id`, neo_table1.`name`
     * from neo_table1 where `group` =  ?  limit 1
     */
    @Test
    @SneakyThrows
    public void testOne1() {
        NeoMap dataMap = NeoMap.of("group", "group_one", "name", "name_one");
        neo.insert(TABLE_NAME, dataMap);

        NeoMap resultMap = neo.one(TABLE_NAME, NeoMap.of("group", "group_one"));
        resultMap.remove("id");

        Assert.assertEquals(dataMap, resultMap);
    }

    /**
     * 查询一行数据
     * 条件通过NeoMap设置
     */
    @Test
    @SneakyThrows
    public void testOne2() {
        DemoEntity data = new DemoEntity();
        data.setGroup("group_one");
        data.setName("name_one");
        neo.insert(TABLE_NAME, data);

        DemoEntity result = neo.one(TABLE_NAME, data);
        result.setId(null);

        Assert.assertEquals(data, result);
    }

    /**
     * 查询一行数据
     * 返回指定的几个列
     * 相当于：select `group`,`name` from neo_table1 where `group` = 'ok' limit 1
     */
    @Test
    @SneakyThrows
    public void testOne3() {
        NeoMap dataMap = NeoMap.of("group", "group_one", "name", "name_one");
        neo.insert(TABLE_NAME, dataMap);

        NeoMap resultMap = neo.one(TABLE_NAME, Columns.of("group", "name"), NeoMap.of("group", "group_one"));

        Assert.assertEquals(dataMap, resultMap);
    }

    /**
     * 查询一行数据
     * 条件通过NeoMap设置
     * 相当于：select `group`,`name` from neo_table1 where `group` = 'group1' limit 1
     */
    @Test
    @SneakyThrows
    public void testOne4() {
        DemoEntity data = new DemoEntity();
        data.setGroup("group_one");
        data.setName("name_one");
        neo.insert(TABLE_NAME, data);

        DemoEntity result = neo.one(TABLE_NAME, Columns.of("group", "name"), data);

        Assert.assertEquals(data, result);
    }
}
