package com.simonalong.neo.express;

import com.simonalong.neo.Columns;
import com.simonalong.neo.NeoBaseTest;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.db.NeoPage;
import org.junit.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.simonalong.neo.express.BaseOperate.BetweenAnd;

/**
 * @author shizi
 * @since 2020/8/31 10:52 上午
 */
public class NeoSearchQueryTest extends NeoBaseTest {

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
     * 复杂表达式的搜索
     * <p>其中有默认的过滤原则
     */
    @Test
    public void oneTest() {
        NeoMap dataMap = NeoMap.of("group", "group_insert_express", "name", "name_insert_express");
        neo.insert(TABLE_NAME, dataMap);

        SearchQuery searchQuery;

        searchQuery = new SearchQuery().and("group", "group_insert_express", "name", "name_insert_express");
        Assert.assertEquals(dataMap, neo.one(TABLE_NAME, searchQuery).assignExcept("id"));
    }

    @Test
    public void listTest() {
        neo.insert(TABLE_NAME, NeoMap.of("group", "group_insert_express", "name", "name_insert_express", "age", 1));
        neo.insert(TABLE_NAME, NeoMap.of("group", "group_insert_express", "name", "name_insert_express", "age", 2));
        neo.insert(TABLE_NAME, NeoMap.of("group", "group_insert_express", "name", "name_insert_express", "age", 3));
        neo.insert(TABLE_NAME, NeoMap.of("group", "group_insert_express", "name", "name_insert_express", "age", 4));
        neo.insert(TABLE_NAME, NeoMap.of("group", "group_insert_express", "name", "name_insert_express", "age", 5));

        SearchQuery searchQuery;

        List<NeoMap> dataList = new ArrayList<>();
        dataList.add(NeoMap.of("group", "group_insert_express", "name", "name_insert_express", "age", 1));
        dataList.add(NeoMap.of("group", "group_insert_express", "name", "name_insert_express", "age", 2));
        dataList.add(NeoMap.of("group", "group_insert_express", "name", "name_insert_express", "age", 3));
        dataList.add(NeoMap.of("group", "group_insert_express", "name", "name_insert_express", "age", 4));
        dataList.add(NeoMap.of("group", "group_insert_express", "name", "name_insert_express", "age", 5));

        searchQuery = new SearchQuery().and("group", "group_insert_express", "name", "name_insert_express");
        Assert.assertEquals(dataList, neo.list(TABLE_NAME, searchQuery).stream().map(e->e.assignExcept("id")).collect(Collectors.toList()));

        // 精确测试
        dataList = new ArrayList<>();
        dataList.add(NeoMap.of("group", "group_insert_express", "name", "name_insert_express", "age", 1));

        searchQuery = new SearchQuery().and("group", "group_insert_express", "name", "name_insert_express", "age", 1);
        Assert.assertEquals(dataList, neo.list(TABLE_NAME, searchQuery).stream().map(e->e.assignExcept("id")).collect(Collectors.toList()));
    }

    @Test
    public void valueTest() {
        neo.insert(TABLE_NAME, NeoMap.of("group", "group_insert_express", "name", "name_insert_express_1", "age", 1));
        neo.insert(TABLE_NAME, NeoMap.of("group", "group_insert_express", "name", "name_insert_express_2", "age", 2));

        SearchQuery searchQuery;

        // select name from neo_table1 where (age=?)    value: 1
        searchQuery = new SearchQuery().and("age", 1);
        Assert.assertEquals("name_insert_express_1", neo.value(TABLE_NAME, "name", searchQuery));

        // select age from neo_table1 where (group=? and name=?)    value: group_insert_express, name_insert_express_2
        searchQuery = new SearchQuery().and("group", "group_insert_express", "name", "name_insert_express_2");
        Assert.assertEquals("2", neo.value(TABLE_NAME, "age", searchQuery));
    }

    @Test
    public void valuesTest() {
        neo.insert(TABLE_NAME, NeoMap.of("group", "group_insert_express", "name", "name_insert_express", "age", 1));
        neo.insert(TABLE_NAME, NeoMap.of("group", "group_insert_express", "name", "name_insert_express", "age", 2));
        neo.insert(TABLE_NAME, NeoMap.of("group", "group_insert_express", "name", "name_insert_express", "age", 3));
        neo.insert(TABLE_NAME, NeoMap.of("group", "group_insert_express", "name", "name_insert_express", "age", 4));

        SearchQuery searchQuery;

        List<Integer> dataList = new ArrayList<>();
        dataList.add(1);
        dataList.add(2);
        dataList.add(3);
        dataList.add(4);

        // select age from neo_table1 where (group=? and age=?)    value: group_insert_express, name_insert_express
        searchQuery = new SearchQuery().and("group", "group_insert_express", "name", "name_insert_express");
        Assert.assertEquals(dataList, neo.values(Integer.class, TABLE_NAME, "age", searchQuery));
    }

