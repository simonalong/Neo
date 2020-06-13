package com.simonalong.neo.devide;

import com.simonalong.neo.Neo;
import com.simonalong.neo.NeoMap;
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
    @Test
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
        devideNeo.start();

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
    @Test
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
        devideNeo.setDevideTable("neo_devide_table{0, 8}", "id");
        devideNeo.start();

        devideNeo.insert("neo_devide_table", NeoMap.of("id", uuid.getUUid("devideTable"), "user_id", 100, "name", "name1"));

        // 查询所有库表
        DevideMultiNeo multiNeo = devideNeo.asDevideMultiNeo();
        System.out.println(multiNeo.list("neo_devide_table", NeoMap.of()));
    }

    /**
     * 测试分库分表
     */
    @Test
    public void devideDbTableTest() {
        List<Neo> neoList = new ArrayList<>();
        DevideNeo devideNeo = new DevideNeo();
        devideNeo.setDbList(neoList);
        // 设置分库及参数
        devideNeo.setDevideDb("neo_devide_table", "id");
        // 设置分表及参数
        devideNeo.setDevideTable("neo_devide_table{0, 12}", "id");

        devideNeo.insert("neo_user", NeoMap.of("id", 12, "user_id", 100, "name", "name1"));
        devideNeo.one("neo_user", NeoMap.of());
    }
}
