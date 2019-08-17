package com.simonalong.neo.table;

import com.simonalong.neo.BaseTest;
import com.simonalong.neo.Neo;
import com.simonalong.neo.NeoTable;
import java.sql.SQLException;
import org.junit.BeforeClass;

/**
 * @author zhouzhenyong
 * @since 2019/3/18 上午12:23
 */
public class BaseNeoTableTest extends BaseTest {

    public  static final String URL = "jdbc:mysql://127.0.0.1:3306/neo?useUnicode=true&characterEncoding=UTF-8&useSSL=false";
    public static final String USER = "neo_test";
    public static final String PASSWORD = "neo@Test123";

    static Neo neo;
    static NeoTable tinaTest;

    public BaseNeoTableTest() throws SQLException {}

    @BeforeClass
    public static void start(){
        neo = Neo.connect(URL, USER, PASSWORD);
        tinaTest = neo.asTable("neo_table2");
    }
}
