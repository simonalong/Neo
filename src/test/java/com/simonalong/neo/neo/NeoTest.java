package com.simonalong.neo.neo;

import com.simonalong.neo.*;
import com.simonalong.neo.db.NeoPage;
import com.simonalong.neo.entity.DemoEntity;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import org.junit.*;

/**
 * 测试，其中待测试的表结构请见文件 /db/test.sql
 * @author zhouzhenyong
 * @since 2019/3/12 下午12:47
 */
public class NeoTest extends NeoBaseTest {

    private ExecutorService pool = Executors.newCachedThreadPool();

    public NeoTest()  {}

    @BeforeClass
    public static void beforeClass() {
        neo.truncateTable(TABLE_NAME);
        neo.truncateTable("neo_table4");
    }

    @Before
    public void beforeTest() {
        neo.truncateTable(TABLE_NAME);
        neo.truncateTable("neo_table4");
    }

    @AfterClass
    public static void afterClass() {
        neo.truncateTable(TABLE_NAME);
        neo.truncateTable("neo_table4");
    }

    /**
     * 链接测试
     */
    @Test
    public void connectTest() {
        neo.test();
    }

    /******************************插入******************************/
    /**
     * insert neoMap
     * 单个列数据
     */
    @Test
    @SneakyThrows
    public void testInsert1(){
        NeoMap dataMap = NeoMap.of("group", "group_insert", "name", "name_insert");
        neo.insert(TABLE_NAME, dataMap);

        NeoMap resultMap = neo.one(TABLE_NAME, NeoMap.of("group", "group_insert"));
        resultMap.remove("id");

        Assert.assertEquals(dataMap, resultMap);
    }

    /**
     * neoMap和实体转换
     */
    @Test
    @SneakyThrows
    public void testInsert3(){
        DemoEntity demoEntity = new DemoEntity();
        demoEntity.setGroup("group_insert");
        demoEntity.setName("name_insert");

        neo.insert(TABLE_NAME, demoEntity);

        DemoEntity search = new DemoEntity();
        search.setGroup("group_insert");

        DemoEntity result = neo.one(TABLE_NAME, search);
        result.setId(null);

        Assert.assertEquals(demoEntity, result);
    }

    /**
     * 测试异步的数据插入
     */
    @Test
    @SneakyThrows
    public void testInsertAsync1(){
        NeoMap dataMap = NeoMap.of("group", "group_insert", "name", "name_insert");
        CompletableFuture<NeoMap> future = neo.insertAsync(TABLE_NAME, dataMap);
        CountDownLatch latch = new CountDownLatch(1);
        future.thenAccept(r-> latch.countDown());
        latch.await();

        NeoMap resultMap = neo.one(TABLE_NAME, NeoMap.of("group", "group_insert"));
        resultMap.remove("id");

        Assert.assertEquals(dataMap, resultMap);
    }

    /**
     * 测试在有数据时候忽略，无数据时候插入
     */
    @Test
    @SneakyThrows
    public void testInsertOfUnExist() {
        NeoMap dataMap = NeoMap.of("group", "group_insert", "name", "name_insert");
        neo.insertOfUnExist(TABLE_NAME, dataMap);

        NeoMap resultMap = neo.one(TABLE_NAME, NeoMap.of("group", "group_insert"));

        Assert.assertEquals(1L, resultMap.getLong("id").longValue());

        // 再次插入
        resultMap = neo.insertOfUnExist(TABLE_NAME, dataMap, "group", "name");

        Assert.assertNull(resultMap.getLong("id"));
    }

    /******************************删除******************************/
    @Test
    @SneakyThrows
    public void testDelete1() {
        NeoMap dataMap = NeoMap.of("group", "group_insert", "name", "name_insert");
        neo.insert(TABLE_NAME, dataMap);

        neo.delete(TABLE_NAME, dataMap);
        Integer result = neo.count(TABLE_NAME, NeoMap.of("group", "group_insert"));

        Assert.assertEquals(0, (int) result);
    }

    @Test
    @SneakyThrows
    public void testDelete2() {
        DemoEntity demoEntity = new DemoEntity();
        demoEntity.setGroup("group_insert");
        demoEntity.setName("name_insert");

        neo.insert(TABLE_NAME, demoEntity);
        neo.delete(TABLE_NAME, demoEntity);

        DemoEntity search = new DemoEntity();
        search.setGroup("group_insert");

        Integer result = neo.count(TABLE_NAME, search);

        Assert.assertEquals(0, (int) result);
    }

