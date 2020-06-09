package com.simonalong.neo.devide;

import com.simonalong.neo.Neo;
import com.simonalong.neo.NeoMap;
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
        List<Neo> neoList = new ArrayList<>();
        DevideNeo devideNeo = new DevideNeo();
        // 设置分库及参数
        devideNeo.setDevideDb(neoList, "neo_devide_table", "id");

        devideNeo.insert("neo_user", NeoMap.of("id", 12, "user_id", 100, "name", "name1"));
        devideNeo.one("neo_user", NeoMap.of());
    }

    /**
     * 测试分表
     */
    @Test
    public void devideTableTest() {
        List<Neo> neoList = new ArrayList<>();
        DevideNeo devideNeo = new DevideNeo();
        // 设置分库及参数
        devideNeo.setDevideDb(neoList, "neo_devide_table", "id");

        devideNeo.insert("neo_user", NeoMap.of("id", 12, "user_id", 100, "name", "name1"));
        devideNeo.one("neo_user", NeoMap.of());
    }

    /**
     * 测试分库分表
     */
    @Test
    public void devideDbTableTest() {
        List<Neo> neoList = new ArrayList<>();
        DevideNeo devideNeo = new DevideNeo();
        // 设置分库及参数
        devideNeo.setDevideDb(neoList, "neo_devide_table", "id");
        // 设置分表及参数
        devideNeo.setDevideTable("neo_devide_table{0, 12}", "id");

        devideNeo.insert("neo_user", NeoMap.of("id", 12, "user_id", 100, "name", "name1"));
        devideNeo.one("neo_user", NeoMap.of());
    }
}
