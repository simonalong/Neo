package com.simonalong.neo.devide;

import com.simonalong.neo.Neo;
import com.simonalong.neo.NeoDevide;
import com.simonalong.neo.NeoMap;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shizi
 * @since 2020/6/6 6:52 PM
 */
public class NeoDevideTest {

    /**
     * 测试分库
     */
    @Test
    public void testDevideDb() {
        List<Neo> neoList = new ArrayList<>();
        NeoDevide neoDevide = new NeoDevide();
        // 设置分库及参数
        neoDevide.setDevideDb(neoList, "id");
        // 设置分表及参数
        neoDevide.setDevideTable("neo_user_{0, 1023}", "user_id");
        neoDevide.setDevideTable("neo_order_{0, 1023}", "order_id");
        neoDevide.insert("neo_user", NeoMap.of("id", 12, "user_id", 100, "name", "name1"));

        neoDevide.one("neo_user", NeoMap.of());
    }
}
