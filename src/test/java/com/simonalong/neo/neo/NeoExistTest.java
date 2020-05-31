package com.simonalong.neo.neo;

import com.simonalong.neo.NeoBaseTest;
import com.simonalong.neo.NeoMap;
import org.junit.Test;

/**
 * @author shizi
 * @since 2020/4/29 6:01 PM
 */
public class NeoExistTest extends NeoBaseTest {

    @Test
    public void existOne() {
        show(neo.exist("neo_table1", NeoMap.of("group", "group1")));
    }
}
