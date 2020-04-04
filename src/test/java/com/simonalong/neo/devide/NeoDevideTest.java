package com.simonalong.neo.devide;

import com.simonalong.neo.Neo;
import com.simonalong.neo.NeoBaseTest;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.NeoPool;
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
        NeoPool neoPool = NeoPool.getInstance();
        // 添加分库
        neoPool.addDevideDbList("neo_devide_{0, 12}", neoList, "user_id");
        // 获取分库
//        Neo devideDb = neoPool.getDevideDb("db1", "user_id");

        neoPool.insert("neo_devide_", NeoMap.of("group", "name"));
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
        NeoPool neoPool = NeoPool.getInstance();
        // 添加分库
        neoPool.addDevideDbList("db1{0, 10}", neoList);
        // 获取分库
        Neo devideDb = neoPool.getDevideDb("db1", "user_id");


//        devideDb.addDevideTable("neo_table1[0, 100]", "order_id");
//        devideDb.insert("neo_table1", NeoMap.of("group", "name"));
    }
}
