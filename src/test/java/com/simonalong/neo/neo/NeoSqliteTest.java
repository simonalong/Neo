package com.simonalong.neo.neo;

import com.simonalong.neo.BaseTest;
import com.simonalong.neo.Neo;
import com.simonalong.neo.NeoMap;
import org.junit.*;

/**
 * @author zhouzhenyong
 * @since 2019/11/22 上午12:07
 */
public class NeoSqliteTest extends BaseTest {

    private static Neo neo = Neo.connect("jdbc:sqlite:/Users/zhouzhenyong/work/db/sqlite/neo.db", null, null);

    @Test
    public void test1() {
        NeoMap dataMap = NeoMap.of("code", "code_sqlite");
        neo.insert("neo", dataMap);

        NeoMap resultMap = neo.one("neo", NeoMap.of("code", "code_sqlite"));

        Assert.assertEquals(dataMap, resultMap);
    }
}
