package com.simonalong.neo.biz;

import com.simonalong.neo.Neo;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.core.AbstractBizService;
import com.simonalong.neo.entity.TestEntity;
import java.sql.SQLException;
import lombok.SneakyThrows;
import org.junit.Test;

/**
 * @author zhouzhenyong
 * @since 2019-08-17 13:24
 */
public class BizServiceTest extends AbstractBizService {

    public BizServiceTest() throws SQLException {
    }

    @Override
    public Neo getDb() {
        String url = "jdbc:mysql://127.0.0.1:3306/neo?useUnicode=true&characterEncoding=UTF-8&useSSL=false";
        String user = "neo_test";
        String password = "neo@Test123";
        return Neo.connect(url, user, password);
    }

    @Override
    public String getTableName() {
        return "neo_table1";
    }

    @Test
    public void testInsert() {
        TestEntity entity = new TestEntity()
            .setGroup("ok")
            .setUserName("me")
            .setName("hello");
        System.out.println(insert(entity));
    }

    @Test
    @SneakyThrows
    public void testInsertAsync(){
        insertAsync(NeoMap.of("group", "ok")).thenAccept(r->{
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName());
            System.out.println(r);
        });

        Thread.sleep(10000);
    }
}
