package com.simonalong.neo.neo;

import com.simonalong.neo.Neo;
import com.simonalong.neo.NeoBaseTest;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.exception.NeoException;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author zhouzhenyong
 * @since 2019/10/1 下午5:08
 */
public class NeoDbTest extends NeoBaseTest {

    public NeoDbTest()  {
    }

    @Test
    public void testDb1() {
        Neo second = Neo.connect(URL, USER, PASSWORD).initDb();

        NeoMap dataMap = NeoMap.of("group", "group_count", "name", "name_count");
        second.insert("neo_table1", dataMap);

        NeoMap expectMap = NeoMap.of("group", "group_count", "name", "name_count");
        NeoMap resultMap = second.one("neo_table1", dataMap);
        Assert.assertEquals(expectMap, resultMap.assignExcept("id"));
    }

    /**
     * 若初始化没有初始化表，则默认初始化所有的表
     */
    @Test
    public void testDb2() {
        Neo second = Neo.connect(URL, USER, PASSWORD);

        NeoMap dataMap = NeoMap.of("group", "group_count", "name", "name_count");
        second.insert("neo_table1", dataMap);

        NeoMap expectMap = NeoMap.of("group", "group_count", "name", "name_count");
        NeoMap resultMap = second.one("neo_table1", dataMap);
        Assert.assertEquals(expectMap, resultMap.assignExcept("id"));
    }

    /**
     * 若初始化没有初始化表，则后面也会可以初始化
     */
    @Test
    public void testDb3() {
        Neo second = Neo.connect(URL, USER, PASSWORD).initDb("neo_table2");

        NeoMap dataMap = NeoMap.of("group", "group_count", "name", "name_count");
        second.insert("neo_table1", dataMap);

        NeoMap expectMap = NeoMap.of("group", "group_count", "name", "name_count");
        NeoMap resultMap = second.one("neo_table1", dataMap);
        Assert.assertEquals(expectMap, resultMap.assignExcept("id"));
    }

    /**
     * 不存在的表，则上报异常
     */
    @Test(expected = NeoException.class)
    public void testDb4() {
        Neo second = Neo.connect(URL, USER, PASSWORD).initDb("neo_tablexxx");

        NeoMap dataMap = NeoMap.of("group", "group_count", "name", "name_count");
        second.insert("neo_tablexxx", dataMap);
    }
}
