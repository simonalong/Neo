package com.simonalong.neo.postgresql;

import com.simonalong.neo.Neo;
import com.simonalong.neo.NeoBase4PgTest;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.db.NeoPage;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author shizi
 * @since 2020/8/17 5:22 PM
 */
public class NeoTest4Pg extends NeoBase4PgTest {

    private static final String innerTableName = "demo_char";

    /**
     * 建表
     */
    @Test
    public void testCreateTable() {
        String sql = "create table if not exists %s (char char not null)";
        neo.execute(sql, innerTableName);
    }

    @Test
    public void testDropTable() {
        String sql = "drop table %s";
        neo.execute(sql, innerTableName);
    }

    @Test
    public void testInsertData1() {
        show(neo.insert(innerTableName, NeoMap.of("char", "t")));
    }

    @Test
    public void testOne(){
        show(neo.insert(innerTableName, NeoMap.of("char", "t")));
        show(neo.one(innerTableName, NeoMap.of()));
    }

    @Test
    public void testList(){
        show(neo.insert(innerTableName, NeoMap.of("char", "2")));
        show(neo.list(innerTableName, NeoMap.of("char", "2")));
    }

    @Test
    public void testValue() {
        String tableName = "demo_char2";
        String sql = "create table if not exists %s (char char not null, char2 char(2) not null)";
        neo.execute(sql, tableName);

        show(neo.insert(tableName, NeoMap.of("char", "2", "char2", "ab")));
        show(neo.value(String.class, tableName, "char2", NeoMap.of("char2", "ab")));
    }

    @Test
    public void testValues() {
        String tableName = "demo_char2";
        String sql = "create table if not exists %s (char char not null, char2 char(2) not null)";
        neo.execute(sql, tableName);

        show(neo.insert(tableName, NeoMap.of("char", "2", "char2", "ab")));
        show(neo.values(String.class, tableName, "char2", NeoMap.of("char2", "ab")));
    }

    @Test
    public void testPage() {
        String tableName = "demo_char2";
        String sql = "create table if not exists %s (char char not null, char2 char(2) not null)";
        neo.execute(sql, tableName);

        show(neo.page(tableName, NeoPage.of(1, 2)));
    }

    @Test
    public void testCount() {
        String tableName = "demo_char2";
        String sql = "create table if not exists %s (char char not null, char2 char(2) not null)";
        neo.execute(sql, tableName);

        show(neo.count(tableName, NeoPage.of(1, 2)));
    }

    @Test
    public void testExit() {
        String tableName = "demo_char2";
        String sql = "create table if not exists %s (char char not null, char2 char(2) not null)";
        neo.execute(sql, tableName);
    }
}
