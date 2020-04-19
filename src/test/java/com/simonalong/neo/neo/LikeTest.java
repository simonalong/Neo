package com.simonalong.neo.neo;

import com.simonalong.neo.BaseTest;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.sql.builder.SqlBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author shizi
 * @since 2020/4/19 11:17 AM
 */
public class LikeTest extends BaseTest {

    @Test
    public void testBuildSql1() {
        NeoMap searchMap = NeoMap.of();
        searchMap.put("name", "nihao");
        searchMap.put("group", "like #ok#");

        // searchMap.put("group", "like ok"); 效果同 like #ok#

        String expect = " where `group` like '%ok%' and `name` =  ?";
        Assert.assertEquals(expect, SqlBuilder.buildWhere(searchMap));
    }

    @Test
    public void testBuildSql2() {
        NeoMap searchMap = NeoMap.of();
        searchMap.put("name", "nihao");
        searchMap.put("group", "like ok#");

        String expect = " where `group` like 'ok%' and `name` =  ?";
        Assert.assertEquals(expect, SqlBuilder.buildWhere(searchMap));
    }

    @Test
    public void testBuildSql3() {
        NeoMap searchMap = NeoMap.of();
        searchMap.put("name", "nihao");
        searchMap.put("group", "like #ok");

        String expect = " where `group` like '%ok' and `name` =  ?";
        Assert.assertEquals(expect, SqlBuilder.buildWhere(searchMap));
    }

    /**
     * 如果没有添加前后缀，则直接默认添加前后缀
     */
    @Test
    public void testBuildSql4() {
        NeoMap searchMap = NeoMap.of();
        searchMap.put("name", "nihao");
        searchMap.put("group", "like ok");

        String expect = " where `group` like '%ok%' and `name` =  ?";
        Assert.assertEquals(expect, SqlBuilder.buildWhere(searchMap));
    }

    @Test
    public void testBuildValue1() {
        NeoMap searchMap = NeoMap.of();
        searchMap.put("name", "nihao");
        searchMap.put("group", "like #ok#");

        List<String> valueList = Collections.singletonList("nihao");
        Assert.assertEquals(valueList, SqlBuilder.buildValueList(searchMap));
    }
}
