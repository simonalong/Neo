package com.simonalong.neo.tenant;

import com.simonalong.neo.*;
import com.simonalong.neo.db.NeoPage;
import com.simonalong.neo.db.PageRsp;
import com.simonalong.neo.express.SearchQuery;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author shizi
 * @since 2021-05-06 10:48:24
 */
public class TenantTest extends NeoBaseTest {

    String tableName = "isc_neo_tenant_test";

    @Before
    public void before() {
        String createTableSql = "create table if not exists `isc_neo_tenant_test`(" +
            "`id` bigint unsigned not null auto_increment, " +
            "`group` varchar(12) default '' comment '测试', " +
            "`name` varchar(12) default '' comment '测试', " +
            "`tenant_id` varchar(32) NOT NULL DEFAULT 'tenantOne' COMMENT '租户id'," +
            "primary key (`id`)) engine=innodb character set= utf8mb4";

        neo.execute(createTableSql);
    }

    @After
    public void after() {
        String dropTable = "drop table " + tableName;
        neo.execute(dropTable);
    }

    @Test
    public void insertTest() {
        TenantContextHolder.setTenantId("test_tenant");

        // 设置租户信息
        TenantHandler tenantHandler = new TenantHandler();
        tenantHandler.setIncludeTables("*");
        tenantHandler.setColumnName("tenant_id");
        neo.setTenantHandler(tenantHandler);

        neo.insert(tableName, NeoMap.of("name", "nihao"));
        Assert.assertEquals("test_tenant", neo.value(tableName, "tenant_id", NeoMap.of()));
    }

    @Test
    public void deleteTest() {
        // 设置租户信息
        TenantHandler tenantHandler = new TenantHandler();
        tenantHandler.setIncludeTables("*");
        tenantHandler.setColumnName("tenant_id");
        neo.setTenantHandler(tenantHandler);

        // 模拟租户1添加数据
        TenantContextHolder.setTenantId("tenant_1");
        neo.insert(tableName, NeoMap.of("name", "nihao"));

        // 模拟租户2添加数据
        TenantContextHolder.setTenantId("tenant_2");
        neo.insert(tableName, NeoMap.of("name", "nihao"));

        // 模拟查询租户2
        Assert.assertEquals(1, neo.list(tableName, NeoMap.of("name", "nihao")).size());

        // 删除租户2数据
        neo.delete(tableName, NeoMap.of("name", "nihao"));

        // 切换租户1，并查询，这个时候还是有数据
        TenantContextHolder.setTenantId("tenant_1");
        Assert.assertEquals(1, neo.list(tableName, NeoMap.of("name", "nihao")).size());
    }

    @Test
    public void updateTest() {
        // 设置租户信息
        TenantHandler tenantHandler = new TenantHandler();
        tenantHandler.setIncludeTables("*");
        tenantHandler.setColumnName("tenant_id");
        neo.setTenantHandler(tenantHandler);

        // 模拟租户1添加数据
        TenantContextHolder.setTenantId("tenant_1");
        neo.insert(tableName, NeoMap.of("name", "nihao"));

        // 模拟租户1添加数据
        TenantContextHolder.setTenantId("tenant_2");
        neo.insert(tableName, NeoMap.of("name", "nihao"));

        // 模拟租户1切换
        TenantContextHolder.setTenantId("tenant_1");
        neo.update(tableName, NeoMap.of("name", "change"), NeoMap.of("name", "nihao"));

        Assert.assertEquals("change", neo.value(tableName, "name", NeoMap.of()));
    }

    @Test
    public void oneTest() {
        // 设置租户信息
        TenantHandler tenantHandler = new TenantHandler();
        tenantHandler.setIncludeTables("*");
        tenantHandler.setColumnName("tenant_id");
        neo.setTenantHandler(tenantHandler);

        // 模拟租户1添加数据
        TenantContextHolder.setTenantId("tenant_1");
        neo.insert(tableName, NeoMap.of("name", "nihao"));

        // 模拟查询租户2
        Assert.assertEquals("tenant_1", neo.value(tableName, "tenant_id", NeoMap.of("name", "nihao")));
    }

