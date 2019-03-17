package com.simon.neo.neotable;

import com.simon.neo.BaseTest;
import com.simon.neo.Neo;
import com.simon.neo.NeoTable;
import java.sql.SQLException;
import org.junit.BeforeClass;

/**
 * @author zhouzhenyong
 * @since 2019/3/18 上午12:23
 */
public class BaseNeoTableTest extends BaseTest {

    public  static final String URL = "jdbc:mysql://118.31.38.50:3306/tina?useUnicode=true&characterEncoding=UTF-8&useSSL=false";
    public static final String USER = "like";
    public static final String PASSWORD = "Like@123";
    static Neo neo;
    static NeoTable tinaTest;

    public BaseNeoTableTest() throws SQLException {}

    @BeforeClass
    public static void start(){
        neo = Neo.connect(URL, USER, PASSWORD);
        tinaTest = neo.asTable("tina_test");
    }
}
