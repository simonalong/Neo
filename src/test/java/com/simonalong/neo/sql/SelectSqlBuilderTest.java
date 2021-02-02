package com.simonalong.neo.sql;

import com.simonalong.neo.*;
import com.simonalong.neo.sql.builder.SelectSqlBuilder;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author shizi
 * @since 2020/3/22 下午8:35
 */
public class SelectSqlBuilderTest extends NeoBaseTest {

    public SelectSqlBuilderTest()  {
    }

    @Test
    public void testBuildOne() {
        String sql = "select `group`, `user_name` from neo_table1 where `name` = ? and `id` = ? limit 1";
        String result = SelectSqlBuilder.buildOne(neo, "neo_table1", Columns.of("group", "user_name"), NeoMap.of("id", 31, "name", "kk"));

        Assert.assertEquals(sql, result);
    }

    @Test
    public void testBuildList() {
        String sql = "select `group`, `user_name` from neo_table1 where `name` = ? and `id` = ?";
        String result = SelectSqlBuilder.buildList(neo, "neo_table1", Columns.of("group", "user_name"), NeoMap.of("id", 31, "name", "kk"));

        Assert.assertEquals(sql, result);
    }

    @Test
    public void testBuildValue() {
        String sql = "select `group` from neo_table1 where `name` = ? and `id` = ? limit 1";
        String result = SelectSqlBuilder.buildValue("neo_table1", "group", NeoMap.of("id", 31, "name", "kk"));

        Assert.assertEquals(sql, result);
    }

    @Test
    public void testBuildValues() {
        String sql = "select `group` from neo_table1 where `name` = ? and `id` = ?";
        String result = SelectSqlBuilder.buildValues("neo_table1", false, "group", NeoMap.of("id", 31, "name", "kk"));

        Assert.assertEquals(sql, result);
    }

    @Test
    public void testBuildPage() {
        String sql = "select `group` from neo_table1 where `name` = ? and `id` = ? limit 20 offset 3";
        String result = SelectSqlBuilder.buildPage(neo, "neo_table1", Columns.of("group"), NeoMap.of("id", 31, "name", "kk"), 3, 20);

        Assert.assertEquals(sql, result);
    }

    @Test
    public void testBuildCount() {
        String sql = "select count(1) from neo_table1 where `name` = ? and `id` = ?";
        String result = SelectSqlBuilder.buildCount("neo_table1", NeoMap.of("id", 31, "name", "kk"));

        Assert.assertEquals(sql, result);
    }

    @Test
    public void testBuildColumns() {
        String sql = "`group`, `name`";
        String result = SelectSqlBuilder.buildColumns(neo, "neo_table1", Columns.of("group", "name"));

        Assert.assertEquals(sql, result);
    }
}

