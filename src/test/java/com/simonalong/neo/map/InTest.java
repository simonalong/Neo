package com.simonalong.neo.map;

import com.simonalong.neo.BaseTest;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.sql.InList;
import com.simonalong.neo.sql.builder.SqlBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author shizi
 * @since 2020/4/19 3:59 PM
 */
public class InTest extends BaseTest {

    @Test
    public void test1() {
        NeoMap searchMap = NeoMap.of();
        searchMap.put("name", "nihao");
        searchMap.put("group_id", new InList(Arrays.asList(12, 21)));

        String expectSql = "where `group_id` in ('12','21') and `name` =  ?";
        Assert.assertEquals(expectSql, SqlBuilder.buildWhere(searchMap));

        List<String> dataList = Collections.singletonList("nihao");
        Assert.assertEquals(dataList, SqlBuilder.buildValueList(searchMap));
    }
}
