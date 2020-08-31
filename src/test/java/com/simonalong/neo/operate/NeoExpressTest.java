package com.simonalong.neo.operate;

import com.simonalong.neo.NeoBaseTest;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.express.Express;
import org.junit.Test;

/**
 * @author shizi
 * @since 2020/8/31 10:52 上午
 */
public class NeoExpressTest extends NeoBaseTest {

    @Test
    public void oneTest(){
        Express searchExpress;

        // select * from neo_table1 where (name=?)    value: name12
//        searchExpress= new Express().and("name", "name12");
//        show(neo.one("neo_table1", searchExpress));

        // select * from neo_table1 where (name=?)    value: name12
        searchExpress = new Express().and("name", null);
        show(neo.one("neo_table1", searchExpress));

//        searchExpress = new Express().and("name", "");
//        show(neo.one("neo_table1", searchExpress));
    }


}
