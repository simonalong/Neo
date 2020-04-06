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
        NeoDevide neoDevide = new NeoDevide();
        // 设置分库
        neoDevide.setDevideDb("neo_devide_{0, 12}", neoList);
        // 添加分库的分库参数
        neoDevide.addDbDevideParameter("neo_table", "user_id");
        neoDevide.addDbDevideParameter("neo_order", "order_id");
        neoDevide.insert("neo_table", NeoMap.of("group", "name"));
    }

    /**
     * 测试分表
     */
    @Test
    public void testDevideTable() {
        NeoDevide neoDevide = new NeoDevide();
        // 设置分表的分表参数
        neoDevide.setDevideTable("neo_table{0, 100}", "user_id");
        neoDevide.insert("neo_table", NeoMap.of("group", "name"));
    }

    /**
     * 测试分库分表
     *
     * <p>
     * 其中库分为12个，
     */
    @Test
    public void testDevideDbAndTable() {
        // todo
        List<Neo> neoList = new ArrayList<>();
        NeoDevide neoDevide = new NeoDevide();
        // 设置分库
        neoDevide.setDevideDb("neo_devide_[0, 12)", neoList);
        // 添加分库的分库参数
        neoDevide.addDbDevideParameter("neo_table", "user_id");
        neoDevide.addDbDevideParameter("neo_order", "order_id");

        // 设置分表
        neoDevide.setDevideTable("neo_table{0, 100}", "user_id");
        neoDevide.setDevideTable("neo_order{0, 200}", "order_id");

        // 添加数据
        neoDevide.insert("neo_table1", NeoMap.of("group", "name"));
    }
}
