package com.simonalong.neo.biz;

import com.simonalong.neo.Neo;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.core.AbstractBizService;
import com.simonalong.neo.core.DbSync;
import java.sql.SQLException;
import lombok.SneakyThrows;
import org.junit.Test;

/**
 * @author zhouzhenyong
 * @since 2019-08-17 13:24
 */
public class BigTest extends AbstractBizService {

    public BigTest() throws SQLException {}

    @Override
    public DbSync getDb() {
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
    public void testInsert(){
        System.out.println(insert(NeoMap.of("group", "ok")));
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
