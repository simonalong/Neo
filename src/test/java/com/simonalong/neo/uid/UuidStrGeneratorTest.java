package com.simonalong.neo.uid;

import org.junit.Test;

/**
 * @author shizi
 * @since 2020/7/14 8:41 PM
 */
public class UuidStrGeneratorTest extends UuidBaseTest {

    @Test
    public void test1() {
        UuidGenerator generator = UuidGenerator.getInstance(neo);
        generator.addNamespaces("test1");
        long uuid = generator.getUUid("test1");
        // 52149403862966272
        show(uuid);
        // 004w4JphV2
        show(generator.getUUidStr("test1"));
    }
}