    @Test
    public void coutTest() {
        // 设置租户信息
        TenantHandler tenantHandler = new TenantHandler();
        tenantHandler.setIncludeTables("*");
        tenantHandler.setColumnName("tenant_id");
        neo.setTenantHandler(tenantHandler);

        // 模拟租户1添加数据
        TenantContextHolder.setTenantId("tenant_1");
        neo.insert(tableName, NeoMap.of("name", "nihao"));
        neo.insert(tableName, NeoMap.of("name", "nihao"));
        neo.insert(tableName, NeoMap.of("name", "nihao"));

        // 模拟租户2添加数据
        TenantContextHolder.setTenantId("tenant_2");
        neo.insert(tableName, NeoMap.of("name", "nihao"));
        neo.insert(tableName, NeoMap.of("name", "nihao"));

        // 模拟租户3添加数据
        TenantContextHolder.setTenantId("tenant_3");
        neo.insert(tableName, NeoMap.of("name", "nihao"));
        neo.insert(tableName, NeoMap.of("name", "nihao"));
        neo.insert(tableName, NeoMap.of("name", "nihao"));
        neo.insert(tableName, NeoMap.of("name", "nihao"));

        TenantContextHolder.setTenantId("tenant_1");
        Assert.assertEquals(3, (int) neo.count(tableName, NeoMap.of("name", "nihao")));

        TenantContextHolder.setTenantId("tenant_2");
        Assert.assertEquals(2, (int) neo.count(tableName, NeoMap.of("name", "nihao")));

        TenantContextHolder.setTenantId("tenant_3");
        Assert.assertEquals(4, (int) neo.count(tableName, NeoMap.of("name", "nihao")));
    }

    @Test
    public void valueTest() {
        // 设置租户信息
        TenantHandler tenantHandler = new TenantHandler();
        tenantHandler.setIncludeTables("*");
        tenantHandler.setColumnName("tenant_id");
        neo.setTenantHandler(tenantHandler);

        // 模拟租户1添加数据
        TenantContextHolder.setTenantId("tenant_1");
        neo.insert(tableName, NeoMap.of("group", "test", "name", "nihao1"));
        neo.insert(tableName, NeoMap.of("group", "test", "name", "nihao2"));

        // 模拟租户2添加数据
        TenantContextHolder.setTenantId("tenant_2");
        neo.insert(tableName, NeoMap.of("group", "test", "name", "nihao1"));
        neo.insert(tableName, NeoMap.of("group", "test", "name", "nihao2"));
        neo.insert(tableName, NeoMap.of("group", "test", "name", "nihao3"));

        // 模拟租户3添加数据
        TenantContextHolder.setTenantId("tenant_3");
        neo.insert(tableName, NeoMap.of("group", "test", "name", "nihao1"));
        neo.insert(tableName, NeoMap.of("group", "test", "name", "nihao2"));
        neo.insert(tableName, NeoMap.of("group", "test", "name", "nihao3"));
        neo.insert(tableName, NeoMap.of("group", "test", "name", "nihao4"));

        TenantContextHolder.setTenantId("tenant_1");
        Assert.assertEquals("tenant_1", neo.value(tableName, "tenant_id", NeoMap.of("group", "test")));

        TenantContextHolder.setTenantId("tenant_2");
        Assert.assertEquals("tenant_2", neo.value(tableName, "tenant_id", NeoMap.of("group", "test")));

        TenantContextHolder.setTenantId("tenant_3");
        Assert.assertEquals("tenant_3", neo.value(tableName, "tenant_id", NeoMap.of("group", "test")));
    }

