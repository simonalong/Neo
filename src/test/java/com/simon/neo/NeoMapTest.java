package com.simon.neo;

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
}
