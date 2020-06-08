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
    public void testDevideDb() {
        List<Neo> neoList = new ArrayList<>();
        DevideNeo devideNeo = new DevideNeo();
        // 设置分库及参数
        devideNeo.setDevideDb(neoList, "id");
        // 设置分表及参数
        devideNeo.setDevideTable("neo_user{0, 1023}", "user_id");
        devideNeo.setDevideTable("neo_order{0, 1023}", "order_id");
        devideNeo.insert("neo_user", NeoMap.of("id", 12, "user_id", 100, "name", "name1"));

        devideNeo.one("neo_user", NeoMap.of());
    }
}