    /******************************修改******************************/
    @Test
    @SneakyThrows
    public void testUpdate1(){
        NeoMap dataMap = NeoMap.of("group", "group_update", "name", "name_update");
        neo.insert(TABLE_NAME, dataMap);

        NeoMap setDataMap = NeoMap.of("name", "name_update_chg");
        NeoMap searchMap = NeoMap.of("group", "group_update");
        neo.update(TABLE_NAME, setDataMap, searchMap);

        NeoMap resultMap = neo.one(TABLE_NAME, searchMap);
        NeoMap expectMap = NeoMap.of("group", "group_update", "name", "name_update_chg");

        Assert.assertEquals(expectMap, resultMap.assignExcept("id"));
    }

    /**
     * 待设置的和搜索的都为neomap
     */
    @Test
    @SneakyThrows
    public void testUpdate2() {
        DemoEntity demoEntity = new DemoEntity();
        demoEntity.setGroup("group_update");
        demoEntity.setName("name_update");
        neo.insert(TABLE_NAME, demoEntity);

        demoEntity.setName("name_update_chg");
        NeoMap searchMap = NeoMap.of("group", "group_update");
        neo.update(TABLE_NAME, demoEntity, searchMap);

        NeoMap resultMap = neo.one(TABLE_NAME, searchMap);
        NeoMap expectMap = NeoMap.of("group", "group_update", "name", "name_update_chg");

        Assert.assertEquals(expectMap, resultMap.assignExcept("id", "sl"));
    }

    /**
     * 待设置的类型为neoMap，搜索条件为columns，其中的变量名为dataMap中的key
     */
    @Test
    @SneakyThrows
    public void testUpdate3() {
        NeoMap dataMap = NeoMap.of("group", "group_update", "name", "name_update");
        neo.insert(TABLE_NAME, dataMap);

        dataMap.put("name", "name_update_chg");
        neo.update(TABLE_NAME, dataMap, Columns.of("group"));

        NeoMap resultMap = neo.one(TABLE_NAME, NeoMap.of("group", "group_update"));
        NeoMap expectMap = NeoMap.of("group", "group_update", "name", "name_update_chg");

        Assert.assertEquals(expectMap, resultMap.assignExcept("id"));
    }

    @Test
    @SneakyThrows
    public void testUpdate4() {
        DemoEntity demoEntity = new DemoEntity();
        demoEntity.setGroup("group_update");
        demoEntity.setName("name_update");
        demoEntity.setUserName("user_name_update");
        neo.insert(TABLE_NAME, demoEntity);

        demoEntity.setGroup("group_update_chg");
        demoEntity.setName("name_update_chg");
        demoEntity.setUserName("user_name_update");
        neo.update(TABLE_NAME, demoEntity, Columns.of("userName"));

        NeoMap resultMap = neo.one(TABLE_NAME, NeoMap.of("user_name", "user_name_update"));
        NeoMap expectMap = NeoMap.of("group", "group_update_chg", "name", "name_update_chg", "user_name", "user_name_update");

        Assert.assertEquals(expectMap, resultMap.assignExcept("id", "sl"));
    }

    /**
     * 搜索类型为主键值
     */
    @Test
    @SneakyThrows
    public void testUpdate5() {
        DemoEntity demoEntity = new DemoEntity();
        demoEntity.setGroup("group_update");
        demoEntity.setName("name_update");
        neo.insert(TABLE_NAME, demoEntity);

        demoEntity.setName("name_update_chg");
        neo.update(TABLE_NAME, demoEntity, 1);

        NeoMap resultMap = neo.one(TABLE_NAME, 1);
        NeoMap expectMap = NeoMap.of("group", "group_update", "name", "name_update_chg");

        Assert.assertEquals(expectMap, resultMap.assignExcept("id", "sl"));
    }