    @Test
    public void pageTest() {
        neo.insert(TABLE_NAME, NeoMap.of("group", "group_insert_express", "name", "name_insert_express", "age", 1));
        neo.insert(TABLE_NAME, NeoMap.of("group", "group_insert_express", "name", "name_insert_express", "age", 2));
        neo.insert(TABLE_NAME, NeoMap.of("group", "group_insert_express", "name", "name_insert_express", "age", 3));
        neo.insert(TABLE_NAME, NeoMap.of("group", "group_insert_express", "name", "name_insert_express", "age", 4));

        List<NeoMap> dataList = new ArrayList<>();
        dataList.add(NeoMap.of("group", "group_insert_express", "name", "name_insert_express", "age", 1));
        dataList.add(NeoMap.of("group", "group_insert_express", "name", "name_insert_express", "age", 2));

        SearchQuery searchQuery = new SearchQuery().and("group", "group_insert_express");
        List<NeoMap> pageList = neo.page(TABLE_NAME, searchQuery, NeoPage.of(1, 2)).stream().map(e->e.assignExcept("id")).collect(Collectors.toList());

        Assert.assertEquals(dataList, pageList);
    }

    @Test
    public void getPageTest() {
        neo.insert(TABLE_NAME, NeoMap.of("group", "group_insert_express", "name", "name_insert_express", "age", 1));
        neo.insert(TABLE_NAME, NeoMap.of("group", "group_insert_express", "name", "name_insert_express", "age", 2));
        neo.insert(TABLE_NAME, NeoMap.of("group", "group_insert_express", "name", "name_insert_express", "age", 3));
        neo.insert(TABLE_NAME, NeoMap.of("group", "group_insert_express", "name", "name_insert_express", "age", 4));

        SearchQuery searchQuery;

        List<NeoMap> dataList = new ArrayList<>();
        dataList.add(NeoMap.of("age", 1));
        dataList.add(NeoMap.of("age", 2));
        dataList.add(NeoMap.of("age", 3));
        dataList.add(NeoMap.of("age", 4));

        searchQuery = new SearchQuery().and("group", "group_insert_express");
        Assert.assertEquals(dataList, neo.page(TABLE_NAME, Columns.of("age"), searchQuery, NeoPage.of(1, 10)));
    }

    @Test
    public void countTest() {
        neo.insert(TABLE_NAME, NeoMap.of("group", "group_insert_express", "name", "name_insert_express", "age", 1));
        neo.insert(TABLE_NAME, NeoMap.of("group", "group_insert_express", "name", "name_insert_express", "age", 2));
        neo.insert(TABLE_NAME, NeoMap.of("group", "group_insert_express", "name", "name_insert_express", "age", 3));
        neo.insert(TABLE_NAME, NeoMap.of("group", "group_insert_express", "name", "name_insert_express", "age", 4));

        SearchQuery searchQuery = new SearchQuery().and("group", "group_insert_express");
        Assert.assertEquals(Integer.valueOf(4), neo.count(TABLE_NAME, searchQuery));
    }

    @Test
    public void existsTest() {
        neo.insert(TABLE_NAME, NeoMap.of("group", "group_insert_express", "name", "name_insert_express", "age", 1));
        neo.insert(TABLE_NAME, NeoMap.of("group", "group_insert_express", "name", "name_insert_express", "age", 2));
        neo.insert(TABLE_NAME, NeoMap.of("group", "group_insert_express", "name", "name_insert_express", "age", 3));
        neo.insert(TABLE_NAME, NeoMap.of("group", "group_insert_express", "name", "name_insert_express", "age", 4));

        Assert.assertEquals(Integer.valueOf(4), neo.count(TABLE_NAME, new SearchQuery().append(BetweenAnd("age", 1, 4))));
        Assert.assertEquals(Integer.valueOf(3), neo.count(TABLE_NAME, new SearchQuery().append(BetweenAnd("age", 2, 4))));
        Assert.assertEquals(Integer.valueOf(2), neo.count(TABLE_NAME, new SearchQuery().append(BetweenAnd("age", 3, 4))));
        Assert.assertEquals(Integer.valueOf(1), neo.count(TABLE_NAME, new SearchQuery().append(BetweenAnd("age", 4, 4))));
    }

}
