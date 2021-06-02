package com.simonalong.neo.neo;

import com.simonalong.neo.NeoBaseTest;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.entity.DemoEntity;
import lombok.SneakyThrows;
import org.junit.*;

import java.util.Arrays;
import java.util.List;

/**
 * value 这里要查询的属性的值，可以跟DB中的字段一致，也可以不一致
 * @author zhouzhenyong
 * @since 2019/3/14 下午6:34
 */
public class NeoValueTest extends NeoBaseTest {

    public NeoValueTest()  {}

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
     * 查询多行数据
     */
    @Test
    @SneakyThrows
    public void testValue1() {
        neo.insert(TABLE_NAME, NeoMap.of("group", "group_value", "name", "name_value"));

        String name = neo.value(TABLE_NAME, "name", NeoMap.of("group", "group_value"));

        Assert.assertEquals("name_value", name);
    }

    /**
     * 查询多行数据
     * 条件通过NeoMap设置
     * 相当于：select `group` from neo_table1 where `group`='ok' order by 'group' limit 1
     * 注意，其中的类型必须为指定的这么几种类型：主要是跟数据库对应的几种类型
     */
    @Test
    @SneakyThrows
    public void testValue2() {
        neo.insert(TABLE_NAME, NeoMap.of("group", "group_value", "age", 12));

        Integer age = neo.value(Integer.class, TABLE_NAME, "age", NeoMap.of("group", "group_value"));

        Assert.assertEquals(12, (int) age);
    }

    /**
     * 查询多行数据
     * 条件通过NeoMap设置
     */
    @Test
    @SneakyThrows
    public void testValue3() {
        neo.insert(TABLE_NAME, new DemoEntity().setGroup("group_value").setName("name_value"));

        DemoEntity search = new DemoEntity();
        search.setGroup("group_value");
        String name = neo.value(TABLE_NAME, "name", search);

        Assert.assertEquals("name_value", name);
    }

    /**
     * 查询多行数据，测试搜索条件
     */
    @Test
    @SneakyThrows
    public void testValue4() {
        neo.insert(TABLE_NAME, NeoMap.of("group", "group_value", "name", "name_value"));

        String name = neo.value(TABLE_NAME, "name", NeoMap.of());

        Assert.assertEquals("name_value", name);
    }
}
