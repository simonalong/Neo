package com.simonalong.neo.sql;

import com.simonalong.neo.BaseTest;
import com.simonalong.neo.NeoMap;
import com.simonalong.neo.sql.builder.UpdateSqlBuilder;
import org.junit.Test;

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
}