    /**
     * 指定某个列作为查询条件
     * 如果没有搜索条件，则默认按照主键作为搜索条件
     */
    @Test
    @SneakyThrows
    public void testUpdate6() {
        NeoMap dataMap = NeoMap.of("group", "group_update", "name", "name_update");
        dataMap = neo.insert(TABLE_NAME, dataMap);

        dataMap.put("name", "name_update_chg");
        neo.update(TABLE_NAME, dataMap);

        NeoMap resultMap = neo.one(TABLE_NAME, 1);
        NeoMap expectMap = NeoMap.of("group", "group_update", "name", "name_update_chg");

        Assert.assertEquals(expectMap, resultMap.assignExcept("id", "sl"));
    }

    /**
     * 指定某个列作为查询条件
     * 如果没有搜索条件，则默认按照主键作为搜索条件
     */
    @Test
    @SneakyThrows
    public void testUpdate7() {
        DemoEntity demoEntity = new DemoEntity();
        demoEntity.setGroup("group_update");
        demoEntity.setName("name_update");
        demoEntity = neo.insert(TABLE_NAME, demoEntity);

        demoEntity.setName("name_update_chg");
        neo.update(TABLE_NAME, demoEntity);

        NeoMap resultMap = neo.one(TABLE_NAME, 1);
        NeoMap expectMap = NeoMap.of("group", "group_update", "name", "name_update_chg");

        Assert.assertEquals(expectMap, resultMap.assignExcept("id", "sl"));
    }

    /**
     * 测试返回值
     */
    @Test
    @SneakyThrows
    public void testUpdate8() {
        NeoMap dataMap = NeoMap.of("group", "group_update", "name", "name_update");
        dataMap = neo.insert(TABLE_NAME, dataMap);

        dataMap.put("name", "name_update_chg");
        NeoMap dataMapAfter = neo.update(TABLE_NAME, dataMap);

        NeoMap expectMap = NeoMap.of("group", "group_update", "name", "name_update_chg", "id", 1);

        Assert.assertEquals(expectMap.toString(), dataMapAfter.toString());
    }

    /**
     * 测试返回值
     */
    @Test
    @SneakyThrows
    public void testUpdate9() {
        DemoEntity demoEntity = new DemoEntity();
        demoEntity.setGroup("group_update");
        demoEntity.setName("name_update");
        demoEntity = neo.insert(TABLE_NAME, demoEntity);

        demoEntity.setName("name_update_chg");
        DemoEntity demoEntity1 = neo.update(TABLE_NAME, demoEntity);

        Assert.assertEquals(demoEntity1, demoEntity.setId(1L).setName("name_update_chg"));
    }

    @Test
    public void testUpdate10() {
        String tableName = "config_center_profile";
        neo.truncateTable(tableName);
        neo.insert(tableName, NeoMap.of("name", "tt"));

        NeoMap searchMap = neo.one(tableName, NeoMap.of());

        NeoMap dataMap = searchMap.clone();
        dataMap.put("name", "tt_chg");
        show("======================");
        show(neo.update(tableName, dataMap, searchMap));
        show("======================");
    }

    /**
     * 带有时间的更新
     * CREATE TABLE `config_center_profile` (
     *   `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
     *   `name` varchar(32) NOT NULL DEFAULT 'default' COMMENT '环境变量名字',
     *   `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     *   `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
     *   PRIMARY KEY (`id`),
     *   UNIQUE KEY `uk_name` (`name`)
     * ) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='环境变量表';
     */
    @Test
    public void testUpdate11() {
        String tableName = "neo_table_time";
        neo.truncateTable(tableName);

        String createTableSql = "CREATE TABLE if not exists `" + tableName + "` (\n" + "  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',\n" + "  `name` varchar(32) NOT NULL DEFAULT 'default' COMMENT '环境变量名字',\n" + "  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',\n" + "  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',\n" + "  PRIMARY KEY (`id`),\n" + "  UNIQUE KEY `uk_name` (`name`)\n" + ") ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='环境变量表'";
        neo.execute(createTableSql);

        neo.truncateTable(tableName);
        neo.insert(tableName, NeoMap.of("name", "name_update_time"));

        NeoMap searchMap = neo.one(tableName, NeoMap.of());

        NeoMap dataMap = searchMap.clone();
        dataMap.put("name", "name_update_time_chg");
        show("======================");
        show(neo.update(tableName, dataMap, searchMap));
        show("======================");
    }

