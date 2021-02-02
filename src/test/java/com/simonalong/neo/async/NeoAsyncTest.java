package com.simonalong.neo.async;

import com.simonalong.neo.NeoMap;
import com.simonalong.neo.NeoBaseTest;

import java.util.concurrent.CountDownLatch;

import lombok.SneakyThrows;
import org.junit.*;

/**
 * @author zhouzhenyong
 * @since 2019/8/18 下午10:27
 */
public class NeoAsyncTest extends NeoBaseTest {

    public NeoAsyncTest() {}

    @BeforeClass
    public static void beforeClass() {
        neo.truncateTable(TABLE_NAME);
    }

    @AfterClass
    public static void afterClass() {
        neo.truncateTable(TABLE_NAME);
    }

    @SneakyThrows
    @Test
    public void testInsertAsync1() {
        CountDownLatch latch = new CountDownLatch(1);
        neo.insertAsync(TABLE_NAME, NeoMap.of("group", "async", "name", "name_async")).thenApply(r -> {
            latch.countDown();
            return r;
        }).exceptionally(r -> {
            System.out.println(r.toString());
            return null;
        });

        latch.await();

        String name = neo.value(TABLE_NAME, "name", NeoMap.of("group", "async"));
        Assert.assertEquals("name_async", name);
    }
}
