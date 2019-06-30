package com.github.simonalong;

import org.junit.Test;

/**
 * @author zhouzhenyong
 * @since 2019/3/16 上午11:19
 */
public class connectTest extends BaseTest{

    @Test
    public void testPropertiesFromPath(){
        Neo neo = Neo.connect("/config/db.properties");

        show(neo.insert("neo_table1", NeoMap.of("group", "con1")));
    }

    @Test
    public void testPropertiesFromPath2(){
        Neo neo = Neo.connect("/config/db2.properties");

        show(neo.insert("neo_table1", NeoMap.of("group", "con1")));
    }

    @Test
    public void testPropertiesFromPath3(){
        Neo neo = Neo.connect("/config/db3.properties");

        show(neo.insert("neo_table1", NeoMap.of("group", "con1")));
    }

    @Test
    public void testDirectConnect(){
        String url = "jdbc:mysql://127.0.0.1:3306/neo?useUnicode=true&characterEncoding=UTF-8&useSSL=false";
        String user = "neo_test";
        String password = "neo@Test123";
        Neo neo = Neo.connect(url, user, password);

        show(neo.insert("neo_table1", NeoMap.of("group", "con1")));
    }
}