    @Test
    public void testUpdate12() {
        NeoMap dataMap = NeoMap.of("group", "group_update", "name", "name_update");
        neo.insert(TABLE_NAME, dataMap);

        dataMap.put("name", "name_update_chg");
        NeoMap dataMapAfter = neo.update(TABLE_NAME, dataMap, 1);

        NeoMap expectMap = NeoMap.of("group", "group_update", "name", "name_update_chg", "id", 1);

        Assert.assertEquals(expectMap.toString(), dataMapAfter.toString());
    }

    /****************************** 直接执行 ******************************/
    @Test
    public void testExecute1() {
        neo.execute("explain select * from neo_table1 where name ='name'");
    }

    /**
     * 注意，转换符是直接将对应的输入转换到对应的位置
     */
    @Test
    public void testExecute2() {
        NeoMap dataMap = NeoMap.of("group", "group_update", "name", "name_update");
        neo.insert(TABLE_NAME, dataMap);

        List<List<TableMap>> resultList = neo.execute("select `name`, `group` from %s where `id`=?", TABLE_NAME, 1);
        NeoMap resultMap = resultList.get(0).get(0).getNeoMap(TABLE_NAME);

        Assert.assertEquals(dataMap, resultMap);
    }

    /****************************** 直接执行 one ******************************/
    @Test
    public void testExecuteOne1() {
        NeoMap dataMap = NeoMap.of("group", "group_update", "name", "name_update");
        neo.insert(TABLE_NAME, dataMap);

        TableMap tableMap = neo.exeOne("select `name`, `group` from %s where `id`=?", TABLE_NAME, 1);
        NeoMap resultMap = tableMap.getNeoMap(TABLE_NAME);

        Assert.assertEquals(dataMap, resultMap);
    }

    @Test
    public void testExecuteOne2() {
        DemoEntity demoEntity = new DemoEntity();
        demoEntity.setGroup("group_one");
        demoEntity.setName("name_one");
        neo.insert(TABLE_NAME, demoEntity);

        DemoEntity result = neo.exeOne(DemoEntity.class, "select `name`, `group` from %s where `id`=?", TABLE_NAME, 1);

        Assert.assertEquals(demoEntity, result);
    }

    /****************************** 直接执行 list ******************************/
    @Test
    public void testExecuteList1() {
        List<NeoMap> dataList = new ArrayList<>();
        dataList.add(NeoMap.of("group", "group_list", "name", "name_list1"));
        dataList.add(NeoMap.of("group", "group_list", "name", "name_list2"));

        neo.batchInsert(TABLE_NAME, dataList);

        List<NeoMap> resultList = neo.exeList("select `name`, `group` from %s where `group`=?", TABLE_NAME, "group_list")
            .stream().map(e->e.getNeoMap(TABLE_NAME)).map(e->e.assignExcept("id")).collect(Collectors.toList());

        Assert.assertEquals(dataList, resultList);
    }

    @Test
    public void testExecuteList2() {
        List<DemoEntity> dataList = new ArrayList<>();
        dataList.add(new DemoEntity().setGroup("group_list").setName("name_list1"));
        dataList.add(new DemoEntity().setGroup("group_list").setName("name_list2"));

        neo.batchInsertEntity(TABLE_NAME, dataList);

        List<DemoEntity> resultList = neo.exeList(DemoEntity.class, "select `name`, `group` from %s where `group`=?", TABLE_NAME, "group_list")
            .stream().map(e->e.setId(null)).collect(Collectors.toList());

        Assert.assertEquals(dataList, resultList);
    }

    /****************************** 直接执行 value ******************************/
    @Test
    public void testExecuteValue1() {
        NeoMap dataMap = NeoMap.of("group", "group_value", "name", "name_value");
        neo.insert(TABLE_NAME, dataMap);

        String name = neo.exeValue("select `name` from %s where `group`=?", TABLE_NAME, "group_value");

        Assert.assertEquals("name_value", name);
    }

    @Test
    public void testExecuteValue2() {
        DemoEntity demoEntity = new DemoEntity();
        demoEntity.setName("name_value");
        demoEntity.setAge(12);
        neo.insert(TABLE_NAME, demoEntity);

        Integer age = neo.exeValue(Integer.class, "select `age` from %s where `name`=?", TABLE_NAME, "name_value");

        Assert.assertEquals(12, (int) age);
    }

