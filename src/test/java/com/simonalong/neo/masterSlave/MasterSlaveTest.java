package com.simonalong.neo.masterSlave;

import com.simonalong.neo.BaseTest;
import com.simonalong.neo.Neo;
import com.simonalong.neo.NeoMap;
import org.junit.Test;

/**
 * @author shizi
 * @since 2020/5/28 1:01 AM
 */
public class MasterSlaveTest extends BaseTest {

    @Test
    public void test1(){
        String url = "jdbc:mysql:replication://127.0.0.1:3307,127.0.0.1:3308/demo1";
        String username = "root";
        String password = "";

        String tableName = "neo_city";
        Neo neo = Neo.connect(url, username, password);
//        neo.insert(tableName, NeoMap.of("name", "121", "center", "ok21", "status", 1, "city_code", "2"));
        show(neo.list(tableName, NeoMap.of()));
    }
}
