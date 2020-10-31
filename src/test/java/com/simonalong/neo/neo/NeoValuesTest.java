package com.simonalong.neo.neo;

import com.simonalong.neo.NeoBaseTest;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.entity.DemoEntity;
import lombok.SneakyThrows;
import org.junit.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author zhouzhenyong
 * @since 2019/3/14 下午6:34
 */
public class NeoValuesTest extends NeoBaseTest {

    public NeoValuesTest()  {}

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
    public void testValues1() {
        neo.insert(TABLE_NAME, NeoMap.of("group", "group_update", "name", "name_update1"));
        neo.insert(TABLE_NAME, NeoMap.of("group", "group_update", "name", "name_update2"));

        List<String> nameList = neo.values(TABLE_NAME, "name", NeoMap.of("group", "group_update"));

        Assert.assertEquals(Arrays.asList("name_update1", "name_update2"), nameList);
    }

    /**
     * 查询多行数据
     * 条件通过NeoMap设置
     * 相当于：select `group` from neo_table1 where `group`='ok' order by 'group' limit 1
     * 注意，其中的类型必须为指定的这么几种类型：主要是跟数据库对应的几种类型
     */
    @Test
    @SneakyThrows
    public void testValues2() {
        neo.insert(TABLE_NAME, NeoMap.of("group", "group_update", "age", 12));
        neo.insert(TABLE_NAME, NeoMap.of("group", "group_update", "age", 22));

        List<Integer> ageList = neo.values(Integer.class, TABLE_NAME, "age", NeoMap.of("group", "group_update"));

        Assert.assertEquals(Arrays.asList(12, 22), ageList);
    }

    /**
     * 查询多行数据
     * 条件通过NeoMap设置
     */
    @Test
    @SneakyThrows
    public void testValues3() {
        neo.insert(TABLE_NAME, new DemoEntity().setGroup("group_values").setName("name_values1"));
        neo.insert(TABLE_NAME, new DemoEntity().setGroup("group_values").setName("name_values2"));

        DemoEntity search = new DemoEntity();
        search.setGroup("group_values");
        List<String> nameList = neo.values(TABLE_NAME, "name", search);

        Assert.assertEquals(Arrays.asList("name_values1", "name_values2"), nameList);
    }
}