    /****************************** 直接执行 values ******************************/
    @Test
    public void testExecuteValues1() {
        List<NeoMap> dataList = new ArrayList<>();
        dataList.add(NeoMap.of("group", "group_values", "name", "name_values1"));
        dataList.add(NeoMap.of("group", "group_values", "name", "name_values2"));

        neo.batchInsert(TABLE_NAME, dataList);

        List<String> resultList = neo.exeValues("select `name` from %s where `group`=?", TABLE_NAME, "group_values");

        Assert.assertEquals(Arrays.asList("name_values1", "name_values2"), resultList);
    }

    @Test
    public void testExecuteValues2() {
        List<DemoEntity> dataList = new ArrayList<>();
        dataList.add(new DemoEntity().setGroup("group_values").setAge(12));
        dataList.add(new DemoEntity().setGroup("group_values").setAge(22));

        neo.batchInsertEntity(TABLE_NAME, dataList);

        List<Integer> resultList = neo.exeValues(Integer.class, "select `age` from %s where `group`=?", TABLE_NAME, "group_values");

        Assert.assertEquals(Arrays.asList(12, 22), resultList);
    }

    /****************************** 直接执行 list ******************************/
    @Test
    public void testExecutePage1() {
        List<NeoMap> dataList = new ArrayList<>();
        dataList.add(NeoMap.of("group", "group_page", "name", "name_page1"));
        dataList.add(NeoMap.of("group", "group_page", "name", "name_page2"));
        dataList.add(NeoMap.of("group", "group_page", "name", "name_page3"));
        dataList.add(NeoMap.of("group", "group_page", "name", "name_page4"));
        dataList.add(NeoMap.of("group", "group_page", "name", "name_page5"));

        neo.batchInsert(TABLE_NAME, dataList);

        List<NeoMap> resultList = neo.exePage("select `name`, `group` from %s where `group`=?", NeoPage.of(1, 20), TABLE_NAME, "group_page")
            .stream().map(e->e.getNeoMap(TABLE_NAME)).map(e->e.assignExcept("id")).collect(Collectors.toList());

        Assert.assertEquals(dataList, resultList);

        Integer count = neo.exeCount("select count(*) from %s where `group`=?", TABLE_NAME, "group_page");
        Assert.assertEquals(5, (int) count);
    }

    /****************************** 查询 ******************************/
    @Test
    public void getColumnNameListTest() {
        Set<String> columnSet = new HashSet<>();
        columnSet.add("desc1");
        columnSet.add("user_name");
        columnSet.add("name");
        columnSet.add("sl");
        columnSet.add("id");
        columnSet.add("age");
        columnSet.add("group");
        columnSet.add("desc");

        Set<String> resultSet = neo.getColumnNameList(TABLE_NAME);
        Assert.assertEquals(columnSet, resultSet);
    }

    @Test
    public void getIndexNameListTest() {
        List<String> keyList = new ArrayList<>();
        keyList.add("fk_desc");
        keyList.add("k_group");
        keyList.add("PRIMARY");
        keyList.add("group_index");

        List<String> resultList = neo.getIndexNameList(TABLE_NAME);

        Assert.assertEquals(keyList, resultList);
    }

