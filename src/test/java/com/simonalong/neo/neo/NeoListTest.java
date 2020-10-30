package com.simonalong.neo.neo;

import com.simonalong.neo.Columns;
import com.simonalong.neo.NeoBaseTest;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.entity.DemoEntity;
import com.simonalong.neo.express.BaseOperate;
import com.simonalong.neo.express.Express;
import com.simonalong.neo.sql.builder.SqlBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lombok.SneakyThrows;
import org.junit.*;

import static com.simonalong.neo.express.BaseOperate.OrderBy;

/**
 * @author zhouzhenyong
 * @since 2019/3/14 下午5:56
 */
public class NeoListTest extends NeoBaseTest {

    public NeoListTest()  {}

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
    public void testExeList1(){
        neo.insert(TABLE_NAME, NeoMap.of("group", "group_list", "name", "name_list1"));
        neo.insert(TABLE_NAME, NeoMap.of("group", "group_list", "name", "name_list2"));
        neo.insert(TABLE_NAME, NeoMap.of("group", "group_list", "name", "name_list3"));

        List<NeoMap> dataList = new ArrayList<>();
        dataList.add(NeoMap.of("group", "group_list", "name", "name_list1"));
        dataList.add(NeoMap.of("group", "group_list", "name", "name_list2"));
        dataList.add(NeoMap.of("group", "group_list", "name", "name_list3"));

        List<NeoMap> resultMapList = neo.exeList("select * from %s where `group`=?", TABLE_NAME, "group_list")
            .stream()
            .map(e->e.getNeoMap(TABLE_NAME).assignExcept("id"))
            .collect(Collectors.toList());

        Assert.assertEquals(dataList.toString(), resultMapList.toString());
    }

    /**
     * 查询一行数据
     * 采用直接执行sql方式
     */
    @Test
    @SneakyThrows
    public void testExeList2(){
        neo.insert(TABLE_NAME, NeoMap.of("group", "group_list", "name", "name_list1"));
        neo.insert(TABLE_NAME, NeoMap.of("group", "group_list", "name", "name_list2"));
        neo.insert(TABLE_NAME, NeoMap.of("group", "group_list", "name", "name_list3"));

        List<NeoMap> dataList = new ArrayList<>();
        dataList.add(NeoMap.of("group", "group_list", "name", "name_list1"));
        dataList.add(NeoMap.of("group", "group_list", "name", "name_list2"));
        dataList.add(NeoMap.of("group", "group_list", "name", "name_list3"));

        List<NeoMap> resultMapList = neo.exeList("select * from "+TABLE_NAME+" where `group`=?", "group_list").stream()
            .map(e->e.getNeoMap(TABLE_NAME).assignExcept("id"))
            .collect(Collectors.toList());

        Assert.assertEquals(dataList.toString(), resultMapList.toString());
    }

    /**
     * 查询一行数据
     * 采用直接执行sql方式，设定返回实体类型
     */
    @Test
    @SneakyThrows
    public void testExeList3(){
        List<DemoEntity> dataList = Arrays.asList(
            new DemoEntity().setGroup("group_list1").setName("name_list").setUserName("user1"),
            new DemoEntity().setGroup("group_list2").setName("name_list").setUserName("user2"),
            new DemoEntity().setGroup("group_list3").setName("name_list").setUserName("user3"),
            new DemoEntity().setGroup("group_list4").setName("name_list").setUserName("user4"),
            new DemoEntity().setGroup("group_list5").setName("name_list").setUserName("user5")
        );
        neo.batchInsertEntity(TABLE_NAME, dataList);

        List<DemoEntity> resultList = neo.exeList(DemoEntity.class, "select * from %s where `name`=?", "neo_table1", "name_list")
            .stream()
            .map(e->e.setId(null))
            .collect(Collectors.toList());

        Assert.assertEquals(dataList.toString(), resultList.toString());
    }

    /**
     * 查询一行数据
     * 采用直接执行sql方式，设定返回实体类型
     */
    @Test
    @SneakyThrows
    public void testExeList4() {
        neo.setExplainFlag(true);
        List<DemoEntity> dataList = Arrays.asList(
            new DemoEntity().setGroup("group_list1").setName("name_list").setUserName("user1"),
            new DemoEntity().setGroup("group_list2").setName("name_list").setUserName("user2"),
            new DemoEntity().setGroup("group_list3").setName("name_list").setUserName("user3"),
            new DemoEntity().setGroup("group_list4").setName("name_list").setUserName("user4"),
            new DemoEntity().setGroup("group_list5").setName("name_list").setUserName("user5")
        );
        neo.batchInsertEntity(TABLE_NAME, dataList);

        List<DemoEntity> resultList = neo.exeList(DemoEntity.class, "select * from %s where `name`=?", "neo_table1", "name_list")
            .stream()
            .map(e->e.setId(null))
            .collect(Collectors.toList());

        Assert.assertEquals(dataList.toString(), resultList.toString());
    }

