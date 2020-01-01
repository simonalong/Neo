package com.simonalong.neo;

import com.simonalong.neo.BaseTest;
import com.simonalong.neo.Neo;
import java.sql.SQLException;
import org.junit.BeforeClass;

/**
 * @author zhouzhenyong
 * @since 2019/3/15 下午6:34
 */
public class NeoBaseTest extends BaseTest {

    protected static final String URL = "jdbc:mysql://127.0.0.1:3306/neo?useUnicode=true&characterEncoding=UTF-8&useSSL=false&&allowPublicKeyRetrieval=true";
    protected static final String USER = "root";
    protected static final String PASSWORD = "Root@123";

    public static final String TABLE_NAME = "neo_table1";

    public static Neo neo;

    public NeoBaseTest() throws SQLException {}

    static {
        System.setProperty("LOG_LEVEL", "debug");
    }

    @BeforeClass
    public static void start(){
        neo = Neo.connect(URL, USER, PASSWORD).initDb("neo", "xx", "uuid");
    }
}
