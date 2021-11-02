package com.simonalong.neo.neo;

import com.simonalong.neo.Columns;
import com.simonalong.neo.NeoBaseTest;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.TableMap;
import com.simonalong.neo.db.NeoPage;
import com.simonalong.neo.db.PageReq;
import com.simonalong.neo.db.PageRsp;
import com.simonalong.neo.entity.DemoEntity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lombok.SneakyThrows;
import org.junit.*;

/**
 * @author zhouzhenyong
 * @since 2019/3/14 下午2:34
 */
public class NeoPageTest extends NeoBaseTest {

    public NeoPageTest()  {}

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
     */
    @Test
    @SneakyThrows
    public void testPage1(){
        neo.insert(TABLE_NAME, NeoMap.of("group", "group_page", "name", "name_page1"));
        neo.insert(TABLE_NAME, NeoMap.of("group", "group_page", "name", "name_page2"));
        neo.insert(TABLE_NAME, NeoMap.of("group", "group_page", "name", "name_page3"));

        List<NeoMap> dataList = new ArrayList<>();
        dataList.add(NeoMap.of("group", "group_page", "name", "name_page1"));
        dataList.add(NeoMap.of("group", "group_page", "name", "name_page2"));
        dataList.add(NeoMap.of("group", "group_page", "name", "name_page3"));

        // page
        List<NeoMap> resultList = neo.page(TABLE_NAME, NeoMap.of("group", "group_page"), NeoPage.of(1, 20))
            .stream().map(e->e.assignExcept("id"))
            .collect(Collectors.toList());

        Assert.assertEquals(dataList, resultList);


        // getPage
        PageRsp<NeoMap> resultRsp;
        resultRsp = neo.getPage(TABLE_NAME, NeoMap.of("group", "group_page"), NeoPage.of(1, 20));

        Assert.assertEquals(3, (int) resultRsp.getTotalNum());
        Assert.assertEquals(dataList, resultRsp.getDataList().stream().map(e->e.assignExcept("id")).collect(Collectors.toList()));
    }

    /**
     * 查询一行数据
     */
    @Test
    @SneakyThrows
    public void testPage5() {
        List<DemoEntity> dataList = Arrays.asList(
            new DemoEntity().setGroup("group_page1").setName("name_page").setUserName("user1"),
            new DemoEntity().setGroup("group_page2").setName("name_page").setUserName("user2"),
            new DemoEntity().setGroup("group_page3").setName("name_page").setUserName("user3"),
            new DemoEntity().setGroup("group_page4").setName("name_page").setUserName("user4"),
            new DemoEntity().setGroup("group_page5").setName("name_page").setUserName("user5")
        );
        neo.batchInsertEntity(TABLE_NAME, dataList);

        DemoEntity search = new DemoEntity();
        search.setName("name_page");

        List<DemoEntity> resultList = neo.page(TABLE_NAME, search, NeoPage.of(1, 20));
        resultList = resultList.stream().map(e -> e.setId(null)).collect(Collectors.toList());

        Assert.assertEquals(dataList, resultList);
    }


    /**
     * 查询一行数据
     */
    @Test
    @SneakyThrows
    public void testPage9(){
        neo.insert(TABLE_NAME, NeoMap.of("group", "group_page", "name", "name_page1"));
        neo.insert(TABLE_NAME, NeoMap.of("group", "group_page", "name", "name_page2"));
        neo.insert(TABLE_NAME, NeoMap.of("group", "group_page", "name", "name_page3"));

        List<NeoMap> dataList = new ArrayList<>();
        dataList.add(NeoMap.of("name", "name_page1"));
        dataList.add(NeoMap.of("name", "name_page2"));
        dataList.add(NeoMap.of("name", "name_page3"));

        List<NeoMap> resultList = neo.page(TABLE_NAME, Columns.of("name"), NeoMap.of("group", "group_page"), NeoPage.of(1, 20));

        Assert.assertEquals(dataList, resultList);
    }