    @Test
    public void valuesTest() {
        // 设置租户信息
        TenantHandler tenantHandler = new TenantHandler();
        tenantHandler.setIncludeTables("*");
        tenantHandler.setColumnName("tenant_id");
        neo.setTenantHandler(tenantHandler);

        // 模拟租户1添加数据
        TenantContextHolder.setTenantId("tenant_1");
        neo.insert(tableName, NeoMap.of("group", "test", "name", "nihao1"));
        neo.insert(tableName, NeoMap.of("group", "test", "name", "nihao2"));

        // 模拟租户2添加数据
        TenantContextHolder.setTenantId("tenant_2");
        neo.insert(tableName, NeoMap.of("group", "test", "name", "nihao1"));
        neo.insert(tableName, NeoMap.of("group", "test", "name", "nihao2"));
        neo.insert(tableName, NeoMap.of("group", "test", "name", "nihao3"));

        // 模拟租户3添加数据
        TenantContextHolder.setTenantId("tenant_3");
        neo.insert(tableName, NeoMap.of("group", "test", "name", "nihao1"));
        neo.insert(tableName, NeoMap.of("group", "test", "name", "nihao2"));
        neo.insert(tableName, NeoMap.of("group", "test", "name", "nihao3"));
        neo.insert(tableName, NeoMap.of("group", "test", "name", "nihao4"));

        TenantContextHolder.setTenantId("tenant_1");
        Assert.assertEquals(Arrays.asList("nihao1", "nihao2"), neo.values(tableName, "name", NeoMap.of("group", "test")));

        TenantContextHolder.setTenantId("tenant_2");
        Assert.assertEquals(Arrays.asList("nihao1", "nihao2", "nihao3"), neo.values(tableName, "name", NeoMap.of("group", "test")));

        TenantContextHolder.setTenantId("tenant_3");
        Assert.assertEquals(Arrays.asList("nihao1", "nihao2", "nihao3", "nihao4"), neo.values(tableName, "name", NeoMap.of("group", "test")));
    }

    @Test
    public void pageTest() {
        // 设置租户信息
        TenantHandler tenantHandler = new TenantHandler();
        tenantHandler.setIncludeTables("*");
        tenantHandler.setColumnName("tenant_id");
        neo.setTenantHandler(tenantHandler);

        // 模拟租户1添加数据
        TenantContextHolder.setTenantId("tenant_1");
        neo.insert(tableName, NeoMap.of("group", "test", "name", "nihao1"));
        neo.insert(tableName, NeoMap.of("group", "test", "name", "nihao2"));

        // 模拟租户2添加数据
        TenantContextHolder.setTenantId("tenant_2");
        neo.insert(tableName, NeoMap.of("group", "test", "name", "nihao1"));
        neo.insert(tableName, NeoMap.of("group", "test", "name", "nihao2"));
        neo.insert(tableName, NeoMap.of("group", "test", "name", "nihao3"));

        // 模拟租户3添加数据
        TenantContextHolder.setTenantId("tenant_3");
        neo.insert(tableName, NeoMap.of("group", "test", "name", "nihao1"));
        neo.insert(tableName, NeoMap.of("group", "test", "name", "nihao2"));
        neo.insert(tableName, NeoMap.of("group", "test", "name", "nihao3"));
        neo.insert(tableName, NeoMap.of("group", "test", "name", "nihao4"));

        TenantContextHolder.setTenantId("tenant_1");
        PageRsp<NeoMap> pageRsp = new PageRsp<>();
        pageRsp.setTotalNum(2);
        pageRsp.setDataList(Arrays.asList(NeoMap.of("name", "nihao1"), NeoMap.of("name", "nihao2")));
        Assert.assertEquals(pageRsp, neo.getPage(tableName, Columns.of("name"), NeoMap.of("group", "test"), NeoPage.of(1, 12)));

        TenantContextHolder.setTenantId("tenant_2");
        pageRsp.setTotalNum(3);
        pageRsp.setDataList(Arrays.asList(NeoMap.of("name", "nihao1"), NeoMap.of("name", "nihao2"), NeoMap.of("name", "nihao3")));
        Assert.assertEquals(pageRsp, neo.getPage(tableName, Columns.of("name"), NeoMap.of("group", "test"), NeoPage.of(1, 12)));

        TenantContextHolder.setTenantId("tenant_3");
        pageRsp.setTotalNum(4);
        pageRsp.setDataList(Arrays.asList(NeoMap.of("name", "nihao1"), NeoMap.of("name", "nihao2"), NeoMap.of("name", "nihao3"), NeoMap.of("name", "nihao4")));
        Assert.assertEquals(pageRsp, neo.getPage(tableName, Columns.of("name"), NeoMap.of("group", "test"), NeoPage.of(1, 12)));
    }

