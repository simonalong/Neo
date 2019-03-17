package com.simon.neo;

import java.util.Properties;
import org.junit.Test;

/**
 * @author zhouzhenyong
 * @since 2019/3/16 上午11:19
 */
public class connectTest extends BaseTest{

    @Test
    public void testPropertiesFromPath(){
        Neo neo = Neo.connect("/config/db.properties");

        show(neo.insert("tina_test", NeoMap.of("group", "con1")));
    }

    @Test
    public void testPropertiesFromPath2(){
        Neo neo = Neo.connect("/config/db2.properties");

        show(neo.insert("tina_test", NeoMap.of("group", "con1")));
    }

    @Test
    public void testPropertiesFromPath3(){
        Neo neo = Neo.connect("/config/db3.properties");

        show(neo.insert("tina_test", NeoMap.of("group", "con1")));
    }

    @Test
    public void testDirectConnect(){
        String url = "jdbc:mysql://118.31.38.50:3306/tina?useUnicode=true&characterEncoding=UTF-8&useSSL=false";
        String user = "like";
        String password = "Like@123";
        Neo neo = Neo.connect(url, user, password);

        show(neo.insert("tina_test", NeoMap.of("group", "con1")));
    }
}
