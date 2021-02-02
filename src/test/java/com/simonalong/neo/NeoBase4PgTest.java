package com.simonalong.neo;

import org.junit.BeforeClass;

/**
 * @author shizi
 * @since 2020/8/17 5:22 PM
 */
public class NeoBase4PgTest extends BaseTest {

    protected static final String URL = "jdbc:postgresql://localhost:54321/db";
    protected static final String USER = "postgres";
    protected static final String PASSWORD = "pg.123";

    public static final String TABLE_NAME = "demo_pg";

    public static Neo neo;

    static {
        System.setProperty("LOG_LEVEL", "debug");
    }

    @BeforeClass
    public static void start(){
        neo = Neo.connect(URL, USER, PASSWORD).initDb("db");
    }
}
