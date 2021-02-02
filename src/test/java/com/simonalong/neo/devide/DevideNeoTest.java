package com.simonalong.neo.devide;

import com.simonalong.neo.Columns;
import com.simonalong.neo.Neo;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.db.NeoPage;
import com.simonalong.neo.devide.strategy.DevideTypeEnum;
import com.simonalong.neo.uid.UuidGenerator;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shizi
 * @since 2020/6/6 6:52 PM
 */
public class DevideNeoTest {

    /**
     * 测试分库
     */
    //@Test
    public void devideDbTest() {
        UuidGenerator uuid = getUuidGenerator();
        uuid.addNamespaces("devideDb");
        List<Neo> neoList = getDevideDb(8);

        DevideNeo devideNeo = new DevideNeo();
        // 设置分库策略
        devideNeo.setDevideTypeEnum(DevideTypeEnum.UUID_HASH);
        devideNeo.setDbList(neoList);
        // 设置分库及参数
        devideNeo.setDevideDb("neo_devide_table", "id");
        devideNeo.init();

        // 数据插入
        devideNeo.insert("neo_devide_table", NeoMap.of("id", uuid.getUUid("devideDb"), "age", 100, "name", "name1"));
        devideNeo.insert("neo_devide_table", NeoMap.of("id", uuid.getUUid("devideDb"), "age", 101, "name", "name2"));
        devideNeo.insert("neo_devide_table", NeoMap.of("id", uuid.getUUid("devideDb"), "age", 102, "name", "name3"));

        // 查询所有库表
        DevideMultiNeo multiNeo = devideNeo.asDevideMultiNeo();
        System.out.println(multiNeo.list("neo_devide_table", NeoMap.of()));
    }

    private List<Neo> getDevideDb(Integer num) {
        List<Neo> neoList = new ArrayList<>();
        String url = "jdbc:mysql://localhost:3310/devide";
        String username = "root";
        String password = "";
        for (int i = 0; i < num; i++) {
            Neo db = Neo.connect(url + i, username, password);
            db.setExplainFlag(false);
            db.setMonitorFlag(false);
            neoList.add(db);
        }
        return neoList;
    }

    private UuidGenerator getUuidGenerator() {
        String url = "jdbc:mysql://localhost:3310/common";
        String username = "root";
        String password = "";
        return UuidGenerator.getInstance(Neo.connect(url, username, password));
    }

    /**
     * 测试分表，一个库中，有一个逻辑表，逻辑表对应多个实际的物理表
     */
    //@Test
    public void devideTableTest() {
        UuidGenerator uuid = getUuidGenerator();
        uuid.addNamespaces("devideTable");

        String url = "jdbc:mysql://localhost:3310/devide_table";
        String username = "root";
        String password = "";

        DevideNeo devideNeo = new DevideNeo();
        devideNeo.setDefaultDb(Neo.connect(url, username, password));
        devideNeo.setDevideTypeEnum(DevideTypeEnum.UUID_HASH);
        // 设置分库及参数
        devideNeo.setDevideTable("neo_devide_table{0, 7}", "id");
        devideNeo.init();

        devideNeo.insert("neo_devide_table", NeoMap.of("id", uuid.getUUid("devideTable"), "user_id", 100, "name", "name1"));

        // 查询所有库表
        DevideMultiNeo multiNeo = devideNeo.asDevideMultiNeo();
        System.out.println(multiNeo.list("neo_devide_table", NeoMap.of()));
    }

