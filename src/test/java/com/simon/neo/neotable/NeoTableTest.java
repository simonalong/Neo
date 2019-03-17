package com.simon.neo.neotable;

import com.simon.neo.BaseTest;
import com.simon.neo.Neo;
import com.simon.neo.NeoMap;
import com.simon.neo.NeoTable;
import lombok.ToString.Exclude;
import org.junit.Test;

/**
 * @author zhouzhenyong
 * @since 2019/3/17 下午9:11
 */
public class NeoTableTest extends BaseTest {

    public  static final String URL = "jdbc:mysql://118.31.38.50:3306/tina?useUnicode=true&characterEncoding=UTF-8&useSSL=false";
    public static final String USER = "like";
    public static final String PASSWORD = "Like@123";

    Neo neo = Neo.connect(URL, USER, PASSWORD);
    @Test
    public void testInsert(){
        NeoTable tinaTest = neo.asTable("tina_test");
        show(tinaTest.insert(NeoMap.of("group", "table1")));
    }
}