    /**
     * 查询一行数据
     */
    @Test
    @SneakyThrows
    public void testPage15(){
        List<DemoEntity> dataList = Arrays.asList(
            new DemoEntity().setGroup("group_page").setName("name_page1"),
            new DemoEntity().setGroup("group_page").setName("name_page2"),
            new DemoEntity().setGroup("group_page").setName("name_page3")
        );
        neo.batchInsertEntity(TABLE_NAME, dataList);

        DemoEntity search = new DemoEntity();
        search.setGroup("group_page");

        List<DemoEntity> resultList = neo.page(TABLE_NAME, Columns.of("name", "group"), search, NeoPage.of(1, 20));

        Assert.assertEquals(dataList, resultList);
    }

    /**
     * 查询一行数据
     */
    @Test
    @SneakyThrows
    public void testGetPage1(){
        neo.insert(TABLE_NAME, NeoMap.of("group", "group_page", "name", "name_page1"));
        neo.insert(TABLE_NAME, NeoMap.of("group", "group_page", "name", "name_page2"));
        neo.insert(TABLE_NAME, NeoMap.of("group", "group_page", "name", "name_page3"));

        List<DemoEntity> dataList = Arrays.asList(
            new DemoEntity().setGroup("group_page").setName("name_page1").setId(1L),
            new DemoEntity().setGroup("group_page").setName("name_page2").setId(2L),
            new DemoEntity().setGroup("group_page").setName("name_page3").setId(3L)
        );

        PageReq<?> pageReq = new PageReq<>();
        pageReq.setCurrent(1);
        pageReq.setSize(20);
        PageRsp<DemoEntity> pageRsp = neo.getPage(TABLE_NAME, NeoMap.of("group", "group_page"), pageReq).convert(e-> e.as(DemoEntity.class));

        Assert.assertEquals(dataList, pageRsp.getDataList());
    }

    /**
     * 普通的分页处理
     */
    @Test
    public void testExePage1() {
        neo.insert(TABLE_NAME, NeoMap.of("group", "group_page", "name", "name_page1"));
        neo.insert(TABLE_NAME, NeoMap.of("group", "group_page", "name", "name_page2"));
        neo.insert(TABLE_NAME, NeoMap.of("group", "group_page", "name", "name_page3"));

        List<NeoMap> dataList = new ArrayList<>();
        dataList.add(NeoMap.of("name", "name_page1"));
        dataList.add(NeoMap.of("name", "name_page2"));
        dataList.add(NeoMap.of("name", "name_page3"));

        List<NeoMap> resultMapList = neo.exePage("select `name` from %s", 0, 20, TABLE_NAME)
            .stream().map(e->e.getNeoMap(TABLE_NAME))
            .map(e->e.assignExcept("id")).collect(Collectors.toList());

        Assert.assertEquals(dataList, resultMapList);
    }

    /**
     * 指定分页的页面位置
     */
    @Test
    public void testExePage4(){
        neo.insert(TABLE_NAME, NeoMap.of("group", "group_page", "name", "name_page1"));
        neo.insert(TABLE_NAME, NeoMap.of("group", "group_page", "name", "name_page2"));
        neo.insert(TABLE_NAME, NeoMap.of("group", "group_page", "name", "name_page3"));

        List<NeoMap> dataList = new ArrayList<>();
        dataList.add(NeoMap.of("name", "name_page1"));
        dataList.add(NeoMap.of("name", "name_page2"));
        dataList.add(NeoMap.of("name", "name_page3"));

        List<TableMap> pageList = neo.exePage("select `name` from %s where id < ?", NeoPage.of(1, 20), TABLE_NAME, 1000);

        List<NeoMap> resultMapList = pageList.stream().map(e->e.getNeoMap(TABLE_NAME))
            .map(e->e.assignExcept("id")).collect(Collectors.toList());

        Assert.assertEquals(dataList, resultMapList);
    }
}
