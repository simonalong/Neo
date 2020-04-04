package com.simonalong.neo.devide;

import com.simonalong.neo.Neo;
import com.simonalong.neo.NeoBaseTest;
import com.simonalong.neo.NeoDevide;
import com.simonalong.neo.NeoMap;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 分库分表功能开发
 *
 * @author shizi
 * @since 2020/4/3 5:34 PM
 */
public class NeoDevideTest extends NeoBaseTest {

    /**
     * 测试分库
     */
    @Test
    public void testDevideDb() {
        List<Neo> neoList = new ArrayList<>();
        NeoDevide neoDevide = new NeoDevide("neo_devide_{0, 12}", neoList, "neo_table", "user_id");
        // 获取分库
//        Neo devideDb = neoDevide.getDevideDb("db1", "user_id");

        neoDevide.insert("neo_table", NeoMap.of("group", "name"));
    }

    /**
     * 测试分表
     */
    @Test
    public void testDevideTable() {
//        neo.addDevideTable("neo_table1_{0, 100}", "order_id");
//        neo.insert("neo_table1_", NeoMap.of("group", "name"));
    }

    /**
     * 测试分库分表
     */
    @Test
    public void testDevideDbAndTable() {
        List<Neo> neoList = new ArrayList<>();
        NeoDevide neoDevide = NeoDevide.getInstance();
        // 添加分库
        neoDevide.addDevideDbList("db1{0, 10}", neoList);
        // 获取分库
        Neo devideDb = neoDevide.getDevideDb("db1", "user_id");


//        devideDb.addDevideTable("neo_table1[0, 100]", "order_id");
//        devideDb.insert("neo_table1", NeoMap.of("group", "name"));
    }
}
