package com.simonalong.neo.db;

import com.simonalong.neo.Columns;
import com.simonalong.neo.NeoMap;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.*;

/**
 * @author zhouzhenyong
 * @since 2019/3/17 下午9:11
 */
public class NeoTableTest extends BaseNeoTableTest {

    static NeoTable table = neo.asTable("neo_table2");

    public NeoTableTest()  {}

    @Before
    public void before() {
        table.truncate();
    }

    @AfterClass
    public static void afterClass() {
        table.truncate();
    }

    @Test
    public void testInsert() {
        table.insert(NeoMap.of("group", "insert_g", "name", "insert_name"));

        String value = table.value("name", NeoMap.of("group", "insert_g"));
        Assert.assertEquals(value, "insert_name");
    }

    @Test
    public void testDelete() {
        table.insert(NeoMap.of("group", "delete_g"));
        Assert.assertEquals(Integer.valueOf(1), table.count(NeoMap.of("group", "delete_g")));

        table.delete(NeoMap.of("group", "delete_g"));
        Assert.assertEquals(Integer.valueOf(0), table.count(NeoMap.of("group", "delete_g")));
    }

    @Test
    public void testUpdate() {
        table.insert(NeoMap.of("group", "update_g", "name", "name_update"));
        Assert.assertEquals("name_update", table.value("name", NeoMap.of("group", "update_g")));

        table.update(NeoMap.of("name", "name_update_change"), NeoMap.of("group", "update_g", "name", "name_update"));
        Assert.assertEquals("name_update_change", table.value("name", NeoMap.of("group", "update_g")));
    }

    @Test
    public void testOne() {
        table.insert(NeoMap.of("group", "insert_g", "name", "insert_name"));
        NeoMap tableInfo = table.one(NeoMap.of("group", "insert_g", "name", "insert_name"));
        NeoMap expectInfo = NeoMap.of("group", "insert_g", "name", "insert_name");

        Assert.assertEquals(expectInfo, tableInfo.assignExcept("id"));
    }

    @Test
    public void list1() {
        NeoMap dataMap = NeoMap.of("group", "group_insert", "name", "name_insert");
        table.insert(dataMap);
        table.insert(dataMap);

        List<NeoMap> dataList = new ArrayList<>();
        dataList.add(dataMap);
        dataList.add(dataMap);

        List<NeoMap> activeList = table.list(dataMap).stream().map(e -> e.assignExcept("id")).collect(Collectors.toList());
        Assert.assertEquals(dataList, activeList);
    }

    @Test
    public void list2() {
        NeoMap dataMap = NeoMap.of("group", "group_insert", "name", "name_insert");
        table.insert(dataMap);
        table.insert(dataMap);

        List<NeoMap> dataList = new ArrayList<>();
        dataList.add(NeoMap.of("group", "group_insert"));
        dataList.add(NeoMap.of("group", "group_insert"));

        Assert.assertEquals(dataList, table.list(Columns.of("group"), NeoMap.of("group", "group_insert")));
    }

    @Test
    public void value() {
        table.insert(NeoMap.of("group", "group_insert", "name", "name_insert"));

        Assert.assertEquals("group_insert", table.value("group", NeoMap.of("group", "group_insert", "name", "name_insert")));
    }

    @Test
    public void value1() {
        table.insert(NeoMap.of("group", "group_insert", "age", 12));
        Assert.assertEquals(Integer.valueOf(12), table.value(Integer.class, "age", NeoMap.of("group", "group_insert")));
    }

    @Test
    public void value2() {
        table.insert(NeoMap.of("group", "group_insert", "age", "12"));
        Assert.assertEquals(Integer.valueOf(12), table.value(Integer.class, "age", NeoMap.of("group", "group_insert")));
    }

    @Test
    public void values() {
        table.insert(NeoMap.of("group", "group_insert", "age", "12"));
        table.insert(NeoMap.of("group", "group_insert", "age", "13"));

        List<Integer> dataList = new ArrayList<>();
        dataList.add(12);
        dataList.add(13);

        Assert.assertEquals(dataList, table.values(Integer.class, "age", NeoMap.of("group", "group_insert")));
    }

    @Test
    public void page() {
        table.insert(NeoMap.of("group", "group_insert", "name", "name_insert", "age", 1));
        table.insert(NeoMap.of("group", "group_insert", "name", "name_insert", "age", 2));
        table.insert(NeoMap.of("group", "group_insert", "name", "name_insert", "age", 3));
        table.insert(NeoMap.of("group", "group_insert", "name", "name_insert", "age", 4));
        table.insert(NeoMap.of("group", "group_insert", "name", "name_insert", "age", 5));

        List<NeoMap> dataList = new ArrayList<>();
        dataList.add(NeoMap.of("group", "group_insert", "name", "name_insert", "age", 1));
        dataList.add(NeoMap.of("group", "group_insert", "name", "name_insert", "age", 2));

        List<NeoMap> pageList = table.page(Columns.of("group", "name", "age"), NeoMap.of("group", "group_insert"), NeoPage.of(1, 2));
        pageList = pageList.stream().map(e->e.assignExcept("id")).collect(Collectors.toList());


    }

    @Test
    public void count() {
        table.insert(NeoMap.of("group", "group_insert", "name", "name_insert", "age", 1));
        table.insert(NeoMap.of("group", "group_insert", "name", "name_insert", "age", 2));
        table.insert(NeoMap.of("group", "group_insert", "name", "name_insert", "age", 3));
        table.insert(NeoMap.of("group", "group_insert", "name", "name_insert", "age", 4));
        table.insert(NeoMap.of("group", "group_insert", "name", "name_insert", "age", 5));

        Assert.assertEquals(Integer.valueOf(5), table.count());
    }

    @Test
    public void count1() {
        table.insert(NeoMap.of("group", "group_insert", "name", "name_insert", "age", 1));
        table.insert(NeoMap.of("group", "group_insert", "name", "name_insert", "age", 2));
        table.insert(NeoMap.of("group", "group_insert", "name", "name_insert", "age", 3));
        table.insert(NeoMap.of("group", "group_insert", "name", "name_insert", "age", 4));
        table.insert(NeoMap.of("group", "group_insert", "name", "name_insert", "age", 5));
        table.insert(NeoMap.of("group", "group_insert", "name", "name_insert", "age", 5));

        Assert.assertEquals(Integer.valueOf(2), table.count(NeoMap.of("age", 5)));
    }

    @Test
    public void getCreateTable() {
        String createSql = "CREATE TABLE `neo_table2` (\n" + "  `id` int unsigned NOT NULL AUTO_INCREMENT,\n" + "  `group` char(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '数据来源组，外键关联lk_config_group',\n" + "  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '任务name',\n" + "  `user_name` varchar(24) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '修改人名字',\n" + "  `age` int DEFAULT NULL,\n" + "  `sort` int DEFAULT NULL,\n" + "  PRIMARY KEY (`id`),\n" + "  KEY `group_index` (`group`)\n" + ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci";
        Assert.assertEquals(createSql, table.getTableCreate());
    }
}