    /**
     * 测试分库分表
     * <p>
     * 4个分库以及每个库里面都有8个分表
     */
    //@Test
    public void devideDbTableTest() {
        UuidGenerator uuid = getUuidGenerator();
        uuid.addNamespaces("devideDbTable", "userNamespace");

        List<Neo> neoList = getDevideDb();
        DevideNeo devideNeo = new DevideNeo();
        devideNeo.setDbList(neoList);
        devideNeo.setDevideTypeEnum(DevideTypeEnum.UUID_HASH);
        // 设置分库及参数
        devideNeo.setDevideDb("neo_devide_table", "user_id");
        // 设置分表及参数
        devideNeo.setDevideTable("neo_devide_table{0, 7}", "id");
        // 初始化
        devideNeo.init();

        Long userId = uuid.getUUid("userNamespace");
//
        devideNeo.insert("neo_devide_table", NeoMap.of("id", uuid.getUUid("devideDbTable"), "user_id", userId, "name", "name1"));
        devideNeo.insert("neo_devide_table", NeoMap.of("id", uuid.getUUid("devideDbTable"), "user_id", userId, "name", "name1"));
        devideNeo.insert("neo_devide_table", NeoMap.of("id", uuid.getUUid("devideDbTable"), "user_id", userId, "name", "name1"));

        // 多库多表查询所有数据
        DevideMultiNeo devideMultiNeo = devideNeo.asDevideMultiNeo();
        System.out.println("one: == " + devideMultiNeo.list("neo_devide_table", NeoMap.of("id", 40901634569601024L)));
        System.out.println("list: == " + devideMultiNeo.list("neo_devide_table", NeoMap.of()));
        System.out.println("value: == " + devideMultiNeo.value("neo_devide_table", "user_id", NeoMap.of("id", 40901634569601024L)));
        System.out.println("values: == " + devideMultiNeo.values("neo_devide_table", "user_id", NeoMap.of()));
        System.out.println("page: == " + devideMultiNeo.page("neo_devide_table", NeoMap.of(), NeoPage.of(1, 10)));
        System.out.println("count: == " + devideMultiNeo.count("neo_devide_table"));
    }

    //@Test
    public void test333() {
        String url = "jdbc:mysql://localhost:3310/devide2";
        String username = "root";
        String password = "";

        Neo db = Neo.connect(url, username, password);
        System.out.println(db.page("neo_devide_table2", Columns.of("id"), NeoMap.of("order by", "id asc"), NeoPage.of(1, 10)));
    }

    private List<Neo> getDevideDb() {
        List<Neo> dbList = new ArrayList<>();
        String url = "jdbc:mysql://localhost:3310/devide";
        String username = "root";
        String password = "";

        for (int index = 0; index < 4; index++) {
            dbList.add(Neo.connect(url + index, username, password));
        }
        return dbList;
    }

    /**
     * 创建分库分表
     */
    //@Test
    public void testCreateDbTable() {
        String url = "jdbc:mysql://localhost:3310/devide";
        String username = "root";
        String password = "";
        for (int index = 0; index < 4; index++) {
            Neo db = Neo.connect(url + index, username, password);
            createTable(db);
        }
    }

    /**
     * 删除分库分表
     */
    //@Test
    public void testDropDbTable() {
        String url = "jdbc:mysql://localhost:3310/devide";
        String username = "root";
        String password = "";
        for (int index = 0; index < 4; index++) {
            Neo db = Neo.connect(url + index, username, password);
            dropTable(db);
        }
    }

    private void createTable(Neo db) {
        for (int index = 0; index < 8; index++) {
            String sql = "CREATE TABLE `neo_devide_table" + index + "` (\n" + "  `id` bigint unsigned NOT NULL,\n" + "  `group` char(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '数据来源组，外键关联lk_config_group',\n" + "  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '任务name',\n" + "  `user_name` varchar(24) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '修改人名字',\n" + "  `age` int DEFAULT NULL,\n" + "  `user_id` bigint not null comment '用户id',\n" + "  `sl` bigint DEFAULT NULL,\n" + "  PRIMARY KEY (`id`),\n" + "  KEY `group_index` (`group`),\n" + "  KEY `k_group` (`group`)\n" + ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci";
            db.execute(sql);
        }
    }

    private void dropTable(Neo db) {
        for (int index = 0; index < 8; index++) {
            String sql = "drop table neo_devide_table" + index;
            db.execute(sql);
        }
    }

}
