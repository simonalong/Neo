package com.simonalong.neo.sql;

import com.simonalong.neo.BaseTest;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.sql.builder.InsertSqlBuilder;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author shizi
 * @since 2020/3/22 下午8:06
 */
public class InsertSqlBuilderTest extends BaseTest {

    @Test
    public void testBuild1() {
        String sql = "insert into table1 (`name`, `age`) values (?, ?)";
        String result = InsertSqlBuilder.build(null, "table1", NeoMap.of("name", "nana", "age", 12));

        Assert.assertEquals(sql, result);
    }

    @Test
    public void testBuild2() {
        String sql = "insert into table1 (`name`, `age`) select ?, ? where not exists (select * from table1 where `name` = ?)";
        String result = InsertSqlBuilder.buildInsertOfUnExist(null, "table1", NeoMap.of("name", "nana", "age", 12), NeoMap.of("name", "nana"));

        Assert.assertEquals(sql, result);
    }

}
