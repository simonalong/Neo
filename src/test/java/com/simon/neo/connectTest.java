package com.simon.neo;

import org.junit.Test;

/**
 * @author zhouzhenyong
 * @since 2019/3/16 上午11:19
 */
public class connectTest extends BaseTest{

    @Test
    public void test1(){
        Neo neo = Neo.connect("/config/db.properties");

        show(neo.insert("tina_test", NeoMap.of("group", "con1")));
    }
}
