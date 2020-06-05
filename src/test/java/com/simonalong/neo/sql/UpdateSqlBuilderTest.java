package com.simonalong.neo.sql;

import com.simonalong.neo.BaseTest;
import com.simonalong.neo.Columns;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.sql.builder.SqlBuilder;
import com.simonalong.neo.sql.builder.UpdateSqlBuilder;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author shizi
 * @since 2020/3/22 下午8:14
 */
public class UpdateSqlBuilderTest extends BaseTest {

    @Test
    public void testBuild(){
        // update table1 set `address`=?, `age`=? where `id` =  ? and `name` =  ?
        show(UpdateSqlBuilder.build("table1", NeoMap.of("age", 12, "address", "河南"), NeoMap.of("id", 123, "name", "nana")));
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
        show(UpdateSqlBuilder.buildBatch("table1", dataList, Columns.of("name")));

        show(SqlBuilder.buildBatchValueList(dataList));
    }
}
