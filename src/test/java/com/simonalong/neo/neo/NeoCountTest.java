package com.simonalong.neo.neo;

import com.simonalong.neo.NeoBaseTest;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.entity.DemoEntity;

import lombok.SneakyThrows;
import org.junit.*;

/**
 * @author zhouzhenyong
 * @since 2019/3/14 下午6:34
 */
public class NeoCountTest extends NeoBaseTest {

    public NeoCountTest() {}

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
     * 查询表的总数
     */
    @Test
    @SneakyThrows
    public void testCount1() {
        NeoMap dataMap = NeoMap.of("group", "group_count", "name", "name_count");
        neo.insert(TABLE_NAME, dataMap);
        Assert.assertEquals(1, (int) neo.exeCount("select count(1) from %s where `group`=?", TABLE_NAME, "group_count"));
    }

    /**
     * 查询表的总数
     */
    @Test
    @SneakyThrows
    public void testCount2() {
        NeoMap dataMap = NeoMap.of("group", "group_count", "name", "name_count");
        neo.insert("neo_table1", dataMap);
        Assert.assertEquals(1, (int) neo.exeCount("select count(1) from neo_table1 where `group`=?", "group_count"));
    }

    /**
     * 查询个数count
     * 条件通过NeoMap设置
     */
    @Test
    @SneakyThrows
    public void testCount3() {
        NeoMap dataMap = NeoMap.of("group", "group_count", "name", "name_count");
        neo.insert("neo_table1", dataMap);
        Assert.assertEquals(1, (int) neo.count(TABLE_NAME));
    }

    /**
     * 查询个数count
     * 条件通过NeoMap设置
     */
    @Test
    @SneakyThrows
    public void testCount4() {
        DemoEntity search = new DemoEntity();
        search.setGroup("group_count");
        search.setName("name_count");

        neo.insert("neo_table1", search);
        Assert.assertEquals(1, (int) neo.count(TABLE_NAME, search));
    }

    /**
     * 查询个数count
     * 条件通过NeoMap设置
     */
    @Test
    @SneakyThrows
    public void testCount5() {
        NeoMap dataMap = NeoMap.of("group", "group_count", "name", "name_count");
        neo.insert("neo_table1", dataMap);

        Assert.assertEquals(1, (int) neo.count(TABLE_NAME, dataMap));
    }
}
