package com.simonalong.neo.uid;

import com.simonalong.neo.NeoBaseTest;
import java.sql.SQLException;
import org.junit.Before;
import org.junit.Test;

/**
 * @author zhouzhenyong
 * @since 2019/5/1 下午10:51
 */
public class UidGeneratorTest extends NeoBaseTest {

    public UidGeneratorTest() throws SQLException {}

    UidGenerator generator;

    @Before
    public void before(){
        generator = UidGenerator.getInstance(neo);
    }

    @Test
    public void generateTest1(){
        show(generator.getUuid());
        show(generator.getUuid());
        show(generator.getUuid());
    }

    @Test
    public void generateTest2(){
        show(generator.getUuid());
    }
}