    @Test
    public void listTest() {
        // 设置租户信息
        TenantHandler tenantHandler = new TenantHandler();
        tenantHandler.setIncludeTables("*");
        tenantHandler.setColumnName("tenant_id");
        neo.setTenantHandler(tenantHandler);

        // 模拟租户1添加数据
        TenantContextHolder.setTenantId("tenant_1");
        neo.insert(tableName, NeoMap.of("group", "test", "name", "nihao1"));
        neo.insert(tableName, NeoMap.of("group", "test", "name", "nihao2"));

        // 模拟租户2添加数据
        TenantContextHolder.setTenantId("tenant_2");
        neo.insert(tableName, NeoMap.of("group", "test", "name", "nihao1"));
        neo.insert(tableName, NeoMap.of("group", "test", "name", "nihao2"));
        neo.insert(tableName, NeoMap.of("group", "test", "name", "nihao3"));

        // 模拟租户3添加数据
        TenantContextHolder.setTenantId("tenant_3");
        neo.insert(tableName, NeoMap.of("group", "test", "name", "nihao1"));
        neo.insert(tableName, NeoMap.of("group", "test", "name", "nihao2"));
        neo.insert(tableName, NeoMap.of("group", "test", "name", "nihao3"));
        neo.insert(tableName, NeoMap.of("group", "test", "name", "nihao4"));

        TenantContextHolder.setTenantId("tenant_1");
        List<NeoMap> dataList1 = Arrays.asList(NeoMap.of("name", "nihao1"), NeoMap.of("name", "nihao2"));
        Assert.assertEquals(dataList1, neo.list(tableName, Columns.of("name"), NeoMap.of("group", "test")));

        TenantContextHolder.setTenantId("tenant_2");
        List<NeoMap> dataList2 = Arrays.asList(NeoMap.of("name", "nihao1"), NeoMap.of("name", "nihao2"), NeoMap.of("name", "nihao3"));
        Assert.assertEquals(dataList2, neo.list(tableName, Columns.of("name"), NeoMap.of("group", "test")));

        TenantContextHolder.setTenantId("tenant_3");
        List<NeoMap> dataList3 = Arrays.asList(NeoMap.of("name", "nihao1"), NeoMap.of("name", "nihao2"), NeoMap.of("name", "nihao3"), NeoMap.of("name", "nihao4"));
        Assert.assertEquals(dataList3, neo.list(tableName, Columns.of("name"), NeoMap.of("group", "test")));
    }

    @Test
    public void tenantHandlerTest1() {
        String tableName1 = "isc_neo_tenant_test1";
        String tableName2 = "isc_neo_tenant_test2";

        String createTableSql1 = "create table if not exists `isc_neo_tenant_test1`(" +
            "`id` bigint unsigned not null auto_increment, " +
            "`group` varchar(12) default '' comment '测试', " +
            "`name` varchar(12) default '' comment '测试', " +
            "`tenant_id` varchar(32) NOT NULL DEFAULT 'tenantOne' COMMENT '租户id'," +
            "primary key (`id`)) engine=innodb character set= utf8mb4";
        neo.execute(createTableSql1);

        String createTableSql2 = "create table if not exists `isc_neo_tenant_test2`(" +
            "`id` bigint unsigned not null auto_increment, " +
            "`group` varchar(12) default '' comment '测试', " +
            "`name` varchar(12) default '' comment '测试', " +
            "`tenant_id` varchar(32) NOT NULL DEFAULT 'tenantOne' COMMENT '租户id'," +
            "primary key (`id`)) engine=innodb character set= utf8mb4";
        neo.execute(createTableSql2);

        // 设置租户信息，这里只关心table1
        TenantHandler tenantHandler = new TenantHandler();
        tenantHandler.setIncludeTables(tableName1);
        tenantHandler.setColumnName("tenant_id");
        neo.setTenantHandler(tenantHandler);

        // 模拟租户1添加数据
        TenantContextHolder.setTenantId("tenant_1");
        neo.insert(tableName1, NeoMap.of("group", "test", "name", "nihao1"));
        neo.insert(tableName2, NeoMap.of("group", "test", "name", "nihao2"));

        Assert.assertEquals("tenant_1", neo.value(tableName1, "tenant_id", NeoMap.of("groupo", "test")));
        Assert.assertEquals("tenantOne", neo.value(tableName2, "tenant_id", NeoMap.of("groupo", "test")));
    }

