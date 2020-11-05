package com.simonalong.neo.biz;

import com.simonalong.neo.Neo;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.core.AbstractBizService;
import com.simonalong.neo.entity.TestEntity;
import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * @author zhouzhenyong
 * @since 2019-08-17 13:24
 */
public class BizServiceTest extends AbstractBizService {

    public BizServiceTest() {
    }

    @Override
    public Neo getDb() {
        String url = "jdbc:mysql://127.0.0.1:3306/neo?useUnicode=true&characterEncoding=UTF-8&useSSL=false&allowPublicKeyRetrieval=true";
        String user = "neo_test";
        String password = "neo@Test123";
        return Neo.connect(url, user, password);
    }

    @Override
    public String getTableName() {
        return "neo_table1";
    }

    @Before
    public void beforeTest() {
        getDb().truncateTable(getTableName());
    }

    @Test
    public void testInsert() {
        TestEntity entity = new TestEntity().setGroup("ok").setUserName("me").setName("hello");
        System.out.println(insert(entity));
    }


    @Test
    @SneakyThrows
    public void testInsertAsync() {
        CountDownLatch latch = new CountDownLatch(1);
        insertAsync(NeoMap.of("group", "biz_async_group", "name", "biz_async_name")).thenAccept(r -> latch.countDown());

        latch.await();

        String name = value("name", NeoMap.of("group", "biz_async_group"));
        Assert.assertEquals("biz_async_name", name);
    }
}
