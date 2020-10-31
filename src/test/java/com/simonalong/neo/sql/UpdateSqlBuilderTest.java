package com.simonalong.neo.sql;

import com.simonalong.neo.BaseTest;
import com.simonalong.neo.Columns;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.sql.builder.SqlBuilder;
import com.simonalong.neo.sql.builder.UpdateSqlBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author shizi
 * @since 2020/3/22 下午8:14
 */
public class UpdateSqlBuilderTest extends BaseTest {

    @Test
    public void testBuild() {
        String sql = "update table1 set `address` = ?, `age` = ? where `name` = ? and `id` = ?";
        String result = UpdateSqlBuilder.build("table1", NeoMap.of("age", 12, "address", "河南"), NeoMap.of("id", 123, "name", "nana"));

        Assert.assertEquals(sql, result);
    }

    /**
     * update table1 a join(
     * select ? as `user_name`, ? as `name`, ? as `group`
     * union
     * select ? as `name`, ? as `group`
     * union
     * select ? as `user_name`, ? as `name`, ? as `group`
     * ) b using(`name`)
     * set a.`user_name`=b.`user_name`, a.`group`=b.`group`
     */
    @Test
    public void testBuild1() {
        List<NeoMap> dataList = Arrays.asList(
            NeoMap.of("group", "group3", "name", "name3chg", "user_name", "user_name3"),
            NeoMap.of("group", "group4", "name", "name4chg"),
            NeoMap.of("group", "group5", "name", "name5chg", "user_name", "user_name5")
        );
        String sql = "update table1 a join( select ? as `user_name`, ? as `name`, ? as `group` union  select ? as `user_name`, ? as `name`, ? as `group` union  select ? as `user_name`, ? as `name`, ? as `group`) b using(`name`) set a.`user_name`=b.`user_name`, a.`group`=b.`group`";
        String result = UpdateSqlBuilder.buildBatch("table1", dataList, Columns.of("name"));

        Assert.assertEquals(sql, result);

        List<Object> expectList = Arrays.asList("user_name3", "name3chg", "group3", null, "name4chg", "group4", "user_name5", "name5chg", "group5");
        List<Object> resultList = SqlBuilder.buildBatchValueList(dataList);

        Assert.assertEquals(expectList, resultList);
    }
}
