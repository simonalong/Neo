package com.simonalong.neo.uid;

import com.simonalong.neo.NeoBaseTest;

import java.sql.SQLException;

import org.junit.Test;

/**
 * @author zhouzhenyong
 * @since 2019/5/1 下午10:51
 */
public class UuidGeneratorTest extends NeoBaseTest {

    public UuidGeneratorTest() throws SQLException {}

    @Test
    public void generateTest1() {
        UuidGenerator generator = UuidGenerator.getInstance(neo);
        generator.addNamespaces("test1");
        show(generator.getUUid("test1"));
//        show(generator.getUUid("test1"));
//        show(generator.getUUid("test2"));
    }
}
