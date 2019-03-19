package com.simon.neo;

import com.simon.neo.NeoMap.NamingChg;
import com.simon.neo.entity.DemoEntity;
import org.junit.Test;

/**
 * @author zhouzhenyong
 * @since 2019/3/12 下午12:49
 */
public class NeoMapTest {

    @Test
    public void testAppend(){
        NeoMap neoMap1 = NeoMap.of("a", 123);
        NeoMap neoMap2 = NeoMap.of("b", 123);

        NeoMap data = NeoMap.of().append(neoMap1).append(neoMap2);
        System.out.println(data);
    }

    @Test
    public void testAppend2(){
        NeoMap neoMap1 = NeoMap.of("a", 111);
        NeoMap neoMap2 = NeoMap.of("a", 222);

        NeoMap data = NeoMap.of().append(neoMap1).append(neoMap2);
        System.out.println(data);
    }

    @Test
    public void testAs(){

        NeoMap map1 = NeoMap.of("user_name", "name", "id", 123, "data_base_name", "tina_test");
        DemoEntity demo1 = map1.as(DemoEntity.class);
        System.out.println(demo1);

        NeoMap.setDefaultNamingChg(NamingChg.UNDERLINE);
        NeoMap map2 = NeoMap.of("user_name", "name", "id", 123, "data_base_name", "tina_test");
        DemoEntity demo2 = map2.as(DemoEntity.class);
        System.out.println(demo2);

        NeoMap.setDefaultNamingChg(NamingChg.NONCHG);
        NeoMap map3 = NeoMap.of("user_name", "name", "id", 123, "data_base_name", "tina_test");
        DemoEntity demo3 = map3.as(DemoEntity.class);
        System.out.println(demo3);
    }

    @Test
    public void testFrom(){
        DemoEntity demo = new DemoEntity();
        demo.setGroup("group1");
        demo.setName("name1");
        demo.setUserName("userName1");
        demo.setDataBaseName("databasename");
        demo.setId(212);

        NeoMap neoMap = NeoMap.from(demo);
        System.out.println(neoMap);
    }
}
