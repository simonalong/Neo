package com.simonalong.neo.neo;

import com.simonalong.neo.BaseTest;
import com.simonalong.neo.Neo;
import com.simonalong.neo.NeoMap;
import org.junit.Test;

/**
 * @author zhouzhenyong
 * @since 2019/11/22 上午12:07
 */
public class NeoSqliteTest extends BaseTest {

    @Test
    public void test1(){
        Neo neo = Neo.connect("jdbc:sqlite:/Users/zhouzhenyong/work/db/sqlite/neo.db", null, null);
        neo.insert("neo", NeoMap.of("id", 2, "code", "haode"));
        show(neo.list("neo", NeoMap.of()));
    }
}
