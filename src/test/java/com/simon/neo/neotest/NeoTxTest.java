package com.simon.neo.neotest;

import com.simon.neo.BaseTest;
import com.simon.neo.NeoMap;
import com.simon.neo.neotable.BaseNeoTableTest;
import java.sql.SQLException;
import org.junit.Test;

/**
 * @author zhouzhenyong
 * @since 2019/3/26 上午8:35
 */
public class NeoTxTest extends NeoBaseTest {

    public NeoTxTest() throws SQLException {}

    @Test
    public void test1(){
        neo.tx(()->{
            neo.update(TABLE_NAME, NeoMap.of("group", "group1"));
        });
    }
}
