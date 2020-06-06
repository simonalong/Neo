package com.simonalong.neo.devide;

import com.simonalong.neo.NeoDevide;
import org.junit.Test;

/**
 * @author shizi
 * @since 2020/6/6 6:52 PM
 */
public class NeoDevideTest {

    @Test
    public void test1(){
        NeoDevide devide = new NeoDevide();
        devide.addDbDevideParameter();
        devide.addDevideTable();
        devide.setDevideDb();
    }
}
