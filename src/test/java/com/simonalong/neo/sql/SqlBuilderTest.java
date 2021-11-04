package com.simonalong.neo.sql;

import com.simonalong.neo.NeoMap;
import com.simonalong.neo.NeoBaseTest;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.simonalong.neo.sql.builder.SqlBuilder;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author zhouzhenyong
 * @since 2019/3/29 下午11:28
 */
public class SqlBuilderTest extends NeoBaseTest {

    public SqlBuilderTest() {}

    @Test
    public void inTest() {
        List<Integer> dataList = new ArrayList<>();
        dataList.add(1);
        dataList.add(2);
        dataList.add(3);
        dataList.add(4);

        String sql = "('1', '2', '3', '4')";
        String result = SqlBuilder.buildIn(dataList);

        Assert.assertEquals(sql, result);
    }

    @Test
    public void buildWhereTest1() {
        NeoMap searchMap = NeoMap.ofSort("group", "group1", "name", "name1");
        String sql = " where `group` = ? and `name` = ?";
        String result = SqlBuilder.buildWhere(searchMap);

        Assert.assertEquals(sql, result);
    }

    @Test
    public void buildSortWhereTest2() {
        NeoMap searchMap = NeoMap.of();
        searchMap.openSorted();
        searchMap.put("group", "group1");
        searchMap.put("name", "name1");

        String sql = " where `group` = ? and `name` = ?";
        String result = SqlBuilder.buildWhere(searchMap);

        Assert.assertEquals(sql, result);
    }

    @Test
    public void buildWhereConditionTest1() {
        NeoMap searchMap = NeoMap.of("group", "group1", "name", "name1");
        String sql = "`name` = ? and `group` = ?";
        String result = SqlBuilder.buildWhereCondition(searchMap);

        Assert.assertEquals(sql, result);
    }

    @Test
    public void buildWhereTest2() {
        NeoMap searchMap = NeoMap.of();
        String sql = "";
        String result = SqlBuilder.buildWhere(searchMap);

        Assert.assertEquals(sql, result);
    }

    @Test
    public void buildConditionTest1() {
        NeoMap searchMap = NeoMap.of("group", "group1", "name", "name1");
        String sql = "`name` = ? and `group` = ?";
        String result = SqlBuilder.buildWhereCondition(searchMap);

        Assert.assertEquals(sql, result);
    }

    @Test
    public void buildConditionTest2() {
        NeoMap searchMap = NeoMap.of("group", "group1", "name", "name1");
        String sql = "`name` = ? and `group` = ?";
        String result = SqlBuilder.buildWhereCondition(searchMap);

        Assert.assertEquals(sql, result);
    }

    @Test
    public void buildValueListTest1() {
        NeoMap neoMap = NeoMap.of("name", "haode", "group", "ok");
        List<String> dataList = Arrays.asList("haode", "ok");
        List<Object> resultList = SqlBuilder.buildValueList(neoMap);

        Assert.assertEquals(dataList, resultList);
    }

    @Test
    public void buildConditionMetaTest1() {
        NeoMap neoMap = NeoMap.of("name", "haode", "group", "ok");
        List<String> dataList = Arrays.asList("`name` = ?", "`group` = ?");
        List<String> resultList = SqlBuilder.buildConditionMeta(neoMap);

        Assert.assertEquals(dataList, resultList);
    }
}