    @Test
    public void tenantHandlerTest2() {
        String tableName1 = "isc_neo_tenant_test1";
        String tableName2 = "isc_neo_tenant_test2";

        String createTableSql1 = "create table if not exists `isc_neo_tenant_test1`(" +
            "`id` bigint unsigned not null auto_increment, " +
            "`group` varchar(12) default '' comment '测试', " +
            "`name` varchar(12) default '' comment '测试', " +
            "`tenant_id` varchar(32) NOT NULL DEFAULT 'tenantOne' COMMENT '租户id'," +
            "primary key (`id`)) engine=innodb character set= utf8mb4";
        neo.execute(createTableSql1);

        String createTableSql2 = "create table if not exists `isc_neo_tenant_test2`(" +
            "`id` bigint unsigned not null auto_increment, " +
            "`group` varchar(12) default '' comment '测试', " +
            "`name` varchar(12) default '' comment '测试', " +
            "`tenant_id` varchar(32) NOT NULL DEFAULT 'tenantOne' COMMENT '租户id'," +
            "primary key (`id`)) engine=innodb character set= utf8mb4";
        neo.execute(createTableSql2);

        // 设置租户信息，这里只关心table1
        TenantHandler tenantHandler = new TenantHandler();
        tenantHandler.setIncludeTables("*");
        tenantHandler.setExcludeTables(tableName2);
        tenantHandler.setColumnName("tenant_id");
        neo.setTenantHandler(tenantHandler);

        // 模拟租户1添加数据
        TenantContextHolder.setTenantId("tenant_1");
        neo.insert(tableName1, NeoMap.of("group", "test", "name", "nihao1"));
        neo.insert(tableName2, NeoMap.of("group", "test", "name", "nihao2"));

        Assert.assertEquals("tenant_1", neo.value(tableName1, "tenant_id", NeoMap.of("groupo", "test")));
        Assert.assertEquals("tenantOne", neo.value(tableName2, "tenant_id", NeoMap.of("groupo", "test")));
    }

    @Test
    public void oneMultiTenantNeoMapTest() {
        // curd还有join

        // 设置租户信息
        TenantHandler tenantHandler = new TenantHandler();
        tenantHandler.setIncludeTables("*");
        tenantHandler.setColumnName("tenant_id");
        neo.setTenantHandler(tenantHandler);
        neo.closeStandard();

        // 模拟租户1添加数据
        TenantContextHolder.setTenantId("tenant_1");
        neo.insert(tableName, NeoMap.of("name", "nihao"));
        neo.value(tableName, "name", NeoMap.of("name", "nihao"));
        neo.update(tableName, NeoMap.of("name", "nihaochange"), NeoMap.of("name", "nihao"));
    }

    @Test
    public void oneMultiTenantQueryTest() {
        // curd还有join

        // 设置租户信息
        TenantHandler tenantHandler = new TenantHandler();
        tenantHandler.setIncludeTables("*");
        tenantHandler.setColumnName("tenant_id");
        neo.setTenantHandler(tenantHandler);
        neo.closeStandard();

        // 模拟租户1添加数据
        TenantContextHolder.setTenantId("tenant_1");
        neo.insert(tableName, NeoMap.of("name", "nihao"));

        SearchQuery query = new SearchQuery();
        query.and("name", "nihao");
        neo.value(tableName, "name", query);

        neo.update(tableName, NeoMap.of("name", "nihaochange"), query);
    }

