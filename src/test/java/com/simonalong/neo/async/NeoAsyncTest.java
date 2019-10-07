package com.simonalong.neo.async;

import com.simonalong.neo.NeoMap;
import com.simonalong.neo.NeoBaseTest;
import java.sql.SQLException;
import org.junit.Test;

/**
 * @author zhouzhenyong
 * @since 2019/8/18 下午10:27
 */
public class NeoAsyncTest extends NeoBaseTest {

    public NeoAsyncTest() throws SQLException {}

    @Test
    public void testInsertAsync1(){
        neo.insertAsync(TABLE_NAME, NeoMap.of("group", "async", "name", "name_async"))
            .thenApply(r->r)
            .exceptionally(r->{
                System.out.println(r.toString());
                return null;
        });
    }
}
