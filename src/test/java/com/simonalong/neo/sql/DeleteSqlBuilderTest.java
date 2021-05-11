package com.simonalong.neo.sql;

import com.simonalong.neo.BaseTest;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.sql.builder.DeleteSqlBuilder;
import com.simonalong.neo.sql.builder.SqlBuilder;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author shizi
 * @since 2020/3/22 下午8:10
 */
public class DeleteSqlBuilderTest extends BaseTest {

    @Test
    public void testBuild() {
        String sql = "delete from table1 where `name` = ? and `age` = ?";
        String result = DeleteSqlBuilder.build(null, "table1", NeoMap.of("name", "nana", "age", 12));

        Assert.assertEquals(sql, result);
    }
}