    @Test
    public void buildJoinWithTenantForNeoMapTest() {
        String createTableSql1 = "create table if not exists `join_test_table1`(" +
            "`id` bigint unsigned not null auto_increment, " +
            "`group` varchar(12) default '' comment '测试', " +
            "`name` varchar(12) default '' comment '测试', " +
            "`tenant_id` varchar(32) NOT NULL DEFAULT 'tenantOne' COMMENT '租户id'," +
            "primary key (`id`)) engine=innodb character set= utf8mb4";

        neo.execute(createTableSql1);

        String createTableSql2 = "create table if not exists `join_test_table2`(" +
            "`id` bigint unsigned not null auto_increment, " +
            "`group` varchar(12) default '' comment '测试', " +
            "`name` varchar(12) default '' comment '测试', " +
            "`tenant_id` varchar(32) NOT NULL DEFAULT 'tenantOne' COMMENT '租户id'," +
            "primary key (`id`)) engine=innodb character set= utf8mb4";

        neo.execute(createTableSql2);

        // 设置租户信息
        TenantHandler tenantHandler = new TenantHandler();
        tenantHandler.setIncludeTables("*");
        tenantHandler.setColumnName("tenant_id");
        neo.setTenantHandler(tenantHandler);
        neo.closeStandard();

        TenantContextHolder.setTenantId("tenant_3");

        NeoJoiner neoJoiner = neo.leftJoin("join_test_table1", "join_test_table2").on("name", "name");
        TableMap searchMap = TableMap.of("join_test_table2", "group", "ff");
        searchMap.put("join_test_table1", "group", "ee");
        neoJoiner.list(Columns.of().table("join_test_table1", "name"), searchMap);
    }

    @Test
    public void buildJoinWithTenantForQueryTest() {
        String createTableSql1 = "create table if not exists `join_test_table1`(" +
            "`id` bigint unsigned not null auto_increment, " +
            "`group` varchar(12) default '' comment '测试', " +
            "`name` varchar(12) default '' comment '测试', " +
            "`tenant_id` varchar(32) NOT NULL DEFAULT 'tenantOne' COMMENT '租户id'," +
            "primary key (`id`)) engine=innodb character set= utf8mb4";

        neo.execute(createTableSql1);

        String createTableSql2 = "create table if not exists `join_test_table2`(" +
            "`id` bigint unsigned not null auto_increment, " +
            "`group` varchar(12) default '' comment '测试', " +
            "`name` varchar(12) default '' comment '测试', " +
            "`tenant_id` varchar(32) NOT NULL DEFAULT 'tenantOne' COMMENT '租户id'," +
            "primary key (`id`)) engine=innodb character set= utf8mb4";

        neo.execute(createTableSql2);

        // 设置租户信息
        TenantHandler tenantHandler = new TenantHandler();
        tenantHandler.setIncludeTables("*");
        tenantHandler.setColumnName("tenant_id");
        neo.setTenantHandler(tenantHandler);
        neo.closeStandard();

        TenantContextHolder.setTenantId("tenant_3");

        NeoJoiner neoJoiner = neo.leftJoin("join_test_table1", "join_test_table2").on("name", "name");
        TableMap searchMap = TableMap.of("join_test_table2", "group", "ff");
        searchMap.put("join_test_table1", "group", "ee");

        SearchQuery searchQuery = new SearchQuery();
        searchQuery.andTable("join_test_table2", "group", "ff");
        searchQuery.andTable("join_test_table1", "group", "ee");
        neoJoiner.list(Columns.of().table("join_test_table1", "name"), searchQuery);

        neoJoiner.list(Columns.of().table("join_test_table1", "name"), searchMap);
    }
}