    /**
     * 查询一行数据
     * 条件通过NeoMap设置
     * 相当于：select neo_table1.`group`, neo_table1.`user_name`, neo_table1.`age`, neo_table1.`id`, neo_table1.`name`
     * from neo_table1 where `group` =  ?
     */
    @Test
    @SneakyThrows
    public void testList1(){
        List<NeoMap> dataList = Arrays.asList(
            NeoMap.of("group", "group_list1", "name", "name_list"),
            NeoMap.of("group", "group_list2", "name", "name_list"),
            NeoMap.of("group", "group_list3", "name", "name_list"),
            NeoMap.of("group", "group_list4", "name", "name_list"),
            NeoMap.of("group", "group_list5", "name", "name_list")
        );
        neo.batchInsert(TABLE_NAME, dataList);

        List<NeoMap> resultMapList = neo.list(TABLE_NAME, NeoMap.of("name", "name_list")).stream().map(e->e.assignExcept("id")).collect(Collectors.toList());
        Assert.assertEquals(dataList.toString(), resultMapList.toString());
    }

    /**
     * 查询一行数据
     * 采用复杂条件查询升序查询
     */
    @Test
    @SneakyThrows
    public void testList2() {
        List<NeoMap> dataList = Arrays.asList(
            NeoMap.of("group", "group_list1", "name", "name_list"),
            NeoMap.of("group", "group_list2", "name", "name_list"),
            NeoMap.of("group", "group_list3", "name", "name_list"),
            NeoMap.of("group", "group_list4", "name", "name_list"),
            NeoMap.of("group", "group_list5", "name", "name_list")
        );
        neo.batchInsert(TABLE_NAME, dataList);

        Express searchExpress = new Express();
        searchExpress.andEm("name", "name_list");
        searchExpress.append(OrderBy("group"));

        // 搜索查询
        List<NeoMap> resultMapList = neo.list(TABLE_NAME, searchExpress).stream().map(e->e.assignExcept("id")).collect(Collectors.toList());
        Assert.assertEquals(dataList.toString(), resultMapList.toString());
    }

    /**
     * 查询一行数据
     * 采用复杂条件查询降序查询
     */
    @Test
    @SneakyThrows
    public void testList2_1() {
        List<NeoMap> dataList = Arrays.asList(
            NeoMap.of("group", "group_list1", "name", "name_list"),
            NeoMap.of("group", "group_list2", "name", "name_list"),
            NeoMap.of("group", "group_list3", "name", "name_list"),
            NeoMap.of("group", "group_list4", "name", "name_list"),
            NeoMap.of("group", "group_list5", "name", "name_list")
        );
        neo.batchInsert(TABLE_NAME, dataList);

        Express searchExpress = new Express();
        searchExpress.andEm("name", "name_list");
        searchExpress.append(OrderBy("group", "desc"));

        // 搜索查询
        List<NeoMap> resultMapList = neo.list(TABLE_NAME, searchExpress).stream().map(e->e.assignExcept("id")).collect(Collectors.toList());

        // 比较
        dataList = Arrays.asList(
            NeoMap.of("group", "group_list5", "name", "name_list"),
            NeoMap.of("group", "group_list4", "name", "name_list"),
            NeoMap.of("group", "group_list3", "name", "name_list"),
            NeoMap.of("group", "group_list2", "name", "name_list"),
            NeoMap.of("group", "group_list1", "name", "name_list")
        );
        Assert.assertEquals(dataList.toString(), resultMapList.toString());
    }

    /**
     * 查询一行数据
     * 条件通过NeoMap设置
     */
    @Test
    @SneakyThrows
    public void testList3() {
        List<DemoEntity> dataList = Arrays.asList(
            new DemoEntity().setGroup("group_list1").setName("name_List").setUserName("user1"),
            new DemoEntity().setGroup("group_list2").setName("name_List").setUserName("user2"),
            new DemoEntity().setGroup("group_list3").setName("name_List").setUserName("user3"),
            new DemoEntity().setGroup("group_list4").setName("name_List").setUserName("user4"),
            new DemoEntity().setGroup("group_list5").setName("name_List").setUserName("user5")
        );
        neo.batchInsertEntity(TABLE_NAME, dataList);

        DemoEntity search = new DemoEntity();
        search.setName("name_List");

        // 搜索
        List<DemoEntity> resultList = neo.list(TABLE_NAME, search).stream().map(e->e.setId(null)).collect(Collectors.toList());
        Assert.assertEquals(dataList.toString(), resultList.toString());
    }

    /**
     * 查询一行数据
     * 返回指定的几个列
     * 相当于：select `group`,`name` from neo_table1 where `group` = 'ok' limit 1
     */
    @Test
    @SneakyThrows
    public void testList4(){
        List<NeoMap> dataList = Arrays.asList(
            NeoMap.of("group", "group_list1", "name", "name_list"),
            NeoMap.of("group", "group_list2", "name", "name_list"),
            NeoMap.of("group", "group_list3", "name", "name_list"),
            NeoMap.of("group", "group_list4", "name", "name_list"),
            NeoMap.of("group", "group_list5", "name", "name_list")
        );
        neo.batchInsert(TABLE_NAME, dataList);

        List<NeoMap> resultList = neo.list(TABLE_NAME, Columns.of("group"), NeoMap.of("name", "name_list"));
        List<NeoMap> expectList = Arrays.asList(
            NeoMap.of("group", "group_list1"),
            NeoMap.of("group", "group_list2"),
            NeoMap.of("group", "group_list3"),
            NeoMap.of("group", "group_list4"),
            NeoMap.of("group", "group_list5")
        );

        Assert.assertEquals(expectList.toString(), resultList.toString());
    }
}