    /****************************** 表的创建语句 ******************************/
    //@Test
    public void getTableCreateTest(){
//        mysql version: 5.6
//        CREATE TABLE `xx_test5` (
//          `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
//          `group` char(64) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '数据来源组，外键关联lk_config_group',
//          `name` varchar(64) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '任务name',
//          `user_name` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '修改人名字',
//          `gander` enum('Y','N') COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '性别：Y=男；N=女',
//          `biginta` bigint(20) NOT NULL,
//          `binarya` binary(1) NOT NULL,
//          `bit2` bit(1) NOT NULL,
//          `blob2` blob NOT NULL,
//          `boolean2` tinyint(1) NOT NULL,
//          `char1` char(1) COLLATE utf8_unicode_ci NOT NULL,
//          `datetime1` datetime NOT NULL,
//          `date2` date NOT NULL,
//          `decimal1` decimal(10,0) NOT NULL,
//          `double1` double NOT NULL,
//          `enum1` enum('a','b') COLLATE utf8_unicode_ci NOT NULL,
//          `float1` float NOT NULL,
//          `geometry` geometry NOT NULL,
//          `int2` int(11) NOT NULL,
//          `linestring` linestring NOT NULL,
//          `longblob` longblob NOT NULL,
//          `longtext` longtext COLLATE utf8_unicode_ci NOT NULL,
//          `medinumblob` mediumblob NOT NULL,
//          `medinumint` mediumint(9) NOT NULL,
//          `mediumtext` mediumtext COLLATE utf8_unicode_ci NOT NULL,
//          `multilinestring` multilinestring NOT NULL,
//          `multipoint` multipoint NOT NULL,
//          `mutipolygon` multipolygon NOT NULL,
//          `point` point NOT NULL,
//          `polygon` polygon NOT NULL,
//          `smallint` smallint(6) NOT NULL,
//          `text` text COLLATE utf8_unicode_ci NOT NULL,
//          `time` time NOT NULL,
//          `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
//          `tinyblob` tinyblob NOT NULL,
//          `tinyint` tinyint(4) NOT NULL,
//          `tinytext` tinytext COLLATE utf8_unicode_ci NOT NULL,
//          `text1` text COLLATE utf8_unicode_ci NOT NULL,
//          `text1123` text COLLATE utf8_unicode_ci NOT NULL,
//          `time1` time NOT NULL,
//          `timestamp1` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
//          `tinyblob1` tinyblob NOT NULL,
//          `tinyint1` tinyint(4) NOT NULL,
//          `tinytext1` tinytext COLLATE utf8_unicode_ci NOT NULL,
//          `year2` year(4) NOT NULL,
//                PRIMARY KEY (`id`)
//        ) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='配置项';
//        show(neo.getTableCreate("xx_test5"));
    }

    @Test
    public void getTableCreateTest2() {
        String createSql = "CREATE TABLE `neo_table1` (\n" + "  `id` int unsigned NOT NULL AUTO_INCREMENT,\n" + "  `group` char(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '数据来源组，外键关联lk_config_group',\n" + "  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '任务name',\n" + "  `user_name` varchar(24) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '修改人名字',\n" + "  `age` int DEFAULT NULL,\n" + "  `sl` bigint DEFAULT NULL,\n" + "  `desc` mediumtext COLLATE utf8_unicode_ci COMMENT '描述',\n" + "  `desc1` text COLLATE utf8_unicode_ci COMMENT '描述',\n" + "  PRIMARY KEY (`id`),\n" + "  KEY `group_index` (`group`),\n" + "  KEY `k_group` (`group`),\n" + "  FULLTEXT KEY `fk_desc` (`desc`)\n" + ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci";

        Assert.assertEquals(createSql, neo.getTableCreate(TABLE_NAME));
    }

    /****************************** save ******************************/
    /**
     * 不存在数据，则插入
     */
    @Test
    public void saveTest1() {
        NeoMap dataMap = NeoMap.of();
        dataMap.put("group", "group_save");
        dataMap.put("name", "name_save");

        neo.save(TABLE_NAME, dataMap);

        NeoMap resultMap = neo.one(TABLE_NAME, 1);
        NeoMap expectMap = NeoMap.of("group", "group_save", "name", "name_save");

        Assert.assertEquals(expectMap, resultMap.assignExcept("id", "sl"));
    }

    /**
     * 不存在数据，则插入
     */
    @Test
    public void saveTest2() {
        NeoMap dataMap = NeoMap.of();
        dataMap.put("group", "group_save");
        dataMap.put("name", "name_save");

        neo.save(TABLE_NAME, dataMap, "group");

        NeoMap resultMap = neo.one(TABLE_NAME, 1);
        NeoMap expectMap = NeoMap.of("group", "group_save", "name", "name_save");

        Assert.assertEquals(expectMap, resultMap.assignExcept("id", "sl"));
    }

    /**
     * 存在数据，则更新
     */
    @Test
    public void saveTest3() {
        NeoMap dataMap = NeoMap.of();
        dataMap.put("group", "group_save");
        dataMap.put("name", "name_save");

        neo.insert(TABLE_NAME, dataMap);

        dataMap.put("name", "name_save_chg");
        neo.save(TABLE_NAME, dataMap, "group");

        NeoMap resultMap = neo.one(TABLE_NAME, 1);
        NeoMap expectMap = NeoMap.of("group", "group_save", "name", "name_save_chg");

        Assert.assertEquals(expectMap, resultMap.assignExcept("id", "sl"));
    }
}
