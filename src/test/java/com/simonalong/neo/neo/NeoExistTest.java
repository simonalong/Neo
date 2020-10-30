package com.simonalong.neo.neo;

import com.simonalong.neo.NeoBaseTest;
import com.simonalong.neo.NeoMap;
import org.junit.*;

/**
 * @author shizi
 * @since 2020/4/29 6:01 PM
 */
public class NeoExistTest extends NeoBaseTest {

    @BeforeClass
    public static void beforeClass() {
        neo.truncateTable(TABLE_NAME);
    }

    @Before
    public void beforeTest() {
        neo.truncateTable(TABLE_NAME);
    }

    @AfterClass
    public static void afterClass() {
        neo.truncateTable(TABLE_NAME);
    }

    @Test
    public void existOne() {
        NeoMap dataMap = NeoMap.of("group", "group_exist");
        neo.insert(TABLE_NAME, dataMap);

        Assert.assertTrue(neo.exist(TABLE_NAME, NeoMap.of("group", "group_exist")));
    }
}
