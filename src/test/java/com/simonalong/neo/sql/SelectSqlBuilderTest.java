package com.simonalong.neo.sql;

import com.simonalong.neo.*;
import com.simonalong.neo.sql.builder.SelectSqlBuilder;
import org.junit.Test;

import java.sql.SQLException;

/**
 * @author shizi
 * @since 2020/3/22 下午8:35
 */
public class SelectSqlBuilderTest extends NeoBaseTest {

    public SelectSqlBuilderTest() throws SQLException {
    }

    @Test
    public void testBuildOne() {
        // select `group`, `user_name` from  where `id` =  ? and `name` =  ? limit 1
        show(SelectSqlBuilder.buildOne(neo, "neo_table1", Columns.of("group", "user_name"), NeoMap.of("id", 31, "name", "kk")));
    }

    @Test
    public void testBuildList() {
        // select `group`, `user_name` from  where `id` =  ? and `name` =  ?
        show(SelectSqlBuilder.buildList(neo, "neo_table1", Columns.of("group", "user_name"), NeoMap.of("id", 31, "name", "kk")));
    }

    @Test
    public void testBuildValue() {
        // select `group` from neo_table1 where `id` =  ? and `name` =  ? limit 1
        show(SelectSqlBuilder.buildValue("neo_table1", "group", NeoMap.of("id", 31, "name", "kk")));
    }

    @Test
    public void testBuildValues() {
        // select `group` from neo_table1 where `id` =  ? and `name` =  ?
        show(SelectSqlBuilder.buildValues("neo_table1", "group", NeoMap.of("id", 31, "name", "kk")));
    }

    @Test
    public void testBuildPage() {
        // select `group` from  where `id` =  ? and `name` =  ? limit 3, 20
        show(SelectSqlBuilder.buildPage(neo, "neo_table1", Columns.of("group"), NeoMap.of("id", 31, "name", "kk"), 3, 20));
    }

    @Test
    public void testBuildCount() {
        // select count(1) from neo_table1 where `id` =  ? and `name` =  ? limit 1
        show(SelectSqlBuilder.buildCount("neo_table1", NeoMap.of("id", 31, "name", "kk")));
    }

    @Test
    public void testBuildColumns() {
        // `group`, `name`
        show(SelectSqlBuilder.buildColumns(neo, "neo_table1", Columns.of("group", "name")));
    }


}

